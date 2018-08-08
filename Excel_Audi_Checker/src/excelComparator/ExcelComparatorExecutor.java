package excelComparator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelComparatorExecutor {

	public static HSSFSheet file1;
	public static HSSFSheet file2 ;
	public static String resultFile ;
	public static HSSFWorkbook resultWorkbook;
	public static FileInputStream excellFile1;
	public static FileInputStream excellFile2;
	public static FileOutputStream fileOut; 
	public static void main(String[] args) {

		System.out.println(" Entered path for File1 : " +args[0]);
		System.out.println(" Entered path for File2 : " +args[1]);
		System.out.println(" Entered Result File name with associated path : " +args[2]);	
		String file1path=args[0];
		String file2path=args[1];
		String resultFile=args[2];

		try{
			File f1= new File(file1path);
			File f2= new File(file2path);
			File f3=new File(resultFile);

			{
				String Result_path=f3.getParent();

				if((f1.exists())&&(f2.exists())){		

					File f4=new File(Result_path);
					if(!f4.isDirectory()){
						System.err.println(" Please enter a valid directory for ResultFile ");
					}
					else{
						excellFile1 = new FileInputStream(new File(file1path));
						excellFile2 = new FileInputStream(new File(file2path));
						fileOut = new FileOutputStream(f3);
						if(method(excellFile1, excellFile2 ,resultFile)){
							System.out.println("Comparison completed...");
						}
					}

				}
				else {
					if((!f1.exists()) && (!f2.exists())){
						System.err.println(" Please enter a valid File name or Directory for File1 and File2 ");
					}
					else{
						if(!f1.exists()){
							System.err.println(" Please enter a valid File name or Directory for File1 ");
						}
						if(!f2.exists()){
							System.err.println("Please enter a valid File name or Directory for File2");
						} 
					}
				}
			}
		}catch(Exception e){

			System.err.println(e);
		}
	}

	public static boolean method(FileInputStream excellFile1, FileInputStream excellFile2, String resultFile){

		try{
			// Create Workbook instance holding reference to .xls file
			HSSFWorkbook workbook1 = new HSSFWorkbook(excellFile1);
			HSSFWorkbook workbook2 = new HSSFWorkbook(excellFile2);	
			resultWorkbook = new HSSFWorkbook();

			for(int sn=0; sn<workbook1.getNumberOfSheets(); sn++)
			{
				file1 = workbook1.getSheetAt(sn); // Get first/desired sheet from the workbook
				file2 = workbook2.getSheetAt(sn);
				String sheetName=file1.getSheetName();
				resultWorkbook.createSheet(sheetName);
				resultWorkbook.setSheetOrder(sheetName, sn);

				if(sheetName.equalsIgnoreCase("Data")){
					ExcelComparatorForData.compareTwoSheets(file1, file2, resultWorkbook,resultFile);
					System.out.println("Comparison has been done Successfully for :" +sheetName);
				}

				else if(sheetName.equalsIgnoreCase("MemSysCounts")){
					ExcelComparatorForMemSysCount.compareTwoSheets(file1, file2, resultWorkbook,resultFile);
					System.out.println("Comparison has been done Successfully for :" +sheetName);
				}
				else{

					System.err.println("Does not found a sheet to compare");
				}
			}
			//close files
			excellFile1.close();
			excellFile2.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}


}
