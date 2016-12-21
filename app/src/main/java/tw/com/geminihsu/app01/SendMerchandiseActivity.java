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

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.bean.OrderLocationBean;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class SendMerchandiseActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    final public static int CLIENT_SEND_MERCHANDISE = 1;
    private LinearLayout target;

    private int choice = 0;
    private NormalOrder order;
    private JsonPutsUtil sendDataRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_merchandise_order);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(SendMerchandiseActivity.this);
        sendDataRequest.setServerRequestOrderManagerCallBackFunction(new JsonPutsUtil.ServerRequestOrderManagerCallBackFunction() {

            @Override
            public void createNormalOrder(NormalOrder order) {

                Intent question = new Intent(SendMerchandiseActivity.this, ClientTakeRideSearchActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, ClientTakeRideSearchActivity.DRIVER_REPORT_PRICE);
                question.putExtras(b);
                startActivity(question);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                choice = bundle.getInt(Constants.ARG_POSITION);
                order = (NormalOrder)bundle.getSerializable(ClientTakeRideActivity.BUNDLE_ORDER_TICKET_ID);

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
       target = (LinearLayout) findViewById(R.id.txt_target);

    }




    private void setLister()
    {

    }

    private void displayLayout() {
        if (choice == CLIENT_SEND_MERCHANDISE) {
            target.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_take));
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set title
                alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

                // set dialog message
                alertDialogBuilder
                        .setMessage(order.getOrderdate()+"\n從:台中火車站\n停:繼光街口\n到:台中市政府")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendDataRequest.putCreateNormalOrder(order);
                                //createMechardiseOrder(1);

                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createMechardiseOrder(String target)
    {
        Utility info = new Utility(SendMerchandiseActivity.this);

        AccountInfo driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude("24.09133");
        begin_location.setLongitude("120.540315");
        begin_location.setZipcode("404");
        begin_location.setAddress("台中市北區市政路172號");

        OrderLocationBean stop_location = new OrderLocationBean();
        stop_location.setId(2);
        stop_location.setLatitude("24.14411");
        stop_location.setLongitude("120.679567");
        stop_location.setZipcode("400");
        stop_location.setAddress("台中市中區柳川里成功路300號");


        OrderLocationBean end_location = new OrderLocationBean();
        end_location.setId(3);
        end_location.setLatitude("24.14411");
        end_location.setLongitude("120.679567");
        end_location.setZipcode("400");
        end_location.setAddress("台中市南區興大路145號");



        NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());
        order.setBegin(begin_location);
        order.setEnd(end_location);
        order.setDtype("4");
        order.setBegin_address(begin_location.getAddress());
        order.setStop_address(stop_location.getAddress());
        order.setEnd_address(end_location.getAddress());
        order.setPrice("1000");
        order.setTip("0");
        order.setTicket_status("0");
        //order.setOrderdate(time.getText().toString());
        order.setTarget(target);

        //sendDataRequest.putCreateNormalOrder(order);

    }

}
