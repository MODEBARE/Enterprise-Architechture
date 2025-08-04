package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertSchoolsWithStudentsBatch(int schoolCount, int studentsPerSchool) {
        System.out.println("Inserting " + schoolCount + " schools with " + studentsPerSchool + " students each using batch operations...");
        
        int batchSize = 100; // Smaller batch for complex operations
        int totalCount = 0;
        
        for (int schoolIndex = 0; schoolIndex < schoolCount; schoolIndex++) {
            School school = new School("School " + schoolIndex);
            
            // Add students to school
            for (int studentIndex = 0; studentIndex < studentsPerSchool; studentIndex++) {
                Student student = new Student(
                    "FirstName" + studentIndex,
                    "LastName" + studentIndex,
                    "student" + studentIndex + "@school" + schoolIndex + ".edu"
                );
                school.addStudent(student);
            }
            
            entityManager.persist(school);
            totalCount++;
            
            // Flush and clear every batch to manage memory
            if (totalCount % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
                System.out.println("Batched " + totalCount + " schools");
            }
        }
        entityManager.flush();
        entityManager.clear();
        
        System.out.println("Completed inserting " + totalCount + " schools with " + (totalCount * studentsPerSchool) + " total students");
    }

    @Transactional(readOnly = true)
    public List<School> retrieveSchoolsOnly() {
        // Scenario 1: Get schools without loading students (optimized)
        return schoolRepository.findAllSchoolsOnly();
    }

    @Transactional(readOnly = true)
    public List<School> retrieveSchoolsWithStudents() {
        // Scenario 2: Get schools with students using JOIN FETCH (optimized)
        return schoolRepository.findAllSchoolsWithStudents();
    }

    @Transactional(readOnly = true)
    public List<School> retrieveSchoolsWithStudentsUnoptimized() {
        // Unoptimized approach for comparison - will cause N+1 problem
        // But we need to trigger the loading within the transaction to avoid LazyInitializationException
        List<School> schools = schoolRepository.findAll();
        // Force loading of students to trigger N+1 problem within transaction
        for (School school : schools) {
            school.getStudents().size(); // This will trigger individual queries (N+1 problem)
        }
        return schools;
    }

    public long getSchoolCount() {
        return schoolRepository.count();
    }
    
    public long getStudentCount() {
        return studentRepository.count();
    }
} 