package com.github.momafoudh.sushibot.dto;

public enum Status {
	STARTED("Started", 1), INITIALIZATION("Initialization", 2), PROCESSING("Processing", 3),
	PATTERN_FOUND("Pattern found", 4), WATCHING_VALIDTY("Watching validity", 5), PATTERN_EXPIRED("Pattern expired", 6),
	PATTERN_NOT_FOUND("Pattern not found", 7), LOADING_OTHER_DATA("Loading other data", 8);

	String statusName;
	int statusValue;

	private Status(String statusName, int statusValue) {
		this.statusName = statusName;
		this.statusValue = statusValue;
	}

	public String toString() {
		return statusName + "|" + statusValue;
	}
}
