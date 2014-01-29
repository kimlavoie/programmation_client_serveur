package com;

import java.util.Calendar;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
  * Helper class to log errors and server connections, with the time of occurence
  */
public class Logger{	
	public final static String defaultLogFile = new String("log.txt");
	public final static String defaultErrorFile = new String("errors.txt");

	/**
	  * Log the message in the default log file
	  */
    public static void log(String message){
	
		log(message, defaultLogFile);
	}
	
	/**
	  * Log the stack trace of the exception in the specified file
	  */
	public static void logError(Exception e, String file){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(bos, true);
		e.printStackTrace(pw);
		String errorString = new String(bos.toByteArray());
		String message = "[" + Calendar.getInstance().getTime().toString() + "]   " + errorString;
		System.err.println(message);
		appendToFile(message, file);
	}

	/**
	  * Log the stack trace of the exception in the default error file
	  */	
	public static void logError(Exception e){
		logError(e, defaultErrorFile);
	}

	/**
	  * Log message in the default error file
	  */	
	public static void logError(String message){
		logError(message, defaultErrorFile);
	}

	/**
	  * Log message in the specified file
	  */	
	public static void logError(String message, String file){
		message = "[" + Calendar.getInstance().getTime().toString() + "]   " + "ERROR: "+ message;
	    System.err.println(message);
		appendToFile(message, file);
	}

	/**
	  * Log message in the specified file
	  */	
	public static void log(String message, String file){
		message = "[" + Calendar.getInstance().getTime().toString() + "]   " + message;
	    System.out.println(message);
		appendToFile(message, file);
	}

	/**
	  * Helper function, which append message to the specified file
	  */
	public static void appendToFile(String message, String file){
	    try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(message);
			out.close();
		} catch (IOException e) {
			System.err.println(e.getMessage() + " in Logger.appendToFile()");
		}
	}
}