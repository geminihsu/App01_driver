package tw.com.geminihsu.app01Client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import io.realm.RealmResults;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.serverbean.ServerBookmark;
import tw.com.geminihsu.app01Client.serverbean.ServerCarbrand;
import tw.com.geminihsu.app01Client.serverbean.ServerContents;
import tw.com.geminihsu.app01Client.serverbean.ServerCountys;
import tw.com.geminihsu.app01Client.serverbean.ServerDriverType;
import tw.com.geminihsu.app01Client.serverbean.ServerImageType;
import tw.com.geminihsu.app01Client.serverbean.ServerSpecial;

/**
 * Created by Shreya Kotak on 04/05/16.
 */
public class Utility {
    public final Context mContext;

    public Utility(Context context) {
        mContext = context;
    }

    public AccountInfo getAccountInfo() {
        SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String phone_number = ConfigSharedPreferencesUtil.getUserName(mContext, configSharedPreferences);
        RealmUtil data = new RealmUtil(mContext);
        AccountInfo accountInfo = null;

        accountInfo = data.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);
        AccountInfo user = new AccountInfo();
        if(accountInfo!=null) {
            user.setId(accountInfo.getId());
            user.setUid(accountInfo.getUid());
            user.setName(accountInfo.getName());
            user.setPhoneNumber(accountInfo.getPhoneNumber());
            user.setIdentify(accountInfo.getIdentify());
            user.setPassword(accountInfo.getPassword());
            user.setConfirm_password(accountInfo.getConfirm_password());
            user.setRecommend_id(accountInfo.getRecommend_id());
            user.setRole(accountInfo.getRole());
            user.setDriver_type(accountInfo.getDriver_type());
            user.setAccessKey(accountInfo.getAccessKey());
            user.setRegisterToken(accountInfo.getRegisterToken());
        }
        return user;
    }

    public DriverIdentifyInfo getDriverAccountInfo() {
        SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String phone_number = ConfigSharedPreferencesUtil.getUserName(mContext, configSharedPreferences);
        RealmUtil data = new RealmUtil(mContext);
        AccountInfo userinfo = null;
        userinfo  = data.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);
        DriverIdentifyInfo user = null;

        if(userinfo!=null) {
            DriverIdentifyInfo accountInfo = data.queryDriver(Constants.ACCOUNT_NAME, userinfo.getPhoneNumber());
            if (accountInfo != null) {
                user = new DriverIdentifyInfo();
                user.setId(accountInfo.getId());
                user.setUid(accountInfo.getUid());
                user.setDid(accountInfo.getDid());
                user.setName(accountInfo.getName());
                user.setDtype(accountInfo.getDtype());
                user.setAccesskey(accountInfo.getAccesskey());
                user.setCar_number(accountInfo.getCar_number());
                user.setCar_brand(accountInfo.getCar_brand());
                user.setCar_born(accountInfo.getCar_born());
                user.setCar_reg(accountInfo.getCar_reg());
                user.setCar_cc(accountInfo.getCar_cc());
                user.setCar_special(accountInfo.getCar_special());
                user.setCar_files(accountInfo.getCar_files());
                user.setCar_imgs(accountInfo.getCar_imgs());
            }
        }
        return user;
    }

    public RealmResults<DriverIdentifyInfo> getAllDriverAccountInfo() {

        RealmUtil data = new RealmUtil(mContext);
        AccountInfo userinfo = null;

        RealmResults<DriverIdentifyInfo> drivers  = data.queryAllDriver();


        return drivers;
    }


    public RealmResults<ServerBookmark> getAllServerBookMark() {

        RealmUtil data = new RealmUtil(mContext);
        AccountInfo userinfo = null;

        RealmResults<ServerBookmark> drivers  = data.queryServerBookmark();


        return drivers;
    }

    public RealmResults<NormalOrder> getRecommendationOrderList() {
        RealmUtil data = new RealmUtil(mContext);
        RealmResults<NormalOrder> orders = data.queryAllOrderList();

        return  orders;
    }

    public RealmResults<NormalOrder> getAccountOrderList() {
        SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String phone_number = ConfigSharedPreferencesUtil.getUserName(mContext, configSharedPreferences);
        RealmUtil data = new RealmUtil(mContext);
        AccountInfo userinfo = data.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);

        //DriverIdentifyInfo accountInfo = data.queryDriver(Constants.ACCOUNT_NAME, userinfo.getPhoneNumber());
        ArrayList<NormalOrder> orderlist = new ArrayList<NormalOrder>();
        RealmResults<NormalOrder> orders = data.queryOrderList(Constants.ACCOUNT_USERNAME, userinfo.getPhoneNumber());
        NormalOrder mOrder = new NormalOrder();
        return  orders;
      }

    public RealmResults<NormalOrder> getAccountOrderListByPhoneNumber(String number) {
        RealmUtil data = new RealmUtil(mContext);
        RealmResults<NormalOrder> orders = data.queryOrderList(Constants.ACCOUNT_USERNAME, number);
        NormalOrder mOrder = new NormalOrder();
        return  orders;
    }

    public RealmResults<NormalOrder> getWaitOrderList() {

        RealmUtil data = new RealmUtil(mContext);
        RealmResults<NormalOrder> orders= data.queryOrderList(Constants.ORDER_TICKET_STATUS, "1");

        return  orders;
    }

    public void clearData(final Class table){
        if(mContext!=null) {
            RealmUtil data = new RealmUtil(mContext);
            data.clearDB(table);
        }
    }

    public void clearServerInfoData(){
        RealmUtil data = new RealmUtil(mContext);
        data.clearDB(ServerBookmark.class);
        data.clearDB(ServerCarbrand.class);
        data.clearDB(ServerCountys.class);
        data.clearDB(ServerDriverType.class);
        data.clearDB(ServerImageType.class);
        data.clearDB(ServerContents.class);
        data.clearDB(ServerSpecial.class);

    }
}
