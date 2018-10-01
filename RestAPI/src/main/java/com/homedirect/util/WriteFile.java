package com.homedirect.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.response.AccountResponse;

@Component
public class WriteFile {

	public void writeListAccountResponsetoExcel(List<AccountResponse> accountResponses)
			throws IOException, ATMException {
		String[] columns = { "Id", "Username", "AccountNumber", "Amount" };
		String FILE_NAME = "Accounts";
		Date dateTime = new Date();
		String outputFilename = FILE_NAME + "_" + dateTime + ".xlsx";

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Accounts");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		// Creating cells
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Other rows and cells with employees data
		int rowNum = 1;
		for (AccountResponse accountResponse : accountResponses) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(accountResponse.getId());
			row.createCell(1).setCellValue(accountResponse.getUsername());
			row.createCell(2).setCellValue(accountResponse.getAccountNumber());
			row.createCell(3).setCellValue(accountResponse.getAmount());
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(outputFilename);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	public void writeListTransactiontoExcel(List<Transaction> transactions)
			throws IOException, ATMException, ParseException {
		String[] columns = { "FromAccountNumber", "ToAccountNumber", "Content", "Status", "Time", "Amount", "Type" };
		String FILE_NAME = "Transactions";
		Date dateTime = new Date();
		String outputFilename = FILE_NAME + "_" + dateTime + ".xlsx";

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Accounts");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		// Creating cells
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Other rows and cells with employees data
		int rowNum = 1;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		for (Transaction transaction : transactions) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(transaction.getFromAccount());
			row.createCell(1).setCellValue(transaction.getToAccount());
			row.createCell(2).setCellValue(transaction.getContent());
			row.createCell(3).setCellValue(transaction.getStatus());
			
			Date time = transaction.getTime();
			row.createCell(4).setCellValue(format.format(time));
			row.createCell(5).setCellValue(transaction.getTransferAmount());
			row.createCell(6).setCellValue(transaction.getType());
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(outputFilename);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
}
