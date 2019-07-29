package com.github.momafoudh.sushibot.manager;

import com.github.momafoudh.sushibot.charts.RealTimePricesChart;
import com.github.momafoudh.sushibot.exchange.PriceEndpoint;
import com.github.momafoudh.sushibot.exchange.binance.BinanceOperationsLive;
import com.github.momafoudh.sushibot.exchange.kraken.KrakenOperationsLive;
import com.github.momafoudh.sushibot.exchange.kraken.KrakenTradingPairs;
import com.github.momafoudh.sushibot.patterns.Pattern;
import com.github.momafoudh.sushibot.patterns.PennantPattern;

/**
 * 
 * @author mohamed
 *
 */
public class App {
	public static void main(String[] args) throws InterruptedException {
		// run();
		testClass(PennantPattern.class);

	}

	public static void testClass(Class<?> clazz) {

		System.out.println(Pattern.class.isAssignableFrom(clazz));

	}

	private static void run() {
		String currency = KrakenTradingPairs.XRPEUR.name();
		PriceEndpoint kraken = new KrakenOperationsLive();
		PriceEndpoint binance = new BinanceOperationsLive();
		BuyPriceProvider priceProvider = BuyPriceProvider.getInstance(currency, kraken);

		long patternValidity = 1000 * 100;
		double noise = 0.000001;

		final Pattern pennant = new PennantPattern(priceProvider, patternValidity, noise);
		final RealTimePricesChart swingWorkerRealTime = new RealTimePricesChart(priceProvider, currency);

		Thread patternLookup = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		patternLookup.start();
		Thread chartPlot = new Thread(new Runnable() {

			@Override
			public void run() {

				swingWorkerRealTime.go();

			}
		});
		chartPlot.start();
	}
}
