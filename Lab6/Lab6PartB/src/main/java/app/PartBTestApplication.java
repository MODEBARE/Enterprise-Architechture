package app;

import domain.*;
import repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "domain")
public class PartBTestApplication implements CommandLineRunner {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private GradeRepository gradeRepository;

    public static void main(String[] args) {
        SpringApplication.run(PartBTestApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // Create test data
        createTestData();
        
        System.out.println("=== Part B - Department/Student/Course/Grade System Demo ===\n");
        
        // Demonstrate method name queries
        testMethodNameQueries();
        
        // Demonstrate @Query optimized queries
        testOptimizedQueries();
        
        // Demonstrate optimization benefits
        testOptimizationComparison();
        
        System.out.println("\n=== Summary of Query Implementations ===");
        printQuerySummary();
    }
    
    private void createTestData() {
        System.out.println("Creating test data...\n");
        
        // Create Departments
        Department computerScience = new Department("Computer Science");
        Department mathematics = new Department("Mathematics"); 
        Department physics = new Department("Physics");
        
        departmentRepository.save(computerScience);
        departmentRepository.save(mathematics);
        departmentRepository.save(physics);
        
        // Create Courses
        Course javaProgramming = new Course("Java Programming");
        Course dataStructures = new Course("Data Structures");
        Course calculus = new Course("Calculus");
        Course quantumPhysics = new Course("Quantum Physics");
        
        courseRepository.save(javaProgramming);
        courseRepository.save(dataStructures);
        courseRepository.save(calculus);
        courseRepository.save(quantumPhysics);
        
        // Create Students
        Student alice = new Student("Alice Johnson", "CS001", computerScience);
        Student bob = new Student("Bob Smith", "CS002", computerScience);
        Student charlie = new Student("Charlie Brown", "MATH001", mathematics);
        Student diana = new Student("Diana Prince", "PHYS001", physics);
        Student eve = new Student("Eve Wilson", "CS003", computerScience);
        
        studentRepository.save(alice);
        studentRepository.save(bob);
        studentRepository.save(charlie);
        studentRepository.save(diana);
        studentRepository.save(eve);
        
        // Create Grades (Student-Course associations with grades)
        // Alice takes Java Programming and Data Structures
        Grade aliceJava = new Grade(85.5, alice, javaProgramming);
        Grade aliceDS = new Grade(92.0, alice, dataStructures);
        
        // Bob takes Java Programming and Calculus
        Grade bobJava = new Grade(78.0, bob, javaProgramming);
        Grade bobCalc = new Grade(88.5, bob, calculus);
        
        // Charlie takes Calculus and Data Structures
        Grade charlieCalc = new Grade(95.5, charlie, calculus);
        Grade charlieDS = new Grade(87.0, charlie, dataStructures);
        
        // Diana takes Quantum Physics and Calculus
        Grade dianaQuantum = new Grade(91.0, diana, quantumPhysics);
        Grade dianaCalc = new Grade(89.5, diana, calculus);
        
        // Eve takes Java Programming
        Grade eveJava = new Grade(82.5, eve, javaProgramming);
        
        gradeRepository.save(aliceJava);
        gradeRepository.save(aliceDS);
        gradeRepository.save(bobJava);
        gradeRepository.save(bobCalc);
        gradeRepository.save(charlieCalc);
        gradeRepository.save(charlieDS);
        gradeRepository.save(dianaQuantum);
        gradeRepository.save(dianaCalc);
        gradeRepository.save(eveJava);
        
        System.out.println("Test data created successfully!\n");
    }
    
    private void testMethodNameQueries() {
        System.out.println("========= METHOD NAME QUERIES =========");
        
        // Query 1: Get all students from a certain department
        System.out.println("1. Method Name Query - All students from Computer Science department:");
        List<Student> csStudents = studentRepository.findByDepartmentName("Computer Science");
        csStudents.forEach(student -> 
            System.out.println("   - " + student.getName() + " (" + student.getStudentNumber() + ")"));
        
        // Query 2: Get all students who took a course with a certain name
        System.out.println("\n2. Method Name Query - All students who took 'Java Programming':");
        List<Student> javaStudents = studentRepository.findDistinctByGradesCourseName("Java Programming");
        javaStudents.forEach(student -> 
            System.out.println("   - " + student.getName() + " (" + student.getStudentNumber() + ")"));
        
        System.out.println();
    }
    
    private void testOptimizedQueries() {
        System.out.println("========= OPTIMIZED @QUERY ANNOTATIONS =========");
        
        // Query 1: Get all students from a certain department (optimized)
        System.out.println("1. Optimized @Query - All students from Mathematics department:");
        List<Student> mathStudents = studentRepository.findStudentsByDepartmentNameOptimized("Mathematics");
        mathStudents.forEach(student -> 
            System.out.println("   - " + student.getName() + " (" + student.getStudentNumber() + 
                             ") - Dept: " + student.getDepartment().getName()));
        
        // Query 2: Get all students who took a course with a certain name (optimized)
        System.out.println("\n2. Optimized @Query - All students who took 'Calculus':");
        List<Student> calculusStudents = studentRepository.findStudentsWhoTookCourseOptimized("Calculus");
        calculusStudents.forEach(student -> 
            System.out.println("   - " + student.getName() + " (" + student.getStudentNumber() + 
                             ") - Dept: " + student.getDepartment().getName()));
        
        // Additional optimized query: Students with their grades for a specific course
        System.out.println("\n3. Advanced @Query - Students with grades for 'Data Structures':");
        List<Student> dsStudentsWithGrades = studentRepository.findStudentsWithGradesForCourse("Data Structures");
        dsStudentsWithGrades.forEach(student -> {
            System.out.println("   - " + student.getName() + " (" + student.getStudentNumber() + "):");
            student.getGrades().stream()
                .filter(grade -> grade.getCourse().getName().equals("Data Structures"))
                .forEach(grade -> 
                    System.out.println("     * " + grade.getCourse().getName() + ": " + grade.getGrade()));
        });
        
        System.out.println();
    }
    
    private void testOptimizationComparison() {
        System.out.println("========= OPTIMIZATION DEMONSTRATION =========");
        
        System.out.println("1. Standard query (may cause N+1 problem):");
        List<Student> allStudents = studentRepository.findAll();
        System.out.println("   Found " + allStudents.size() + " students");
        
        System.out.println("\n2. Optimized query with JOIN FETCH (prevents N+1 problem):");
        List<Student> allStudentsOptimized = studentRepository.findAllWithDepartment();
        System.out.println("   Found " + allStudentsOptimized.size() + " students with departments:");
        allStudentsOptimized.forEach(student -> 
            System.out.println("   - " + student.getName() + " (" + student.getDepartment().getName() + ")"));
        
        System.out.println("\n3. Department-specific optimized query:");
        List<Student> physicsStudents = studentRepository.findByDepartmentNameWithFetch("Physics");
        physicsStudents.forEach(student -> 
            System.out.println("   - " + student.getName() + " from " + student.getDepartment().getName()));
        
        System.out.println();
    }
    
    private void printQuerySummary() {
        System.out.println("METHOD NAME QUERIES:");
        System.out.println("  • studentRepository.findByDepartmentName(departmentName)");
        System.out.println("  • studentRepository.findDistinctByGradesCourseName(courseName)");
        
        System.out.println("\nOPTIMIZED @QUERY ANNOTATIONS:");
        System.out.println("  • studentRepository.findStudentsByDepartmentNameOptimized(departmentName)");
        System.out.println("  • studentRepository.findStudentsWhoTookCourseOptimized(courseName)");
        System.out.println("  • studentRepository.findStudentsWithGradesForCourse(courseName)");
        
        System.out.println("\nOPTIMIZATION FEATURES:");
        System.out.println("  • JOIN FETCH used to prevent N+1 problems");
        System.out.println("  • LAZY loading configured for all associations");
        System.out.println("  • Bidirectional relationships properly maintained");
        System.out.println("  • Query result caching through efficient JOINs");
        
        System.out.println("\nENTITY RELATIONSHIPS:");
        System.out.println("  • Department (1) ←→ (*) Student");
        System.out.println("  • Student (1) ←→ (*) Grade");
        System.out.println("  • Course (1) ←→ (*) Grade");
        System.out.println("  • Grade acts as association table between Student and Course");
    }
} 