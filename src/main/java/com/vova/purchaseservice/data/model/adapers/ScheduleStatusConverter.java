package com.vova.purchaseservice.data.model.adapers;

import com.vova.purchaseservice.data.model.enums.ScheduleStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ScheduleStatusConverter implements AttributeConverter<ScheduleStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ScheduleStatus scheduleStatus) {
        if (scheduleStatus == null) {
            return null;
        }
        return scheduleStatus.getCode();
    }

    @Override
    public ScheduleStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return ScheduleStatus.of(code);
    }
}