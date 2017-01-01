package tw.com.geminihsu.app01.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AccountInfo extends RealmObject implements Cloneable ,Serializable {
    private static final long serialVersionUID = 8683739988249633893L;


    private int id;
    private String uid;
    private String name;
    private String level;
    private String identify;
    @PrimaryKey
    private String phoneNumber;
    private String password;
    private String confirm_password;
    private String recommend_id;
    private int role;//0:client,1:driver,2:both
    private String registerToken;
    private String accessKey;
    private String driver_type;//記錄目前營運的身份


    private String client_ticket_id;
    private String driver_ticket_id;
    //private DriverIdentifyInfo driverIdentifyInfo;


    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getRecommend_id() {
        return recommend_id;
    }

    public void setRecommend_id(String recommend_id) {
        this.recommend_id = recommend_id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClient_ticket_id() {
        return client_ticket_id;
    }

    public void setClient_ticket_id(String client_ticket_id) {
        this.client_ticket_id = client_ticket_id;
    }

    public String getDriver_ticket_id() {
        return driver_ticket_id;
    }

    public void setDriver_ticket_id(String driver_ticket_id) {
        this.driver_ticket_id = driver_ticket_id;
    }

    public String getDriver_type() {
        return driver_type;
    }

    public void setDriver_type(String driver_type) {
        this.driver_type = driver_type;
    }
}
