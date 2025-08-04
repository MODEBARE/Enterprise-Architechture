package domain;

import javax.persistence.*;

@Entity
@Table(name = "appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "APDATE")
	private String appdate;
	
	@Column(name = "PAYDATE")
	private String paydate;
	
	@Column(name = "AMOUNT")
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "PATIENT")
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name = "DOCTOR")
	private Doctor doctor;

	public Appointment() {
	}
	
	public Appointment(String appdate, String paydate, double amount, Patient patient, Doctor doctor) {
		this.appdate = appdate;
		this.paydate = paydate;
		this.amount = amount;
		this.patient = patient;
		this.doctor = doctor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppdate() {
		return appdate;
	}

	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
}

