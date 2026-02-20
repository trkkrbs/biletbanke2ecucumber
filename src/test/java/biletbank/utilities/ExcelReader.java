package biletbank.utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    Workbook workbook;
    Sheet sheet;
    //Bu classtan object olusturmamizin amaci excel dosyasini okumaktir
    public ExcelReader(String dosyayolu, String sayfaIsmi){
        try {
            FileInputStream fis = new FileInputStream(dosyayolu);
            workbook=WorkbookFactory.create(fis);
            sheet=workbook.getSheet(sayfaIsmi);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Satir ve sutun indexlerini girerek ilgili cell datasini string olarak return eder
    public String getCellData(int satir, int sutun){
        Cell cell = sheet.getRow(satir).getCell(sutun);
        return cell.toString();
    }
    public int rowCount(){
        return sheet.getLastRowNum();
    }

    public java.util.List<java.util.Map<String, String>> getDataList() {
        java.util.List<java.util.Map<String, String>> data = new java.util.ArrayList<>();

        // İlk satırı (Header) sütun isimleri olarak alıyoruz
        org.apache.poi.ss.usermodel.Row headerRow = sheet.getRow(0);
        java.util.List<String> columns = new java.util.ArrayList<>();
        for (Cell cell : headerRow) {
            columns.add(cell.toString());
        }

        // Diğer tüm satırları dönüp Map'e ekliyoruz
        for (int i = 1; i <= rowCount(); i++) {
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
            if (row == null) continue;

            java.util.Map<String, String> rowMap = new java.util.LinkedHashMap<>();
            for (int j = 0; j < columns.size(); j++) {
                Cell cell = row.getCell(j);
                rowMap.put(columns.get(j), cell == null ? "" : cell.toString());
            }
            data.add(rowMap);
        }
        return data;
    }
}