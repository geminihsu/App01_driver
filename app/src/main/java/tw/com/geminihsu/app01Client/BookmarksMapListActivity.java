package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.adapter.BookmarkListItem;
import tw.com.geminihsu.app01Client.adapter.BookmarkListItemAdapter;
import tw.com.geminihsu.app01Client.bean.USerBookmark;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.RealmUtil;

public class BookmarksMapListActivity extends Activity {

    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    private ListView listView;
    private final List<BookmarkListItem> mBookmarkListData = new ArrayList<BookmarkListItem>();;
    private BookmarkListItemAdapter listViewAdapter;
    private int provide_location;



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
        setLister();
        Bundle args = getIntent().getExtras();
        if(args!=null)
            provide_location = args.getInt(Constants.ARG_POSITION);



    }

    private void findViews()
    {
        listView = (ListView)findViewById(R.id.listView1);
        getDataFromDB();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.sure_take_spec));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return true;
    }




    private void setLister()
    {
        listViewAdapter = new BookmarkListItemAdapter(BookmarksMapListActivity.this, 0, mBookmarkListData);

        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

                final BookmarkListItem orderItem = mBookmarkListData.get(position);

                final USerBookmark info = orderItem.userbookmark;

                final RadioButton select = (RadioButton) v.findViewById(R.id.title);

                orderItem.check=!orderItem.check;
                if(orderItem.check) {
                    USerBookmark bookmark = new USerBookmark();
                    bookmark.setId(info.getId());
                    bookmark.setLat(info.getLat());
                    bookmark.setLng(info.getLng());
                    bookmark.setLocation(info.getLocation());
                    bookmark.setStreetAddress(info.getStreetAddress());
                    bookmark.setLocality(info.getLocality());
                    bookmark.setZipCode(info.getZipCode());
                    bookmark.setCountryName(info.getCountryName());

                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.BUNDLE_LOCATION, bookmark);
                    i.putExtras(b);
                    if (provide_location == Constants.DEPARTURE_QUERY_BOOKMARK)
                        setResult(Constants.DEPARTURE_QUERY_BOOKMARK, i);
                    else if (provide_location == Constants.DESTINATION_QUERY_BOOKMARK)
                        setResult(Constants.DESTINATION_QUERY_BOOKMARK, i);
                    else if (provide_location == Constants.STOP_QUERY_BOOKMARK)
                        setResult(Constants.STOP_QUERY_BOOKMARK, i);
                    mBookmarkListData.set(position,orderItem);
                }
                for(int i = 0;i<mBookmarkListData.size();i++)
                {
                    if(i!=position)
                    {

                        BookmarkListItem item = mBookmarkListData.get(i);
                        item.check = false;
                        mBookmarkListData.set(position,orderItem);
                    }
                }

                listViewAdapter.notifyDataSetChanged();


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ACTIONBAR_MENU_ITEM_SUMMIT:
                this.finish();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    private void getDataFromDB() {

        RealmUtil data = new RealmUtil(this);
        RealmResults<ServerBookmark> bookmarks = data.queryServerBookmark();
        mBookmarkListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < bookmarks.size(); i++) {
                // for listview 要用的資料
                BookmarkListItem item = new BookmarkListItem();


                item.book_title = bookmarks.get(i).getLocation()+":\t";

                 item.book_address =bookmarks.get(i).getStreetAddress();

                item.bookmark = bookmarks.get(i);
                mBookmarkListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(BookmarksMapListActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/

    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        RealmUtil data = new RealmUtil(this);
        RealmResults<USerBookmark> bookmarks = data.queryUserBookmark();
        mBookmarkListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < bookmarks.size(); i++) {
                // for listview 要用的資料
                BookmarkListItem item = new BookmarkListItem();


                item.book_title = bookmarks.get(i).getLocation()+":\t";

                item.book_address =bookmarks.get(i).getStreetAddress();

                item.userbookmark = bookmarks.get(i);
                mBookmarkListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(BookmarksMapListActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
