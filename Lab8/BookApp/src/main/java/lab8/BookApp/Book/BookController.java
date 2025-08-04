package lab8.BookApp.Book;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository repo = new BookRepository();

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        try {
            repo.add(book);
            return ResponseEntity.ok("Book added");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    public String updateBook(@RequestBody Book book) {
        repo.update(book);
        return "Book updated";
    }

    @DeleteMapping("/{isbn}")
    public String deleteBook(@PathVariable String isbn) {
        repo.delete(isbn);
        return "Book deleted";
    }

    @GetMapping("/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        return repo.get(isbn);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return repo.getAll();
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String author) {
        return repo.searchByAuthor(author);
    }
}
