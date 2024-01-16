package co.cstad.sen.base;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BaseSpecification {
    public static <T> Specification<T> filters(Map<String,Object> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            filters.forEach((key, value) -> {
                if (value != null) {
                    if (value instanceof String){
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%"));
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                    }
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> hasUser(Object user) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("user"), user);
        };
    }
}
