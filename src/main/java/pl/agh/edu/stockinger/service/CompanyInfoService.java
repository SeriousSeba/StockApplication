package pl.agh.edu.stockinger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.edu.stockinger.exception.MissingCompanyException;
import pl.agh.edu.stockinger.exception.MissingQuotationException;
import pl.agh.edu.stockinger.model.entity.CompanyBalance;
import pl.agh.edu.stockinger.model.entity.ProfitAndLoss;
import pl.agh.edu.stockinger.repository.CompanyBalanceRepository;
import pl.agh.edu.stockinger.repository.CompanyInfoRepository;
import pl.agh.edu.stockinger.repository.CompanyPeriodFundamentalInfoRepository;
import pl.agh.edu.stockinger.repository.ProfitAndLossRepository;
import pl.agh.edu.stockinger.model.entity.CompanyInfo;
import pl.agh.edu.stockinger.model.entity.CompanyPeriodFundamentalInfo;
import pl.agh.edu.stockinger.storage.html.HtmlParser;
import pl.agh.edu.stockinger.storage.xls.XlsStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class CompanyInfoService {

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private CompanyBalanceRepository companyBalanceRepository;

    @Autowired
    private ProfitAndLossRepository profitAndLossRepository;

    @Autowired
    private CompanyPeriodFundamentalInfoRepository fundamentalInfoRepository;

    private XlsStorage xlsStorage = new XlsStorage();

    HtmlParser htmlParser = new HtmlParser();

    public CompanyInfo getCompanyInfo(String companyName) throws MissingCompanyException, IOException, MissingQuotationException {
        CompanyInfo companyInfo = companyInfoRepository.getByCompanyName(companyName);
        if(companyInfo == null){
            companyInfo = createCompanyInfo(companyName);
        }
        return companyInfo;
    }

    @Transactional
    public CompanyInfo createCompanyInfo(String companyName) throws MissingCompanyException, IOException, MissingQuotationException {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setIsin(xlsStorage.getRecentDailyQuotation(companyName).getCodeISIN());
        List<ProfitAndLoss> profitAndLossList = htmlParser.getFinanceData(companyName);
        List<CompanyBalance> companyBalanceList = htmlParser.getBalanceData(companyName);

        for(int i = 0; i < profitAndLossList.size(); i++){
            ProfitAndLoss profitAndLoss = profitAndLossList.get(i);
            CompanyBalance companyBalance = companyBalanceList.get(i);
            String timePeriod = profitAndLoss.getTimePeriod();
            CompanyPeriodFundamentalInfo fundamentalInfo = new CompanyPeriodFundamentalInfo();
            fundamentalInfo.setTimePeriod(timePeriod);
            fundamentalInfo.setCompanyBalance(companyBalance);
            fundamentalInfo.setProfitAndLoss(profitAndLoss);
            fundamentalInfo.setCompanyName(companyName);

            profitAndLossRepository.save(profitAndLoss);
            companyBalanceRepository.save(companyBalance);
            fundamentalInfoRepository.save(fundamentalInfo);
            companyInfo.getPeriodInfo().add(fundamentalInfo);
        }
        companyInfoRepository.save(companyInfo);
        return companyInfo;
    }

    @PostConstruct
    public void construction(){
        try {
            getCompanyInfo("GUNWO");
        } catch (MissingCompanyException | IOException | MissingQuotationException e) {
            e.printStackTrace();
        }
    }





}
