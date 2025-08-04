package repositories;

import domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // Method name query to find department by name
    Optional<Department> findByName(String name);
    
    // Method name query to check if department exists by name
    boolean existsByName(String name);
} 