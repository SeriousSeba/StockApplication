package pl.agh.edu.stockinger.model.profitandloss;

import lombok.Data;

@Data
public class TradeIncome {
    private int basicValue;
    private int technicalProductionCost;
    private int sellCost;
    private int maintenanceCost;
}
