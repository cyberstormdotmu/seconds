package excelComparator;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class ExcelComparatorForMemSysCount {

	public static HSSFSheet file1;
	public static HSSFSheet file2 ;
	public static HSSFSheet outputSheet;
	public static HSSFCellStyle style;
	public static int lastRowNumber;
	public static CellStyle errorCellStyle;

	// Compare Two Sheets
	public static boolean compareTwoSheets(HSSFSheet file1, HSSFSheet file2,HSSFWorkbook resultWorkbook, String resultFile) {

		lastRowNumber = file1.getLastRowNum();
		HSSFRow outRow;
		boolean equalSheets = true;
		outputSheet = resultWorkbook.getSheetAt(0);
		outputSheet.setColumnWidth(8, 15000);
		outputSheet.setColumnWidth(2, 8000);
		outputSheet.setColumnWidth(3, 12000);
		outputSheet.setColumnWidth(4, 5500);
		outRow = outputSheet.createRow(0);

		style = resultWorkbook.createCellStyle(); // Style Creation for Worksheet
		Font font = resultWorkbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setWrapText(true);

		errorCellStyle = resultWorkbook.createCellStyle();
		errorCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		errorCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 	

		String []headers=new String[]{"CountType","Identifier","System","Package", "Type", "Previous", "Current"," ","Error log"};

		for(int i=0; i<headers.length; i++ ) { 
			outRow.createCell(i).setCellValue(headers[i]);
			outRow.getCell(i).setCellStyle(style);
		}

		for(int i=1; i <=lastRowNumber; i++) {      
			HSSFRow row1 = file1.getRow(i);
			HSSFRow row2 = file2.getRow(i);
			outRow = outputSheet.createRow(i);

			if(!compareTwoRows(row1, row2,outRow,resultWorkbook)) {
				equalSheets = false;

			} else {
				try{
					File file=new File(resultFile);
					FileOutputStream fileOut = new FileOutputStream(file);
					resultWorkbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
				}catch(Exception e){
					System.out.println("Input Output Error :"+e);
					e.printStackTrace();
				}
			} 
		}
		return equalSheets;
	}

	/* Compare two rows 
	 * 1. row1 : row from file 1 (left sheet ), cell1 : cell of row1
	 * 2. row2 : row from file 2 (right sheet), cell2 : cell of row2 
	 * 3. outRow: row from result sheet, outCell : cell of outRow
	 * 4. outSheet : result Sheet of resultWorkbook 
	 */
	public static boolean compareTwoRows(HSSFRow row1, HSSFRow row2, HSSFRow outRow,HSSFWorkbook resultWorkbook) {

		boolean equalRows = true;
		String error = "";
		HSSFCell outcell=null;
		HSSFCell outcellError=outRow.createCell(8);
		int countcell2 = 0;

		CellStyle styleHeader = resultWorkbook.createCellStyle();			

		for( int i=0; i <= row1.getLastCellNum()-1; i++)
		{
			HSSFCell cell1 = row1.getCell(i,Row.RETURN_BLANK_AS_NULL);
			HSSFCell cell2 = row2.getCell(i,Row.RETURN_BLANK_AS_NULL);
			outcell = outRow.createCell(i);
			int rowIndex= row2.getRowNum();
			rowIndex= rowIndex+1;  // header contains 0th row, so actual rows start from +1
			HSSFCell ColName = outputSheet.getRow(0).getCell(i);

			if(row1.getRowNum()<6){          // Header Rows are created 
				styleHeader.setWrapText(false);
				Font fontHeader = resultWorkbook.createFont();
				fontHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
				styleHeader.setFont(fontHeader);
				outcell.setCellStyle(styleHeader);
			}

			if((cell1!=null)&&(cell2==null)){
				countcell2++;

				if(countcell2== (row1.getLastCellNum()-1)){
					error = "";
					error = "Content missing {"+row1.getCell(2)+"} at row {"+rowIndex+"} in file2";
					outcellError=outRow.createCell(8);
					outcellError.setCellValue(error);

					if(cell1.getCellType()==HSSFCell.CELL_TYPE_STRING){
						outcell.setCellValue(cell1.getStringCellValue());	
						outcell.setCellStyle(errorCellStyle);
					}
					else{
						int s= (int) cell1.getNumericCellValue();  //for Numeric Value
						outcell.setCellValue(s);
						outcell.setCellStyle(errorCellStyle);
					}

				}
				else if(cell1.getCellType()==HSSFCell.CELL_TYPE_STRING) { 
					outcell.setCellValue(cell1.getStringCellValue());
					outcell.setCellStyle(errorCellStyle);
					String strError=ColName+" should be {" +cell1.getStringCellValue()+"} in File2 but it is {EMPTY}\n";
					error=error.concat(strError);
				}

				else{
					int s= (int) cell1.getNumericCellValue();  //for Numeric Value
					outcell.setCellValue(s);
					outcell.setCellStyle(errorCellStyle);
					outcellError=outRow.createCell(8);
					String strError=ColName+" should be {" +s+"} in File2 but it is {EMPTY}.\n" ;
					error=error.concat(strError);
				}		
			}
			
			else if((cell1!=null)&&(cell2!=null)){
				if((cell1.getCellType()==HSSFCell.CELL_TYPE_STRING) && (cell2.getCellType()==HSSFCell.CELL_TYPE_STRING)) {
					if(cell1.getStringCellValue().equalsIgnoreCase(cell2.getStringCellValue())){	
						outcell.setCellValue(cell1.getStringCellValue());
					}
					else {
						outcellError=outRow.createCell(8);
						outcell.setCellValue(cell1.getStringCellValue());
						outcell.setCellStyle(errorCellStyle);
						String strError=ColName+" differs :"+ColName+" in File1 is {"+cell1.getStringCellValue()+"},"
								+ ColName+" in File2 is {"+cell2.getStringCellValue()+"}.\n";
						error=error.concat(strError);
					}  
				}
				
				else if (((cell1.getCellType()==HSSFCell.CELL_TYPE_STRING) && (cell2.getCellType()==HSSFCell.CELL_TYPE_NUMERIC))||
						((cell1.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) && (cell2.getCellType()==HSSFCell.CELL_TYPE_STRING))){
					
					outcellError=outRow.createCell(8);
					outcell.setCellStyle(errorCellStyle);
					String strError="Inappropriate record found for Column {"+ColName+"} in row number {"+rowIndex+"}, please enter a valid record.\n ";
					error=error.concat(strError);
					
				}

				else{
					if(cell1.getNumericCellValue()==cell2.getNumericCellValue()){
						outcell.setCellValue(cell1.getNumericCellValue());
					}

					else {						
						double a= cell1.getNumericCellValue();
						double b=cell2.getNumericCellValue();
						double difference;
						double avalue;
						double abs_value;
						if(a<b){
							difference=b-a;
							avalue=(difference/b)*100;
							abs_value=Math.round(avalue);
						}
						else{
							difference=a-b;
							avalue=(difference/a)*100.0;
							abs_value=Math.round(avalue);
						}
						outcellError=outRow.createCell(8);
						outcell.setCellValue(cell1.getNumericCellValue());
						outcell.setCellStyle(errorCellStyle);
						// write code for set Message and error log 
						String strError="Data mismatch at Column {"+ColName+"} between rows{"+rowIndex+"}/"+"{"+rowIndex+"}"
								+ " values {"+cell1.getNumericCellValue()+"}/{"+cell2.getNumericCellValue()+"} "
								+ "difference {"+difference+"}/{"+abs_value+"%}.\n";
						error=error.concat(strError);
					}  
				}
			}

			else if ((cell1==null) &&(cell2!=null)){
				outcellError=outRow.createCell(8);	
				String strError;	
				if(countcell2==(row1.getLastCellNum()-1)){  
					error = "";
					error = "Content missing at {"+rowIndex+"} in file 1";
					outcellError=outRow.createCell(8);
					outcellError.setCellValue(error);		
				}
				else if(cell2.getCellType()==HSSFCell.CELL_TYPE_STRING){
					outcell.setCellValue("EMPTY");
					outcell.setCellStyle(errorCellStyle);
					strError= ColName+" should be {" +cell2.getStringCellValue()+"} in File1 but it is {EMPTY}.\n";				
					error=error.concat(strError);
				}

				else{
					outcell.setCellValue(0);
					outcell.setCellStyle(errorCellStyle);
					strError=ColName+" should be {" +cell2.getNumericCellValue()+"} in File1 but it is {EMPTY}.\n";
					error=error.concat(strError);
				}			
			}
		}
		outcellError.setCellValue(error);
		outcellError.setCellStyle(style);

		return equalRows;
	}
}
