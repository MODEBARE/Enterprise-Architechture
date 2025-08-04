package domain;

import javax.persistence.*;

@Entity
public class CD extends Product {
    
    private String artist;
    
    public CD() {
        super();
    }
    
    public CD(String productNumber, String name, double price, String artist) {
        super(productNumber, name, price);
        this.artist = artist;
    }
    
    public String getArtist() {
        return artist;
    }
    
    public void setArtist(String artist) {
        this.artist = artist;
    }
} 