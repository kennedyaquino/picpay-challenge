package com.kennedy.picpay.config;

import com.kennedy.picpay.entities.WalletType;
import com.kennedy.picpay.repositories.WalletTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class Dataloader implements CommandLineRunner {

    private final WalletTypeRepository walletTypeRepository;

    public Dataloader(WalletTypeRepository walletTypeRepository) {
        this.walletTypeRepository = walletTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(WalletType.Enum.values())
                .forEach(walletType -> walletTypeRepository.save(walletType.get()));
    }
}
