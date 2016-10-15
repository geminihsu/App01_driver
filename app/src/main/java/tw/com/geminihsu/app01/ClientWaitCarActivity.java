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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.adapter.CommentListItem;
import tw.com.geminihsu.app01.adapter.CommentListItemAdapter;
import tw.com.geminihsu.app01.adapter.DriverReportPriceListItem;
import tw.com.geminihsu.app01.adapter.DriverReportPriceListItemAdapter;
import tw.com.geminihsu.app01.adapter.RecommendListItem;
import tw.com.geminihsu.app01.adapter.RecommendListItemAdapter;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class ClientWaitCarActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private ImageView checkdriver;
    private Button checklocation;
    private Button shareTakeInfo;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_order_car_wait_for_driver);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();

        this.setLister();




    }

    private void findViews()
    {
        checkdriver = (ImageView) findViewById(R.id.name);
        checklocation = (Button) findViewById(R.id.location);
        shareTakeInfo = (Button) findViewById(R.id.share);
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
                Intent question = new Intent(ClientWaitCarActivity.this, MapsActivity.class);
                startActivity(question);
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
                                Intent page = new Intent(ClientWaitCarActivity.this, BookmarksMapListActivity.class);
                                startActivity(page);
                                break;
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

}
