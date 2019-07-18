package com.trading.manager;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

import com.trading.exchange.PriceEndpoint;

/**
 * 
 * @author mohamed
 *
 */
public class BuyPriceProvider {
// matchingPrices is for testing purposes only
	public LinkedList matchingPrices = new LinkedList<Double>();
	private AtomicReference<Double> currentPrice = new AtomicReference(0.0);
	private PriceEndpoint priceEndpoint;
	private String assetPair;

	public BuyPriceProvider(String assetPair, PriceEndpoint p) {
		this.priceEndpoint = p;

		initTestData();
		this.assetPair = assetPair;
		updatePricePeriodically();

	}

	private void initTestData() {
		matchingPrices.addLast(9.0);
		matchingPrices.addLast(1.1);

		matchingPrices.addLast(8.0);
		matchingPrices.addLast(2.0);

		matchingPrices.addLast(7.0);
		matchingPrices.addLast(3.0);

		matchingPrices.addLast(6.0);
		matchingPrices.addLast(4.0);
	}

	public synchronized double getPrice() {

		return currentPrice.get();

	}

	private void updatePricePeriodically() {

		Thread t = new Thread(new Runnable() {

			public void run() {
				// Date start = new Date();
				// boolean showTestMessage = true;
				while (true) {
					// here we serve prices that matche pennant partern after x seconds for testing
					// purposes
					/*
					 * if (new Date().getTime() - start.getTime() > 1000 * 20 &&
					 * matchingPrices.size() > 0) { currentPrice = (Double) matchingPrices.poll();
					 * if (showTestMessage)
					 * System.out.println("serving matching prices for test prupose");
					 * showTestMessage = false; }
					 * 
					 * else { double rangeMin = 1.0; double rangeMax = 10.0; currentPrice = rangeMin
					 * + (rangeMax - rangeMin) * new Random().nextDouble(); }
					 */

					currentPrice = new AtomicReference<Double>(priceEndpoint.getAssetAskPrice(assetPair));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		t.start();

	}

	public String getAssetPair() {
		return assetPair;
	}
}
