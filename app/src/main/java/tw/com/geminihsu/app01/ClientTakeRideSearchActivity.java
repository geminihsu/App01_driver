package tw.com.geminihsu.app01;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.adapter.DriverReportPriceListItem;
import tw.com.geminihsu.app01.adapter.DriverReportPriceListItemAdapter;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.canvas.DrawView;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class ClientTakeRideSearchActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    final public static int DRIVER_REPORT_PRICE = 1;

    private LinearLayout LinearLayout_info;
    private LinearLayout circle;
    private TextView reportTitle;
    private TextView reportFee;
    private ListView driverList;
    private TextView number;
    private int option;
    private final List<DriverReportPriceListItem> mDriverListData = new ArrayList<DriverReportPriceListItem>();;
    private DriverReportPriceListItemAdapter listViewAdapter;

    private int MAXSIZE=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_order_car_search);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();

        this.setLister();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                option = bundle.getInt(Constants.ARG_POSITION);
                if(option == DRIVER_REPORT_PRICE)
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

        setLister();
        new CountDownTimer(2000,1000){

            @Override
            public void onFinish() {
                Intent question = new Intent(ClientTakeRideSearchActivity.this, ClientWaitCarActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {
               // mTextView.setText("seconds remaining:"+millisUntilFinished/1000);
            }

        }.start();

    }

    private void findViews()
    {
        reportTitle = (TextView) findViewById(R.id.txt_forget_password);
        reportFee = (TextView) findViewById(R.id.fee);
        number = (TextView)findViewById(R.id.number);
        number.setText("已通知\n2\n輛車");
        driverList = (ListView)findViewById(R.id.listView1);


        LinearLayout_info = (LinearLayout) findViewById(R.id.info);

        /*final DrawView view=new DrawView(this);
        view.setMinimumHeight(50);
        view.setMinimumWidth(50);
        //通知view组件重绘
        view.invalidate();
        view.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));
        circle.addView(view);*/

    }



    private void displayLayout()
    {
        reportTitle.setText(getString(R.string.txt_report_price));
        reportFee.setText(getString(R.string.txt_add_fee));
        driverList.setVisibility(View.VISIBLE);
        LinearLayout_info.setVisibility(View.GONE);

    }

    private void setLister()
    {
        getActionBar().setTitle(getString(R.string.driver_price_report_title));
        getDataFromDB();
        listViewAdapter = new DriverReportPriceListItemAdapter(ClientTakeRideSearchActivity.this, 0, mDriverListData);
        driverList.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        driverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent question = new Intent(ClientTakeRideSearchActivity.this, DriverAccountActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, DriverAccountActivity.DRIVER_SERVICE);
                question.putExtras(b);
                startActivity(question);


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(option == DRIVER_REPORT_PRICE)
        {
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.cancel_take_spec));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);

            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }else {
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.txt_cancel_order));
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
                Intent question = new Intent(ClientTakeRideSearchActivity.this, MenuMainActivity.class);
                question.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(question);

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

        mDriverListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < MAXSIZE; i++) {
                // for listview 要用的資料
                DriverReportPriceListItem item = new DriverReportPriceListItem();

                if(i%2==0) {
                    item.driver_name = "王小明";
                    item.title = "車神";
                    item.price = "報價：1000元";
                    item.value = "5";
                }else{
                    item.driver_name = "陳大明";
                    item.title = "車爵";
                    item.price = "報價：1050元";
                    item.value = "5";
                }

                mDriverListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(ClientTakeRideSearchActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
