package com.trading.patterns;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import com.trading.manager.BuyPriceProvider;

public abstract class Pattern {

	protected Date startTime;
	protected AtomicReference<Date> patternFoundTime = new AtomicReference<Date>();

	protected double patternBuyPrice;
	protected double[] patternData;
	protected BuyPriceProvider buyPriceProvider;
	protected long PatternValidity;
	protected long maxExectuionTime = 1000 * 600;
	protected double noise;
	protected String asset;

	public Pattern(BuyPriceProvider bp, long patternValidity, double noise) {
		this.buyPriceProvider = bp;
		this.PatternValidity = patternValidity;
		this.noise = noise;

	}

	public abstract void patternFind();

	void patternValidityWatch() {

		Thread t = new Thread(new Runnable() {

			public void run() {
				String watchString = "watching pattern validity ";
				while (true) {

					boolean patternAuthorizeTimeExpired = (new Date().getTime()
							- getPatternFoundTime().getTime() > PatternValidity);
					double newPrice = buyPriceProvider.getPrice();
					boolean patternFaded = newPrice < patternBuyPrice;
					if (patternAuthorizeTimeExpired || patternFaded) {

						System.out.println("pattern no longer valid -> expired ? :" + patternAuthorizeTimeExpired
								+ " - faded ? : " + patternFaded + " duration "
								+ (new Date().getTime() - getPatternFoundTime().getTime()) + " ms");
						reset();
						break;

					}

				}

			}
		});

		t.start();
	}

	public synchronized Date getPatternFoundTime() {

		return patternFoundTime.get();
	}

	void reset() {

		patternFoundTime = null;
		patternBuyPrice = 0;
	}

	public synchronized double[] getPatternData() {
		return patternData;
	}
}
