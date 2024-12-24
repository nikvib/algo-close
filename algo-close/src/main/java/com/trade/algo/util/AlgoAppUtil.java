package com.trade.algo.util;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgoAppUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(AlgoAppUtil.class);
	
	public static void main(String[] args) {
		for(int i=0; i<=100;i++) {
			logger.info(getRandomNumber().toString());
		}
	}
	
	
	public static Integer getRandomNumber() {
		Random r = new Random();
		int low = -1;
		int high = 4;
		Integer result = (r.nextInt(high - low) + low);
		return result;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}


	
	public static String usingRandomUUID() {
		UUID randomUUID = UUID.randomUUID();
		return randomUUID.toString().replaceAll("-", "");
	}
	
	

}
