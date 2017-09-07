package tw.com.geminihsu.app01Client.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.ThreadPoolUtil;

/**
 * Created by geminihsu on 2016/11/23.
 */

public class App01Service extends Service {
    private final String TAG = this.getClass().getSimpleName();
    public final static String MY_BROADCAST_ACTION_ACCOUNT_EXPIRED = "tw.com.geminihsu.app01.accountExpiredNotification";
    public static final String MY_BROADCAST_ACTION_ACCOUNT_EXPIRED_RESULT = "expiredFlag";
    public static final String MY_BROADCAST_ACTION_ACCOUNT_EXPIRED_RESULT_MESSAGE = "expiredMessage";

    private final int GET_DEVICE_INFO_THREAD_SLEEP_TIME_SECOND = 10;

    private App01ServiceServiceBinder mBinder = new App01ServiceServiceBinder();

    private GetPushNotifyInfo_Thread getPushNotifyInfo_thread_Thread;
    private GetServerContentDetail_Thread getServerContentDetail_Thread;

    private AccountInfo accountInfo;
    //Check user location
    private LocationListener listener;
    private LocationManager locationManager;
    private JsonPutsUtil sendrequest;



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

    public void requestServerContentDetail() {

        Log.d(TAG, "[App01ServiceBinder]requestServerContentDetail() executed");
        getServerContentDetail_Thread = new GetServerContentDetail_Thread();
        getServerContentDetail_Thread.start();
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

    private class GetServerContentDetail_Thread extends Thread {

        public void run() {


            sendCommandToRequestServerContentDetail();

        }
    }

    private void sendCommandToRequestNotification() {

        sendrequest.getPushNotification(accountInfo);
        sendrequest.setClientOrderHasBeenTakenOVerManagerCallBackFunction(new JsonPutsUtil.ClientOrderHasBeenTakenOVerManagerCallBackFunction() {


            @Override
            public void getOrderTakenSuccess(String ticket_id,String message,String uid) {

                RealmUtil data = new RealmUtil(App01Service.this);
                NormalOrder order = data.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
                NormalOrder new_order = new NormalOrder();
                new_order.setBegin(order.getBegin());
                new_order.setOrderdate(order.getOrderdate());
                new_order.setDtype(order.getDtype());
                new_order.setTicket_id(order.getTicket_id());
                new_order.setTarget(order.getTarget());
                new_order.setEnd_address(order.getEnd_address());
                new_order.setEnd(order.getEnd());
                new_order.setAccesskey(order.getAccesskey());
                new_order.setBegin_address(order.getBegin_address());
                new_order.setCar_special(order.getCar_special());
                new_order.setCargo_imgs(order.getCargo_imgs());
                new_order.setOrder_id(order.getOrder_id());
                new_order.setPrice(order.getPrice());
                new_order.setCargo_size(order.getCargo_size());
                new_order.setCargo_type(order.getCargo_type());
                new_order.setRemark(order.getRemark());
                new_order.setStop(order.getStop());
                new_order.setTimebegin(order.getTimebegin());
                new_order.setTip(order.getTip());
                new_order.setUser_did(uid);
                new_order.setUser_id(order.getUser_id());
                new_order.setUser_name(order.getUser_name());
                new_order.setStop_address(order.getStop_address());
                new_order.setUser_uid(order.getUser_uid());
                new_order.setTicket_status("1");

                data.updateOrder(new_order);

                Intent i = new Intent("wait_order");
                i.putExtra("ticket_id",ticket_id);
                i.putExtra("driver_uid",uid);
                sendBroadcast(i);
                Log.e(TAG, "Notify Server");

            }

            @Override
            public void getOrderFinishSuccess(String ticket_id, String message) {
                RealmUtil data = new RealmUtil(App01Service.this);
                NormalOrder order = data.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
                NormalOrder new_order = new NormalOrder();
                new_order.setBegin(order.getBegin());
                new_order.setOrderdate(order.getOrderdate());
                new_order.setDtype(order.getDtype());
                new_order.setTicket_id(order.getTicket_id());
                new_order.setTarget(order.getTarget());
                new_order.setEnd_address(order.getEnd_address());
                new_order.setEnd(order.getEnd());
                new_order.setAccesskey(order.getAccesskey());
                new_order.setBegin_address(order.getBegin_address());
                new_order.setCar_special(order.getCar_special());
                new_order.setCargo_imgs(order.getCargo_imgs());
                new_order.setOrder_id(order.getOrder_id());
                new_order.setPrice(order.getPrice());
                new_order.setCargo_size(order.getCargo_size());
                new_order.setCargo_type(order.getCargo_type());
                new_order.setRemark(order.getRemark());
                new_order.setStop(order.getStop());
                new_order.setTimebegin(order.getTimebegin());
                new_order.setTip(order.getTip());
                new_order.setUser_did(order.getUser_did());
                new_order.setUser_id(order.getUser_id());
                new_order.setUser_name(order.getUser_name());
                new_order.setStop_address(order.getStop_address());
                new_order.setUser_uid(order.getUser_uid());
                new_order.setTicket_status("2");

                data.updateOrder(new_order);

                Intent i = new Intent("finish_order");
                i.putExtra("ticket_id",ticket_id);
                sendBroadcast(i);
                Log.e(TAG, "Notify Server");
            }

            @Override
            public void accountExpired(boolean expired, String err) {
                sendAccountExpiredNotify(expired,err);

            }
        });

    }

    private void sendCommandToRequestServerContentDetail() {

        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                sendrequest.sendRequestServerContentDetail();

            }
        }));

    }

    public void stopToGetNotifyInfo() {
        if (getPushNotifyInfo_thread_Thread != null)
            getPushNotifyInfo_thread_Thread.stopThisThread();
    }

    // 通知Account過期，帳戶被重新登入不同裝置
    public void sendAccountExpiredNotify(boolean flag,String message) {
        Intent intent = new Intent(MY_BROADCAST_ACTION_ACCOUNT_EXPIRED);
        Bundle bundle = new Bundle();
        bundle.putBoolean(MY_BROADCAST_ACTION_ACCOUNT_EXPIRED_RESULT,flag);
        bundle.putString(MY_BROADCAST_ACTION_ACCOUNT_EXPIRED_RESULT_MESSAGE,message);
        sendBroadcast(intent);
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
