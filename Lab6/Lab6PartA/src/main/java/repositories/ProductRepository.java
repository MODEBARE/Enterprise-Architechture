package repositories;

import domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by name
    List<Product> findByName(String name);
    
    // Find products by name containing
    List<Product> findByNameContaining(String name);
    
    // Find products by price less than
    List<Product> findByPriceLessThan(double price);
} 