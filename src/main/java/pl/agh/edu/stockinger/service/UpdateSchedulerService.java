package pl.agh.edu.stockinger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.agh.edu.stockinger.exception.MissingQuotationException;

import java.io.IOException;

@Service
public class UpdateSchedulerService {

    private final Logger logger = LoggerFactory.getLogger(UpdateSchedulerService.class);
    private final QuotationsService quotationsService;
    private final CompanyInfoService companyInfoService;

    @Autowired
    public UpdateSchedulerService(QuotationsService quotationsService, CompanyInfoService companyInfoService) {
        this.quotationsService = quotationsService;
        this.companyInfoService = companyInfoService;
    }

    @Scheduled(cron = "0 10 17 * * *")
    public void updateDailyQuotation(){
        try {
            quotationsService.getTodayQuotations();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Getting daily quotations failed");
        } catch (MissingQuotationException e) {
            e.printStackTrace();
            logger.warn("No quotations available at GPW");
        }
    }

    @Scheduled(cron = "30 03 01 */3 * *")
    public void updateCompanyInfo(){

    }




}
