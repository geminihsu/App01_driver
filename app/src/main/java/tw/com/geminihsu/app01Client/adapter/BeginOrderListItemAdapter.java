package tw.com.geminihsu.app01Client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01Client.R;


public class BeginOrderListItemAdapter extends ArrayAdapter<BeginOrderListItem> {

	TakeLookButtonListener takeLookButtonListner;

	public interface TakeLookButtonListener {
		public void ontakeLookButtonListener(int position);
	}

	public void setTakeLookButtonListner(TakeLookButtonListener listener) {
		this.takeLookButtonListner = listener;
	}

	TakeOverButtonListner takeOverButtonListner;

	public interface TakeOverButtonListner {
		public void ontakeOverButtonListener(int position);
	}

	public void setTakeOverButtonListner(TakeOverButtonListner listener) {
		this.takeOverButtonListner = listener;
	}

	private LayoutInflater mInflater;


    public BeginOrderListItemAdapter(Context _context,
									 int rid, List<BeginOrderListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
   
    public View getView(final int position,
						View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final BeginOrderListItem item = (BeginOrderListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.fragment_begin_order_list_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
			holder.order_title = (TextView) convertView.findViewById(R.id.order_title);
			holder.order_title.setText(item.order_title);

			holder.departure = (TextView)convertView.findViewById(R.id.departure);
			holder.departure.setText(item.departure);

			holder.destination = (TextView) convertView.findViewById(R.id.destination);
			holder.destination.setText(item.destination);

			holder.order_time = (TextView)convertView.findViewById(R.id.order_time);
			holder.order_time.setText(item.order_time);

			holder.btn_take_over = (Button) convertView.findViewById(R.id.take_over);
			holder.btn_take_look = (Button) convertView.findViewById(R.id.take_look);
			holder.btn_take_look.setTag(position);
			holder.btn_take_over.setText(item.button_information);

			holder.btn_take_look.setVisibility(item.button_take_look_visible);
			holder.btn_take_over.setTag(position);
			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();

			holder.order_title.setText(item.order_title);
			holder.departure.setText(item.departure);
        	holder.destination.setText(item.destination);
			//時間類型(即時，預約)
			holder.order_time.setText(item.order_time);
			holder.btn_take_over.setText(item.button_information);
			holder.btn_take_look.setVisibility(item.button_take_look_visible);
			holder.btn_take_look.setTag(position);
			holder.btn_take_over.setTag(position);


		}


		holder.btn_take_look.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (takeLookButtonListner != null) {
					takeLookButtonListner.ontakeLookButtonListener(position);
				}

			}
		});

		holder.btn_take_over.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (takeOverButtonListner != null) {
					takeOverButtonListner.ontakeOverButtonListener(position);
				}

			}
		});

		return convertView;
    }

    private class ViewHolder {
		TextView order_title;
		TextView departure;
		TextView destination;
		TextView order_time;

		Button btn_take_look;
		Button btn_take_over;

	}



}
