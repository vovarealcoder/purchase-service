package com.vova.purchaseservice.rest.restmodel.response.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseItem {
    private int idPurchase;
    private Date purchased;
    private Date planDate;
    private Date created;
    private String name;
    private String comment;
    private Integer planPrice;
    private Integer factPrice;
    private PurchaseStatus status;
    private int count;

    public static PurchaseItem fromDbPurchase(Purchase purchase) {
        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setIdPurchase(purchase.getIdPurchase());
        purchaseItem.setName(purchase.getName());
        purchaseItem.setStatus(purchase.getStatus());
        purchaseItem.setComment(purchase.getComment());
        purchaseItem.setCreated(purchase.getCreated());
        purchaseItem.setCount(purchase.getCount());
        purchaseItem.setFactPrice(purchase.getFactPrice());
        purchaseItem.setPlanDate(purchase.getPlanDate());
        purchaseItem.setPlanPrice(purchase.getPlanPrice());
        purchaseItem.setPurchased(purchase.getPurchased());
        return purchaseItem;
    }

    public static Page<PurchaseItem> convertPagable(Page<Purchase> purchases) {
        List<PurchaseItem> purchaseItems = purchases.getContent()
                .stream()
                .map(PurchaseItem::fromDbPurchase)
                .collect(Collectors.toList());
        return new PageImpl<>(purchaseItems);
    }

    public int getIdPurchase() {
        return idPurchase;
    }

    public void setIdPurchase(int idPurchase) {
        this.idPurchase = idPurchase;
    }

    public Date getPurchased() {
        return purchased;
    }

    public void setPurchased(Date purchased) {
        this.purchased = purchased;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
