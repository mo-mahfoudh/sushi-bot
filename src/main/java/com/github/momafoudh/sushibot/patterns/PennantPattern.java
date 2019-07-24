package com.github.momafoudh.sushibot.patterns;

import static com.github.momafoudh.sushibot.common.Utils.extractDataAtEvenPositions;
import static com.github.momafoudh.sushibot.common.Utils.extractDataAtOddPositions;
import static com.github.momafoudh.sushibot.common.Utils.isSortedInDecreasingOrder;
import static com.github.momafoudh.sushibot.common.Utils.isSortedInIncreasingOrder;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import com.github.momafoudh.sushibot.manager.BuyPriceProvider;

/**
 * 
 * @author mohamed
 *
 */
public class PennantPattern extends Pattern {

	public PennantPattern(BuyPriceProvider bp, long patternValidity, double noise) {
		super(bp, patternValidity, noise);
	}

	@Override
	public void patternFind() {
		System.out.println("checking for pattern ");
		startTime = new Date();
		double[] prices = getInitData(6);// fill datastructure to start the algorithm

		while (new Date().getTime() - startTime.getTime() < maxExectuionTime) {
			// check for pattern
			double[] dataAtEvenPositions = extractDataAtEvenPositions(prices);
			double[] dataAtOddPositions = extractDataAtOddPositions(prices);

			// found
			boolean isSortedIncreasing = isSortedInIncreasingOrder(dataAtEvenPositions);
			boolean isSortedDecreasing = isSortedInDecreasingOrder(dataAtOddPositions);

			if (isSortedIncreasing && isSortedDecreasing) {
				patternBuyPrice = prices[prices.length - 1];
				patternFoundTime = new AtomicReference<Date>(new Date());
				patternData = prices;
				System.out.println("pattern found for data " + Arrays.toString(prices));
				System.out.println("pattern date" + patternFoundTime);
				patternValidityWatch();
				return;

			}

			// not found
			else {
				prices = getUpdatedArrayFor(prices);

			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private double[] getUpdatedArrayFor(double[] prices) {
		// System.out.println("Data to shift " + Arrays.toString(prices));

		double newValue = buyPriceProvider.getPrice();

		while (Math.abs(newValue - prices[prices.length - 1]) <= noise) {// loop until a new value is big engough
			newValue = buyPriceProvider.getPrice();

		}

		double[] newArray = new double[prices.length];
		newArray[newArray.length - 1] = newValue;
		int j = 0;
		for (int i = 1; i < prices.length && j < newArray.length - 1; i++, j++) {

			newArray[j] = prices[i];

		}
		return newArray;
	}

	private double[] getInitData(int size) {
		double[] prices = new double[size];
		double lastCheckedPrice = 0;
		while ((lastCheckedPrice = buyPriceProvider.getPrice()) == 0)
			;
		prices[0] = lastCheckedPrice;

		int i = 1;
		while (i < size) {

			double newPrice = buyPriceProvider.getPrice();
			if (Math.abs(lastCheckedPrice - newPrice) > noise) {
				prices[i] = newPrice;
				lastCheckedPrice = prices[i - 1];
				i++;
			}

		}
		return prices;

	}
}
