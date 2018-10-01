//package com.homedirect.service.impl;
//
////import java.util.List;
////import java.util.Map;
////
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
////
////import org.apache.poi.hssf.usermodel.HSSFFont;
////import org.apache.poi.hssf.usermodel.HSSFRow;
////import org.apache.poi.hssf.usermodel.HSSFSheet;
////import org.apache.poi.hssf.util.HSSFColor;
////import org.apache.poi.ss.usermodel.CellStyle;
////import org.apache.poi.ss.usermodel.Font;
////import org.apache.poi.ss.usermodel.Workbook;
////import org.springframework.web.servlet.view.document.*;
////
////import com.homedirect.response.AccountResponse;
////
////public class ExcelBuilder extends AbstractXlsxView {
////
////	@Override
////	protected void buildExcelDocument(Map<String, Object> model, Workbook workAccountResponse,
////			HttpServletRequest request, HttpServletResponse response) throws Exception {
////		List<AccountResponse> accountResponses = (List<AccountResponse>) model.get("listAccounts");
////
////		// create a new Excel sheet
////		HSSFSheet sheet = (HSSFSheet) workAccountResponse.createSheet("Java Books");
////		sheet.setDefaultColumnWidth(30);
////
////		// create style for header cells
////		CellStyle style = workAccountResponse.createCellStyle();
////		Font font = workAccountResponse.createFont();
////		font.setFontName("Arial");
////		style.setFillForegroundColor(HSSFColor.BLUE.index);
////		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
////		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
////		font.setColor(HSSFColor.WHITE.index);
////		style.setFont(font);
////
////		// create header row
////		HSSFRow header = sheet.createRow(0);
////
////		header.createCell(0).setCellValue("Id");
////		header.getCell(0).setCellStyle(style);
////
////		header.createCell(1).setCellValue("Username");
////		header.getCell(1).setCellStyle(style);
////
////		header.createCell(2).setCellValue("AccountNumber");
////		header.getCell(2).setCellStyle(style);
////
////		header.createCell(4).setCellValue("Amount");
////		header.getCell(4).setCellStyle(style);
////
////		// create data rows
////		int rowCount = 1;
////
////		for (AccountResponse accountResponse : accountResponses) {
////			HSSFRow aRow = sheet.createRow(rowCount++);
////			aRow.createCell(0).setCellValue(accountResponse.getId());
////			aRow.createCell(1).setCellValue(accountResponse.getUsername());
////			aRow.createCell(2).setCellValue(accountResponse.getAccountNumber());
////			aRow.createCell(3).setCellValue(accountResponse.getAmount());
////		}
////
////	}
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import com.homedirect.response.AccountResponse;
//
//public class WriteExcel {
//
//	private static String[] COLUMNs = { "Id", "Username", "AccountNumber", "Amount" };
//	private static List<AccountResponse> accountResponses = Arrays.asList();
//
//	public static void main(String[] args) throws IOException {
//
//		Workbook workbook = new XSSFWorkbook();
//
//		CreationHelper createHelper = workbook.getCreationHelper();
//
//		Sheet sheet = workbook.createSheet("Customers");
//
//		Font headerFont = workbook.createFont();
//		headerFont.setBoldweight(true);
//		headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//		CellStyle headerCellStyle = workbook.createCellStyle();
//		headerCellStyle.setFont(headerFont);
//
//		// Row for Header
//		Row headerRow = sheet.createRow(0);
//
//		// Header
//		for (int col = 0; col < COLUMNs.length; col++) {
//			Cell cell = headerRow.createCell(col);
//			cell.setCellValue(COLUMNs[col]);
//			cell.setCellStyle(headerCellStyle);
//		}
//
//		// CellStyle for Amount
//		CellStyle amountCellStyle = workbook.createCellStyle();
//		amountCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
//
//		int rowIdx = 1;
//		for (AccountResponse accountResponse : accountResponses) {
//			Row row = sheet.createRow(rowIdx++);
//
//			row.createCell(0).setCellValue(accountResponse.getId());
//			row.createCell(1).setCellValue(accountResponse.getUsername());
//			row.createCell(2).setCellValue(accountResponse.getAccountNumber());
//
//			Cell ageCell = row.createCell(3);
//			ageCell.setCellValue(accountResponse.getAmount());
//			ageCell.setCellStyle(amountCellStyle);
//		}
//
//		FileOutputStream fileOut = new FileOutputStream("customers.xlsx");
//		workbook.write(fileOut);
//		fileOut.close();
//		workbook.close();
//	}
//}
