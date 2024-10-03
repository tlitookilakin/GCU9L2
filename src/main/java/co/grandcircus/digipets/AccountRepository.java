package co.grandcircus.digipets;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Long>{

    Account findByApiKey(String apiKey);

    Account addCreditToId(Long id, Integer credits);
}
