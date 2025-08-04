package repositories;

import domain.Grade;
import domain.Student;
import domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    // Method name queries
    List<Grade> findByStudent(Student student);
    List<Grade> findByCourse(Course course);
    Optional<Grade> findByStudentAndCourse(Student student, Course course);
    
    // Optimized queries with JOIN FETCH to avoid N+1 problems
    @Query("SELECT g FROM Grade g JOIN FETCH g.student JOIN FETCH g.course WHERE g.student = :student")
    List<Grade> findByStudentWithFetch(@Param("student") Student student);
    
    @Query("SELECT g FROM Grade g JOIN FETCH g.student JOIN FETCH g.course WHERE g.course = :course")
    List<Grade> findByCourseWithFetch(@Param("course") Course course);
} 