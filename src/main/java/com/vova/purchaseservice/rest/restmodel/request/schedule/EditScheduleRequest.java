package com.vova.purchaseservice.rest.restmodel.request.schedule;

import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.enums.Periodic;
import com.vova.purchaseservice.data.model.enums.ScheduleStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Optional;

public class EditScheduleRequest {

    private Periodic period;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startDate;
    private ScheduleStatus status;
    @Size(min = 2, max = 40, message = "{validation.purchase-name.length}")
    private String name;
    @Size(max = 1024, message = "{validation.purchase-comment.length}")
    private String comment;
    @PositiveOrZero(message = "{validation.planprice.diap}")
    private Integer planPrice;
    @Positive(message = "{validation.count.diap}")
    private Integer count;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "EditScheduleRequest{" +
                "period=" + period +
                ", startDate=" + startDate +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", planPrice=" + planPrice +
                ", count=" + count +
                '}';
    }

    public Schedule applyChanges(Schedule schedule) {
        Schedule newSchedule = new Schedule();
        newSchedule.setPeriod(Optional.ofNullable(this.period).orElse(schedule.getPeriod()));
        newSchedule.setName(Optional.ofNullable(this.name).orElse(schedule.getName()));
        newSchedule.setStartDate(Optional.ofNullable(this.startDate).orElse(schedule.getStartDate()));
        newSchedule.setComment(Optional.ofNullable(this.comment).orElse(schedule.getComment()));
        newSchedule.setCount(Optional.ofNullable(this.count).orElse(schedule.getCount()));
        newSchedule.setStatus(Optional.ofNullable(this.status).orElse(schedule.getStatus()));
        newSchedule.setPlanPrice(Optional.ofNullable(this.planPrice).orElse(schedule.getPlanPrice()));
        return newSchedule;
    }
}
