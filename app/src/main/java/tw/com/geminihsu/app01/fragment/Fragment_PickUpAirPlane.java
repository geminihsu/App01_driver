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
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.BookmarksMapListActivity;
import tw.com.geminihsu.app01.ClientTakeRideSearchActivity;
import tw.com.geminihsu.app01.MapsActivity;
import tw.com.geminihsu.app01.MerchandiseOrderActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;


public class Fragment_PickUpAirPlane extends Fragment {
    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;


    final public static int SEND_AIRPORT = 0;
    final public static int PICKUP_AIRPORT =1;
    private LinearLayout change;
    private ImageButton timerPicker;
    private ImageButton departure;
    private ImageButton stop;
    private ImageButton destination;
    private ImageButton spec;
    private TextView show_title;

    private Spinner spinner_airport_location_departure;
    private Spinner spinner_airport_location_destination;
    private ArrayAdapter arrayAdapter_location;
    private EditText departure_address;

    private int option;


    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;


    private TimePickerDialog.OnTimeSetListener timePicker;


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
        return inflater.inflate(R.layout.client_pick_up_airplane, container, false);
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
         getActivity().setTitle(getString(R.string.client_airplane_pick_up));

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
        spinner_airport_location_departure = (Spinner) getView().findViewById(R.id.airport_location_departure);
        spinner_airport_location_destination = (Spinner) getView().findViewById(R.id.airport_location_destination);
        departure_address = (EditText) getView().findViewById(R.id.departure_address);
    }


    private void displayLayout() {
        if (option == SEND_AIRPORT) {
            //getActivity().getActionBar().setTitle(getString(R.string.client_take_ride_title));
            change.setVisibility(View.VISIBLE);
            spinner_airport_location_destination.setVisibility(View.VISIBLE);
        } else if (option == PICKUP_AIRPORT){
            //show_title.setText(getString(R.string.txt_send_merchandise));
            //getActivity().getActionBar().setTitle(getString(R.string.client_send_merchandise_title));
            departure_address.setVisibility(View.GONE);
            spinner_airport_location_departure.setVisibility(View.VISIBLE);
            change.setVisibility(View.VISIBLE);
        }
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

        String[] location = {"桃園第一航廈", "桃園第二航廈", "松山機場", "高雄機場"};
        arrayAdapter_location = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, location);
        spinner_airport_location_departure.setAdapter(arrayAdapter_location);
        spinner_airport_location_destination.setAdapter(arrayAdapter_location);

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
