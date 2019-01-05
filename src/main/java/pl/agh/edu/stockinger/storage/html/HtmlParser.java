package pl.agh.edu.stockinger.storage.html;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.agh.edu.stockinger.exception.MissingCompanyException;
import pl.agh.edu.stockinger.model.entity.CompanyBalance;
import pl.agh.edu.stockinger.model.entity.ProfitAndLoss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HtmlParser {
        private static final String FLOW_URL_ADDRESS = "https://www.biznesradar.pl/raporty-finansowe-rachunek-zyskow-i-strat/";
        private static final String BALANCE_URL_ADDRESS  = "https://www.biznesradar.pl/raporty-finansowe-bilans/";

        private static final String TABLE_CLASS = "report-table";

        public List<ProfitAndLoss> getFinanceData(Document document) throws MissingCompanyException {
                Elements table = getTableFromDocument(document);
                List<String> timePeriods = getTimePeriods(table);
                Elements rows = table.get(0).select("tr[data-field]");
                List<ProfitAndLoss> profitsAndLoss = getProfitsAndLoss(rows, timePeriods);
                return profitsAndLoss;
        }

        public List<CompanyBalance> getBalanceData(Document document) throws MissingCompanyException {
                Elements table = getTableFromDocument(document);
                List<String> timePeriods = getTimePeriods(table);
                Elements rows = table.get(0).select("tr[data-field]");
                List<CompanyBalance> companyBalance = getCompanyBalance(rows, timePeriods);
                return companyBalance;
        }

        public List<String> getTimePeriods(Elements table){
                Elements periodColumns = table.get(0).getElementsByClass("thq h");
                periodColumns.addAll(table.get(0).getElementsByClass("thq h newest"));
                List<String> periods = new ArrayList<>();
                periodColumns.forEach(
                        element -> periods.add(element.text())
                );
                return periods;
        }

        public Elements getTableFromDocument(Document document){
                return document.getElementsByClass(TABLE_CLASS);
        }

        public Document getCompanyBalanceDocument(String companyName) throws MissingCompanyException {
               return getCompanyDocument(BALANCE_URL_ADDRESS, companyName);
        }

        public Document getCompanyFlowDocument(String companyName) throws MissingCompanyException {
                return getCompanyDocument(FLOW_URL_ADDRESS, companyName);
        }

        private Document getCompanyDocument(String url, String companyName) throws MissingCompanyException {
                Document document;
                try {
                        document = Jsoup.connect(url + companyName).followRedirects(true).get();
                        return document;
                } catch (IOException e) {
                        e.printStackTrace();
                        throw new MissingCompanyException("No company found with that name");
                }
        }

        private List<ProfitAndLoss> getProfitsAndLoss(Elements dataRows, List<String> timePeriods){
                List<ProfitAndLoss> result = new ArrayList<>();
                AtomicInteger counter = new AtomicInteger();
                timePeriods.stream().forEach(
                        timePeriod -> {
                                int column = counter.get();
                                ProfitAndLoss profitAndLoss = parseFinancialTable(dataRows, column);
                                profitAndLoss.setTimePeriod(timePeriod);
                                result.add(profitAndLoss);
                                counter.getAndIncrement();
                        }
                );
                return result;
        }

        private List<CompanyBalance> getCompanyBalance(Elements elements, List<String> timePeriods) {
                List<CompanyBalance> result = new ArrayList<>();
                AtomicInteger counter = new AtomicInteger();
                timePeriods.stream().forEach(
                        timePeriod -> {
                                int column = counter.get();
                                CompanyBalance companyBalance = parseBalanceTable(elements, column);
                                companyBalance.setTimePeriod(timePeriod);
                                result.add(companyBalance);
                                counter.incrementAndGet();
                        }
                );
                return result;
        }

        private ProfitAndLoss parseFinancialTable(Elements rows, int column){
                ProfitAndLoss profitAndLoss = new ProfitAndLoss();
                profitAndLoss.setTradeIncome(Integer.valueOf(
                                        getValueFromRow(rows.get(0), column)
                                ));
                profitAndLoss.setTradeProfit(Integer.valueOf(
                        getValueFromRow(rows.get(4), column)
                ));
                profitAndLoss.setOperationalProfit(Integer.valueOf(
                        getValueFromRow(rows.get(7), column)
                ));
                profitAndLoss.setOperationalProfit(Integer.valueOf(
                        getValueFromRow(rows.get(11), column)
                ));
                profitAndLoss.setBeforeTaxationProfit(Integer.valueOf(
                        getValueFromRow(rows.get(13), column)
                ));
                profitAndLoss.setNetProfit(Integer.valueOf(
                        getValueFromRow(rows.get(15), column)
                ));
                profitAndLoss.setDominantShareholderNetProfit(Integer.valueOf(
                        getValueFromRow(rows.get(16), column)
                ));
                return profitAndLoss;
        }

        private CompanyBalance parseBalanceTable(Elements rows, int column) {
                CompanyBalance companyBalance = new CompanyBalance();
                companyBalance.setNonCurrentAssets(Integer.valueOf(
                        getValueFromRow(rows.get(0), column)
                ));
                companyBalance.setCurrentAssets(Integer.valueOf(
                        getValueFromRow(rows.get(6), column)
                ));
                companyBalance.setTotalAssets(Integer.valueOf(
                        getValueFromRow(rows.get(13), column)
                ));
                companyBalance.setDominantCapital(Integer.valueOf(
                        getValueFromRow(rows.get(14), column)
                ));
                companyBalance.setNonShareCapital(Integer.valueOf(
                        getValueFromRow(rows.get(18), column)
                ));
                companyBalance.setNonCurrentLiabilities(Integer.valueOf(
                        getValueFromRow(rows.get(19), column)
                ));
                companyBalance.setCurrentLiabilities(Integer.valueOf(
                        getValueFromRow(rows.get(25), column)
                ));
                companyBalance.setTotalEquityAndLiability(Integer.valueOf(
                        getValueFromRow(rows.get(32), column)
                ));
                return companyBalance;
        }

        private String getValueFromRow(Element row, int column){
                Element columnElement = row.select(".h").get(column);
                return columnElement.select(".pv").get(0).
                        getAllElements().get(0).text().replace(" ", "");
        }

}
