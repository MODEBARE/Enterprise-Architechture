package Lab4;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Flight {
    @Id
    private Long id;

    private String flightnumber;

    @Column(name = "from_city")
    private String from;

    @Column(name = "to_city")
    private String to;

    private LocalDate date;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightnumber() { return flightnumber; }
    public void setFlightnumber(String flightnumber) { this.flightnumber = flightnumber; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}

