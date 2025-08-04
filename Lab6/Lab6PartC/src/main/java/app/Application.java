package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SchoolService schoolService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=== JPA OPTIMIZATION DEMO ===\n");
		
		// Part 1: Customer/Account Optimization
		demonstrateCustomerOptimization();
		
		// Part 2: School/Student System
		demonstrateSchoolOptimization();
		
		System.out.println("\n=== OPTIMIZATION SUMMARY ===");
		printOptimizationSummary();
	}

	private void demonstrateCustomerOptimization() {
		System.out.println("========= CUSTOMER/ACCOUNT OPTIMIZATION =========\n");
		
		// Test with smaller number for demo purposes (5000 instead of 50000)
		int customerCount = 5000;
		
		// 1. Optimized Insert
		System.out.println("1. OPTIMIZED BATCH INSERT:");
		long start = System.currentTimeMillis();
		customerService.insertCustomersBatch(customerCount);
		long insertTime = System.currentTimeMillis() - start;
		System.out.println("✅ Optimized insert of " + customerCount + " customers took: " + insertTime + " ms\n");
		
		// 2. Optimized Retrieve
		System.out.println("2. OPTIMIZED RETRIEVE:");
		start = System.currentTimeMillis();
		List<Customer> customers = customerService.retrieveAllCustomers();
		long retrieveTime = System.currentTimeMillis() - start;
		System.out.println("✅ Optimized retrieve of " + customers.size() + " customers took: " + retrieveTime + " ms\n");
		
		// 3. Compare Update Methods
		System.out.println("3. UPDATE COMPARISON:");
		
		// 3a. Optimized Batch Update (JPQL)
		start = System.currentTimeMillis();
		customerService.updateAllCustomersBatch("James Bond");
		long batchUpdateTime = System.currentTimeMillis() - start;
		System.out.println("✅ Optimized batch update took: " + batchUpdateTime + " ms");
		
		// 3b. Old Way Update (for comparison)
		start = System.currentTimeMillis();
		customerService.updateAllCustomersOldWay("John McClane");
		long oldUpdateTime = System.currentTimeMillis() - start;
		System.out.println("⚠️  Old way update took: " + oldUpdateTime + " ms");
		
		double improvementRatio = (double) oldUpdateTime / batchUpdateTime;
		System.out.println("🚀 Batch update is " + String.format("%.1fx", improvementRatio) + " faster!\n");
	}

	private void demonstrateSchoolOptimization() {
		System.out.println("========= SCHOOL/STUDENT OPTIMIZATION =========\n");
		
		// Create test data: 100 schools with 50 students each = 5000 students
		int schoolCount = 100;
		int studentsPerSchool = 50;
		
		// 1. Bulk Insert Schools and Students
		System.out.println("1. BULK INSERT SCHOOLS AND STUDENTS:");
		long start = System.currentTimeMillis();
		schoolService.insertSchoolsWithStudentsBatch(schoolCount, studentsPerSchool);
		long insertTime = System.currentTimeMillis() - start;
		System.out.println("✅ Inserted " + schoolService.getSchoolCount() + " schools with " + 
		                  schoolService.getStudentCount() + " students in: " + insertTime + " ms\n");
		
		// 2. Scenario 1: Retrieve Schools Only (Optimized)
		System.out.println("2. SCENARIO 1 - RETRIEVE SCHOOLS ONLY (NO STUDENTS):");
		start = System.currentTimeMillis();
		List<School> schoolsOnly = schoolService.retrieveSchoolsOnly();
		long schoolsOnlyTime = System.currentTimeMillis() - start;
		
		System.out.println("School names retrieved:");
		schoolsOnly.stream().limit(5).forEach(school -> 
			System.out.println("   - " + school.getName()));
		System.out.println("   ... and " + (schoolsOnly.size() - 5) + " more schools");
		System.out.println("✅ Optimized schools-only retrieval took: " + schoolsOnlyTime + " ms\n");
		
		// 3. Scenario 2: Retrieve Schools with Students (Optimized)
		System.out.println("3. SCENARIO 2 - RETRIEVE SCHOOLS WITH STUDENTS (OPTIMIZED):");
		start = System.currentTimeMillis();
		List<School> schoolsWithStudents = schoolService.retrieveSchoolsWithStudents();
		long optimizedTime = System.currentTimeMillis() - start;
		
		System.out.println("Schools with students retrieved:");
		schoolsWithStudents.stream().limit(3).forEach(school -> {
			System.out.println("   - " + school.getName() + " (" + school.getStudents().size() + " students):");
			school.getStudents().stream().limit(3).forEach(student ->
				System.out.println("     * " + student.getFirstname() + " " + student.getLastname()));
			if (school.getStudents().size() > 3) {
				System.out.println("     ... and " + (school.getStudents().size() - 3) + " more students");
			}
		});
		System.out.println("   ... and " + (schoolsWithStudents.size() - 3) + " more schools");
		System.out.println("✅ Optimized schools-with-students retrieval took: " + optimizedTime + " ms\n");
		
		// 4. Comparison with Unoptimized Approach
		System.out.println("4. COMPARISON WITH UNOPTIMIZED APPROACH:");
		start = System.currentTimeMillis();
		List<School> unoptimizedSchools = schoolService.retrieveSchoolsWithStudentsUnoptimized();
		// Force loading of students to trigger N+1 problem
		for (School school : unoptimizedSchools) {
			school.getStudents().size(); // This will trigger individual queries
		}
		long unoptimizedTime = System.currentTimeMillis() - start;
		System.out.println("⚠️  Unoptimized approach (N+1 problem) took: " + unoptimizedTime + " ms");
		
		if (unoptimizedTime > optimizedTime) {
			double improvementRatio = (double) unoptimizedTime / optimizedTime;
			System.out.println("🚀 Optimized approach is " + String.format("%.1fx", improvementRatio) + " faster!\n");
		}
	}

	private void printOptimizationSummary() {
		System.out.println("KEY OPTIMIZATIONS IMPLEMENTED:");
		System.out.println("  📊 Batch Processing: Using EntityManager.flush() and clear() in batches");
		System.out.println("  🔄 Transaction Management: @Transactional annotations for proper boundaries");
		System.out.println("  📝 Batch Updates: JPQL UPDATE queries instead of individual saves");
		System.out.println("  🚀 JOIN FETCH: Preventing N+1 problems with optimized queries");
		System.out.println("  💤 LAZY Loading: Loading data only when needed");
		System.out.println("  ⚙️  Hibernate Batch Settings: jdbc.batch_size=1000 for bulk operations");
		System.out.println("  🔗 Connection Pooling: Optimized HikariCP settings");
		System.out.println();
		System.out.println("PERFORMANCE BENEFITS:");
		System.out.println("  • Reduced memory consumption with batching");
		System.out.println("  • Fewer database round trips");
		System.out.println("  • Elimination of N+1 query problems");
		System.out.println("  • Optimized SQL generation by Hibernate");
		System.out.println("  • Proper transaction boundaries for consistency");
	}
}
