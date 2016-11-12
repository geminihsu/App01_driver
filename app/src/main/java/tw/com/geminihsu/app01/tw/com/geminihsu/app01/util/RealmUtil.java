package tw.com.geminihsu.app01.tw.com.geminihsu.app01.util;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.realm.AccountInfo;

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

    public AccountInfo queryAccount(String column,String value) {

        AccountInfo user = mRealm.where(AccountInfo.class).equalTo(column, value).findFirst();
        return user;
    }
}
