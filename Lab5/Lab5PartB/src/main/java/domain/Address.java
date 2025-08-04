package domain;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    
    @Id
    @Column(name = "PATIENT_ID")
    private Long patientId;
    
    @Column(name = "STREET")
    private String street;
    
    @Column(name = "CITY")
    private String city;
    
    @Column(name = "ZIP")
    private String zip;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;
    
    public Address() {
    }
    
    public Address(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }
    
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getZip() {
        return zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
} 