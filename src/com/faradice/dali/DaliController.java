package com.faradice.dali;

import java.io.Closeable;
import java.io.IOException;

public class DaliController extends ADaliController  implements Closeable {
	public static String DALI_COMMAND_FILE = "NewDaliCommand";
	
	public static String DALI_MAX = "";
	public static String DALI_MEDIUM = "";
	public static String DALI_OFF = "0130313030313046463030454617";
	public static String DALI_MEDIUM_LOW = "";
	public static String DALI_LOW = "";

	
	@Override
	public void open() throws IOException {
		// file open and close will be implementead on write
	}
	
	
	@Override
   	public void writeToDriver(char c)  throws IOException {
	}
	
	@Override
	public void daliMax(String addr) throws Exception {
		sendDaliCommand(DALI_MAX);
	}
	
	@Override
	public void daliMed(String addr) throws Exception {
		sendDaliCommand(DALI_MEDIUM);
	}

	@Override
	public void daliMedLow(String addr) throws Exception {
		sendDaliCommand(DALI_MEDIUM_LOW);
	}

	@Override
	public void daliLow(String addr) throws Exception {
		sendDaliCommand(DALI_LOW);
	}
	
	@Override
	public void daliOff(String addr) throws Exception {
		sendDaliCommand(DALI_OFF);
	}
	
	
	public void sendDaliCommand(String msg) throws Exception {
		System.out.println("Write cmd to dali:");
		DaliUtil.delay(50);
		FaraFiles.deleteFile(DALI_COMMAND_FILE);
		FaraFiles.appendToFile(DALI_COMMAND_FILE, msg);
	}

	@Override
	public void toDali(String msg) throws Exception {
	}
	
	/**
	 * Sends a light strength command to Dali
	 * @param address  Address, Id of light from 00 to FE.  FF = Broadcast
	 * @param strength number representing light strength
	 * @throws Exception
	 */
	@Override
	public void toDali(String address, String strength) throws Exception {
	}
	
	public void close() {
		// close file or connection
	}
		
}
