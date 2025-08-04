package domain;

import javax.persistence.*;

@Entity
public class DVD extends Product {
    
    private String genre;
    
    public DVD() {
        super();
    }
    
    public DVD(String productNumber, String name, double price, String genre) {
        super(productNumber, name, price);
        this.genre = genre;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
} 