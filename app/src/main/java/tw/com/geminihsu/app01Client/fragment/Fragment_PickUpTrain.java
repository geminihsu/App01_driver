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
package tw.com.geminihsu.app01Client.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Spinner;
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
import tw.com.geminihsu.app01Client.BookmarksMapListActivity;
import tw.com.geminihsu.app01Client.ClientTakeRideSearchActivity;
import tw.com.geminihsu.app01Client.OrderMapActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01Client.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.LocationAddress;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.bean.OrderLocationBean;
import tw.com.geminihsu.app01Client.bean.USerBookmark;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.serverbean.ServerBookmark;
import tw.com.geminihsu.app01Client.serverbean.ServerSpecial;
import tw.com.geminihsu.app01Client.utils.DateTimeUtil;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01Client.utils.Utility;


public class Fragment_PickUpTrain extends Fragment {
    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    public final static String BUNDLE_KEEP_BOOMARK = "add_bookmark";// from

    final public static int SEND_TRAIN = 0;
    final public static int PICKUP_TRAIN =1;
    private LinearLayout change;
    private LinearLayout linearLayout_departure;
    private LinearLayout linearLayout_destination;
    private LinearLayout linearLayout_date_picker;
    private LinearLayout linearLayout_time_picker;
    private LinearLayout linearLayout_spec;

    private ImageButton timerPicker;
    private ImageButton departure;
    private ImageButton stop;
    private ImageButton destination;
    private ImageButton spec;
    private ImageButton btn_datePicker;
    private TextView show_title;
    private TextView spec_value;

    private Spinner spinner_go_location;
    private Spinner spinner_departure;
    private RadioGroup radiogroup_leave_location;
    private RadioGroup radiogroup_destination_station;
    private RadioGroup radioGroup_orderTimetype;

    private RadioButton realtime;
    private RadioButton reservation;

    private RadioButton thsrStation;
    private RadioButton traStation;

    private RadioButton departure_tra_train;
    private RadioButton departure_thsr_train;


    private EditText leave_train;
    private EditText stop_train;
    private EditText destination_train;
    private EditText date;
    private EditText time;
    private EditText reMark;

    private ArrayAdapter arrayAdapter_location;
    private ArrayAdapter arrayAdapter_departure;
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

    private static int dType;//哪一種司機型態
    private static int cargoType;//那一種訂單型態

    private static Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private static Constants.APP_REGISTER_ORDER_TYPE orderCargoType;

    private Dialog dialog;
    private ArrayList<ServerBookmark> tainStationList;
    private ServerBookmark currentLocation;

    private long order_timeStamp;
    private ArrayList<ClientTakeRideSelectSpecListItem> spec_list;
    private ProgressDialog progressDialog_loading;


    private String curAddress = "";
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
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        //if (savedInstanceState != null) {
         //   mCurrentPosition = savedInstanceState.getInt(Constants.ARG_POSITION);
        //}

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.client_pick_up_train, container, false);
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
                   if (data.containsKey(Fragment_TrainPlanePickUp.BUNDLE_ORDER_CUR_ADDRESS)) {
                       curAddress = data.getString(Fragment_TrainPlanePickUp.BUNDLE_ORDER_CUR_ADDRESS);
                   }
                }
        this.findViews();
        displayLayout();
        setLister();
        getLocationFromDB("火車");
    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle(getString(R.string.client_train_pick_up));

    }

    @Override
	public void onStop() {
        if(progressDialog_loading!= null)
        {
            progressDialog_loading.cancel();
            progressDialog_loading = null;
        }
		super.onStop();

	}

    private void findViews()
    {
        spec_list = new ArrayList<ClientTakeRideSelectSpecListItem>();
        change = (LinearLayout) getView().findViewById (R.id.change);
        linearLayout_departure = (LinearLayout) getView().findViewById (R.id.layout_departure);
        linearLayout_destination = (LinearLayout) getView().findViewById (R.id.layout_destination);
        linearLayout_date_picker = (LinearLayout) getView().findViewById(R.id.date_layout);
        linearLayout_time_picker = (LinearLayout) getView().findViewById(R.id.time_layout);
        linearLayout_spec = (LinearLayout) getView().findViewById(R.id.spec_require);


        btn_datePicker = (ImageButton) getView().findViewById(R.id.date_picker);
        timerPicker = (ImageButton) getView().findViewById(R.id.time_picker);
        departure = (ImageButton) getView().findViewById(R.id.departure);
        stop = (ImageButton) getView().findViewById(R.id.stop);
        destination = (ImageButton) getView().findViewById(R.id.destination_find);
        spec = (ImageButton) getView().findViewById(R.id.spec_option);
        show_title = (TextView) getView().findViewById(R.id.txt_info);
        spec_value = (TextView) getView().findViewById(R.id.passenger_spec_value);


        spinner_go_location = (Spinner)getActivity().findViewById(R.id.train_go_location);
        spinner_departure =(Spinner)getActivity().findViewById(R.id.train_departure);
        radiogroup_leave_location= (RadioGroup)getActivity().findViewById(R.id.departure_train);
        radiogroup_destination_station =(RadioGroup)getActivity().findViewById(R.id.train);
        radioGroup_orderTimetype = (RadioGroup) getActivity().findViewById(R.id.source);

        realtime = (RadioButton)getActivity().findViewById(R.id.real_radio);
        reservation = (RadioButton) getActivity().findViewById(R.id.reservation_radio);
        thsrStation = (RadioButton) getActivity().findViewById(R.id.thsr_train);
        traStation = (RadioButton) getActivity().findViewById(R.id.tra_train);
        departure_tra_train = (RadioButton) getActivity().findViewById(R.id.departure_tra_train);
        departure_thsr_train = (RadioButton) getActivity().findViewById(R.id.departure_thsr_train);


        leave_train = (EditText) getActivity().findViewById(R.id.leave_location);
        leave_train.setText(curAddress);
        destination_train = (EditText)getActivity().findViewById(R.id.destination_map);
        destination_train.setText(curAddress);
        stop_train= (EditText)getActivity().findViewById(R.id.stoptrain);


        date = (EditText) getActivity().findViewById(R.id.date_info);
        time = (EditText) getActivity().findViewById(R.id.time_info);
        reMark = (EditText) getActivity().findViewById(R.id.spec_info);



        Date dateIfo=new Date();
        date.setText(new SimpleDateFormat("yyyy/MM/dd").format(dateIfo));
        time.setText(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(dateIfo));

    }


    private void displayLayout() {
        if (option == SEND_TRAIN) {
            //getActivity().getActionBar().setTitle(getString(R.string.client_take_ride_title));
            //spinner_go_location.setVisibility(View.GONE);
            radiogroup_leave_location.setVisibility(View.VISIBLE);
            radiogroup_destination_station.setVisibility(View.GONE);
            leave_train.setVisibility(View.GONE);
            //departure.setVisibility(View.VISIBLE);
            destination_train.setVisibility(View.VISIBLE);
            destination.setVisibility(View.GONE);
            //linearLayout_departure.setVisibility(View.GONE);
            //spinner_departure.setVisibility(View.VISIBLE);
            linearLayout_destination.setVisibility(View.VISIBLE);
            change.setVisibility(View.VISIBLE);
        } else if (option == PICKUP_TRAIN){
            leave_train.setVisibility(View.VISIBLE);
            departure.setVisibility(View.GONE);
            spinner_go_location.setVisibility(View.VISIBLE);
            spinner_departure.setVisibility(View.GONE);
            radiogroup_leave_location.setVisibility(View.GONE);
            radiogroup_destination_station.setVisibility(View.VISIBLE);
            destination_train.setVisibility(View.GONE);
            //destination.setVisibility(View.VISIBLE);
            linearLayout_departure.setVisibility(View.VISIBLE);
            linearLayout_destination.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
        }
    }
    private void setLister()
    {

        calendar = Calendar.getInstance();
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
        datePicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth+ "/" + (month+1) + "/" + year);
            }

        };
        btn_datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), datePicker,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        radioGroup_orderTimetype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == reservation.getId()) {
                    linearLayout_date_picker.setVisibility(View.VISIBLE);
                    linearLayout_time_picker.setVisibility(View.GONE);
                } else {
                    linearLayout_date_picker.setVisibility(View.GONE);
                    linearLayout_time_picker.setVisibility(View.VISIBLE);
                }
            }
        });


        radiogroup_destination_station.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == thsrStation.getId()) {
                    getLocationFromDB("高鐵");
                } else {

                    getLocationFromDB("火車");
                }
            }
        });

        radiogroup_leave_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == departure_thsr_train.getId()) {
                    getLocationFromDB("高鐵");
                } else {

                    getLocationFromDB("火車");
                }
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


        getDataFromDB();
        linearLayout_spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(getActivity());
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
                    listViewAdapter = new ClientTakeRideSelectSpecListItemAdapter(getActivity(), 0, mCommentListData);

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


        spinner_go_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                currentLocation = tainStationList.get(position);
                destination_detail = new LocationAddress();
                destination_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                destination_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                destination_detail.setAddress(currentLocation.getStreetAddress().substring(3,currentLocation.getStreetAddress().length()));
                destination_detail.setLocation(currentLocation.getLocation());
                String zipCpde = (getTrainStationZip(currentLocation.getLocation()));
                destination_detail.setZipCode(zipCpde);
                destination_detail.setCountryName("Taiwan");
                destination_detail.setLocality(currentLocation.getStreetAddress());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinner_departure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                currentLocation = tainStationList.get(position);
                departure_detail = new LocationAddress();
                departure_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                departure_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                departure_detail.setAddress(currentLocation.getStreetAddress().substring(3,currentLocation.getStreetAddress().length()));
                departure_detail.setLocation(currentLocation.getLocation());
                //departure_detail.setZipCode(currentLocation.getStreetAddress().substring(0,3));
                String zipCode = (getTrainStationZip(currentLocation.getLocation()));
                departure_detail.setZipCode(zipCode);
                departure_detail.setCountryName("Taiwan");
                departure_detail.setLocality(currentLocation.getStreetAddress());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }



    private void getDataFromDB() {

        mCommentListData.clear();
        //Resources res =getResources();
        //String[] opt=res.getStringArray(R.array.client_take_ride_requirement);
        RealmUtil data = new RealmUtil(getActivity());
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
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
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
        //arrayAdapter_departure= new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stationInfo);
        spinner_go_location.setAdapter(arrayAdapter_location);
        spinner_departure.setAdapter(arrayAdapter_location);
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
                    leave_train.setText(departure_detail.getAddress());
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
                    stop_train.setText(stop_detail.getAddress());
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
                    destination_train.setText(destination_detail.getAddress());
                    //destination_detail.setAddress(locationInfo2.get(0).getAddressLine(0));
                    //destination_detail.setLocation(locationInfo2.get(0).getAddressLine(0));
                    //showAddressList(locationInfo2,destination_address,destination_detail);
                }
                break;

            case Constants.DEPARTURE_QUERY_BOOKMARK:
                if (data!=null) {
                    departure_detail = new LocationAddress();
                    USerBookmark uSerBookmark = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    leave_train.setText(uSerBookmark.getStreetAddress());
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
                    stop_train.setText(uSerBookmark.getStreetAddress());
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
                    destination_train.setText(uSerBookmark1.getStreetAddress());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_take));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ACTIONBAR_MENU_ITEM_SUMMIT:
                if(!leave_train.getText().toString().isEmpty()||!destination_train.getText().toString().isEmpty())
                    sendOrder();
                else
                    alert();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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
            for(ClientTakeRideSelectSpecListItem item:spec_list){
                spec+=item.spec_id+",";
            }
            spec = spec.substring(0,spec.length()-1);

        }

        //Log.e(TAG,"spec car:"+spec);
        order.setCar_special(spec);
        order.setRemark(reMark.getText().toString());
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
            String departure = leave_train.getText().toString();


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
        String stop = stop_train.getText().toString();


        if(stop_detail==null) {
            List<Address> stop_locations = null;
            try {
                stop_locations = fwdGeocoder.getFromLocationName(stop, 10);
            } catch (IOException e) {
            }


            //Log.e("", "Stop zipCode:" + stop_locations.get(0).getPostalCode());
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
        String destination = destination_train.getText().toString();


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

        if (option == SEND_TRAIN) {
            //離開車站
            currentLocation = tainStationList.get(spinner_go_location.getSelectedItemPosition());
            //if(destination_detail==null) {
                destination_detail = new LocationAddress();
                destination_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                destination_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                destination_detail.setAddress(currentLocation.getStreetAddress().substring(3, currentLocation.getStreetAddress().length()));
                destination_detail.setLocation(currentLocation.getLocation());
                //destination_detail.setZipCode(currentLocation.getStreetAddress().substring(0, 3));
                String zipCpde = (getTrainStationZip(currentLocation.getLocation()));
                destination_detail.setZipCode(zipCpde);
                destination_detail.setCountryName("Taiwan");
                destination_detail.setLocality(currentLocation.getStreetAddress());
           // }
        }else {
            //前往車站
            currentLocation = tainStationList.get(spinner_departure.getSelectedItemPosition());
            if(departure_detail==null) {
                departure_detail = new LocationAddress();
                departure_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                departure_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                departure_detail.setAddress(currentLocation.getStreetAddress().substring(3, currentLocation.getStreetAddress().length()));
                departure_detail.setLocation(currentLocation.getLocation());
                //departure_detail.setZipCode(currentLocation.getStreetAddress().substring(0, 3));
                String zipCpde = (getTrainStationZip(currentLocation.getLocation()));
                destination_detail.setZipCode(zipCpde);
                departure_detail.setCountryName("Taiwan");
                departure_detail.setLocality(currentLocation.getStreetAddress());
            }
        }
        String departure_address_info;
        if(departure_detail!=null)
            departure_address_info=departure_detail.getAddress();
        else
            departure_address_info = leave_train.getText().toString();
        String stop_address_info;
        if(stop_detail!=null)
            stop_address_info=stop_detail.getAddress();
        else
            stop_address_info = stop_train.getText().toString();

        String destination_address_info;
        if(destination_detail!=null)
            destination_address_info=destination_detail.getAddress();
        else
            destination_address_info = destination_train.getText().toString();

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

    private String getTrainStationZip(String address) {

        Geocoder fwdGeocoder = new Geocoder(getActivity());

        String zipCode = "";
        String departure = address;


            List<Address> departure_locations = null;
            try {
                departure_locations = fwdGeocoder.getFromLocationName(departure, 10);
            } catch (IOException e) {
            }


            zipCode = departure_locations.get(0).getPostalCode();



        return zipCode;
    }

}

