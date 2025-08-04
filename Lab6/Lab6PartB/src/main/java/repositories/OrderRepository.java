package repositories;

import domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    // ======= METHOD NAME QUERIES =======
    List<Order> findByStatus(String status);
    List<Order> findByCustomerAddressCity(String city);
    
    // ======= JPQL QUERIES WITH @Query =======
    // JPQL: Give the ordernumbers of all orders with status 'closed'
    @Query("SELECT o.orderNumber FROM Order o WHERE o.status = 'closed'")
    List<String> findOrderNumbersByStatusClosed();
    
    // JPQL: Give the ordernumbers of all orders from customers who live in a certain city
    @Query("SELECT o.orderNumber FROM Order o WHERE o.customer.address.city = :city")
    List<String> findOrderNumbersByCustomerCity(@Param("city") String city);
    
    // JPQL: Give all orders with status 'closed' (full objects for specifications)
    @Query("SELECT o FROM Order o WHERE o.status = 'closed'")
    List<Order> findOrdersByStatusClosed();
} 