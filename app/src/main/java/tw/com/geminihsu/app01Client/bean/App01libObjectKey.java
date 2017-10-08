package tw.com.geminihsu.app01Client.bean;

/**
 * Created by geminihsu on 2016/11/20.
 */

public class App01libObjectKey {

    public static final String APP_OBJECT_KEY_PUTS_METHOD = "method";

    //send json to server method attribute
    public static final String APP_OBJECT_KEY_PUTS_METHOD_REGISTER = "m_reg_form";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_VERIFY = "m_reg_verify";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_RE_SEND_VERIFY = "m_reg_resend";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_RE_SEND_PASSWORD = "m_forget_sendsms";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_MODIFY_PASSWORD = "m_modify_passowrd";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_RE_MODIFY_PASSWORD = "m_forget_modify";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_LOGIN_VERIFY = "m_auth_user";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_GET_PUSH_NOTIFICATION = "p_get";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_GET_USER_INFORMATION = "m_fetch_user";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_GET_DRIVER_INFORMATION = "m_get_driver";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_DELETE_PUSH_NOTIFICATION = "p_delete";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_IMAGE = "m_upload_img";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_REGISTER_DRIVER = "m_apply_driver";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_CREATE_QUICK_TAXI_ORDER = "t_create_taxi";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_CREATE_NORMAL_ORDER = "t_create_normal";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_GPS_LOCATION = "m_upload_location";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_QUERY_CLIENT_ORDER_LIST = "t_mylist";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_QUERY_DRIVER_ORDER_LIST = "t_mylist_driver";
    public static final String APP_OBJECT_KEY_PUTS_METHOD_QUERY_ORDER_DETAIL = "t_detail";
    public static final String APP_OBJECT_KEY_PUTS_PUSH_NOTIFICATION_TO_ORDER_OWNER = "t_push_message";
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_WORK_IDENTITY = "m_modify_did";
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_TAKE_OVER_ORDER = "t_order";
    public static final String APP_OBJECT_KEY_PUTS_CUSTOMER_CANCEL_ORDER = "t_cancel";
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_FINISH_ORDER = "t_finish";
    public static final String APP_OBJECT_KEY_PUTS_COMMENT_ORDER = "t_assess";
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_RECOMMEND_ORDER = "t_match";
    public static final String APP_OBJECT_KEY_PUTS_ACCOUNT_COMMENT = "m_get_assess";
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_LOCATION = "m_get_location";


    public static final String APP_OBJECT_KEY_PUTS_CONTENT_DETAIL = "c_get_all";
    public static final String APP_OBJECT_KEY_PUTS_NEW_LIST = "n_fetch_list";
    public static final String APP_OBJECT_KEY_PUTS_NEW_LIST_CONTENT = "n_fetch_content";

    public static final String APP_OBJECT_KEY_PUTS_CUSTOMER_SUGGESTION = "f_push_suggest";



    //send register json to server attribute
    public static final String APP_OBJECT_KEY_REGISTER_USERNAME = "username";
    public static final String APP_OBJECT_KEY_REGISTER_PASSWORD = "password";
    public static final String APP_OBJECT_KEY_REGISTER_IDCARD = "idcard";
    public static final String APP_OBJECT_KEY_REGISTER_REALNAME = "realname";
    public static final String APP_OBJECT_KEY_REGISTER_REGISTER_ID = "registration_id";
    public static final String APP_OBJECT_KEY_REGISTER_PLATFORM = "devicePlatform";
    public static final String APP_OBJECT_KEY_REGISTER_APP_VERSION = "appVer";

    //send verify json to server attribute
    public static final String APP_OBJECT_KEY_VERIFY_CODE = "verifycode";
    public static final String APP_OBJECT_KEY_VERIFY_USERNAME = "username";

    //send re-verify json to server attribute
    public static final String APP_OBJECT_KEY_RE_SEND_VERIFY_USERNAME = "username";

    //send re-send password json to server attribute
    public static final String APP_OBJECT_KEY_RE_SEND_PASSWORD_USERNAME = "username";
    public static final String APP_OBJECT_KEY_RE_SEND_PASSWORD_IDCARD = "idcard";

    //send re-forgot-modify password json to server attribute
    public static final String APP_OBJECT_KEY_RE_SEND_VERIFY_CODE = "verifycode";
    public static final String APP_OBJECT_KEY_RE_SEND_NEW_PASSWORD = "new_password";

    //send modify password json to server attribute
    public static final String APP_OBJECT_KEY_MODIFY_PASSWORD_USERNAME = "username";
    public static final String APP_OBJECT_KEY_MODIFY_PASSWORD_OLD_PASSWORD = "old_password";
    public static final String APP_OBJECT_KEY_MODIFY_PASSWORD_NEW_PASSWORD = "new_password";
    public static final String APP_OBJECT_KEY_MODIFY_PASSWORD_ACCESSKEY = "accesskey";


    //send login json to server attribute
    public static final String APP_OBJECT_KEY_LOGIN_USERNAME = "username";
    public static final String APP_OBJECT_KEY_LOGIN_PASSWORD = "password";
    public static final String APP_OBJECT_KEY_LOGIN_REGISTER_ID = "registration_id";
    public static final String APP_OBJECT_KEY_LOGIN_PLATFORM = "devicePlatform";
    public static final String APP_OBJECT_KEY_LOGIN_APP_VERSION = "appVer";

    //send post image json to server attribute
    public static final String APP_OBJECT_KEY_IMAGE_USERNAME = "username";
    public static final String APP_OBJECT_KEY_IMAGE_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_IMAGE_UPLOAD_TYPE = "uploadtype";
    public static final String APP_OBJECT_KEY_IMAGE_UPLOAD_IMAGE_DATA = "images";

    //send apply driver json to server attribute
    public static final String APP_OBJECT_KEY_DRIVER_USERNAME = "username";
    public static final String APP_OBJECT_KEY_DRIVER_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_DRIVER_TYPE = "dtype";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_NUMBER = "car_number";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_BRAND = "car_brand";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_BORN = "car_born";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_REGISTER = "car_reg";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_CC = "car_cc";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_SPECIAL = "car_special";
    public static final String APP_OBJECT_KEY_DRIVER_FILE_IMAGE = "car_files";
    public static final String APP_OBJECT_KEY_DRIVER_CAR_IMAGE = "car_imgs";


    //send create quick taxi order for calling taxi json to server attribute
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_USERNAME = "username";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_BEG = "beg";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_STOP = "stop";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_END = "end";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_LAT = "lat";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_LNG = "lng";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_ZIPCODE = "zipcode";
    public static final String APP_OBJECT_KEY_QUICK_TAXI_ORDER_ADDRESS = "address";

    //send create normal order json to server attribute
    public static final String APP_OBJECT_KEY_ORDER_USERNAME = "username";
    public static final String APP_OBJECT_KEY_ORDER_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_ORDER_TYPE = "dtype";
    public static final String APP_OBJECT_KEY_ORDER_BEG = "beg";
    public static final String APP_OBJECT_KEY_ORDER_STOP = "stop";
    public static final String APP_OBJECT_KEY_ORDER_END = "end";
    public static final String APP_OBJECT_KEY_ORDER_CARGO_TYPE = "cargo_type";
    public static final String APP_OBJECT_KEY_ORDER_CARGO_SIZE= "cargo_size";
    public static final String APP_OBJECT_KEY_ORDER_CARGO_IMAGES = "cargo_imgs";
    public static final String APP_OBJECT_KEY_ORDER_CARGO_SPECIAL = "car_special";
    public static final String APP_OBJECT_KEY_ORDER_TIME_BEGIN = "timebegin";
    public static final String APP_OBJECT_KEY_ORDER_REMARK = "remark";
    public static final String APP_OBJECT_KEY_ORDER_PRICE = "price";
    public static final String APP_OBJECT_KEY_ORDER_TIP = "tip";
    public static final String APP_OBJECT_KEY_ORDER_STATUS = "status";
    public static final String APP_OBJECT_KEY_ORDER_STATUS_CHT = "status_cht";
    public static final String APP_OBJECT_KEY_ORDER_TIMEBEGIN = "timebegin";
    public static final String APP_OBJECT_KEY_ORDER_CLIENT = "client";


    //send create normal order beg json to server attribute
    public static final String APP_OBJECT_KEY_ORDER_LAT = "lat";
    public static final String APP_OBJECT_KEY_ORDER_LNG = "lng";
    public static final String APP_OBJECT_KEY_ORDER_ZIPCODE = "zipcode";
    public static final String APP_OBJECT_KEY_ORDER_ADDRESS = "address";

    //received normal order address attribute
    public static final String APP_OBJECT_KEY_QUERY_ORDER_LATLNG = "latlng";//經緯度以逗號隔開
    public static final String APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE = "zipcode";
    public static final String APP_OBJECT_KEY_QUERY_ORDER_ADDRESS = "address";


    //received json from server attribute
    public static final String APP_OBJECT_KEY_ACCOUNT_INFO_STATUS = "status";
    public static final String APP_OBJECT_KEY_DEVICE_INFO_MESSAGE = "message";
    public static final String APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE = "datas";
    public static final String APP_OBJECT_KEY_NOTIFICATION_INFO_TICKET_ID = "ticket_id";
    public static final String APP_OBJECT_KEY_NOTIFICATION_INFO_TYPE = "type";
    public static final String APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_UID = "uid";
    public static final String APP_OBJECT_KEY_CLIENT_UID = "client_uid";
    public static final String APP_OBJECT_KEY_UPLOAD_FILE_ID = "file_id";
    public static final String APP_OBJECT_KEY_UPLOAD_FILE_URL = "file_url";
    public static final String APP_OBJECT_KEY_DRIVER_DID = "did";
    public static final String APP_OBJECT_KEY_DRIVER_DRIVER_DID = "driver_did";
    public static final String APP_OBJECT_KEY_DRIVER_DRIVER = "driver";
    public static final String APP_OBJECT_KEY_DRIVER_TICKET_ID = "ticket_id";
    public static final String APP_OBJECT_KEY_SEND_NOTIFICATION_MESSAGE = "message";
    public static final String APP_OBJECT_KEY_SEND_ORDER_COMMENT_SCORE = "stars";
    public static final String APP_OBJECT_KEY_CLEAR_NOTIFICATION = "pid";
    public static final String APP_OBJECT_KEY_NOTIFICATION_ID = "id";
    public static final String APP_OBJECT_KEY_USER_REALNAME = "realname";
    public static final String APP_OBJECT_KEY_USER_LEVEL = "level";
    public static final String APP_OBJECT_KEY_USER_TREE = "tree";
    public static final String APP_OBJECT_KEY_DRIVER_ENABLE = "enabled";
    public static final String APP_OBJECT_KEY_DRIVER_ENABLE_CHT = "enabled_cht";
    public static final String APP_OBJECT_KEY_USER_CLIENT_TICKETS = "client_tickets";
    public static final String APP_OBJECT_KEY_USER_DRIVER_TICKETS = "driver_tickets";
    public static final String APP_OBJECT_KEY_BOOKMARK_LOCATION = "locations";


    //get Server Content information
    public static final String APP_OBJECT_KEY_ALL_SERVER_CONTENTS = "contents";
    public static final String APP_OBJECT_KEY_ALL_SERVER_COUNTYS = "countys";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DTYPE = "dtype";
    public static final String APP_OBJECT_KEY_ALL_SERVER_UTYPE = "utype";
    public static final String APP_OBJECT_KEY_ALL_SERVER_CARBRAND = "carbrand";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DSPECIAL = "dspecial";
    public static final String APP_OBJECT_KEY_ALL_SERVER_BOOKMARK = "bookmark";


    public static final String APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CODE = "code";
    public static final String APP_OBJECT_KEY_ALL_SERVER_CONTENTS_TITLE = "title";
    public static final String APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CONTENT = "content";

    public static final String APP_OBJECT_KEY_ALL_SERVER_COUNTYS_ID = "id";
    public static final String APP_OBJECT_KEY_ALL_SERVER_COUNTYS_NAME = "name";

    public static final String APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE = "dtype";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE_CHT = "dtype_cht";

    public static final String APP_OBJECT_KEY_ALL_SERVER_DRIVER_IMAGE_UTYPE = "utype";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DRIVER_IMAGE_UTYPE_NAME = "utype_cht";

    public static final String APP_OBJECT_KEY_ALL_SERVER_CARBRAND_ID = "id";
    public static final String APP_OBJECT_KEY_ALL_SERVER_CARBRAND_NAME = "name";


    public static final String APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_ID = "id";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_DTYPE = "dtype";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_DTPYE_CHT = "dtype_cht";
    public static final String APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_CONTENT = "content";


    public static final String APP_OBJECT_KEY_BOOKMARK_LOCATION_ID = "id";
    public static final String APP_OBJECT_KEY_BOOKMARK_LOCATION_LOCATION = "location";
    public static final String APP_OBJECT_KEY_BOOKMARK_LOCATION_LAT = "lat";
    public static final String APP_OBJECT_KEY_BOOKMARK_LOCATION_LNG = "lng";


    //get User tree information from sever
    public static final String APP_OBJECT_KEY_USER_TREE_LV = "lv";
    public static final String APP_OBJECT_KEY_USER_TREE_LAST_WATERING = "last_watering";
    public static final String APP_OBJECT_KEY_USER_TREE_NEXT = "next";
    public static final String APP_OBJECT_KEY_USER_TREE_STATUS = "status";



    //send user location json to server attribute
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_USERNAME = "username";
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_LONGITUDE = "lat";
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_LATITUDE = "lng";

    //send request user location json to server attribute
    public static final String APP_OBJECT_KEY_GET_USER_GPS_USERNAME = "username";
    public static final String APP_OBJECT_KEY_GET_USER_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_GET_USER_UID = "uid";

    //send customer feedback to server
    public static final String APP_OBJECT_KEY_SEND_CUSTOMER_FEEDBACK_NAME = "name";
    public static final String APP_OBJECT_KEY_SEND_CUSTOMER_FEEDBACK_CONTACT = "contact";
    public static final String APP_OBJECT_KEY_SEND_CUSTOMER_FEEDBACK_CONTENT = "content";

    //get customer tree watering
    public static final String APP_OBJECT_KEY_CLIENT_TREE_WATERING = "m_tree_watering";
    //gain bound
    public static final String APP_OBJECT_KEY_CLIENT_GAIN_BOUND = "m_tree_crop";



    public enum APP_REGISTER_RESPONSE_CODE
    {
        K_APP_REGISTER_RESPONSE_CODE_SUCCESS (100)  ,
        K_APP_REGISTER_RESPONSE_CODE_SMS_SEND_FAIL (403)     ,
        K_APP_REGISTER_RESPONSE_CODE_DATABASE_ERROR (401)  ,
        K_APP_REGISTER_RESPONSE_CODE_RECOMMEND_ERROR (803) ,
        K_APP_REGISTER_RESPONSE_CODE_USER_IDCARD_EXIST (802),
        K_APP_REGISTER_RESPONSE_CODE_USER_ACCOUNT_EXIST (801),
        K_APP_REGISTER_RESPONSE_CODE_ENTER_ERROR (900);

        private int value;

        private APP_REGISTER_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_VERIFY_RESPONSE_CODE
    {
        K_APP_ACCOUNT_VERIFY_CODE_SUCCESS (100)  ,
        K_APP_ACCOUNT_VERIFY_CODE_DATABASE_ERROR (401)  ,
        K_APP_ACCOUNT_VERIFY_CODE_USER_EXIST (702),
        K_APP_ACCOUNT_VERIFY_CODE_EXPIRED (801),
        K_APP_ACCOUNT_VERIFY_CODE_ERROR (711),
        K_APP_ACCOUNT_VERIFY_CODE_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_VERIFY_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE
    {
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_SUCCESS (100)  ,
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_SYSTEM_ERROR (403)  ,
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_APPLY_ERROR (713),
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_DATABASE_ERROR (401),
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_TIMEOUT (703),
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_REPEATED_ERROR (702),
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_ACCOUNT_NO_EXSIT (701),
        K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE
    {
        K_APP_ACCOUNT_RE_SEND_PASSWORD_SUCCESS (100)  ,
        K_APP_ACCOUNT_RE_SEND_PASSWORD_SYSTEM_ERROR (403)  ,
        K_APP_ACCOUNT_RE_SEND_PASSWORD_DATABASE_ERROR (401),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_TIMEOUT (703),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_COMPARE_ERROR (706),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_VERIFY_FAIL (712),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_REPEATED_ERROR (713),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_ACCOUNT_NO_EXSIT (701),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_ACCOUNT_NO_VERIFY (704),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_IDCARD_NO_EXSIT (705),
        K_APP_ACCOUNT_RE_SEND_PASSWORD_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE
    {
        K_APP_ACCOUNT_LOGIN_VERIFY_CODE_SUCCESS (100)  ,
        K_APP_ACCOUNT_LOGIN_VERIFY_CODE_DATABASE_ERROR (401),
        K_APP_ACCOUNT_LOGIN_VERIFY_CODE_PASSWORD_ERROR (707),
        K_APP_ACCOUNT_LOGIN_VERIFY_CODE_ACCOUNT_NO_EXSIT (701),
        K_APP_ACCOUNT_LOGIN_VERIFY_CODE_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE
    {
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_SUCCESS (100)  ,
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_SAME_OLD_ERROR (902),
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_EXPIRED_ERROR (708),
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_NO_EXSIT (701),
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_NO_SMS (704),
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_OLD_PASSWORD_ERROR (709),
        K_APP_ACCOUNT_CHANE_PASSWORD_CODE_DATABASE_ERROR(401),
        K_APP_ACCOUNT_LOGIN_VERIFY_CODE_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_GET_COMMENT_RESPONSE_CODE
    {
        K_APP_ACCOUNT_COMMENT_CODE_SUCCESS (100)  ,
        K_APP_ACCOUNT_COMMENT_ACCOUNT_EXPIRED (708),
        K_APP_ACCOUNT_COMMENT_ACCOUNT_NO_SMS_VERIFY(704),
        K_APP_ACCOUNT_COMMENT_NO_EXSIT (701),
        K_APP_ACCOUNT_COMMENT_ACCOUNT_ERROR (703),
        K_APP_ACCOUNT_COMMENT_DATABASE_ERROR (403),
        K_APP_GET_PUSH_CODE_ENTER_ERROR (900);

        private int value;

        private APP_GET_COMMENT_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };
    public enum APP_GET_PUSH_RESPONSE_CODE
    {
        K_APP_GET_PUSH_CODE_SUCCESS (100)  ,
        K_APP_GET_PUSH_CODE_ACCOUNT_EXPIRED (708),
        K_APP_GET_PUSH_CODE_ACCOUNT_NO_SMS_VERIFY(704),
        K_APP_GET_PUSH_CODE_ACCOUNT_NO_EXSIT (701),
        K_APP_GET_PUSH_CODE_ACCOUNT_ERROR (703),
        K_APP_GET_PUSH_CODE_ACCOUNT_VERIFY_ERROR (903),
        K_APP_GET_PUSH_CODE_ACCOUNT_NOT_DRIVER (9301),
        K_APP_GET_PUSH_CODE_ENTER_ERROR (900);

        private int value;

        private APP_GET_PUSH_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_POST_UPLOAD_IMAGE_RESPONSE_CODE
    {
        K_APP_POST_UPLOAD_IMAGE_SUCCESS (100)  ,
        K_APP_POST_UPLOAD_IMAGE_ACCOUNT_ERROR (402),
        K_APP_POST_UPLOAD_IMAGE_OVER_SIZE(503),
        K_APP_POST_UPLOAD_IMAGE_ERROR_FORMAT (501),
        K_APP_POST_UPLOAD_IMAGE_ERROR (502),
        K_APP_POST_UPLOAD_IMAGE_VERIFY_EXPIRED(708),
        K_APP_POST_UPLOAD_IMAGE_NO_SMS_VERIFY(704),
        K_APP_POST_UPLOAD_IMAGE_NO_ACCOUNT(701),
        K_APP_POST_UPLOAD_IMAGE_ENTER_ERROR (900);

        private int value;

        private APP_POST_UPLOAD_IMAGE_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_PUTS_APPLY_DRIVER_RESPONSE_CODE
    {
        K_APP_PUTS_APPLY_DRIVER_SUCCESS (100)  ,
        K_APP_PUTS_APPLY_DRIVER_ACCOUNT_EXIT (835),
        K_APP_PUTS_APPLY_DRIVER_VERIFY_ERROR(834),
        K_APP_PUTS_APPLY_DRIVER_VERIFYING (833),
        K_APP_PUTS_APPLY_DRIVER_VERIFY_EXPIRED (708),
        K_APP_PUTS_APPLY_DRIVER_NO_SMS_VERIFY(704),
        K_APP_PUTS_APPLY_DRIVER_NO_ACCOUNT(701),
        K_APP_PUTS_APPLY_DRIVER_ENTER_ERROR (900),
        K_APP_PUTS_APPLY_DRIVER_CAR_CC_ERROR(919),
        K_APP_PUTS_APPLY_DRIVER_CAR_BORN_ERROR(918),
        K_APP_PUTS_APPLY_DRIVER_CAR_BRAND_ERROR(917);


        private int value;

        private APP_PUTS_APPLY_DRIVER_RESPONSE_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_PUTS_USER_GPS_CODE
    {
        K_APP_ACCOUNT_PUTS_USER_GPS_CODE_SUCCESS (100)  ,
        K_APP_ACCOUNT_PUTS_USER_GPS_CODE_DATABASE_ERROR (401)  ,
        K_APP_ACCOUNT_PUTS_APPLY_DRIVER_NO_SMS_VERIFY(704),
        K_APP_ACCOUNT_PUTS_USER_GPS_CODE_EXPIRED (708),
        K_APP_ACCOUNT_PUTS_USER_GPS_NO_ACCOUNT (701),
        K_APP_ACCOUNT_PUTS_USER_GPS_CODE_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_PUTS_USER_GPS_CODE(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_ACCOUNT_CREATE_NORMEL_ORDER
    {
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_SUCCESS (100)  ,
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_DATABASE_ERROR (401)  ,
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_NO_SMS_VERIFY(704),
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_EXPIRED (708),
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_NO_ACCOUNT (701),
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_NO_GPS (837),
        K_APP_ACCOUNT_CREATE_NORMEL_ORDER_ENTER_ERROR (900);

        private int value;

        private APP_ACCOUNT_CREATE_NORMEL_ORDER(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_DRIVER_WORK_IDENTIFY
    {
        K_APP_DRIVER_WORK_IDENTITY_SUCCESS (100)  ,
        K_APP_DRIVER_WORK_IDENTITY_DATABASE_ERROR (401)  ,
        K_APP_DRIVER_WORK_IDENTITY_EXPIRED (708),
        K_APP_DRIVER_WORK_IDENTITY_WORK_ERROR(836),
        K_APP_DRIVER_WORK_IDENTITY_NO_EXSIT (701),
        K_APP_DRIVER_WORK_IDENTITY_VERIFY_ERROR (903),
        K_APP_DRIVER_WORK_IDENTITY_ERROR (900);

        private int value;

        private APP_DRIVER_WORK_IDENTIFY(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_DRIVER_TAKE_OVER_ORDER
    {
        K_APP_DRIVER_TAKE_OVER_ORDER_SUCCESS (100)  ,
        K_APP_DRIVER_TAKE_OVER_ORDER_DATABASE_ERROR (401),
        K_APP_DRIVER_TAKE_OVER_ORDER_EXPIRED (708),
        K_APP_DRIVER_TAKE_IDENTITY_ERROR (836),
        K_APP_DRIVER_TAKE_OVER_ORDER_NO_SMS_VERIFY(704),
        K_APP_DRIVER_TAKE_OVER_ORDER_NO_EXSIT (701),
        K_APP_DRIVER_TAKE_OVER_ORDER_ERROR (900);

        private int value;

        private APP_DRIVER_TAKE_OVER_ORDER(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_DRIVER_RECOMMEND_ORDER
    {
        K_APP_DRIVER_RECOMMEND_ORDER_SUCCESS (100),
        K_APP_DRIVER_RECOMMEND_ORDER_EXPIRED (708),
        K_APP_DRIVER_NO_WORK (836),
        K_APP_DRIVER_RECOMMEND_ORDER_SMS_VERIFY(704),
        K_APP_DRIVER_DRIVER_RECOMMEND_NO_EXSIT (701),
        K_APP_DRIVER_RECOMMEND_ORDER_ERROR (900);

        private int value;

        private APP_DRIVER_RECOMMEND_ORDER(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_DRIVER_QUERY_ORDER_LIST
    {
        K_APP_DRIVER_QUERY_ORDER_SUCCESS (100),
        K_APP_DRIVER_QUERY_ORDER_EXPIRED (708),
        K_APP_DRIVER_QUERY_ORDER_EMPTY (840),
        K_APP_DRIVER_QUERY_ORDER_SMS_VERIFY(704),
        K_APP_DRIVER_QUERY_ORDER_NO_EXSIT (701),
        K_APP_DRIVER_QUERY_ORDER_NO_WORK (836),
        K_APP_DRIVER_QUERY_ORDER_VERIFY_ERROR(903);

        private int value;

        private APP_DRIVER_QUERY_ORDER_LIST(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_CUSTOMER_ADD_TREE_WATERING
    {
        K_APP_CUSTOMER_ADD_TREE_WATERING_SUCCESS (100),
        K_APP_CUSTOMER_ADD_TREE_WATERING_EXPIRED (708),
        K_APP_CUSTOMER_ADD_TREE_WATERING_GROWN (842),
        K_APP_CUSTOMER_ADD_TREE_WATERING_SMS_VERIFY(704),
        K_APP_CUSTOMER_ADD_TREE_WATERING_NO_EXSIT (701),
        K_APP_CUSTOMER_ADD_TREE_WATERING_ERROR (401),
        K_APP_CUSTOMER_ADD_HAS_BEEN_TREE_WATERING(841);

        private int value;

        private APP_CUSTOMER_ADD_TREE_WATERING(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public enum APP_CUSTOMER_TAKE_BOUNS
    {
        K_APP_CUSTOMER_TAKE_BOUNS_SUCCESS (100),
        K_APP_CUSTOMER_TAKE_BOUNS_EXPIRED (708),
        K_APP_CUSTOMER_TAKE_BOUNS_NO_GROWN (843),
        K_APP_CUSTOMER_TAKE_BOUNS_SMS_VERIFY(704),
        K_APP_CUSTOMER_TAKE_BOUNS_NO_EXSIT (701),
        K_APP_CUSTOMER_TAKE_BOUNS_ERROR (900),
        K_APP_CUSTOMER_TAKE_BOUNS_DATABASE_ERROR (401),
        K_APP_CUSTOMER_TAKE_BOUNS_WAIT_TOMORROW(844);

        private int value;

        private APP_CUSTOMER_TAKE_BOUNS(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public static APP_REGISTER_RESPONSE_CODE conversion_register_connect_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_SUCCESS;
            }
            if (index == 403) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_SMS_SEND_FAIL;
            }
            if (index == 401) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_DATABASE_ERROR;
            }
            if (index == 803) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_RECOMMEND_ERROR;
            }
            if (index == 802) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_USER_IDCARD_EXIST;
            }
            if (index == 801) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_USER_ACCOUNT_EXIST;
            }
            if (index == 900) {
                return APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_ACCOUNT_VERIFY_RESPONSE_CODE conversion_verify_code_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS;
            }
            if (index == 401) {
                return APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_DATABASE_ERROR;
            }
            if (index == 702) {
                return APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_USER_EXIST;
            }
            if (index == 801) {
                return APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_EXPIRED;
            }
            if (index == 711) {
                return APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_ERROR;
            }
            if (index == 900) {
                return APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE conversion_resend_verify_code_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_SUCCESS;
            }
            if (index == 403) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_SYSTEM_ERROR;
            }
            if (index == 713) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_APPLY_ERROR;
            }
            if (index == 401) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_DATABASE_ERROR;
            }
            if (index == 703) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_TIMEOUT;
            }
            if (index == 702) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_REPEATED_ERROR;
            }
            if (index == 701) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_ACCOUNT_NO_EXSIT;
            }
            if (index == 900) {
                return APP_ACCOUNT_RE_SEND_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_RE_SEND_VERIFY_CODE_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE conversion_login_verify_code_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_SUCCESS;
            }
            if (index == 401) {
                return APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_DATABASE_ERROR;
            }
            if (index == 707) {
                return APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_PASSWORD_ERROR;
            }
            if (index == 701) {
                return APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_ACCOUNT_NO_EXSIT;
            }
            if (index == 900) {
                return APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE conversion_resend_password_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_SUCCESS;
            }
            if (index == 403) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_SYSTEM_ERROR;
            }
            if (index == 713) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_REPEATED_ERROR;
            }
            if (index == 712) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_VERIFY_FAIL;
            }
            if (index == 401) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_DATABASE_ERROR;
            }
            if (index == 703) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_TIMEOUT;
            }
            if (index == 704) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_ACCOUNT_NO_VERIFY;
            }
            if (index == 705) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_IDCARD_NO_EXSIT;
            }
            if (index == 706) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_COMPARE_ERROR;
            }
            if (index == 701) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_ACCOUNT_NO_EXSIT;
            }
            if (index == 900) {
                return APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_GET_PUSH_RESPONSE_CODE conversion_get_put_notification_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS;
            }
            if (index == 708) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_EXPIRED;
            }
            if (index == 701) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_NO_EXSIT;
            }
            if (index == 704) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_NO_SMS_VERIFY;
            }
            if (index == 900) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ENTER_ERROR;
            }

            if (index == 903) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_VERIFY_ERROR;
            }

            if (index == 9301) {
                return APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_NOT_DRIVER;
            }
        }
        return null;
    }

    public static APP_GET_COMMENT_RESPONSE_CODE conversion_get_account_comment_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_GET_COMMENT_RESPONSE_CODE.K_APP_ACCOUNT_COMMENT_CODE_SUCCESS;
            }
            if (index == 708) {
                return APP_GET_COMMENT_RESPONSE_CODE.K_APP_ACCOUNT_COMMENT_ACCOUNT_EXPIRED;
            }
            if (index == 701) {
                return APP_GET_COMMENT_RESPONSE_CODE.K_APP_ACCOUNT_COMMENT_NO_EXSIT;
            }
            if (index == 704) {
                return APP_GET_COMMENT_RESPONSE_CODE.K_APP_ACCOUNT_COMMENT_ACCOUNT_NO_SMS_VERIFY;
            }
            if (index == 900) {
                return APP_GET_COMMENT_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ENTER_ERROR;
            }

            if (index == 401) {
                return APP_GET_COMMENT_RESPONSE_CODE.K_APP_ACCOUNT_COMMENT_DATABASE_ERROR;
            }


        }
        return null;
    }

    public static APP_POST_UPLOAD_IMAGE_RESPONSE_CODE conversion_upload_image_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_SUCCESS;
            }
            if (index == 402) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_ACCOUNT_ERROR;
            }
            if (index == 503) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_OVER_SIZE;
            }
            if (index == 501) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_ERROR_FORMAT;
            }
            if (index == 502) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_ERROR;
            }
            if (index == 708) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_VERIFY_EXPIRED;
            }
            if (index == 704) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_NO_SMS_VERIFY;
            }
            if (index == 701) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_NO_ACCOUNT;
            }
            if (index == 900) {
                return APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_PUTS_APPLY_DRIVER_RESPONSE_CODE conversion_apply_driver_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_SUCCESS;
            }
            if (index == 835) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_ACCOUNT_EXIT;
            }
            if (index == 834) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_VERIFY_ERROR;
            }
            if (index == 833) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_VERIFYING;
            }
            if (index == 708) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_VERIFY_EXPIRED;
            }
            if (index == 704) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_NO_SMS_VERIFY;
            }
            if (index == 701) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_NO_ACCOUNT;
            }
            if (index == 900) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_ENTER_ERROR;
            }
            if (index == 901) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_CAR_CC_ERROR;
            }
            if (index == 918) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_CAR_BORN_ERROR;
            }
            if (index == 917) {
                return APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_CAR_BRAND_ERROR;
            }

        }
        return null;
    }

    public static APP_DRIVER_RECOMMEND_ORDER conversion_get_put_driver_recommend_order_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_SUCCESS;
            }
            if (index == 708) {
                return APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_EXPIRED;
            }
            if (index == 701) {
                return APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_DRIVER_RECOMMEND_NO_EXSIT;
            }
            if (index == 704) {
                return APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_SMS_VERIFY;
            }
            if (index == 836) {
                return APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_NO_WORK;
            }
            if (index == 900) {
                return APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_ERROR;
            }
        }
        return null;
    }

    public static APP_ACCOUNT_CREATE_NORMEL_ORDER conversion_create_normal_order_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_SUCCESS;
            }
            if (index == 401) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_DATABASE_ERROR;
            }
            if (index == 708) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_EXPIRED;
            }
            if (index == 701) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_NO_ACCOUNT;
            }
            if (index == 704) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_NO_SMS_VERIFY;
            }
            if (index == 837) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_NO_GPS;
            }
            if (index == 900) {
                return APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_DRIVER_WORK_IDENTIFY conversion_driver_work_indentify_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_SUCCESS;
            }
            if (index == 401) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_DATABASE_ERROR;
            }
            if (index == 708) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_EXPIRED;
            }
            if (index == 701) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_NO_EXSIT;
            }
            if (index == 836) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_WORK_ERROR;
            }
            if (index == 903) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_VERIFY_ERROR;
            }
            if (index == 900) {
                return APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_ERROR;
            }
        }
        return null;
    }

    public static APP_DRIVER_TAKE_OVER_ORDER conversion_driver_take_over_order_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_SUCCESS;
            }
            if (index == 401) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_DATABASE_ERROR;
            }
            if (index == 708) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_EXPIRED;
            }
            if (index == 701) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_NO_EXSIT;
            }
            if (index == 704) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_NO_SMS_VERIFY;
            }
            if (index == 836) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_IDENTITY_ERROR;
            }
            if (index == 900) {
                return APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_ERROR;
            }
        }
        return null;
    }


    public static APP_ACCOUNT_PUTS_USER_GPS_CODE conversion_get_put_user_location_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_SUCCESS;
            }
            if (index == 401) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_DATABASE_ERROR;
            }
            if (index == 708) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_EXPIRED;
            }
            if (index == 701) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_NO_ACCOUNT;
            }
            if (index == 704) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_DATABASE_ERROR;
            }
            if (index == 401) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_DATABASE_ERROR;
            }
            if (index == 900) {
                return APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_ENTER_ERROR;
            }
        }
        return null;
    }
    public static APP_DRIVER_QUERY_ORDER_LIST conversion_driver_query_order_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_SUCCESS;
            }

            if (index == 708) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_EXPIRED;
            }
            if (index == 701) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_NO_EXSIT;
            }
            if (index == 840) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_EMPTY;
            }

            if (index == 836) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_NO_WORK;
            }
            if (index == 704) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_SMS_VERIFY;
            }
            if (index == 903) {
                return APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_VERIFY_ERROR;
            }
        }
        return null;
    }

    public static APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE conversion_account_change_password_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_SUCCESS;
            }

            if (index == 902) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_SAME_OLD_ERROR;
            }
            if (index == 708) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_EXPIRED_ERROR;
            }
            if (index == 701) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_NO_EXSIT;
            }

            if (index == 704) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_NO_SMS;
            }
            if (index == 709) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_OLD_PASSWORD_ERROR;
            }
            if (index == 401) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_CHANE_PASSWORD_CODE_DATABASE_ERROR;
            }
            if (index == 900) {
                return APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_ENTER_ERROR;
            }
        }
        return null;
    }

    public static APP_CUSTOMER_ADD_TREE_WATERING conversion_customer_add_watering_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_SUCCESS;
            }

            if (index == 708) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_EXPIRED;
            }
            if (index == 842) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_GROWN;
            }
            if (index == 704) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_SMS_VERIFY;
            }

            if (index == 701) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_NO_EXSIT;
            }
            if (index == 401) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_ERROR;
            }
            if (index == 841) {
                return APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_HAS_BEEN_TREE_WATERING;
            }
        }
        return null;
    }


    public static APP_CUSTOMER_TAKE_BOUNS conversion_customer_take_bouns_result(int index) {
        if (index >= 0) {
            if (index == 100) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_SUCCESS;
            }

            if (index == 708) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_EXPIRED;
            }
            if (index == 843) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_NO_GROWN;
            }
            if (index == 704) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_SMS_VERIFY;
            }

            if (index == 701) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_NO_EXSIT;
            }
            if (index == 900) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_ERROR;
            }
            if (index == 401) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_DATABASE_ERROR;
            }

            if (index == 844) {
                return APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_WAIT_TOMORROW;
            }
        }
        return null;
    }

}
