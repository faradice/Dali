/*================================================================================
 * Faradice Firmware
 *
 * Copyright (c) 2016 Faradice ehf.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Faradice ehf. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Faradice.
 *================================================================================
 */
package com.faradice.dali;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FaraFiles {
	private static final byte[] buffer = new byte[1024];

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	/** Appends a line to the end of a file */
	public static void appendToFile(String fileName, String line) {
		if (line == null || line.length() < 1) {
			return;
		}
		File file = initFile(fileName);
		line = line.replace("\r", "");
		if (!line.endsWith("\n")) {
			line += "\n";
		}
		line = line.replace(", ,",",");
		line = line.replace(",,",",");
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),Charset.forName("UTF-8").newEncoder()))) {
			bw.write(line);
			bw.flush();
		} catch (Exception ex) {
			Log.error(ex.getMessage(), ex);
		}
	}
	
	/** Appends a line to the end of a file */
	public static void appendToFile(String fileName, byte[] buffer, int len) {
		if (buffer == null || len < 1) {
			return;
		}
		try (FileOutputStream out = new FileOutputStream(fileName, true)) {
			out.write(buffer, 0, len);
			out.flush();
		} catch (Exception ex) {
			Log.error(ex.getMessage(), ex);
		}
	}
	
	public static String getRow(String fileName, String containing) {
		if (containing == null || containing.trim().length() < 1) {
			return null;
		}
		File file = initFile(fileName);
		if (!file.exists()) {
			return null;
		}
		containing = containing.toLowerCase();
		String row = null;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((row = br.readLine()) != null) {
				if (row.toLowerCase().contains(containing)) {
					return row;
				}
			}
		} catch (Exception ex) {
			Log.error(ex.getMessage(), ex);
		}
		return null;
	}
	
	/** Loads all rows from a file to a list */
	public static synchronized List<String> loadRows(String fileName) {
		return loadRows(fileName, Integer.MAX_VALUE);
	}
	
	/** Loads some number of rows from a file, starting with the first row in the file */
	public static synchronized List<String> loadRows(String fileName, int maxRows) {
		ArrayList<String> reads = new ArrayList<String>();
		File file = initFile(fileName);
		if (!file.exists()) {
			return reads;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String row = null;
			int counter = 0;
			while ((row = br.readLine()) != null && counter < maxRows) {
				reads.add(row);
				counter++;
			}
		} catch (Exception ex) {
			Log.error(ex.getMessage(), ex);
		}
		return reads;
	}

	/**
	 * Gets the last complete line in filename.
	 * Note: The line MUST end with either CR or EOL otherwize "" is returned
	 * @param fileName the name of the file
	 * @return The last line ONLY IF it ends with either CR or EOL, else empty String ""
	 * @throws IOException 
	 */
	public static synchronized String  getLastLineInFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			Log.info("File not found: " + file.getAbsolutePath());
			return "";
		}
		long fileSizeNow = file.length();
		String result = "";
		try (RandomAccessFile input = new RandomAccessFile(file, "r")) {
			long seekPos = Math.max(0, fileSizeNow - (buffer.length - 4));
			input.seek(seekPos);
			int len = input.read(buffer);
			result = getLastCompleteLineInBuffer(buffer, len);
		}
		return result;
	}
	
	public static boolean exists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	
	public static synchronized String getLastCompleteLineInBuffer(byte[] buf, int length) {
		if (length < 5) {
			return "";
		}

		// We only process if last line is complete (ends with CR)
		int eol = length - 1;
		byte b = buf[eol];
		if (!isEOLChar(b)) {
			return "";
		}
		while (isEOLChar(buf[eol])) {
			eol--;
		}
		// find the beginning of the line
		int sol = eol - 2;
		b = 0;
		while (sol > 0 && (!isEOLChar(b))) {
			b = buf[sol];
			sol--;
		}
		if (sol != 0) {
			sol++;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = sol; i <= eol; i++) {
			if (buf[i] != 0 && (!isEOLChar(buf[i]))) {
				sb.append((char) buf[i]);
			}
		}
		String result = sb.toString();
		return result;
	}

	public static boolean isEOLChar(byte b) {
		return b == 10 || b == 13;
	}
	
	public static void saveToFile(String fileName, ArrayList<String> rows) {
		try (PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName),Charset.forName("UTF-8").newEncoder()))) {
			for (String row : rows) {
				bw.println(row);
			}
		} catch (Exception ex) {
			Log.error(ex.getMessage(), ex);
		}	
	}
	
	private static File initFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return file;
		}
		File parent = file.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		try {
			if (!file.exists()) {
				File newFile = new File(fileName);
				newFile.setReadable(true, false);
				newFile.setWritable(true, false);
				newFile.createNewFile();
				file = newFile;
			}
		} catch (Exception ex) {
			Log.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return file;
	}


}
