package com.tatva.reports;

import java.util.List;
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
import com.tatva.domain.AppointmentMaster;
import com.tatva.framework.AbstractITextPdfView;
import com.tatva.utils.ListUtils;

/**
 *  {@link :AbstractITextPdfView}
 *	 @author pci94
 * 	this class is for generating the cancelled appointment report 
 */
public class AppointmentCancelledRpt extends AbstractITextPdfView
{

	/*
	 * (non-Javadoc)
	 * @see com.tatva.framework.AbstractITextPdfView#buildPdfDocument(java.util.Map, com.itextpdf.text.Document, com.itextpdf.text.pdf.PdfWriter, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Document buildPdfDocument(Map<String, Object> model,
									Document document, PdfWriter writer, HttpServletRequest request,
									HttpServletResponse response) throws Exception {
		
		@SuppressWarnings("unchecked")
		//get the list of cancelled appointment
		List<AppointmentMaster> cancelledAppointmentMasterList = (List<AppointmentMaster>)model.get("cancelledAppointmentMasterList");
		String message = null;
		message = (String)model.get("message");
		
		// check whether the list contains data or not
		if(message == null){
		document.add(new Paragraph("List of appointments cancelled"));
		document.add(new Paragraph(cancelledAppointmentMasterList.get(0).getAppointmentDate().toString()));
		
		// table layout for pdf
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[] {0.5f, 1.0f, 1.5f, 1.5f, 1.0f, 1.0f, 1.5f, 1.0f, 1.0f});
        table.setSpacingBefore(10);
         
        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.COURIER);
        font.setColor(BaseColor.WHITE);
        font.setSize(10);
        
        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setPadding(5);

        
        // write table header
        cell.setPhrase(new Phrase("No.", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Type of Appointment", font));
        table.addCell(cell);
 
        
        cell.setPhrase(new Phrase("Types of Transaction", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Craft No.", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Previous Date", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Previous Time", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Contact No.", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Reference No.", font));
        table.addCell(cell);
        
        
        
        cell.setPhrase(new Phrase("Remarks", font));
        table.addCell(cell);
        
        font = FontFactory.getFont(FontFactory.TIMES);
        font.setSize(9);

		
        int count = 1;
        // write table row data
        for (AppointmentMaster appointmentMaster : cancelledAppointmentMasterList) {
            table.addCell(new Phrase(count + "", font));
            table.addCell(new Phrase(appointmentMaster.getAppointmentType(), font));
            table.addCell(new Phrase(ListUtils.convertTransactionMasterListToString(appointmentMaster.getTransacMaster()), font));
            table.addCell(new Phrase(ListUtils.convertAppointMentCraftListToString(appointmentMaster.getAppointmentCraft()), font));
            table.addCell(new Phrase(appointmentMaster.getAppointmentDate().toString(),font));
            table.addCell(new Phrase(appointmentMaster.getAppointmentTime().toString(), font));
            table.addCell(new Phrase(appointmentMaster.getContactNo(), font));
            table.addCell(new Phrase(appointmentMaster.getReferenceNo(), font));
            table.addCell(new Phrase(appointmentMaster.getRemark(), font));
            count++;
        }
        document.add(table);
		return document;
		}
		else
		{
			document.add(new Paragraph(message));
			return document;
		}
		
		
		
	}

}
