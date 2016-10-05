package tw.com.geminihsu.app01;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.adapter.CommentListItem;
import tw.com.geminihsu.app01.adapter.CommentListItemAdapter;
import tw.com.geminihsu.app01.adapter.OrderRecordListItem;
import tw.com.geminihsu.app01.adapter.OrderRecordListItemAdapter;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class CommentActivity extends Activity {

    private ListView listView;
    private final List<CommentListItem> mCommentListData = new ArrayList<CommentListItem>();;
    private CommentListItemAdapter listViewAdapter;

    private int MAXSIZE=3;


    private int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        getDataFromDB();
        listViewAdapter = new CommentListItemAdapter(CommentActivity.this, 0, mCommentListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();





    }

    private void findViews()
    {
        listView = (ListView)findViewById(R.id.listView1);

    }



    private void displayLayout()
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        mCommentListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < MAXSIZE; i++) {
                // for listview 要用的資料
                CommentListItem item = new CommentListItem();

                item.driver_name="王先生";
                item.date = "2016-06-29";
                item.comment = "準時，服務好，熱心又親切";
                item.value = "3";

                mCommentListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(CommentActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
