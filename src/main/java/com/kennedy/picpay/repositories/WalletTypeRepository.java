package com.kennedy.picpay.repositories;

import com.kennedy.picpay.entities.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTypeRepository extends JpaRepository<WalletType, Long> {
}
