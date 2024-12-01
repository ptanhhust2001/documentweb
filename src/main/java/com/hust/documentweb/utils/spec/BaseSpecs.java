package com.hust.documentweb.utils.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class BaseSpecs {
    private BaseSpecs(){}
    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> E getEnumValue(Class<?> c, String value) {
        return Enum.valueOf((Class<E>) c, value);
    }

    public static <T> Specification<T> idIn(List<Long> ids) {
        return (root, query, builder) -> root.get("id").in(ids);
    }

    public static <T> Specification<T> idNotIn(List<Long> ids) {
        return (root, query, builder) -> root.get("id").in(ids).not();
    }

    public static <T> Specification<T> searchQuery(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            Stack<Predicate> number = new Stack<>();
            Stack<String> operaterStack = new Stack<>();
            String rawStringOp = "";
            for (int i = 0; i < search.length(); i++) {
                char cha = search.charAt(i);
                if (cha == '（') {
                    if (!rawStringOp.isEmpty())
                        return null;
                    operaterStack.push(Character.toString(cha));
                } else if (cha == '）') {
                    if (operaterStack.isEmpty())
                        return null;
                    String opr = operaterStack.pop();
                    Operation op = convertString(rawStringOp);
                    if (op == null) {
                        if (!opr.equals("（")) {
                            if (opr.equals("＆"))
                                number.push(builder.and(number.pop(), number.pop()));
                            else
                                number.push(builder.or(number.pop(), number.pop()));
                            if (operaterStack.isEmpty() || !operaterStack.pop().equals("（"))
                                return null;
                        }
                    } else {
                        Predicate predicate = prepareFilter(op, builder, root);
                        if (predicate == null)
                            return null;
                        if (opr.equals("（"))
                            number.push(predicate);
                        else {
                            Predicate prePredicate = number.pop();
                            if (opr.equals("＆"))
                                number.push(builder.and(prePredicate, predicate));
                            else
                                number.push(builder.or(prePredicate, predicate));
                            if (operaterStack.isEmpty() || !operaterStack.pop().equals("（"))
                                return null;
                        }
                    }
                    rawStringOp = "";
                } else if (cha == '｜' || cha == '＆') {
                    if (rawStringOp.isEmpty()) {
                        if (!operaterStack.isEmpty()) {
                            if (operaterStack.peek().equals("（"))
                                return null;
                            var opr = operaterStack.pop();
                            if (opr.equals("＆"))
                                number.push(builder.and(number.pop(), number.pop()));
                            else
                                number.push(builder.or(number.pop(), number.pop()));
                        }
                        operaterStack.push(Character.toString(cha));

                    } else {
                        Operation op = convertString(rawStringOp);
                        if (op == null)
                            return null;
                        Predicate predicate = prepareFilter(op, builder, root);
                        if (predicate == null)
                            return null;
                        if (operaterStack.isEmpty() || operaterStack.peek().equals("（")) {
                            number.push(predicate);
                            operaterStack.push(Character.toString(cha));
                        } else {
                            Predicate prePredicate = number.pop();
                            String opr = operaterStack.pop();
                            if (opr.equals("＆"))
                                number.push(builder.and(prePredicate, predicate));
                            else
                                number.push(builder.or(prePredicate, predicate));
                            operaterStack.push(Character.toString(cha));
                        }
                    }
                    rawStringOp = "";
                } else {
                    rawStringOp += cha;
                }
                if (i == search.length() - 1) {
                    if (rawStringOp.isEmpty()) {
                        if (operaterStack.isEmpty())
                            predicates.add(number.pop());
                        else {
                            var opr = operaterStack.pop();
                            if (opr.equals("＆"))
                                predicates.add(builder.and(number.pop(), number.pop()));
                            else
                                predicates.add(builder.or(number.pop(), number.pop()));
                            if (!operaterStack.isEmpty() || !number.isEmpty())
                                return null;
                        }
                    } else {
                        Operation op = convertString(rawStringOp);
                        if (op == null)
                            return null;
                        Predicate predicate = prepareFilter(op, builder, root);
                        if (predicate == null)
                            return null;
                        if (operaterStack.isEmpty()) {
                            if (number.isEmpty())
                                predicates.add(predicate);
                            else
                                return null;
                        } else {
                            if (number.isEmpty())
                                return null;
                            Predicate prePredicate = number.pop();
                            if (!number.isEmpty())
                                return null;
                            String opr = operaterStack.pop();
                            if (opr.equals("＆"))
                                predicates.add(builder.and(prePredicate, predicate));
                            else
                                predicates.add(builder.or(prePredicate, predicate));
                        }
                    }
                }
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Operation convertString(String input) {
        var pattern = Pattern.compile("\\s*([^＞＜＝～\\s]+)\\s*([＞＜＝～]+)\\s*([^＞＜＝～]*[^＞＜＝～\\s]+)\\s*");
        var matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String key = matcher.group(1);
            String op = matcher.group(2);
            String value = matcher.group(3);
            return new Operation(key, op, value);
        }
        return null;
    }

    public static Predicate prepareFilter(Operation op, CriteriaBuilder builder, Path<?> root) {
        Predicate nextPre = null;
        var li = op.getKey().split("\\.");
        Path<?> path = root.get(li[0]);
        for (int i = 1; i < li.length; i++) {
            path = path.get(li[i]);
        }
        var type = path.getJavaType();
        Object compareValue = op.getValue();
        if (!compareValue.toString().contains("［") && type.isEnum()) {
            compareValue = getEnumValue(type, op.getValue());
        }
        if (type == Boolean.class) {
            compareValue = Boolean.parseBoolean(op.getValue());
        }
        switch (op.getOp()) {
            case "＝": {
                if (compareValue.toString().equalsIgnoreCase("NULL")) {
                    nextPre = builder.isNull(path);
                    break;
                } else if (compareValue.toString().equalsIgnoreCase("！NULL")) {
                    nextPre = builder.isNotNull(path);
                    break;
                } else if (compareValue.toString().startsWith("！")) {
                    compareValue = compareValue.toString().replace("！", "");
                    nextPre = builder.notEqual(path, compareValue);
                    break;
                } else if (compareValue.toString().startsWith("［")) {
                    nextPre = predicateIn(type, compareValue, path);
                    break;
                }
                nextPre = builder.equal(path, compareValue);
                break;
            }
            case "＞":
                nextPre = builder.greaterThan(path.as(String.class), compareValue.toString());
                break;
            case "＞＝":
                nextPre = builder.greaterThanOrEqualTo(path.as(String.class), compareValue.toString());
                break;
            case "＜":
                nextPre = builder.lessThan(path.as(String.class), compareValue.toString());
                break;
            case "＜＝":
                nextPre = builder.lessThanOrEqualTo(path.as(String.class), compareValue.toString());
                break;
            case "～":
                if (compareValue.toString().contains("！")) {
                    nextPre = builder.notLike(builder.lower(path.as(String.class)), "%" + compareValue.toString().replace("！", "").toLowerCase() + "%");
                } else {
                    nextPre = builder.like(builder.lower(path.as(String.class)), "%" + compareValue.toString().toLowerCase() + "%");
                }
                break;
            default:
                break;
        }
        return nextPre;
    }

    private static Predicate predicateIn(Class<?> type, Object compareValue, Path<?> path){
        String compareValueStr = compareValue.toString();
        if (compareValueStr.length() > 2) {
            String listStr = compareValueStr.substring(1, compareValueStr.length() - 1);
            List<Object> listValues;
            if (type.isEnum()) {
                listValues = Arrays.stream(listStr.split("，")).map(e -> getEnumValue(type, e)).toList();
            } else {
                listValues = List.of(listStr.split("，"));
            }
            return path.in(listValues);
        } else {
            return null;
        }
    }

    public static  <T> Specification<T> addOrderByToSpecification(String attr) {
        return (root, query, builder) -> {
            Expression<Integer> strLength = builder.length(root.get(attr));
            query.orderBy(builder.asc(strLength), builder.asc(root.get(attr)));
            return builder.and();
        };
    }

}
