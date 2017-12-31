package tw.com.geminihsu.app01Client.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by geminihsu on 22/12/2017.
 */

public class RideType implements Parcelable {

    public final String title;
    public final int option;
    //public final int dtype;
   // public final int cargoType;
    //public final String curAddress;

    public RideType(String title, int option) {
        this.title = title;
        this.option = option;
       // this.dtype = dtype;
       // this.cargoType = cargoType;
       // this.curAddress = curAddress;
    }


    protected RideType(Parcel in) {
        title = in.readString();
        option = in.readInt();
        //dtype = in.readInt();
       // cargoType = in.readInt();
        //curAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(option);
        //dest.writeInt(dtype);
        //dest.writeInt(cargoType);
        //dest.writeString(curAddress);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RideType> CREATOR = new Creator<RideType>() {
        @Override
        public RideType createFromParcel(Parcel in) {
            return new RideType(in);
        }

        @Override
        public RideType[] newArray(int size) {
            return new RideType[size];
        }
    };
}
