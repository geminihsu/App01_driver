package tw.com.geminihsu.app01Client.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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
import com.newrelic.agent.android.Agent;
import com.newrelic.agent.android.NewRelic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.RealmResults;
import tw.com.geminihsu.app01Client.BuildConfig;
import tw.com.geminihsu.app01Client.DriverCommentActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.AccountTreeInfo;
import tw.com.geminihsu.app01Client.bean.App01libObjectKey;
import tw.com.geminihsu.app01Client.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01Client.bean.ImageBean;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.bean.OrderLocationBean;
import tw.com.geminihsu.app01Client.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01Client.serverbean.ServerBookmark;
import tw.com.geminihsu.app01Client.serverbean.ServerCarbrand;
import tw.com.geminihsu.app01Client.serverbean.ServerContents;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.serverbean.ServerCountys;
import tw.com.geminihsu.app01Client.serverbean.ServerDriverType;
import tw.com.geminihsu.app01Client.serverbean.ServerImageType;
import tw.com.geminihsu.app01Client.serverbean.ServerSpecial;
import tw.com.geminihsu.app01Client.uploadImage.AppHelper;
import tw.com.geminihsu.app01Client.uploadImage.VolleyMultipartRequest;
import tw.com.geminihsu.app01Client.uploadImage.VolleySingleton;

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
        //setCrossProcessHeader();

    }

    public void sendRegisterRequest(final AccountInfo user)
    {
        String versionName = BuildConfig.VERSION_NAME;
        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


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
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
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
                        if (mClientRegisterDataManagerCallBackFunction!=null)
                            mClientRegisterDataManagerCallBackFunction.registerClientError(true,status);

                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendReSendPasswordRequest(final AccountInfo accountInfo)
    {
       // final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
       // requestQueue.add(jsonObjectRequest);
    // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendModifyPasswordRequest(final AccountInfo accountInfo,String new_password)
    {
        // final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_MODIFY_PASSWORD);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_MODIFY_PASSWORD_USERNAME, accountInfo.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_MODIFY_PASSWORD_OLD_PASSWORD, accountInfo.getPassword());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_MODIFY_PASSWORD_NEW_PASSWORD, new_password);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_MODIFY_PASSWORD_ACCESSKEY, accountInfo.getAccessKey());

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
                    App01libObjectKey.APP_ACCOUNT_CHANE_PASSWORD_RESPONSE_CODE connectResult =App01libObjectKey.conversion_account_change_password_result(Integer.valueOf(status));

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
                        if (mAccountChangePasswordDataManagerCallBackFunction !=null)
                            mAccountChangePasswordDataManagerCallBackFunction.modifyPassword(true);

                        //sendLoginRequest(accountInfo);
                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();
                        if (mAccountChangePasswordDataManagerCallBackFunction !=null)
                            mAccountChangePasswordDataManagerCallBackFunction.modifyPassword(false);

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        // requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //會員-忘記密碼-驗證&修改密碼
    public void sendForgetModify(final AccountInfo accountInfo, String verifiyCode, final String newPassword)
    {
        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


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
                            sendLoginRequest(user,false);
                        }else{
                            //更新資料庫裡面的密碼欄位
                            AccountInfo new_user = new AccountInfo();
                            new_user.setId(user.getId());
                            new_user.setUid(user.getUid());
                            new_user.setName(user.getName());
                            new_user.setPhoneNumber(user.getPhoneNumber());
                            new_user.setIdentify(user.getIdentify());
                            new_user.setDriver_type(user.getDriver_type());
                            new_user.setPassword(newPassword);
                            new_user.setConfirm_password(newPassword);
                            new_user.setRecommend_id(user.getRecommend_id());
                            new_user.setRole(user.getRole());
                            new_user.setAccessKey(user.getAccessKey());
                            //user.setPassword(newPassword);
                            realmUtil.updateAccount(new_user);
                            sendLoginRequest(new_user,true);
                        }


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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendVerify(String code, final AccountInfo user)
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


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

                        //if (mClientSmsVerifyDataManagerCallBackFunction !=null)
                         //  mClientSmsVerifyDataManagerCallBackFunction.verifyClient(user);

                        sendLoginRequest(user,true);


                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);
                if(volleyError!=null)
                    if(volleyError.getMessage()!=null)
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
       // requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendLoginRequest(final AccountInfo user, final boolean isRegister)
    {
        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                    App01libObjectKey.APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_login_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_SUCCESS))
                    {
                        String accesskey = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY);
                        RealmUtil realmUtil = new RealmUtil(mContext);
                        AccountInfo userInfo = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,user.getPhoneNumber());
                        if(userInfo!=null)
                        {
                            //更新資料庫裡面的密碼欄位
                            AccountInfo new_user = new AccountInfo();
                            new_user.setId(userInfo.getId());
                            new_user.setUid(userInfo.getUid());
                            new_user.setName(userInfo.getName());
                            new_user.setPhoneNumber(userInfo.getPhoneNumber());
                            new_user.setIdentify(userInfo.getIdentify());
                            new_user.setPassword(userInfo.getPassword());
                            new_user.setConfirm_password(userInfo.getConfirm_password());
                            new_user.setRecommend_id(userInfo.getRecommend_id());
                            new_user.setDriver_type(userInfo.getDriver_type());
                            new_user.setRole(userInfo.getRole());
                            new_user.setAccessKey(accesskey);
                            //user.setPassword(newPassword);
                            realmUtil.updateAccount(new_user);
                            if (mClientSmsVerifyDataManagerCallBackFunction !=null)
                                mClientSmsVerifyDataManagerCallBackFunction.verifyClient(new_user);

                        }else
                        {
                            user.setAccessKey(accesskey);
                            if(isRegister) {
                                realmUtil.addAccount(user);

                                if (mClientSmsVerifyDataManagerCallBackFunction != null)
                                    mClientSmsVerifyDataManagerCallBackFunction.verifyClient(user);
                            }else
                                getUserInfo(user,false);
                        }


                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);
                        if (mClientLoginDataManagerCallBackFunction != null)
                            mClientLoginDataManagerCallBackFunction.loginError(true);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
       // AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void getUserInfo(final AccountInfo user, final boolean isCheckInfo) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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

                        NewRelic.noticeHttpTransaction(Constants.SERVER_URL, "Puts", Integer.valueOf(status), System.nanoTime(), System.nanoTime(),100 ,100, "getUserInfo");

                        if(!datas.equals("null")){
                            //JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            int id = Integer.valueOf(data.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID));
                            String did = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DID);
                            String level = data.getString(App01libObjectKey.APP_OBJECT_KEY_USER_LEVEL);
                            String realname = data.getString(App01libObjectKey.APP_OBJECT_KEY_USER_REALNAME);
                            String client_ticket = data.optString(App01libObjectKey.APP_OBJECT_KEY_USER_CLIENT_TICKETS);
                            String driver_ticket = data.optString(App01libObjectKey.APP_OBJECT_KEY_USER_DRIVER_TICKETS);
                            ArrayList<DriverIdentifyInfo> driverIdentifyInfos = new ArrayList<DriverIdentifyInfo>();

                            //if(user.getRole()==2) {
                            String driver_enable=user.getDriver_type();
                            String driver="";
                            if(data.optString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER)!=null)
                                driver = data.optString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER);

                                if (!driver.equals("")) {
                                    //表示有司機的資料
                                    //JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER);
                                    JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER);

                                    for (int i = 0; i < info.length(); i++) {
                                        JSONObject object = info.getJSONObject(i);
                                        String _did = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DID);
                                        String _dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE);
                                        String _dtype_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE_CHT);
                                        String _enable = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_ENABLE);

                                        //表示客戶已經審核司機通過
                                        if(_enable.equals("100")) {
                                            if (driver_enable!=null) {
                                                if (driver_enable.equals(""))
                                                    user.setDriver_type(_dtype);
                                            }
                                        }

                                        String _enable_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_ENABLE_CHT);
                                        String car_brand = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BRAND);
                                        String car_number = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_NUMBER);
                                        String car_born = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BORN);
                                        String car_reg = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_REGISTER);
                                        String car_cc = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_CC);
                                        String car_special = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_SPECIAL);
                                        String car_files = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_FILE_IMAGE);
                                        String car_imgs = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_IMAGE);

                                        DriverIdentifyInfo driverIdentifyInfo = new DriverIdentifyInfo();
                                        driverIdentifyInfo.setDid(_did);
                                        driverIdentifyInfo.setAccesskey(user.getAccessKey());
                                        driverIdentifyInfo.setUid("" + id);
                                        driverIdentifyInfo.setDtype(_dtype);
                                        driverIdentifyInfo.setCar_born(car_born);
                                        driverIdentifyInfo.setCar_imgs(car_imgs);
                                        driverIdentifyInfo.setCar_cc(car_cc);
                                        driverIdentifyInfo.setCar_brand(car_brand);
                                        driverIdentifyInfo.setCar_reg(car_reg);
                                        driverIdentifyInfo.setCar_special(car_special);
                                        driverIdentifyInfo.setCar_number(car_number);
                                        driverIdentifyInfo.setCar_files(car_files);
                                        driverIdentifyInfo.setEnable(_enable);
                                        driverIdentifyInfo.setName(user.getPhoneNumber());
                                        driverIdentifyInfo.setAccesskey(user.getAccessKey());
                                        driverIdentifyInfo.setId(id);
                                        driverIdentifyInfos.add(driverIdentifyInfo);
                                    }
                                }

                            //}
                            JSONObject tree = data.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_USER_TREE);

                            int tree_lv = tree.getInt(App01libObjectKey.APP_OBJECT_KEY_USER_TREE_LV);
                            int tree_last_watering = tree.getInt(App01libObjectKey.APP_OBJECT_KEY_USER_TREE_LAST_WATERING);
                            int tree_next = tree.getInt(App01libObjectKey.APP_OBJECT_KEY_USER_TREE_NEXT);
                            int tree_status = tree.getInt(App01libObjectKey.APP_OBJECT_KEY_USER_TREE_STATUS);


                            user.setUid(did);
                            user.setId(id);
                            user.setConfirm_password(user.getPassword());
                            user.setLevel(level);
                            user.setName(realname);
                            user.setDriver_ticket_id(driver_ticket);
                            user.setClient_ticket_id(client_ticket);
                            if(!driverIdentifyInfos.isEmpty()&&user.getDriver_type()!=null)
                                user.setRole(2);

                            AccountTreeInfo treeInfo = new AccountTreeInfo();
                            treeInfo.setUser_id(user.getId());
                            treeInfo.setUser_uid(""+user.getId());
                            treeInfo.setUser_did(did);
                            treeInfo.setAccesskey(user.getAccessKey());
                            treeInfo.setUser_name(user.getName());
                            treeInfo.setLv(tree_lv);
                            treeInfo.setLast_watering(tree_last_watering);
                            treeInfo.setNext(tree_next);
                            treeInfo.setStatus(tree_status);

                            RealmUtil database = new RealmUtil(mContext);
                            database.clearDB(AccountTreeInfo.class);
                            database.addUserTreeInfo(treeInfo);

                            if(!isCheckInfo) {
                                //RealmUtil database = new RealmUtil(mContext);
                                database.clearDB(AccountInfo.class);
                                database.clearDB(DriverIdentifyInfo.class);
                                for(DriverIdentifyInfo driverIdentifyInfo : driverIdentifyInfos)
                                {
                                    database.addDriverInfo(driverIdentifyInfo);
                                }
                                database.addAccount(user);
                                //設定檔顯示登入的帳號密碼
                                SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

                                configSharedPreferences.edit().putString(mContext.getString(R.string.config_login_phone_number_key), user.getPhoneNumber()).commit();
                                configSharedPreferences.edit().putString(mContext.getString(R.string.config_login_password_key), user.getPassword()).commit();

                            /*if(Integer.valueOf(did)>0)
                            {
                                //表示為司機身份
                                //需要下命令取得司機資料再存到資料庫
                                getDriverInfo(user);

                            }else */
                                {
                                    if (mClientSmsVerifyDataManagerCallBackFunction != null)
                                        mClientSmsVerifyDataManagerCallBackFunction.modifyPassword(user);


                                    if (mClientLoginDataManagerCallBackFunction != null)
                                        mClientLoginDataManagerCallBackFunction.loginClient(user);
                                }

                            }else
                            {

                                    //將查詢到的司機身份資訊回傳
                                    if (mClientLoginDataManagerCallBackFunction != null)
                                        mClientLoginDataManagerCallBackFunction.findDriverInfo(user,driverIdentifyInfos);
                            }
                        }
                        Log.e(TAG, "get push!!!!");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();
                        if (mClientLoginDataManagerCallBackFunction != null)
                            mClientLoginDataManagerCallBackFunction.loginError(true);

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError!=null) {
                    if(volleyError.getMessage()!=null)
                    Log.e(TAG, volleyError.getMessage().toString());
                    if (mClientLoginDataManagerCallBackFunction != null)
                        mClientLoginDataManagerCallBackFunction.loginError(true);

                }
            }
        });
        // Adding request to request queue
       // VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//NewRelic.noticeHttpTransaction(url, httpMethod, statusCode, startTimeMs, endTimeMs, bytesSent, bytesReceived, responseBody, params, response);
//Where xNewRelicAppDataHeader is the value of the X-NewRelic-App-Data header
           }


    public void getDriverInfo(final NormalOrder user, int driverUid, final double lat, final double lng) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_GET_DRIVER_INFORMATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UID, driverUid);

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
                            String uid = data.getString(App01libObjectKey.APP_OBJECT_KEY_UID);
                            String username = data.getString(App01libObjectKey.APP_OBJECT_KEY_REGISTER_USERNAME);
                            String realname = data.getString(App01libObjectKey.APP_OBJECT_KEY_USER_REALNAME);
                            String dtype = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TYPE);
                            String carbrand = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BRAND);
                            String carnumber = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_NUMBER);
                            String carborn = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_BORN);
                            String carcc = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_CC);
                            String carimage = data.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_CAR_IMAGE);



                            DriverIdentifyInfo driver = new DriverIdentifyInfo();
                            driver.setUid(uid);
                            driver.setName(username);
                            driver.setRealname(realname);
                            driver.setDid(did);
                            driver.setDtype(dtype);
                            driver.setCar_brand(carbrand);
                            driver.setCar_number(carnumber);
                            driver.setCar_born(carborn);
                            driver.setCar_cc(carcc);
                            driver.setCar_imgs(carimage);


                            if (mAccountQueryUserLocationManagerCallBackFunction != null)
                                    mAccountQueryUserLocationManagerCallBackFunction.getLocationInfo(driver,lng, lat);


                           /* Utility info = new Utility(mContext);
                            RealmUtil database = new RealmUtil(mContext);
                            if(info.getDriverAccountInfo()==null)
                               database.addDriverInfo(driver);
                            //設定檔顯示登入的帳號密碼
                            SharedPreferences  configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);


                            if (mClientLoginDataManagerCallBackFunction !=null)
                                mClientLoginDataManagerCallBackFunction.loginDriver(driver);
*/


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
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void getPushNotification(final AccountInfo user) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                Log.e(TAG,"[getPushNotification]:"+ jsonObject.toString());

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

                                String ticketStatus = messageInfo.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_TYPE);

                                String content="";
                                String driver_uid="";
                                if(ticketStatus.equals("ticket_order")) {
                                    content = "已有司機接單";
                                    driver_uid =object.getString(App01libObjectKey.APP_OBJECT_KEY_UID);
                                    Log.e(TAG,"driver_uid:"+driver_uid);
                                }
                                else  if(ticketStatus.equals("ticket_finish"))
                                    content = "已到目的地，感謝您的搭乘";

                                RealmUtil data = new RealmUtil(mContext);
                                NormalOrder order = data.queryOrder(Constants.ORDER_TICKET_ID,ticketOrder);
                               if(order!=null){
                                   if(order.getTicket_id().equals(ticketOrder))
                                   {
                                       Log.e(TAG, "已有司機接單");
                                       if(ticketStatus.equals("ticket_order"))
                                           sendNotification("一般搭乘ticket no:"+ticketOrder,content);
                                       else  if(ticketStatus.equals("ticket_finish"))
                                           sendFinishNotification("一般搭乘ticket no:"+ticketOrder,content,ticketOrder);

                                       clearPushNotification(user,Integer.valueOf(id));

                                       if (mClientOrderHasBeenTakenOVerManagerCallBackFunction!=null) {
                                           if(ticketStatus.equals("ticket_order"))
                                           mClientOrderHasBeenTakenOVerManagerCallBackFunction.getOrderTakenSuccess(ticketOrder, message,driver_uid);
                                           else  if(ticketStatus.equals("ticket_finish"))
                                           mClientOrderHasBeenTakenOVerManagerCallBackFunction.getOrderFinishSuccess(ticketOrder, message);
                                       }

                                   }
                               }

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

                    } else if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_EXPIRED)||connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_ACCOUNT_VERIFY_ERROR))
                    {
                      //發現驗證失敗，所以重新登入
                        //sendLoginRequest(user,false);

                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);
                        Utility info = new Utility(mContext);
                        info.clearData(AccountInfo.class);
                        info.clearData(AccountTreeInfo.class);

                        if (mClientOrderHasBeenTakenOVerManagerCallBackFunction!=null) {
                            mClientOrderHasBeenTakenOVerManagerCallBackFunction.accountExpired(false,message);
                        }


                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError!=null)
                    if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
       // requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
       // AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void clearPushNotification(AccountInfo user,int pid) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }


    public void putsUserGPSLocation(double longitude, double latitude, AccountInfo user) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError!=null)
                    if(volleyError.getMessage()!=null)
                      Log.e(TAG, volleyError.getMessage().toString());
            }
        });
       // requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void getUserLocation(final NormalOrder user, final int driverUid) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_LOCATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_GET_USER_GPS_USERNAME, user.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_GET_USER_ACCESSKEY ,user.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_GET_USER_UID, driverUid-1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[getUserLocation]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_ACCOUNT_PUTS_USER_GPS_CODE connectResult = App01libObjectKey.conversion_get_put_user_location_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_ACCOUNT_PUTS_USER_GPS_CODE.K_APP_ACCOUNT_PUTS_USER_GPS_CODE_SUCCESS)) {
                        Log.e(TAG, "get GPS success!!!!");
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);

                        if(!datas.equals("null")) {
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String uid = data.getString(App01libObjectKey.APP_OBJECT_KEY_UID);

                            if (uid.equals(user.getUser_uid())) {
                                Double lat = data.getDouble(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION_LAT);
                                Double lng = data.getDouble(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION_LNG);
                                /*if (mAccountQueryUserLocationManagerCallBackFunction != null)
                                    mAccountQueryUserLocationManagerCallBackFunction.getLocationInfo(lng, lat);*/
                                getDriverInfo(user,driverUid,lat,lng);

                            }


                        }
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError!=null)
                    Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //查詢客戶訂單
    public void queryClientOrderList(AccountInfo user) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //查詢司機訂單
    public void queryDriverOrderList(final DriverIdentifyInfo user) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                NewRelic.noticeHttpTransaction(Constants.SERVER_URL, "Puts", 200, System.nanoTime(), System.nanoTime(),100 ,100, "Recommendation List");

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_DRIVER_QUERY_ORDER_LIST connectResult = App01libObjectKey.conversion_driver_query_order_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        String datas = jsonObject.optString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                        ArrayList<String> ticketList = new ArrayList<String>();
                        RealmUtil database = new RealmUtil(mContext);
                        RealmResults<ServerBookmark> location =database.queryServerBookmark();
                        if(datas.equals("null"))
                        {
                            if (mDriverRecommendationOrderListManagerCallBackFunction !=null)
                                mDriverRecommendationOrderListManagerCallBackFunction.getOrderListFail(true,status);
                        }else{
                                JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                                String queryId="";
                                ArrayList<NormalOrder> orderList = new ArrayList<NormalOrder>();
                                for (int i = 0; i < info.length(); i++){
                                    JSONObject object = info.getJSONObject(i);
                                    String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                                    String ticket_status = object.getString(App01libObjectKey.APP_OBJECT_KEY_USER_TREE_STATUS);
                                    if(ticket_status.equals("1"))
                                      ticketList.add(id);

                                /*NormalOrder order = new NormalOrder();
                                //取得訂單詳細資料
                                String ticket_id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                                String uid = object.getString(App01libObjectKey.APP_OBJECT_KEY_CLIENT_UID);
                                String dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE);
                                String cargo_type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE);
                                String price = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE);
                                String tip = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP);

                                order.setTicket_id(ticket_id);
                                order.setUser_uid(uid);
                                order.setDtype(dtype);
                                order.setUser_name(user.getPhoneNumber());
                                order.setUser_id(user.getId());
                                order.setCargo_type(cargo_type);
                                order.setAccesskey(user.getAccessKey());
                                order.setPrice(price);
                                order.setTip(tip);
                                order.setTicket_status("0");

                                ServerBookmark bookmark= location.get(i%2);
                                //以下欄位做假資料
                                order.setTarget("1");
                                OrderLocationBean begin = new OrderLocationBean();
                                begin.setZipcode("100");
                                begin.setLatitude(bookmark.getLat());
                                begin.setLongitude(bookmark.getLng());
                                begin.setAddress(bookmark.getLocation());
                                order.setBegin(begin);
                                order.setBegin_address(bookmark.getStreetAddress());

                                ServerBookmark bookmark1= location.get(i%3);
                                OrderLocationBean end = new OrderLocationBean();
                                end.setZipcode("320");
                                end.setLatitude(bookmark1.getLat());
                                end.setLongitude(bookmark1.getLat());
                                end.setAddress(bookmark1.getLocation());
                                order.setEnd(end);
                                order.setEnd_address(bookmark1.getStreetAddress());
                                Date dateIfo=new Date();
                                order.setOrderdate(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));
                                orderList.add(order);*/
                                }
                                //if (mDriverRecommendationOrderListManagerCallBackFunction !=null)
                                //  mDriverRecommendationOrderListManagerCallBackFunction.getOrderListSuccess(orderList);


                                Log.e(TAG,"ticketList size:"+ticketList.size());
                                //查詢訂單明細
                                if(ticketList.size()>0)
                                    getRecommendationTicketInfo(user,ticketList);
                                else {
                                    if (mDriverRecommendationOrderListManagerCallBackFunction != null)
                                        mDriverRecommendationOrderListManagerCallBackFunction.getOrderListFail(true,status);
                                }


                        }

                } else if (connectResult.equals(App01libObjectKey.APP_DRIVER_QUERY_ORDER_LIST.K_APP_DRIVER_QUERY_ORDER_EXPIRED))
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);
                        Utility info = new Utility(mContext);
                        info.clearData(AccountInfo.class);
                        info.clearData(AccountTreeInfo.class);

                        if (mClientOrderHasBeenTakenOVerManagerCallBackFunction!=null) {
                            mClientOrderHasBeenTakenOVerManagerCallBackFunction.accountExpired(false,message);
                        }


                    }else {
                        if (mDriverRecommendationOrderListManagerCallBackFunction !=null)
                            mDriverRecommendationOrderListManagerCallBackFunction.getOrderListFail(true,status);
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }
    // 訂單-自動配對可以接的訂單
    public void queryRecommendOrderList(final AccountInfo user) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_RECOMMEND_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
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
                    NewRelic.noticeHttpTransaction(Constants.SERVER_URL, "Puts", Integer.valueOf(status), System.nanoTime(), System.nanoTime(),100 ,100, "queryRecommendOrderList");

                    App01libObjectKey.APP_DRIVER_RECOMMEND_ORDER connectResult = App01libObjectKey.conversion_get_put_driver_recommend_order_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        String datas = jsonObject.optString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                        ArrayList<String> ticketList = new ArrayList<String>();
                        RealmUtil database = new RealmUtil(mContext);
                        RealmResults<ServerBookmark> location =database.queryServerBookmark();
                        if(datas.equals("null"))
                        {
                            if (mDriverRecommendationOrderListManagerCallBackFunction !=null)
                                mDriverRecommendationOrderListManagerCallBackFunction.getOrderListFail(true,status);
                        }else{
                            JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String queryId="";
                            ArrayList<NormalOrder> orderList = new ArrayList<NormalOrder>();
                            for (int i = 0; i < info.length(); i++){
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                                ticketList.add(id);

                                /*NormalOrder order = new NormalOrder();
                                //取得訂單詳細資料
                                String ticket_id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                                String uid = object.getString(App01libObjectKey.APP_OBJECT_KEY_CLIENT_UID);
                                String dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE);
                                String cargo_type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE);
                                String price = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE);
                                String tip = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP);

                                order.setTicket_id(ticket_id);
                                order.setUser_uid(uid);
                                order.setDtype(dtype);
                                order.setUser_name(user.getPhoneNumber());
                                order.setUser_id(user.getId());
                                order.setCargo_type(cargo_type);
                                order.setAccesskey(user.getAccessKey());
                                order.setPrice(price);
                                order.setTip(tip);
                                order.setTicket_status("0");

                                ServerBookmark bookmark= location.get(i%2);
                                //以下欄位做假資料
                                order.setTarget("1");
                                OrderLocationBean begin = new OrderLocationBean();
                                begin.setZipcode("100");
                                begin.setLatitude(bookmark.getLat());
                                begin.setLongitude(bookmark.getLng());
                                begin.setAddress(bookmark.getLocation());
                                order.setBegin(begin);
                                order.setBegin_address(bookmark.getStreetAddress());

                                ServerBookmark bookmark1= location.get(i%3);
                                OrderLocationBean end = new OrderLocationBean();
                                end.setZipcode("320");
                                end.setLatitude(bookmark1.getLat());
                                end.setLongitude(bookmark1.getLat());
                                end.setAddress(bookmark1.getLocation());
                                order.setEnd(end);
                                order.setEnd_address(bookmark1.getStreetAddress());
                                Date dateIfo=new Date();
                                order.setOrderdate(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));
                                orderList.add(order);*/
                            }
                            //if (mDriverRecommendationOrderListManagerCallBackFunction !=null)
                              //  mDriverRecommendationOrderListManagerCallBackFunction.getOrderListSuccess(orderList);


                            Log.e(TAG,"ticketList size:"+ticketList.size());
                            //查詢訂單明細
                            getRecommendationTicketInfo(user,ticketList);
                        }
                    } else {
                        if (mDriverRecommendationOrderListManagerCallBackFunction !=null)
                            mDriverRecommendationOrderListManagerCallBackFunction.getOrderListFail(true,status);

                        //String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        //Toast.makeText(mContext,
                        //        message,
                          //      Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    // 訂單-取消訂單
    public void clientCancelOrder(final NormalOrder order,String driverPhoneNumber) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_CUSTOMER_CANCEL_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, driverPhoneNumber);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_TICKET_ID, Integer.valueOf(order.getTicket_id()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[clientCancelOrder]:"+ jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    NewRelic.noticeHttpTransaction(Constants.SERVER_URL, "Puts", Integer.valueOf(status), System.nanoTime(), System.nanoTime(),100 ,100, "queryRecommendOrderList");

                    App01libObjectKey.APP_DRIVER_RECOMMEND_ORDER connectResult = App01libObjectKey.conversion_get_put_driver_recommend_order_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_DRIVER_RECOMMEND_ORDER.K_APP_DRIVER_RECOMMEND_ORDER_SUCCESS)) {

                        if(mServerRequestOrderManagerCallBackFunction!=null)
                            mServerRequestOrderManagerCallBackFunction.cancelNormalOrder(order);
                    } else {

                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                              Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //查詢訂單明細
    public void queryOrderInformation(final DriverIdentifyInfo user, final String orderId) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                        Log.e(TAG, "[queryOrderInformation]: success!!");
                        String datas = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                        if(datas!=null)
                        {
                            JSONObject object = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_TICKET_ID);

                            if(id.equals(orderId))
                            {
                                NormalOrder order = new NormalOrder();
                                //取得訂單詳細資料
                                String uid = object.getString(App01libObjectKey.APP_OBJECT_KEY_CLIENT_UID);
                                String did = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER_DID);
                                String dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE);
                                String dtype_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE_CHT);
                                String cargo_type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE);
                                String cargo_size = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SIZE);
                                String price = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE);
                                String tip = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP);
                                String order_status = object.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                                order.setUser_uid(uid);
                                order.setUser_did(did);
                                order.setDtype(dtype);
                                order.setUser_name(user.getName());
                                order.setUser_id(user.getId());
                                order.setCargo_type(cargo_type);
                                order.setCargo_size(cargo_size);
                                order.setAccesskey(user.getAccesskey());
                                order.setPrice(price);
                                order.setTip(tip);
                                order.setTicket_status(order_status);

                                //以下欄位做假資料
                                order.setTarget("0");
                                OrderLocationBean begin = new OrderLocationBean();
                                begin.setZipcode("100");
                                begin.setLatitude("25.0477");
                                begin.setLongitude("121.518");
                                begin.setAddress("100台灣台北市中正區北平西路3號");
                                order.setBegin(begin);
                                order.setBegin_address("100台灣台北市中正區北平西路3號");

                                OrderLocationBean end = new OrderLocationBean();
                                end.setZipcode("320");
                                end.setLatitude("25.0135");
                                end.setLongitude("121.215");
                                end.setAddress("320台灣桃園市中壢區高鐵北路一段6號");
                                order.setEnd(end);
                                order.setEnd_address("320台灣桃園市中壢區高鐵北路一段6號");

                                RealmUtil database = new RealmUtil(mContext);
                                database.addNormalOrder(order);
                            }

                        }
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //查詢訂單明細
    public void queryOrderInformation(final AccountInfo user, final String orderId, final int ticketLen) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_QUERY_ORDER_DETAIL);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
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
                                NormalOrder order = new NormalOrder();
                                //取得訂單詳細資料
                                String uid = object.getString(App01libObjectKey.APP_OBJECT_KEY_CLIENT_UID);
                                String did = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER_DID);
                                String dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE);
                                String dtype_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_DTPYE_CHT);

                                //取得出發點資訊
                                JSONObject begin = object.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_BEG);
                                String beg_zipcode="";
                                String beg_address="";
                                String beg_latlng="";
                                String beg_gps[];
                                String beg_lat="";
                                String beg_lng="";
                                if(begin!=null) {
                                    beg_zipcode = begin.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                    //beg_address = begin.getString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);
                                    beg_latlng = begin.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);
                                    if(!beg_latlng.equals("")) {
                                        beg_gps = beg_latlng.split(",");
                                        beg_lat = beg_gps[0];
                                        beg_lng = beg_gps[1];
                                    }

                                    beg_address = begin.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);

                                }


                                OrderLocationBean beginInfo = new OrderLocationBean();
                                beginInfo.setId(0);
                                String zipCode="";
                                Log.e(TAG,beg_zipcode);
                                if(!beg_zipcode.equals(""))
                                 zipCode=beg_zipcode.substring(0,3);
                                beginInfo.setZipcode(zipCode);
                                beginInfo.setLongitude(beg_lng);
                                beginInfo.setLatitude(beg_lat);
                                beginInfo.setAddress(beg_address);
                                order.setBegin(beginInfo);
                                order.setBegin_address(beg_address);

                                //取得暫停點資訊

                                JSONArray info = object.optJSONArray(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_STOP);

                                String stop_latlng = "";
                                String stop_lat="";
                                String stop_lng="";
                                String stop_gps[];
                                String stop_zipcode="";
                                String stop_address="";

                                if(info!=null) {
                                    for (int i = 0; i < info.length(); i++) {
                                        JSONObject stopObject = info.getJSONObject(i);
                                        stop_latlng = stopObject.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);


                                        if (!stop_latlng.equals("")) {
                                            stop_gps = stop_latlng.split(",");
                                            stop_lat = stop_gps[0];
                                            stop_lng = stop_gps[1];
                                        }

                                        stop_zipcode = stopObject.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                        stop_address = stopObject.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);


                                        OrderLocationBean stopInfo = new OrderLocationBean();
                                        stopInfo.setZipcode(stop_zipcode);
                                        stopInfo.setLongitude(stop_lng);
                                        stopInfo.setLatitude(stop_lat);
                                        stopInfo.setAddress(stop_address);

                                        order.setStop(stopInfo);
                                        order.setStop_address(stop_address);


                                    }
                                }

                                //取得目的地資訊
                                JSONObject end = object.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_END);

                                String end_zipcode="";
                                String end_address="";
                                String end_latlng="";
                                String end_gps[];
                                String end_lat="";
                                String end_lng="";
                                if(end!=null) {
                                    end_zipcode = end.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                    //end_address = end.getString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);

                                    end_latlng = end.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);
                                    if(!end_latlng.equals("")) {
                                        end_gps = end_latlng.split(",");
                                        end_lat = end_gps[0];
                                        end_lng = end_gps[1];
                                    }
                                    end_address = end.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);

                                }

                                OrderLocationBean endInfo = new OrderLocationBean();
                                endInfo.setId(2);
                                String zipcode="";
                                if(!end_zipcode.equals(""))
                                zipcode =end_zipcode.substring(0,3);
                                endInfo.setZipcode(zipcode);
                                endInfo.setLongitude(end_lng);
                                endInfo.setLatitude(end_lat);
                                endInfo.setAddress(end_address);
                                order.setEnd(endInfo);
                                order.setEnd_address(end_address);


                                String cargo_type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE);
                                String cargo_size = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SIZE);
                                String cargo_imgs = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_IMAGES);
                                String cargo_spec = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SPECIAL);


                                String price = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE);
                                String tip = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP);
                                String remark = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_REMARK);
                                String timebegin = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIMEBEGIN);
                                String order_status = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_STATUS);
                                String order_status_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_STATUS_CHT);


                                //取得訂單客戶資訊
                                JSONObject client = object.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_ORDER_CLIENT);
                                String userPhoneNumber="";
                                String userRealName="";
                                if(begin!=null) {
                                    userPhoneNumber = client.getString(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_GPS_USERNAME);
                                    userRealName = client.getString(App01libObjectKey.APP_OBJECT_KEY_REGISTER_REALNAME);
                                }


                                order.setTicket_id(id);
                                order.setUser_uid(uid);
                                order.setDtype(dtype);
                                order.setUser_name(userPhoneNumber);
                                order.setUser_id(Integer.valueOf(uid));
                                order.setCargo_imgs(cargo_imgs);
                                order.setCar_special(cargo_spec);
                                order.setTimebegin(timebegin);
                                order.setRemark(remark);
                                order.setTicket_status(order_status);
                                order.setTarget("1");
                                order.setCargo_size(cargo_size);
                                order.setUser_did(did);
                                order.setCargo_type(cargo_type);
                                order.setAccesskey(user.getAccessKey());
                                order.setPrice(price);
                                order.setTip(tip);
                                order.setTicket_status("0");

                                Date dateIfo=new Date();
                                order.setOrderdate(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));


                                RealmUtil database = new RealmUtil(mContext);
                                if(database.queryOrder(Constants.ORDER_TICKET_ID,order.getTicket_id())==null)
                                {
                                    database.addNormalOrder(order);
                                    Log.e(TAG,"add order to database");
                                }

                                //if(order.getTicket_id().equals(lastTicket_no)) {
                                    Utility orders = new Utility(mContext);
                                    //orders.clearData(NormalOrder.class);
                                    RealmResults<NormalOrder> data=orders.getRecommendationOrderList();
                                    System.out.println("call back: data.size():"+data.size());
                                    if (data.size() == ticketLen) {
                                        if (mDriverRecommendationOrderListManagerCallBackFunction != null)
                                            mDriverRecommendationOrderListManagerCallBackFunction.getWaitOrderListSuccess(data);

                                    }
                               // }
                                /*ServerBookmark bookmark= location.get(i%2);
                                //以下欄位做假資料
                                order.setTarget("1");
                                OrderLocationBean begin = new OrderLocationBean();
                                begin.setZipcode("100");
                                begin.setLatitude(bookmark.getLat());
                                begin.setLongitude(bookmark.getLng());
                                begin.setAddress(bookmark.getLocation());
                                order.setBegin(begin);
                                order.setBegin_address(bookmark.getStreetAddress());

                                ServerBookmark bookmark1= location.get(i%3);
                                OrderLocationBean end = new OrderLocationBean();
                                end.setZipcode("320");
                                end.setLatitude(bookmark1.getLat());
                                end.setLongitude(bookmark1.getLat());
                                end.setAddress(bookmark1.getLocation());
                                order.setEnd(end);
                                order.setEnd_address(bookmark1.getStreetAddress());
                                Date dateIfo=new Date();
                                order.setOrderdate(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));
                                orderList.add(order)*/
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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
       // requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);


    }

    //查詢訂單明細
    public void queryOrderInformation(final DriverIdentifyInfo user, final String orderId, final int ticketLen) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                                NormalOrder order = new NormalOrder();
                                //取得訂單詳細資料
                                String uid = object.getString(App01libObjectKey.APP_OBJECT_KEY_CLIENT_UID);
                                String did = object.getString(App01libObjectKey.APP_OBJECT_KEY_DRIVER_DRIVER_DID);
                                String dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE);
                                String dtype_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_DTPYE_CHT);

                                //取得出發點資訊
                                JSONObject begin = object.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_BEG);
                                String beg_zipcode="";
                                String beg_address="";
                                String beg_latlng="";
                                String beg_gps[];
                                String beg_lat="";
                                String beg_lng="";
                                if(begin!=null) {
                                    beg_zipcode = begin.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                    beg_address = begin.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);
                                    beg_latlng = begin.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);
                                    if(beg_latlng!=null) {
                                        beg_gps = beg_latlng.split(",");
                                        if(beg_gps.length>1) {
                                            beg_lat = beg_gps[0];
                                            beg_lng = beg_gps[1];
                                        }
                                    }

                                }


                                OrderLocationBean beginInfo = new OrderLocationBean();
                                beginInfo.setId(0);
                                //String zipCode=beg_zipcode.substring(0,3);
                                beginInfo.setZipcode(beg_zipcode);
                                beginInfo.setLongitude(beg_lng);
                                beginInfo.setLatitude(beg_lat);
                                beginInfo.setAddress(beg_address);
                                order.setBegin(beginInfo);
                                order.setBegin_address(beg_address);

                                //取得暫停點資訊

                                JSONArray info = object.optJSONArray(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_STOP);


                                String stop_latlng ="";
                                String stop_lat = "";
                                String stop_lng = "";
                                String stop_gps[];
                                String stop_zipcode="";
                                String stop_address="";

                                if(info!=null) {
                                    for (int i = 0; i < info.length(); i++) {
                                        JSONObject stopObject = info.getJSONObject(i);
                                        stop_latlng = stopObject.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);


                                        if (!stop_latlng.equals("")) {
                                            stop_gps = stop_latlng.split(",");
                                            if (stop_gps.length > 1) {
                                                stop_lat = stop_gps[0];
                                                stop_lng = stop_gps[1];
                                            }
                                        }

                                        stop_zipcode = stopObject.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                        stop_address = stopObject.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);

                                        OrderLocationBean stopInfo = new OrderLocationBean();
                                        stopInfo.setId(0);
                                        stopInfo.setZipcode(stop_zipcode);
                                        stopInfo.setLongitude(stop_lng);
                                        stopInfo.setLatitude(stop_lat);
                                        stopInfo.setAddress(stop_address);
                                        order.setStop(stopInfo);
                                        order.setStop_address(stop_address);

                                    }
                                }

                                    //取得暫停點資訊
                                /*JSONObject stop = jsonObject.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_STOP);
                                String stop_zipcode = stop.getString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                String stop_address = stop.getString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);

                                String stop_latlng = stop.getString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);
                                String stop_gps[] = stop_latlng.split(",");
                                String stop_lat = stop_gps[0];
                                String stop_lng = stop_gps[1];

                                OrderLocationBean stopInfo = new OrderLocationBean();
                                stopInfo.setId(0);
                                stopInfo.setZipcode(stop_zipcode);
                                stopInfo.setLongitude(stop_lng);
                                stopInfo.setLatitude(stop_lat);
                                stopInfo.setAddress(stop_address);
                                order.setStop(stopInfo);
                                order.setStop_address(stop_address);

                                */

                                //取得目的地資訊
                                JSONObject end = object.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_QUICK_TAXI_ORDER_END);

                                String end_zipcode="";
                                String end_address="";
                                String end_latlng="";
                                String end_gps[];
                                String end_lat="";
                                String end_lng="";
                                if(end!=null) {
                                    end_zipcode = end.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ZIPCODE);
                                    end_address = end.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_ADDRESS);

                                    end_latlng = end.optString(App01libObjectKey.APP_OBJECT_KEY_QUERY_ORDER_LATLNG);
                                    if(end_latlng!=null) {
                                        end_gps = end_latlng.split(",");
                                        if(end_gps.length>1) {
                                            end_lat = end_gps[0];
                                            end_lng = end_gps[1];
                                        }
                                    }
                                }

                                OrderLocationBean endInfo = new OrderLocationBean();
                                endInfo.setId(2);
                                //String zipcode =end_zipcode.substring(0,3);
                                endInfo.setZipcode(end_zipcode);
                                endInfo.setLongitude(end_lng);
                                endInfo.setLatitude(end_lat);
                                endInfo.setAddress(end_address);
                                order.setEnd(endInfo);
                                order.setEnd_address(end_address);


                                String cargo_type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE);
                                String cargo_size = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SIZE);
                                String cargo_imgs = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_IMAGES);
                                String cargo_spec = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SPECIAL);


                                String price = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE);
                                String tip = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP);
                                String remark = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_REMARK);
                                String timebegin = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIMEBEGIN);
                                String order_status = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_STATUS);
                                String order_status_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ORDER_STATUS_CHT);


                                //取得訂單客戶資訊
                                JSONObject client = object.optJSONObject(App01libObjectKey.APP_OBJECT_KEY_ORDER_CLIENT);
                                String userPhoneNumber="";
                                String userRealName="";
                                if(client!=null) {
                                    userPhoneNumber = client.getString(App01libObjectKey.APP_OBJECT_KEY_UPLOAD_GPS_USERNAME);
                                    userRealName = client.getString(App01libObjectKey.APP_OBJECT_KEY_REGISTER_REALNAME);
                                    order.setUser_name(userPhoneNumber);
                                }


                                order.setTicket_id(id);
                                order.setUser_uid(uid);
                                order.setDtype(dtype);
                                order.setUser_name(userPhoneNumber);
                                order.setUser_id(Integer.valueOf(uid));
                                order.setCargo_imgs(cargo_imgs);
                                order.setCar_special(cargo_spec);
                                order.setTimebegin(timebegin);
                                order.setRemark(remark);
                                order.setTicket_status(order_status);
                                order.setTarget("1");
                                order.setCargo_size(cargo_size);
                                order.setUser_did(did);
                                order.setCargo_type(cargo_type);
                                order.setAccesskey(user.getAccesskey());
                                order.setPrice(price);
                                order.setTip(tip);
                                order.setTicket_status("0");

                                Date dateIfo=new Date();
                                order.setOrderdate(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));


                                RealmUtil database = new RealmUtil(mContext);
                                if(database.queryOrder(Constants.ORDER_TICKET_ID,order.getTicket_id())==null)
                                    database.addNormalOrder(order);

                               // if(max) {
                                    Utility orders = new Utility(mContext);
                                    //orders.clearData(NormalOrder.class);
                                    //RealmResults<NormalOrder> data=orders.getAccountOrderListByPhoneNumber(userPhoneNumber);
                                    RealmResults<NormalOrder> data=orders.getRecommendationOrderList();

                                    if(data.size() == ticketLen) {
                                        if (mDriverRecommendationOrderListManagerCallBackFunction != null)
                                            mDriverRecommendationOrderListManagerCallBackFunction.getOrderListSuccess(data);
                                    }
                              //  }
                                /*ServerBookmark bookmark= location.get(i%2);
                                //以下欄位做假資料
                                order.setTarget("1");
                                OrderLocationBean begin = new OrderLocationBean();
                                begin.setZipcode("100");
                                begin.setLatitude(bookmark.getLat());
                                begin.setLongitude(bookmark.getLng());
                                begin.setAddress(bookmark.getLocation());
                                order.setBegin(begin);
                                order.setBegin_address(bookmark.getStreetAddress());

                                ServerBookmark bookmark1= location.get(i%3);
                                OrderLocationBean end = new OrderLocationBean();
                                end.setZipcode("320");
                                end.setLatitude(bookmark1.getLat());
                                end.setLongitude(bookmark1.getLat());
                                end.setAddress(bookmark1.getLocation());
                                order.setEnd(end);
                                order.setEnd_address(bookmark1.getStreetAddress());
                                Date dateIfo=new Date();
                                order.setOrderdate(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));
                                orderList.add(order)*/
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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG, volleyError.getMessage().toString());
            }
        });
       // requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);


    }
    //推訊息給訂單建立者
    public void pushNotificationToOrderOwner(NormalOrder order,String content) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //訂單-快速接單(司機權限)
    public void driverTakeOverOrder(final NormalOrder order,String driverPhone) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_TAKE_OVER_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, driverPhone);
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
                        RealmUtil database = new RealmUtil(mContext);
                        NormalOrder new_order= new NormalOrder();
                        NormalOrder old_order= database.queryOrder(Constants.ORDER_TICKET_ID,order.getTicket_id());
                        new_order.setUser_id(old_order.getUser_id());
                        new_order.setUser_name(old_order.getUser_name());
                        new_order.setUser_did(old_order.getUser_did());
                        new_order.setAccesskey(old_order.getAccesskey());
                        new_order.setRemark(old_order.getRemark());
                        new_order.setEnd(old_order.getEnd());
                        new_order.setDtype(old_order.getDtype());
                        new_order.setOrderdate(old_order.getOrderdate());
                        new_order.setTicket_status("1");
                        new_order.setBegin(old_order.getBegin());
                        new_order.setBegin_address(old_order.getBegin_address());
                        new_order.setCar_special(old_order.getCar_special());
                        new_order.setCargo_imgs(old_order.getCargo_imgs());
                        new_order.setCargo_size(old_order.getCargo_size());
                        new_order.setStop_address(old_order.getStop_address());
                        new_order.setCargo_type(old_order.getCargo_type());
                        new_order.setEnd_address(old_order.getEnd_address());
                        new_order.setOrder_id(old_order.getOrder_id());
                        new_order.setTarget(old_order.getTarget());
                        new_order.setTimebegin(old_order.getTimebegin());
                        new_order.setPrice(old_order.getPrice());
                        new_order.setTicket_id(old_order.getTicket_id());
                        new_order.setTip(old_order.getTip());
                        new_order.setUser_uid(old_order.getUser_uid());
                        new_order.setStop(old_order.getStop());

                        database.updateOrder(new_order);

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
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //會員-更改營運中的司機身分
    public void driverWorkIdentity(final DriverIdentifyInfo driverIdentifyInfo) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                        if(mDriverChangeWorkIdentityManagerCallBackFunction!=null)
                            mDriverChangeWorkIdentityManagerCallBackFunction.changeIdentityError(true);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);
                    if(mDriverChangeWorkIdentityManagerCallBackFunction!=null)
                        mDriverChangeWorkIdentityManagerCallBackFunction.changeIdentityError(true);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);
                if(mDriverChangeWorkIdentityManagerCallBackFunction!=null)
                    mDriverChangeWorkIdentityManagerCallBackFunction.changeIdentityError(true);

                if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //訂單-結案訂單(司機權限)
    public void driverFinishOrder(final NormalOrder order,String driverPhoneNumber) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_DRIVER_FINISH_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, driverPhoneNumber);
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
                     NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //訂單-給訂單對方評價星星數
    public void commentOrder(final NormalOrder order,final AccountInfo accountInfo, int star) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_COMMENT_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, accountInfo.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, accountInfo.getAccessKey());
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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //會員-取得-使用者評價
    public void getAccountComment(final AccountInfo accountInfo) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_ACCOUNT_COMMENT);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, accountInfo.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, accountInfo.getAccessKey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_UID, Integer.valueOf(accountInfo.getId()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "[getAccountComment]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_COMMENT_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_account_comment_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                        if (mAccountQueryCommentManagerCallBackFunction!=null)
                            mAccountQueryCommentManagerCallBackFunction.getCommentList("23");

                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }
    //申請註冊司機
    public void registerDriverAccount(final DriverIdentifyInfo driverIdentifyInfo) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                        AccountInfo userInfo = database.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, driverIdentifyInfo.getName());
                        if(userInfo!=null)
                        {
                            //更新資料庫裡面的密碼欄位
                            AccountInfo new_user = new AccountInfo();
                            new_user.setId(userInfo.getId());
                            new_user.setUid(userInfo.getUid());
                            new_user.setName(userInfo.getName());
                            new_user.setPhoneNumber(userInfo.getPhoneNumber());
                            new_user.setIdentify(userInfo.getIdentify());
                            new_user.setDriver_type(userInfo.getDriver_type());
                            new_user.setPassword(userInfo.getPassword());
                            new_user.setConfirm_password(userInfo.getConfirm_password());
                            new_user.setRecommend_id(userInfo.getRecommend_id());
                            new_user.setRole(Constants.Identify.BOTH.ordinal());
                            new_user.setAccessKey(userInfo.getAccessKey());
                            //user.setPassword(newPassword);
                            database.updateAccount(new_user);
                        };

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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //client create order call taxi
    public void putCreateQuickTaxiOrder(final NormalOrder order) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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

        String beginZipCode="0";
        if(order.getBegin().getZipcode()!=null)
            beginZipCode = order.getBegin().getZipcode();

        try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.parseDouble(order.getBegin().getLatitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.parseDouble(order.getBegin().getLongitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(beginZipCode));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getBegin().getAddress());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject stop = new JSONObject();
        /*try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, 24.09133);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, 120.540315);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, 404);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, "台中市北區市政路172號");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        /*String stopZipCode="0";
        if(order.getStop().getZipcode()!=null)
            stopZipCode = order.getStop().getZipcode();

        try {
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.parseDouble(order.getStop().getLatitude()));
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.parseDouble(order.getStop().getLongitude()));
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(stopZipCode));
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getStop_address());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

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
        /*String endZipCode="0";
        if(order.getEnd().getZipcode()!=null)
            endZipCode = order.getEnd().getZipcode();

        try {
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT,Double.parseDouble(order.getEnd().getLatitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.parseDouble(order.getEnd().getLongitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(endZipCode));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getEnd().getAddress());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        //JSONArray jsonArrayBegin = new JSONArray();

        //jsonArrayBegin.put(begin);

        JSONArray jsonArrayStop = new JSONArray();

        jsonArrayStop.put(stop);


        //JSONArray jsonArrayEnd = new JSONArray();

        //jsonArrayEnd.put(end);

        JSONObject obj = new JSONObject();

        try {
           /* obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_CREATE_NORMAL_ORDER);
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
            */
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_CREATE_QUICK_TAXI_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE, Integer.valueOf(order.getDtype()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_BEG, begin);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_STOP, jsonArrayStop);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_END, end);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE, Integer.valueOf(order.getCargo_type()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SIZE, 0);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_IMAGES, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SPECIAL, order.getCar_special());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIME_BEGIN, Long.parseLong(order.getTimebegin()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_REMARK, order.getRemark());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_PRICE, Integer.valueOf(order.getPrice()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIP, Integer.valueOf(order.getTip()));


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
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    //client create normal order
    public void putCreateNormalOrder(final NormalOrder order) {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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

        String beginZipCode="0";
        if(order.getBegin().getZipcode()!=null)
            beginZipCode = order.getBegin().getZipcode();

        try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.parseDouble(order.getBegin().getLatitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.parseDouble(order.getBegin().getLongitude()));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(beginZipCode));
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getBegin().getAddress());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject stop = new JSONObject();
        /*try {
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, 24.09133);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, 120.540315);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, 404);
            begin.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, "台中市北區市政路172號");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        String stopZipCode="0";
        if(order.getStop().getZipcode()!=null)
             stopZipCode = order.getStop().getZipcode();

        try {
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT, Double.parseDouble(order.getStop().getLatitude()));
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.parseDouble(order.getStop().getLongitude()));
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(stopZipCode));
            stop.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getStop_address());

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
        String endZipCode="0";
        if(order.getEnd().getZipcode()!=null)
            endZipCode = order.getEnd().getZipcode();

        try {
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LAT,Double.parseDouble(order.getEnd().getLatitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_LNG, Double.parseDouble(order.getEnd().getLongitude()));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ZIPCODE, Integer.valueOf(endZipCode));
            end.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ADDRESS, order.getEnd().getAddress());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //JSONArray jsonArrayBegin = new JSONArray();

        //jsonArrayBegin.put(begin);

        JSONArray jsonArrayStop = new JSONArray();

        jsonArrayStop.put(stop);


        //JSONArray jsonArrayEnd = new JSONArray();

        //jsonArrayEnd.put(end);

        JSONObject obj = new JSONObject();

        try {
           /* obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_CREATE_NORMAL_ORDER);
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
            */
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_CREATE_NORMAL_ORDER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_USERNAME, order.getUser_name());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_ACCESSKEY, order.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TYPE, Integer.valueOf(order.getDtype()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_BEG, begin);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_STOP, jsonArrayStop);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_END, end);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_TYPE, Integer.valueOf(order.getCargo_type()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SIZE, 0);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_IMAGES, "");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_CARGO_SPECIAL, order.getCar_special());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_TIME_BEGIN, Long.parseLong(order.getTimebegin()));
            obj.put(App01libObjectKey.APP_OBJECT_KEY_ORDER_REMARK, order.getRemark());
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
                        /*order.setTicket_id(ticket_id);
                        RealmUtil database = new RealmUtil(mContext);
                        database.addNormalOrder(order);

                        //若為司機身份接單前要下更改營業身份
                        Utility info = new Utility(mContext);
                        JsonPutsUtil sendRequest = new JsonPutsUtil(mContext);
                        sendRequest.driverWorkIdentity(info.getDriverAccountInfo());

                        if (mServerRequestOrderManagerCallBackFunction!=null)
                            mServerRequestOrderManagerCallBackFunction.createNormalOrder(order);*/

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
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

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
                    //NewRelic.noticeHttpTransaction(Constants.SERVER_URL, "Post", response.statusCode, response.networkTimeMs, response.networkTimeMs, Long.valueOf(response.headers.get("X-Android-Sent-Millis")),Long.valueOf(response.headers.get("X-Android-Received-Millis"))  , "upload image");

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
                        if (mDriverRegisterUploadPhotoManagerCallBackFunction!=null)
                            mDriverRegisterUploadPhotoManagerCallBackFunction.uploadStatusSuccess(imageInfo);

                        //imagelist.add(imageInfo);
                        //info.setImageList(imagelist);

                    } else {
                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

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
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),error);

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
                params.put("images", new DataPart("ic_camera_72x72.png", AppHelper.getFileDataFromPicture(mContext, bitmap), "image/jpeg"));
                // params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(multipartRequest);
    }

    public void sendRequestServerContentDetail()
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_CONTENT_DETAIL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[sendGetServerContentDetail]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS))
                    {
                        //取得新得資料前先把舊得資料表清空
                        Utility dataTable = new Utility(mContext);
                        dataTable.clearServerInfoData();

                        String contents = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS);
                        if(!contents.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String code = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CODE);
                                String title = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_TITLE);
                                String content = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CONTENT);
                                ServerContents site = new ServerContents();
                                site.setCode(code);
                                site.setTitle(title);
                                site.setContent(content);
                                database.addServerContents(site);

                            }
                        }

                        String countys = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_COUNTYS);
                        if(!countys.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_COUNTYS);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_COUNTYS_ID);
                                String name = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_COUNTYS_NAME);

                                ServerCountys site = new ServerCountys();
                                site.setId(id);
                                site.setName(name);
                                database.addServerCountys(site);

                            }
                        }

                        String dtype = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE);
                        if(!dtype.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE);
                                String name = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DTYPE_DRIVER_TYPE_CHT);

                                ServerDriverType site = new ServerDriverType();
                                site.setDtype(type);
                                site.setDtype_cht(name);
                                database.addServerDriverType(site);

                            }
                        }

                        String utype = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_UTYPE);
                        if(!utype.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_UTYPE);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DRIVER_IMAGE_UTYPE);
                                String name = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DRIVER_IMAGE_UTYPE_NAME);

                                ServerImageType site = new ServerImageType();
                                site.setUtype(type);
                                site.setUtype_cht(name);
                                database.addServerImageType(site);

                            }
                        }

                        String carbrand = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CARBRAND);
                        if(!carbrand.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CARBRAND);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String type = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CARBRAND_ID);
                                String name = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CARBRAND_NAME);

                                ServerCarbrand site = new ServerCarbrand();
                                site.setId(type);
                                site.setName(name);
                                database.addServerCarBrand(site);

                            }
                        }

                        String dspecial = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL);
                        if(!dspecial.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");

                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_ID);
                                String s_dtype = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_DTYPE);
                                String s_dtype_cht = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_DTPYE_CHT);
                                String content = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_DSPECIAL_CONTENT);


                                ServerSpecial site = new ServerSpecial();
                                site.setId(id);
                                site.setDtype(s_dtype);
                                site.setDtype_cht(s_dtype_cht);
                                site.setContent(content);
                                database.addServerSpecial(site);

                            }
                        }
                        String bookmark = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_BOOKMARK);
                        if(!bookmark.equals("null")){
                            JSONObject data = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_BOOKMARK);
                            RealmUtil database = new RealmUtil(mContext);
                            JSONArray info = data.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION);
                            //JSONObject data = info.getJSONObject("data");
                            ArrayList<ServerBookmark> bookmarks = new ArrayList<ServerBookmark>();
                            for (int i = 0; i < info.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION_ID);
                                String location = object.getString(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION_LOCATION);
                                String lat = object.getString(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION_LAT);
                                String lng = object.getString(App01libObjectKey.APP_OBJECT_KEY_BOOKMARK_LOCATION_LNG);



                                ServerBookmark site = new ServerBookmark();
                                site.setId(id);
                                site.setLocation(location);
                                site.setLat(lat);
                                site.setLng(lng);
                                //database.addServerBookMark(site);
                                bookmarks.add(site);
                            }
                            getStreetBookMarkAddress(bookmarks,database);
                        }

                            }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendRequestServerNewsList()
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_NEW_LIST);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[sendGetServerContentDetail]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS))
                    {
                        String contents = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                        if(!contents.equals("null")){
                            JSONArray info = jsonObject.getJSONArray(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_INFO_MESSAGE);
                            //JSONObject data = info.getJSONObject("data");
                            HashMap<String,String> list = new HashMap<String,String>();
                            for (int i = 0; i < info.length(); i++) {
                                JSONObject object = info.getJSONObject(i);
                                String id = object.getString(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID);
                                String title = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_TITLE);
                                list.put(id,title);

                            }
                            if (mServerQueryNewsListManagerCallBackFunction!=null)
                                mServerQueryNewsListManagerCallBackFunction.getNewList(list);

                        }
                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendRequestServerNewsListContent(int newId)
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_NEW_LIST_CONTENT);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_NOTIFICATION_ID, newId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[sendGetServerContentDetail]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS))
                    {
                        String contents = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CONTENT);
                        if(!contents.equals("null")){
                            JSONObject object = jsonObject.getJSONObject(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CONTENT);

                            String content = object.getString(App01libObjectKey.APP_OBJECT_KEY_ALL_SERVER_CONTENTS_CONTENT);

                             if (mServerQueryNewsListManagerCallBackFunction!=null)
                                 mServerQueryNewsListManagerCallBackFunction.getNewContent(content);

                        }
                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendCustomerSuggestion(AccountInfo user,String suggestion)
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_CUSTOMER_SUGGESTION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_SEND_CUSTOMER_FEEDBACK_NAME, user.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_SEND_CUSTOMER_FEEDBACK_CONTACT, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_SEND_CUSTOMER_FEEDBACK_CONTENT, suggestion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[sendGetServerContentDetail]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS))
                    {
                        if (mCustomerReturnFeedBackManagerCallBackFunction!=null)
                            mCustomerReturnFeedBackManagerCallBackFunction.sendStatus(true);

                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();
                        if (mCustomerReturnFeedBackManagerCallBackFunction!=null)
                            mCustomerReturnFeedBackManagerCallBackFunction.sendStatus(false);

                    }

                } catch (JSONException e) {
                    NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }
    //new method for appending the crossProcessID necessary for CAT in New Relic
    /*public static void setCrossProcessHeader(HttpURLConnection conn) {
        String crossProcessId = Agent.getCrossProcessId(); // API call into the agent for the X-NewRelic-ID
        if (crossProcessId != null) {
            conn.setRequestProperty("X-NewRelic-ID", crossProcessId);
        }
    }*/

    public void sendCustomerTreeWatering(AccountInfo info)
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_CLIENT_TREE_WATERING);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_USERNAME, info.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_ACCESSKEY, info.getAccessKey());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[sendGetServerContentDetail]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_CUSTOMER_ADD_TREE_WATERING connectResult =App01libObjectKey.conversion_customer_add_watering_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_CUSTOMER_ADD_TREE_WATERING.K_APP_CUSTOMER_ADD_TREE_WATERING_SUCCESS))
                    {
                        if (mCustomerAddWateringCallBackFunction!=null)
                            mCustomerAddWateringCallBackFunction.sendStatus(true);

                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();
                        if (mCustomerAddWateringCallBackFunction!=null)
                            mCustomerAddWateringCallBackFunction.sendStatus(false);

                    }

                } catch (JSONException e) {
                    //NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                //NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    public void sendCustomerGainBound(AccountInfo info)
    {

        //final RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_CLIENT_GAIN_BOUND);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_USERNAME, info.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DRIVER_ACCESSKEY, info.getAccessKey());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,"[sendGetServerContentDetail]:"+jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);

                    App01libObjectKey.APP_CUSTOMER_TAKE_BOUNS connectResult =App01libObjectKey.conversion_customer_take_bouns_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_CUSTOMER_TAKE_BOUNS.K_APP_CUSTOMER_TAKE_BOUNS_SUCCESS))
                    {
                        if (mCustomerAddWateringCallBackFunction!=null)
                            mCustomerAddWateringCallBackFunction.sendStatus(true);

                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();
                        if (mCustomerAddWateringCallBackFunction!=null)
                            mCustomerAddWateringCallBackFunction.sendStatus(false);

                    }

                } catch (JSONException e) {
                    //NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Exception e =  new Exception();
                e.setStackTrace(volleyError.getStackTrace());
                //NewRelic.noticeNetworkFailure(Constants.SERVER_URL, "Puts",System.nanoTime(), System.nanoTime(),e);

                if(volleyError.getMessage()!=null)
                    Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        //requestQueue.add(jsonObjectRequest);
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }


    //callback fucntion
    private ClientRegisterDataManagerCallBackFunction mClientRegisterDataManagerCallBackFunction;

    public void setClientRegisterDataManagerCallBackFunction(ClientRegisterDataManagerCallBackFunction clientRegisterDataManagerCallBackFunction) {
        mClientRegisterDataManagerCallBackFunction = clientRegisterDataManagerCallBackFunction;

    }

    public interface ClientRegisterDataManagerCallBackFunction {
        public void registerClient(AccountInfo accountInfo);
        public void registerClientError(Boolean isError,String message);

    }

    //callback fucntion
    private ClientSmsVerifyDataManagerCallBackFunction mClientSmsVerifyDataManagerCallBackFunction;

    public void setmClientSmsVerifyDataManagerCallBackFunctionn(ClientSmsVerifyDataManagerCallBackFunction clientSmsVerifyDataManagerCallBackFunction) {
        mClientSmsVerifyDataManagerCallBackFunction = clientSmsVerifyDataManagerCallBackFunction;

    }

    public interface ClientSmsVerifyDataManagerCallBackFunction {
        public void verifyClient(AccountInfo accountInfo);
        public void reSendSMS(AccountInfo accountInfo);
        public void modifyPassword(AccountInfo accountInfo);

    }

    //callback fucntion
    private AccountChangePasswordDataManagerCallBackFunction mAccountChangePasswordDataManagerCallBackFunction;

    public void setAccountChangePasswordDataManagerCallBackFunction(AccountChangePasswordDataManagerCallBackFunction accountChangePasswordDataManagerCallBackFunction) {
        mAccountChangePasswordDataManagerCallBackFunction = accountChangePasswordDataManagerCallBackFunction;

    }

    public interface AccountChangePasswordDataManagerCallBackFunction {
        public void modifyPassword(boolean status);

    }

    //callback fucntion
    private ClientLoginDataManagerCallBackFunction mClientLoginDataManagerCallBackFunction;

    public void setClientLoginDataManagerCallBackFunction(ClientLoginDataManagerCallBackFunction clientLoginDataManagerCallBackFunction) {
        mClientLoginDataManagerCallBackFunction = clientLoginDataManagerCallBackFunction;

    }

    public interface ClientLoginDataManagerCallBackFunction {
        public void loginClient(AccountInfo accountInfo);
        public void loginDriver(DriverIdentifyInfo driver);
        public void findDriverInfo(AccountInfo accountInfo,ArrayList<DriverIdentifyInfo> driver);
        public void loginError(boolean error);

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
        public void cancelNormalOrder(NormalOrder order);

    }

    //callback fucntion
    private DriverChangeWorkIdentityManagerCallBackFunction mDriverChangeWorkIdentityManagerCallBackFunction;

    public void setDriverChangeWorkIdentityManagerCallBackFunction(DriverChangeWorkIdentityManagerCallBackFunction driverChangeWorkIdentityManagerCallBackFunction) {
        mDriverChangeWorkIdentityManagerCallBackFunction = driverChangeWorkIdentityManagerCallBackFunction;

    }

    public interface DriverChangeWorkIdentityManagerCallBackFunction {
        public void driverChangeWorkIdentity(DriverIdentifyInfo driver);
        public void changeIdentityError(boolean error);

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

    //註冊司機上傳圖片的callback
    private DriverRegisterUploadPhotoManagerCallBackFunction mDriverRegisterUploadPhotoManagerCallBackFunction;

    public void setDriverRegisterUploadPhotoManagerCallBackFunction(DriverRegisterUploadPhotoManagerCallBackFunction driverRegisterUploadPhotoManagerCallBackFunction) {
        mDriverRegisterUploadPhotoManagerCallBackFunction = driverRegisterUploadPhotoManagerCallBackFunction;

    }

    public interface DriverRegisterUploadPhotoManagerCallBackFunction {
        public void uploadStatusSuccess(ImageBean photo);
        public void uploadFail(boolean error);

    }

    //推薦司機訂單callback
    private DriverRecommendationOrderListManagerCallBackFunction mDriverRecommendationOrderListManagerCallBackFunction;

    public void setDriverRecommendationOrderListManagerCallBackFunction(DriverRecommendationOrderListManagerCallBackFunction driverRecommendationOrderListManagerCallBackFunction) {
        mDriverRecommendationOrderListManagerCallBackFunction = driverRecommendationOrderListManagerCallBackFunction;

    }

    public interface DriverRecommendationOrderListManagerCallBackFunction {
        public void getWaitOrderListSuccess(RealmResults<NormalOrder> data);
        public void getOrderListSuccess(RealmResults<NormalOrder> data);
        public void getOrderListFail(boolean error,String message);

    }

    //告知客戶訂單已被接單callback
    private ClientOrderHasBeenTakenOVerManagerCallBackFunction mClientOrderHasBeenTakenOVerManagerCallBackFunction;

    public void setClientOrderHasBeenTakenOVerManagerCallBackFunction(ClientOrderHasBeenTakenOVerManagerCallBackFunction clientOrderHasBeenTakenOVerManagerCallBackFunction) {
        mClientOrderHasBeenTakenOVerManagerCallBackFunction = clientOrderHasBeenTakenOVerManagerCallBackFunction;

    }

    public interface ClientOrderHasBeenTakenOVerManagerCallBackFunction {
        public void getOrderTakenSuccess(String ticket_id,String message,String driverUid);
        public void getOrderFinishSuccess(String ticket_id,String message);
        public void accountExpired(boolean expired, String err);

    }

    //查詢帳號評價
    private AccountQueryCommentManagerCallBackFunction mAccountQueryCommentManagerCallBackFunction;

    public void setAccountQueryCommentManagerCallBackFunction(AccountQueryCommentManagerCallBackFunction accountQueryCommentManagerCallBackFunction) {
        mAccountQueryCommentManagerCallBackFunction = accountQueryCommentManagerCallBackFunction;

    }

    public interface AccountQueryCommentManagerCallBackFunction {
        public void getCommentList(String message);

    }

    //查詢司機位置
    private AccountQueryUserLocationManagerCallBackFunction mAccountQueryUserLocationManagerCallBackFunction;

    public void setAccountQueryUserLocationManagerCallBackFunction(AccountQueryUserLocationManagerCallBackFunction accountQueryUserLocationManagerCallBackFunction) {
        mAccountQueryUserLocationManagerCallBackFunction = accountQueryUserLocationManagerCallBackFunction;

    }

    public interface AccountQueryUserLocationManagerCallBackFunction {
        public void getLocationInfo(DriverIdentifyInfo account,Double lat, Double lng);

    }

    //查詢新聞列表
    private ServerQueryNewsListManagerCallBackFunction mServerQueryNewsListManagerCallBackFunction;

    public void setServerQueryNewsListManagerCallBackFunction(ServerQueryNewsListManagerCallBackFunction serverQueryNewsListManagerCallBackFunction) {
        mServerQueryNewsListManagerCallBackFunction = serverQueryNewsListManagerCallBackFunction;

    }

    public interface ServerQueryNewsListManagerCallBackFunction {
        public void getNewList(HashMap<String,String> list);
        public void getNewContent(String content);

    }

    //回傳使用者建議
    private CustomerReturnFeedBackManagerCallBackFunction mCustomerReturnFeedBackManagerCallBackFunction;

    public void setCustomerReturnFeedBackManagerCallBackFunction(CustomerReturnFeedBackManagerCallBackFunction customerReturnFeedBackManagerCallBackFunction) {
        mCustomerReturnFeedBackManagerCallBackFunction = customerReturnFeedBackManagerCallBackFunction;

    }

    public interface CustomerReturnFeedBackManagerCallBackFunction {
        public void sendStatus(boolean status);

    }

    //使用者澆水
    private CustomerTreeWateringCallBackFunction mCustomerAddWateringCallBackFunction;

    public void setCustomerTreeWateringFeedBackManagerCallBackFunction(CustomerTreeWateringCallBackFunction customerTreeWateringCallBackFunctionCallBackFunction) {
        mCustomerAddWateringCallBackFunction = customerTreeWateringCallBackFunctionCallBackFunction;

    }

    public interface CustomerTreeWateringCallBackFunction {
        public void sendStatus(boolean status);

    }

    private void getStreetBookMarkAddress(ArrayList<ServerBookmark> bookmarks,RealmUtil database)
    {
        List<Address> addresses = null;

        Geocoder gc = new Geocoder(mContext, Locale.getDefault());
        try {
            for(int i= 0 ;i<bookmarks.size();i++) {
                ServerBookmark bookmark = bookmarks.get(i);
                addresses = gc.getFromLocation(Double.parseDouble(bookmarks.get(i).getLat()), Double.parseDouble(bookmarks.get(i).getLng()), 10);
                bookmark.setStreetAddress(addresses.get(0).getAddressLine(0));
                database.addServerBookMark(bookmark);
            }
        } catch (IOException e) {}



    }


    private void getRecommendationTicketInfo(AccountInfo user,ArrayList<String> ticketList) {

        Utility orders = new Utility(mContext);
        orders.clearData(NormalOrder.class);

        Log.e(TAG,"[getRecommendationTicketInfo] ticketList.size():"+ticketList.size());
        for (int i = 0; i < ticketList.size(); i++) {

            String ticket_no = ticketList.get(i);

            queryOrderInformation(user, ticket_no, ticketList.size());
        }
    }

    private void getRecommendationTicketInfo(DriverIdentifyInfo user,ArrayList<String> ticketList) {

        Utility orders = new Utility(mContext);
        orders.clearData(NormalOrder.class);
        for (int i = 0; i < ticketList.size(); i++) {

            String ticket_no = ticketList.get(i);
            queryOrderInformation(user, ticket_no, ticketList.size());
        }
    }

//        if(orderList.size()>0) {
//            if (mDriverRecommendationOrderListManagerCallBackFunction != null)
//                mDriverRecommendationOrderListManagerCallBackFunction.getOrderListSuccess(orderList);
//        }else{
//            if (mDriverRecommendationOrderListManagerCallBackFunction != null)
//                mDriverRecommendationOrderListManagerCallBackFunction.getOrderListFail(true);
//        }

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

    private void sendFinishNotification(String title,String body,String id) {

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(mContext, DriverCommentActivity.class);
        Bundle b = new Bundle();
        b.putString(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET_ID, id);
        intent.putExtras(b);

        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, 0);
        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_look)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_action_search, "前往評分", pIntent)
                .setSound(notificationSound);

        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
    }
}