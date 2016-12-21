package tw.com.geminihsu.app01.bean;

import java.io.Serializable;

/**
 * Created by geminihsu on 2016/12/20.
 */

public class LocationAddress implements Cloneable ,Serializable {
    private static final long serialVersionUID = 7318969003680718433L;
    private double longitude;
    private double latitude;
    private String address;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocationAddress clone()  {
        try {
            return (LocationAddress) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
