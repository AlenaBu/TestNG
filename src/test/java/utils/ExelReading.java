package utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExelReading {

    static Workbook book;
    static Sheet sheet;

    //to open exel book
    public static void openExel(String filePath){

        try {
            FileInputStream fis = new FileInputStream(filePath);
            book=new XSSFWorkbook(fis);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
//to open sheet
    public static void getSheet(String sheetName){
        sheet= book.getSheet(sheetName);
    }
//to get total rows
    public static int getRowCount(){
        return sheet.getPhysicalNumberOfRows();
    }
    //to get total columns
    public static int getColumnCount(int rowIndex){
        return sheet.getRow(rowIndex).getPhysicalNumberOfCells();
    }
    //to collect the data from every cell in form of a String, we use this function
    public static String getCellData(int rowIndex, int colIndex){
        return sheet.getRow(rowIndex).getCell(colIndex).toString();
    }

    public static List<Map<String, String>> excelIntoListMap(String filePath, String sheetName){
        openExel(filePath);
        getSheet(sheetName);

        List<Map<String, String>> ListData = new ArrayList<>();

        //outer loop
        for(int row=1; row<getRowCount(); row++){
            //creating a map for every row
            Map<String,String> map=new LinkedHashMap<>();
            //looping through the values of all cells
            for(int col=0; col<getColumnCount(row); col++){
                map.put(getCellData(0,col),getCellData(row,col));
            }
            //to add the map in List
            ListData.add(map);
        }
        return ListData;

    }
}
