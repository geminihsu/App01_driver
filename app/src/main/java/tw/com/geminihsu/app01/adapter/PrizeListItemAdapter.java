package tw.com.geminihsu.app01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01.R;

public class PrizeListItemAdapter extends ArrayAdapter<PrizeListItem> {

    private LayoutInflater mInflater;


    public PrizeListItemAdapter(Context _context,
								int rid, List<PrizeListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final PrizeListItem item = (PrizeListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.prize_activity_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
			// 設定尊稱
			holder.prize_title = (TextView)convertView.findViewById(R.id.prize_info);
			holder.prize_title.setText(item.prize_title);


			holder.btn_take = (Button) convertView.findViewById(R.id.btn_take);
			holder.btn_take.setText(item.take);


			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();

			holder.prize_title.setText(item.prize_title);
			holder.btn_take.setText(item.take);



		}


        return convertView;
    }

    private class ViewHolder {
		TextView prize_title;
		 Button btn_take;
	}
    


}
