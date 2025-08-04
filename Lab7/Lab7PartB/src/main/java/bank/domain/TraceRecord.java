package bank.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//mport javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TraceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ✅ Primary key

    private LocalDateTime timestamp;

    private String message;

    // === Constructors ===
    public TraceRecord() {}

    public TraceRecord(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    // === Getters and Setters ===
    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
