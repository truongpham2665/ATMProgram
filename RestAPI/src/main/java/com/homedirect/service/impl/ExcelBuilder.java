//package com.homedirect.service.impl;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.web.servlet.view.document.*;
//
//import com.homedirect.response.AccountResponse;
//
///**
// * This class builds an Excel spreadsheet document using Apache POI library.
// * 
// * @author www.codejava.net
// *
// */
//public class ExcelBuilder extends AbstractXlsxView {
//
//	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workAccountResponse,
//			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// get data model which is passed by the Spring container
//		List<AccountResponse> accountResponses = (List<AccountResponse>) model.get("listAccounts");
//
//		// create a new Excel sheet
//		HSSFSheet sheet = workAccountResponse.createSheet("Java Books");
//		sheet.setDefaultColumnWidth(30);
//
//		// create style for header cells
//		CellStyle style = workAccountResponse.createCellStyle();
//		Font font = workAccountResponse.createFont();
//		font.setFontName("Arial");
//		style.setFillForegroundColor(HSSFColor.BLUE.index);
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		font.setColor(HSSFColor.WHITE.index);
//		style.setFont(font);
//
//		// create header row
//		HSSFRow header = sheet.createRow(0);
//
//		header.createCell(0).setCellValue("Book Title");
//		header.getCell(0).setCellStyle(style);
//
//		header.createCell(1).setCellValue("Author");
//		header.getCell(1).setCellStyle(style);
//
//		header.createCell(2).setCellValue("ISBN");
//		header.getCell(2).setCellStyle(style);
//
//		header.createCell(3).setCellValue("Published Date");
//		header.getCell(3).setCellStyle(style);
//
//		header.createCell(4).setCellValue("Price");
//		header.getCell(4).setCellStyle(style);
//
//		// create data rows
//		int rowCount = 1;
//
//		for (AccountResponse accountResponse : accountResponses) {
//			HSSFRow aRow = sheet.createRow(rowCount++);
//			aRow.createCell(0).setCellValue(accountResponse.getId());
//			aRow.createCell(1).setCellValue(accountResponse.getUsername());
//			aRow.createCell(2).setCellValue(accountResponse.getAccountNumber());
//			aRow.createCell(3).setCellValue(accountResponse.getAmount());
//		}
//	}
//
//	@Override
//	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//}
