package specifications;

import domain.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OrderSpecifications {
    
    // Specification: Give the ordernumbers of all orders with status 'closed'
    public static Specification<Order> hasStatusClosed() {
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("status"), "closed");
            }
        };
    }
    
    // Alternative lambda approach for the same specification
    public static Specification<Order> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("status"), status);
    }
    
    // Specification: Orders from customers in a specific city
    public static Specification<Order> fromCustomersInCity(String city) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("customer").get("address").get("city"), city);
    }
} 