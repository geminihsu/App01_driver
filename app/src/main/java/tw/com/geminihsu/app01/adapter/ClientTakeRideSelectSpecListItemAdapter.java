package tw.com.geminihsu.app01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

import tw.com.geminihsu.app01.R;

public class ClientTakeRideSelectSpecListItemAdapter extends ArrayAdapter<ClientTakeRideSelectSpecListItem> {

    private LayoutInflater mInflater;


    public ClientTakeRideSelectSpecListItemAdapter(Context _context,
												   int rid, List<ClientTakeRideSelectSpecListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final ClientTakeRideSelectSpecListItem item = (ClientTakeRideSelectSpecListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.clien_take_ride_selectspec_requirement_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
			// 設定尊稱
			holder.title = (CheckBox)convertView.findViewById(R.id.title);
			holder.title.setChecked(item.check);
			holder.title.setText(item.book_title);


			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();

			holder.title.setChecked(item.check);
			holder.title.setText(item.book_title);


		}
        

        return convertView;
    }

    private class ViewHolder {
		CheckBox title;

	}
    


}
