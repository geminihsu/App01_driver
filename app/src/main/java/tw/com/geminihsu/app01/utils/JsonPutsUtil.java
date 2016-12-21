package tw.com.geminihsu.app01.utils;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.com.geminihsu.app01.BuildConfig;
import tw.com.geminihsu.app01.MainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.RegisterActivity;
import tw.com.geminihsu.app01.VerifyCodeActivity;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.App01libObjectKey;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.ImageBean;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.uploadImage.AppHelper;
import tw.com.geminihsu.app01.uploadImage.VolleyMultipartRequest;
import tw.com.geminihsu.app01.uploadImage.VolleySingleton;

/**
 * Created by geminihsu on 2016/11/20.
 */

public class JsonPutsUtil {
    private final Context mContext;
    private final String TAG = JsonPutsUtil.class.toString();
    private RequestQueue requestQueue;


    public JsonPutsUtil(Context mContext) {
        this.mContext = mContext;
        this.requestQueue = Volley.newRequestQueue(mContext);

    }

    public void sendRegisterRequest(final AccountInfo user)
    {
        String versionName = BuildConfig.VERSION_NAME;
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_REGISTER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_PASSWORD, user.getPassword());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_IDCARD, user.getIdentify());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_REALNAME, user.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_REGISTER_ID, user.getRegisterToken());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_PLATFORM, "android");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_APP_VERSION, versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_REGISTER_RESPONSE_CODE connectResult =App01libObjectKey.conversion_register_connect_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_SUCCESS))
                    {
                        int uid = jsonObject.getInt(App01libObjectKey.APP_OBJECT_KEY_UID);
                        user.setUid(""+uid);
                        Constants.Driver = false;
                        if (mClientRegisterDataManagerCallBackFunction!=null)
                            mClientRegisterDataManagerCallBackFunction.registerClient(user);


                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void sendReSendPasswordRequest(final AccountInfo accountInfo)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_RE_SEND_PASSWORD);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_USERNAME, accountInfo.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_IDCARD, accountInfo.getIdentify());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE connectResult =App01libObjectKey.conversion_resend_password_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_SUCCESS))
                    {
                        /*RealmUtil realmUtil = new RealmUtil(mContext);
                        AccountInfo user = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,accountInfo.getPhoneNumber());

                        if(user==null)
                        {
                            user = new AccountInfo();
                            user.setPhoneNumber(user.getPhoneNumber());
                            user.setIdentify(user.getIdentify());
                        }*/
                        if (mClientSmsVerifyDataManagerCallBackFunction !=null)
                            mClientSmsVerifyDataManagerCallBackFunction.reSendSMS(accountInfo);

                        //sendLoginRequest(accountInfo);
                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    //會員-忘記密碼-驗證&修改密碼
    public void sendForgetModify(final AccountInfo accountInfo, String verifiyCode, final String newPassword)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_RE_MODIFY_PASSWORD);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_USERNAME, accountInfo.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_IDCARD, accountInfo.getIdentify());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_VERIFY_CODE, verifiyCode);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_NEW_PASSWORD, newPassword);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE connectResult =App01libObjectKey.conversion_resend_password_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_SUCCESS))
                    {
                        RealmUtil realmUtil = new RealmUtil(mContext);
                        AccountInfo user = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,accountInfo.getPhoneNumber());

                        if(user==null)
                        {
                            user = new AccountInfo();
                            user.setPhoneNumber(accountInfo.getPhoneNumber());
                            user.setIdentify(accountInfo.getIdentify());
                            user.setPassword(newPassword);
                            String token = FirebaseInstanceId.getInstance().getToken();
                            user.setRegisterToken(token);
                        }
                        sendLoginRequest(user);
                        //if (mClientSmsVerifyDataManagerCallBackFunction !=null)
                        //    mClientSmsVerifyDataManagerCallBackFunction.reSendSMS(user);


                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void sendVerify(String code, final AccountInfo user)
    {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_VERIFY);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_VERIFY_CODE, code);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_VERIFY_USERNAME, user.getPhoneNumber());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS))
                    {

                        if (mClientSmsVerifyDataManagerCallBackFunction !=null)
                            mClientSmsVerifyDataManagerCallBackFunction.verifyClient(user);



                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void sendLoginRequest(final AccountInfo user)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String versionName = BuildConfig.VERSION_NAME;


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_LOGIN_VERIFY);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_PASSWORD, user.getPassword());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_REGISTER_ID, user.getRegisterToken());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_PLATFORM, "android");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_APP_VERSION, versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    String accesskey = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY);
                    App01libObjectKey.APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_login_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_SUCCESS))
                    {
                        user.setAccessKey(accesskey);
                        getUserInfo(user);


                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void getUserInfo(final AccountInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_GET_USER_INFORMATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[getUserInfo]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);

                        if(!datas.equals("null")){
                            //JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String id = data.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                            String did = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DID);

                            user.setUid(id);
                            RealmUtil database = new RealmUtil(mContext);
                            database.addAccount(user);
                            //設定檔顯示登入的帳號密碼
                            SharedPreferences  configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

                            configSharedPreferences.edit().putString(mContext.getString(R.string.config_login_phone_number_key), user.getPhoneNumber()).commit();
                            configSharedPreferences.edit().putString(mContext.getString(R.string.config_login_password_key), user.getPassword()).commit();

                            if(Integer.valueOf(did)>0)
                            {
                                //表示為司機身份
                                //需要下命令取得司機資料再存到資料庫
                                getDriverInfo(user);

                            }else {
                                if (mClientSmsVerifyDataManagerCallBackFunction !=null)
                                    mClientSmsVerifyDataManagerCallBackFunction.reSendSMS(user);


                                if (mClientLoginDataManagerCallBackFunction != null)
                                    mClientLoginDataManagerCallBackFunction.loginClient(user);
                            }


                        }
                        Log.e(TAG, "get push!!!!");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null)
                    Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void getDriverInfo(final AccountInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_GET_DRIVER_INFORMATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UID, user.getUid());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[getDriverInfo]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);

                        if(!datas.equals("null")){
                            //JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String did = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DID);
                            String dtype = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TYPE);
                            String carbrand = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BRAND);
                            String carnumber = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_NUMBER);
                            String carborn = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BORN);
                            String carcc = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_CC);
                            String carimage = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_IMAGE);



                            DriverIdentifyInfo driver = new DriverIdentifyInfo();
                            driver.setUid(user.getUid());
                            driver.setName(user.getPhoneNumber());
                            driver.setDid(did);
                            driver.setDtype(dtype);
                            driver.setCar_brand(carbrand);
                            driver.setCar_number(carnumber);
                            driver.setCar_born(carborn);
                            driver.setCar_cc(carcc);
                            driver.setCar_imgs(carimage);



                            RealmUtil database = new RealmUtil(mContext);
                            database.addDriverInfo(driver);
                            //設定檔顯示登入的帳號密碼
                            SharedPreferences  configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);


                            if (mClientLoginDataManagerCallBackFunction !=null)
                                mClientLoginDataManagerCallBackFunction.loginDriver(driver);



                        }
                        Log.e(TAG, "get push!!!!");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null)
                    Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getPushNotification(final AccountInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_GET_PUSH_NOTIFICATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);

                        if(!datas.equals("null")){
                            JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);

                                JSONObject messageInfo =object.getJSONObject("data");
                                String message = messageInfo.optString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);
                                String ticketOrder = messageInfo.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_TICKET_ID);

                                // RealmUtil data = new RealmUtil(mContext);
                               // NormalOrder order = data.queryOrder(Constants.ORDER_TICKET_ID,App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_TICKET_ID);
                                Log.e(TAG, "message from notification:" + message);
                                sendNotification("一般搭乘ticket no:"+ticketOrder,message);
                                clearPushNotification(user,Integer.valueOf(id));
                                /*JSONArray messageObj = object.getJSONArray("data");
                                for (int j = 0; j < messageObj.length(); j++) {
                                    JSONObject objectInfo = messageObj.getJSONObject(i);
                                    String message = objectInfo.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                                    Log.e(TAG, "message from notification:" + message);
                                    //sendNotification(message);
                                }*/
                            }
                        }
                        Log.e(TAG, "get push!!!!");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void clearPushNotification(AccountInfo user,int pid) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_DELETE_PUSH_NOTIFICATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_CLEAR_NOTIFICATION, pid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[clearPushNotification]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {

                        Log.e(TAG, "get push!!!!");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void putsUserGPSLocation(double longitude, double latitude, AccountInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_GPS_LOCATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_GPS_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_GPS_ACCESSKEY, user.getAccessKey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_GPS_LONGITUDE, longitude);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_GPS_LATITUDE, latitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[putsUserGPSLocation]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_ACCOUNT_PUTS_USER_GPS_CODE connectResult = App01libObjectKey.conversion_get_put_user_location_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_SUCCESS)) {
                        Log.e(TAG, "get GPS success!!!!");
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null)
                 Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //查詢客戶訂單
    public void queryClientOrderList(AccountInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_QUERY_CLIENT_ORDER_LIST);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[queryClientOrderList]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //查詢司機訂單
    public void queryDriverOrderList(DriverIdentifyInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_QUERY_DRIVER_ORDER_LIST);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccesskey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[queryDRIVEROrderList]:"+ jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    // 訂單-自動配對可以接的訂單
    public void queryRecommendOrderList(final DriverIdentifyInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_RECOMMEND_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccesskey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[queryRecommendOrderList]:"+ jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_DRIVER_RECOMMEND_ORDER connectResult = App01libObjectKey.conversion_get_put_driver_recommend_order_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                        if(datas!=null)
                        {
                            JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String queryId="";
                            for (int i = 0; i < info.length(); i++){
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                                queryId = id;
                            }
                            queryOrderInformation(user,queryId);
                        }
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //查詢訂單明細
    public void queryOrderInformation(DriverIdentifyInfo user, final String orderId) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_QUERY_ORDER_DETAIL);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID, Integer.valueOf(orderId));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[queryOrderInformation]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                        if(datas!=null)
                        {
                            JSONObject object = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_TICKET_ID);

                            if(id.equals(orderId))
                            {
                                //取得訂單詳細資料
                            }
                            // JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
//                            for (int i = 0; i < info.length(); i++){
//                                JSONObject object = info.getJSONObject(i);
//                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
//
//                            }

                        }
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //推訊息給訂單建立者
    public void pushNotificationToOrderOwner(NormalOrder order,String content) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_PUSH_NOTIFICATION_TO_ORDER_OWNER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID, Integer.valueOf(order.getTicket_id()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_SEND_NOTIFICATION_MESSAGE, content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[pushNotificationToOrderOwner]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //訂單-快速接單(司機權限)
    public void driverTakeOverOrder(final NormalOrder order) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_TAKE_OVER_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID, Integer.valueOf(order.getTicket_id()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[driverTakeOverOrder]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_DRIVER_TAKE_OVER_ORDER connectResult = App01libObjectKey.conversion_driver_take_over_order_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_DRIVER_TAKE_OVER_ORDER.K_APP_DRIVER_TAKE_OVER_ORDER_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        if (mDriverRequestTakeOverOrderManagerCallBackFunction!=null)
                            mDriverRequestTakeOverOrderManagerCallBackFunction.driverTakeOverOrder(order);

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //會員-更改營運中的司機身分
    public void driverWorkIdentity(final DriverIdentifyInfo driverIdentifyInfo) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_WORK_IDENTITY);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, driverIdentifyInfo.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, driverIdentifyInfo.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DID, Integer.valueOf(driverIdentifyInfo.getDid()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[driverWorkIdentity]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_DRIVER_WORK_IDENTIFY connectResult = App01libObjectKey.conversion_driver_work_indentify_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_DRIVER_WORK_IDENTIFY.K_APP_DRIVER_WORK_IDENTITY_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");

                        if(mDriverChangeWorkIdentityManagerCallBackFunction!=null)
                            mDriverChangeWorkIdentityManagerCallBackFunction.driverChangeWorkIdentity(driverIdentifyInfo);
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //訂單-結案訂單(司機權限)
    public void driverFinishOrder(final NormalOrder order) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_FINISH_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID, Integer.valueOf(order.getTicket_id()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[driverTakeOverOrder]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        if (mDriverRequestFinishOrderManagerCallBackFunction!=null)
                            mDriverRequestFinishOrderManagerCallBackFunction.driverFinishOrder(order);

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //訂單-給訂單對方評價星星數
    public void commentOrder(final NormalOrder order, int star) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_COMMENT_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID, Integer.valueOf(order.getTicket_id()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_SEND_ORDER_COMMENT_SCORE, star);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[commentOrder]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        //pushNotificationToOrderOwner(order,"恭喜你完成訂單!");

                        if (mDriverRequestClientCommentManagerCallBackFunction!=null)
                            mDriverRequestClientCommentManagerCallBackFunction.driverAskComment(order);

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //申請註冊司機
    public void registerDriverAccount(final DriverIdentifyInfo driverIdentifyInfo) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_REGISTER_DRIVER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_USERNAME, driverIdentifyInfo.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_ACCESSKEY, driverIdentifyInfo.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TYPE, driverIdentifyInfo.getDtype());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_NUMBER, driverIdentifyInfo.getCar_number());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BRAND, driverIdentifyInfo.getCar_brand());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BORN, driverIdentifyInfo.getCar_born());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_REGISTER, driverIdentifyInfo.getCar_reg());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_CC, driverIdentifyInfo.getCar_cc());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_SPECIAL, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_FILE_IMAGE, driverIdentifyInfo.getCar_files());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_IMAGE, driverIdentifyInfo.getCar_imgs());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_PUTS_APPLY_DRIVER_RESPONSE_CODE connectResult = App01libObjectKey.conversion_apply_driver_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_PUTS_APPLY_DRIVER_RESPONSE_CODE.K_APP_PUTS_APPLY_DRIVER_SUCCESS)) {
                        Log.e(TAG, "register success!!!!");
                        int did = jsonObject.getInt(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DID);
                        driverIdentifyInfo.setDid(""+did);
                        RealmUtil database = new RealmUtil(mContext);
                        database.addDriverInfo(driverIdentifyInfo);
                        if (mServerRequestDataManagerCallBackFunction!=null)
                            mServerRequestDataManagerCallBackFunction.registerDriver(driverIdentifyInfo);

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //client create order call taxi
    public void putCreateQuickTaxiOrder(final NormalOrder order) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject begin = new JSONObject();
        /*try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, 24.09133);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, 120.540315);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, 404);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, "台中市北區市政路172號");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.valueOf(order.getBegin().getLatitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.valueOf(order.getBegin().getLongitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(order.getBegin().getZipcode()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getBegin().getAddress());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JSONObject end = new JSONObject();
        /*try {
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, 24.14411);
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, 120.679567);
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, 400);
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, "台中市中區柳川里成功路300號");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        try {
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.valueOf(order.getEnd().getLatitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.valueOf(order.getEnd().getLongitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(order.getEnd().getZipcode()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getEnd().getAddress());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray jsonArrayBegin = new JSONArray();

        jsonArrayBegin.put(begin);

        JSONArray jsonArrayEnd = new JSONArray();

        jsonArrayEnd.put(end);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_CREATE_QUICK_TAXI_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_BEG, jsonArrayBegin);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_STOP, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_END, jsonArrayEnd);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        String ticket_id = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID);
                        order.setTicket_id(ticket_id);
                        RealmUtil database = new RealmUtil(mContext);
                        database.addNormalOrder(order);
                        if (mServerRequestOrderManagerCallBackFunction!=null)
                            mServerRequestOrderManagerCallBackFunction.createNormalOrder(order);

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //client create normal order
    public void putCreateNormalOrder(final NormalOrder order) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject begin = new JSONObject();
        /*try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, 24.09133);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, 120.540315);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, 404);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, "台中市北區市政路172號");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.valueOf(order.getBegin().getLatitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.valueOf(order.getBegin().getLongitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(order.getBegin().getZipcode()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getBegin().getAddress());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JSONObject end = new JSONObject();
        /*try {
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, 24.14411);
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, 120.679567);
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, 400);
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, "台中市中區柳川里成功路300號");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        try {
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.valueOf(order.getEnd().getLatitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.valueOf(order.getEnd().getLongitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(order.getEnd().getZipcode()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getEnd().getAddress());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray jsonArrayBegin = new JSONArray();

        jsonArrayBegin.put(begin);

        JSONArray jsonArrayEnd = new JSONArray();

        jsonArrayEnd.put(end);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_CREATE_NORMAL_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE, 4);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_BEG, jsonArrayBegin);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_STOP, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_END, jsonArrayEnd);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SIZE, 20);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_IMAGES, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SPECIAL, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_REMARK, "test");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE, Integer.valueOf(order.getPrice()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP, Integer.valueOf(order.getTip()));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[putCreateNormalOrder]:"+ jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_ACCOUNT_CREATE_NORMEL_ORDER connectResult = App01libObjectKey.conversion_create_normal_order_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_ACCOUNT_CREATE_NORMEL_ORDER.K_APP_ACCOUNT_CREATE_NORMEL_ORDER_SUCCESS)) {
                        String ticket_id = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID);
                        Log.e(TAG, "get push!!!!");
                        order.setTicket_id(ticket_id);
                        RealmUtil database = new RealmUtil(mContext);
                        database.addNormalOrder(order);

                        //若為司機身份接單前要下更改營業身份
                        Utility info = new Utility(mContext);
                        JsonPutsUtil sendRequest = new JsonPutsUtil(mContext);
                        sendRequest.driverWorkIdentity(info.getDriverAccountInfo());

                        if (mServerRequestOrderManagerCallBackFunction!=null)
                            mServerRequestOrderManagerCallBackFunction.createNormalOrder(order);

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //取得每一張圖檔的資料
    public void postImageToServer(final DriverIdentifyInfo info, final ImageView imageView, final String imageType) {
        // loading or check internet connection or something...
        // ... then
        // String url = "http://www.angga-ari.com/api/something/awesome";
        final ImageBean imageInfo = new ImageBean();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.SERVER_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    String status = result.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    String message = result.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);
                    App01libObjectKey.APP_POST_UPLOAD_IMAGE_RESPONSE_CODE connectResult = App01libObjectKey.conversion_upload_image_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_SUCCESS)) {
                        Log.e(TAG, "get file info!!!!");
                        //ArrayList<ImageBean> imagelist = info.getImageList();
                        String id = result.getString(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_FILE_ID);
                        String url = result.getString(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_FILE_URL);
                        imageInfo.setUser_id(info.getId());
                        imageInfo.setUser_name(info.getName());
                        imageInfo.setUploadtype(imageType);
                        imageInfo.setFile_id(id);
                        imageInfo.setFile_url(url);
                        RealmUtil data = new RealmUtil(mContext);
                        data.addImageFileInfo(imageInfo);
                        //imagelist.add(imageInfo);
                        //info.setImageList(imagelist);

                    } else {
                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {

                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                        String message = response.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_IMAGE);
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_USERNAME, info.getName());
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_ACCESSKEY, info.getAccesskey());
                // params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_IMAGE_DATA, image);
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_TYPE, imageType);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("images", new DataPart("ic_camera_72x72.jpg", AppHelper.getFileDataFromDrawable(mContext, imageView.getDrawable()), "image/jpeg"));
                // params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(multipartRequest);
    }

    //取得每一張圖檔的資料
    public void postImageToServer2(final DriverIdentifyInfo info, final Bitmap bitmap, final String imageType) {
        // loading or check internet connection or something...
        // ... then
        // String url = "http://www.angga-ari.com/api/something/awesome";
        final ImageBean imageInfo = new ImageBean();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.SERVER_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    String status = result.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    String message = result.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);
                    App01libObjectKey.APP_POST_UPLOAD_IMAGE_RESPONSE_CODE connectResult = App01libObjectKey.conversion_upload_image_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_POST_UPLOAD_IMAGE_RESPONSE_CODE.K_APP_POST_UPLOAD_IMAGE_SUCCESS)) {
                        Log.e(TAG, "get file info!!!!");
                        //ArrayList<ImageBean> imagelist = info.getImageList();
                        String id = result.getString(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_FILE_ID);
                        String url = result.getString(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_FILE_URL);
                        imageInfo.setUser_id(info.getId());
                        imageInfo.setUser_name(info.getName());
                        imageInfo.setUploadtype(imageType);
                        imageInfo.setFile_id(id);
                        imageInfo.setFile_url(url);
                        RealmUtil data = new RealmUtil(mContext);
                        data.addImageFileInfo(imageInfo);
                        //imagelist.add(imageInfo);
                        //info.setImageList(imagelist);

                    } else {
                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {

                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                        String message = response.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_IMAGE);
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_USERNAME, info.getName());
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_ACCESSKEY, info.getAccesskey());
                // params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_IMAGE_DATA, image);
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_TYPE, imageType);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("images", new DataPart("sunshine.jpg", AppHelper.getFileDataFromPicture(mContext, bitmap), "image/jpeg"));
                // params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(multipartRequest);
    }

    //callback fucntion
    private ClientRegisterDataManagerCallBackFunction mClientRegisterDataManagerCallBackFunction;

    public void setClientRegisterDataManagerCallBackFunction(ClientRegisterDataManagerCallBackFunction clientRegisterDataManagerCallBackFunction) {
        mClientRegisterDataManagerCallBackFunction = clientRegisterDataManagerCallBackFunction;

    }

    public interface ClientRegisterDataManagerCallBackFunction {
        public void registerClient(AccountInfo accountInfo);

    }

    //callback fucntion
    private ClientSmsVerifyDataManagerCallBackFunction mClientSmsVerifyDataManagerCallBackFunction;

    public void setmClientSmsVerifyDataManagerCallBackFunctionn(ClientSmsVerifyDataManagerCallBackFunction clientSmsVerifyDataManagerCallBackFunction) {
        mClientSmsVerifyDataManagerCallBackFunction = clientSmsVerifyDataManagerCallBackFunction;

    }

    public interface ClientSmsVerifyDataManagerCallBackFunction {
        public void verifyClient(AccountInfo accountInfo);
        public void reSendSMS(AccountInfo accountInfo);

    }


    //callback fucntion
    private ClientLoginDataManagerCallBackFunction mClientLoginDataManagerCallBackFunction;

    public void setClientLoginDataManagerCallBackFunction(ClientLoginDataManagerCallBackFunction clientLoginDataManagerCallBackFunction) {
        mClientLoginDataManagerCallBackFunction = clientLoginDataManagerCallBackFunction;

    }

    public interface ClientLoginDataManagerCallBackFunction {
        public void loginClient(AccountInfo accountInfo);
        public void loginDriver(DriverIdentifyInfo driver);

    }


    //callback fucntion
    private ServerRequestDataManagerCallBackFunction mServerRequestDataManagerCallBackFunction;

    public void setServerRequestDataManagerCallBackFunction(ServerRequestDataManagerCallBackFunction serverRequestDataManagerCallBackFunction) {
        mServerRequestDataManagerCallBackFunction = serverRequestDataManagerCallBackFunction;

    }

    public interface ServerRequestDataManagerCallBackFunction {
        public void registerDriver(DriverIdentifyInfo driverIdentifyInfo);

    }

    //callback fucntion
    private ServerRequestOrderManagerCallBackFunction mServerRequestOrderManagerCallBackFunction;

    public void setServerRequestOrderManagerCallBackFunction(ServerRequestOrderManagerCallBackFunction serverRequestOrderManagerCallBackFunction) {
        mServerRequestOrderManagerCallBackFunction = serverRequestOrderManagerCallBackFunction;

    }

    public interface ServerRequestOrderManagerCallBackFunction {
        public void createNormalOrder(NormalOrder order);


    }

    //callback fucntion
    private DriverChangeWorkIdentityManagerCallBackFunction mDriverChangeWorkIdentityManagerCallBackFunction;

    public void setDriverChangeWorkIdentityManagerCallBackFunction(DriverChangeWorkIdentityManagerCallBackFunction driverChangeWorkIdentityManagerCallBackFunction) {
        mDriverChangeWorkIdentityManagerCallBackFunction = driverChangeWorkIdentityManagerCallBackFunction;

    }

    public interface DriverChangeWorkIdentityManagerCallBackFunction {
        public void driverChangeWorkIdentity(DriverIdentifyInfo driver);


    }
    //callback fucntion
    private DriverRequestTakeOverOrderManagerCallBackFunction mDriverRequestTakeOverOrderManagerCallBackFunction;

    public void setDriverRequestTakeOverOrderManagerCallBackFunction(DriverRequestTakeOverOrderManagerCallBackFunction driverRequestTakeOverOrderManagerCallBackFunction) {
        mDriverRequestTakeOverOrderManagerCallBackFunction = driverRequestTakeOverOrderManagerCallBackFunction;

    }

    public interface DriverRequestTakeOverOrderManagerCallBackFunction {
        public void driverTakeOverOrder(NormalOrder order);


    }

    //callback fucntion
    private DriverRequestFinishOrderManagerCallBackFunction mDriverRequestFinishOrderManagerCallBackFunction;

    public void setDriverRequestFinishOrderManagerCallBackFunction(DriverRequestFinishOrderManagerCallBackFunction driverRequestFinishOrderManagerCallBackFunction) {
        mDriverRequestFinishOrderManagerCallBackFunction = driverRequestFinishOrderManagerCallBackFunction;

    }

    public interface DriverRequestFinishOrderManagerCallBackFunction {
        public void driverFinishOrder(NormalOrder order);


    }

    //callback fucntion
    private DriverRequestClientCommentManagerCallBackFunction mDriverRequestClientCommentManagerCallBackFunction;

    public void setDriverRequestClientCommentManagerCallBackFunction(DriverRequestClientCommentManagerCallBackFunction driverRequestClientCommentManagerCallBackFunction) {
        mDriverRequestClientCommentManagerCallBackFunction = driverRequestClientCommentManagerCallBackFunction;

    }

    public interface DriverRequestClientCommentManagerCallBackFunction {
        public void driverAskComment(NormalOrder order);


    }

    private void sendNotification(String title,String body) {

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_look)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound);

        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
    }
}