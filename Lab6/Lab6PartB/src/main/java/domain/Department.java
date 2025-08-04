package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    // Optimized: Use LAZY loading to avoid unnecessary database access
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();
    
    // Constructors
    public Department() {}
    
    public Department(String name) {
        this.name = name;
    }
    
    // Helper method to maintain bidirectional relationship
    public void addStudent(Student student) {
        students.add(student);
        student.setDepartment(this);
    }
    
    public void removeStudent(Student student) {
        students.remove(student);
        student.setDepartment(null);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
} 