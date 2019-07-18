package com.trading.exchange.kraken;

public enum KrakenTradingPairs {
	//total minium budget 171
 	XRPEUR(30,0.0016,0.0026), //21
	REPEUR(0.3,0.0016,0.0026),  //9,54
	ETHEUR(0.02,0.0016,0.0026), //11,26
	ETCEUR(0.3,0.0016,0.0026),  //5,31
	EOSEUR(3,0.0016,0.0026), //47
	XLMEUR(30,0.0016,0.0026),//10,8
	LTCEUR(0.1,0.0016,0.0026),//12,3
	DASHEUR(0.03,0.0016,0.0026),//11,85
	BCHEUR(0.002,0.0016,0.0026),//2,4
	ZECEUR(0.03,0.0016,0.0026),//7,2
	XMREUR(0.1,0.0016,0.0026), //20,4
	XBTEUR(0.002,0.0016,0.0026);//15
 	
 
	double min;
	double feesm;
	double feest;
private KrakenTradingPairs(double min,double feesm,double feest){this.min=min;this.feesm=feesm;this.feest=feest;}



public double getMin() {
	return min;
}



public void setMin(double min) {
	this.min = min;
}



public double getFeesm() {
	return feesm;
}



public void setFeesm(double feesm) {
	this.feesm = feesm;
}



public double getFeest() {
	return feest;
}



public void setFeest(double feest) {
	this.feest = feest;
}



public static void main (String args[]){
	int i=0;
	while(i<KrakenTradingPairs.values().length){
	KrakenTradingPairs a=KrakenTradingPairs.values()[i];
	System.out.println(a.name()+" min "+a.getMin()+" feest "+a.getFeest()+" feesm "+a.getFeesm());
	i++;
	}
}




}
