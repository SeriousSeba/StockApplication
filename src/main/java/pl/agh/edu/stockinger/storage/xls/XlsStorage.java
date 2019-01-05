package pl.agh.edu.stockinger.storage.xls;

import pl.agh.edu.stockinger.exception.MissingQuotationException;
import pl.agh.edu.stockinger.model.entity.SingleDayQuote;
import pl.agh.edu.stockinger.storage.StorageSupervisor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class XlsStorage implements StorageSupervisor {
    private XlsDocumentDownloader xlsDocumentDownloader = new XlsDocumentDownloader();
    private XlsFilesCoordinator xlsFilesCoordinator = new XlsFilesCoordinator();
    private XlsDocumentParser xlsDocumentParser = new XlsDocumentParser();

    @Override
    public SingleDayQuote getDailyQuotation(LocalDateTime dateTime, String name) throws IOException, MissingQuotationException {
        String date = convertDate(dateTime);
        checkIfQuoatationsExists(date);
        return xlsDocumentParser.getCompanyQuotation(name, xlsFilesCoordinator.getDailyQuotationFile(date));
    }

    @Override
    public List<SingleDayQuote> getDailyQuotations(LocalDateTime dateTime) throws IOException, MissingQuotationException {
        String date = convertDate(dateTime);
        checkIfQuoatationsExists(date);
        return xlsDocumentParser.getDailyQuoataions(xlsFilesCoordinator.getDailyQuotationFile(date));
    }

    private String convertDate(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(localDateTime);
    }

    private void checkIfQuoatationsExists(String date) throws IOException, MissingQuotationException {
        if(!xlsFilesCoordinator.doesQuotationDocumentExists(date)){
            xlsFilesCoordinator.downloadQuotationDocument(
                   xlsDocumentDownloader.getDateSpecificDocument(date), date
            );
        }
    }

}
