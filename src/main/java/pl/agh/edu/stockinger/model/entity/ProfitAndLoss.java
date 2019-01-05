package pl.agh.edu.stockinger.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ProfitAndLoss {

    @Id
    @Column(name = "PROFIT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String timePeriod;
    private int tradeIncome;
    private int tradeProfit;
    private int operationalProfit;
    private int economicActivityProfit;
    private int beforeTaxationProfit;
    private int netProfit;
    private int dominantShareholderNetProfit;
}
