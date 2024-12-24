package com.trade.algo.strategy;


import static com.trade.algo.constants.AlgoConstants.EVERYSEC;
import static com.trade.algo.constants.AlgoConstants.ON;
import static com.trade.algo.constants.AlgoConstants.POSITIONCLOSED;
import static com.trade.algo.constants.AlgoConstants.TRADINGSYMBOL;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.trade.algo.constants.AlgoConstants;
import com.trade.algo.order.AlgoPlaceOrder;
import com.trade.algo.util.AlgoDateUtils;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Tick;


@Component
public class ExitScheduler  {
	
	@Autowired
	private AlgoTickRunner tickRunner;
	
	
	private Order order;
	private Position position;
	private Double currentPrice = null;
	private Double exitpoint;
	
	
	@Autowired
	AlgoPlaceOrder pc;
	
	@Scheduled(cron = EVERYSEC)
	public void runFastCandleCalculation() throws IOException, ParseException, InterruptedException, KiteException {
		if(ON) {
			if(POSITIONCLOSED==false) {
				closePosition();
			}
		}
		
	}

    private void closePosition() throws JSONException, IOException, KiteException, ParseException {
		boolean exit=AlgoDateUtils.isExitSessionActive();
		if(exit && position!=null && order!=null) {
			setCurrentTickInfo();
			if(currentPrice==null) {
				return;
			}
			Date entryDate = AlgoDateUtils.getDateTillHour(order.orderTimestamp);
			Date currentDate = AlgoDateUtils.getDateTillHour(AlgoDateUtils.getDateAsString()) ;
			Double orderprice = Double.parseDouble(order.averagePrice);
			int netQuantity = position.netQuantity;
			if(netQuantity>0) {
				if(entryDate.equals(currentDate)) {
					Double slprice  = (orderprice-AlgoConstants.SLBIG)+AlgoConstants.TRIGGER;
					if(currentPrice<=slprice) {
						pc.exitOrder();
						completeTask();
					}
				}
				else if(currentDate.after(entryDate)) {
					boolean sltaken=false;
					Double slprice  = (orderprice-AlgoConstants.SLSMALL)+AlgoConstants.TRIGGER;
					if(currentPrice<=slprice) {
						pc.exitOrder();
						completeTask();
						exitpoint=null;
						sltaken=true;
					}
					
				}
			}
			else if(netQuantity<0) {
				if(entryDate.equals(currentDate)) {
					Double slprice  = (orderprice+AlgoConstants.SLBIG)-AlgoConstants.TRIGGER;
					if(currentPrice>=slprice) {
						pc.exitOrder();
						completeTask();
						exitpoint=null;
					}
				}
				else if(currentDate.after(entryDate)) {
					boolean sltaken=false;
					Double slprice  = (orderprice+AlgoConstants.SLSMALL)-AlgoConstants.TRIGGER;
					if(currentPrice>=slprice) {
						pc.exitOrder();
						completeTask();
						exitpoint=null;
						sltaken=true;
					}
				}
			}
		}
    }
    
    
    

	public void completeTask() {
		POSITIONCLOSED=true;
		position=null;
		order=null;
	}

	@EventListener(ApplicationReadyEvent.class)
	private void doSomethingAfterStartup() throws IOException, KiteException {
		if(isPositionClosed()) {
			POSITIONCLOSED=true;
			completeTask();
		}else {
			POSITIONCLOSED=false;
		}
		
	}

	public boolean isPositionClosed() throws KiteException, IOException {
		order = pc.getLatestCompletedOrder(TRADINGSYMBOL);
		position = pc.getPosition(TRADINGSYMBOL);
		if (order == null) {
			System.out.println("No Comleted Order....");
			return true;
		}
		if (position == null) {
			System.out.println("No Position.....");
			return true;
		}
		int netQuantity = position.netQuantity;
		if (netQuantity == 0) {
			System.out.println("No Open Position.....");
			return true;
		}
		return false;
	}
    
    
    
    private void setCurrentTickInfo() {
		Tick tick = tickRunner.getCurrentTick(TRADINGSYMBOL);
		if (tick == null) {
			return;
		}
		currentPrice = tick.getLastTradedPrice();
	}

}
