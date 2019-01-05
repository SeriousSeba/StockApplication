package pl.agh.edu.stockinger.storage.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.agh.edu.stockinger.exception.MissingQuotationException;
import pl.agh.edu.stockinger.model.entity.SingleDayQuote;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class XlsDocumentParser {

    public SingleDayQuote getCompanyQuotation(String company, File document) throws IOException, MissingQuotationException {
        Workbook workbook = new HSSFWorkbook(new FileInputStream(document));
        Sheet sheet = workbook.getSheetAt(0);
        AtomicReference<Row> result = new AtomicReference<>();
        sheet.forEach(
                row -> {
                    if(row.getCell(1).getStringCellValue().equals(company)){
                        result.set(row);
                    }
                }
        );
        if(result.get() != null){
            return createSingleDayQuote(result.get());
        } else {
            throw new MissingQuotationException("No quotation for that company on day " + document.getName());
        }
    }

    public List<SingleDayQuote> getDailyQuoataions(File document) throws IOException {
        Workbook workbook = new HSSFWorkbook(new FileInputStream(document));
        Sheet sheet = workbook.getSheetAt(0);
        List<SingleDayQuote> result = new ArrayList<>();
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        while (iterator.hasNext()) {
            result.add(createSingleDayQuote(iterator.next()));
        }
        return result;
    }

    private SingleDayQuote createSingleDayQuote(Row row) {
        final Iterator<Cell> iterator = row.cellIterator();
        SingleDayQuote singleDayQuote = new SingleDayQuote();
        singleDayQuote.setDate(getStringValue(iterator));
        singleDayQuote.setName(getStringValue(iterator));
        singleDayQuote.setCodeISIN(getStringValue(iterator));
        singleDayQuote.setCurrency(getStringValue(iterator));
        singleDayQuote.setOpeningPrice(getNumericalValue(iterator));
        singleDayQuote.setMaxPrice(getNumericalValue(iterator));
        singleDayQuote.setMinPrice(getNumericalValue(iterator));
        singleDayQuote.setClosingPrice(getNumericalValue(iterator));
        singleDayQuote.setDailyPriceChange(getNumericalValue(iterator));
        singleDayQuote.setVolume((int) getNumericalValue(iterator));
        singleDayQuote.setTransactionsNumber((int) getNumericalValue(iterator));
        singleDayQuote.setFlow(getNumericalValue(iterator));
        return singleDayQuote;
    }

    private String getStringValue(Iterator<Cell> iterator){
        Cell cell = iterator.next();
        switch (cell.getCellType()){
            case _NONE: return "";
            case NUMERIC: return  Double.toString(cell.getNumericCellValue());
            case STRING:  return cell.getStringCellValue();
            default: return "";
        }
    }

    private double getNumericalValue(Iterator<Cell> iterator){
        Cell cell = iterator.next();
        switch (cell.getCellType()){
            case NUMERIC: return cell.getNumericCellValue();
            case STRING: return Double.parseDouble(cell.getStringCellValue());
            default: return 0;
        }
    }

}
