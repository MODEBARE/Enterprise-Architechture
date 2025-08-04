package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    // Optimized: Use LAZY loading to avoid unnecessary database access
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades = new ArrayList<>();
    
    // Constructors
    public Course() {}
    
    public Course(String name) {
        this.name = name;
    }
    
    // Helper method to maintain bidirectional relationship
    public void addGrade(Grade grade) {
        grades.add(grade);
        grade.setCourse(this);
    }
    
    public void removeGrade(Grade grade) {
        grades.remove(grade);
        grade.setCourse(null);
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
    
    public List<Grade> getGrades() {
        return grades;
    }
    
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
} 