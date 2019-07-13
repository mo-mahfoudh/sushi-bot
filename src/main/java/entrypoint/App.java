package entrypoint;

import com.trading.patterns.Pattern;
import com.trading.patterns.PennantPattern;
import com.trading.remote.BuyPriceProvider;

/**
 * 
 * @author mohamed
 *
 */
public class App {
	public static void main(String[] args) {
		String currency = "someCurrency";
		BuyPriceProvider priceProvider = new BuyPriceProvider();
		Pattern pennant = new PennantPattern(priceProvider, 10000, currency, 0.001);
		pennant.patternFind();

	}
}
