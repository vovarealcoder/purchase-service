package com.vova.purchaseservice.rest.restmodel.request.purchase;

import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Optional;

public class EditPurchaseRequest {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date purchased;
    @PositiveOrZero(message = "{validation.fact-price.diap}")
    private Integer factPrice;
    private PurchaseStatus status;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date planDate;
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

    public Date getPurchased() {
        return purchased;
    }

    public void setPurchased(Date purchased) {
        this.purchased = purchased;
    }

    public Integer getFactPrice() {
        return factPrice;
    }

    public void setFactPrice(Integer factPrice) {
        this.factPrice = factPrice;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "EditPurchaseRequest{" +
                "purchased=" + purchased +
                ", factPrice=" + factPrice +
                ", status=" + status +
                ", planDate=" + planDate +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", planPrice=" + planPrice +
                ", count=" + count +
                '}';
    }

    public Purchase applyChanges(Purchase purchase) {
        Purchase newPurchase = new Purchase();
        newPurchase.setName(Optional.ofNullable(this.name).orElse(purchase.getName()));
        newPurchase.setComment(Optional.ofNullable(this.comment).orElse(purchase.getComment()));
        newPurchase.setCount(Optional.ofNullable(this.count).orElse(purchase.getCount()));
        newPurchase.setPlanPrice(Optional.ofNullable(this.planPrice).orElse(purchase.getPlanPrice()));
        newPurchase.setPlanDate(Optional.ofNullable(this.planDate).orElse(purchase.getPlanDate()));
        newPurchase.setPurchased(Optional.ofNullable(this.purchased).orElse(purchase.getPurchased()));
        newPurchase.setFactPrice(Optional.ofNullable(this.factPrice).orElse(purchase.getFactPrice()));
        newPurchase.setStatus(Optional.ofNullable(this.status).orElse(purchase.getStatus()));
        return newPurchase;
    }

}
