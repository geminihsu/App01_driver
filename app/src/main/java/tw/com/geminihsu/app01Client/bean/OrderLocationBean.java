package tw.com.geminihsu.app01Client.bean;

import java.io.Serializable;

import io.realm.RealmObject;

public class OrderLocationBean extends RealmObject implements Cloneable ,Serializable {

    private static final long serialVersionUID = 541836561141812221L;
    private int id;
    private String longitude;
    private String latitude;
    private String zipcode;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}