package tw.com.geminihsu.app01.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 2016/12/28.
 */

public class USerBookmark extends RealmObject implements Cloneable ,Serializable
{

    private static final long serialVersionUID = 1525266946868945830L;
    private String id;
    private String location;
    private String lat;
    private String lng;
    private String Locality;
    private String zipCode;
    private String CountryName;
    private String streetAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}
