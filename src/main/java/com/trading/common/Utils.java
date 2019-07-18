package com.trading.common;

import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * @author mohamed
 *
 */
public class Utils {

	public static boolean isSortedInDecreasingOrder(double[] data) {

		for (int i = 0; i < data.length - 1; i++) {
			if (data[i] > data[i + 1]) {
				return false;
			}
		}

		return true;
	}

	public static boolean isSortedInIncreasingOrder(double[] data) {

		for (int i = 0; i < data.length - 1; i++) {
			if (data[i] < data[i + 1]) {
				return false;
			}
		}

		return true;
	}

	public static double[] extractDataAtEvenPositions(double[] data) {

		int i = 0;

		int outputAarraySize = 0;

		if (data.length % 2 == 0)
			outputAarraySize = data.length / 2;
		else
			outputAarraySize = (data.length / 2) + 1;

		double[] outputArray = new double[outputAarraySize];
		int j = 0;

		while (i < data.length && j < outputAarraySize) {
			// System.out.println("extracting data from Even Pos");

			if (i % 2 == 0) {

				outputArray[j] = data[i];
				j++;
			}
			i++;
		}

		// System.out.println("Data at even positions " + Arrays.toString(outputArray));
		return outputArray;
	}

	public static double[] extractDataAtOddPositions(double[] data) {

		int i = 0;

		int outputAarraySize = data.length / 2;

		double[] outputArray = new double[outputAarraySize];
		int j = 0;
		while (i < data.length && j < outputAarraySize) {

			if (i % 2 != 0) {
				outputArray[j] = data[i];
				j++;
			}
			i++;
		}
		// System.out.println("Data at odd positions " + Arrays.toString(outputArray));

		return outputArray;

	}

	/**
	 * 
	 * @param subArray
	 * @param parentArray
	 * @return
	 */
	public static int findArrayIndex(double[] subArray, double[] parentArray) {
		return Collections.indexOfSubList(Arrays.asList(subArray), Arrays.asList(parentArray));
	}

}