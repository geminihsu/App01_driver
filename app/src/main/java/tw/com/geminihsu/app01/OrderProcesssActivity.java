package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class OrderProcesssActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_CANCEL = 0x0001;

    final public static int MERCHANDISE = 1;
    final public static int PASSENGER= 2;

    private LinearLayout linearLayout_sender;
    private LinearLayout linearLayout_receiver;
    private LinearLayout linearLayout_change_price;
    private LinearLayout linearLayout_online_check_price;
    private LinearLayout linearLayout_call_panel_merchandise;
    private LinearLayout linearLayout_call_panel_client;

    private TextView information;
    private TextView send_method;
    private TextView date;
    private TextView payment;
    private TextView from;
    private TextView location;

    private ImageView detail;
    private ImageView departure;
    private ImageView destination;
    private ImageView change_money;
    private ImageView passenger_status;
    private ImageView done_order;
    private ImageView online_payment;
    private ImageView online_check;


    private String ticket_id;
    private NormalOrder order;
    private JsonPutsUtil sendDataRequest;
    private int option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_processs_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(this);
        sendDataRequest.setDriverRequestFinishOrderManagerCallBackFunction(new JsonPutsUtil.DriverRequestFinishOrderManagerCallBackFunction() {


            @Override
            public void driverFinishOrder(NormalOrder order) {
                finishOrder();

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
            option=bundle.getInt(Constants.ARG_POSITION);
            ticket_id = bundle.getString(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET_ID);
            order = (NormalOrder)bundle.getSerializable(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET);

            if(order==null) {
                RealmUtil info = new RealmUtil(OrderProcesssActivity.this);
                order = info.queryOrder(Constants.ORDER_TICKET_ID, ticket_id);
            }
            displayLayout();
        }
        else
        {
            //Error!!!!
        }





    }

    private void findViews()
    {
        information = (TextView) findViewById(R.id.txt_pannel_info);
        send_method = (TextView) findViewById(R.id.send_method);
        payment = (TextView) findViewById(R.id.payment);
        from  = (TextView) findViewById(R.id.txt_from);
        location = (TextView) findViewById(R.id.txt_dest);
        date = (TextView) findViewById(R.id.date);

        linearLayout_sender = (LinearLayout) findViewById(R.id.sender);
        linearLayout_receiver = (LinearLayout) findViewById(R.id.receiver);
        linearLayout_change_price = (LinearLayout) findViewById(R.id.online_check_price);
        linearLayout_online_check_price = (LinearLayout) findViewById(R.id.change_price);
        linearLayout_call_panel_merchandise= (LinearLayout) findViewById(R.id.phone_merchandise);
        linearLayout_call_panel_client= (LinearLayout) findViewById(R.id.phone_client);


        detail = (ImageView) findViewById(R.id.detail);
        departure = (ImageView) findViewById(R.id.navigation);
        destination = (ImageView) findViewById(R.id.navigation_destination);
        change_money = (ImageView) findViewById(R.id.order_change);
        passenger_status = (ImageView) findViewById(R.id.passeger_status);
        done_order = (ImageView) findViewById(R.id.order_finish);
        online_payment = (ImageView) findViewById(R.id.order_online_payment);
        online_check = (ImageView) findViewById(R.id.order_online_check);
    }

    private void displayLayout() {
        if(option == PASSENGER)
        {
            send_method.setText("一般載客");
            if(order.getDtype().equals("1"))
                payment.setText("跳表計費");
            else
            payment.setText("價格:"+order.getPrice()+"元\t小費" +
                    ":"+order.getTip()+"元");
            date.setText("即時");
            from.setText(order.getBegin_address());
            location.setText(order.getEnd_address());
            linearLayout_change_price.setVisibility(View.GONE);
            linearLayout_online_check_price.setVisibility(View.GONE);
            linearLayout_sender.setVisibility(View.GONE);
            linearLayout_receiver.setVisibility(View.GONE);
            linearLayout_call_panel_merchandise.setVisibility(View.GONE);
            linearLayout_call_panel_client.setVisibility(View.VISIBLE);
        }
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
                Intent question = new Intent(OrderProcesssActivity.this, MapsActivity.class);
                startActivity(question);
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(OrderProcesssActivity.this, MapsActivity.class);
                startActivity(question);
            }
        });
        change_money.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(OrderProcesssActivity.this);
                dialog.setContentView(R.layout.dialog_enter_change_price_layout);
                dialog.setTitle(getString(R.string.order_change_fee));
                TextView content = (TextView)dialog.findViewById(R.id.txt_msg);
                content.setVisibility(View.VISIBLE);
                EditText enter=(EditText)dialog.findViewById(R.id.editText_password);
                enter.setText("1000");
                Button sure = (Button) dialog.findViewById(R.id.sure_action);
                sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent question = new Intent(OrderProcesssActivity.this, SupportAnswerActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.REPORT_PRICE);
                        question.putExtras(b);
                        startActivity(question);
                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel_action);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
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

                                Intent question = new Intent(OrderProcesssActivity.this, DriverCommentActivity.class);
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

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderProcesssActivity.this);
            // set title
            alertDialogBuilder.setTitle(getString(R.string.order_finish_make_sure_title));

            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.order_finish_make_sure_content))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.order_finish_make_sure_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            sendDataRequest.driverFinishOrder(order);
                        }
                    })
                    .setNegativeButton(getString(R.string.order_finish_make_sure_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();



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
            b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.ONLINE_CHECK);
            question.putExtras(b);
            startActivity(question);
        }
    });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_CANCEL, Menu.NONE, getString(R.string.menu_cancel_order));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

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

    public void finishOrder()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.order_finish_title));

            alertDialogBuilder
                    .setMessage(getString(R.string.order_finish_ok))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.order_finish_make_sure_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent question = new Intent(OrderProcesssActivity.this, MenuMainActivity.class);

                            startActivity(question);
                            finish();
                        }
                    });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


}
