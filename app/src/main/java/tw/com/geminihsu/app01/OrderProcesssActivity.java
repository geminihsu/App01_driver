package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class OrderProcesssActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_CANCEL = 0x0001;

    private TextView information;
    private ImageView detail;
    private ImageView departure;
    private ImageView destination;
    private ImageView change_money;
    private ImageView passenger_status;
    private ImageView done_order;
    private ImageView online_payment;
    private ImageView online_check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_processs_activity);
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
               // choice = bundle.getInt(Constants.ARG_POSITION);
               // displayLayout();
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
        information = (TextView) findViewById(R.id.txt_pannel_info);


        detail = (ImageView) findViewById(R.id.detail);
        departure = (ImageView) findViewById(R.id.navigation);
        destination = (ImageView) findViewById(R.id.navigation_destination);
        change_money = (ImageView) findViewById(R.id.order_change);
        passenger_status = (ImageView) findViewById(R.id.passeger_status);
        done_order = (ImageView) findViewById(R.id.order_finish);
        online_payment = (ImageView) findViewById(R.id.order_online_payment);
        online_check = (ImageView) findViewById(R.id.order_online_check);
    }




    private void setLister()
    {
        information.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
            question.putExtras(b);
            startActivity(question);
        }
    });
        detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(OrderProcesssActivity.this, MerchandiseOrderActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.QUERY_MERCHANDISE);
                question.putExtras(b);
                startActivity(question);
            }
        });
        departure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });
        change_money.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });
        passenger_status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderProcesssActivity.this);
                // set title
                alertDialogBuilder.setTitle(getString(R.string.dialog_get_on_car));

                // set dialog message
                alertDialogBuilder
                        .setMessage(getString(R.string.dialog_get_on_car_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_get_on_car_comfirm),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity

                                Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                                Bundle b = new Bundle();
                                b.putInt(Constants.ARG_POSITION, Constants.CANCEL_ORDER_FEEDBACK);
                                question.putExtras(b);
                                startActivity(question);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
        done_order.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
            question.putExtras(b);
            startActivity(question);
        }
    });
        online_payment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });
        online_check.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
            question.putExtras(b);
            startActivity(question);
        }
    });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_CANCEL, Menu.NONE, getString(R.string.menu_cancel_order));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_CANCEL:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle(getString(R.string.menu_cancel_order));

                // set dialog message
                alertDialogBuilder
                        .setMessage(getString(R.string.menu_cancel_order_warn))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.menu_sure_cancel_order),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity

                                Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                                Bundle b = new Bundle();
                                b.putInt(Constants.ARG_POSITION, Constants.CANCEL_ORDER_FEEDBACK);
                                question.putExtras(b);
                                startActivity(question);
                            }
                        })
                        .setNegativeButton(getString(R.string.menu_give_up_cancel_order),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
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

}
