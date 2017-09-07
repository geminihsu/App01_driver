
package tw.com.geminihsu.app01Client.common;


public class Constants {
	/** <code>APP_NAME</code> APP_NAME*/
	final public static String APP_NAME = "App01";

	/** <code>ARG_POSITION</code> ARG_POSITION*/
	final public static String ARG_POSITION = "position";

	/** <code>SDACRD_DIR_APP</code> */
	final public static String SDACRD_DIR_APP_ROOT = "/App01Client/";

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
	public static final int DESTINATION_QUERY_BOOKMARK = 5;
	public static final int DEPARTURE_QUERY_BOOKMARK = 6;
	public static final int DISPLAY_USER_LOCATION = 7;
	public static final int STOP_QUERY_GPS = 8;
	public static final int STOP_QUERY_BOOKMARK = 9;
	public static final String BUNDLE_LOCATION = "map";
	public static final String BUNDLE_MAP_LATITUDE = "latitude";
	public static final String BUNDLE_MAP_LONGITUDE = "longitude";
	public static final String BUNDLE_MAP_CURRENT_ADDRESS = "currAddress";


	public final static String ACCOUNT_PHONE_NUMBER = "phoneNumber";// from
	public final static String ACCOUNT_USERNAME = "user_name";// from
	public final static String ACCOUNT_PASSWORD = "password";// from

	public final static String ACCOUNT_NAME = "name";// from

	public final static String ACCOUNT_DRIVER_UID = "uid";// from

	public final static String ACCOUNT_USER_ID = "user_id";// from
	public final static String ACCOUNT_USER_UID = "user_uid";// from


	public final static String ORDER_TICKET_ID = "ticket_id";// from
	public final static String ORDER_TICKET_STATUS = "ticket_status";// from

	public final static String SERVER_CONTENT_CODE = "code";// from

	public final static String SERVER_SPECIAL = "dtype";// from

	public final static String NEW_ORDER_DTYPE = "dtype";// from
	public final static String NEW_ORDER_CARGO_TYPE = "cargo_type";// from

	public final static String NEWS_CONTENT = "news_content";// from
	public final static String NEWS_TITLE = "news_content_title";// from

	public final static String INTERFACE = "interface";// from


	public final static String SPEC_ID = "id";// from


	public enum APP_REGISTER_DRIVER_TYPE
	{
		K_REGISTER_DRIVER_TYPE_NO_WORK (0)  ,
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
		//if (index >= 0) {
			if (index == 0) {
				return APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_NO_WORK;
			}
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
		//}
		return null;
	}


	public enum APP_REGISTER_ORDER_TYPE
	{
		K_REGISTER_ORDER_TYPE_TAKE_RIDE (1)  ,
		K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE (2)  ,
		K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN (3),
		K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT (4);

		private int value;

		private APP_REGISTER_ORDER_TYPE(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	};

	public static Constants.APP_REGISTER_ORDER_TYPE conversion_create_new_order_cargo_type_result(int index) {
		if (index >= 0) {
			if (index == 1) {
				return APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE;
			}
			if (index == 2) {
				return APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE;
			}
			if (index == 3) {
				return APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN;
			}
			if (index == 4) {
				return APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT;
			}

		}
		return null;
	}

}



