package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	

public class readExcel {
	
	public String[] read(String fileName,String sheetName, int numberOfItems) throws IOException
	{
		//Create an object of File class to open xlsx file
		File file = new File("./"+fileName);
		
		//Create an object of FileInputStream class to read excel file
	    FileInputStream inputStream = new FileInputStream(file);
	    Workbook wBook = null;
	    
	    //Find the file extension by splitting file name in substring  and getting only extension name
	    String fileExtensionName = fileName.substring(fileName.indexOf("."));
	    
	  //Check condition if the file is xlsx file

	    if(fileExtensionName.equals(".xlsx")){

	    //If it is xlsx file then create object of XSSFWorkbook class
	    wBook = new XSSFWorkbook(inputStream);

	    }
	    //Check condition if the file is xls file
	    else if(fileExtensionName.equals(".xls")){
	        //If it is xls file then create object of HSSFWorkbook class
	        wBook = new HSSFWorkbook(inputStream);
	    }
	    
	  //Read sheet inside the workbook by its name
	    Sheet sheet = wBook.getSheet(sheetName);
	    
	    //Find number of rows in excel file
	    int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
	    
	  //Create a loop over all the rows of excel file to read it
	    String[] cellValues = new String[numberOfItems];;
	    for (int i = 0; i < rowCount+1 & i<numberOfItems; i++) {
	        Row row = sheet.getRow(i);
	        //Create a loop to print cell values in a row
	        for (int j = 0; j < row.getLastCellNum(); j++) {
	            
	            cellValues[i]=row.getCell(j).getStringCellValue();
	            
	          //Print Excel data in console
	           //System.out.print(cellValues[i]);
	        }
	        //System.out.println();
	    } 
	    return cellValues;
	}
	
/*	public static void main(String[] args) throws IOException
	{
		readExcel obj = new readExcel();
	   	//Call read file method of the class to read data
	    String[] results = obj.read("data.xlsx","AmazonData",2	);
	    
	    for(int i=0;i<results.length;i++){
	    	System.out.println(results[i]);
	    }
	}
*/
}
