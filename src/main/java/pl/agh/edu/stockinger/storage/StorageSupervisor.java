package pl.agh.edu.stockinger.storage;

import pl.agh.edu.stockinger.exception.MissingQuotation;
import pl.agh.edu.stockinger.model.SingleDayQuote;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface StorageSupervisor {
     SingleDayQuote getDailyQuotation(LocalDateTime dateTime, String name) throws IOException, MissingQuotation;
     List<SingleDayQuote> getDailyQuotations(LocalDateTime dateTime) throws IOException, MissingQuotation;
}
