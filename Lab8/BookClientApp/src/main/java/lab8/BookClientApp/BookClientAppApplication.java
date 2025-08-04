package lab8.BookClientApp;

import lab8.BookClientApp.Book.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;

@SpringBootApplication
public class BookClientAppApplication implements CommandLineRunner {

	private final String BASE_URL = "http://localhost:8080/api/books";
	private final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(BookClientAppApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Running Book Client...");

		// 1. Create a new book
		Book book = new Book("9090", "Ariyibi Elijah", "RestTemplate Mastery", 29.99);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Book> request = new HttpEntity<>(book, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);
		System.out.println("POST Response: " + response.getBody());

		// 2. List all books
		ResponseEntity<Book[]> allBooks = restTemplate.getForEntity(BASE_URL, Book[].class);
		System.out.println("GET ALL BOOKS:");
		Arrays.stream(allBooks.getBody()).forEach(System.out::println);

		// 3. Get one book by ISBN
		Book fetched = restTemplate.getForObject(BASE_URL + "/9090", Book.class);
		System.out.println("GET ONE BOOK: " + fetched);

		// 4. Update the book
		book.setPrice(34.99);
		restTemplate.put(BASE_URL, book);
		System.out.println("PUT (Update) done.");

		// 5. Delete the book
		restTemplate.delete(BASE_URL + "/9090");
		System.out.println("DELETE done.");
	}
}

