package pl.agh.edu.stockinger.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CompanyInfo {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMPANY_ID")
    private long id;

    @Id
    private String isin;
    private String companyName;

    @OneToMany
    @JoinTable(
            name = "COMPANY_PERIOD",
            joinColumns = @JoinColumn(name = "COMPANY_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERIOD_ID")
    )
    private List<CompanyPeriodFundamentalInfo> periodInfo = new ArrayList<>();
}
