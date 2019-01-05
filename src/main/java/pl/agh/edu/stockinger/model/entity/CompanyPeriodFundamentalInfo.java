package pl.agh.edu.stockinger.model.entity;

import lombok.Data;
import pl.agh.edu.stockinger.model.entity.CompanyBalance;
import pl.agh.edu.stockinger.model.entity.ProfitAndLoss;

import javax.persistence.*;

@Data
@Entity
public class CompanyPeriodFundamentalInfo {

    @Id
    @Column(name = "PERIOD_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String timePeriod;
    private String companyName;

    @OneToOne
    @JoinTable(
            name = "PERIOD_PROFIT",
            joinColumns = @JoinColumn(name = "PERIOD_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROFIT_ID")
    )
    private ProfitAndLoss profitAndLoss;

    @OneToOne
    @JoinTable(
            name = "PERIOD_BALANCE",
            joinColumns = @JoinColumn(name = "PERIOD_ID"),
            inverseJoinColumns = @JoinColumn(name = "BALANCE_ID")
    )
    private CompanyBalance companyBalance;

    @OneToOne
    @JoinTable(
            name = "PERIOD_INDICATOR",
            joinColumns = @JoinColumn(name = "PERIOD_ID"),
            inverseJoinColumns = @JoinColumn(name = "INDICATOR_ID")
    )
    private CompanyPeriodIndicator companyPeriodIndicator;
}
