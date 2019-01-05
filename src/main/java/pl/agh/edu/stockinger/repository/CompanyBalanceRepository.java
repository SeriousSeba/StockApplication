package pl.agh.edu.stockinger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.agh.edu.stockinger.model.entity.CompanyBalance;

@Repository
public interface CompanyBalanceRepository extends JpaRepository<CompanyBalance, Long> {
}
