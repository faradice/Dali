package com.faradice.dali;

import java.io.Closeable;
import java.io.IOException;

public abstract class ADaliController implements Closeable {
	abstract void open() throws IOException;
	abstract void writeToDriver(char c)  throws IOException;
	
	public void testDaliAddresses() throws Exception {
		for (short i=0; i< 64; i++) {
			System.out.println("Send Max to address: "+i);
			daliOff("FE");
			DaliUtil.delay(500);
			daliMax(i+"");
		}
		daliOff("FE"); 
	}
	
	public void daliMax(String addr) throws Exception {
		if (addr.length() < 2) addr = "0"+addr;
		toDali("010010"+addr+"FE");
	}
	
	public void daliMed(String addr) throws Exception {
		if (addr.length() < 2) addr = "0"+addr;
		toDali("010010"+addr+"E5");
	}

	public void daliMedLow(String addr) throws Exception {
		if (addr.length() < 2) addr = "0"+addr;
		toDali("010010"+addr+"C5");
	}

	public void daliLow(String addr) throws Exception {
		if (addr.length() < 2) addr = "0"+addr;
		toDali("010010"+addr+"02");
	}
	
	public void daliOff(String addr) throws Exception {
		if (addr.length() < 2) addr = "0"+addr;
		toDali("010010"+addr+"00");
	}
	
	public void daliOff(short address) throws Exception {
		if (address < 0) {
			address = 0xFF;
		} else if (address > 63) {
			throw new Exception("invalid Dali address: "+address);
		}
		char[] addr = DaliUtil.asciiOf(address);
		toDali("010010"+addr[0]+addr[1]+"00");
	}
	
	public void sendCommand(char[] command) throws Exception {
		System.out.println("Write cmd to dali:");
		DaliUtil.delay(10);
		int cc = 0;
		for (int i = 0; i < command.length; i++) {
			char c = command[i];
			if (c == '\n' || c == '\r' || c == '\t' || c == ' ') {
				continue;
			}
			if (c == DaliUtil.SOH) {
				System.out.print("Z ");
			} else if (c == DaliUtil.ETB) {
				System.out.print("X");
			} else {
				cc++;
				System.out.print(c);
				if ((cc % 2) == 0)
					System.out.print(" ");
			}
			writeToDriver(c);
		}
		System.out.println("\nDone");
	}

	public void toDali(String msg) throws Exception {
		if (msg == null || msg.length() < 1) {
			return;
		}
		msg = msg.toUpperCase();
		char[] chars = new char[msg.length()];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = msg.charAt(i);
		}
		char[] cmd = DaliUtil.cmdFor(chars);
		sendCommand(cmd);
	}
	
	/**
	 * Sends a light strength command to Dali
	 * @param address  Address, Id of light from 00 to FE.  FF = Broadcast
	 * @param strength number representing light strength
	 * @throws Exception
	 */
	public void toDali(String address, String strength) throws Exception {
		if (address.length() < 1 || strength.length() < 1) {
			return;
		}
		if (address.length() > 2 || strength.length() > 2) {
			return;
		}
		if (address.length() < 2) {
			address = "0"+address;
		}
		if (strength.length() < 2) {
			strength = "0"+strength;
		}
		String command = "010010" + address + strength; 
		toDali(command);
	}
	
}
