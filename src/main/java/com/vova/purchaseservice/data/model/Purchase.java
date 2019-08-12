package com.vova.purchaseservice.data.model;

import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
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

    @Override
    public String toString() {
        return "Purchase{" +
                "idPurchase=" + idPurchase +
                ", user=" + user +
                ", purchased=" + purchased +
                ", planDate=" + planDate +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", planPrice=" + planPrice +
                ", factPrice=" + factPrice +
                ", status=" + status +
                ", count=" + count +
                '}';
    }

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false, updatable = false)
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
    @Column(name = "purchased", insertable = false)
    @Temporal(TemporalType.DATE)
    public Date getPurchased() {
        return purchased;
    }

    public void setPurchased(Date purchased) {
        this.purchased = purchased;
    }

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Basic
    @Column(name = "plan_date")
    @Temporal(TemporalType.DATE)
    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "comment", columnDefinition = "TEXT")
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
