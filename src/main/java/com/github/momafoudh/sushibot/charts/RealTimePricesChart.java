package com.github.momafoudh.sushibot.charts;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import com.github.momafoudh.sushibot.manager.BuyPriceProvider;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;


/**
 * Creates a real-time chart using SwingWorker
 */
public class RealTimePricesChart {
	static BuyPriceProvider bp;
	MySwingWorker mySwingWorker;
	SwingWrapper<XYChart> sw;
	XYChart chart;
	public static double[] patternToShow;

	public RealTimePricesChart(BuyPriceProvider bp) {
		this.bp = bp;

	}

	public static void main(String[] args) throws Exception {

		RealTimePricesChart swingWorkerRealTime = new RealTimePricesChart(bp);
		swingWorkerRealTime.go();
	}

	public void go() {

		// Create Chart
		while (bp.getPrice() == 0)
			;
		// chart = QuickChart.getChart("SUSHI -" + bp.getAssetPair() + " Real-time
		// prices", "Time", "Value", "prices",
		// new double[] { bp.getPrice() }, new double[] { bp.getPrice() });
		chart = new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName())
				.xAxisTitle("elapsed seconds").yAxisTitle("price").build();

		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);

		chart.getStyler().setPlotMargin(0);
		chart.getStyler().setPlotContentSize(.95);
		double intiprice = bp.getPrice();
		chart.addSeries(bp.getAssetPair(), new double[] { intiprice }, new double[] { intiprice });

		// Show it
		sw = new SwingWrapper<XYChart>(chart);
		sw.displayChart();

		mySwingWorker = new MySwingWorker();
		mySwingWorker.execute();
	}

	private class MySwingWorker extends SwingWorker<Boolean, double[]> {

		LinkedList<Double> fifo = new LinkedList<Double>();
		private int time;

		public MySwingWorker() {

		}

		@Override
		protected Boolean doInBackground() throws Exception {

			while (!isCancelled()) {
				if (patternToShow != null) {

				}
				time++;
				fifo.add(bp.getPrice());
				if (fifo.size() > 500) {
					fifo.removeFirst();
				}

				double[] array = new double[fifo.size()];
				for (int i = 0; i < fifo.size(); i++) {
					array[i] = fifo.get(i);
				}
				publish(array);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// eat it. caught when interrupt is called
					System.out.println("MySwingWorker shut down.");
				}

			}

			return true;
		}

		@Override
		protected void process(List<double[]> chunks) {

			double[] mostRecentDataSet = chunks.get(chunks.size() - 1);

			chart.updateXYSeries(bp.getAssetPair(), null, mostRecentDataSet, null);

			/*
			 * if (patternToShow != null) { double[] timeData =
			 * chart.getSeriesMap().get("prices").getXData(); double[] pricesData =
			 * chart.getSeriesMap().get("prices").getYData(); int index =
			 * Utils.findArrayIndex(patternToShow, pricesData); System.out.println(index);
			 * System.out.println(Arrays.toString(pricesData));
			 * System.out.println(Arrays.toString(patternToShow));
			 * System.out.println(Arrays.toString(timeData));
			 * 
			 * double[] xdata = new double[patternToShow.length]; for (int j = 0; j <
			 * patternToShow.length; j++) { xdata[j] = timeData[index + j];
			 * 
			 * } chart.updateXYSeries("pattern", xdata, patternToShow, null);
			 * 
			 * }
			 */

			sw.repaintChart();

			long start = System.currentTimeMillis();
			long duration = System.currentTimeMillis() - start;
			try {
				Thread.sleep(40 - duration); // 40 ms ==> 25fps
				// Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
			} catch (InterruptedException e) {
			}

		}
	}
}
