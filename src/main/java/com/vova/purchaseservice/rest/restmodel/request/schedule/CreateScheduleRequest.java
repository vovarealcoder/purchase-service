package com.vova.purchaseservice.rest.restmodel.request.schedule;

import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.enums.Periodic;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

public class CreateScheduleRequest {
    @NotEmpty(message = "{validation.purchase-name.empty}")
    @Size(min = 2, max = 40, message = "{validation.purchase-name.length}")
    private String name;
    @Size(max = 1024, message = "{validation.purchase-comment.length}")
    private String comment;
    @PositiveOrZero(message = "{validation.planprice.diap}")
    private Integer planPrice;
    @NotNull(message = "{validation.period.not-null}")
    private Periodic period;
    @NotNull(message = "{validation.start-date.not-null}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startDate;
    @Positive(message = "{validation.count.diap}")
    private Integer count;

    public Schedule toDbScheduled() {
        Schedule schedule = new Schedule();
        schedule.setName(name);
        schedule.setComment(comment);
        schedule.setPlanPrice(planPrice);
        schedule.setPeriod(period);
        schedule.setStartDate(startDate);
        schedule.setCount(count);
        return schedule;
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

    @Override
    public String toString() {
        return "CreateScheduleRequest{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", planPrice=" + planPrice +
                ", period=" + period +
                ", startDate=" + startDate +
                ", count=" + count +
                '}';
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
