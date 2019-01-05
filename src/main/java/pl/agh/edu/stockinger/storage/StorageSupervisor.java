package pl.agh.edu.stockinger.storage;

import pl.agh.edu.stockinger.exception.MissingQuotationException;
import pl.agh.edu.stockinger.model.entity.SingleDayQuote;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface StorageSupervisor {
     SingleDayQuote getDailyQuotation(LocalDateTime dateTime, String name) throws IOException, MissingQuotationException;
     List<SingleDayQuote> getDailyQuotations(LocalDateTime dateTime) throws IOException, MissingQuotationException;

     default SingleDayQuote getRecentDailyQuotation(String name) throws IOException, MissingQuotationException {
          LocalDateTime localDateTime = LocalDateTime.now();
          for(int i = 0; i < 14; i++) {
               try {
                    return getDailyQuotation(localDateTime, name);
               } catch (MissingQuotationException e) {
                    localDateTime = localDateTime.minusDays(1);
               }
          }
          throw new MissingQuotationException("No quotation found for this request");
     }
}
