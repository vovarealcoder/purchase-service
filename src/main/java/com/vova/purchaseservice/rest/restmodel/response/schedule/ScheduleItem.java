package com.vova.purchaseservice.rest.restmodel.response.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.enums.Periodic;
import com.vova.purchaseservice.data.model.enums.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleItem {
    private int idSchedule;
    private Date created;
    private String name;
    private String comment;
    private Integer planPrice;
    private Periodic period;
    private Date startDate;
    private ScheduleStatus status;
    private Integer count;
    private Date lastPurchase;

    public static ScheduleItem fromDbSchedule(Schedule schedule) {
        ScheduleItem scheduleResponse = new ScheduleItem();
        schedule.setIdSchedule(schedule.getIdSchedule());
        schedule.setCreated(schedule.getCreated());
        schedule.setName(schedule.getName());
        schedule.setComment(schedule.getComment());
        schedule.setPlanPrice(schedule.getPlanPrice());
        schedule.setPeriod(schedule.getPeriod());
        schedule.setStartDate(schedule.getStartDate());
        schedule.setStatus(schedule.getStatus());
        schedule.setLastPurchase(schedule.getLastPurchase());
        schedule.setCount(schedule.getCount());
        return scheduleResponse;
    }

    public static Page<ScheduleItem> convertPagable(Page<Schedule> schedules) {
        List<ScheduleItem> scheduleResponses = schedules.getContent()
                .stream()
                .map(ScheduleItem::fromDbSchedule)
                .collect(Collectors.toList());
        return new PageImpl<>(scheduleResponses);
    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(Integer planPrice) {
        this.planPrice = planPrice;
    }

    public Periodic getPeriod() {
        return period;
    }

    public void setPeriod(Periodic period) {
        this.period = period;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(Date lastPurchase) {
        this.lastPurchase = lastPurchase;
    }
}
