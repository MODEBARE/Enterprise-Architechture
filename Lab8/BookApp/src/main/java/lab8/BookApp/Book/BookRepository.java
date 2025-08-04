package lab8.BookApp.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepository {
    private static final Map<String, Book> books = new HashMap<>();

    public void add(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new RuntimeException("Book already exists");
        }
        books.put(book.getIsbn(), book);
    }

    public void update(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void delete(String isbn) {
        books.remove(isbn);
    }

    public Book get(String isbn) {
        return books.get(isbn);
    }

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public List<Book> searchByAuthor(String author) {
        return books.values().stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .toList();
    }
}
