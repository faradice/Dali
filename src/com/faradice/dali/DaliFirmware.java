package com.faradice.dali;

public class DaliFirmware {
//	public static final String DATA_FILE_ROOT = "/root/data/";
	public static final String DATA_FILE_ROOT = "/Users/ragnar/fdruntime/dali/";
	public static final String DEFAULT_COMMAND_FILE = DATA_FILE_ROOT + "command.txt";
	public static final String DALI_ROUTER_IP  = "192.168.1.105";


	final DaliEthernetController dc;
	final FileCommands fileCommands;

	public DaliFirmware(String ip, int port, String cmdFileName) throws Exception {
		dc = new DaliEthernetController(ip, port);
		fileCommands = new FileCommands(cmdFileName);
		cmdLoop();
	}

	void cmdLoop() throws Exception {
		System.out.println("Listening to commands...");
		while (true) {
			try {
				DaliUtil.delay(1000);
				DaliCmd cmd = DaliCmd.fromCommand(fileCommands);
				if (cmd == null) continue;
				String[] addresses = cmd.addresses.split(",");
				for (String addr : addresses) {
					if (cmd.value == 0) {
						dc.daliOff(addr);
					} else if (cmd.value < 3) {
						dc.daliLow(addr);
					} else if (cmd.value < 6) {
						dc.daliMedLow(addr);
					} else if (cmd.value < 8) {
						dc.daliMed(addr);
					} else if (cmd.value < 16) {
						dc.daliMax(addr);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class DaliCmd {
		final String addresses;
		final int value;

		public DaliCmd(String addresses, int value) {
			this.addresses = addresses;
			this.value = value;
		}

		@Override
		public String toString() {
			return addresses + "  " + value;
		}

		public static DaliCmd fromCommand(FileCommands fileCommands) {

			String cmd = fileCommands.received();
			if (cmd.length() < 1) {
				return null;
			}
			String addresses = null;
			int value = 0;
			DaliCmd result = null;
			if (cmd.startsWith("DALI")) {
				String[] cols = cmd.split(" ");
				if (cols.length > 1)
					addresses = cols[1];
				if (cols.length > 2) {
					value = Integer.parseInt(cols[2].trim());
				}
				result = new DaliCmd(addresses, value);
			}
			return result;
		}
	}

	public static void main(String[] args) throws Exception {
		String cmdFileName = DEFAULT_COMMAND_FILE;
		String ip = "192.168.1.241";
		int port = 23;
/*		
//		String ip = "194.144.250.105";
//		int port = 8023;
		if (args.length > 0) {
			String[] addr = args[0].split(":");
			ip = addr[0];
			if (addr.length > 1) {
				port = Integer.parseInt(addr[1]);
			}
		}
		if (args.length > 1) {
			cmdFileName = args[1].trim();
		}
		*/
		System.out.println("Connecting to "+ip+":"+port);
		new DaliFirmware(ip, port, cmdFileName);
	}

}
