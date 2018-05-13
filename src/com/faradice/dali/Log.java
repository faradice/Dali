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

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	public static void error(String msg, Throwable exc) {
		log(Level.SEVERE, msg, exc);
	}

	public static void error(Throwable exc) {
		error(exc.getMessage(), exc);
	}

	public static void error(String msg) {
		log(Level.SEVERE, msg);
	}

	public static void warning(String msg) {
		log(Level.WARNING, msg);
	}

	public static void warning(String msg, Throwable exc) {
		log(Level.WARNING, msg, exc);
	}

	public static void debug(String msg) {
		log(Level.FINEST, msg);
	}

	public static void info(String msg) {
		System.out.println(msg);
	}

	public static void log(Level l, String m) {
		log(l, m, null);
	}

	public static void log(Level l, String m, Throwable t) {
		if (t == null) {
			logger().log(l, m);
		} else {
			logger().log(l, m, t);
		}
	}

	/** Returns the Faradice Logger */
	private static Logger logger() {
		return Logger.getLogger("Faradice");
	}
}
