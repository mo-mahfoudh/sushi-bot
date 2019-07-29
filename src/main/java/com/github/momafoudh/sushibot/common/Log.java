package com.github.momafoudh.sushibot.common;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

 


public class Log {
	
	public Logger log;
	
	public Log(String name ) {
		
	 log=Logger.getLogger(name)	;
		
		
	}
	
 

	public static final TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");  
	public static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 
	
	public   void fatal(String message,Throwable e){
		
     dateFormat.setTimeZone(Log.timeZone);
     Date launch = new Date();  
     log.fatal("(1)"+dateFormat.format(launch)+"  "+message );
     log.fatal("(2)"+dateFormat.format(launch),  e );	
      // UI.SetWindowText(message);
     ;}
	
	
	
	public  void fatal(String message){

	 
	 
	 dateFormat.setTimeZone(Log.timeZone);
     Date launch = new Date();  
     log.fatal(dateFormat.format(launch)+" " +message );
  //UI.SetWindowText(message);
	
	}
	
	public   void info(String message){

	 
		 
		 dateFormat.setTimeZone(Log.timeZone);
	     Date launch = new Date();  
	     log.info(dateFormat.format(launch)+" "+message );
	  //UI.SetWindowText(message);
		
		}
	
 public static void main (String args[]){
	 
	 
	 dateFormat.setTimeZone(Log.timeZone);
     Date launch = new Date();  
     
 	 
	 
	 
	 
 }
	

}
