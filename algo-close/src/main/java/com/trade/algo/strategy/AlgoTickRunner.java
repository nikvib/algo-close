package com.trade.algo.strategy;

import static com.trade.algo.constants.AlgoConstants.TOKEN;
import static com.trade.algo.constants.AlgoConstants.TRADINGSYMBOL;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.trade.algo.connection.AlgoKiteConnection;
import com.trade.algo.connection.AlgoKiteTickerConnection;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnOrderUpdate;
import com.zerodhatech.ticker.OnTicks;

import jakarta.annotation.PreDestroy;;

@Component
public class AlgoTickRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(AlgoTickRunner.class);

	private KiteTicker tickerProvider;
	private ArrayList<Long> tokens = new ArrayList<>();
	private Map<String, Double> price = new LinkedHashMap<>();
	private Map<String, Tick> tick = new LinkedHashMap<>();
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterSpingBootStart() throws IOException, KiteException, ParseException {
		mainAlert();
	}

	public void mainAlert() throws JSONException, IOException, KiteException, ParseException {
		KiteConnect kite = AlgoKiteConnection.getInstance();
		tickerProvider = AlgoKiteTickerConnection.getInstance(kite);
		tokens.add(TOKEN);
		ticker();
	}

	public Double getCurrentPrice(String name) {
		return price.get(name);
	}

	public Tick getCurrentTick(String name) {
		return tick.get(name);
	}

	private void ticker() throws KiteException, IOException {
		tickerProvider.setOnConnectedListener(new OnConnect() {
			@Override
			public void onConnected() {
				tickerProvider.subscribe(tokens);
				tickerProvider.setMode(tokens, KiteTicker.modeFull);
			}
		});

		tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
			@Override
			public void onDisconnected() {
				logger.info("On Disconnected----------------------------");
			}
		});

		tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
			@Override
			public void onOrderUpdate(Order order) {
			}
		});

		tickerProvider.setOnTickerArrivalListener(new OnTicks() {
			@Override
			public void onTicks(ArrayList<Tick> ticks) {
				if (ticks != null) {
					for (Tick t : ticks) {
						try {
							Double current = t.getLastTradedPrice();
							price.put(TRADINGSYMBOL, current);
							tick.put(TRADINGSYMBOL, t);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}
			}

		});
		boolean connectionOpen = tickerProvider.isConnectionOpen();
		if (!connectionOpen) {
			tickerProvider.setTryReconnection(true);
			tickerProvider.setMaximumRetries(10);
			tickerProvider.setMaximumRetryInterval(30);
			tickerProvider.setMode(tokens, KiteTicker.modeLTP);
			tickerProvider.connect();
		}
	}

	public void disconnect() {
		boolean connectionOpen = tickerProvider.isConnectionOpen();
		if (connectionOpen) {
			tickerProvider.unsubscribe(tokens);
			tickerProvider.disconnect();
		}
	}

	@PreDestroy
	public void onExit() {
		disconnect();
	}

}
