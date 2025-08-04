package repositories;

import domain.CD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDRepository extends JpaRepository<CD, Long>, JpaSpecificationExecutor<CD> {
    
    // ======= METHOD NAME QUERIES =======
    // Query 2: Give all CD's from U2 with a price smaller than 10 euro
    List<CD> findByArtistAndPriceLessThan(String artist, double price);
    
    // Alternative method names for the same query
    List<CD> findByArtistEqualsAndPriceLessThan(String artist, double price);
    
    // More specific method if you want to find by artist containing "U2"
    List<CD> findByArtistContainingAndPriceLessThan(String artist, double price);
    
    // ======= NAMED QUERIES (defined in entity) =======
    // Named query: Give all CD's from a certain artist
    @Query("SELECT cd FROM CD cd WHERE cd.artist = :artist")
    List<CD> findCDsByArtistNamed(@Param("artist") String artist);
    
    // ======= JPQL QUERIES WITH @Query =======
    // JPQL: Give all CD's from a certain artist with a price bigger than a certain amount
    @Query("SELECT cd FROM CD cd WHERE cd.artist = :artist AND cd.price > :amount")
    List<CD> findCDsByArtistAndPriceGreaterThan(@Param("artist") String artist, @Param("amount") double amount);
    
    // ======= NATIVE QUERIES =======
    // Native query: Give all CD's from U2
    @Query(value = "SELECT p.id, p.name, p.price, p.product_number, c.artist " +
                  "FROM product p " +
                  "INNER JOIN cd c ON p.id = c.id " +
                  "WHERE c.artist = 'U2'", 
           nativeQuery = true)
    List<Object[]> findU2CDsNative();
    
    // Native query: Give all CD's from a certain artist
    @Query(value = "SELECT p.id, p.name, p.price, p.product_number, c.artist " +
                  "FROM product p " +
                  "INNER JOIN cd c ON p.id = c.id " +
                  "WHERE c.artist = ?1", 
           nativeQuery = true)
    List<Object[]> findCDsByArtistNative(String artist);
} 