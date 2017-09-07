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
import tw.com.geminihsu.app01Client.adapter.CommentListItem;
import tw.com.geminihsu.app01Client.adapter.CommentListItemAdapter;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.Utility;

public class CommentActivity extends Activity {

    private ListView listView;
    private final List<CommentListItem> mCommentListData = new ArrayList<CommentListItem>();;
    private CommentListItemAdapter listViewAdapter;

    private int MAXSIZE=3;

    private JsonPutsUtil sendDataRequest;
    private int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(this);
        sendDataRequest.setAccountQueryCommentManagerCallBackFunction(new JsonPutsUtil.AccountQueryCommentManagerCallBackFunction() {


            @Override
            public void getCommentList(String message) {

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Utility info = new Utility(this);
        AccountInfo accountInfo = info.getAccountInfo();
        if(accountInfo!=null)
            sendDataRequest.getAccountComment(accountInfo);
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
