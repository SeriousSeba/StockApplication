package pl.agh.edu.stockinger.storage.html;


import pl.agh.edu.stockinger.exception.MissingCompanyException;
import pl.agh.edu.stockinger.model.entity.CompanyBalance;
import pl.agh.edu.stockinger.model.entity.CompanyInfo;
import pl.agh.edu.stockinger.model.entity.CompanyPeriodFundamentalInfo;
import pl.agh.edu.stockinger.model.entity.ProfitAndLoss;

import java.util.ArrayList;
import java.util.List;

public class HtmlFinancialRaportsSupervisor {

    private HtmlParser htmlParser = new HtmlParser();

    public boolean isCompanyInfoUpdated(CompanyInfo companyInfo) throws MissingCompanyException {
        List<String> timePeriods = htmlParser.getTimePeriods(
                htmlParser.getTableFromDocument(
                        htmlParser.getCompanyFlowDocument(companyInfo.getCompanyName())
                )
        );
        return timePeriods.size() == companyInfo.getPeriodInfo().size();
    }

    public List<CompanyPeriodFundamentalInfo> getCompanyFundamentalInfos(CompanyInfo companyInfo) throws MissingCompanyException {
        String companyName = companyInfo.getCompanyName();
        List<ProfitAndLoss> profitAndLossList = htmlParser.getFinanceData(
                htmlParser.getCompanyFlowDocument(companyName)
        );
        List<CompanyBalance> companyBalanceList = htmlParser.getBalanceData(
                htmlParser.getCompanyBalanceDocument(companyName)
        );

        List<CompanyPeriodFundamentalInfo> result = new ArrayList<>();

        for(int i = 0; i < profitAndLossList.size(); i++){
            CompanyPeriodFundamentalInfo fundamentalInfo = createFundamentalInfo(
                    profitAndLossList.get(i), companyBalanceList.get(i), companyName
            );
            result.add(fundamentalInfo);
        }
        return result;
    }

    private CompanyPeriodFundamentalInfo createFundamentalInfo(ProfitAndLoss profitAndLoss, CompanyBalance companyBalance, String companyName){
        String timePeriod = profitAndLoss.getTimePeriod();
        CompanyPeriodFundamentalInfo fundamentalInfo = new CompanyPeriodFundamentalInfo();
        fundamentalInfo.setTimePeriod(timePeriod);
        fundamentalInfo.setCompanyBalance(companyBalance);
        fundamentalInfo.setProfitAndLoss(profitAndLoss);
        fundamentalInfo.setCompanyName(companyName);
        return fundamentalInfo;
    }
}
