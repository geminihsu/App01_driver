package tw.com.geminihsu.app01;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.adapter.BookmarkListItem;
import tw.com.geminihsu.app01.adapter.BookmarkListItemAdapter;
import tw.com.geminihsu.app01.adapter.RecommendListItem;
import tw.com.geminihsu.app01.adapter.RecommendListItemAdapter;

public class BookmarksMapListActivity extends Activity {

    private ListView listView;
    private final List<BookmarkListItem> mBookmarkListData = new ArrayList<BookmarkListItem>();;
    private BookmarkListItemAdapter listViewAdapter;

    private int MAXSIZE=3;


    private int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        getDataFromDB();
        listViewAdapter = new BookmarkListItemAdapter(BookmarksMapListActivity.this, 0, mBookmarkListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();





    }

    private void findViews()
    {
        listView = (ListView)findViewById(R.id.listView1);
        getDataFromDB();

        listViewAdapter = new BookmarkListItemAdapter(BookmarksMapListActivity.this, 0, mBookmarkListData);

        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

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

        mBookmarkListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < MAXSIZE; i++) {
                // for listview 要用的資料
                BookmarkListItem item = new BookmarkListItem();

                if(i==0)
                    item.check=true;
                else
                    item.check=false;
                item.book_title = "住家：";
                item.book_address = "台中市市民大道一段一號二樓";

                mBookmarkListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(BookmarksMapListActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
