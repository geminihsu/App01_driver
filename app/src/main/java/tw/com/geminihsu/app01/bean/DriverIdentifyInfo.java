package tw.com.geminihsu.app01.bean;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmObject;

public class DriverIdentifyInfo extends RealmObject implements Cloneable ,Serializable {
    private int id;
    private String uid;
    private String did;//註冊司機成功，從Server回傳的did
    private String name;
    private String realname;
    private String accesskey;
    private String dtype;
    private String car_number;
    private String car_brand;
    private String car_born;
    private String car_reg;
    private String car_cc;
    private String car_special;
    private String car_files;
    private String car_imgs;
    private String enable;
    //private ArrayList<ImageBean> imageList;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_born() {
        return car_born;
    }

    public void setCar_born(String car_born) {
        this.car_born = car_born;
    }

    public String getCar_reg() {
        return car_reg;
    }

    public void setCar_reg(String car_reg) {
        this.car_reg = car_reg;
    }

    public String getCar_cc() {
        return car_cc;
    }

    public void setCar_cc(String car_cc) {
        this.car_cc = car_cc;
    }

    public String getCar_special() {
        return car_special;
    }

    public void setCar_special(String car_special) {
        this.car_special = car_special;
    }

    public String getCar_files() {
        return car_files;
    }

    public void setCar_files(String car_files) {
        this.car_files = car_files;
    }

    public String getCar_imgs() {
        return car_imgs;
    }

    public void setCar_imgs(String car_imgs) {
        this.car_imgs = car_imgs;
    }
    /*public ArrayList<ImageBean> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageBean> imageList) {
        this.imageList = imageList;
    }*/

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
