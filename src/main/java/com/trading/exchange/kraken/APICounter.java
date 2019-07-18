package com.trading.exchange.kraken;

public class APICounter {

	public static volatile int API_CALLS;
	static volatile int APICunterThreashHold=5;//the api counter value above it the counter must be reset


	public static void pauseAPICALLS(int time){
	//	System.out.println("Pausing API Calls by "+time);

		
		try {
			Thread.sleep(time);
			int number=time/3000 ;	
			API_CALLS-=number;
			if(API_CALLS<0)API_CALLS=0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	
	public static void decreaseAPICALLSBy(int number){
 	System.out.println("decreasing api calls counter by "+number +" current counter "+API_CALLS);
		int time=number*3*1000 ;
		try {
			Thread.sleep(time);
			API_CALLS-=number;
			if(API_CALLS<0)API_CALLS=0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void increaseAPICALLSBy(int number){
//		System.out.println("increasing api calls counter by "+number +" current counter "+API_CALLS);
		 API_CALLS+=number;
		
		
	}	
	
	
	
	
}
