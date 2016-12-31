package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class ClientWaitCarActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private ImageView checkdriver;
    private Button checklocation;
    private Button shareTakeInfo;
    private TextView fee;


    public static final String LINE_PACKAGE_NAME = "jp.naver.line.android";
    public static final String LINE_CLASS_NAME = "jp.naver.line.android.activity.selectchat.SelectChatActivity";

    private BroadcastReceiver finishOrderBroadcastReceiver;

    private NormalOrder order;
    private String driverUid;

    private JsonPutsUtil sendDataRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_order_car_wait_for_driver);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        finishOrderBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Intent question = new Intent(ClientWaitCarActivity.this, MenuMainActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        };

        sendDataRequest.setAccountQueryUserLocationManagerCallBackFunction(new JsonPutsUtil.AccountQueryUserLocationManagerCallBackFunction() {


            @Override
            public void getLocationInfo(Double lat, Double lng) {
                Intent map = new Intent(ClientWaitCarActivity.this, MapsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.DISPLAY_USER_LOCATION);
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                map.putExtras(b);
                startActivity(map);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        sendDataRequest = new JsonPutsUtil(this);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ORDER_TICKET_ID)) {
                String ticket_id = bundle.getString(Constants.ORDER_TICKET_ID);
                driverUid = bundle.getString(Constants.ACCOUNT_DRIVER_UID);
                RealmUtil database = new RealmUtil(this);
                order = database.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
                fee.setText("小費:\t"+order.getTip()+"元");
            }
        }
         this.setLister();
        if(finishOrderBroadcastReceiver!=null)
            registerReceiver((finishOrderBroadcastReceiver), new IntentFilter("finish_order"));



    }

    private void findViews()
    {
        checkdriver = (ImageView) findViewById(R.id.name);
        checklocation = (Button) findViewById(R.id.location);
        shareTakeInfo = (Button) findViewById(R.id.share);

        fee = (TextView) findViewById(R.id.fee);

    }


    private void setLister()
    {
        checkdriver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(ClientWaitCarActivity.this, DriverAccountActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });

        checklocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Utility info = new Utility(ClientWaitCarActivity.this);
                //AccountInfo accountInfo = info.getAccountInfo();

                sendDataRequest.getUserLocation(order,Integer.valueOf(driverUid));
                /*Intent question = new Intent(ClientWaitCarActivity.this, MapsActivity.class);
                startActivity(question);*/
            }
        });

        shareTakeInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ClientWaitCarActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ClientWaitCarActivity.this,
                android.R.layout.select_dialog_item);
        arrayAdapter.add(getString(R.string.txt_share_sms));
        arrayAdapter.add(getString(R.string.txt_share_line));
        arrayAdapter.add(getString(R.string.txt_share_email));


                builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                        switch (which){
                            case 0:
                                Intent question = new Intent(ClientWaitCarActivity.this, SupportAnswerActivity.class);
                                Bundle b = new Bundle();
                                b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.SMS_REPORT);
                                question.putExtras(b);
                                startActivity(question);
                                break;
                            case 1:
//                                Intent page = new Intent(ClientWaitCarActivity.this, BookmarksMapListActivity.class);
//                                startActivity(page);
                                //發送訊息到line
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setClassName(LINE_PACKAGE_NAME, LINE_CLASS_NAME);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, "我在2015年12月15日上午09:00搭乘車牌EHE-530車輛，從台中火車站前往台中市政府。");
                                startActivity(intent);

                                break;
                            case 2:
                                //發送訊息到email
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("message/rfc822");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"abc@email.com"});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "我在搭車");
                                emailIntent.putExtra(Intent.EXTRA_TEXT   , "我在2015年12月15日上午09:00搭乘車牌EHE-530車輛，從台中火車站前往台中市政府。");

                                try {
                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                    finish();
                                    //Log.i("Finished sending email...", "");
                                }
                                catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(ClientWaitCarActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
        builderSingle.show();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.txt_cancel_order));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                Intent question = new Intent(ClientWaitCarActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CANCEL_ORDER_FEEDBACK);
                question.putExtras(b);
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
    @Override
    protected void onStop()
    {
        super.onStop();
        if (finishOrderBroadcastReceiver != null&&finishOrderBroadcastReceiver.isOrderedBroadcast()){
            unregisterReceiver(finishOrderBroadcastReceiver);
            finishOrderBroadcastReceiver=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (finishOrderBroadcastReceiver != null&&finishOrderBroadcastReceiver.isOrderedBroadcast()){
            unregisterReceiver(finishOrderBroadcastReceiver);
            finishOrderBroadcastReceiver=null;
        }
    }
}
