package app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    
    // Scenario 1: Get all schools without students (optimized - no JOIN)
    @Query("SELECT s FROM School s")
    List<School> findAllSchoolsOnly();
    
    // Scenario 2: Get all schools with students (optimized with JOIN FETCH)
    @Query("SELECT DISTINCT s FROM School s LEFT JOIN FETCH s.students")
    List<School> findAllSchoolsWithStudents();
} 