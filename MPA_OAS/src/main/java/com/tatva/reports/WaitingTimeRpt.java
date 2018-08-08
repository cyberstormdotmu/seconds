package com.tatva.reports;

import java.util.ArrayList;
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
import com.tatva.framework.AbstractITextPdfView;
import com.tatva.model.WaitingTimeModel;
import com.tatva.utils.MPAContext;

/**
 * {@link:AbstractITextPdfView}
 * @author pci94
 *	this class is for creating Waiting Time report
 */
public class WaitingTimeRpt extends AbstractITextPdfView{

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.framework.AbstractITextPdfView#buildPdfDocument(java.util.Map, com.itextpdf.text.Document, com.itextpdf.text.pdf.PdfWriter, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Document buildPdfDocument(Map<String, Object> model,
									Document document, 
									PdfWriter writer, 
									HttpServletRequest request,
									HttpServletResponse response) throws Exception {

		//get the list of waiting time appointment
		List<WaitingTimeModel> waitingTimeModels=(List<WaitingTimeModel>)model.get("listWaitingTime");
		document.open();
		
		//list for showing the time slots of 1hr.
		List<String> list=new ArrayList<>();
		int temp=MPAContext.startTime;
		list.add("<"+temp);
		for(int i=MPAContext.startTime;i<MPAContext.endTime;i++){
			list.add(temp+"-"+(temp+1));
			temp++;
		}
		
		document.add(new Paragraph("Statistic Of Waiting Time"));
		
		//table layout for pdf
		PdfPTable pdfPTable=new PdfPTable(10);
		pdfPTable.setWidthPercentage(100.0f);
		pdfPTable.setWidths(new float[] {2.0f, 2.0f, 2.0f, 2.0f,2.0f,2.0f,2.0f,2.0f,2.0f,2.0f});
		pdfPTable.setSpacingBefore(10);
		
		//set font size and color
		Font font = FontFactory.getFont(FontFactory.COURIER);
	    font.setColor(BaseColor.WHITE);
	    font.setSize(10);
	    
	    //table headers
	    PdfPCell cell = new PdfPCell();
	    cell.setBackgroundColor(BaseColor.GRAY);
	    cell.setPadding(5);
	    
	    cell.setPhrase(new Phrase("Service Name",font));
	    cell.setRowspan(2);
	    pdfPTable.addCell(cell);	    
	    
	    cell.setPhrase(new Phrase("Waiting Time Interval",font));
	    cell.setRowspan(2);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("A<5min",font));
	    cell.setRowspan(1);
	    cell.setColspan(2);
	    pdfPTable.addCell(cell);
	    	    	    
	    cell.setPhrase(new Phrase("B:5-10mins",font));
	    cell.setRowspan(1);
	    cell.setColspan(2);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("C:10>min",font));
	    cell.setRowspan(1);
	    cell.setColspan(2);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Total",font));
	    cell.setRowspan(1);
	    cell.setColspan(2);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Ticket Served",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("WT(Mins)",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);

	    cell.setPhrase(new Phrase("Ticket Served",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("WT(Mins)",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);

	    cell.setPhrase(new Phrase("Ticket Served",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("WT(Mins)",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Ticket Served",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);
	    
	    cell.setPhrase(new Phrase("WT(Mins)",font));
	    cell.setRowspan(1);
	    cell.setColspan(1);
	    pdfPTable.addCell(cell);
	    
	    font = FontFactory.getFont(FontFactory.TIMES);
	    font.setSize(9);
	    
	    cell.setBackgroundColor(BaseColor.WHITE);
	    
	    //set first column value
	    cell.setPhrase(new Phrase("HCL/PCL",font));
	    cell.setPaddingTop(100);
	    pdfPTable.addCell(cell).setRowspan(waitingTimeModels.size());
	    cell.setPadding(10);
	    
	    //write cell values
	    for(int i=0;i<waitingTimeModels.size();i++){
	    	
	    	cell.setPhrase(new Phrase(list.get(i),font));
	    	pdfPTable.addCell(cell);
	    	
	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getCounterLessThan5()),font));
	    	pdfPTable.addCell(cell);
	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getAvgLessThan5()),font));
	    	pdfPTable.addCell(cell);

	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getCounterBetween5And10()),font));
	    	pdfPTable.addCell(cell);
	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getAvgBetween5And10()),font));
	    	pdfPTable.addCell(cell);

	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getCounterGreaterThan10()),font));
	    	pdfPTable.addCell(cell);
	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getAvgGreaterThan10()),font));
	    	pdfPTable.addCell(cell);
	    	
	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getCounter()),font));
	    	pdfPTable.addCell(cell);
	    	cell.setPhrase(new Phrase(String.valueOf(waitingTimeModels.get(i).getAvg()),font));
	    	pdfPTable.addCell(cell);
	    }
	    
	    document.add(pdfPTable);
		return document;
	    
	}
}
