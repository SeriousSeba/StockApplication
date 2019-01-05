package pl.agh.edu.stockinger.storage.xls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class XlsDocumentDownloader {
    private static final String XLS_URL_PATTERN = "https://www.gpw.pl/archiwum-notowan?fetch=1&type=10&instrument=&date=";

    public byte[] getDateSpecificDocument(String date) throws IOException {
        URL url = new URL(XLS_URL_PATTERN + date);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = url.openStream ();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        } catch (IOException e) {
            throw new IOException("Error while reading from URL " + url.toString());
        }
        return baos.toByteArray();
    };
}
