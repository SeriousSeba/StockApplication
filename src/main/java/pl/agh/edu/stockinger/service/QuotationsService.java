package pl.agh.edu.stockinger.service;

import org.springframework.stereotype.Service;
import pl.agh.edu.stockinger.exception.MissingQuotationException;
import pl.agh.edu.stockinger.model.entity.SingleDayQuote;
import pl.agh.edu.stockinger.storage.StorageSupervisor;
import pl.agh.edu.stockinger.storage.xls.XlsStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuotationsService {

    private StorageSupervisor supervisor = new XlsStorage();


    public List<SingleDayQuote> getTodayQuotations() throws IOException, MissingQuotationException {
        return getDailyQuotations(LocalDateTime.now());
    }

    public List<SingleDayQuote> getDailyQuotations(LocalDateTime localDateTime) throws IOException, MissingQuotationException {
        List<SingleDayQuote> result = supervisor.getDailyQuotations(localDateTime);
        return result;
    }

    public List<SingleDayQuote> getRecentQuotations(String companyName, int days) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<SingleDayQuote> result = new ArrayList<>();
        for (int i  = 0; i < days;){
            try {
                result.add(supervisor.getDailyQuotation(localDateTime, companyName));
                i++;
            }  catch (MissingQuotationException missingQuotationException) {}
            localDateTime = localDateTime.minusDays(1);
        }
        return result;
    }

    public List<SingleDayQuote> getLastQuoatations() throws IOException{
        LocalDateTime localDateTime = LocalDateTime.now();
        List<SingleDayQuote> result = null;
        for (int i = 0; i < 31; i++){
            try {
                result = supervisor.getDailyQuotations(localDateTime);
            } catch (MissingQuotationException missingQuotationException) {
                localDateTime = localDateTime.minusDays(1);
            }
        }
        return result;
    }

    public SingleDayQuote getLastQuotation(String companyName) throws IOException, MissingQuotationException {
        LocalDateTime localDateTime = LocalDateTime.now();
        return supervisor.getDailyQuotation(localDateTime, companyName);
    }


}
