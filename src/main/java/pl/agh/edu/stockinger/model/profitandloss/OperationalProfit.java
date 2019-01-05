package pl.agh.edu.stockinger.model.profitandloss;

import lombok.Data;

@Data
public class OperationalProfit {
    private int basicValue;
    private int financialIncomes;
    private int financialCosts;
    private int otherIncomes;
}
