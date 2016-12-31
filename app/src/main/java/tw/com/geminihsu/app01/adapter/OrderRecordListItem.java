package tw.com.geminihsu.app01.adapter;

import android.graphics.Bitmap;

import tw.com.geminihsu.app01.bean.NormalOrder;

public class OrderRecordListItem {
    public Bitmap image;
    public String order_status;
    public String time;
    public String departure;
    public String destination="";
    public String pay_method="";
    public String car_status;
    public int order_status_fontColor;
    public int car_status_Visibility;
    public NormalOrder normalOrder;

}
