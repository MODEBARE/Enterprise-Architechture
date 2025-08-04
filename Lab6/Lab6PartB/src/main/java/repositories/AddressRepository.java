package repositories;

import domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    // ======= METHOD NAME QUERIES =======
    List<Address> findByCity(String city);
    List<Address> findByCountry(String country);
    
    // ======= NATIVE QUERIES =======
    // Native query: Give all addresses in Amsterdam
    @Query(value = "SELECT * FROM address WHERE city = 'Amsterdam'", nativeQuery = true)
    List<Address> findAddressesInAmsterdamNative();
    
    // Native query: Give all addresses in a certain city
    @Query(value = "SELECT * FROM address WHERE city = ?1", nativeQuery = true)
    List<Address> findAddressesByCityNative(String city);
} 