package Lab4;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class School {
    @Id
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKey(name = "studentid")
    private Map<String, Student> students = new HashMap<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<String, Student> getStudents() { return students; }
    public void setStudents(Map<String, Student> students) { this.students = students; }
}

