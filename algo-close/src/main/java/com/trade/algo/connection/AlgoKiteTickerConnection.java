package com.trade.algo.connection;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.ticker.KiteTicker;

public class AlgoKiteTickerConnection {

	private static KiteTicker kiteTicker = null;

	private AlgoKiteTickerConnection() {
	}

	public static KiteTicker getInstance(KiteConnect kite) {
		if (kiteTicker == null) {
			kiteTicker = new KiteTicker(kite.getAccessToken(), kite.getApiKey());
		}
		return kiteTicker;
	}
}
