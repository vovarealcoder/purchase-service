package com.vova.purchaseservice.data.model;

import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
@EntityListeners(AuditingEntityListener.class)
public class Purchase {
    private int idPurchase;
    private User user;
    private Date purchased;
    private Date planDate;
    private Date created;
    private String name;
    private String comment;
    private Integer planPrice;
    private Integer factPrice;
    private PurchaseStatus status;
    private int count;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "id_purchase", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdPurchase() {
        return idPurchase;
    }

    public void setIdPurchase(int idPurchase) {
        this.idPurchase = idPurchase;
    }

    @Basic
    @Column(name = "purchased", nullable = false, insertable = false)
    public Date getPurchased() {
        return purchased;
    }

    public void setPurchased(Date purchased) {
        this.purchased = purchased;
    }

    @Basic
    @CreatedDate
    @Column(name = "created", nullable = false, insertable = false, updatable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Basic
    @Column(name = "plan_date", nullable = false, insertable = false)
    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "comment", length = -1)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "plan_price")
    public Integer getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(Integer planPrice) {
        this.planPrice = planPrice;
    }

    @Basic
    @Column(name = "fact_price", insertable = false)
    public Integer getFactPrice() {
        return factPrice;
    }

    public void setFactPrice(Integer factPrice) {
        this.factPrice = factPrice;
    }

    @Basic
    @Column(name = "status")
    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    @Basic
    @Column(name = "count", nullable = false)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}