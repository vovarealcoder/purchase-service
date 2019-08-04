package com.vova.purchaseservice.data.model.enums;

public enum ScheduleStatus {
    ENABLED(1), DISABLED(2), REJECTED(3);
    private final int code;

    ScheduleStatus(int code) {
        this.code = code;
    }

    public static ScheduleStatus of(int code) {
        for (ScheduleStatus value : values()) {
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
        return "ScheduleStatus{" +
                "code=" + code +
                '}';
    }
}
