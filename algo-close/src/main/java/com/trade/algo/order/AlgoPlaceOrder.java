package com.trade.algo.order;

import static com.trade.algo.constants.AlgoConstants.EXCHANGE;
import static com.trade.algo.constants.AlgoConstants.LONG;
import static com.trade.algo.constants.AlgoConstants.QN;
import static com.trade.algo.constants.AlgoConstants.SHORT;
import static com.trade.algo.constants.AlgoConstants.TRADINGSYMBOL;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trade.algo.connection.AlgoKiteConnection;
import com.trade.algo.constants.AlgoConstants;
import com.trade.algo.util.AlgoDateUtils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;

@Component
public class AlgoPlaceOrder {
	
	
	private static final Logger logger = LoggerFactory.getLogger(AlgoPlaceOrder.class);
	
	
	public void exitOrder()throws JSONException, IOException, KiteException {
		try {
			KiteConnect kiteSdk = AlgoKiteConnection.getInstance();
			OrderParams orderParams = new OrderParams();
			orderParams.tradingsymbol = TRADINGSYMBOL;
			orderParams.exchange = EXCHANGE;
			orderParams.validity = Constants.VALIDITY_DAY;
			orderParams.product = Constants.PRODUCT_NRML;
			orderParams.orderType = Constants.ORDER_TYPE_MARKET;
			Integer netQuantity = getPositionsQuantity(TRADINGSYMBOL);
			if (netQuantity != null && netQuantity != 0) {
				String direction = netQuantity < 0 ? Constants.TRANSACTION_TYPE_BUY : Constants.TRANSACTION_TYPE_SELL;
				orderParams.quantity = Math.abs(netQuantity);
				orderParams.transactionType = direction;
				kiteSdk.placeOrder(orderParams, Constants.VARIETY_REGULAR);
				Thread.sleep(1000);
				Order order = getLatestCompletedOrder(TRADINGSYMBOL);
				Double price=Double.parseDouble(order.averagePrice);
				logger.info("Market Exit Placed Price " + price + " at " +AlgoDateUtils.getDateAsString());
			}
		} catch (KiteException e) {
			logger.error("Kite Realated Error Occured while taking trade " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Kite Realated Error Occured while taking trade " + e.getMessage());
		}
	}
	
	
	public void entryOrder(String position)throws JSONException, IOException, KiteException {
		try {
				KiteConnect kiteSdk = AlgoKiteConnection.getInstance();
				OrderParams orderParams = new OrderParams();
				orderParams.tradingsymbol = TRADINGSYMBOL;
				orderParams.exchange = EXCHANGE;
				orderParams.validity = Constants.VALIDITY_DAY;
				orderParams.product = Constants.PRODUCT_NRML;
				orderParams.orderType = Constants.ORDER_TYPE_MARKET;
				Integer netQuantity = getPositionsQuantity(TRADINGSYMBOL);
				orderParams.quantity = QN;
				if (netQuantity != null && netQuantity != 0) {
					if (netQuantity > 0 && position.toUpperCase().equals(LONG)) {
						return;
					}
					if (netQuantity < 0 && position.toUpperCase().equals(SHORT)) {
						return;
					}
				}
				orderParams.transactionType = position.toUpperCase().equals(LONG) ? Constants.TRANSACTION_TYPE_BUY : Constants.TRANSACTION_TYPE_SELL;
				kiteSdk.placeOrder(orderParams, Constants.VARIETY_REGULAR);
				Thread.sleep(1000);
				Order order = getLatestCompletedOrder(TRADINGSYMBOL);
				Double price=Double.parseDouble(order.averagePrice);
				logger.info("Market Entry Placed Price " + price +" At " +AlgoDateUtils.getDateAsString());
		}
		 catch (KiteException e) {
			logger.error("Kite Realated Error Occured while taking trade " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Kite Realated Error Occured while taking trade " + e.getMessage());
		}
	}

	
	
	private Integer getPositionsQuantity(String tradingsymbol) throws JSONException, IOException, KiteException {
		KiteConnect kiteSdk = AlgoKiteConnection.getInstance();
		Map<String, List<Position>> positionsMap = kiteSdk.getPositions();
		List<Position> positions = positionsMap.get(AlgoConstants.TYPE);
		if (positions != null && !positions.isEmpty()) {
			for (Position p : positions) {
				if(p.tradingSymbol.equals(tradingsymbol)) {
					int qn = p.netQuantity;
					return qn;
				}
			}
		}
		return null;
	}
	
	public Order getLatestCompletedOrder(String name) throws KiteException, IOException {
			KiteConnect kiteSdk = AlgoKiteConnection.getInstance();
			List<Order> orders = kiteSdk.getOrders();
			 for (int i = orders.size() - 1; i >= 0; i--) {
				String status = orders.get(i).status;
				String symbol = orders.get(i).tradingSymbol;
				if ("COMPLETE".equals(status) && name.equals(symbol)) {
					return orders.get(i);
				}
			}
		return null;
	}
	
	
	
	public Position getPosition(String name) throws JSONException, IOException, KiteException {
			KiteConnect kiteSdk = AlgoKiteConnection.getInstance();
			Map<String, List<Position>> positionsMap = kiteSdk.getPositions();
			List<Position> positions = positionsMap.get(AlgoConstants.TYPE);
			if (positions != null && !positions.isEmpty()) {
				for (Position p : positions) {
					if(p.tradingSymbol.equals(name)){
						return p;
					}
				}
			}
		return null;
	}
	
	public String getRandomNumberString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    return String.format("%06d", number);
	}
	
	public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	


	

}
