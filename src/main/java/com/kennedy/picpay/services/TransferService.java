package com.kennedy.picpay.services;

import com.kennedy.picpay.controllers.dto.TransferDto;
import com.kennedy.picpay.entities.Transfer;
import com.kennedy.picpay.entities.Wallet;
import com.kennedy.picpay.exception.InsulfficientBalanceException;
import com.kennedy.picpay.exception.TransferNotAllowedWalletTypeException;
import com.kennedy.picpay.exception.TransferNotAuthorizedException;
import com.kennedy.picpay.exception.WalletNotFoundException;
import com.kennedy.picpay.repositories.TransferRepository;
import com.kennedy.picpay.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {

    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(AuthorizationService authorizationService, NotificationService notificationService, TransferRepository transferRepository, WalletRepository walletRepository) {
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Transfer transfer(TransferDto transferDto) {
        var sender = walletRepository.findById(transferDto.payer()).orElseThrow(() -> new WalletNotFoundException(transferDto.payer()));
        var receiver = walletRepository.findById(transferDto.payee()).orElseThrow(() -> new WalletNotFoundException(transferDto.payee()));

        validateTransfer(transferDto, sender);

        sender.debit(transferDto.value());
        receiver.credit(transferDto.value());

        var transfer = new Transfer(sender, receiver, transferDto.value());
        walletRepository.saveAll(Arrays.asList(sender, receiver));
        transferRepository.save(transfer);

        var transferResult = transferRepository.save(transfer);

        CompletableFuture.runAsync(() -> notificationService.setNotification(transferResult));

        return transferResult;
    }

    private void validateTransfer(TransferDto transferDto, Wallet sender) {

        if(!sender.isTransferAllowedForWalletType()) {
            throw new TransferNotAllowedWalletTypeException();
        }

        if(!sender.isBalanceEqualOrGreaterThan(transferDto.value())) {
            throw  new InsulfficientBalanceException();
        }

        if(!authorizationService.isAuthorized(transferDto)) {
            throw  new TransferNotAuthorizedException();
        }
    }
}
