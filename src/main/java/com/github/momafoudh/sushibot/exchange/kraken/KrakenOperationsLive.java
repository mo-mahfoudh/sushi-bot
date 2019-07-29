package com.github.momafoudh.sushibot.exchange.kraken;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.momafoudh.sushibot.common.Log;
import com.github.momafoudh.sushibot.exchange.PriceEndpoint;
import com.github.momafoudh.sushibot.exchange.kraken.remote.KrakenApi;

public class KrakenOperationsLive implements PriceEndpoint {

	// this is a fake api key. Will move all static params to props file
	protected static String key = "Iu2Z8xzUU2NlBQsMUTUU4Kv1XereOqlzSpTTzTfFV+TU6/f4slr2KSO8"; // API key
	protected static String secret = "Vb47rTL1NS0plypV7PD+xfwKnaSim7O6xE3qTiPSdK7Yd6MY1YXIrcVsprtvw53B3/Tq8gpR0EDWcDec01JcPA=="; // API
	// secret
	private static Log transactions = new Log("transaction");
	private static Log errors = new Log("error");
	private static Log orders = new Log("orders"); // secret

	public volatile Date lastAskPriceDate;
	public volatile Date lastBidPriceDate;

	@Override
	public double getAssetAskPrice(String assetPair) {
		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String response;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("pair", assetPair);
		try {
			response = api.queryPrivate(KrakenApi.Method.TICKER, input);
			lastAskPriceDate = new Date();
			// System.out.println(response.toString());
		} catch (Exception e) {
			String message = "Connection error for getAssetAskprice : API call counter= " + APICounter.API_CALLS + "  "
					+ new Date().toLocaleString();

			errors.fatal("Error while getting Ask price for" + assetPair + " \n" + message, e);
			return 0;
		}

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(response);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			errors.fatal(e.toString());
		}
		String res = json.get("result").toString();
		JSONObject resJson = null;
		try {
			resJson = (JSONObject) parser.parse(res);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal(e.toString());
		}
		String assetPair2 = "";
		if (!(assetPair.equals("EOSEUR") || assetPair.equals("DASHEUR") || assetPair.equals("BCHEUR")))
			assetPair2 = "X" + assetPair.replace("EUR", "ZEUR");
		else
			assetPair2 = assetPair;
		String open = resJson.get(assetPair2).toString();
		JSONObject openJson = null;
		try {
			openJson = (JSONObject) parser.parse(open);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal(e.toString());
		}
		String ask = openJson.get("a").toString();
		ask = ask.replace("[", "");
		ask = ask.replace("]", "");
		ask = ask.replace("\"", "");
		String[] array = ask.split(",");

		return Double.parseDouble(array[0]);

	}

	public double getAssetBidPrice(String assetPair) {

		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String response;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("pair", assetPair);

		try {
			response = api.queryPrivate(KrakenApi.Method.TICKER, input);
			lastBidPriceDate = new Date();

		} catch (Exception e) {
			String message = "Connection error for getAssetBidprice  : API call counter= " + APICounter.API_CALLS + "  "
					+ new Date().toLocaleString();

			errors.fatal("Error while getting Ask price for" + assetPair + " \n" + message, e);

			return 0;
		}

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal(e.toString());
		}
		String res = json.get("result").toString();
		JSONObject resJson = null;
		try {
			resJson = (JSONObject) parser.parse(res);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal(e.toString());
		}
		String assetPair2 = "";
		if (!(assetPair.equals("EOSEUR") || assetPair.equals("DASHEUR") || assetPair.equals("BCHEUR")))
			assetPair2 = "X" + assetPair.replace("EUR", "ZEUR");
		else
			assetPair2 = assetPair;
		String open = resJson.get(assetPair2).toString();
		JSONObject openJson = null;
		try {
			openJson = (JSONObject) parser.parse(open);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal(e.toString());
		}
		String ask = openJson.get("b").toString();
		ask = ask.replace("[", "");
		ask = ask.replace("]", "");
		ask = ask.replace("\"", "");
		String[] array = ask.split(",");

		return Double.parseDouble(array[0]);

	}

	/**
	 * @param assetPair
	 * @param timeInMinutes
	 * @param OHLC
	 * @param size
	 * @return
	 */

	public static double[] getOHLC(String assetPair, int timeInMinutes, String OHLC, int size) {

		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String response;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("pair", assetPair);
		input.put("interval", timeInMinutes + "");
		try {
			response = api.queryPrivate(KrakenApi.Method.OHLC, input);

			// System.out.println(response.toString());
		} catch (Exception e) {
			String message = "Connection error for getOHLC: API call   " + new Date().toLocaleString();

			errors.fatal("Error while getting OHLC price for" + assetPair + " \n" + message, e);
			return null;
		}

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal("error while trying to parse data" + e.toString());
		}
		if (json.get("result") == null) {
			errors.fatal(response);
			return null;
		}
		String res = json.get("result").toString();
		JSONObject resJson = null;
		try {
			resJson = (JSONObject) parser.parse(res);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal("error while trying to parse data" + e.toString());
		}

		String assetPair2 = "";
		if (!(assetPair.equals("EOSEUR") || assetPair.equals("DASHEUR") || assetPair.equals("BCHEUR")))
			assetPair2 = "X" + assetPair.replace("EUR", "ZEUR");
		else
			assetPair2 = assetPair;

		String open = resJson.get(assetPair2).toString();
		org.json.simple.JSONArray openJson = null;
		org.json.simple.JSONArray openJsonArray = null;
		double[] OHLCArray = null;

		try {
			openJson = (org.json.simple.JSONArray) parser.parse(open);

			int i = 2;
			if (OHLC.toLowerCase().equals("c"))
				i = 4;
			OHLCArray = new double[size];

			int dataSize = openJson.size();
			for (int k = 0; k < size; k++) {
				openJsonArray = (org.json.simple.JSONArray) openJson.get(dataSize - k - 1);
				// System.out.println(openJsonArray);
				double j = Double.parseDouble(openJsonArray.get(i).toString());
				OHLCArray[k] = j;// here get(4) means Open price
				// System.out.println(new Date((long) openJsonArray.get(0)*1000)+"-"+ j);
			}
			// System.out.println(new Date((long)openJson.get(0)*1000));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal("error while prasing data" + e.getMessage());
		}

		return OHLCArray;

	}

	public static Map<String, ArrayList<Double>> getAllOHLC(String assetPair, int timeInMinutes, int size) {

		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String response;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("pair", assetPair);
		input.put("interval", timeInMinutes + "");
		try {
			response = api.queryPrivate(KrakenApi.Method.OHLC, input);

			// System.out.println(response.toString());
		} catch (Exception e) {
			String message = "Connection error for getAllOHLC: API call   " + new Date().toLocaleString();

			errors.fatal("Error while getting OHLC price for" + assetPair + " \n" + message, e);
			return null;
		}

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal("error while trying to parse data" + e.toString());
		}
		if (json.get("result") == null) {
			errors.fatal(response);
			return null;
		}
		String res = json.get("result").toString();
		JSONObject resJson = null;
		try {
			resJson = (JSONObject) parser.parse(res);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal("error while trying to parse data" + e.toString());
		}

		String assetPair2 = "";
		if (!(assetPair.equals("EOSEUR") || assetPair.equals("DASHEUR") || assetPair.equals("BCHEUR")))
			assetPair2 = "X" + assetPair.replace("EUR", "ZEUR");
		else
			assetPair2 = assetPair;

		String open = resJson.get(assetPair2).toString();
		org.json.simple.JSONArray openJson = null;
		org.json.simple.JSONArray openJsonArray = null;

		try {
			openJson = (org.json.simple.JSONArray) parser.parse(open);

			Map<String, ArrayList<Double>> data = new HashMap<String, ArrayList<Double>>();
			ArrayList<Double> openPrices = new ArrayList<Double>();
			ArrayList<Double> highPrices = new ArrayList<Double>();
			ArrayList<Double> lowPrices = new ArrayList<Double>();
			ArrayList<Double> closedPrices = new ArrayList<Double>();

			int dataSize = openJson.size();
			for (int k = 0; k < size; k++) {
				openJsonArray = (org.json.simple.JSONArray) openJson.get(dataSize - k - 1);
				// System.out.println(openJsonArray);
				double o = Double.parseDouble(openJsonArray.get(1).toString());
				double h = Double.parseDouble(openJsonArray.get(2).toString());
				double l = Double.parseDouble(openJsonArray.get(3).toString());
				double c = Double.parseDouble(openJsonArray.get(4).toString());
				openPrices.add(o);
				highPrices.add(h);
				lowPrices.add(l);
				closedPrices.add(c);

				// System.out.println(new Date((long) openJsonArray.get(0)*1000)+"-"+ c);
			}

			data.put("o", openPrices);
			data.put("h", highPrices);
			data.put("l", lowPrices);
			data.put("c", closedPrices);
			return data;

			// System.out.println(new Date((long)openJson.get(0)*1000));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errors.fatal("error while prasing data" + e.getMessage());
		}

		return null;

	}

	/**
	 * @param assetPair
	 * @param price
	 * @param qty
	 * @param ordertype
	 * @return if sucess returns the transaction ID. Returns error if error or null
	 *         if connection failed;
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public Map buyOrder(String assetPair, double price, double qty, String ordertype, boolean realTrade)
			throws ParseException {

		String message = ";" + "sending buy order request for : asset pair " + assetPair + "  asset price " + price
				+ "  qty " + qty + " order type " + ordertype;

		System.out.println(message);
		final double originPrice = price;

		double currentAskprice = getAssetAskPrice(assetPair);
		if (ordertype.equals("market"))
			price = currentAskprice;

		if (currentAskprice < price && ordertype.equals("limit")) {

			String message1 = "  price is too high : will use current price " + currentAskprice
					+ " instead of original price " + price + "   " + new Date().toLocaleString();

			System.out.println(message1);

			price = currentAskprice;
			transactions.fatal(message1);
		}
		if (!realTrade) {
			transactions.info(";" + "by order  received for " + assetPair + " Current ask " + currentAskprice
					+ " my buy price " + price + " for qty " + qty + " order type " + ordertype);

			Map data = new HashMap();
			data.put("txid", "SimulatedBuyID-" + new Date().toLocaleString());
			data.put("price", price + "");
			data.put("Originprice", originPrice + "");
			data.put("assetPair", assetPair);
			data.put("qty", qty);
			return data;
		}
		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String response = null;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("pair", assetPair);
		if (ordertype.equals("limit"))
			input.put("price", price + "");
		input.put("ordertype", ordertype);
		input.put("type", "buy");
		input.put("volume", qty + "");
		try {
			transactions.info(";" + "buy order  received for " + assetPair + " Current ask " + currentAskprice
					+ " my buy price " + price + " for qty " + qty + " order type " + ordertype);

			response = api.queryPrivate(KrakenApi.Method.ADD_ORDER, input);
			orders.info(response.toString());
		} catch (Exception e) {
			String message1 = "connexion error for buy Order  : API call counter= " + APICounter.API_CALLS + "  "
					+ new Date().toLocaleString();

			System.out.println(message1);
			errors.fatal(message1, e);
			Map data = new HashMap();
			data.put("txid", "error " + message);
			data.put("price", 0 + "");
			data.put("assetPair", assetPair);
			data.put("qty", qty);
			data.put("Originprice", originPrice + "");
			data.put("message", response);
			data.put("message", response);
			return data;

		}

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response);

		String error = json.get("error").toString();

		error = error.replace("]", "");
		error = error.replace("[", "");

		if (error.length() > 0) {
			Map data = new HashMap();
			data.put("txid", "error " + error);
			data.put("price", 0 + "");
			data.put("assetPair", assetPair);
			data.put("qty", qty);
			data.put("Originprice", originPrice + "");
			data.put("message", response);
			return data;
		}

		String txid = ((JSONObject) parser.parse(json.get("result").toString())).get("txid").toString();
		txid = txid.replace("]", "");
		txid = txid.replace("[", "");
		txid = txid.replace("\"", "");

		Map data = new HashMap();
		data.put("txid", txid);
		data.put("price", price + "");
		data.put("Originprice", originPrice + "");
		data.put("assetPair", assetPair);
		data.put("qty", qty);
		data.put("message", response);
		orders.info(data.toString());
		return data;

	}

	public Map sellOrder(String assetPair, double price, double qty, String ordertype, boolean realTrade)
			throws ParseException {
		final double originPrice = price;
		String message = ";" + "sending sell order request for : asset pair " + assetPair + "  asset price" + price
				+ "  qty " + qty + " " + ordertype;

		System.out.println(message);

		double currentBidprice = getAssetBidPrice(assetPair);
		if (ordertype.equals("market"))
			price = currentBidprice;

		if (currentBidprice > price && ordertype.equals("limit")) {
			String message1 = ";" + "price is too low: will use current bid price " + currentBidprice
					+ " instead of original price " + price + "   " + new Date().toLocaleString();
			System.out.println(message1);
			transactions.fatal(message1);
			price = currentBidprice;

		}

		if (!realTrade) {
			transactions.info(";" + " sell order   received for " + assetPair + " Current bid " + currentBidprice
					+ " my sell price " + price + " for qty " + qty + " order type " + ordertype);

			Map data = new HashMap();
			data.put("txid", "SimulatedSellID-" + new Date().toLocaleString());
			data.put("price", price + "");
			data.put("Originprice", originPrice + "");
			data.put("assetPair", assetPair);
			data.put("qty", qty);
			return data;
		}

		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME
		String response = null;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("pair", assetPair);
		if (ordertype.equals("limit"))
			input.put("price", price + "");
		input.put("ordertype", ordertype);
		input.put("type", "sell");
		input.put("volume", qty + "");
		try {
			transactions.info(";" + " sell order   received for " + assetPair + " Current bid " + currentBidprice
					+ " my sell price " + price + " for qty " + qty + " order type " + ordertype);

			response = api.queryPrivate(KrakenApi.Method.ADD_ORDER, input);
			orders.info(response.toString());
		} catch (Exception e) {
			String message1 = "connexion error for sell Order  : API call counter= " + APICounter.API_CALLS + "  "
					+ new Date().toLocaleString();
			System.out.println(message1);
			errors.fatal(message1, e);
			Map data = new HashMap();
			data.put("txid", "error " + message);
			data.put("price", 0 + "");
			data.put("Originprice", originPrice + "");
			data.put("assetPair", assetPair);
			data.put("qty", qty);
			data.put("message", response);
			return data;

		}

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response);

		String error = json.get("error").toString();

		error = error.replace("]", "");
		error = error.replace("[", "");

		if (error.length() > 0) {
			Map data = new HashMap();
			data.put("txid", "error " + error);
			data.put("price", 0 + "");
			data.put("Originprice", originPrice + "");
			data.put("assetPair", assetPair);
			data.put("qty", qty);
			data.put("message", response);
			return data;
		}

		String txid = ((JSONObject) parser.parse(json.get("result").toString())).get("txid").toString();
		txid = txid.replace("]", "");
		txid = txid.replace("[", "");
		txid = txid.replace("\"", "");

		Map data = new HashMap();
		data.put("txid", txid);
		data.put("price", price + "");
		data.put("Originprice", originPrice + "");
		data.put("assetPair", assetPair);
		data.put("qty", qty);
		data.put("message", response);
		orders.info(data.toString());
		return data;

	}

	private int getOrderClosureStatus(String orderID)
			throws InvalidKeyException, NoSuchAlgorithmException, ParseException {
		System.out.println("APICALLS " + APICounter.API_CALLS);
		if (APICounter.API_CALLS > APICounter.APICunterThreashHold)
			APICounter.decreaseAPICALLSBy(APICounter.API_CALLS);
		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String closedOrders = null;
		String openOrders = null;
		Map<String, String> input = new HashMap<>();

		input.clear();
		input.put("id", "");

		try {
			closedOrders = api.queryPrivate(KrakenApi.Method.CLOSED_ORDERS, input);
			APICounter.increaseAPICALLSBy(1);

		} catch (IOException e1) {
			// TODO Auto-generated catch block

			String message = "connexion error getClosedorder   : API call counter=" + APICounter.API_CALLS + "  -  "
					+ new Date().toLocaleString();
			System.out.println(message + e1.toString());
			errors.fatal(message, e1);
			return 0;
		}
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(closedOrders);
		} catch (ParseException e) {
			errors.fatal("Could not get order satus due to parse error" + orderID, e);
			errors.fatal("parsed string " + closedOrders);
			// TODO Auto-generated catch block
			return 0;
		}
		JSONObject txid = null;
		try {
			JSONObject res = ((JSONObject) parser.parse(json.get("result").toString()));
			JSONObject closed = ((JSONObject) parser.parse(res.get("closed").toString()));
			Object orderID1 = closed.get(orderID);
			if (orderID1 == null)
				return 1;// no order found in the closed orders
			txid = ((JSONObject) parser.parse(closed.get(orderID).toString()));
		} catch (Exception e) {

			errors.fatal("Could not get order satus due to parse error" + orderID, e);
			errors.fatal("parsed string " + json.get("result"));

		}
		// JSONObject status= ((JSONObject) parser.parse(
		// descr.get("status").toString()));

		String status = txid.get("status").toString();

		if (status.equals("closed")) {
			orders.info(txid + "");
			return 2;
			/* order has been closed */
		}
		if (status.equals("canceled")) {
			orders.info(txid + "");
			return 3;
			/* order has been canceled */
		} else
			return 0;// otherwise
	}

	public boolean checkIfOrderIsClosed(String txid) {

		if (txid.toLowerCase().contains("simu")) {

			orders.info(txid);
			return true;
		}

		int status;
		int numberOfSecondsToCheck = 60 * 1;
		Date startTime = new Date();
		try {
			while ((status = getOrderClosureStatus(txid)) == 1) {
				long duration = new Date().getTime() - startTime.getTime();
				if (duration / 1000 > numberOfSecondsToCheck) {
					System.out.println("Order " + txid + " cant be found after " + duration / 1000 + " seconds");
					break;
				}
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | ParseException e) {
			// TODO Auto-generated catch block
			errors.fatal("Could not check order satus " + txid, e);
			e.printStackTrace();
			return false;
		}

		if (status == 2)
			return true;
		return false;

	}

	/*
	 * Thu Oct 12 02:04:19 CEST 2017 -785.3500 withdrawal ZEUR Thu Oct 05 03:12:42
	 * CEST 2017 -443.9100 withdrawal ZEUR Wed Aug 23 01:00:22 CEST 2017
	 * 0.0068760300 deposit XXBT Thu Aug 17 18:56:45 CEST 2017 500.0000 deposit ZEUR
	 * Tue Aug 22 23:46:04 CEST 2017 758.09999000 deposit XXLM Tue Sep 12 14:45:20
	 * CEST 2017 500.0000 deposit ZEUR Tue Aug 01 13:57:27 CEST 2017 200.0000
	 * deposit ZEUR
	 *
	 */

	private void getLedger(String orderID) throws InvalidKeyException, NoSuchAlgorithmException, ParseException {
		KrakenApi api = new KrakenApi();
		api.setKey(key); // FIXME
		api.setSecret(secret); // FIXME

		String closedOrders = null;
		String openOrders = null;
		Map<String, String> input = new HashMap<>();

		input.clear();
		// input.put("start", "4323283200");
		// input.put("end", "4354732800");
		input.put("closed position", "deposit");

		try {
			closedOrders = api.queryPrivate(KrakenApi.Method.TRADES_HISTORY, input);
			System.out.println(closedOrders);

		} catch (IOException e1) {
			// TODO Auto-generated catch block

			String message = "connexion error getClosedorder   : API call counter=" + APICounter.API_CALLS + "  -  "
					+ new Date().toLocaleString();
			System.out.println(message + e1.toString());
			errors.fatal(message, e1);

		}
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(closedOrders);
		} catch (ParseException e) {
			errors.fatal("Could not get order satus due to parse error" + orderID, e);
			errors.fatal("parsed string " + closedOrders);
			// TODO Auto-generated catch block
		}
		JSONObject txid = null;
		try {
			JSONObject res = ((JSONObject) parser.parse(json.get("result").toString()));
			JSONObject ledgerEntries = ((JSONObject) parser.parse(res.get("trades").toString()));
			;
			// System.out.println(ledgerEntries);
			// org.json.simple.JSONArray re=(org.json.simple.JSONArray) parser.parse
			// (ledgerEntries.toJSONString());
			Set keys = ledgerEntries.keySet();
			List<String> list = new ArrayList<String>(keys);
			for (int k = 0; k < list.size(); k++) {
				JSONObject res1 = (JSONObject) parser.parse(ledgerEntries.get(list.get(k)) + "");
				String type = (String) res1.get("type");
				String asset = (String) res1.get("pair");
				String amount = (String) res1.get("cost");

				String d = res1.get("time").toString().substring(0, 11).replace(".", "");
				// System.out.println(d+"|"+res1.get("time").toString());
				Date date = new Date((long) Long.parseLong(d) * 1000);
				// if(type.contains("trade"));
				System.out.println(date + " " + amount + " " + type + " " + asset);
			}
		} catch (Exception e) {

			errors.fatal("Could not get order satus due to parse error" + orderID, e);
			errors.fatal("parsed string " + json.get("result"));

			System.out.println("error");
		}
		// JSONObject status= ((JSONObject) parser.parse(
		// descr.get("status").toString()));

	}

	public Date getLastAskPriceDate() {
		return lastAskPriceDate;
	}

	public void setLastAskPriceDate(Date lastAskPriceDate) {
		this.lastAskPriceDate = lastAskPriceDate;
	}

	public Date getLastBidPriceDate() {
		return lastBidPriceDate;
	}

	public void setLastBidPriceDate(Date lastBidPriceDate) {
		this.lastBidPriceDate = lastBidPriceDate;
	}

	public static void main(String args[])
			throws InvalidKeyException, NoSuchAlgorithmException, InterruptedException, ParseException {
		System.out.println(new KrakenOperationsLive().getAssetAskPrice("XRPEUR"));

	}

}
