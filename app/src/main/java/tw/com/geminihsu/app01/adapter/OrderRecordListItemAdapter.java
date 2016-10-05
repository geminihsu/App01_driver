package tw.com.geminihsu.app01.adapter;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.geminihsu.app01.R;

public class OrderRecordListItemAdapter extends ArrayAdapter<OrderRecordListItem> {

    private LayoutInflater mInflater;


    public OrderRecordListItemAdapter(Context _context,
									  int rid, List<OrderRecordListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final OrderRecordListItem item = (OrderRecordListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.fragment_order_record_list_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
	        
	        // 設定圖片
	        holder.image = (ImageView)convertView.findViewById(R.id.image);
	        holder.image.setImageBitmap(item.image);


			// 設定紀錄狀態
			holder.order_status = (TextView)convertView.findViewById(R.id.order_status);
			holder.order_status.setText(item.order_status);
			holder.order_status.setTextColor(item.order_status_fontColor);


			// 設定記錄時間
	        holder.time = (TextView)convertView.findViewById(R.id.time);
	        holder.time.setText(item.time);
	
	        // 設定departure
	        holder.departure = (TextView)convertView.findViewById(R.id.departure);
	        holder.departure.setText(item.departure);
	        
	        // 設定destination
	        holder.destination = (TextView)convertView.findViewById(R.id.destination);
	        holder.destination.setText(item.destination);

			// 設定destination
			holder.pay_method = (TextView)convertView.findViewById(R.id.pay_method);
			holder.pay_method.setText(item.pay_method);

			// 顯示搭乘方式
			holder.car_status = (TextView)convertView.findViewById(R.id.car_status);
			holder.car_status.setText(item.car_status);
			holder.car_status.setVisibility(item.car_status_Visibility);


			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();
        	 holder.image.setImageBitmap(item.image);
			 holder.order_status.setText(item.order_status);
			 holder.time.setText(item.time);
        	 holder.departure.setText(item.departure);
        	 holder.destination.setText(item.destination);
			 holder.pay_method.setText(item.pay_method);
			 holder.car_status.setVisibility(item.car_status_Visibility);



		}
        

        return convertView;
    }

    private class ViewHolder {
		ImageView image;
		TextView order_status;
		TextView time;
		TextView departure;
		TextView destination;
		TextView pay_method;

		TextView car_status;
	}
    


}
