package com.github.momafoudh.sushibot.exchange.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;
import com.binance.api.client.exception.BinanceApiException;
import com.github.momafoudh.sushibot.exchange.PriceEndpoint;


/**
 * Examples on how to get market data information such as the latest price of a
 * symbol, etc.
 */
public class BinanceOperationsLive implements PriceEndpoint {

	public static void main(String[] args) {
		BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
		BinanceApiRestClient client = factory.newRestClient();

		// Getting latest price of a symbol
		TickerStatistics tickerStatistics = client.get24HrPriceStatistics("NEOETH");
		System.out.println(tickerStatistics.getAskPrice());

		try {
			client.getOrderBook("UNKNOWN", 10);
		} catch (BinanceApiException e) {
			System.out.println(e.getError().getCode()); // -1121
			System.out.println(e.getError().getMsg()); // Invalid symbol
		}
	}

	@Override
	public double getAssetAskPrice(String assetPair) {
		BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
		BinanceApiRestClient client = factory.newRestClient();

		// Getting latest price of a symbol
		TickerStatistics tickerStatistics = client.get24HrPriceStatistics(assetPair);

		return Double.parseDouble(tickerStatistics.getAskPrice());
	}
}