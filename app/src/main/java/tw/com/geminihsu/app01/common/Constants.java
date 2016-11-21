
package tw.com.geminihsu.app01.common;


public class Constants {
	/** <code>APP_NAME</code> APP_NAME*/
	final public static String APP_NAME = "App01";

	/** <code>ARG_POSITION</code> ARG_POSITION*/
	final public static String ARG_POSITION = "position";

	final public static String SERVER_URL= "http://app01.cumi.co/api";

	final public static int CONTROL_PANNEL_MANUAL = 3;
	final public static int CANCEL_ORDER_FEEDBACK = 4;


	final public static int QUERY_MERCHANDISE = 0;
	final public static int QUERY_PASSENGER = 1;

	//用來判斷目前登入是司機還是乘客的流程
	public static boolean Driver =false;

	public enum Identify
	{
		CLIENT, DRIVER, BOTH
	}


	final public static String hotel_latitude = "latitude";
	final public static String hotel_longitude = "longitude";

	public final static String ACCOUNT_PHONE_NUMBER = "phoneNumber";// from
	public final static String ACCOUNT_PASSWORD = "password";// from

}



