package specifications;

import domain.Customer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomerSpecifications {
    
    // Specification: Give all customers who live in Amsterdam
    public static Specification<Customer> livesInAmsterdam() {
        return new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("address").get("city"), "Amsterdam");
            }
        };
    }
    
    // Alternative lambda approach
    public static Specification<Customer> livesInCity(String city) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("address").get("city"), city);
    }
    
    // Specification: Customers from a specific country
    public static Specification<Customer> fromCountry(String country) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("address").get("country"), country);
    }
    
    // Specification: Customers with specific first name
    public static Specification<Customer> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("fname"), firstName);
    }
} 