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
import android.widget.ImageView;
import android.widget.RatingBar;

import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class DriverCommentActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    private ImageView driver_info;
    private JsonPutsUtil sendDataRequest;
    private String ticket_id;
    private RatingBar score;
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();

        this.setLister();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            ticket_id = bundle.getString(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET_ID);
        }



        }

    private void findViews()
    {
        driver_info = (ImageView)findViewById(R.id.image);
        score = (RatingBar) findViewById(R.id.rating_level);
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
                doneAlert();
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
                .setMessage(getString(R.string.dialog_finish_order_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_finish_order_comfirm),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        RealmUtil info = new RealmUtil(DriverCommentActivity.this);
                        NormalOrder order =info.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
                        sendDataRequest.commentOrder(order,(int)score.getRating());
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}
