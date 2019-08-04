package com.vova.purchaseservice.data.model;

import com.vova.purchaseservice.data.model.enums.Periodic;
import com.vova.purchaseservice.data.model.enums.ScheduleStatus;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Schedule.class)
public class Schedule_ {
    public static volatile SingularAttribute<Schedule, Integer> idSchedule;
    public static volatile SingularAttribute<Schedule, Periodic> period;
    public static volatile SingularAttribute<Schedule, Date> startDate;
    public static volatile SingularAttribute<Schedule, Date> created;
    public static volatile SingularAttribute<Schedule, String> name;
    public static volatile SingularAttribute<Schedule, User> user;
    public static volatile SingularAttribute<Schedule, String> comment;
    public static volatile SingularAttribute<Schedule, Integer> planPrice;
    public static volatile SingularAttribute<Schedule, ScheduleStatus> status;
    public static volatile SingularAttribute<Schedule, Integer> count;
    public static volatile SingularAttribute<Schedule, Date> lastPurchase;
}
