package domain;

import javax.persistence.*;

@Entity
@Table(name = "patient")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
	private Address address;

	public Patient() {
	}

	public Patient(String name) {
		this.name = name;
	}
	
	public Patient(String name, String street, String city, String zip) {
		this.name = name;
		this.address = new Address(street, city, zip);
		this.address.setPatient(this);
	}

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
		if (address != null) {
			address.setPatient(this);
		}
	}
}
