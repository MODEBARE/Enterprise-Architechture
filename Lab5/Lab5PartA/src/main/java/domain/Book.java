package domain;

import javax.persistence.*;

@Entity
public class Book extends Product {
    
    private String isbn;
    
    public Book() {
        super();
    }
    
    public Book(String productNumber, String name, double price, String isbn) {
        super(productNumber, name, price);
        this.isbn = isbn;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
} 