
package tw.com.geminihsu.app01.common;


import tw.com.geminihsu.app01.bean.App01libObjectKey;

public class Constants {
	/** <code>APP_NAME</code> APP_NAME*/
	final public static String APP_NAME = "App01";

	/** <code>ARG_POSITION</code> ARG_POSITION*/
	final public static String ARG_POSITION = "position";

	/** <code>SDACRD_DIR_APP</code> */
	final public static String SDACRD_DIR_APP_ROOT = "/App01/";

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

	public static final int DEPARTURE_QUERY_GPS = 3;
	public static final int DESTINATION_QUERY_GPS = 4;
	public static final String BUNDLE_LOCATION = "map";
	public static final String BUNDLE_MAP_LATITUDE = "latitude";
	public static final String BUNDLE_MAP_LONGITUDE = "longitude";

	public final static String ACCOUNT_PHONE_NUMBER = "phoneNumber";// from
	public final static String ACCOUNT_USERNAME = "user_name";// from
	public final static String ACCOUNT_PASSWORD = "password";// from

	public final static String ACCOUNT_NAME = "name";// from

	public final static String ACCOUNT_DRIVER_UID = "uid";// from

	public final static String ORDER_TICKET_ID = "ticket_id";// from
	public final static String SERVER_CONTENT_CODE = "code";// from


	public enum APP_REGISTER_DRIVER_TYPE
	{
		K_REGISTER_DRIVER_TYPE_TAXI (1)  ,
		K_REGISTER_DRIVER_TYPE_UBER (2)  ,
		K_REGISTER_DRIVER_TYPE_TRUCK (3),
		K_REGISTER_DRIVER_TYPE_CARGO (4),
		K_REGISTER_DRIVER_TYPE_TRAILER (5);

		private int value;

		private APP_REGISTER_DRIVER_TYPE(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	};

	public static Constants.APP_REGISTER_DRIVER_TYPE conversion_register_driver_account_result(int index) {
		if (index >= 0) {
			if (index == 1) {
				return Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI;
			}
			if (index == 2) {
				return Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER;
			}
			if (index == 3) {
				return Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK;
			}
			if (index == 4) {
				return APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO;
		}
			if (index == 5) {
				return APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER;
			}
		}
		return null;
	}


}



