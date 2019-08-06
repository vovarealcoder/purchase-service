package com.vova.purchaseservice.data.model.enums;

public enum Periodic {
    DAY(1), WEEK(2),
    MONTH(3), KVARTAL(4), YEAR(5);

    private final int code;

    Periodic(int code) {
        this.code = code;
    }

    public static Periodic of(int code) {
        for (Periodic value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Неизвестный идентификатор периодичности: " + code);
    }

    @Override
    public String toString() {
        return "Periodic{" +
                "code=" + code +
                '}';
    }

    public int getCode() {
        return code;
    }
}
