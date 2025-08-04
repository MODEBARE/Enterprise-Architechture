package repositories;

import domain.Department;
import domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Basic method name queries
    Optional<Student> findByStudentNumber(String studentNumber);
    List<Student> findByName(String name);
    boolean existsByStudentNumber(String studentNumber);
    
    // ======= METHOD NAME QUERIES (as requested in assignment) =======
    
    // Query 1: Get all students from a certain department
    List<Student> findByDepartment(Department department);
    List<Student> findByDepartmentName(String departmentName);
    
    // Query 2: Get all students who took a course with a certain name
    // This uses the relationship: Student -> Grade -> Course
    List<Student> findDistinctByGradesCourseName(String courseName);
    
    // ======= @QUERY ANNOTATIONS (as requested in assignment) =======
    
    // Query 1: Get all students from a certain department (optimized with JOIN FETCH)
    @Query("SELECT s FROM Student s JOIN FETCH s.department WHERE s.department.name = :departmentName")
    List<Student> findStudentsByDepartmentNameOptimized(@Param("departmentName") String departmentName);
    
    // Alternative approach - using Department object
    @Query("SELECT s FROM Student s JOIN FETCH s.department WHERE s.department = :department")
    List<Student> findStudentsByDepartmentOptimized(@Param("department") Department department);
    
    // Query 2: Get all students who took a course with a certain name (optimized with JOIN FETCH)
    @Query("SELECT DISTINCT s FROM Student s " +
           "JOIN FETCH s.department " +
           "JOIN s.grades g " +
           "JOIN g.course c " +
           "WHERE c.name = :courseName")
    List<Student> findStudentsWhoTookCourseOptimized(@Param("courseName") String courseName);
    
    // Additional optimized query to get students with their grades for a specific course
    @Query("SELECT DISTINCT s FROM Student s " +
           "JOIN FETCH s.department " +
           "JOIN FETCH s.grades g " +
           "JOIN FETCH g.course c " +
           "WHERE c.name = :courseName")
    List<Student> findStudentsWithGradesForCourse(@Param("courseName") String courseName);
    
    // Optimized query to get all students with their department (avoid N+1 problem)
    @Query("SELECT s FROM Student s JOIN FETCH s.department")
    List<Student> findAllWithDepartment();
    
    // Get students from department with minimal database access
    @Query("SELECT s FROM Student s JOIN FETCH s.department d WHERE d.name = :departmentName")
    List<Student> findByDepartmentNameWithFetch(@Param("departmentName") String departmentName);
} 