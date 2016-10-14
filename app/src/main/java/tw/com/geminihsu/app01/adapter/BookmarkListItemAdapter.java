package tw.com.geminihsu.app01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01.R;

public class BookmarkListItemAdapter extends ArrayAdapter<BookmarkListItem> {

    private LayoutInflater mInflater;


    public BookmarkListItemAdapter(Context _context,
								   int rid, List<BookmarkListItem> list) {
        super(_context, rid, list);
        mInflater = (LayoutInflater)_context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       
    }
   
    public View getView(int position,
                        View convertView, ViewGroup parent) {
    	
    	
        // 取出資料
        final BookmarkListItem item = (BookmarkListItem)getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
        	holder = new ViewHolder();
	        // 以layout檔案產生View
        	convertView = mInflater.inflate(R.layout.bookmarks_maplist_activity_item, null);
	        //DisplayUtil displayUtil = new DisplayUtil();
			//displayUtil.setFontSize(view, context.getResources().getDimension(R.dimen.default_text_size_px));
			// 設定尊稱
			holder.title = (RadioButton)convertView.findViewById(R.id.title);
			holder.title.setChecked(item.check);
			holder.title.setText(item.book_title);

			// 設定日期
			holder.address = (TextView)convertView.findViewById(R.id.address);
			holder.address.setText(item.book_address);




			convertView.setTag(holder);
        }
        else
        {
        	 holder = (ViewHolder)convertView.getTag();

			holder.title.setChecked(item.check);
			holder.title.setText(item.book_title);
			holder.address.setText(item.book_address);




		}
        

        return convertView;
    }

    private class ViewHolder {
		RadioButton title;
		TextView address;

	}
    


}
