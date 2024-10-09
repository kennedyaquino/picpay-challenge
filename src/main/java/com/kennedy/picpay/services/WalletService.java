package com.kennedy.picpay.services;

import com.kennedy.picpay.controllers.dto.CreateWalletDto;
import com.kennedy.picpay.entities.Wallet;
import com.kennedy.picpay.exception.WalletDataAlreadyExistsException;
import com.kennedy.picpay.repositories.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfCnpjOrEmail(dto.cpfCnpj(), dto.email());
        if(walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCnpj or Email already exists");
        }

        return walletRepository.save(dto.toWallet());
    }
}
