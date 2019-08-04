package com.vova.purchaseservice.data.model.adapers;

import com.vova.purchaseservice.data.model.enums.PurchaseStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PurchaseStatusConverter implements AttributeConverter<PurchaseStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PurchaseStatus purchaseStatus) {
        if (purchaseStatus == null) {
            return null;
        }
        return purchaseStatus.getCode();
    }

    @Override
    public PurchaseStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return PurchaseStatus.of(code);
    }
}