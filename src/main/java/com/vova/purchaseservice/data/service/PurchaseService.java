package com.vova.purchaseservice.data.service;

import com.vova.purchaseservice.data.PurchaseSpecificationFactory;
import com.vova.purchaseservice.data.crud.PurchaseRepository;
import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.ex.PurchaseNotFoundException;
import com.vova.purchaseservice.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PurchaseService {

    private PurchaseRepository purchaseRepository;
    private UserRepository userRepository;

    public Page<Purchase> getPurchases(Pageable pageable, String login) {
        return purchaseRepository.getByUser_Login(login, pageable);
    }

    public Purchase create(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase edit(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase getPurchaseByIdAndLogin(int idPurchase, String login) {
        return purchaseRepository.getByUser_LoginAndIdPurchase(login, idPurchase)
                .orElseThrow(() -> new PurchaseNotFoundException(idPurchase, login));
    }

    public Page<Purchase> getPurchases(Pageable pageable, String login, Map<String, String> params) {
        User byLogin = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        return purchaseRepository.findAll(PurchaseSpecificationFactory.createFromGetQuery(params, byLogin), pageable);
    }

    public void deletePurchase(int idPurchase, String login) {
        Purchase purchase = purchaseRepository.getByUser_LoginAndIdPurchase(login, idPurchase)
                .orElseThrow(() -> new PurchaseNotFoundException(idPurchase, login));
        purchaseRepository.delete(purchase);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPurchaseRepository(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }
}
