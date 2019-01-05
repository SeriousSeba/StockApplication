package pl.agh.edu.stockinger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.edu.stockinger.exception.MissingCompanyException;
import pl.agh.edu.stockinger.exception.MissingQuotationException;
import pl.agh.edu.stockinger.repository.CompanyBalanceRepository;
import pl.agh.edu.stockinger.repository.CompanyInfoRepository;
import pl.agh.edu.stockinger.repository.CompanyPeriodFundamentalInfoRepository;
import pl.agh.edu.stockinger.repository.ProfitAndLossRepository;
import pl.agh.edu.stockinger.model.entity.CompanyInfo;
import pl.agh.edu.stockinger.model.entity.CompanyPeriodFundamentalInfo;
import pl.agh.edu.stockinger.storage.html.HtmlFinancialRaportsSupervisor;
import pl.agh.edu.stockinger.storage.xls.XlsStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class CompanyInfoService {

    private final Logger logger = LoggerFactory.getLogger(CompanyInfoService.class);
    private final CompanyInfoRepository companyInfoRepository;
    private final CompanyBalanceRepository companyBalanceRepository;
    private final ProfitAndLossRepository profitAndLossRepository;
    private final CompanyPeriodFundamentalInfoRepository fundamentalInfoRepository;
    private XlsStorage xlsStorage = new XlsStorage();
    private HtmlFinancialRaportsSupervisor raportsSupervisor = new HtmlFinancialRaportsSupervisor();

    @Autowired
    public CompanyInfoService(CompanyInfoRepository companyInfoRepository, CompanyBalanceRepository companyBalanceRepository, ProfitAndLossRepository profitAndLossRepository, CompanyPeriodFundamentalInfoRepository fundamentalInfoRepository) {
        this.companyInfoRepository = companyInfoRepository;
        this.companyBalanceRepository = companyBalanceRepository;
        this.profitAndLossRepository = profitAndLossRepository;
        this.fundamentalInfoRepository = fundamentalInfoRepository;
    }

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
        companyInfo.setCompanyName(companyName);
        List<CompanyPeriodFundamentalInfo> fundamentalInfos = raportsSupervisor.getCompanyFundamentalInfos(companyInfo);
        fundamentalInfos.forEach(
                companyPeriodFundamentalInfo -> {
                    saveFundamentalInfo(companyPeriodFundamentalInfo);
                    companyInfo.getPeriodInfo().add(companyPeriodFundamentalInfo);
                }
        );
        companyInfoRepository.save(companyInfo);
        calculateCompanyIndicators(companyInfo);
        return companyInfo;
    }

    public void updateAllCompanyInfos(){
        List<CompanyInfo> companyInfos = companyInfoRepository.findAll();
        companyInfos.stream().forEach(
                companyInfo -> {
                    try {
                        updateCompanyInfo(companyInfo.getCompanyName());
                    } catch (MissingCompanyException e) {
                        e.printStackTrace();
                        logger.error(String.format("Company with name %s does not exists", companyInfo.getCompanyName()));
                    }
                }
        );
    }

    @Transactional
    public void updateCompanyInfo(String companyName) throws MissingCompanyException {
        CompanyInfo companyInfo = companyInfoRepository.getByCompanyName(companyName);
        if(companyInfo == null){
            handleCompanyDownload(companyName);
            calculateCompanyIndicators(companyInfo);
        } else {
            if(!raportsSupervisor.isCompanyInfoUpdated(companyInfo)){
                companyInfoUpdateProcedure(companyInfo);
            }
        }
    }

    private void companyInfoUpdateProcedure(CompanyInfo companyInfo) throws MissingCompanyException {
        List<CompanyPeriodFundamentalInfo> fundamentalInfos =
                raportsSupervisor.getCompanyFundamentalInfos(companyInfo);
        companyInfo.getPeriodInfo().clear();
        fundamentalInfos.forEach(
                companyPeriodFundamentalInfo -> {
                    saveFundamentalInfo(companyPeriodFundamentalInfo);
                    companyInfo.getPeriodInfo().add(companyPeriodFundamentalInfo);
                }
        );
        calculateCompanyIndicators(companyInfo);
    }

    private void saveFundamentalInfo(CompanyPeriodFundamentalInfo companyPeriodFundamentalInfo){
        profitAndLossRepository.save(companyPeriodFundamentalInfo.getProfitAndLoss());
        companyBalanceRepository.save(companyPeriodFundamentalInfo.getCompanyBalance());
        fundamentalInfoRepository.save(companyPeriodFundamentalInfo);
    }

    private void calculateCompanyIndicators(CompanyInfo companyInfo) {
        //TODO
    }

    private void handleCompanyDownload(String companyName){
        logger.info(String.format("Company with name %s not found, downloading", companyName));
        try {
            createCompanyInfo(companyName);
        } catch (MissingCompanyException e) {
            logger.warn(String.format("No company found with name %s", companyName));
        } catch (IOException e) {
            logger.error(String.format("Error while downloading informations for company %s", companyName));
        } catch (MissingQuotationException e) {
            logger.warn(String.format("Company %s does not exists in GPW archive", companyName));
        }
    }

    @PostConstruct
    public void construction(){
        try {
            getCompanyInfo("11BIT");
        } catch (MissingCompanyException | IOException | MissingQuotationException e) {
            e.printStackTrace();
        }
    }





}
