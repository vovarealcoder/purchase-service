package com.vova.purchaseservice.data.crud;

import com.vova.purchaseservice.data.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>, JpaSpecificationExecutor<Purchase> {
    Page<Purchase> getByUser_Login(String login, Pageable pagable);

    Optional<Purchase> getByUser_LoginAndIdPurchase(String login, int idPurchase);
}
