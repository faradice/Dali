package com.faradice.dali;

import java.util.ArrayList;

/**
 * High level Dali methods to communicate with the Dali ASCII protocol
 * in a convenient way
 */
public class DaliUtil {
	public static char SOH = 0x01;
	public static char ETB = 0x17;

	public static char[] testCmd() {
		char[] data = { '0', '1', '0', '0', '1', '0', 'F', 'F', '1', '0' };
		char[] cmd = cmdFor(data);
		return cmd;
	}
	
	public static char[] cmdFor(char[] data) {
		char[] chkSum = checksum(data);
		int len = 1 + data.length + chkSum.length + 1;
		char[] cmd = new char[len];

		cmd[0] = SOH;
		for (int i = 0; i < data.length; i++) {
			cmd[1 + i] = data[i];
		}
		cmd[cmd.length - 3] = chkSum[0];
		cmd[cmd.length - 2] = chkSum[1];
		cmd[cmd.length - 1] = ETB;
		return cmd;
	}

	public static char[] checksum(char[] data) {
		int sum = 0;
		Short[] values = asciiToValues(data);
		for (int i : values) {
			sum += i;
		}
		short checkSum = (short) (sum % 0x100);
		checkSum = (short) (0xFF - checkSum);
		return asciiOf(checkSum);
	}


	public static char[] asciiOf(short value) {
		char first = (char) ((value >> 4) & 0xF);
		char second = (char) (value & 0xF);
		return new char[] { singleAsciiOf(first), singleAsciiOf(second) };
	}

	public static Short[] asciiToValues(char[] ascii) {
		byte first = 0;
		byte second = 0;
		if (ascii.length % 2 != 0) {
			throw new RuntimeException("invalid length of ascii command " + ascii.length);
		}
		ArrayList<Short> valueList = new ArrayList<>();
		for (int i = 0; i < ascii.length; i++) {
			if ((i + 1) % 2 == 0) {
				second = byteOf(ascii[i]);
				short value = (short) (first * 16 + second);
				valueList.add(value);
			} else {
				first = byteOf(ascii[i]);
			}
		}
		Short[] result = new Short[valueList.size()];
		return valueList.toArray(result);
	}

	public static byte byteOf(char ascii) {
		if (ascii < 48 || ascii > 70)
			throw new RuntimeException("Invaid ascii number: " + ascii);
		if (ascii <= 0x39) {
			return (byte) (ascii - 0x30);
		} else if (ascii >= 0x41) {
			return (byte) (ascii - 0x37);
		} else {
			throw new RuntimeException("Invaid ascii number: " + ascii);
		}
	}

	public static char singleAsciiOf(char number) {
		if (number < 0 || number > 15)
			throw new RuntimeException("cannot convert " + number + " to one Hex Ascii value");
		if (number < 10) {
			return (char) (number + 0x30);
		} else {
			return (char) (number + 0x37);
		}
	}
	
	public static void delay(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
