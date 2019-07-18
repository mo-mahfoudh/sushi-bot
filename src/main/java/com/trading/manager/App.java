package com.trading.manager;

import com.trading.charts.RealTimePricesChart;
import com.trading.exchange.PriceEndpoint;
import com.trading.exchange.binance.BinanceOperationsLive;
import com.trading.exchange.kraken.KrakenOperationsLive;
import com.trading.exchange.kraken.KrakenTradingPairs;
import com.trading.patterns.Pattern;
import com.trading.patterns.PennantPattern;

/**
 * 
 * @author mohamed
 *
 */
public class App {
	public static void main(String[] args) throws InterruptedException {
		String currency = KrakenTradingPairs.BCHEUR.name();
		PriceEndpoint kraken = new KrakenOperationsLive();
		PriceEndpoint binance = new BinanceOperationsLive();
		BuyPriceProvider priceProvider = new BuyPriceProvider(currency, kraken);

		RealTimePricesChart swingWorkerRealTime = new RealTimePricesChart(priceProvider);
		swingWorkerRealTime.go();

		long patternValidity = 1000 * 100;
		double noise = 0.000001;

		Pattern pennant = new PennantPattern(priceProvider, patternValidity, noise);

		while (true) {

			if (pennant.getPatternData() != null)
				RealTimePricesChart.patternToShow = pennant.getPatternData();
			pennant.patternFind();
			Thread.sleep(10000);

		}
	}
}
