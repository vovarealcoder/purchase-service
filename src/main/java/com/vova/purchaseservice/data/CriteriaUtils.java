package com.vova.purchaseservice.data;

import com.vova.purchaseservice.ex.InvalidCriteriaException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class CriteriaUtils {
    private static final FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy");

    private CriteriaUtils() {
    }

    static Predicate stringPredicate(CriteriaBuilder cb, Expression<String> attribute, String value) {
        Matcher matcher = parseRawCondition(value);
        String operator = matcher.group(1);
        String pattern = matcher.group(2);
        switch (operator) {
            case "eq":
                return cb.equal(cb.lower(attribute), StringUtils.lowerCase(pattern));
            case "like":
                return cb.like(cb.lower(attribute), getContainsLikePattern(pattern));
            default:
                throw new InvalidCriteriaException("Invalid criteria operator");
        }
    }

    private static Matcher parseRawCondition(String value) {
        Matcher matcher = Pattern.compile("^\\[(\\w+)](.*)").matcher(value);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new InvalidCriteriaException("invalid condition format");
        }
        return matcher;
    }

    static Predicate integerPredicate(CriteriaBuilder cb, Expression<Integer> attribute, String value) {
        Matcher matcher = parseRawCondition(value);
        String operator = matcher.group(1);
        Integer[] ints = parseIntListToArray(matcher.group(2));
        return comparePredicate(cb, operator, attribute, ints);

    }

    private static <T extends Comparable<? super T>> Predicate comparePredicate(CriteriaBuilder cb, String operator, Expression<T> attribute, T[] ints) {
        switch (operator) {
            case "gte":
                checkArrayLen(ints, 1);
                return cb.greaterThanOrEqualTo(attribute, ints[0]);
            case "gt":
                checkArrayLen(ints, 1);
                return cb.greaterThan(attribute, ints[0]);
            case "lt":
                checkArrayLen(ints, 1);
                return cb.lessThan(attribute, ints[0]);
            case "lte":
                checkArrayLen(ints, 1);
                return cb.lessThanOrEqualTo(attribute, ints[0]);
            case "between":
                checkArrayLen(ints, 2);
                return cb.between(attribute, ints[0], ints[1]);
            default:
                throw new InvalidCriteriaException("Invalid criteria operator");
        }
    }

    static Predicate datePredicate(CriteriaBuilder cb, Expression<Date> attribute, String value) {
        Matcher matcher = parseRawCondition(value);
        String operator = matcher.group(1);
        String dates = matcher.group(2);
        Date[] dateArray = Arrays.stream(dates.split(",")).map(String::trim).map(e -> {
            try {
                return fdf.parse(e);
            } catch (ParseException e1) {
                throw new InvalidCriteriaException(e);
            }
        }).toArray(Date[]::new);


        return comparePredicate(cb, operator, attribute, dateArray);
    }

    private static <V> void checkArrayLen(V[] array, int expected) {
        if (array.length != expected) {
            throw new InvalidCriteriaException("array length is not " + expected);
        }
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }

    static Set<Integer> parseIntList(String list) {
        if (StringUtils.trimToNull(list) == null) {
            throw new InvalidCriteriaException("list is empty");
        }
        try {
            return Arrays.stream(list.trim().split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        } catch (NumberFormatException e) {
            throw new InvalidCriteriaException(e);
        }
    }

    static <T extends Enum<T>> Set<T> parsePurchaseStatusList(String list, Class<T> clazz) {
        if (StringUtils.trimToNull(list) == null) {
            throw new InvalidCriteriaException("list is empty");
        }
        try {
            return Arrays.stream(list.trim().split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .map(String::toUpperCase)
                    .map(e -> Enum.valueOf(clazz, e))
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException e) {
            throw new InvalidCriteriaException(e);
        }
    }

    private static Integer[] parseIntListToArray(String list) {
        return new ArrayList<>(parseIntList(list)).toArray(new Integer[0]);
    }
}
