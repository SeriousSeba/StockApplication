package pl.agh.edu.stockinger.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CompanyBalance {

    @Id
    @Column(name = "BALANCE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String timePeriod;

    private int nonCurrentAssets;
    private int currentAssets;
    private int totalAssets;
    private int dominantCapital;
    private int nonShareCapital;
    private int nonCurrentLiabilities;
    private int currentLiabilities;
    private int totalEquityAndLiability;
}
