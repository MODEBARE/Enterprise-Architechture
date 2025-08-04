package specifications;

import domain.CD;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CDSpecifications {
    
    // Specification: Give all CD's from a certain artist with a price bigger than a certain amount
    public static Specification<CD> byArtistAndPriceGreaterThan(String artist, double amount) {
        return new Specification<CD>() {
            @Override
            public Predicate toPredicate(Root<CD> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate artistPredicate = criteriaBuilder.equal(root.get("artist"), artist);
                Predicate pricePredicate = criteriaBuilder.greaterThan(root.get("price"), amount);
                return criteriaBuilder.and(artistPredicate, pricePredicate);
            }
        };
    }
    
    // Alternative lambda approach for the same specification
    public static Specification<CD> hasArtistAndPriceGreaterThan(String artist, double amount) {
        return (root, query, criteriaBuilder) -> {
            Predicate artistPredicate = criteriaBuilder.equal(root.get("artist"), artist);
            Predicate pricePredicate = criteriaBuilder.greaterThan(root.get("price"), amount);
            return criteriaBuilder.and(artistPredicate, pricePredicate);
        };
    }
    
    // Specification: CDs from a specific artist
    public static Specification<CD> byArtist(String artist) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("artist"), artist);
    }
    
    // Specification: CDs with price greater than amount
    public static Specification<CD> priceGreaterThan(double amount) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("price"), amount);
    }
    
    // Specification: CDs with price less than amount
    public static Specification<CD> priceLessThan(double amount) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.lessThan(root.get("price"), amount);
    }
} 