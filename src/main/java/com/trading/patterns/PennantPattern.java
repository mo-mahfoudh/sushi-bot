package com.trading.patterns;

import static com.trading.patterns.common.Utils.extractDataAtEvenPositions;
import static com.trading.patterns.common.Utils.extractDataAtOddPositions;
import static com.trading.patterns.common.Utils.isSorted;
import static com.trading.patterns.common.Utils.isSortedArrayDecreasing;
import static com.trading.patterns.common.Utils.isSortedArrayIncreasing;

import java.util.Date;

import com.trading.remote.BuyPriceProvider;

/**
 * 
 * @author mohamed
 *
 */
public class PennantPattern extends Pattern {

	public PennantPattern(BuyPriceProvider bp, long patternValidity, String asset, double noise) {
		super(bp, patternValidity, asset, noise);
	}

	@Override
	public void patternFind() {
		System.out.println("pennant pattern lookup started");
		startTime = new Date();
		double[] prices = getInitData(5);// fill datastructure to start the algorithm
		System.out.println(new Date().getTime() - startTime.getTime());
		while (new Date().getTime() - startTime.getTime() < maxExectuionTime) {
			System.out.println("checking for pattern ");
			// check for pattern
			double[] dataAtEvenPositions = extractDataAtEvenPositions(prices);
			double[] dataAtOddPositions = extractDataAtOddPositions(prices);

			// found
			if (isSorted(dataAtEvenPositions) && isSorted(dataAtOddPositions)
					&& isSortedArrayDecreasing(dataAtEvenPositions) && isSortedArrayIncreasing(dataAtOddPositions)) {

				patternBuyPrice = prices[prices.length - 1];
				patternFoundTime = new Date();
				System.out.println(" pattern found ");

				patternValidityWatch();
				return;

			}

			// not found
			else {
				System.out.println("no pattern found ...continue lookup");
				prices = getUpdatedArrayFor(prices);
			}
		}
	}

	private double[] getUpdatedArrayFor(double[] prices) {

		double newValue = buyPriceProvider.getPrice(asset);

		while (Math.abs(newValue - prices[prices.length - 1]) < noise) {// loop until a new value is big engough
			newValue = buyPriceProvider.getPrice(asset);
			System.out.println("no matching price found");

		}

		double[] newArray = new double[prices.length];
		newArray[newArray.length - 2] = newValue;
		for (int i = 1; i < newArray.length - 2; i++) {

			newArray[i - 1] = prices[i];
		}

		return newArray;
	}

	private double[] getInitData(int size) {
		System.out.println("data initialization started");
		double[] prices = new double[size];
		double lastCheckedPrice = buyPriceProvider.getPricesFromMatchingPatternForTestPurpose();
		prices[0] = lastCheckedPrice;

		int i = 1;
		while (i < size) {

			double newPrice = buyPriceProvider.getPricesFromMatchingPatternForTestPurpose();

			if (Math.abs(lastCheckedPrice - newPrice) > noise) {
				prices[i] = newPrice;
				i++;
			}

		}
		System.out.println("data initialization finished");
		return prices;

	}
}
