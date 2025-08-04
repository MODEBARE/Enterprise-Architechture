package app;

import domain.Appointment;
import domain.Doctor;
import domain.Patient;
import domain.Address;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootApplication
@EnableJpaRepositories("repositories")
@EntityScan("domain") 
public class Application implements CommandLineRunner {
	
	@PersistenceContext
	private EntityManager entityManager;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		System.out.println("=== Creating and persisting entities ===");
		
		// Create doctors
		Doctor doctor1 = new Doctor("Chirurg", "Frank", "Brown");
		Doctor doctor2 = new Doctor("Nurse", "Mary", "Jones");
		
		// Create patients with addresses
		Patient patient1 = new Patient("Jerry Lewis", "34 4th avenue", "New York", "13221");
		Patient patient2 = new Patient("Frank Moore", "34 Mainstreet", "New York", "13221");
		Patient patient3 = new Patient("Sam Ruby", "105 N Street", "New York", "13221");

		// Persist doctors first
		entityManager.persist(doctor1);
		entityManager.persist(doctor2);
		
		// Persist patients (addresses will be persisted automatically due to cascade)
		entityManager.persist(patient1);
		entityManager.persist(patient2);
		entityManager.persist(patient3);
		
		// Create appointments with payment information
		Appointment appointment1 = new Appointment("11-11-2008", "10-10-2008", 12.50, patient1, doctor1);
		Appointment appointment2 = new Appointment("12-11-2008", "11-10-2008", 45.00, patient2, doctor2);
		Appointment appointment3 = new Appointment("13-11-2008", "12-10-2008", 99.60, patient3, doctor2);
		Appointment appointment4 = new Appointment("14-11-2008", "13-10-2008", 55.80, patient1, doctor1);

		// Persist appointments
		entityManager.persist(appointment1);
		entityManager.persist(appointment2);
		entityManager.persist(appointment3);
		entityManager.persist(appointment4);
		
		// Flush to ensure all entities are saved
		entityManager.flush();
		
		System.out.println("=== All entities persisted successfully ===");
		
		// Test retrieval to verify mappings work
		testRetrievalAndMappings();
	}
	
	@Transactional
	public void testRetrievalAndMappings() {
		System.out.println("\n=== Testing retrieval and mappings ===");
		
		// Retrieve all appointments and verify relationships
		var appointments = entityManager.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();
		
		System.out.println("\nFound " + appointments.size() + " appointments:");
		for (Appointment apt : appointments) {
			System.out.println("\n--- Appointment ID: " + apt.getId() + " ---");
			System.out.println("Appointment Date: " + apt.getAppdate());
			System.out.println("Payment Date: " + apt.getPaydate());
			System.out.println("Amount: $" + apt.getAmount());
			
			// Test Patient mapping
			Patient patient = apt.getPatient();
			System.out.println("Patient: " + patient.getName() + " (ID: " + patient.getId() + ")");
			
			// Test Address mapping
			Address address = patient.getAddress();
			if (address != null) {
				System.out.println("Address: " + address.getStreet() + ", " + address.getCity() + " " + address.getZip());
			}
			
			// Test Doctor mapping
			Doctor doctor = apt.getDoctor();
			System.out.println("Doctor: " + doctor.getFirstname() + " " + doctor.getLastname() + 
			                  " (" + doctor.getType() + ") (ID: " + doctor.getId() + ")");
		}
		
		System.out.println("\n=== JPA Mapping test completed successfully! ===");
	}
}
