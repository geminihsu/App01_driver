package tw.com.geminihsu.app01;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class MerchandiseOrderActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_TAKE = 0x0001;

    private LinearLayout linearLayout_merchandise;
    private LinearLayout linearLayout_passenger;


    private TextView driver_comment;
    private TextView passenger_comment;

    final public static int MERCHANDISE = 0;

    final public static int PASSENGER = 1;

    private int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchandise_order_information_activity);
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
        linearLayout_merchandise = (LinearLayout) findViewById(R.id.merchandise_info);

        linearLayout_passenger = (LinearLayout) findViewById(R.id.passeger_info);
        driver_comment = (TextView) findViewById(R.id.comment);
        passenger_comment = (TextView) findViewById(R.id.passeger_comment);
    }



    private void displayLayout()
    {
        if(choice == MERCHANDISE)
        {
            linearLayout_merchandise.setVisibility(View.VISIBLE);
            getActionBar().setTitle(getString(R.string.merchandise_page_title));

        }else if(choice == PASSENGER)
        {
            linearLayout_passenger.setVisibility(View.VISIBLE);
            getActionBar().setTitle(getString(R.string.passenger_page_title));

        }
    }

    private void setLister()
    {
        driver_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(MerchandiseOrderActivity.this, CommentActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });

        passenger_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(MerchandiseOrderActivity.this, CommentActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_TAKE, Menu.NONE, getString(R.string.take_order));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_TAKE:
                //將表單資料送出後回到主畫面
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

}
