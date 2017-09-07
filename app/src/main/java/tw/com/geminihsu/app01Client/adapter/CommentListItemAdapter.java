package tw.com.geminihsu.app01Client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01Client.R;

public class CommentListItemAdapter extends ArrayAdapter<CommentListItem> {

    private LayoutInflater mInflater;


    public CommentListItemAdapter(Context _context,
								  int rid, List<CommentListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final CommentListItem item = (CommentListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.comment_activity_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
			// 設定尊稱
			holder.driver_name = (TextView)convertView.findViewById(R.id.driver_name);
			holder.driver_name.setText(item.driver_name);

			// 設定日期
			holder.date = (TextView)convertView.findViewById(R.id.date);
			holder.date.setText(item.date);

			// 設定評語
			holder.comment = (TextView)convertView.findViewById(R.id.comment);
			holder.comment.setText(item.comment);

			RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
			holder.ratingBar = ratingBar;
			holder.ratingBar.setRating((Float.parseFloat(item.value)));



			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();

			holder.driver_name.setText(item.driver_name);
			holder.date.setText(item.date);
			holder.comment.setText(item.comment);
			holder.ratingBar.setRating((Float.parseFloat(item.value)));



		}
        

        return convertView;
    }

    private class ViewHolder {
		TextView driver_name;
		TextView date;
		TextView comment;
		RatingBar ratingBar;
	}
    


}
