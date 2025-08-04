package Lab4;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    private String isbn;

    private String title;
    private String author;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private Publisher publisher;

    // Getters and setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
}

