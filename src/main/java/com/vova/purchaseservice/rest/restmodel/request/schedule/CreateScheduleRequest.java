package com.vova.purchaseservice.rest.restmodel.request.schedule;

import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.enums.Periodic;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

public class CreateScheduleRequest {
    @NotEmpty(message = "{validation.purchase-name.empty}")
    @Size(min = 2, max = 40, message = "{validation.purchase-name.length}")
    private String name;
    @Size(max = 1024, message = "{validation.purchase-comment.length}")
    private String comment;
    @PositiveOrZero(message = "{validation.planprice.diap}")
    private Integer planPrice;
    private Periodic period;
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
}
