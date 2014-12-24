package com.xsw.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class DynamicSpecifications {
    @SuppressWarnings("all")
    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> clazz) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (Collections3.isNotEmpty(filters)) {

                    List<Predicate> predicates = new ArrayList<Predicate>();
                    for (SearchFilter filter : filters) {
                        // nested path translate, 如Task的名为"user.name"的filedName,
                        // 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }

                        // 日期转换
                        Object valer = filter.value;
                        if ("class java.util.Date".equals(expression.getJavaType().toString())) {
                            try {
                                if (valer.toString().length() == 10) {
                                    valer = Util.strToDate((String) valer);
                                } else if (valer.toString().length() == 19) {
                                    valer = Util.strToDateTime((String) valer, "yyyy-MM-dd HH:mm:ss");
                                } else if (valer.toString().length() == 23) {
                                    valer = Util.strToDateTime((String) valer, "yyyy-MM-dd HH:mm:ss.SSS");
                                } else {
                                    valer = Util.strToTimeStamp((String) valer);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if ("class java.lang.Boolean".equals(expression.getJavaType().toString())) {
                            valer = Boolean.valueOf((String) filter.value);
                        }

                        // logic operator
                        switch (filter.operator) {
                        case EQ:
                            predicates.add(builder.equal(expression, valer));
                            break;
                        case LIKE:
                            predicates.add(builder.like(expression, "%" + valer + "%"));
                            break;
                        case GT:
                            predicates.add(builder.greaterThan(expression, (Comparable) valer));
                            break;
                        case LT:
                            predicates.add(builder.lessThan(expression, (Comparable) valer));
                            break;
                        case GTE:
                            predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) valer));
                            break;
                        case LTE:
                            predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) valer));
                            break;
                        case IN:
                            List<Object> lst = new LinkedList<Object>();
                            if (filter.value instanceof String) {
                                String val = (String) filter.value;
                                if (!Util.isEmpty(val)) {
                                    String[] sls = val.split(",");
                                    for (String s : sls) {
                                        if ("class java.lang.String".equals(expression.getJavaType().toString())) {
                                            lst.add(s);
                                        } else if ("class java.lang.Long".equals(expression.getJavaType().toString())
                                                || "long".equals(expression.getJavaType().toString())) {
                                            lst.add(Long.parseLong(s));
                                        } else {
                                            lst.add(Integer.parseInt(s));
                                        }
                                    }
                                }
                            } else if (filter.value instanceof List) {
                                lst = (List<Object>) filter.value;
                            }
                            if (lst.size() == 0)
                                break;
                            predicates.add(builder.in(expression).value(lst));
                            break;
                        case NOTNULL:
                            predicates.add(builder.isNotNull(expression));
                            break;
                        case NULL:
                            predicates.add(builder.isNull(expression));
                            break;
                        case LEFTLIKE:
                            predicates.add(builder.like(expression, "%" + valer));
                            break;
                        case RIGHTLIKE:
                            predicates.add(builder.like(expression, valer + "%"));
                            break;
                        }
                    }

                    // 将所有条件用 and 联合起来
                    if (predicates.size() > 0) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }
        };
    }
}
