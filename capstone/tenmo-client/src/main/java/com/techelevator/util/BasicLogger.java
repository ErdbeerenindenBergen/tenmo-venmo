package com.techelevator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BasicLogger {

	private static PrintWriter pw = null;
	private static final String DIRECTORY_NAME = "tenmo-client";
	
	public static void log(String message) {
		try {
			if (pw == null) {
				String userDir = System.getProperty("user.dir");

				if(!userDir.endsWith(DIRECTORY_NAME)) {
					userDir += File.separator + DIRECTORY_NAME;
				}

				String logFilename = userDir + File.separator + "logs/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
				pw = new PrintWriter(new FileOutputStream(logFilename, true));
			}
			pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
			pw.flush();
		}
		catch (FileNotFoundException e) {
			throw new BasicLoggerException(e.getMessage());
		}
	}

}
