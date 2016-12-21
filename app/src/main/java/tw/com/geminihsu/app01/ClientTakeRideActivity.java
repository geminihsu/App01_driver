package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.LocationAddress;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.bean.OrderLocationBean;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class ClientTakeRideActivity extends Activity {
    private final String TAG = ClientTakeRideActivity.class.toString();
    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;


    final public static int TAKE_RIDE = 1;
    final public static int SEND_MERCHANDISE= 2;
    private LinearLayout linearLayout_date_picker;
    private LinearLayout change;
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
    private EditText date;
    private EditText time;
    private EditText departure_address;
    private EditText destination_address;
    private int option;

    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;

    private DatePickerDialog.OnDateSetListener datePicker;
    private TimePickerDialog.OnTimeSetListener timePicker;

    private Calendar calendar;

    private JsonPutsUtil sendDataRequest;
    private AccountInfo driver;

    private LocationAddress departure_detail;
    private LocationAddress destination_detail;
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

                Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                //Bundle b = new Bundle();
                //b.putString(BUNDLE_ACCESS_KEY, accesskey);
                //intent.putExtras(b);
                startActivity(intent);
                //finish();
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
            if (bundle.containsKey(Constants.ARG_POSITION)){
                option = bundle.getInt(Constants.ARG_POSITION);
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

    private void findViews()
    {
        change = (LinearLayout) findViewById (R.id.change);
        linearLayout_date_picker = (LinearLayout) findViewById(R.id.date_layout);

        btn_datePicker = (ImageButton) findViewById(R.id.date_picker);
        btn_timerPicker = (ImageButton) findViewById(R.id.time_picker);
        departure = (ImageButton) findViewById(R.id.departure);
        stop = (ImageButton) findViewById(R.id.stop);
        destination = (ImageButton) findViewById(R.id.destination);
        spec = (ImageButton) findViewById(R.id.spec_option);
        show_title = (TextView) findViewById(R.id.txt_info);

        radioGroup_type = (RadioGroup) findViewById(R.id.source);
        realtime = (RadioButton)findViewById(R.id.real_radio);
        reservation = (RadioButton) findViewById(R.id.reservation_radio);

        date = (EditText) findViewById(R.id.date_info);
        time = (EditText) findViewById(R.id.time_info);
        departure_address = (EditText) findViewById(R.id.departure_address);
        destination_address = (EditText) findViewById(R.id.destination_address);


        Date dateIfo=new Date();
        time.setText(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));
        //Log.e(TAG,new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(date));

    }


    private void displayLayout() {
        if (option == ClientTakeRideActivity.TAKE_RIDE) {
            getActionBar().setTitle(getString(R.string.client_take_ride_title));
            change.setVisibility(View.VISIBLE);
        } else if (option == ClientTakeRideActivity.SEND_MERCHANDISE){
            show_title.setText(getString(R.string.txt_send_merchandise));
            getActionBar().setTitle(getString(R.string.client_send_merchandise_title));
            change.setVisibility(View.GONE);
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
                    if (checkedId == reservation.getId()) {
                        linearLayout_date_picker.setVisibility(View.VISIBLE);
                     } else {
                        linearLayout_date_picker.setVisibility(View.GONE);

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
                                        Intent map = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(ClientTakeRideActivity.this, BookmarksMapListActivity.class);
                                        startActivity(page);
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
                Intent question = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                startActivity(question);
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
                                        Intent map = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DESTINATION_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DESTINATION_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(ClientTakeRideActivity.this, BookmarksMapListActivity.class);
                                        startActivity(page);
                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(ClientTakeRideActivity.this);
                dialog.setContentView(R.layout.client_take_ride_selectspec_requirement);
                dialog.setTitle(getString(R.string.txt_take_spec));
                Button cancel = (Button) dialog.findViewById(R.id.button_category_ok);

                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // set the custom dialog components - text, image and button
                ListView requirement = (ListView) dialog.findViewById(R.id.listViewDialog);

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
                if (option == ClientTakeRideActivity.TAKE_RIDE) {
                    String departure_address_info;
                    if(departure_detail!=null)
                        departure_address_info=departure_detail.getAddress();
                    else
                        departure_address_info = departure_address.getText().toString();
                    String destination_address_info;
                    if(departure_detail!=null)
                        destination_address_info=destination_detail.getAddress();
                    else
                        destination_address_info = destination_address.getText().toString();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            this);

                    // set title
                    alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(time.getText().toString()+"\n從:"+departure_address_info+"\n停:繼光街口\n到:"+destination_address_info)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNegativeButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if(option == TAKE_RIDE)
                                        if(departure_detail!=null)
                                         createTaxiOrder(""+option);
                                        else
                                         createTempTaxiOrder(""+option);
                                    else
                                    createMechardiseOrder(""+option);

                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }else
                {
                    NormalOrder order = createMechardiseOrder(""+SEND_MERCHANDISE);
                    Intent question = new Intent(ClientTakeRideActivity.this, MerchandiseOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.ARG_POSITION, MerchandiseOrderActivity.SEND_MERCHANDISE);
                    b.putSerializable(BUNDLE_ORDER_TICKET_ID, order);
                    question.putExtras(b);
                    startActivity(question);
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

    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        mCommentListData.clear();
        Resources res =getResources();
        String[] opt=res.getStringArray(R.array.client_take_ride_requirement);
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < opt.length; i++) {
                // for listview 要用的資料
                ClientTakeRideSelectSpecListItem item = new ClientTakeRideSelectSpecListItem();

                if(i==0)
                  item.check=true;
                else
                  item.check=false;
                item.book_title =opt[i];
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

       //sendDataRequest.putCreateNormalOrder(order);
        return order;

    }

    private void createTaxiOrder(String target)
    {

        Utility info = new Utility(ClientTakeRideActivity.this);
        driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude(""+departure_detail.getLatitude());
        begin_location.setLongitude(""+departure_detail.getLongitude());
        begin_location.setZipcode("404");
        begin_location.setAddress(departure_detail.getAddress());

        OrderLocationBean stop_location = new OrderLocationBean();
        stop_location.setId(2);
        stop_location.setLatitude("24.14411");
        stop_location.setLongitude("120.679567");
        stop_location.setZipcode("400");
        stop_location.setAddress("台中市中區柳川里成功路300號");


        OrderLocationBean end_location = new OrderLocationBean();
        end_location.setId(3);
        end_location.setLatitude(""+destination_detail.getLatitude());
        end_location.setLongitude(""+destination_detail.getLongitude());
        end_location.setZipcode("400");
        end_location.setAddress(destination_detail.getAddress());



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
                    departure_detail = new LocationAddress();
                    locationInfo =(ArrayList<Address>) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    departure_detail.setLatitude(Double.valueOf(latitude));
                    departure_detail.setLongitude(Double.valueOf(longitude));
                    //departure_address.setText(locationInfo.get(0).getAddressLine(0));
                    showAddressList(locationInfo,departure_address,departure_detail);
                }
            break;
            case Constants.DESTINATION_QUERY_GPS:
                ArrayList<Address> locationInfo2=null;
                if(data!=null) {
                    destination_detail = new LocationAddress();
                    locationInfo2 =(ArrayList<Address>) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    destination_detail.setLatitude(Double.valueOf(latitude));
                    destination_detail.setLongitude(Double.valueOf(longitude));
                    //departure_address.setText(locationInfo.get(0).getAddressLine(0));
                    showAddressList(locationInfo2,destination_address,destination_detail);
                }
            break;
        }
        }
    private void showAddressList(ArrayList<Address> address, final EditText showAddress, final LocationAddress locationAddress)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ClientTakeRideActivity.this);
        builderSingle.setTitle("You address:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                ClientTakeRideActivity.this,
                android.R.layout.select_dialog_item);


        for(int i=  0 ;i<address.size();i++)
        {
            Address _address = address.get(i);
            for(int j = 0 ;j<_address.getMaxAddressLineIndex();j++)
                arrayAdapter.add(_address.getAddressLine(j));
        }
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                ClientTakeRideActivity.this);
                        locationAddress.setAddress(strName);
                        showAddress.setText(strName);

                    }
                });
        builderSingle.show();
    }

}
