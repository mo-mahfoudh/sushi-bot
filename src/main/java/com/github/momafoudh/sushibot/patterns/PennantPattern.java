package com.github.momafoudh.sushibot.patterns;

import static com.github.momafoudh.sushibot.common.Utils.extractDataAtEvenPositions;
import static com.github.momafoudh.sushibot.common.Utils.extractDataAtOddPositions;
import static com.github.momafoudh.sushibot.common.Utils.getArrayFromDtos;
import static com.github.momafoudh.sushibot.common.Utils.isSortedInDecreasingOrder;
import static com.github.momafoudh.sushibot.common.Utils.isSortedInIncreasingOrder;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import com.github.momafoudh.sushibot.common.Log;
import com.github.momafoudh.sushibot.dto.PriceDataDto;
import com.github.momafoudh.sushibot.dto.Status;
import com.github.momafoudh.sushibot.manager.BuyPriceProvider;

/**
 * 
 * @author mohamed
 *
 */
public class PennantPattern extends Pattern {
	public static final String PROCESS_ID = UUID.randomUUID().toString();
	private static Log trends = new Log("trends");

	public PennantPattern(BuyPriceProvider bp, long patternValidity, double noise) {
		super(bp, patternValidity, noise);
	}

	@Override
	public void patternFind() {
		startTime = new Date();
		currentStatus = Status.STARTED;
		PriceDataDto[] pricesDtos = getInitData(6);// fill datastructure to start the algorithm
		if (!started)
			return;
		while (new Date().getTime() - startTime.getTime() < maxExectuionTime) {
			currentStatus = Status.PROCESSING;
			// check for pattern
			if (!started)
				return;
			double[] dataAtEvenPositions = extractDataAtEvenPositions(pricesDtos);
			double[] dataAtOddPositions = extractDataAtOddPositions(pricesDtos);

			// found
			boolean isSortedIncreasing = isSortedInIncreasingOrder(dataAtEvenPositions);
			boolean isSortedDecreasing = isSortedInDecreasingOrder(dataAtOddPositions);

			if (isSortedIncreasing && isSortedDecreasing) {
				patternBuyPrice = getArrayFromDtos(pricesDtos)[pricesDtos.length - 1];
				patternFoundTime = new AtomicReference<Date>(new Date());
				this.patternData = pricesDtos;
				trends.info(Arrays.asList(pricesDtos).toString());
				System.out.println("pattern found for data " + Arrays.toString(pricesDtos));
				System.out.println("pattern date" + patternFoundTime);
				currentStatus = Status.PATTERN_FOUND;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				patternValidityWatch();
				return;

			}

			// not found
			else {
				pricesDtos = getUpdatedArrayFor(pricesDtos);
				currentStatus = Status.PATTERN_NOT_FOUND;

			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private PriceDataDto[] getUpdatedArrayFor(PriceDataDto[] prices) {
		currentStatus = Status.LOADING_OTHER_DATA;
		PriceDataDto lastCheckedPrice = null;
		while (lastCheckedPrice == null && started) {
			PriceDataDto price = buyPriceProvider.getPriceAsPriceDataDto(this.getClass());

			if (price != null && price.getPriceValue() > 0)
				lastCheckedPrice = price;
		}

		PriceDataDto newValue = lastCheckedPrice;
		while (Math.abs(newValue.getPriceValue() - prices[prices.length - 1].getPriceValue()) <= noise) {
			// loop until a new value is
			// big engough

			newValue = buyPriceProvider.getPriceAsPriceDataDto(this.getClass());

		}

		PriceDataDto[] newArray = new PriceDataDto[prices.length];
		newArray[newArray.length - 1] = newValue;
		int j = 0;
		for (int i = 1; i < prices.length && j < newArray.length - 1; i++, j++) {

			newArray[j] = prices[i];

		}
		if (!started)
			return null;
		return newArray;
	}

	private PriceDataDto[] getInitData(int size) {

		currentStatus = Status.INITIALIZATION;

		PriceDataDto[] prices = new PriceDataDto[size];
		PriceDataDto lastCheckedPrice = null;
		while (lastCheckedPrice == null) {
			PriceDataDto price = buyPriceProvider.getPriceAsPriceDataDto(this.getClass());
			if (price != null && price.getPriceValue() > 0)
				lastCheckedPrice = price;
		}

		prices[0] = lastCheckedPrice;
		int i = 1;
		while (i < size) {
			PriceDataDto newPrice = buyPriceProvider.getPriceAsPriceDataDto(this.getClass());

			// check if the price has changed significantly
			if (Math.abs(lastCheckedPrice.getPriceValue() - newPrice.getPriceValue()) > noise) {
				prices[i] = newPrice;
				lastCheckedPrice = newPrice;
				i++;
			}
			if (!started)
				return null;

		}

		return prices;

	}

}
