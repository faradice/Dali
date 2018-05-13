package com.faradice.dali;

import java.io.IOException;

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
