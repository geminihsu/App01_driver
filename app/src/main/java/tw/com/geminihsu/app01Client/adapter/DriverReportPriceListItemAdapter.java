package tw.com.geminihsu.app01Client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01Client.R;

public class DriverReportPriceListItemAdapter extends ArrayAdapter<DriverReportPriceListItem> {

    private LayoutInflater mInflater;


    public DriverReportPriceListItemAdapter(Context _context,
											int rid, List<DriverReportPriceListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final DriverReportPriceListItem item = (DriverReportPriceListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.driver_report_price_list_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
	        

			// 設定司機名稱
			holder.driver_name = (TextView)convertView.findViewById(R.id.people_value);
			holder.driver_name.setText(item.driver_name);


			// 司機等級
			holder.driver_title = (Button)convertView.findViewById(R.id.host_title);
			holder.driver_title.setText(item.title);

			// 單價
			holder.price = (TextView) convertView.findViewById(R.id.money_value);
			holder.price.setText(item.price);


			RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
			holder.ratingBar = ratingBar;
			holder.ratingBar.setRating((Float.parseFloat(item.value)));



			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();
			 holder.driver_name.setText(item.driver_name);
			 holder.driver_title.setText(item.title);
			 holder.price.setText(item.price);



		}
        

        return convertView;
    }

    private class ViewHolder {
		TextView driver_name;
		Button driver_title;
		TextView price;
		RatingBar ratingBar;

	}
    


}
