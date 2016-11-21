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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.com.geminihsu.app01.BookmarksMapListActivity;
import tw.com.geminihsu.app01.MapsActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01.common.Constants;


public class Fragment_PickUpTrain extends Fragment {
    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;


    final public static int SEND_TRAIN = 0;
    final public static int PICKUP_TRAIN =1;
    private LinearLayout change;
    private LinearLayout linearLayout_departure;
    private LinearLayout linearLayout_destination;
    private LinearLayout linearLayout_date_picker;

    private ImageButton timerPicker;
    private ImageButton departure;
    private ImageButton stop;
    private ImageButton destination;
    private ImageButton spec;
    private ImageButton btn_datePicker;
    private TextView show_title;

    private Spinner spinner_go_location;
    private Spinner spinner_leave_location;
    private RadioGroup radiogroup_leave_location;
    private RadioGroup radiogroup_destination_station;

    private RadioGroup radioGroup_type;
    private RadioButton realtime;
    private RadioButton reservation;

    private EditText leave_train;
    private EditText destination_train;
    private EditText date;
    private EditText time;

    private ArrayAdapter arrayAdapter_location;
    private ArrayAdapter arrayAdapter_departure;
    private int option;

    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;

    private DatePickerDialog.OnDateSetListener datePicker;
    private TimePickerDialog.OnTimeSetListener timePicker;

    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        }
        this.findViews();
        displayLayout();
        setLister();
    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle(getString(R.string.client_train_pick_up));

    }

    @Override
	public void onStop() {
		super.onStop();

	}

    private void findViews()
    {
        change = (LinearLayout) getView().findViewById (R.id.change);
        linearLayout_departure = (LinearLayout) getView().findViewById (R.id.layout_depature);
        linearLayout_destination = (LinearLayout) getView().findViewById (R.id.layout_destination);
        linearLayout_date_picker = (LinearLayout) getView().findViewById(R.id.date_layout);

        btn_datePicker = (ImageButton) getView().findViewById(R.id.date_picker);
        timerPicker = (ImageButton) getView().findViewById(R.id.time_picker);
        departure = (ImageButton) getView().findViewById(R.id.departure);
        stop = (ImageButton) getView().findViewById(R.id.stop);
        destination = (ImageButton) getView().findViewById(R.id.destination_find);
        spec = (ImageButton) getView().findViewById(R.id.spec_option);
        show_title = (TextView) getView().findViewById(R.id.txt_info);
        spinner_go_location = (Spinner)getActivity().findViewById(R.id.train_go_location);
        spinner_leave_location =(Spinner)getActivity().findViewById(R.id.train_leave_location);
        radiogroup_leave_location= (RadioGroup)getActivity().findViewById(R.id.departure_train);
        radiogroup_destination_station =(RadioGroup)getActivity().findViewById(R.id.train);
        radioGroup_type = (RadioGroup) getActivity().findViewById(R.id.source);
        realtime = (RadioButton)getActivity().findViewById(R.id.real_radio);
        reservation = (RadioButton) getActivity().findViewById(R.id.reservation_radio);


        leave_train = (EditText) getActivity().findViewById(R.id.leave_location);
        destination_train = (EditText)getActivity().findViewById(R.id.destination_map);
    }


    private void displayLayout() {
        if (option == SEND_TRAIN) {
            //getActivity().getActionBar().setTitle(getString(R.string.client_take_ride_title));
            //spinner_go_location.setVisibility(View.GONE);
            radiogroup_leave_location.setVisibility(View.VISIBLE);
            radiogroup_destination_station.setVisibility(View.GONE);
            leave_train.setVisibility(View.GONE);
            departure.setVisibility(View.VISIBLE);
            destination_train.setVisibility(View.VISIBLE);
            destination.setVisibility(View.GONE);
            //linearLayout_departure.setVisibility(View.GONE);
            spinner_leave_location.setVisibility(View.VISIBLE);
            linearLayout_destination.setVisibility(View.VISIBLE);
            change.setVisibility(View.VISIBLE);
        } else if (option == PICKUP_TRAIN){
            //show_title.setText(getString(R.string.txt_send_merchandise));
            //getActivity().getActionBar().setTitle(getString(R.string.client_send_merchandise_title));
            leave_train.setVisibility(View.VISIBLE);
            departure.setVisibility(View.GONE);
            spinner_go_location.setVisibility(View.VISIBLE);
            spinner_leave_location.setVisibility(View.GONE);
            radiogroup_leave_location.setVisibility(View.GONE);
            radiogroup_destination_station.setVisibility(View.VISIBLE);
            destination_train.setVisibility(View.GONE);
            destination.setVisibility(View.VISIBLE);
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
                                        Intent question = new Intent(getActivity(), MapsActivity.class);
                                        startActivity(question);
                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
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
                Intent question = new Intent(getActivity(), MapsActivity.class);
                startActivity(question);
            }
        });

        destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(getActivity(), MapsActivity.class);
                startActivity(question);
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

        String[] location = {"台北高鐵站", "台中高鐵站", "桃園高鐵站", "新竹高鐵站", "嘉義高鐵站"};
        arrayAdapter_location = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, location);
        arrayAdapter_departure= new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, location);
        spinner_go_location.setAdapter(arrayAdapter_location);
        spinner_leave_location.setAdapter(arrayAdapter_location);

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
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

