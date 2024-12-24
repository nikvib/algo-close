package com.trade.algo.connection;

import static com.trade.algo.constants.AlgoConstants.LONG;
import static com.trade.algo.constants.AlgoConstants.POSITIONCLOSED;
import static com.trade.algo.constants.AlgoConstants.SHORT;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trade.algo.constants.AlgoConstants;
import com.trade.algo.order.AlgoPlaceOrder;
import com.trade.algo.strategy.ExitScheduler;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

@RestController
public class AlgoTradeAlert {
	
	
	@Autowired
	private AlgoPlaceOrder pc;
	
	@Autowired
	private ExitScheduler scheduler;
	
	@GetMapping("/pause")
	public void pauseApp() {
		AlgoConstants.ON=false;
	}
	
	@GetMapping("/resume")
	public void resumeApp() {
		AlgoConstants.ON=true;
	}
	
	@GetMapping("/lt")
	public void longTrade() throws JSONException, IOException, KiteException {
		pc.entryOrder(LONG);
		checkPosition();
	}
	
	@GetMapping("/st")
	public void shortTrade() throws JSONException, IOException, KiteException {
		pc.entryOrder(SHORT);
		checkPosition();
	}
	
	@GetMapping("/ct")
	public void closeTrade() throws JSONException, IOException, KiteException {
		pc.exitOrder();
		checkPosition();
	}
	
	@GetMapping("/cp")
	public void calculatePosition() throws JSONException, IOException, KiteException, ParseException {
		checkPosition();
	}

	private void checkPosition() throws KiteException, IOException {
		if(scheduler.isPositionClosed()) {
			POSITIONCLOSED=true;
			scheduler.completeTask();
		}else {
			POSITIONCLOSED=false;
		}
	}

}
