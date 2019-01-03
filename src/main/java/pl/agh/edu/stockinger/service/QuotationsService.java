package pl.agh.edu.stockinger.service;

import org.springframework.stereotype.Service;
import pl.agh.edu.stockinger.exception.MissingQuotation;
import pl.agh.edu.stockinger.model.SingleDayQuote;
import pl.agh.edu.stockinger.storage.StorageSupervisor;
import pl.agh.edu.stockinger.storage.XlsStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuotationsService {

    private StorageSupervisor supervisor = new XlsStorage();

    public List<SingleDayQuote> getDailyQuotations(LocalDateTime localDateTime) throws IOException, MissingQuotation {
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
            }  catch (MissingQuotation missingQuotation) {}
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
            } catch (MissingQuotation missingQuotation) {
                localDateTime = localDateTime.minusDays(1);
            }
        }
        return result;
    }


}
