package com.vova.purchaseservice.rest.restmodel.request.purchase;

import com.vova.purchaseservice.data.model.Purchase;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

public class CreatePurchaseRequest {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date planDate;
    @NotEmpty(message = "{validation.purchase-name.empty}")
    @Size(min = 2, max = 40, message = "{validation.purchase-name.length}")
    private String name;
    @Size(max = 1024, message = "{validation.purchase-comment.length}")
    private String comment;
    @PositiveOrZero(message = "{validation.planprice.diap}")
    private Integer planPrice;
    @Positive(message = "{validation.count.diap}")
    private Integer count;

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
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

    public Purchase toDbPurchase() {
        Purchase purchase = new Purchase();
        purchase.setName(this.getName());
        purchase.setComment(this.getComment());
        purchase.setCount(this.getCount());
        purchase.setPlanPrice(this.getPlanPrice());
        purchase.setPlanDate(this.getPlanDate());
        return purchase;
    }

    @Override
    public String toString() {
        return "CreatePurchaseRequest{" +
                "planDate=" + planDate +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", planPrice=" + planPrice +
                ", count=" + count +
                '}';
    }
}
