package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.adapter.RecommendListItem;
import tw.com.geminihsu.app01Client.adapter.RecommendListItemAdapter;

public class RecommendActivity extends Activity {

    private ListView listView;
    private final List<RecommendListItem> mCommentListData = new ArrayList<RecommendListItem>();;
    private RecommendListItemAdapter listViewAdapter;

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
        listViewAdapter = new RecommendListItemAdapter(RecommendActivity.this, 0, mCommentListData);
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
                RecommendListItem item = new RecommendListItem();

                item.friend_name="王先生";
                item.date = "2016-06-29";
                item.prize = getString(R.string.txt_obtain_prize);

                mCommentListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(RecommendActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
