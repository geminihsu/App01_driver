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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01.common.Constants;

public class ClientTakeRideActivity extends Activity {

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
    private int option;

    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;

    private DatePickerDialog.OnDateSetListener datePicker;
    private TimePickerDialog.OnTimeSetListener timePicker;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_take_ride);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


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
                                        Intent question = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        startActivity(question);
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
                Intent question = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                startActivity(question);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            this);

                    // set title
                    alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("2015/12/08 上午07:04\n從:台中火車站\n停:繼光街口\n到:台中市政府")
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNegativeButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent question = new Intent(ClientTakeRideActivity.this, ClientTakeRideSearchActivity.class);
                                    startActivity(question);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }else
                {
                    Intent question = new Intent(ClientTakeRideActivity.this, MerchandiseOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.ARG_POSITION, MerchandiseOrderActivity.SEND_MERCHANDISE);
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
}
