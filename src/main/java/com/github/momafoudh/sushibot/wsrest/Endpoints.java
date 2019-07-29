package com.github.momafoudh.sushibot.wsrest;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.momafoudh.sushibot.exchange.PriceEndpoint;
import com.github.momafoudh.sushibot.exchange.binance.BinanceOperationsLive;
import com.github.momafoudh.sushibot.exchange.kraken.KrakenOperationsLive;
import com.github.momafoudh.sushibot.manager.BuyPriceProvider;
import com.github.momafoudh.sushibot.patterns.Pattern;
import com.github.momafoudh.sushibot.patterns.PennantPattern;

@Path("/patterns")
public class Endpoints {

	private static Thread patternThread;
	public static AtomicReference<Pattern> patternWrapper = new AtomicReference<>();

	@GET
	@Path("status")
	@Produces(MediaType.TEXT_PLAIN)
	public synchronized String getPatternStatus() {
		if (patternWrapper.get() != null)
			return patternWrapper.get().getCurrentStatus().toString();
		return "not ready";
	}

	@GET
	@Path("start")
	@Produces(MediaType.TEXT_PLAIN)
	public String start(@QueryParam("exchange") String exchange, @QueryParam("tradingpair") String tradingPair,
			@QueryParam("noise") double noise) {

		PriceEndpoint kraken = new KrakenOperationsLive();
		PriceEndpoint binance = new BinanceOperationsLive();

		PriceEndpoint endpoint = null;
		if (exchange.toLowerCase().equals("kraken")) {
			endpoint = kraken;
		}
		if (exchange.toLowerCase().equals("binance")) {

			endpoint = binance;
		}

		BuyPriceProvider priceProvider = BuyPriceProvider.getInstance(tradingPair, endpoint);

		long patternValidity = 1000 * 100;

		Pattern pattern = new PennantPattern(priceProvider, patternValidity, noise);
		patternWrapper = new AtomicReference<Pattern>(pattern);

		patternThread = new Thread(new Runnable() {

			@Override
			public void run() {
				patternWrapper.get().started = true;
				while (patternWrapper.get().started == true) {
					patternWrapper.get().patternFind();

				}
			}
		});
		patternThread.start();

		if (patternWrapper.get() != null)
			return "started " + tradingPair + " " + exchange;
		return "waiting";

	}

	@GET
	@Path("stop")
	@Produces(MediaType.TEXT_PLAIN)
	public String stop() {
		patternThread.interrupt();
		patternWrapper.get().started = false;
		BuyPriceProvider.instance = null;
		return patternThread.getState().name();
	}

	@GET
	@Path("prices")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPrices() {
		if (BuyPriceProvider.instance == null || BuyPriceProvider.instance.collectedPrices == null
				|| BuyPriceProvider.instance.collectedPrices.size() == 0)
			return "not ready" + (BuyPriceProvider.instance.collectedPrices == null);
		return BuyPriceProvider.instance.collectedPrices.toString();
	}

	@GET
	@Path("price")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPrice() {
		if (BuyPriceProvider.instance == null || BuyPriceProvider.instance.collectedPrices == null
				|| BuyPriceProvider.instance.collectedPrices.size() == 0)
			return "not ready";
		return BuyPriceProvider.instance.getPriceAsPriceDataDto(this.getClass()).toString();
	}

	@GET
	@Path("patterndata")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPatternData() {
		if (patternWrapper != null && patternWrapper.get() != null && patternWrapper.get().getPatternData() != null)

			return Arrays.asList(patternWrapper.get().getPatternData()).toString();
		return "not ready";
	}

	public static void main(String... strings) throws InterruptedException {

		Endpoints endpoints = new Endpoints();

		endpoints.start("kraken", "XRPEUR", 0.0);

		int i = 1;
		while (i > 0) {
			endpoints.patternWrapper.get().getPatternData();
			System.out.println(endpoints.patternWrapper.get().getCurrentStatus());
			System.out.println(endpoints.getPrice());

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
