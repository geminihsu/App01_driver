package tw.com.geminihsu.app01.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.geminihsu.app01.BuildConfig;
import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.App01libObjectKey;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.ConfigSharedPreferencesUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

/**
 * Created by geminihsu on 2016/11/23.
 */

public class App01Service extends Service {
    private final String TAG = this.getClass().getSimpleName();
    private final int GET_DEVICE_INFO_THREAD_SLEEP_TIME_SECOND = 30;

    private App01ServiceServiceBinder mBinder = new App01ServiceServiceBinder();

    private GetPushNotifyInfo_Thread getPushNotifyInfo_thread_Thread;
    private AccountInfo accountInfo;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class App01ServiceServiceBinder extends Binder {


        public App01Service getService(AccountInfo user)
        {
            accountInfo = user;
            Log.d(TAG, "[App01Service]_onBind() executed");
            return App01Service.this;
        }



    }

    public void startToGetPutNotify() {

        Log.d(TAG, "[App01ServiceBinder]startToGetPutNotify() executed");
        if (getPushNotifyInfo_thread_Thread != null)
            getPushNotifyInfo_thread_Thread.stopThisThread();

        getPushNotifyInfo_thread_Thread = new GetPushNotifyInfo_Thread();
        getPushNotifyInfo_thread_Thread.start();

    }

    private class GetPushNotifyInfo_Thread extends Thread {

        private boolean startFlag = true;

        public void stopThisThread() {
            startFlag = false;
        }

        public void run() {


            // boolean isFirst =true;
            while (startFlag) {
                Log.d(TAG, "[NotifyAppService]GetNotification_Thread_run thread, threadId:" + Thread.currentThread().getId());
                sendCommandToRequestNotification();
                try {
                    sleep(GET_DEVICE_INFO_THREAD_SLEEP_TIME_SECOND * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
    }

    private void sendCommandToRequestNotification() {

        /*final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_GET_PUSH_NOTIFICATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
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
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult =App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS))
                    {
                       Log.e(TAG,"get push!!!!");
                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
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
        requestQueue.add(jsonObjectRequest);*/
        JsonPutsUtil sendrequest = new JsonPutsUtil(this);
        sendrequest.getPushNotification(accountInfo);


    }

    public void setUserInfo(AccountInfo user){
        accountInfo = user;
    }

}
