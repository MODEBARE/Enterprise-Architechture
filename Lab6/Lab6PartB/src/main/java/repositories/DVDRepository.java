package repositories;

import domain.DVD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DVDRepository extends JpaRepository<DVD, Long> {
    
    // Find DVDs by name
    List<DVD> findByName(String name);
    
    // Find DVDs by name containing
    List<DVD> findByNameContaining(String name);
    
    // Find DVDs by genre
    List<DVD> findByGenre(String genre);
    
    // Find DVDs by name and genre
    List<DVD> findByNameAndGenre(String name, String genre);
} 