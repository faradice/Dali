package com.faradice.dali;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileCommands {

	final static int MIN_CHECK_INTERVAL = 2000;

	private String lastCommandLine = "";
	private long lastCheck = 0;
	private long lastFileSize = 0;
	private final byte[] buffer = new byte[512];
	private final String fileName;

	public FileCommands(String fileName) {
		this.fileName = fileName;
		start();
	}

	public void start() {
		Log.info("Will monitor command file: " + fileName);
//		FaraFiles.deleteFile(fileName);
	}

	public void stop() {
		Log.info("Stop monitoring command file: " + fileName);
	}

	public String received() {
		String line = "";
		try {
			long start = System.currentTimeMillis();
			try {
				line = getNewCommand();
				if (line == null) {
					line = "";
				}
				if (line.length() < 3) {
					line = "";
				}
			} catch (Throwable e) {
				line = "";
				Log.error(e.getMessage(), e);
			}
			long end = System.currentTimeMillis();
			Log.debug("Command read in " + (end - start) + " ms");
		} catch (Throwable ex) {
			Log.error("Error reading expernal command line");
		}
		return line;
	}

	private String getNewCommand() throws IOException, InterruptedException {
		long now = System.currentTimeMillis();
		if (now - MIN_CHECK_INTERVAL < lastCheck) {
			return "";
		}
		lastCheck = System.currentTimeMillis();
		File pilotFile = new File(fileName);
		if (!pilotFile.exists()) {
			Log.debug("Command File not found: " + pilotFile.getAbsolutePath());
			return "";
		}
		long fileSizeNow = pilotFile.length();
		if (fileSizeNow  < lastFileSize + 5) {
			Log.debug("No new commands in file: " + pilotFile.getAbsolutePath());
			return "";
		}

		String result = "";
		try (RandomAccessFile rfidInput = new RandomAccessFile(pilotFile, "r")) {
			long seekPos = Math.max(0, fileSizeNow - (buffer.length - 4));
			rfidInput.seek(seekPos);
			int len = rfidInput.read(buffer);
			result = FaraFiles.getLastCompleteLineInBuffer(buffer, len);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		lastCheck = System.currentTimeMillis();
		lastFileSize = fileSizeNow;
		if (result.equals(lastCommandLine)) {
			return "";
		} else {
			lastCommandLine = result;
			return result;
		}
	}
}
