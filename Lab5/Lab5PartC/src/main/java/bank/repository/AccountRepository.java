package bank.repository;

import bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.entryList LEFT JOIN FETCH a.customer WHERE a.accountnumber = :accountNumber")
    Optional<Account> findByAccountnumberWithEntries(@Param("accountNumber") Long accountNumber);
    
    Optional<Account> findByAccountnumber(Long accountNumber);
} 