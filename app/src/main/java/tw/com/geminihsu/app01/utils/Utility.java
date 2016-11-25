package tw.com.geminihsu.app01.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.common.Constants;

/**
 * Created by Shreya Kotak on 04/05/16.
 */
public class Utility {
    public final Context mContext;

    public Utility(Context context) {
        mContext = context;
    }

    public static boolean isBlankField(EditText etPersonData) {
        return etPersonData.getText().toString().trim().equals("");
    }

    public AccountInfo getAccountInfo() {
        SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String phone_number = ConfigSharedPreferencesUtil.getUserName(mContext, configSharedPreferences);
        RealmUtil data = new RealmUtil(mContext);
        AccountInfo accountInfo = data.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);

        AccountInfo user = new AccountInfo();
        user.setId(accountInfo.getId());
        user.setName(accountInfo.getName());
        user.setPhoneNumber(accountInfo.getPhoneNumber());
        user.setIdentify(accountInfo.getIdentify());
        user.setPassword(accountInfo.getPassword());
        user.setConfirm_password(accountInfo.getConfirm_password());
        user.setRecommend_id(accountInfo.getRecommend_id());
        user.setRole(accountInfo.getRole());
        user.setAccessKey(accountInfo.getAccessKey());
        user.setRegisterToken(accountInfo.getRegisterToken());

        return user;
    }
}
