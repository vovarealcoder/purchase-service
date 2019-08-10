package com.vova.purchaseservice.data.model;

import com.vova.purchaseservice.data.model.enums.Periodic;
import com.vova.purchaseservice.data.model.enums.ScheduleStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "schedule")
public class Schedule {
    private User user;
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

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "id_schedule", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
    @Column(name = "period", nullable = false, length = -1)
    public Periodic getPeriod() {
        return period;
    }

    public void setPeriod(Periodic period) {
        this.period = period;
    }

    @Basic
    @Column(name = "status")
    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }


    @Basic
    @Column(name = "start_date", nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "count", nullable = false)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Basic
    @Column(name = "last_purchase", nullable = false, insertable = false)
    public Date getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(Date lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "user=" + user +
                ", idSchedule=" + idSchedule +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", planPrice=" + planPrice +
                ", period=" + period +
                ", startDate=" + startDate +
                ", status=" + status +
                ", count=" + count +
                ", lastPurchase=" + lastPurchase +
                '}';
    }
}
