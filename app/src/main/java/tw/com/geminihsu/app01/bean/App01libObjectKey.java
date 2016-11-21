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
    public static final String APP_OBJECT_KEY_PUTS_METHOD_LOGIN_VERIFY = "m_auth_user";


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


    //send login json to server attribute
    public static final String APP_OBJECT_KEY_LOGIN_USERNAME = "username";
    public static final String APP_OBJECT_KEY_LOGIN_PASSWORD = "password";
    public static final String APP_OBJECT_KEY_LOGIN_REGISTER_ID = "registration_id";
    public static final String APP_OBJECT_KEY_LOGIN_PLATFORM = "devicePlatform";
    public static final String APP_OBJECT_KEY_LOGIN_APP_VERSION = "appVer";


    //received json from server attribute
    public static final String APP_OBJECT_KEY_ACCOUNT_INFO_STATUS = "status";
    public static final String APP_OBJECT_KEY_DEVICE_INFO_MESSAGE = "message";

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

}
