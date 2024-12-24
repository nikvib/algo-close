package com.trade.algo.constants;

import java.util.Date;

import com.trade.algo.util.AlgoDateUtils;

public class AlgoConstants {
	
	public static final String EVERYSEC = "*/1 * * * * *";
	public static final String TRADINGSYMBOL = "NATURALGAS24DECFUT"; 
	public static final Long TOKEN = 111636487L;
	public static final Date ALREADYEXPRIRED = AlgoDateUtils.getSpecificDate(2024, 11, 26);
	public static final int QN = 10;
	public static final Double SLBIG = 5.0;
	public static final Double SLSMALL = 0.0;
	public static final Double EXITPRICE = 5.0;
	public static final Double TRIGGER = 0.2;
	public static final Double TG = 1.5;
	public static final String EXCHANGE = "MCX";
	public static final Double MULTIPLIER = 5000.0;
	public static final Double BROKERAGE = 250.0;
	public static final Double BUFFER = 0.2;
	public static final String TYPE = "net";
	public static boolean POSITIONCLOSED = false;
	public static boolean ON = true;
	public static final String LONG="LONG";
	public static final String SHORT="SHORT";
	
	
}
