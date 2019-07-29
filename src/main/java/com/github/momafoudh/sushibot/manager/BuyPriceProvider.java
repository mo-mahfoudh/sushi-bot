package com.github.momafoudh.sushibot.manager;

import java.util.Date;
import java.util.LinkedList;

import com.github.momafoudh.sushibot.common.Log;
import com.github.momafoudh.sushibot.dto.PriceDataDto;
import com.github.momafoudh.sushibot.exchange.PriceEndpoint;
import com.github.momafoudh.sushibot.patterns.Pattern;

/**
 * 
 * @author mohamed
 *
 */
public class BuyPriceProvider {
// matchingPrices is for testing purposes only
	public LinkedList matchingPrices = new LinkedList<Double>();
	private PriceEndpoint priceEndpoint;
	private String assetPair;
	private long numberOfDataRead;
	public LinkedList<PriceDataDto> collectedPrices = new LinkedList<>();
	public static BuyPriceProvider instance;
	private static Log transactions = new Log("transaction");

	private BuyPriceProvider(String assetPair, PriceEndpoint p) {
		this.priceEndpoint = p;

		this.assetPair = assetPair;
		if (instance == null)
			instance = this;

	}

	public static BuyPriceProvider getInstance(String assetPair, PriceEndpoint p) {

		new BuyPriceProvider(assetPair, p);
		return instance;

	}

	public static BuyPriceProvider getCurrentInstance() {

		return instance;

	}

	/**
	 * If the caller is a pattern then get a new price store it before returning it
	 * if the caller is not a pattern (graphic chart ) then retreive a stored price
	 * 
	 * @param clazz the class of the caller
	 * @return
	 */
	public PriceDataDto getPriceAsPriceDataDto(Class<?> clazz) {
		if (Pattern.class.isAssignableFrom(clazz)) {
			PriceDataDto price = getNewPriceAndStoreIt();
			transactions.info("*Pattern" + " " + price + " " + collectedPrices.size());
			return price;

		}
		PriceDataDto price = collectedPrices.pollFirst();
		transactions.info("*Non Pattern" + " " + price + " " + collectedPrices.size());
		return price;
	}

	public double getPrice() {

		return priceEndpoint.getAssetAskPrice(assetPair);
	}

	private PriceDataDto getNewPriceAndStoreIt() {
		double priceValue = priceEndpoint.getAssetAskPrice(assetPair);

		PriceDataDto price = new PriceDataDto();
		price.setPriceReadDate(new Date());
		price.setPriceReadSequenceNumber(numberOfDataRead);
		price.setPriceValue(priceValue);
		if (priceValue > 0) {
			collectedPrices.add(price);
			numberOfDataRead++;
		}
		return price;
	}

	public String getAssetPair() {
		return assetPair;
	}
}
