package tw.com.geminihsu.app01Client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01Client.R;

public class RecommendListItemAdapter extends ArrayAdapter<RecommendListItem> {

    private LayoutInflater mInflater;


    public RecommendListItemAdapter(Context _context,
									int rid, List<RecommendListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final RecommendListItem item = (RecommendListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.recommdate_activity_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
			// 設定尊稱
			holder.friend_name = (TextView)convertView.findViewById(R.id.friend_name);
			holder.friend_name.setText(item.friend_name);

			// 設定日期
			holder.date = (TextView)convertView.findViewById(R.id.date);
			holder.date.setText(item.date);

			// 設定評語
			holder.prize = (TextView)convertView.findViewById(R.id.prize);
			holder.prize.setText(item.prize);



			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();

			holder.friend_name.setText(item.friend_name);
			holder.date.setText(item.date);
			holder.prize.setText(item.prize);



		}
        

        return convertView;
    }

    private class ViewHolder {
		TextView friend_name;
		TextView date;
		TextView prize;

	}
    


}
