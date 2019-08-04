package com.vova.purchaseservice.data.model.adapers;

import com.vova.purchaseservice.data.model.enums.Periodic;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PeriodicConverter implements AttributeConverter<Periodic, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Periodic periodic) {
        if (periodic == null) {
            return null;
        }
        return periodic.getCode();
    }

    @Override
    public Periodic convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return Periodic.of(code);
    }
}