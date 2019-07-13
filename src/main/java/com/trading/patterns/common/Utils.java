package com.trading.patterns.common;

/**
 * 
 * @author mohamed
 *
 */
public class Utils {

	public static boolean isSorted(double[] data) {

		for (int i = 0; i < data.length - 1; i++) {
			if (data[i] > data[i + 1]) {
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

		while (i < data.length) {
			System.out.println("extracting data from Even Pos");

			int j = 0;

			if (i % 2 != 0) {

				outputArray[j] = data[i];
				j++;
			}
			i++;
		}
		return outputArray;
	}

	public static double[] extractDataAtOddPositions(double[] data) {

		int i = 0;

		int outputAarraySize = data.length / 2;

		double[] outputArray = new double[outputAarraySize];

		while (i < data.length) {
			System.out.println("extracting data from Odd Pos");

			int j = 0;

			if (i % 2 == 0) {

				outputArray[j] = data[i];
				j++;
			}
			i++;
		}
		return outputArray;

	}

	public static boolean isSortedArrayDecreasing(double[] array) {

		return array[0] > array[1];
	}

	public static boolean isSortedArrayIncreasing(double[] array) {

		return array[0] < array[1];
	}

}
