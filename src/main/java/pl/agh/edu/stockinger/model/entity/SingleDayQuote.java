package pl.agh.edu.stockinger.model.entity;

import lombok.Data;

@Data
public class SingleDayQuote {
    private String name;
    private String date;
    private String codeISIN;
    private String currency;
    private double openingPrice;
    private double closingPrice;
    private double maxPrice;
    private double minPrice;
    private double dailyPriceChange;
    private double flow;
    private int volume;
    private int transactionsNumber;
}
