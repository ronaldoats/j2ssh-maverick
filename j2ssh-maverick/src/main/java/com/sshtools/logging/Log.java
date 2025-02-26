/**
 * Copyright 2003-2014 SSHTOOLS Limited. All Rights Reserved.
 *
 * For product documentation visit https://www.sshtools.com/
 *
 * This file is part of J2SSH Maverick.
 *
 * J2SSH Maverick is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * J2SSH Maverick is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with J2SSH Maverick.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sshtools.logging;


/**
 * This is a wrapper class. It allows all log.info and log.debug calls to be
 * replaced with its methods.
 * 
 * @author Lee David Painter
 * 
 */
public final class Log {

	public static boolean isDebugEnabled() {
		return LoggerFactory.isEnabled() && LoggerFactory.getInstance().isLevelEnabled(LoggerLevel.DEBUG);
	}

	public static boolean isInfoEnabled() {
		return LoggerFactory.isEnabled() && LoggerFactory.getInstance().isLevelEnabled(LoggerLevel.INFO);
	}

	public static boolean isErrorEnabled() {
		return LoggerFactory.isEnabled() && LoggerFactory.getInstance().isLevelEnabled(LoggerLevel.ERROR);
	}

	/**
	 * A normal log event
	 * 
	 * @param source
	 * @param message
	 */
	public static void info(Object source, String message) {
		LoggerFactory.getInstance().log(LoggerLevel.INFO, source, message);
	}

	/**
	 * An error log event
	 * 
	 * @param source
	 * @param message
	 */
	public static void error(Object source, String message) {
		LoggerFactory.getInstance().log(LoggerLevel.ERROR, source, message);
	}

	/**
	 * An error log event
	 * 
	 * @param source
	 * @param message
	 */
	public static void debug(Object source, String message, Throwable t) {
		LoggerFactory.getInstance().log(LoggerLevel.DEBUG, source, message, t);
	}
	/**
	 * A debug event
	 * 
	 * @param source
	 * @param message
	 */
	public static void debug(Object source, String message) {
		LoggerFactory.getInstance().log(LoggerLevel.INFO, source, message);
	}

	/**
	 * An exception event
	 * 
	 * @param source
	 * @param message
	 * @param t
	 */
	public static void error(Object source, String message, Throwable t) {
		LoggerFactory.getInstance().log(LoggerLevel.ERROR, source, message, t);
	}
}
