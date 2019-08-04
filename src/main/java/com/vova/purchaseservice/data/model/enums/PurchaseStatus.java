package com.vova.purchaseservice.data.model.enums;

public enum PurchaseStatus {
    NEW(0), DONE(1), REJECTED(2);
    private final int code;

    PurchaseStatus(int code) {
        this.code = code;
    }

    public static PurchaseStatus of(int code) {
        for (PurchaseStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Неизвестный идентификатор статуса расписания: " + code);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "PurchaseStatus{" +
                "code=" + code +
                '}';
    }
}
