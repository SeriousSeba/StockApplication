package pl.agh.edu.stockinger.storage;

import org.apache.commons.io.FileUtils;
import pl.agh.edu.stockinger.exception.MissingQuotation;

import java.io.File;
import java.io.IOException;

public class XlsFilesCoordinator {
    private static final String FILES_PATH = "C:/Stockinger/fileSystem/xls/";
    private static final String XLS_EXTENSION = ".xls";
    private static final int MIN_SIZE = 10000;

    public boolean doesQuotationDocumentExists(String date){
        return new File(FILES_PATH + date + XLS_EXTENSION).exists();
    }

    public File getDailyQuatationFile(String date){
        return new File(FILES_PATH + date + XLS_EXTENSION);
    }

    public void downloadQuotationDocument(byte[] data, String date) throws IOException, MissingQuotation {
        File file = new File(FILES_PATH + date + XLS_EXTENSION);
        FileUtils.writeByteArrayToFile(file, data);
        if(file.length() < 10000){
            FileUtils.forceDelete(file);
            throw new MissingQuotation("Quotation not found on GPW archive");
        }
    }

}
