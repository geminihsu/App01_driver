package tw.com.geminihsu.app01.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
    //Check user location
    private LocationListener listener;
    private LocationManager locationManager;
    private  JsonPutsUtil sendrequest;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class App01ServiceServiceBinder extends Binder {


        public App01Service getService(AccountInfo user)
        {
            accountInfo = user;
            sendrequest = new JsonPutsUtil(App01Service.this);
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

    public void App01ServiceCheckGPS(){

        Log.d(TAG, "[App01ServiceBinder]App01ServiceCheckGPS() executed");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                i.putExtra("longitude",location.getLongitude());
                i.putExtra("latitude",location.getLatitude());
                sendBroadcast(i);
                Log.e(TAG, "Longitude:"+location.getLongitude()+",Latitude:"+location.getLatitude());
                sendrequest.putsUserGPSLocation(location.getLongitude(),location.getLatitude(),accountInfo);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);

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

        sendrequest.getPushNotification(accountInfo);


    }

    public void stopToGetNotifyInfo() {
        if (getPushNotifyInfo_thread_Thread != null)
            getPushNotifyInfo_thread_Thread.stopThisThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
