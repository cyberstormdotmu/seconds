package com.tatva.reports;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.collection.PdfTargetDictionary;
import com.tatva.controller.AppointmentController;
import com.tatva.domain.AppointmentMaster;
import com.tatva.framework.AbstractITextPdfView;
import com.tatva.model.AppointmentForm;
import com.tatva.service.impl.AppointmentMasterServiceImpl;

public class AppointmentReceiptRpt extends AbstractITextPdfView {

	public Document buildPdfDocument(Map<String, Object> model,
			Document document, 
			PdfWriter writer,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception 
{
@SuppressWarnings("unchecked")
//get the list of scheduled appointment
AppointmentForm appointmentForm = (AppointmentForm)model.get("appointmentForm");

// check whether the list contains data or not

// table layout for pdf
PdfPTable table = new PdfPTable(3);
table.setWidthPercentage(100.0f);
table.setWidths(new float[] {25.0f,45.0f,30.0f});
table.setSpacingBefore(5);


PdfPTable table2 = new PdfPTable(1);
table2.setWidthPercentage(100.0f);
table2.setWidths(new float[] {100.0f} );
table2.setSpacingAfter(5);


// define font for table header row
Font font = FontFactory.getFont(FontFactory.COURIER);
font.setColor(BaseColor.WHITE);
font.setSize(10);

Font font2 = FontFactory.getFont(FontFactory.COURIER);
font2.setColor(BaseColor.BLACK);
font2.setSize(10);




// define table header cell
PdfPCell cell = new PdfPCell();
cell.setBackgroundColor(BaseColor.GRAY);
cell.setPadding(5);
cell.setBorderWidth(0);


//define table header cell for extra text

PdfPCell cell2 = new PdfPCell();
cell2.setBackgroundColor(BaseColor.WHITE);
cell2.setPadding(5);
cell2.setBorderWidth(0);



//write table header
cell.setPhrase(new Phrase("",font));
table.addCell(cell);

cell.setPhrase(new Phrase("Your Appointment Details" ,font));
table.addCell(cell);

cell.setPhrase(new Phrase("",font));
table.addCell(cell);


//write table header

cell2.setPhrase(new Phrase("Important Notes:",font2));


font = FontFactory.getFont(FontFactory.TIMES);
font.setSize(10);

font2 = FontFactory.getFont(FontFactory.TIMES);
font2.setSize(10);



table.addCell(new Phrase("Reference No.", font ));
table.addCell(new Phrase(AppointmentController.refNo, font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("NRIC/Passport Number", font));
table.addCell(new Phrase(appointmentForm.getNricPpassportNumber(), font));
table.addCell(new Phrase("", font));

String CraftNumbers = "";
for (String CN:appointmentForm.getCraftNumbers()){
	CraftNumbers += CN; 	
}

table.addCell(new Phrase("Craft Numbers", font));
table.addCell(new Phrase(CraftNumbers, font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("Name", font));
table.addCell(new Phrase(appointmentForm.getName(), font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("Company Name", font));
table.addCell(new Phrase(appointmentForm.getCompany(), font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("Transaction Type", font));
table.addCell(new Phrase("", font));
table.addCell(new Phrase("", font));

for(String mainType: appointmentForm.getTransactionType() ){
	table.addCell(new Phrase("", font));
	if(mainType.equals("HCL")){
		table.addCell(new Phrase("Harbour Craft", font));
		table.addCell(new Phrase("Qty", font));
		for(String subType1 : appointmentForm.getHarbourCraftCheckBox()){
			
			table.addCell(new Phrase("", font));
			if(subType1.equals("HCLNL")){
				table.addCell(new Phrase("*New / Renewal of licence", font));
				table.addCell(new Phrase(appointmentForm.getHCLNLSelect(), font));
			}
			else if(subType1.equals("HCLAD")){
				table.addCell(new Phrase("*Application to de-licence", font));
				table.addCell(new Phrase(appointmentForm.getHCLADSelect(), font));
			}
			else if(subType1.equals("HCLUC")){
				table.addCell(new Phrase("*Update of craft particulars", font));
				table.addCell(new Phrase(appointmentForm.getHCLUCSelect(), font));
			}
			
			else if(subType1.equals("HCLCO")){
				table.addCell(new Phrase("*Change of ownership", font));
				table.addCell(new Phrase(appointmentForm.getHCLCOSelect(), font));
			}
			else if(subType1.equals("HCLNM")){
				table.addCell(new Phrase("*New/Renewal of Manning licence", font));
				table.addCell(new Phrase(appointmentForm.getHCLNMSelect(), font));
			}
			else if(subType1.equals("HCLRH")){
				table.addCell(new Phrase("*Return of HARTS", font));
				table.addCell(new Phrase(appointmentForm.getHCLRHSelect(), font));
			}
		}
	}
	
	else if(mainType.equals("PCL")){
		
		table.addCell(new Phrase("Pleasure Craft", font));
		table.addCell(new Phrase("Qty", font));
		
		for(String subType2 : appointmentForm.getPleasureCraftCheckBox()){
			table.addCell(new Phrase("", font));
			if(subType2.equals("PCLNL")){
				table.addCell(new Phrase("*New / Renewal of licence", font));
				table.addCell(new Phrase(appointmentForm.getPCLNLSelect(), font));
				
			}
			else if(subType2.equals("PCLAD")){
				table.addCell(new Phrase("*Application to de-licence", font));
				table.addCell(new Phrase(appointmentForm.getPCLADSelect(), font));								
			}
			else if(subType2.equals("PCLUC")){
				table.addCell(new Phrase("*Update of craft particulars", font));
				table.addCell(new Phrase(appointmentForm.getPCLUCSelect(), font));				
			}
			else if(subType2.equals("PCLNP")){
				table.addCell(new Phrase("*New / Renewal of PPCDL or APPCDL", font));
				table.addCell(new Phrase(appointmentForm.getPCLNPSelect(), font));				
			}
						
		}
			
	}
	else if(mainType.equals("PC")){
		
		table.addCell(new Phrase("Port Clearance", font));
		table.addCell(new Phrase("Qty" , font));
		
		for(String subType3 : appointmentForm.getPortClearanceCheckBox()){
			table.addCell(new Phrase("" , font));
			
			if(subType3.equals("PCGD")){
				table.addCell(new Phrase("*General Declaration",font));
				table.addCell(new Phrase(appointmentForm.getPCGDSelect(), font));
			}
			else if(subType3.equals("PCAL")){
				table.addCell(new Phrase("*Application for launching permit",font));
				table.addCell(new Phrase(appointmentForm.getPCALSelect(), font));
			}
			else if(subType3.equals("PCAB")){
				table.addCell(new Phrase("*Application for Break-up permit",font));
				table.addCell(new Phrase(appointmentForm.getPCABSelect(), font));
			}
		}
	}
	else if(mainType.equals("OTHS")){
		
		table.addCell(new Phrase("Others" , font));
		table.addCell(new Phrase("Qty" , font));
		for(String subType4 : appointmentForm.getOthersCheckBox()){
			table.addCell(new Phrase("" , font));
			
			if(subType4.equals("OTHS")){
				table.addCell(new Phrase("*Others",font));
				table.addCell(new Phrase(appointmentForm.getOTHSSelect(), font));
			}
		}
	}
}

table.addCell(new Phrase("Date/Time", font));
table.addCell(new Phrase(appointmentForm.getDate()+"  "+appointmentForm.getTime(), font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("E-Mail Address", font));
table.addCell(new Phrase(appointmentForm.getEmailAddress(), font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("ContactTel.No.", font));
table.addCell(new Phrase(appointmentForm.getContactNumber(), font));
table.addCell(new Phrase("", font));

table.addCell(new Phrase("Remarks", font));
table.addCell(new Phrase(appointmentForm.getRemark(), font));
table.addCell(new Phrase("", font));


document.add(table);

document.add(new Paragraph("Important Notes :-"));
document.add(new Paragraph("---------------"));

document.add(new Paragraph("Please refere to the relevant forms for the list of information documents required for the appointment"));
document.add(new Paragraph(" "));
document.add(new Paragraph("1.Bring all required documents indicated"));
document.add(new Paragraph("2.Be punctual for your appointment"));
document.add(new Paragraph("3.If you missed the appointment time, you are required to wait for the next available slot or book"));
document.add(new Paragraph("  a new appintment for the following day."));
document.add(new Paragraph(" "));
document.add(new Paragraph("Alternatively,you may visit our website to check or cancel your appointment"));



return document;

}
}
