/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tw.com.geminihsu.app01.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.BookmarksMapListActivity;
import tw.com.geminihsu.app01.ClientTakeRideSearchActivity;
import tw.com.geminihsu.app01.MapsActivity;
import tw.com.geminihsu.app01.OrderMapActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.LocationAddress;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.bean.OrderLocationBean;
import tw.com.geminihsu.app01.bean.USerBookmark;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.serverbean.ServerBookmark;
import tw.com.geminihsu.app01.utils.DateTimeUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01.utils.Utility;


public class Fragment_PickUpMerchandiseStation extends Fragment {
    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    public final static String BUNDLE_KEEP_BOOMARK = "add_bookmark";// from


    final public static int GO_DORK = 0;
    final public static int NO_DORK =1;
    final public static int BACK_DORK =2;
    private LinearLayout change;
    private ImageButton timerPicker;
    private ImageButton departure;
    private ImageButton stop;
    private ImageButton destination;
    private ImageButton spec;
    private TextView show_title;
    private int option;

    private EditText departure_address;
    private EditText stop_address;
    private EditText destination_address;
    private EditText date;
    private EditText time;


    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;


    private TimePickerDialog.OnTimeSetListener timePicker;

    private Spinner spinner_dork_location_departure;
    private Spinner spinner_dork_location_destination;
    private ArrayAdapter arrayAdapter_location;

    private JsonPutsUtil sendDataRequest;
    private AccountInfo driver;

    private LocationAddress departure_detail;
    private LocationAddress stop_detail;
    private LocationAddress destination_detail;


    private static int dType;//哪一種司機型態
    private static int cargoType;//那一種訂單型態

    private static Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private static Constants.APP_REGISTER_ORDER_TYPE orderCargoType;

    private Dialog dialog;
    private ArrayList<ServerBookmark> tainStationList;
    private ServerBookmark currentLocation;

    private long order_timeStamp;
    private ArrayList<String> spec_list;
    private ProgressDialog progressDialog_loading;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sendDataRequest = new JsonPutsUtil(getActivity());
        sendDataRequest.setServerRequestOrderManagerCallBackFunction(new JsonPutsUtil.ServerRequestOrderManagerCallBackFunction() {

            @Override
            public void createNormalOrder(NormalOrder order) {

                Intent intent = new Intent(getActivity(), ClientTakeRideSearchActivity.class);

                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void cancelNormalOrder(NormalOrder order) {

            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.client_pick_up_merchandise_dork, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle data=getArguments();
        if ( data !=null)
        {
            option = data.getInt(Constants.ARG_POSITION);
            if (data.containsKey(Constants.NEW_ORDER_DTYPE)) {
                dType = data.getInt(Constants.NEW_ORDER_DTYPE);
                dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(dType));
            }
            if (data.containsKey(Constants.NEW_ORDER_CARGO_TYPE)) {
                cargoType = data.getInt(Constants.NEW_ORDER_CARGO_TYPE);
                orderCargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargoType));
            }
        }
        this.findViews();
        displayLayout();
        setLister();
        getLocationFromDB("港");
    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle(getString(R.string.client_merchanse_send_title));

    }

    @Override
	public void onStop() {
		super.onStop();

	}

    private void findViews()
    {
        change = (LinearLayout) getView().findViewById (R.id.change);
        timerPicker = (ImageButton) getView().findViewById(R.id.time_picker);
        departure = (ImageButton) getView().findViewById(R.id.departure);
        stop = (ImageButton) getView().findViewById(R.id.stop);
        destination = (ImageButton) getView().findViewById(R.id.destination);
        spec = (ImageButton) getView().findViewById(R.id.spec_option);
        show_title = (TextView) getView().findViewById(R.id.txt_info);
        spinner_dork_location_destination = (Spinner) getView().findViewById(R.id.dork_location_destination);
        departure_address = (EditText) getView().findViewById(R.id.departure_info);
        stop_address = (EditText) getView().findViewById(R.id.stop_info);
        destination_address = (EditText) getView().findViewById(R.id.departure_address);


        date = (EditText) getActivity().findViewById(R.id.date_pick_info);
        time = (EditText) getActivity().findViewById(R.id.time_info);

        spinner_dork_location_departure = (Spinner) getActivity().findViewById(R.id.dork_location_departure);
        spinner_dork_location_destination = (Spinner) getActivity().findViewById(R.id.dork_location_destination);


        Date dateIfo=new Date();
        date.setText(new SimpleDateFormat("yyyy/MM/dd").format(dateIfo));
        time.setText(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            case Constants.STOP_QUERY_GPS:
                //ArrayList<Address> locationInfo=null;
                if(data!=null) {
                    //departure_detail = new LocationAddress();
                    stop_detail =(LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String add_bookmark = data.getStringExtra(BUNDLE_KEEP_BOOMARK);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //departure_detail.setLatitude(location.getLatitude());
                    //departure_detail.setLongitude(location.getLongitude());
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK,false);
                    if(isBookMark)
                    {
                        addUserLocationToBookMark(stop_detail);
                    }
                    stop_address.setText(stop_detail.getAddress());
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

            case Constants.DEPARTURE_QUERY_BOOKMARK:
                if (data!=null) {
                    departure_detail = new LocationAddress();
                    USerBookmark uSerBookmark = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    departure_address.setText(uSerBookmark.getStreetAddress());
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
                    stop_address.setText(uSerBookmark.getStreetAddress());
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
                    destination_address.setText(uSerBookmark1.getStreetAddress());
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




    private void displayLayout() {
        if (option == GO_DORK || option == BACK_DORK) {
            //getActivity().getActionBar().setTitle(getString(R.string.client_take_ride_title));

            spinner_dork_location_destination.setVisibility(View.GONE);
            departure_address.setVisibility(View.VISIBLE);
            change.setVisibility(View.VISIBLE);
        } else if (option == NO_DORK){
            //show_title.setText(getString(R.string.txt_send_merchandise));
            //getActivity().getActionBar().setTitle(getString(R.string.client_send_merchandise_title));
            change.setVisibility(View.VISIBLE);
            spinner_dork_location_destination.setVisibility(View.VISIBLE);
            departure_address.setVisibility(View.VISIBLE);
        }
    }

    private void addUserLocationToBookMark(LocationAddress location)
    {
        USerBookmark item = new USerBookmark();
        item.setLat(""+location.getLatitude());
        item.setLng(""+location.getLongitude());
        item.setLocation(location.getLocation());
        item.setLocality(location.getLocality());
        item.setCountryName(location.getCountryName());
        item.setZipCode(location.getZipCode());
        item.setStreetAddress(location.getAddress());

        //新增地點到資料庫
        RealmUtil database = new RealmUtil(getActivity());
        database.addUserBookMark(item);

    }

    private void setLister()
    {

        timePicker=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {

            }
        };
        timerPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),
                        timePicker,
                        24,
                        59,
                        true).show();
            }
        });


        departure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
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
                                        Intent map = new Intent(getActivity(), OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);

                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
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
                                        Intent map = new Intent(getActivity(), OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.STOP_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.STOP_QUERY_GPS);

                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
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
                                        Intent map = new Intent(getActivity(), OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DESTINATION_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DESTINATION_QUERY_GPS);

                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
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

        spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.client_take_ride_selectspec_requirement);
                dialog.setTitle(getString(R.string.txt_take_spec));
                Button cancel = (Button) dialog.findViewById(R.id.button_category_ok);

                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // set the custom dialog components - text, image and button
                ListView requirement = (ListView) dialog.findViewById(R.id.listViewDialog);

                getDataFromDB();
                listViewAdapter = new ClientTakeRideSelectSpecListItemAdapter(getActivity(), 0, mCommentListData);
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

        spinner_dork_location_destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                currentLocation = tainStationList.get(position);
                destination_detail = new LocationAddress();
                destination_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                destination_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                destination_detail.setAddress(currentLocation.getStreetAddress().substring(3,currentLocation.getStreetAddress().length()));
                destination_detail.setLocation(currentLocation.getLocation());
                destination_detail.setZipCode(currentLocation.getStreetAddress().substring(0,3));
                destination_detail.setCountryName("Taiwan");
                destination_detail.setLocality(currentLocation.getStreetAddress());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinner_dork_location_departure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                currentLocation = tainStationList.get(position);
                departure_detail = new LocationAddress();
                departure_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                departure_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                departure_detail.setAddress(currentLocation.getStreetAddress().substring(3,currentLocation.getStreetAddress().length()));
                departure_detail.setLocation(currentLocation.getLocation());
                departure_detail.setZipCode(currentLocation.getStreetAddress().substring(0,3));
                departure_detail.setCountryName("Taiwan");
                departure_detail.setLocality(currentLocation.getStreetAddress());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    //顯示車站資料
    private void getLocationFromDB(String filterName) {
        tainStationList  = new ArrayList<ServerBookmark>();
        ArrayList<String> stationInfo = new ArrayList<String>();
        RealmUtil database = new RealmUtil(getActivity());
        RealmResults<ServerBookmark> tainList= database.queryServerBookmark();
        for (ServerBookmark station:tainList)
        {
            if(station.getLocation().contains(filterName)) {
                stationInfo.add(station.getLocation());
                tainStationList.add(station);
            }
        }


        arrayAdapter_location = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stationInfo);
        spinner_dork_location_departure.setAdapter(arrayAdapter_location);
        spinner_dork_location_destination.setAdapter(arrayAdapter_location);

    }


    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        mCommentListData.clear();
        Resources res =getResources();
        String[] opt=res.getStringArray(R.array.client_send_merchandise_requirement);
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
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void provideOrderPrice() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setTitle(getString(R.string.order_change_fee));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_order_price_layout, null);
        dialogBuilder.setView(dialogView);

        TextView content = (TextView) dialogView.findViewById(R.id.txt_msg);
        final EditText enter = (EditText) dialogView.findViewById(R.id.editText_password);
        enter.setText("");
        final EditText tip = (EditText) dialogView.findViewById(R.id.editText_tip);
        tip.setText("");
        Button sure = (Button) dialogView.findViewById(R.id.sure_action);


        Button cancel = (Button) dialogView.findViewById(R.id.cancel_action);

        final AlertDialog alertDialog = dialogBuilder.create();
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                parserAddressToGPS();
                createTaxiOrder("" + option, enter.getText().toString(), tip.getText().toString());
                if(alertDialog!=null) {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();


    }
    private void createTaxiOrder(String target,String price,String tip)
    {

        Utility info = new Utility(getActivity());
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





        final NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());

        //Log.e(TAG,"time stamp:"+time.getText().toString());
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

        order.setPrice(price);
        order.setTip(tip);

        String spec="";
        if(!spec_list.isEmpty())
        {
            for(String spec_id:spec_list){
                spec+=spec_id+",";
            }
            spec.substring(0,spec.length()-1);

        }
        //Log.e(TAG,"spec car:"+spec);
        order.setCar_special(spec);

        //sendDataRequest.putCreateQuickTaxiOrder(order);
        if(progressDialog_loading==null) {
            progressDialog_loading = ProgressDialog.show(getActivity(), "",
                    "Loading. Please wait...", true);
        }
        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                sendDataRequest.putCreateNormalOrder(order);
            }
        }));

    }

    private void parserAddressToGPS() {

        Geocoder fwdGeocoder = new Geocoder(getActivity());

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


            Log.e("", "Stop zipCode:" + stop_locations.get(0).getPostalCode());
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

        if (option == GO_DORK) {
            //離開車站
            currentLocation = tainStationList.get(spinner_dork_location_departure.getSelectedItemPosition());
            if(destination_detail==null) {
                destination_detail = new LocationAddress();
                destination_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                destination_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                destination_detail.setAddress(currentLocation.getStreetAddress().substring(3, currentLocation.getStreetAddress().length()));
                destination_detail.setLocation(currentLocation.getLocation());
                destination_detail.setZipCode(currentLocation.getStreetAddress().substring(0, 3));
                destination_detail.setCountryName("Taiwan");
                destination_detail.setLocality(currentLocation.getStreetAddress());
            }
        }else {
            //前往車站
            currentLocation = tainStationList.get(spinner_dork_location_destination.getSelectedItemPosition());
            if(departure_detail==null) {
                departure_detail = new LocationAddress();
                departure_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                departure_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                departure_detail.setAddress(currentLocation.getStreetAddress().substring(3, currentLocation.getStreetAddress().length()));
                departure_detail.setLocation(currentLocation.getLocation());
                departure_detail.setZipCode(currentLocation.getStreetAddress().substring(0, 3));
                departure_detail.setCountryName("Taiwan");
                departure_detail.setLocality(currentLocation.getStreetAddress());
            }
        }
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
                getActivity());

        // set title
        alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

        // set dialog message
        alertDialogBuilder
                .setMessage(date.getText().toString()+"\t"+time.getText().toString()+"\n從:"+departure_address_info+"\n停:+"+stop_address_info+"\n到:"+destination_address_info)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                            parserAddressToGPS();
                            createTaxiOrder("" + option, "0", "0");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ACTIONBAR_MENU_ITEM_SUMMIT:
                if(!departure_address.getText().toString().isEmpty()||!destination_address.getText().toString().isEmpty())
                    sendOrder();
                else
                    alert();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void alert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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

