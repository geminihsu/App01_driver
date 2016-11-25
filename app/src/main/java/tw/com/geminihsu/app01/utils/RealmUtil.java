package tw.com.geminihsu.app01.utils;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tw.com.geminihsu.app01.bean.AccountInfo;

/**
 * Created by geminihsu on 16/10/28.
 */
public class RealmUtil {
    private static  RealmUtil sIntance;
    public final Context mContext;
    private String realmName = "realm_demo.realm";
    private Realm mRealm;
    public RealmUtil(Context mContext) {

        this.mContext = mContext;
        mRealm = Realm.getInstance(
                new RealmConfiguration.Builder(mContext)
                        .name("data.realm")
                        .build()
        );
    }
    /**
     * 双检索单例
     * @param context
     * @return
     */
    public  static  RealmUtil getIntance(Context context){
        if (sIntance == null) {
            synchronized (RealmUtil.class) {
                if (sIntance == null) {
                    sIntance = new RealmUtil(context);
                }
            }
        }
        return  sIntance;
    }
    /**
     * 获取realm对象
     * @return
     */
    /*public Realm getRealm(){
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder(mContext)
                                .name(realmName)
                                .build());
        return realm;
    }*/

    public AccountInfo queryAccount(String column, String value) {

        AccountInfo user = mRealm.where(AccountInfo.class).equalTo(column, value).findFirst();
        return user;
    }

    public void addAccount(AccountInfo user)
    {
        mRealm.beginTransaction();

        AccountInfo accountInfo = mRealm.createObject(AccountInfo.class);
        accountInfo.setId(user.getId());
        accountInfo.setName(user.getName());
        accountInfo.setPhoneNumber(user.getPhoneNumber());
        accountInfo.setIdentify(user.getIdentify());
        accountInfo.setPassword(user.getPassword());
        accountInfo.setConfirm_password(user.getConfirm_password());
        accountInfo.setRecommend_id(user.getRecommend_id());
        accountInfo.setRole(user.getRole());
        accountInfo.setAccessKey(user.getAccessKey());
        accountInfo.setRegisterToken(user.getRegisterToken());
        mRealm.copyToRealm(accountInfo);
        mRealm.commitTransaction();
    }

    public void updateAccount(AccountInfo user)
    {
        ///mRealm.beginTransaction();
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(user.getId());
        accountInfo.setName(user.getName());
        accountInfo.setPhoneNumber(user.getPhoneNumber());
        accountInfo.setIdentify(user.getIdentify());
        accountInfo.setPassword(user.getPassword());
        accountInfo.setConfirm_password(user.getConfirm_password());
        accountInfo.setRecommend_id(user.getRecommend_id());
        accountInfo.setRole(user.getRole());
        accountInfo.setAccessKey(user.getAccessKey());
        mRealm.copyToRealmOrUpdate(accountInfo);
        mRealm.commitTransaction();
    }
}
