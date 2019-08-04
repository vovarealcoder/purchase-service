package com.vova.purchaseservice.data.model;

import com.vova.purchaseservice.data.model.enums.PurchaseStatus;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Purchase.class)
public class Purchase_ {
    public static volatile SingularAttribute<Purchase, Integer> idPurchase;
    public static volatile SingularAttribute<Purchase, Date> purchased;
    public static volatile SingularAttribute<Purchase, Date> planDate;
    public static volatile SingularAttribute<Purchase, Date> created;
    public static volatile SingularAttribute<Purchase, String> name;
    public static volatile SingularAttribute<Purchase, User> user;
    public static volatile SingularAttribute<Purchase, String> comment;
    public static volatile SingularAttribute<Purchase, Integer> planPrice;
    public static volatile SingularAttribute<Purchase, Integer> factPrice;
    public static volatile SingularAttribute<Purchase, PurchaseStatus> status;
    public static volatile SingularAttribute<Purchase, Integer> count;
}
