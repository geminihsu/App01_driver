package tw.com.geminihsu.app01.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.ImageBean;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;

/**
 * Created by geminihsu on 16/10/28.
 */
public class RealmUtil {

    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_REALM_FILE_NAME = "App01.realm";

    private static  RealmUtil sIntance;
    public final Context mContext;
    private Realm mRealm;
    public RealmUtil(Context mContext) {
        File file = new File(Environment.getExternalStorageDirectory()+ Constants.SDACRD_DIR_APP_ROOT);

        //if(!file.exists())
         //   file.mkdir();
        this.mContext = mContext;
        mRealm = Realm.getInstance(
                new RealmConfiguration.Builder(file)
                        .name(mContext.getString(R.string.app_database_name))
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

    public DriverIdentifyInfo queryDriver(String column, String value) {

        DriverIdentifyInfo driverIdentifyInfo = mRealm.where(DriverIdentifyInfo.class).equalTo(column, value).findFirst();
        return driverIdentifyInfo;
    }

    public NormalOrder queryOrder(String column, String value) {

        NormalOrder order = mRealm.where(NormalOrder.class).equalTo(column, value).findFirst();
        return order;
    }

    public RealmResults<NormalOrder> queryOrderList(String column, String value) {

        RealmResults<NormalOrder> orders = mRealm.where(NormalOrder.class).equalTo(column, value).findAll();
        return orders;
    }

    public RealmResults<DriverIdentifyInfo> queryAllDriver() {

        RealmResults<DriverIdentifyInfo> drivers= mRealm.where(DriverIdentifyInfo.class).findAll();
        return drivers;
    }

    public RealmResults<ImageBean> queryImage(String column, String value) {

        RealmResults<ImageBean> file = mRealm.where(ImageBean.class).equalTo(column, value).findAll();
        return file;
    }

    public void addAccount(AccountInfo user)
    {
        mRealm.beginTransaction();

        AccountInfo accountInfo = mRealm.createObject(AccountInfo.class);
        accountInfo.setId(user.getId());
        accountInfo.setUid(user.getUid());
        accountInfo.setName(user.getName());
        accountInfo.setLevel(user.getLevel());
        accountInfo.setPhoneNumber(user.getPhoneNumber());
        accountInfo.setIdentify(user.getIdentify());
        accountInfo.setPassword(user.getPassword());
        accountInfo.setConfirm_password(user.getConfirm_password());
        accountInfo.setRecommend_id(user.getRecommend_id());
        accountInfo.setRole(user.getRole());
        accountInfo.setAccessKey(user.getAccessKey());
        accountInfo.setRegisterToken(user.getRegisterToken());
        accountInfo.setClient_ticket_id(user.getClient_ticket_id());
        accountInfo.setDriver_ticket_id(user.getDriver_ticket_id());
        mRealm.copyToRealm(accountInfo);
        mRealm.commitTransaction();
    }

    public void addImageFileInfo(ImageBean image)
    {
        mRealm.beginTransaction();

        ImageBean imageBean = mRealm.createObject(ImageBean.class);
        imageBean.setUser_id(image.getUser_id());
        imageBean.setUser_name(image.getUser_name());
        imageBean.setUploadtype(image.getUploadtype());
        imageBean.setFile_id(image.getFile_id());
        imageBean.setFile_url(image.getFile_url());
        mRealm.copyToRealm(imageBean);
        mRealm.commitTransaction();
    }

    public void addDriverInfo(DriverIdentifyInfo driver)
    {
        mRealm.beginTransaction();

        DriverIdentifyInfo driverIdentifyInfo = mRealm.createObject(DriverIdentifyInfo.class);
        driverIdentifyInfo.setId(driver.getId());
        driverIdentifyInfo.setUid(driver.getUid());
        driverIdentifyInfo.setDid(driver.getDid());
        driverIdentifyInfo.setName(driver.getName());
        driverIdentifyInfo.setAccesskey(driver.getAccesskey());
        driverIdentifyInfo.setDtype(driver.getDtype());
        driverIdentifyInfo.setCar_number(driver.getCar_number());
        driverIdentifyInfo.setCar_brand(driver.getCar_brand());
        driverIdentifyInfo.setCar_born(driver.getCar_born());
        driverIdentifyInfo.setCar_reg(driver.getCar_reg());
        driverIdentifyInfo.setCar_cc(driver.getCar_cc());
        driverIdentifyInfo.setCar_special(driver.getCar_special());
        driverIdentifyInfo.setCar_files(driver.getCar_files());
        driverIdentifyInfo.setCar_imgs(driver.getCar_imgs());


        mRealm.copyToRealm(driverIdentifyInfo);
        mRealm.commitTransaction();
    }

    public void addNormalOrder(NormalOrder order)
    {
        mRealm.beginTransaction();

        NormalOrder normalOrder = mRealm.createObject(NormalOrder.class);
        normalOrder.setOrder_id(order.getOrder_id());
        normalOrder.setUser_id(order.getUser_id());
        normalOrder.setUser_uid(order.getUser_uid());
        normalOrder.setUser_did(order.getUser_did());
        normalOrder.setUser_name(order.getUser_name());
        normalOrder.setAccesskey(order.getAccesskey());
        normalOrder.setDtype(order.getDtype());

        normalOrder.setBegin_address(order.getBegin().getAddress());
        //normalOrder.setStop_address(order.getStop().getAddress());
        normalOrder.setEnd_address(order.getEnd().getAddress());
        normalOrder.setCargo_type(order.getCargo_type());
        normalOrder.setCargo_size(order.getCargo_size());
        normalOrder.setCargo_imgs(order.getCargo_imgs());
        normalOrder.setTimebegin(order.getTimebegin());
        normalOrder.setCar_special(order.getCar_special());
        normalOrder.setRemark(order.getRemark());
        normalOrder.setPrice(order.getPrice());
        normalOrder.setTip(order.getTip());
        normalOrder.setTicket_id(order.getTicket_id());
        normalOrder.setTicket_status(order.getTicket_status());
        normalOrder.setOrderdate(order.getOrderdate());
        normalOrder.setTarget(order.getTarget());

        mRealm.copyToRealm(normalOrder);
        mRealm.commitTransaction();
    }



    public void updateAccount(AccountInfo user)
    {
        ///mRealm.beginTransaction();
        int new_Id = user.getId();
        String new_uId = user.getUid();
        String new_name = user.getName();
        String new_phoneNumber = user.getPhoneNumber();
        String new_Identify = user.getIdentify();
        String new_password = user.getPassword();
        String new_confirm_password = user.getPassword();
        String new_recommend_id = user.getRecommend_id();
        String new_level = user.getLevel();
        String new_client_ticket_id = user.getClient_ticket_id();
        String new_driver_ticket_id = user.getDriver_ticket_id();
        int new_role = user.getRole();
        String new_accessKey = user.getAccessKey();



        AccountInfo accountInfo = mRealm.where(AccountInfo.class).equalTo(Constants.ACCOUNT_PHONE_NUMBER, user.getPhoneNumber()).findFirst();
        mRealm.beginTransaction();
        if(accountInfo ==null){
            AccountInfo account = mRealm.createObject(AccountInfo.class);
            account.setUid(new_uId);
            account.setId(new_Id);
            account.setName(new_name);
            account.setLevel(new_level);
            account.setPhoneNumber(new_phoneNumber);
            account.setIdentify(new_Identify);
            account.setPassword(new_password);
            account.setConfirm_password(new_confirm_password);
            account.setRecommend_id(new_recommend_id);
            account.setRole(new_role);
            account.setAccessKey(new_accessKey);
            account.setClient_ticket_id(new_client_ticket_id);
            account.setDriver_ticket_id(new_driver_ticket_id);
            account.setLevel(new_level);

        }else{
            accountInfo.setId(new_Id);
            accountInfo.setUid(new_uId);
            accountInfo.setName(new_name);
            accountInfo.setLevel(new_level);
            accountInfo.setPhoneNumber(new_phoneNumber);
            accountInfo.setIdentify(new_Identify);
            accountInfo.setPassword(new_password);
            accountInfo.setConfirm_password(new_confirm_password);
            accountInfo.setRecommend_id(new_recommend_id);
            accountInfo.setRole(new_role);
            accountInfo.setAccessKey(new_accessKey);
            accountInfo.setClient_ticket_id(new_client_ticket_id);
            accountInfo.setDriver_ticket_id(new_driver_ticket_id);

            mRealm.copyToRealmOrUpdate(accountInfo);
        }

        mRealm.commitTransaction();
    }

    public void clearDB() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(NormalOrder.class);
            }
        });
    }

    public Realm getmRealm() {
        return mRealm;
    }

    public void setmRealm(Realm mRealm) {
        this.mRealm = mRealm;
    }
}
