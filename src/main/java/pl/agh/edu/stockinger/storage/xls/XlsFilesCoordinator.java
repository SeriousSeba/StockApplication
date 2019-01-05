package pl.agh.edu.stockinger.storage.xls;

import org.apache.commons.io.FileUtils;
import pl.agh.edu.stockinger.exception.MissingQuotationException;

import java.io.File;
import java.io.IOException;

public class XlsFilesCoordinator {
    private static final String FILES_PATH = "C:/Stockinger/fileSystem/xls/";
    private static final String XLS_EXTENSION = ".xls";
    private static final int MIN_SIZE = 10000;

    public boolean doesQuotationDocumentExists(String date){
        File file = new File(FILES_PATH + date + XLS_EXTENSION);
        return file.exists() && file.length() > MIN_SIZE;
    }

    public File getDailyQuotationFile(String date){
        return new File(FILES_PATH + date + XLS_EXTENSION);
    }

    public void downloadQuotationDocument(byte[] data, String date) throws IOException, MissingQuotationException {
        File file = new File(FILES_PATH + date + XLS_EXTENSION);
        FileUtils.writeByteArrayToFile(file, data);
        if(file.length() < MIN_SIZE){
            throw new MissingQuotationException("Quotation not found on GPW archive");
        }
    }

}
