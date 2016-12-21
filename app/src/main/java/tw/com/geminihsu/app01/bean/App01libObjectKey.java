package tw.com.geminihsu.app01.bean;

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
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_FINISH_ORDER = "t_finish";
    public static final String APP_OBJECT_KEY_PUTS_COMMENT_ORDER = "t_assess";
    public static final String APP_OBJECT_KEY_PUTS_DRIVER_RECOMMEND_ORDER = "t_match";





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
    public static final String APP_OBJECT_KEY_ORDER_CARGO_SIZE= "cargo_size";
    public static final String APP_OBJECT_KEY_ORDER_CARGO_IMAGES = "cargo_imgs";
    public static final String APP_OBJECT_KEY_ORDER_CARGO_SPECIAL = "car_special";
    public static final String APP_OBJECT_KEY_ORDER_REMARK = "remark";
    public static final String APP_OBJECT_KEY_ORDER_PRICE = "price";
    public static final String APP_OBJECT_KEY_ORDER_TIP = "tip";

    //send create normal order beg json to server attribute
    public static final String APP_OBJECT_KEY_ORDER_LAT = "lat";
    public static final String APP_OBJECT_KEY_ORDER_LNG = "lng";
    public static final String APP_OBJECT_KEY_ORDER_ZIPCODE = "zipcode";
    public static final String APP_OBJECT_KEY_ORDER_ADDRESS = "address";

    //received json from server attribute
    public static final String APP_OBJECT_KEY_ACCOUNT_INFO_STATUS = "status";
    public static final String APP_OBJECT_KEY_DEVICE_INFO_MESSAGE = "message";
    public static final String APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE = "datas";
    public static final String APP_OBJECT_KEY_NOTIFICATION_INFO_TICKET_ID = "ticket_id";
    public static final String APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_UID = "uid";
    public static final String APP_OBJECT_KEY_UPLOAD_FILE_ID = "file_id";
    public static final String APP_OBJECT_KEY_UPLOAD_FILE_URL = "file_url";
    public static final String APP_OBJECT_KEY_DRIVER_DID = "did";
    public static final String APP_OBJECT_KEY_DRIVER_TICKET_ID = "ticket_id";
    public static final String APP_OBJECT_KEY_SEND_NOTIFICATION_MESSAGE = "message";
    public static final String APP_OBJECT_KEY_SEND_ORDER_COMMENT_SCORE = "stars";
    public static final String APP_OBJECT_KEY_CLEAR_NOTIFICATION = "pid";
    public static final String APP_OBJECT_KEY_NOTIFICATION_ID = "id";
    public static final String APP_OBJECT_KEY_USER_REALNAME = "realname";


    //send user location json to server attribute
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_USERNAME = "username";
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_ACCESSKEY = "accesskey";
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_LONGITUDE = "lat";
    public static final String APP_OBJECT_KEY_UPLOAD_GPS_LATITUDE = "lng";


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

    public enum APP_GET_PUSH_RESPONSE_CODE
    {
        K_APP_GET_PUSH_CODE_SUCCESS (100)  ,
        K_APP_GET_PUSH_CODE_ACCOUNT_EXPIRED (708),
        K_APP_GET_PUSH_CODE_ACCOUNT_NO_SMS_VERIFY(704),
        K_APP_GET_PUSH_CODE_ACCOUNT_NO_EXSIT (701),
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
}
