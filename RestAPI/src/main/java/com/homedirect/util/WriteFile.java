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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;

@Component
public class WriteFile {

	public void writeListAccountResponsetoExcel(List<Account> accounts) throws IOException, ATMException {
		String[] columns = { "Id", "Username", "AccountNumber", "Amount", "Password" };
		String FILE_NAME = "Accounts";
		String outputFilename = createFileName(FILE_NAME);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Accounts");
		createCell(workbook, sheet, columns);

		int rowNum = 1;
		for (Account account : accounts) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(account.getId());
			row.createCell(1).setCellValue(account.getUsername());
			row.createCell(2).setCellValue(account.getAccountNumber());
			row.createCell(3).setCellValue(account.getAmount());
			row.createCell(4).setCellValue(account.getPassword());
		}

		reSizeAllColumn(columns, sheet);
		writeFile(outputFilename, workbook);
	}

	public void writeListTransactiontoExcel(List<Transaction> transactions)
			throws IOException, ATMException, ParseException {
		String[] columns = { "FromAccountNumber", "ToAccountNumber", "Content", "Status", "Time", "Amount", "Type" };
		String FILE_NAME = "Transactions";
		String outputFilename = createFileName(FILE_NAME);

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Transactions");

		createCell(workbook, sheet, columns);

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

		reSizeAllColumn(columns, sheet);
		writeFile(outputFilename, workbook);
	}

	public String createFileName(String FILE_NAME) {
		Date dateTime = new Date();
		String outputFilename = FILE_NAME + "_" + dateTime + ".xlsx";
		return outputFilename;
	}

	public void createCell(Workbook workbook, Sheet sheet, String[] columns) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
	}

	public void reSizeAllColumn(String[] columns, Sheet sheet) {
		for (int i = 0; i < columns.length; i++) {

			sheet.autoSizeColumn(i);
		}
	}

	public void writeFile(String outputFilename, Workbook workbook) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(outputFilename);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
}