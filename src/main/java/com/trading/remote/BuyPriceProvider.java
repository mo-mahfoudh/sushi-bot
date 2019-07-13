package com.trading.remote;

import java.util.LinkedList;
import java.util.Random;

/**
 * 
 * @author mohamed
 *
 */
public class BuyPriceProvider {

	LinkedList matchingPrices = new LinkedList<Double>();

	public BuyPriceProvider() {

		initTestData();

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

	public double getPrice(String asset) {

		double rangeMin = 1.0;
		double rangeMax = 10.0;
		double randomValue = rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();

		return randomValue;

	}

	public double getPricesFromMatchingPatternForTestPurpose() {
		return (Double) matchingPrices.poll();
	}
}
