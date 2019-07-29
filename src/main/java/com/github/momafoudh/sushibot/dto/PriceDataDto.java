package com.github.momafoudh.sushibot.dto;

import java.util.Date;

public class PriceDataDto {

	double priceValue;
	long priceReadSequenceNumber;
	Date priceReadDate;
	boolean partOfPattern;
	String readerId;

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public long getPriceReadSequenceNumber() {
		return priceReadSequenceNumber;
	}

	public void setPriceReadSequenceNumber(long numberOfDataRead) {
		this.priceReadSequenceNumber = numberOfDataRead;
	}

	public Date getPriceReadDate() {
		return priceReadDate;
	}

	public double getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(double priceValue) {
		this.priceValue = priceValue;
	}

	public void setPriceReadDate(Date priceRead) {
		this.priceReadDate = priceRead;
	}

	public boolean isPartOfPattern() {
		return partOfPattern;
	}

	public void setPartOfPattern(boolean partOfPattern) {
		this.partOfPattern = partOfPattern;
	}

	@Override
	public String toString() {
		return priceReadDate + "|" + priceValue + "|" + priceReadSequenceNumber;

	}
}
