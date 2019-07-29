package com.github.momafoudh.sushibot.patterns;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import com.github.momafoudh.sushibot.dto.PriceDataDto;
import com.github.momafoudh.sushibot.dto.Status;
import com.github.momafoudh.sushibot.manager.BuyPriceProvider;

public abstract class Pattern {

	protected Date startTime;
	protected AtomicReference<Date> patternFoundTime = new AtomicReference<Date>();
	protected double patternBuyPrice;
	protected volatile PriceDataDto[] patternData;
	protected BuyPriceProvider buyPriceProvider;
	protected long PatternValidity;
	protected long maxExectuionTime = 1000 * 600;
	protected double noise;
	protected String asset;
	protected Status currentStatus;
	public volatile boolean started;

	public Pattern(BuyPriceProvider bp, long patternValidity, double noise) {
		this.buyPriceProvider = bp;
		this.PatternValidity = patternValidity;
		this.noise = noise;

	}

	public abstract void patternFind();

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	void patternValidityWatch() {

		Thread t = new Thread(new Runnable() {

			public void run() {
				String watchString = "watching pattern validity ";
				currentStatus = Status.WATCHING_VALIDTY;
				while (true) {

					boolean patternAuthorizeTimeExpired = (new Date().getTime()
							- getPatternFoundTime().getTime() > PatternValidity);
					double newPrice = buyPriceProvider.getPrice();
					boolean patternFaded = newPrice < patternBuyPrice;
					if (patternAuthorizeTimeExpired || patternFaded) {
						currentStatus = Status.PATTERN_EXPIRED;
						System.out.println("pattern no longer valid -> expired ? :" + patternAuthorizeTimeExpired
								+ " - faded ? : " + patternFaded + " duration "
								+ (new Date().getTime() - getPatternFoundTime().getTime()) + " ms");
						reset();
						break;

					}

				}

			}
		});

		// t.start();
	}

	public synchronized Date getPatternFoundTime() {

		return patternFoundTime.get();
	}

	void reset() {

		patternFoundTime = null;
		patternBuyPrice = 0;
	}

	public PriceDataDto[] getPatternData() {
		System.out.println(patternData);
		return patternData;
	}

}
