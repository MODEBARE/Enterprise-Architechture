package Lab4;

import jakarta.persistence.*;

@Entity
public class Employee {
    @Id
    private Long employeenumber;

    private String name;

    @ManyToOne
    private Department department;

    // Getters and setters
    public Long getEmployeenumber() { return employeenumber; }
    public void setEmployeenumber(Long employeenumber) { this.employeenumber = employeenumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}

