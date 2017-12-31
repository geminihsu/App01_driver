package tw.com.geminihsu.app01Client;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.Utility;

public class DriverCommentActivity extends Activity {
    final public static int CLIENT_COMMENT = 1;
    final public static int DRIVER_COMMENT = 2;
    public final static String BUNDLE_CLIENT = "client";// from

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    private ImageView driver_info;
    private JsonPutsUtil sendDataRequest;
    private String ticket_id;
    private RatingBar score;
    private TextView customer_name;

    private LinearLayout linearLayout_client_info;
    private LinearLayout linearLayout_client_comment;

    private NormalOrder order;

    private int client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_comment);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(this);
        sendDataRequest.setDriverRequestClientCommentManagerCallBackFunction(new JsonPutsUtil.DriverRequestClientCommentManagerCallBackFunction() {


            @Override
            public void driverAskComment(NormalOrder order) {
                Intent question = new Intent(DriverCommentActivity.this, MenuMainActivity.class);
                startActivity(question);
            }
        });


        this.findViews();

        this.setLister();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            ticket_id = bundle.getString(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET_ID);
            RealmUtil info = new RealmUtil(DriverCommentActivity.this);
            order = info.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
            if (bundle.containsKey(BUNDLE_CLIENT)) {
                //判斷是客戶評分還是司機評分
                client = bundle.getInt(BUNDLE_CLIENT);
                if (client == CLIENT_COMMENT)
                {
                    setTitle(getString(R.string.client_comment));
                    Utility user = new Utility(this);
                    AccountInfo userInfo = user.getAccountInfo();
                    customer_name.setText(userInfo.getName());
                    driver_info.setVisibility(View.GONE);
                    linearLayout_client_info.setVisibility(View.VISIBLE);
                    linearLayout_client_comment.setVisibility(View.VISIBLE);
                }

            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();



        }

    private void findViews()
    {
        driver_info = (ImageView)findViewById(R.id.image);
        score = (RatingBar) findViewById(R.id.rating_level);
        customer_name = (TextView) findViewById(R.id.customer_name);

        linearLayout_client_info = (LinearLayout)findViewById(R.id.customer_info);
        linearLayout_client_comment = (LinearLayout)findViewById(R.id.customer_change);
    }



     private void setLister()
    {
        driver_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(DriverCommentActivity.this, DriverAccountActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, DriverAccountActivity.DRIVER_COMMENT);
                question.putExtras(b);
                startActivity(question);
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                if (client == CLIENT_COMMENT)
                {
                    Utility info = new Utility(this);
                    AccountInfo accountInfo = info.getAccountInfo();
                    sendDataRequest.commentOrder(order,accountInfo,(int)score.getRating());

                }else
                {
                    doneAlert();
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

    private void doneAlert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DriverCommentActivity.this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.dialog_finish_order));

        // set dialog messagex
        alertDialogBuilder
                .setMessage("完成訂單時間"+order.getOrderdate()+"，恭喜您獲得績分200點")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_finish_order_comfirm),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       // RealmUtil info = new RealmUtil(DriverCommentActivity.this);
                       // NormalOrder order =info.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
                        Utility info = new Utility(DriverCommentActivity.this);
                        AccountInfo accountInfo = info.getAccountInfo();
                        sendDataRequest.commentOrder(order,accountInfo,(int)score.getRating());
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}
