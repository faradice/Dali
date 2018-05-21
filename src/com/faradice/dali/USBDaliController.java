package com.faradice.dali;

import java.io.IOException;

/*
 * Dali controller (driver) to deliver Dali commands using USB or RS232 (Serial)
 * 
 * Supports a connection to a standard Dali controller using USB interface on Rasperry Pi
 * 
 * Information regarding hardware plugin:
 * https://ubuntuforums.org/showthread.php?t=1716756
 *
 * Without USB cable connected to pi:  
 * $ lsusb
 * (Should not show the device) so plug in USB and run command again and device should be listed:
 * Bus 001 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
 * 
 *  dmesg | tail
 *  usb 1-1.5: pl2303 converter now attached to ttyUSB0
 *  lsusb -v -d 1d6b:0002
 * 
 */

public class USBDaliController  extends ADaliController {

	public void close() throws IOException {
		Log.info("Close USB Dali controller");
	}

	@Override
	void open() throws IOException {
		Log.info("Open USB Dali controller");
	}

	@Override
	void writeToDriver(char c) throws IOException {
		Log.info("Write "+c);
	}

}
