package tw.com.geminihsu.app01;


import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01.service.App01Service;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class OrderProcesssActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_CANCEL = 0x0001;

    final public static int MERCHANDISE = 1;
    final public static int PASSENGER = 2;

    private LinearLayout linearLayout_sender;
    private LinearLayout linearLayout_receiver;
    private LinearLayout linearLayout_change_price;
    private LinearLayout linearLayout_online_check_price;
    private LinearLayout linearLayout_call_panel_merchandise;
    private LinearLayout linearLayout_call_panel_client;

    private LinearLayout linearLayout_stop_address;

    private TextView information;
    private TextView send_method;
    private TextView date;
    private TextView payment;
    private TextView from;
    private TextView stop;
    private TextView location;

    private ImageView detail;
    private ImageView departure;
    private ImageView destination;
    private ImageView change_money;
    private ImageView passenger_status;
    private ImageView done_order;
    private ImageView online_payment;
    private ImageView online_check;

    private ImageButton phone;


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
            option = bundle.getInt(Constants.ARG_POSITION);
            ticket_id = bundle.getString(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET_ID);
            order = (NormalOrder) bundle.getSerializable(Fragment_BeginOrderList.BUNDLE_ORDER_TICKET);

            if (order == null) {
                RealmUtil info = new RealmUtil(OrderProcesssActivity.this);
                order = info.queryOrder(Constants.ORDER_TICKET_ID, ticket_id);
            }
            displayLayout();
        } else {
            //Error!!!!
        }


    }

    private void findViews() {
        information = (TextView) findViewById(R.id.txt_pannel_info);
        send_method = (TextView) findViewById(R.id.send_method);
        payment = (TextView) findViewById(R.id.payment);
        from = (TextView) findViewById(R.id.txt_from);
        location = (TextView) findViewById(R.id.txt_dest);
        stop = (TextView) findViewById(R.id.txt_stop);
        date = (TextView) findViewById(R.id.date);

        linearLayout_sender = (LinearLayout) findViewById(R.id.sender);
        linearLayout_receiver = (LinearLayout) findViewById(R.id.receiver);
        linearLayout_change_price = (LinearLayout) findViewById(R.id.online_check_price);
        linearLayout_online_check_price = (LinearLayout) findViewById(R.id.change_price);
        linearLayout_call_panel_merchandise = (LinearLayout) findViewById(R.id.phone_merchandise);
        linearLayout_call_panel_client = (LinearLayout) findViewById(R.id.phone_client);

        linearLayout_stop_address = (LinearLayout) findViewById(R.id.stop_information);

        phone = (ImageButton) findViewById(R.id.call);

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
        //if(option == PASSENGER)
        // {
        String driver_type = order.getDtype();
        String cargo_type = order.getCargo_type();
        Constants.APP_REGISTER_ORDER_TYPE orderCargoType;

        Constants.APP_REGISTER_DRIVER_TYPE driverType;


        orderCargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargo_type));
        driverType = Constants.conversion_register_driver_account_result(Integer.valueOf(driver_type));


        String take_ride_order_type ="一般載客";

        if (orderCargoType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)
            take_ride_order_type+=":車站接送";
        else if (orderCargoType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)
            take_ride_order_type+=":機場接送";
        if (orderCargoType != Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
            send_method.setText(take_ride_order_type);

        if (driverType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI)
            payment.setText("跳表計費");
        else
            payment.setText("價格:" + order.getPrice() + "元\t小費" +
                    ":" + order.getTip() + "元");
        date.setText("即時");
        from.setText(order.getBegin_address());

        Log.e("",order.getStop_address());
        if ((order.getStop_address().equals("0"))||(order.getStop_address().equals(""))) {
            linearLayout_stop_address.setVisibility(View.GONE);
        }else
        {
            linearLayout_stop_address.setVisibility(View.VISIBLE);
            stop.setText(order.getStop_address());
        }
        location.setText(order.getEnd_address());
        linearLayout_change_price.setVisibility(View.GONE);
        linearLayout_online_check_price.setVisibility(View.GONE);
        linearLayout_sender.setVisibility(View.GONE);
        linearLayout_receiver.setVisibility(View.GONE);
        linearLayout_call_panel_merchandise.setVisibility(View.GONE);
        linearLayout_call_panel_client.setVisibility(View.VISIBLE);
        // }
    }


    private void setLister() {
        information.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (order.isValid()) {
                    /*ticket_id = order.getTicket_id();
                    //final String cargo_type = order.getCargo_type();
                    //final Constants.APP_REGISTER_ORDER_TYPE[] orderCargoType = new Constants.APP_REGISTER_ORDER_TYPE[1];

                    Intent question = new Intent(OrderProcesssActivity.this, MerchandiseOrderActivity.class);
                    Bundle b = new Bundle();

                    //orderCargoType[0] = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargo_type));

                    //if (orderCargoType[0] != Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
                    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
                    //else
                    //    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.MERCHANDISE);
                    b.putString(MerchandiseOrderActivity.BUNDLE_ORDER_TICKET_ID, ticket_id);
                    //b.putSerializable(BUNDLE_ORDER_TICKET,orderItem.order);

                    question.putExtras(b);
                    startActivity(question);*/
                    orderDetail(order);
                }
            }
        });
        detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (order.isValid()) {
                    /*ticket_id = order.getTicket_id();
                    //final String cargo_type = order.getCargo_type();
                    //final Constants.APP_REGISTER_ORDER_TYPE[] orderCargoType = new Constants.APP_REGISTER_ORDER_TYPE[1];

                    Intent question = new Intent(OrderProcesssActivity.this, MerchandiseOrderActivity.class);
                    Bundle b = new Bundle();

                    //orderCargoType[0] = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargo_type));

                    //if (orderCargoType[0] != Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
                    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
                    //else
                    //    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.MERCHANDISE);
                    b.putString(MerchandiseOrderActivity.BUNDLE_ORDER_TICKET_ID, ticket_id);
                    //b.putSerializable(BUNDLE_ORDER_TICKET,orderItem.order);

                    question.putExtras(b);
                    startActivity(question);*/
                    orderDetail(order);
                }
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
                TextView content = (TextView) dialog.findViewById(R.id.txt_msg);
                content.setVisibility(View.VISIBLE);
                EditText enter = (EditText) dialog.findViewById(R.id.editText_password);
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
                        .setPositiveButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runtime_permissions()) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getUser_name()));

                    if (ActivityCompat.checkSelfPermission(OrderProcesssActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
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

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE, android.Manifest.permission.CALL_PHONE},100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getUser_name()));

                if (ActivityCompat.checkSelfPermission(OrderProcesssActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }else {
                runtime_permissions();
            }
        }
    }

    private void orderDetail(NormalOrder order)
    {
        String departure = "從:"+order.getBegin_address()+"\n";
        String stop = "";
        String spec = "";
        if(!order.getStop_address().equals("0"))
            stop = "停靠："+order.getStop_address()+"\n";
        String destination ="到："+ order.getEnd_address()+"\n";
        String orderType = "時間：即時"+"\n";
        if(order.getCar_special()!=null)
            spec ="特殊需求："+order.getCar_special();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                OrderProcesssActivity.this);

        // set title
        alertDialogBuilder.setTitle(getString(R.string.order_info));

        // set dialog message
        alertDialogBuilder
                .setMessage("客戶電話:"+order.getUser_name()+"\n"+departure+stop+destination+orderType+spec)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sure_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
