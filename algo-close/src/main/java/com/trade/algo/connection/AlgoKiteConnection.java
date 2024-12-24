package com.trade.algo.connection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zerodhatech.kiteconnect.KiteConnect;

public class AlgoKiteConnection {
	
	private static final Logger logger = LoggerFactory.getLogger(AlgoKiteConnection.class);

	private static KiteConnect kite = null;

	private AlgoKiteConnection() {
	}

	public static KiteConnect getInstance() {
		if (kite == null) {
			try {
				Path filePath = Path.of(System.getenv("KITE_TOK_PATH"));
				String TOK = Files.readString(filePath);
				kite = new KiteConnect(System.getenv("KITE_KEY"));
				kite.setUserId(System.getenv("KITE_ID"));
				kite.setAccessToken(TOK);
			} catch (IOException e) {
				logger.error("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return kite;
	}

}
