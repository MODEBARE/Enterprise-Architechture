package repositories;

import domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Method name query to find course by name
    Optional<Course> findByName(String name);
    
    // Method name query to check if course exists by name
    boolean existsByName(String name);
} 