package app;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school")
public class School {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    // Optimized: Use LAZY loading to avoid N+1 problems when fetching schools only
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();
    
    // Constructors
    public School() {}
    
    public School(String name) {
        this.name = name;
    }
    
    // Helper method to add student and maintain bidirectional relationship
    public void addStudent(Student student) {
        students.add(student);
        student.setSchool(this);
    }
    
    public void removeStudent(Student student) {
        students.remove(student);
        student.setSchool(null);
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