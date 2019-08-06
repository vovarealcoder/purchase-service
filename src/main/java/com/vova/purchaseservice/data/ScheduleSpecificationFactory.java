package com.vova.purchaseservice.data;

import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.Schedule_;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.data.model.enums.Periodic;
import com.vova.purchaseservice.data.model.enums.ScheduleStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ScheduleSpecificationFactory {
    private ScheduleSpecificationFactory() {
    }

    public static Specification<Schedule> createFromGetQuery(Map<String, String> searchConditions, User user) {
        return (root, query, criteriaBuilder) ->
        {
            List<Predicate> predicates = toPredicates(searchConditions, criteriaBuilder, root);
            predicates.add(criteriaBuilder.equal(root.get(Schedule_.user), user));
            return criteriaBuilder.and(predicates
                    .toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> toPredicates(Map<String, String> queryStringConditions, CriteriaBuilder cb, Root<Schedule> root) {
        List<Predicate> predicates = new LinkedList<>();
        for (Map.Entry<String, String> entry : queryStringConditions.entrySet()) {
            switch (entry.getKey()) {
                case "idSchedule":
                    predicates.add(root.get(Schedule_.idSchedule).in(CriteriaUtils.parseIntList(entry.getValue())));
                    break;
                case "period":
                    Set<Periodic> periodCandidates = CriteriaUtils.parseEnumList(entry.getValue(), Periodic.class);
                    predicates.add(root.get(Schedule_.period).in(periodCandidates));
                    break;
                case "startDate":
                    predicates.add(CriteriaUtils.datePredicate(cb, root.get(Schedule_.startDate), entry.getValue()));
                    break;
                case "created":
                    predicates.add(CriteriaUtils.datePredicate(cb, root.get(Schedule_.created), entry.getValue()));
                    break;
                case "lastPurchase":
                    predicates.add(CriteriaUtils.datePredicate(cb, root.get(Schedule_.lastPurchase), entry.getValue()));
                    break;
                case "name":
                    predicates.add(CriteriaUtils.stringPredicate(cb, root.get(Schedule_.name), entry.getValue()));
                    break;
                case "comment":
                    predicates.add(CriteriaUtils.stringPredicate(cb, root.get(Schedule_.comment), entry.getValue()));
                    break;
                case "planPrice":
                    predicates.add(CriteriaUtils.integerPredicate(cb, root.get(Schedule_.planPrice), entry.getValue()));
                    break;
                case "status":
                    Set<ScheduleStatus> scheduleCandidates = CriteriaUtils.parseEnumList(entry.getValue(), ScheduleStatus.class);
                    predicates.add(root.get(Schedule_.status).in(scheduleCandidates));
                    break;
                case "count":
                    predicates.add(CriteriaUtils.integerPredicate(cb, root.get(Schedule_.count), entry.getValue()));
                    break;
                default:
            }
        }
        return predicates;
    }
}
