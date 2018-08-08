package com.tatva.framework;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *  
 * @author pci94
 *	This Class has override the methods required for creating PDF report.
 */
public abstract class AbstractITextPdfView extends AbstractView
{
	public AbstractITextPdfView() {
		setContentType("application/pdf");
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#generatesDownloadContent()
	 */
	protected boolean generatesDownloadContent() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// IE workaround: write into byte array first.
		ByteArrayOutputStream baos = createTemporaryOutputStream();

		// Apply preferences and build metadata.
		Document document = newDocument();
		PdfWriter writer = newWriter(document, baos);
		prepareWriter(model, writer, request);
		buildPdfMetadata(model, document, request);

		// Build PDF document.
		document.open();
		buildPdfDocument(model, document, writer, request, response);
		document.close();

		// Flush to HTTP response.
		writeToResponse(response, baos);
	}

	/**
	 * 
	 * @return Document with Page Size.
	 */
	@SuppressWarnings("deprecation")
	protected Document newDocument() {
		return new Document(PageSize.A4_LANDSCAPE);
	}
	
	/**
	 * 
	 * @param document
	 * @param os
	 * @return Writer object 
	 * @throws DocumentException
	 */
	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
		return PdfWriter.getInstance(document, os);
	}

	/**
	 * 
	 * @param model
	 * @param writer
	 * @param request
	 * @throws DocumentException
	 */
	protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
			throws DocumentException {

		writer.setViewerPreferences(getViewerPreferences());
	}

	/**
	 * 
	 * @return
	 */
	protected int getViewerPreferences() {
		return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
	}

	/**
	 * 
	 * @param model
	 * @param document
	 * @param request
	 * this method generate the pdf file with layouts
	 */
	protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
	}

	/**
	 * 
	 * @param model
	 * @param document
	 * @param writer
	 * @param request
	 * @param response
	 * @return PDF Document 
	 * @throws Exception
	 */
	protected abstract Document buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
}
