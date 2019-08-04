package com.vova.purchaseservice.ex;

public class PurchaseNotFoundException extends RuntimeException {
    private final int idPurchase;
    private final String login;

    public PurchaseNotFoundException(int idPurchase, String login) {
        this.idPurchase = idPurchase;
        this.login = login;
    }


}
