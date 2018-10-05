package com.homedirect.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CsvUtil {

	private static final char DEFAULT_SEPARATOR = ',';
	
	public static void writerLine(Writer writer, List<String> values) throws IOException {
		writerLine(writer, values, DEFAULT_SEPARATOR, " ");
	}
	
//	public static void writerLine(Writer writer, List<String> values, char separator) throws IOException {
//		writerLine(writer, values, separator, " ");
//	}
	
	public static String followCsvFomat(String value) {
		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		} 
		return result;
	}
	
	public static void writerLine(Writer writer, List<String> values, char separator, String customQuote) throws IOException {
		boolean first = true;
		if (separator == ' ') {
			separator = DEFAULT_SEPARATOR;
		}
		StringBuilder builder = new StringBuilder();
		for (String value : values) {
			if (!first) {
				builder.append(separator);
			}
			if (customQuote == " ") {
				builder.append(followCsvFomat(value));
				
			} else {
				builder.append(customQuote).append(followCsvFomat(value)).append(customQuote);
			}
			first = false;
		}
		builder.append("\n");
		writer.append(builder.toString());
	}
}
