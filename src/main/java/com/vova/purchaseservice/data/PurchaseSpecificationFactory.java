package com.vova.purchaseservice.data;

import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.Purchase_;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class PurchaseSpecificationFactory {


    private PurchaseSpecificationFactory() {
    }

    public static Specification<Purchase> createFromGetQuery(Map<String, String> searchConditions, User user) {
        return (root, query, criteriaBuilder) ->
        {
            List<Predicate> predicates = toPredicates(searchConditions, criteriaBuilder, root);
            predicates.add(criteriaBuilder.equal(root.get(Purchase_.user), user));
            return criteriaBuilder.and(predicates
                    .toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> toPredicates(Map<String, String> queryStringConditions, CriteriaBuilder cb, Root<Purchase> root) {
        List<Predicate> predicates = new LinkedList<>();
        for (Map.Entry<String, String> entry : queryStringConditions.entrySet()) {
            switch (entry.getKey()) {
                case "idPurchase":
                    predicates.add(root.get(Purchase_.idPurchase).in(CriteriaUtils.parseIntList(entry.getValue())));
                    break;
                case "purchased":
                    predicates.add(CriteriaUtils.datePredicate(cb, root.get(Purchase_.purchased), entry.getValue()));
                    break;
                case "planDate":
                    predicates.add(CriteriaUtils.datePredicate(cb, root.get(Purchase_.planDate), entry.getValue()));
                    break;
                case "created":
                    predicates.add(CriteriaUtils.datePredicate(cb, root.get(Purchase_.created), entry.getValue()));
                    break;
                case "name":
                    predicates.add(CriteriaUtils.stringPredicate(cb, root.get(Purchase_.name), entry.getValue()));
                    break;
                case "comment":
                    predicates.add(CriteriaUtils.stringPredicate(cb, root.get(Purchase_.comment), entry.getValue()));
                    break;
                case "planPrice":
                    predicates.add(CriteriaUtils.integerPredicate(cb, root.get(Purchase_.planPrice), entry.getValue()));
                    break;
                case "factPrice":
                    predicates.add(CriteriaUtils.integerPredicate(cb, root.get(Purchase_.factPrice), entry.getValue()));
                    break;
                case "status":
                    predicates.add(root.get(Purchase_.status).in(CriteriaUtils.parsePurchaseStatusList(entry.getValue(), PurchaseStatus.class)));
                    break;
                case "count":
                    predicates.add(CriteriaUtils.integerPredicate(cb, root.get(Purchase_.count), entry.getValue()));
                    break;
                default:
            }
        }
        return predicates;
    }
}
