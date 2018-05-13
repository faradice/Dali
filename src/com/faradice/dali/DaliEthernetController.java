package com.faradice.dali;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DaliEthernetController extends ADaliController {
	
	final String ip;
	final int port;
	Socket daliSocket = null;
	boolean keepListen = true;
	byte[] buffer = new byte[125000];
	int len = 0;

	public DaliEthernetController(String ip, int port) throws Exception {
		this.ip = ip;
		this.port = port;
		open();
	}

	@Override
	public void open() throws IOException {
		if (daliSocket != null) {
			daliSocket.close();
			DaliUtil.delay(500);;
		}
		daliSocket = new Socket(ip, port);
	}
	
	public void createListener() {
		System.out.println("Dali listening to " + ip + ":" + port);
		new Thread() {
			@Override
			public void run() {
				try {
					InputStream inFromDali = daliSocket.getInputStream();
					int len;
					while (keepListen) {
						len = inFromDali.available();
						if (len > 0) {
							len = inFromDali.read(buffer, 0, len);
							System.out.print("Received from Dali: ");
							System.out.write(buffer, 0, len);
							System.out.println();
						}
						Thread.sleep(200);
					}
					System.out.println("Dali Stopped listening to port " + port);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void writeToDriver(char c)  throws IOException {
		OutputStream outToServer = daliSocket.getOutputStream();
		outToServer.write(c);
	}
	
	public void close() {
		if (daliSocket != null) {
			try {
				daliSocket.close();
			} catch (Exception e) {
				Log.error("Could not close dali socket", e);
			}
		}
	}

	
	public static void main(String[] args) throws Exception {
		System.out.println("starting dali");

//		String ip = "192.168.1.241";
//		int port = 23;

		String ip = "194.144.250.105";
		int port = 8023;
		
		
/*		
		if (args.length > 0) {
			ip = args[0];
		}
*/		
		DaliEthernetController dl = new DaliEthernetController(ip, port);
		dl.createListener();
		
//		dl.daliMax("02");
//		dl.daliOff("02");
				
		String address = "0";
		DaliUtil.delay(300);
		dl.toDali(address, "0");
		DaliUtil.delay(1000);
		dl.toDali(address, "EE");
		DaliUtil.delay(1000);
		dl.toDali(address, "F5");
		DaliUtil.delay(1000);
		dl.toDali(address, "FE");
		DaliUtil.delay(1000);
		dl.toDali(address, "A5");
		DaliUtil.delay(1000);
		dl.toDali(address, "B5");
		DaliUtil.delay(1000);
		dl.toDali(address, "C5");
		DaliUtil.delay(1000);
		dl.toDali(address, "D5");
		DaliUtil.delay(1000);
		dl.toDali(address, "E5");
		DaliUtil.delay(1000);
		dl.toDali(address, "15");
		DaliUtil.delay(1000);
		dl.toDali(address, "25");
		DaliUtil.delay(1000);
		dl.toDali(address, "35");
		DaliUtil.delay(1000);
		dl.toDali(address, "45");
		DaliUtil.delay(1000);
		dl.toDali(address, "55");
		DaliUtil.delay(1000);
		dl.toDali(address, "65");
		DaliUtil.delay(1000);
		dl.toDali(address, "75");
		DaliUtil.delay(1000);
		dl.toDali(address, "85");
		DaliUtil.delay(1000);
		dl.toDali(address, "95");
		DaliUtil.delay(1000);
		dl.toDali(address, "A5");
		DaliUtil.delay(1000);
		dl.toDali(address, "B5");
		DaliUtil.delay(1000);
		dl.toDali(address, "C5");
		DaliUtil.delay(300);
		dl.toDali(address, "D5");
		DaliUtil.delay(300);
		dl.toDali(address, "E5");
		DaliUtil.delay(300);
		dl.toDali(address, "35");
		DaliUtil.delay(300);
		dl.toDali(address, "25");
//		dl.daliOff(address);
	
	
		
		if (true) return;
		DaliUtil.delay(1000);
//		dl.testDaliAddresses();
		DaliUtil.delay(1000);
//		dl.daliOff("02");
/*
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Dali: ");
			String line = sc.nextLine();
			System.out.println();
			if (line.toLowerCase().startsWith("q")) {
				System.out.println("bye");
				System.exit(0);
			}
	 		try {
				dl.toDali(line);
     		} catch (Exception ex) {
     			System.out.println(ex.getMessage());
     			dl.open();
     		}
			Thread.sleep(500);
		}
		*/
	}	
}
