package excelComparator;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class ExcelComparatorForData {

	public static HSSFSheet file1;
	public static HSSFSheet file2 ;
	public static HSSFWorkbook resultWorkbook; 
	public static HSSFSheet outputSheet;
	public static CellReference cellrefferenceObj ;
	static double key;
	static int value;
	public static Set<Integer> file1Set;
	public static Set<Integer> file2Set;
	public static CellStyle style;
	public static CellStyle style2;	

	// Compare Two Sheets
	public static boolean compareTwoSheets(HSSFSheet file1, HSSFSheet file2,HSSFWorkbook resultWorkbook, String resultFile) {

		file2Set=new HashSet<Integer>(); 
		file1Set=new HashSet<Integer>(); 
		int lastRowNumber = file1.getLastRowNum();
		boolean equalSheets = true;
		HSSFRow outRow;   
		HSSFRow	out_Extra_row;

		outputSheet = resultWorkbook.getSheetAt(1);

		outRow = outputSheet.createRow(0);	
		outputSheet.setColumnWidth(5, 21000);

		style = resultWorkbook.createCellStyle(); // for bold header
		Font font = resultWorkbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setWrapText(true);	

		style2 = resultWorkbook.createCellStyle();	
		style2.setWrapText(false);

		/* Header Creation done */
		String []headers=new String[]{"CCSA Code","Last Month","This Month","Avg", " ", "Error log"};
		for(int i=0; i<headers.length; i++ ) { 
			outRow.createCell(i).setCellValue(headers[i]);
			outRow.getCell(i).setCellStyle(style);	
		}

		/* storing CCSA and Row number in map to lookup*/
		Iterator<Row> rowIterator = file2.iterator();
		HashMap<Double,Integer> map=new HashMap<Double,Integer>();  
		while(rowIterator.hasNext()) {
			HSSFCell cellCCSAnull=null;
			Row row = rowIterator.next();
			if(row.getRowNum()==0){
				continue;
			}
			else{
				cellCCSAnull =(HSSFCell) row.getCell(0);
				if(cellCCSAnull!=null){
					map.put((double) row.getCell(0).getNumericCellValue(), row.getRowNum());
					file2Set.add((int) row.getCell(0).getNumericCellValue());	
				}
			}					
		}

		/* Storing file2 ccsa into set to get Symmetric difference*/
		for(int j=1 ; j<=file1.getLastRowNum(); j++){
			HSSFRow row11 = file1.getRow(j);
			try{
				if(row11.getCell(0)!=null){
					file1Set.add((int) row11.getCell(0).getNumericCellValue());
				}
			}catch(Exception e){

			}		
		}

		Set<Integer> symmetricDiff = new HashSet<Integer>(file1Set);			
		symmetricDiff.addAll(file2Set);
		file1Set.retainAll(file2Set);
		symmetricDiff.removeAll(file1Set);

		Iterator<Integer> iterator = symmetricDiff.iterator();    
		String comma= "";
		// check values	 
		while (iterator.hasNext()){
			comma=iterator.next().toString().concat(", "+comma);
		}
		out_Extra_row=  outputSheet.createRow(file1.getLastRowNum()+2);
		HSSFCell msg_cell=out_Extra_row.createCell(0);	
		msg_cell.setCellStyle(style2);
		msg_cell.setCellValue("CCSA Codes present in File2 but not in File1 are : "+comma.substring(0, comma.length()-2));	

		/* Perform matching operation on rows from file 1 and file2*/
		for(int i=1; i <= lastRowNumber; i++) {       
			HSSFRow row1 = file1.getRow(i);
			outRow = outputSheet.createRow(i);	
			if(!compareTwoRows(row1, outRow,file2,  map, resultWorkbook)) {
				equalSheets = false;
			} 
			else {
				try{
					File file=new File(resultFile);
					FileOutputStream fileOut = new FileOutputStream(file);
					resultWorkbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
				}catch(Exception e){
					e.printStackTrace();
				}	
			}		
		}	
		return equalSheets;
	}
	
	// Compare Two Rows
	public static boolean compareTwoRows(HSSFRow row1, HSSFRow outRow, HSSFSheet file2, Map<Double , Integer> map , HSSFWorkbook resultWorkbook) {

		boolean equalRows = true;
		HSSFCell outcell=null;
		HSSFCell outcellCCSA=outRow.createCell(0);
		HSSFCell outcellError=outRow.createCell(5);
		String error="";
		HSSFCell cell1 =null;
		HSSFCell cell2=null;
		HSSFCell cellFromFile1=null;

		CellStyle errorCellStyle = resultWorkbook.createCellStyle();
		errorCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		errorCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 

		if(row1==null){
			return equalRows;
		}

		cellFromFile1= row1.getCell(0);
		if(cellFromFile1!=null){
			if (map.containsKey(cellFromFile1.getNumericCellValue())){
				for(java.util.Map.Entry<Double, Integer> m:map.entrySet()){  
					if(m.getKey()==cellFromFile1.getNumericCellValue()){
						value=(int) m.getValue();
						key=(double) m.getKey();
					}
					else{
						continue;
					}
				}
				outcellCCSA.setCellValue(row1.getCell(0).getNumericCellValue()); 
				HSSFRow row2 = file2.getRow(value);

				for( int i=1; i <= 3; i++) {
					cell1= row1.getCell(i);
					cell2 = row2.getCell(i);
					outcell = outRow.createCell(i);
					int rowIndex= row2.getRowNum();
					rowIndex= rowIndex+1;
					cellrefferenceObj = new CellReference(row1.getRowNum(), i);

					if((cell1!=null) && (cell2!=null) ){
						double cell1value= cell1.getNumericCellValue();
						double cell2value=cell2.getNumericCellValue();
						double difference;
						double avalue;
						double abs_value;
						if(cell1value<cell2value){
							difference=cell2value-cell1value;
							avalue=(difference/cell2value)*100;
							abs_value=Math.round(avalue);
						}
						else{
							difference=cell1value-cell2value;
							avalue=(difference/cell1value)*100.0;
							abs_value=Math.round(avalue);
						}

						if(cell1.getNumericCellValue()!=cell2.getNumericCellValue())
						{
							outcell.setCellValue(cell1.getNumericCellValue());
							outcell.setCellStyle(errorCellStyle);
							String strError="Data mismatch from File2 cell {"+cellrefferenceObj.formatAsString()+"} between rows{"+rowIndex+"}/"+"{"+rowIndex+"}"
									+ " values {"+cell1.getNumericCellValue()+"}/{"+cell2.getNumericCellValue()+"} "
									+ "difference {"+difference+"}/{"+abs_value+"%} \n";
							error=error.concat(strError);
							outcellError.setCellStyle(style);
						}  

						else {
							outcell.setCellValue(cell1.getNumericCellValue());
						}
					}
					
					else {

						if((cell1!=null) && (cell2==null)){
							int ccsa= (int) cell1.getNumericCellValue();
							outcell.setCellValue(ccsa);
							outcell.setCellStyle(errorCellStyle);
							String strError="Empty cell is {"+ cellrefferenceObj.formatAsString()+"} from File2\n" ;
							error=error.concat(strError);
						}	

						else if((cell1==null)&&(cell2!=null)){
							String strError="Cell{"+cellrefferenceObj.formatAsString()+"} from File1 is empty\n" ;
							outRow.createCell(0).setCellValue(row1.getCell(0).getNumericCellValue());
							outcell.setCellValue(0);
							outcell.setCellStyle(errorCellStyle);
							error=error.concat(strError);

						}
						else if ((cell1==null)&&(cell2==null)) {
							String strError="Both the cells {"+(i+1)+"} are Empty from File1 and File2";
							outcell.setCellValue(0);
							outcell.setCellStyle(errorCellStyle);
							error=error.concat(strError);

						} 
						else{
							String strError="Inappropriate data from file1 or file2 for cell {" +cellrefferenceObj.formatAsString()+"}";
							outcell.setCellStyle(errorCellStyle);
							error=error.concat(strError);	
						}
					}    
				}
			}
			
			else{
				int missingCCSA= (int) row1.getCell(0).getNumericCellValue();
				outRow.createCell(0).setCellValue(row1.getCell(0).getNumericCellValue());
				outRow.createCell(1).setCellValue(row1.getCell(1).getNumericCellValue());
				outRow.createCell(2).setCellValue(row1.getCell(2).getNumericCellValue());
				outRow.createCell(3).setCellValue(row1.getCell(3).getNumericCellValue());
				outRow.getCell(0).setCellStyle(errorCellStyle);
				outRow.getCell(1).setCellStyle(errorCellStyle);
				outRow.getCell(2).setCellStyle(errorCellStyle);
				outRow.getCell(3).setCellStyle(errorCellStyle);
				String strError="CCSA Code {"+missingCCSA+"} is Missing in File2";
				outRow.createCell(5).setCellValue(strError);
				error=error.concat(strError);
			}		
		}

		else{
			HSSFCell emptyCellFromFile1=null;
			for(int cn=row1.getFirstCellNum();cn<=row1.getLastCellNum(); cn++){
				emptyCellFromFile1=row1.getCell(cn);
				if(emptyCellFromFile1!=null){
					outRow.createCell(cn).setCellValue(row1.getCell(cn).getNumericCellValue());
				}
				else {
					outRow.createCell(cn).setCellValue(0); // set Zero for Empty Cell 
				}
			}
			int rowIndex=row1.getRowNum();
			rowIndex=rowIndex+1;
			String strError="CCSA is Missing from Row Number {"+rowIndex+"} of File1";
			error=error.concat(strError);
		}
		outcellError.setCellValue(error);
		outcellError.setCellStyle(style);

		return equalRows;
	}

}
