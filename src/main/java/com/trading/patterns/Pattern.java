package com.trading.patterns;

import java.util.Date;

import com.trading.remote.BuyPriceProvider;

public abstract class Pattern {

	protected Date startTime;
	protected Date patternFoundTime;
	protected double patternBuyPrice;
	protected BuyPriceProvider buyPriceProvider;
	protected long PatternValidity;
	protected long maxExectuionTime = 10000;
	protected double noise;
	protected String asset;

	public Pattern(BuyPriceProvider bp, long patternValidity, String asset, double noise) {
		this.buyPriceProvider = bp;
		this.PatternValidity = patternValidity;
		this.noise = noise;

	}

	public abstract void patternFind();

	void patternValidityWatch() {

		Thread t = new Thread(new Runnable() {

			public void run() {

				while (true) {

					if (patternFoundTime != null && ((buyPriceProvider.getPrice(asset) < patternBuyPrice)
							|| (new Date().getTime() - patternFoundTime.getTime() > PatternValidity))) {

						reset();

						break;

					}
				}

			}
		});

		t.start();
	}

	void reset() {

		patternFoundTime = null;
		patternBuyPrice = 0;
	}

}
