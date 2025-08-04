package Lab4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private final DataSource dataSource;

	public Application(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try (Connection conn = dataSource.getConnection()) {

			// 1. Save Department
			PreparedStatement insertDept = conn.prepareStatement("INSERT INTO department (id, name) VALUES (?, ?)");
			insertDept.setLong(1, 1L);
			insertDept.setString(2, "Computer Science");
			insertDept.executeUpdate();

			// 2. Save Employees assigned to that Department
			PreparedStatement insertEmp1 = conn.prepareStatement("INSERT INTO employee (employeenumber, name, department_id) VALUES (?, ?, ?)");
			insertEmp1.setLong(1, 100L);
			insertEmp1.setString(2, "Alice Johnson");
			insertEmp1.setLong(3, 1L);
			insertEmp1.executeUpdate();

			PreparedStatement insertEmp2 = conn.prepareStatement("INSERT INTO employee (employeenumber, name, department_id) VALUES (?, ?, ?)");
			insertEmp2.setLong(1, 101L);
			insertEmp2.setString(2, "Bob Smith");
			insertEmp2.setLong(3, 1L);
			insertEmp2.executeUpdate();

			// 3. Retrieve the Department and verify if Employees are also retrieved
			System.out.println("📌 Retrieved Department and Employees:");
			PreparedStatement getDept = conn.prepareStatement("SELECT * FROM department WHERE id = ?");
			getDept.setLong(1, 1L);
			ResultSet rsDept = getDept.executeQuery();

			if (rsDept.next()) {
				String deptName = rsDept.getString("name");
				System.out.println("Department: " + deptName);

				PreparedStatement getEmps = conn.prepareStatement("SELECT * FROM employee WHERE department_id = ?");
				getEmps.setLong(1, 1L);
				ResultSet rsEmps = getEmps.executeQuery();

				while (rsEmps.next()) {
					System.out.println(" - Employee: " + rsEmps.getString("name") + " (ID: " + rsEmps.getLong("employeenumber") + ")");
				}
			}

			System.out.println("✅ Bidirectional Department–Employee test complete.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
