package tw.com.geminihsu.app01Client.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 28/12/2016.
 */

public class AccountTreeInfo extends RealmObject implements Cloneable ,Serializable {
    private static final long serialVersionUID = 6553052117682233184L;
    private int user_id;//account uid
    private String user_uid;
    private String user_did;
    private String user_name;
    private String accesskey;
    private int lv;
    private int last_watering;
    private int next;
    private int status;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_did() {
        return user_did;
    }

    public void setUser_did(String user_did) {
        this.user_did = user_did;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getLast_watering() {
        return last_watering;
    }

    public void setLast_watering(int last_watering) {
        this.last_watering = last_watering;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
