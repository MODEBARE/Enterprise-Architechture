package Lab4;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    private String studentid;

    private String firstname;
    private String lastname;

    // Getters and setters
    public String getStudentid() { return studentid; }
    public void setStudentid(String studentid) { this.studentid = studentid; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
}

