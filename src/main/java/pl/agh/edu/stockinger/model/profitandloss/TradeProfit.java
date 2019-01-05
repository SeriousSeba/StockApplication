package pl.agh.edu.stockinger.model.profitandloss;

import lombok.Data;

@Data
public class TradeProfit {
    private int basicValue;
    private int operationalIncome;
    private int operationalCosts;
}
