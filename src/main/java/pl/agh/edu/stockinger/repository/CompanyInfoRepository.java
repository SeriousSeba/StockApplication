package pl.agh.edu.stockinger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.agh.edu.stockinger.model.entity.CompanyInfo;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
    CompanyInfo getById(long id);
    CompanyInfo getByCompanyName(String companyName);
    CompanyInfo getByIsin(String isin);
}
