package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.adapter.CommentListItem;
import tw.com.geminihsu.app01.adapter.CommentListItemAdapter;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class DriverAccountActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private LinearLayout linearLayout_form;
    private LinearLayout linearLayout_driver_comment;

    final public static int DRIVER_SERVICE = 1;
    final public static int DRIVER_COMMENT=2;


    private int choice = 0;

    private ListView listView;
    private final List<CommentListItem> mCommentListData = new ArrayList<CommentListItem>();;
    private CommentListItemAdapter listViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_account_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                choice = bundle.getInt(Constants.ARG_POSITION);
                displayLayout();
            }else
            {
                //Error!!!!
            }
        }
        else
        {
            //Error!!!!
        }
        this.setLister();




    }

    private void findViews()
    {
        linearLayout_form = (LinearLayout) findViewById(R.id.driver_service);
        linearLayout_driver_comment = (LinearLayout) findViewById(R.id.driver);
        listView = (ListView)findViewById(R.id.listView1);


    }



    private void displayLayout()
    {
         if(choice == DRIVER_SERVICE)
         {
             linearLayout_form.setVisibility(View.VISIBLE);
             getActionBar().setTitle(getString(R.string.driver_price_report_check_title));
         }else if(choice == DRIVER_COMMENT)
         {
             listView.setVisibility(View.VISIBLE);
             getDataFromDB();
             listViewAdapter = new CommentListItemAdapter(DriverAccountActivity.this, 0, mCommentListData);
             listView.setAdapter(listViewAdapter);
             listViewAdapter.notifyDataSetChanged();

         }
    }

    private void setLister()
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(choice== DriverAccountActivity.DRIVER_SERVICE){
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_take));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }else if(choice== DriverAccountActivity.DRIVER_COMMENT){
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.passenger_comment_report));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                if(choice == DRIVER_COMMENT)
                {
                    Intent question = new Intent(DriverAccountActivity.this, SupportAnswerActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.REPORT_DRIVER);
                    question.putExtras(b);
                    startActivity(question);
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DriverAccountActivity.this);
                    // set title
                    alertDialogBuilder.setTitle(getString(R.string.client_agree_price));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(getString(R.string.client_agree_notion))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.client_wait_take_order), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setNegativeButton(getString(R.string.client_immendately_take_order), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent question = new Intent(DriverAccountActivity.this, ClientWaitCarActivity.class);
                                    startActivity(question);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                return true;
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
            for (int i = 0; i < 3; i++) {
                // for listview 要用的資料
                CommentListItem item = new CommentListItem();

                item.driver_name="王先生";
                item.date = "2016-06-29";
                item.comment = "準時，服務好，熱心又親切";
                item.value = "3";

                mCommentListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(DriverAccountActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
