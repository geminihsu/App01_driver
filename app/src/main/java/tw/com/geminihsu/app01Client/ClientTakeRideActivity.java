package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01Client.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.LocationAddress;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.bean.OrderLocationBean;
import tw.com.geminihsu.app01Client.bean.USerBookmark;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.serverbean.ServerSpecial;
import tw.com.geminihsu.app01Client.utils.DateTimeUtil;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01Client.utils.Utility;

public class ClientTakeRideActivity extends Activity {
    private final String TAG = ClientTakeRideActivity.class.toString();
    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from
    public final static String BUNDLE_ORDER_DRIVER_TYPE = "dtype";// from
    public final static String BUNDLE_ORDER_CARGO_TYPE = "cargo_type";// from

    //public final static String BUNDLE_ORDER_CUR_LONGITUDE = "dtype";// from
    //public final static String BUNDLE_ORDER_CUR_LATITUDE = "cargo_type";// from


    public final static String BUNDLE_ORDER_CUR_ADDRESS = "address";// from


    public final static String BUNDLE_KEEP_BOOMARK = "add_bookmark";// from

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;


    final public static int TAKE_RIDE = 1;
    final public static int SEND_MERCHANDISE= 2;
    private LinearLayout linearLayout_date_picker;
    private LinearLayout change;
    private LinearLayout linearLayout_spec;
    private ImageButton btn_datePicker;
    private ImageButton btn_timerPicker;
    private ImageButton departure;
    private ImageButton stop;
    private ImageButton destination;
    private ImageButton spec;
    private RadioGroup radioGroup_type;
    private RadioButton realtime;
    private RadioButton reservation;
    private TextView show_title;
    private TextView spec_value;
    private EditText date;
    private EditText time;
    private EditText departure_address;
    private EditText stop_address;
    private EditText destination_address;
    private EditText remark;
    private EditText merchandise_content;
    private EditText merchandise_weight;
    private EditText merchandise_unit;
    private int option;

    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;

    private DatePickerDialog.OnDateSetListener datePicker;
    private TimePickerDialog.OnTimeSetListener timePicker;

    private Calendar calendar;

    private JsonPutsUtil sendDataRequest;
    private AccountInfo driver;

    private LocationAddress departure_detail;
    private LocationAddress stop_detail;
    private LocationAddress destination_detail;

    private int dType;//哪一種司機型態
    private int cargoType;//那一種訂單型態

    private Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private Constants.APP_REGISTER_ORDER_TYPE orderCargoType;

    private Dialog dialog;
    private long order_timeStamp;
    private ArrayList<ClientTakeRideSelectSpecListItem> spec_list;

    private ProgressDialog progressDialog_loading;

    private String currAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_take_ride);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(ClientTakeRideActivity.this);
        sendDataRequest.setServerRequestOrderManagerCallBackFunction(new JsonPutsUtil.ServerRequestOrderManagerCallBackFunction() {

            @Override
            public void createNormalOrder(NormalOrder order) {

                if(progressDialog_loading!=null)
                {
                    progressDialog_loading.cancel();
                    progressDialog_loading = null;
                }

                Intent intent = new Intent(getApplicationContext(), ClientTakeRideSearchActivity.class);

                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Integer.valueOf(order.getTicket_id()));
                intent.putExtras(b);
                startActivity(intent);
                //finish();
            }

            @Override
            public void cancelNormalOrder(NormalOrder order) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        this.findViews();
        this.setLister();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                option = bundle.getInt(Constants.ARG_POSITION);
                if (bundle.containsKey(Constants.NEW_ORDER_DTYPE)) {
                    dType = bundle.getInt(Constants.NEW_ORDER_DTYPE);
                    dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(dType));
                }
                if (bundle.containsKey(Constants.NEW_ORDER_CARGO_TYPE)) {
                    cargoType = bundle.getInt(Constants.NEW_ORDER_CARGO_TYPE);
                    orderCargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargoType));
                }

                if (bundle.containsKey(BUNDLE_ORDER_CUR_ADDRESS)) {
                    currAddress = bundle.getString(BUNDLE_ORDER_CUR_ADDRESS);
                    departure_address.setText(currAddress);
                }

                option = bundle.getInt(Constants.NEW_ORDER_CARGO_TYPE);
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


    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    private void findViews()
    {
        spec_list = new ArrayList<ClientTakeRideSelectSpecListItem>();
        change = (LinearLayout) findViewById (R.id.merchandise_content);
        linearLayout_date_picker = (LinearLayout) findViewById(R.id.date_layout);
        linearLayout_spec = (LinearLayout) findViewById(R.id.spec_require);

        btn_datePicker = (ImageButton) findViewById(R.id.date_picker);
        btn_timerPicker = (ImageButton) findViewById(R.id.time_picker);
        departure = (ImageButton) findViewById(R.id.departure);
        stop = (ImageButton) findViewById(R.id.stop);
        destination = (ImageButton) findViewById(R.id.destination);
        spec = (ImageButton) findViewById(R.id.spec_option);
        show_title = (TextView) findViewById(R.id.txt_info);
        spec_value = (TextView) findViewById(R.id.passenger_spec_value);

        radioGroup_type = (RadioGroup) findViewById(R.id.source);
        realtime = (RadioButton)findViewById(R.id.real_radio);
        reservation = (RadioButton) findViewById(R.id.reservation_radio);

        date = (EditText) findViewById(R.id.date_info);
        time = (EditText) findViewById(R.id.time_info);
        departure_address = (EditText) findViewById(R.id.departure_address);
        departure_address.setText(currAddress);
        stop_address = (EditText) findViewById(R.id.stop_address);
        remark = (EditText) findViewById(R.id.spec_info);
        destination_address = (EditText) findViewById(R.id.destination_address);
        merchandise_content = (EditText) findViewById(R.id.merchandise_content_info);
        merchandise_weight = (EditText) findViewById(R.id.merchandise_weight);
        merchandise_unit = (EditText) findViewById(R.id.merchandise_unit);


        Date dateIfo=new Date();
        time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo));
        //Log.e(TAG,new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(date));

    }


    private void displayLayout() {
        if (option == ClientTakeRideActivity.TAKE_RIDE) {
            getActionBar().setTitle(getString(R.string.client_take_ride_title));
            change.setVisibility(View.GONE);
        } else if (option == ClientTakeRideActivity.SEND_MERCHANDISE){
            show_title.setText(getString(R.string.txt_send_merchandise));
            getActionBar().setTitle(getString(R.string.client_send_merchandise_title));
            change.setVisibility(View.VISIBLE);
        }
    }
    private void setLister()
    {

        calendar = Calendar.getInstance();
        timePicker=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                time.setText(hourOfDay+ ":" +minute);
            }
        };
        btn_timerPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(ClientTakeRideActivity.this,
                        timePicker,
                        24,
                        59,
                        true).show();
            }
        });

        datePicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth+ "/" + (month+1) + "/" + year);
            }

        };
        btn_datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ClientTakeRideActivity.this, datePicker,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        radioGroup_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Date dateIfo=new Date();
                    if (checkedId == reservation.getId()) {
                        linearLayout_date_picker.setVisibility(View.VISIBLE);

                        String schedule = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo);
                        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateIfo));
                        time.setText(new SimpleDateFormat("HH:mm:ss").format(dateIfo));
                        order_timeStamp = DateTimeUtil.convertString_yyyymmddToMillisecondsTime(schedule);
                    } else {
                        linearLayout_date_picker.setVisibility(View.GONE);
                        time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo));
                        order_timeStamp = 0;

                    }
          }
        });

        departure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ClientTakeRideActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        ClientTakeRideActivity.this,
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pop_map_option1));
                arrayAdapter.add(getString(R.string.pop_map_option2));

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                switch (which){
                                    case 0:
                                        /*Intent map = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);*/
                                        Intent map = new Intent(ClientTakeRideActivity.this, OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(ClientTakeRideActivity.this, BookmarksMapListActivity.class);
                                        Bundle flag = new Bundle();
                                        flag.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_BOOKMARK);
                                        page.putExtras(flag);
                                        startActivityForResult(page,Constants.DEPARTURE_QUERY_BOOKMARK);

                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ClientTakeRideActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        ClientTakeRideActivity.this,
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pop_map_option1));
                arrayAdapter.add(getString(R.string.pop_map_option2));

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                switch (which){
                                    case 0:
                                        /*Intent map = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);*/
                                        Intent map = new Intent(ClientTakeRideActivity.this, OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.STOP_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.STOP_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(ClientTakeRideActivity.this, BookmarksMapListActivity.class);
                                        Bundle flag = new Bundle();
                                        flag.putInt(Constants.ARG_POSITION, Constants.STOP_QUERY_BOOKMARK);
                                        page.putExtras(flag);
                                        startActivityForResult(page,Constants.STOP_QUERY_BOOKMARK);

                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ClientTakeRideActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        ClientTakeRideActivity.this,
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pop_map_option1));
                arrayAdapter.add(getString(R.string.pop_map_option2));

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                switch (which){
                                    case 0:
                                        Intent map = new Intent(ClientTakeRideActivity.this, OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DESTINATION_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DESTINATION_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(ClientTakeRideActivity.this, BookmarksMapListActivity.class);
                                        Bundle flag = new Bundle();
                                        flag.putInt(Constants.ARG_POSITION, Constants.DESTINATION_QUERY_BOOKMARK);
                                        page.putExtras(flag);
                                        startActivityForResult(page,Constants.DESTINATION_QUERY_BOOKMARK);

                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        getDataFromDB();
        linearLayout_spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(ClientTakeRideActivity.this);
                dialog.setContentView(R.layout.client_take_ride_selectspec_requirement);
                dialog.setTitle(getString(R.string.txt_take_spec));
                Button cancel = (Button) dialog.findViewById(R.id.button_category_cancel);
                Button ok = (Button) dialog.findViewById(R.id.button_category_ok);

                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // set the custom dialog components - text, image and button
                ListView requirement = (ListView) dialog.findViewById(R.id.listViewDialog);

                requirement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        ClientTakeRideSelectSpecListItem item = mCommentListData.get(position);
                        item.check= !item.check;
                        mCommentListData.set(position,item);
                        listViewAdapter.notifyDataSetChanged();
                    }
                });

                if(listViewAdapter == null) {
                    //getDataFromDB();
                    listViewAdapter = new ClientTakeRideSelectSpecListItemAdapter(ClientTakeRideActivity.this, 0, mCommentListData);

                }
                requirement.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();

                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                String require;
                ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String require="";
                        for(ClientTakeRideSelectSpecListItem item:mCommentListData)
                        {
                            if(item.check)
                            {
                                spec_list.add(item);
                                require+=item.book_title+",";
                            }
                        }

                        if(!require.isEmpty())
                            require=require.substring(0,require.length()-1);
                        spec_value.setText(require);
                        dialog.cancel();
                    }
                });

            }
        });

        spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(ClientTakeRideActivity.this);
                dialog.setContentView(R.layout.client_take_ride_selectspec_requirement);
                dialog.setTitle(getString(R.string.txt_take_spec));
                Button cancel = (Button) dialog.findViewById(R.id.button_category_cancel);
                Button ok = (Button) dialog.findViewById(R.id.button_category_ok);

                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // set the custom dialog components - text, image and button
                ListView requirement = (ListView) dialog.findViewById(R.id.listViewDialog);

                requirement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        ClientTakeRideSelectSpecListItem item = mCommentListData.get(position);
                        item.check= !item.check;
                        mCommentListData.set(position,item);
                        listViewAdapter.notifyDataSetChanged();
                    }
                });
                getDataFromDB();
                listViewAdapter = new ClientTakeRideSelectSpecListItemAdapter(ClientTakeRideActivity.this, 0, mCommentListData);
                requirement.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();


                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                String require;
                ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String require="";
                        for(ClientTakeRideSelectSpecListItem item:mCommentListData)
                        {
                            if(item.check)
                            {
                                spec_list.add(item);
                                require+=item.book_title+",";
                            }
                        }
                        //require.substring(0,require.length()-2);
                        //require=require.substring(0,require.length()-1);
                        spec_value.setText(require);
                        dialog.cancel();
                    }
                });

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (option == ClientTakeRideActivity.TAKE_RIDE) {
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_take));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }else if (option == ClientTakeRideActivity.SEND_MERCHANDISE) {
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.sure_take_spec));
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

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                //if (option == ClientTakeRideActivity.TAKE_RIDE) {
               if(!departure_address.getText().toString().isEmpty())
                     sendOrder();
                else
                   alert();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        mCommentListData.clear();
        //Resources res =getResources();
        //String[] opt=res.getStringArray(R.array.client_take_ride_requirement);
        RealmUtil data = new RealmUtil(this);
        RealmResults<ServerSpecial> specials= data.queryServerSpecial(Constants.SERVER_SPECIAL,"1");
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < specials.size(); i++) {
                // for listview 要用的資料
                ClientTakeRideSelectSpecListItem item = new ClientTakeRideSelectSpecListItem();


                item.check=false;
                //if(specials.get(i).getDtype().equals(dType))
                item.spec_id = specials.get(i).getId();
                item.book_title =specials.get(i).getContent();
                mCommentListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(ClientTakeRideActivity.this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private NormalOrder createMechardiseOrder(String target)
    {
        Utility info = new Utility(ClientTakeRideActivity.this);
        driver = info.getAccountInfo();
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
        order.setOrderdate(time.getText().toString());
        order.setTarget(target);

        if (option == ClientTakeRideActivity.SEND_MERCHANDISE)
        {
            order.setRemark(merchandise_content.getText().toString()+","+merchandise_weight.getText().toString()+","+merchandise_unit.getText().toString());
        }
       //sendDataRequest.putCreateNormalOrder(order);
        return order;

    }

    private void createTaxiOrder(String target,String price,String tip)
    {

        Utility info = new Utility(ClientTakeRideActivity.this);
        driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude(""+departure_detail.getLatitude());
        begin_location.setLongitude(""+departure_detail.getLongitude());
        //String zip = departure_detail.getAddress().substring(0,3);
        begin_location.setZipcode(departure_detail.getZipCode());
        begin_location.setAddress(departure_detail.getAddress());

        OrderLocationBean stop_location = new OrderLocationBean();
        stop_location.setId(2);
        stop_location.setLatitude(""+stop_detail.getLatitude());
        stop_location.setLongitude(""+stop_detail.getLongitude());
        stop_location.setZipcode(stop_detail.getZipCode());
        stop_location.setAddress(stop_detail.getAddress());


        OrderLocationBean end_location = new OrderLocationBean();
        end_location.setId(3);
        end_location.setLatitude(""+destination_detail.getLatitude());
        end_location.setLongitude(""+destination_detail.getLongitude());
        //String zip1 = destination_detail.getAddress().substring(0,3);
        end_location.setZipcode(destination_detail.getZipCode());
        end_location.setAddress(destination_detail.getAddress());





        //long unixTime = System.currentTimeMillis() / 1000L;


        final NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());

        Log.e(TAG,"time stamp:"+time.getText().toString());
        order_timeStamp = DateTimeUtil.convertString_yyyymmddToMillisecondsTime(time.getText().toString());

        order.setTimebegin(""+order_timeStamp);
        order.setDtype(""+dataType.value());
        order.setCargo_type(""+orderCargoType.value());
        order.setBegin(begin_location);
        order.setEnd(end_location);
        order.setStop(stop_location);
        order.setBegin_address(begin_location.getAddress());
        order.setStop_address(stop_location.getAddress());
        order.setEnd_address(end_location.getAddress());
        order.setTicket_status("0");
        order.setOrderdate(time.getText().toString());
        order.setTarget(target);
        order.setRemark(remark.getText().toString());
        if(price.equals(""))
            price = "0";
        order.setPrice(price);
        order.setTip(tip);

        if (option == ClientTakeRideActivity.SEND_MERCHANDISE)
        {
            order.setRemark(merchandise_content.getText().toString()+","+merchandise_weight.getText().toString()+","+merchandise_unit.getText().toString());
        }

        String spec="";
       if(!spec_list.isEmpty())
        {
            for(ClientTakeRideSelectSpecListItem item:spec_list){
                spec+=item.spec_id+",";
            }
            spec = spec.substring(0,spec.length()-1);

        }
        Log.e(TAG,"spec car:"+spec);
        order.setCar_special(spec);

        //sendDataRequest.putCreateQuickTaxiOrder(order);
        if(progressDialog_loading==null) {
            progressDialog_loading = ProgressDialog.show(this, "",
                    "Loading. Please wait...", true);
        }
        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                sendDataRequest.putCreateNormalOrder(order);
            }
        }));


    }


    private void createTempTaxiOrder(String target)
    {

        Utility info = new Utility(ClientTakeRideActivity.this);
        driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude("24.09133");
        begin_location.setLongitude("120.540315");
        begin_location.setZipcode("404");
        begin_location.setAddress(departure_address.getText().toString());

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
        end_location.setAddress(destination_address.getText().toString());



        NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());
        order.setDtype("1");
        order.setBegin(begin_location);
        order.setEnd(end_location);
        order.setBegin_address(begin_location.getAddress());
        order.setStop_address(stop_location.getAddress());
        order.setEnd_address(end_location.getAddress());
        order.setTicket_status("0");
        order.setOrderdate(time.getText().toString());
        order.setTarget(target);

        sendDataRequest.putCreateQuickTaxiOrder(order);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.DEPARTURE_QUERY_GPS:
                ArrayList<Address> locationInfo=null;
                if(data!=null) {
                    //departure_detail = new LocationAddress();
                    departure_detail =(LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String add_bookmark = data.getStringExtra(BUNDLE_KEEP_BOOMARK);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //departure_detail.setLatitude(location.getLatitude());
                    //departure_detail.setLongitude(location.getLongitude());
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK,false);
                    if(isBookMark)
                    {
                        addUserLocationToBookMark(departure_detail);
                    }
                    departure_address.setText(departure_detail.getAddress());
                    //departure_detail.setLocation(location.getAddress());
                    //departure_detail.setAddress(location.getAddress());
                    /*if(add_bookmark.equals(true))
                    {
                        //新增到資料庫的BookMark
                    }*/
                    //showAddressList((ArrayList<Address>)locationInfo,departure_address,departure_detail);
                }
            break;
            case Constants.DESTINATION_QUERY_GPS:
                ArrayList<Address> locationInfo2=null;
                if(data!=null) {
                    //destination_detail = new LocationAddress();
                    destination_detail =(LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //destination_detail.setLatitude(Double.valueOf(latitude));
                    //destination_detail.setLongitude(Double.valueOf(longitude));
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK,false);
                    if(isBookMark)
                    {
                        addUserLocationToBookMark(destination_detail);
                    }
                    destination_address.setText(destination_detail.getAddress());
                    //destination_detail.setAddress(locationInfo2.get(0).getAddressLine(0));
                    //destination_detail.setLocation(locationInfo2.get(0).getAddressLine(0));
                    //showAddressList(locationInfo2,destination_address,destination_detail);
                }
            break;

            case Constants.STOP_QUERY_GPS:
                ArrayList<Address> locationInfo3=null;
                if(data!=null) {
                    //destination_detail = new LocationAddress();
                    stop_detail =(LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //destination_detail.setLatitude(Double.valueOf(latitude));
                    //destination_detail.setLongitude(Double.valueOf(longitude));
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK,false);
                    if(isBookMark)
                    {
                        addUserLocationToBookMark(stop_detail);
                    }
                    stop_address.setText(stop_detail.getAddress());
                    //destination_detail.setAddress(locationInfo2.get(0).getAddressLine(0));
                    //destination_detail.setLocation(locationInfo2.get(0).getAddressLine(0));
                    //showAddressList(locationInfo2,destination_address,destination_detail);
                }
                break;

            case Constants.DEPARTURE_QUERY_BOOKMARK:
                if (data!=null) {
                    departure_detail = new LocationAddress();
                    USerBookmark uSerBookmark = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    departure_address.setText(uSerBookmark.getLocation()+"("+uSerBookmark.getStreetAddress()+")");
                    departure_detail.setLongitude(Double.parseDouble(uSerBookmark.getLng()));
                    departure_detail.setLatitude(Double.parseDouble(uSerBookmark.getLat()));
                    departure_detail.setAddress(uSerBookmark.getStreetAddress());
                    departure_detail.setLocation(uSerBookmark.getLocation());
                    departure_detail.setCountryName(uSerBookmark.getCountryName());
                    departure_detail.setLocality(uSerBookmark.getLocality());
                    departure_detail.setZipCode(uSerBookmark.getZipCode());

                }
                break;
            case Constants.STOP_QUERY_BOOKMARK:
                if (data!=null) {
                    stop_detail = new LocationAddress();
                    USerBookmark uSerBookmark = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    stop_address.setText(uSerBookmark.getLocation()+"("+uSerBookmark.getStreetAddress()+")");
                    stop_detail.setLongitude(Double.parseDouble(uSerBookmark.getLng()));
                    stop_detail.setLatitude(Double.parseDouble(uSerBookmark.getLat()));
                    stop_detail.setAddress(uSerBookmark.getStreetAddress());
                    stop_detail.setLocation(uSerBookmark.getLocation());
                    stop_detail.setCountryName(uSerBookmark.getCountryName());
                    stop_detail.setLocality(uSerBookmark.getLocality());
                    stop_detail.setZipCode(uSerBookmark.getZipCode());

                }
                break;
            case Constants.DESTINATION_QUERY_BOOKMARK:
                if (data!=null) {
                    destination_detail = new LocationAddress();
                    USerBookmark uSerBookmark1 = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    destination_address.setText(uSerBookmark1.getLocation()+"("+uSerBookmark1.getStreetAddress()+")");
                    destination_detail.setLongitude(Double.parseDouble(uSerBookmark1.getLng()));
                    destination_detail.setLatitude(Double.parseDouble(uSerBookmark1.getLat()));
                    destination_detail.setAddress(uSerBookmark1.getStreetAddress());
                    destination_detail.setLocation(uSerBookmark1.getLocation());
                    destination_detail.setCountryName(uSerBookmark1.getCountryName());
                    destination_detail.setLocality(uSerBookmark1.getLocality());
                    destination_detail.setZipCode(uSerBookmark1.getZipCode());

                }
                break;
        }
        }


    private void provideOrderPrice()
    {
        if(dialog==null) {
            dialog = new Dialog(ClientTakeRideActivity.this);
            dialog.setContentView(R.layout.dialog_enter_order_price_layout);
            dialog.setTitle(getString(R.string.order_change_fee));
            TextView content = (TextView) dialog.findViewById(R.id.txt_msg);
            final EditText enter = (EditText) dialog.findViewById(R.id.editText_password);
            enter.setText("");
            final EditText tip = (EditText) dialog.findViewById(R.id.editText_tip);
            tip.setText("");
            Button sure = (Button) dialog.findViewById(R.id.sure_action);
            sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //if (option == TAKE_RIDE)
                    //    if (departure_detail != null)
                    //if (departure_detail.getLongitude()==0)
                        parserAddressToGPS();
                            createTaxiOrder("" + option, enter.getText().toString(), tip.getText().toString());
                   //     else
                   //         createTempTaxiOrder("" + option);
                   // else
                    //    createMechardiseOrder("" + option);
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
    }

    private void addUserLocationToBookMark(LocationAddress location)
    {
        String[] addressInfo = location.getLocation().split("\\(");
        USerBookmark item = new USerBookmark();
        item.setLat(""+location.getLatitude());
        item.setLng(""+location.getLongitude());
        item.setLocation(addressInfo[0]);
        item.setLocality(location.getLocality());
        item.setCountryName(location.getCountryName());
        item.setZipCode(location.getZipCode());
        item.setStreetAddress(addressInfo[1].substring(0,addressInfo[1].length()-1));

        //新增地點到資料庫
        RealmUtil database = new RealmUtil(ClientTakeRideActivity.this);
        database.addUserBookMark(item);

    }



    @Override
    public void onStop() {
        super.onStop();
        if(dialog!=null)
        {
            dialog.dismiss();
            dialog = null;
        }

        if(progressDialog_loading!=null)
        {
            progressDialog_loading.cancel();
            progressDialog_loading = null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog!=null)
        {
            dialog.dismiss();
            dialog = null;
        }

        if(progressDialog_loading!=null)
        {
            progressDialog_loading.cancel();
            progressDialog_loading = null;
        }
    }

    private void parserAddressToGPS() {

        Geocoder fwdGeocoder = new Geocoder(ClientTakeRideActivity.this);

        if(departure_detail==null) {
        String departure = departure_address.getText().toString();


        List<Address> departure_locations = null;
        try {
            departure_locations = fwdGeocoder.getFromLocationName(departure, 10);
        } catch (IOException e) {
        }



            departure_detail = new LocationAddress();
            if (departure_locations.size() > 0) {
                departure_detail.setLongitude(departure_locations.get(0).getLongitude());
                departure_detail.setLatitude(departure_locations.get(0).getLatitude());
                departure_detail.setAddress(departure);
                departure_detail.setLocation(departure);
                departure_detail.setCountryName(departure_locations.get(0).getCountryName());
                departure_detail.setLocality(departure_locations.get(0).getLocality());
                departure_detail.setZipCode(departure_locations.get(0).getPostalCode());
            }
        }
        String stop = stop_address.getText().toString();


        if(stop_detail==null) {
            List<Address> stop_locations = null;
            try {
                stop_locations = fwdGeocoder.getFromLocationName(stop, 10);
            } catch (IOException e) {
            }


            //Log.e(TAG, "Stop zipCode:" + stop_locations.get(0).getPostalCode());
            stop_detail = new LocationAddress();
            if (stop_locations.size() > 0) {
                stop_detail.setLongitude(stop_locations.get(0).getLongitude());
                stop_detail.setLatitude(stop_locations.get(0).getLatitude());
                stop_detail.setAddress(stop);
                stop_detail.setLocation(stop);
                stop_detail.setCountryName(stop_locations.get(0).getCountryName());
                stop_detail.setLocality(stop_locations.get(0).getLocality());
                stop_detail.setZipCode(stop_locations.get(0).getPostalCode());
            }
        }
        String destination = destination_address.getText().toString();


        if(destination_detail==null) {
            List<Address> destination_locations = null;
            try {
                destination_locations = fwdGeocoder.getFromLocationName(destination, 10);
            } catch (IOException e) {
            }


            destination_detail = new LocationAddress();
            if (destination_locations.size() > 0) {
                destination_detail.setLongitude(destination_locations.get(0).getLongitude());
                destination_detail.setLatitude(destination_locations.get(0).getLatitude());
                destination_detail.setAddress(destination);
                destination_detail.setLocation(destination);
                destination_detail.setCountryName(destination_locations.get(0).getCountryName());
                destination_detail.setLocality(destination_locations.get(0).getLocality());
                destination_detail.setZipCode(destination_locations.get(0).getPostalCode());
            }
        }


    }

    private void sendOrder(){
        String departure_address_info;
        if(departure_detail!=null)
            departure_address_info=departure_detail.getAddress();
        else
            departure_address_info = departure_address.getText().toString();
        String stop_address_info;
        if(stop_detail!=null)
            stop_address_info=stop_detail.getAddress();
        else
            stop_address_info = stop_address.getText().toString();

        String destination_address_info;
        if(destination_detail!=null)
            destination_address_info=destination_detail.getAddress();
        else
            destination_address_info = destination_address.getText().toString();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

        // set dialog message
        alertDialogBuilder
                .setMessage(date.getText().toString()+"\t"+time.getText().toString()+"\n從:"+departure_address_info+"\n停:"+stop_address_info+"\n到:"+destination_address_info)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                            //if (option == TAKE_RIDE) {
                                //if (departure_detail.getLongitude()==0)
                                parserAddressToGPS();
                                createTaxiOrder("" + option, "0", "0");
                            //}
                            //else
                             //   createMechardiseOrder("" + option);
                        }else
                        {
                            //讓使用者填入價錢和小費
                            provideOrderPrice();
                        }

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
               /* }else
                {
                    NormalOrder order = createMechardiseOrder(""+SEND_MERCHANDISE);
                    Intent question = new Intent(ClientTakeRideActivity.this, MerchandiseOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.ARG_POSITION, MerchandiseOrderActivity.SEND_MERCHANDISE);
                    b.putSerializable(BUNDLE_ORDER_TICKET_ID, order);
                    question.putExtras(b);
                    startActivity(question);
                }*/
    }

    private void alert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.order));

            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_register_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
