package tw.com.geminihsu.app01;


import android.app.Activity;
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
import android.widget.TextView;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class MerchandiseOrderActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_TAKE = 0x0001;

    private LinearLayout linearLayout_merchandise;
    private LinearLayout linearLayout_send_merchandise;
    private LinearLayout linearLayout_passenger;


    private TextView driver_comment;
    private TextView passenger_comment;
    private TextView insurance_info;
    private TextView merchandise_restrict;
    private TextView merchandise_count;
    private TextView merchandise_car_requirement;
    private TextView merchandise_car_requirement_spec;
    private TextView merchandise_title;

    private TextView passeger_name;
    private TextView passenger_address;
    private TextView passenger_address_destination;


    final public static int MERCHANDISE = 0;
    final public static int SEND_MERCHANDISE = 1;
    final public static int PASSENGER = 2;
    final public static int CLIENT_SEND_MERCHANDISE = 3;

    private int choice = 0;
    private NormalOrder order;

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
                order = (NormalOrder)bundle.getSerializable(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET);
               if(order==null) {
                   String ticket_id = bundle.getString(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET_ID);
                   RealmUtil info = new RealmUtil(MerchandiseOrderActivity.this);
                   order = info.queryOrder(Constants.ORDER_TICKET_ID, ticket_id);
               }
                Utility user = new Utility(this);
                AccountInfo userInfo = user.getAccountInfo();
                passeger_name.setText(userInfo.getName());
                passenger_address.setText(order.getBegin_address());
                passenger_address_destination.setText(order.getEnd_address());
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
        linearLayout_send_merchandise= (LinearLayout) findViewById(R.id.send_merchandise_info);
        linearLayout_passenger = (LinearLayout) findViewById(R.id.passeger_info);
        driver_comment = (TextView) findViewById(R.id.comment);
        passenger_comment = (TextView) findViewById(R.id.passeger_comment);
        insurance_info = (TextView) findViewById(R.id.insurance_inform);
        merchandise_count = (TextView) findViewById(R.id.send_merchandise_count);
        merchandise_restrict = (TextView) findViewById(R.id.send_merchandise_restrict);
        merchandise_car_requirement = (TextView) findViewById(R.id.send_content);
        merchandise_car_requirement_spec = (TextView) findViewById(R.id.send_merchandise_content);
        merchandise_title= (TextView) findViewById(R.id.txt_info);

        passeger_name = (TextView)findViewById(R.id.passeger_name);
        passenger_address = (TextView)findViewById(R.id.passenger_address);

        passenger_address_destination = (TextView)findViewById(R.id.passenger_address_destination);

    }



    private void displayLayout()
    {
        if(choice == MERCHANDISE)
        {
            linearLayout_merchandise.setVisibility(View.VISIBLE);
            getActionBar().setTitle(getString(R.string.merchandise_page_title));

        }else if(choice == SEND_MERCHANDISE)
        {
            linearLayout_send_merchandise.setVisibility(View.VISIBLE);
            getActionBar().setTitle(getString(R.string.client_send_merchandise_title));

        }else if(choice == CLIENT_SEND_MERCHANDISE)
        {
            merchandise_title.setVisibility(View.VISIBLE);
            linearLayout_send_merchandise.setVisibility(View.VISIBLE);
            merchandise_car_requirement.setText(getString(R.string.merchandise_car_requirement));
            merchandise_car_requirement_spec.setText("20呎");
            getActionBar().setTitle(getString(R.string.client_send_merchandise_title));

        }
        else if(choice == PASSENGER)
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

        merchandise_count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(MerchandiseOrderActivity.this, MerchandiseUnitcalculatorActivity.class);
                startActivity(question);
            }
        });
        insurance_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(MerchandiseOrderActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.INSURANCE_INFO);
                question.putExtras(b);
                startActivity(question);
            }
        });
        merchandise_restrict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(MerchandiseOrderActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.MERCHANDISE_RESTRICT);
                question.putExtras(b);
                startActivity(question);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(choice ==SEND_MERCHANDISE || choice == CLIENT_SEND_MERCHANDISE)
        {
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_TAKE, Menu.NONE, getString(R.string.sure_ok));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }else
        {
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_TAKE, Menu.NONE, getString(R.string.take_order));
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

            case ACTIONBAR_MENU_ITEM_TAKE:
                if(choice != PASSENGER) {
                    Intent question = new Intent(MerchandiseOrderActivity.this, SendMerchandiseActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.ARG_POSITION, SendMerchandiseActivity.CLIENT_SEND_MERCHANDISE);
                    b.putSerializable(ClientTakeRideActivity.BUNDLE_ORDER_TICKET_ID, order);
                    question.putExtras(b);
                    startActivity(question);
                }else
                    finish();
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
