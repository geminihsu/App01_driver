package tw.com.geminihsu.app01Client;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01Client.bean.NormalOrder;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;

public class ClientWaitCarActivity extends Activity implements LocationListener {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private ImageView checkdriver;
    private ImageButton phone;
    private Button checklocation;
    private Button shareTakeInfo;
    private TextView fee;
    private TextView driver_name;
    private TextView car_type;
    private TextView car_brand;
    private TextView show_distance;


    public static final String LINE_PACKAGE_NAME = "jp.naver.line.android";
    public static final String LINE_CLASS_NAME = "jp.naver.line.android.activity.selectchat.SelectChatActivity";

    private BroadcastReceiver finishOrderBroadcastReceiver;

    private NormalOrder order;
    private String driverUid;

    private JsonPutsUtil sendDataRequest;
    private Double driver_lat,driver_lng;

    private LocationManager locationManager;
    private String provider;
    private double longitude;
    private double latitude;



    private float distance=0;
    private DriverIdentifyInfo driverInfo;
    private String between_distance="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tw.com.geminihsu.app01Client.R.layout.client_order_car_wait_for_driver);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(ClientWaitCarActivity.this);

        sendDataRequest.setAccountQueryUserLocationManagerCallBackFunction(new JsonPutsUtil.AccountQueryUserLocationManagerCallBackFunction() {


            @Override
            public void getLocationInfo(DriverIdentifyInfo driver, Double lat, Double lng) {
                /*Intent map = new Intent(ClientWaitCarActivity.this, MapsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.DISPLAY_USER_LOCATION);
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                map.putExtras(b);
                startActivity(map);*/
                driverInfo =driver;
                driver_lat = lat;
                driver_lng = lng;

                Location crntLocation=new Location("crntlocation");
                crntLocation.setLatitude(driver_lat);
                crntLocation.setLongitude(driver_lng);

                if(latitude!=0.0) {
                    Location newLocation = new Location("newlocation");
                    newLocation.setLatitude(latitude);
                    newLocation.setLongitude(longitude);


                    //float distance = crntLocation.distanceTo(newLocation);  in meters
                    distance = crntLocation.distanceTo(newLocation) / 1000; // in km
                    //double d = distance;
                    //DecimalFormat df=new DecimalFormat("#.##");
                    //double t = Double.parseDouble(df.format(d));
                    String tmp = String.valueOf(distance);
                    between_distance = tmp.substring(0,3);
                    driver_name.setText(driver.getRealname());
                    show_distance.setText("距您 "+between_distance+" 公里");
                    Constants.APP_REGISTER_DRIVER_TYPE dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(driver.getDtype()));

                    if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                        car_type.setText("車型:"+getString(R.string.taxi_driver));
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER) {
                        car_type.setText("車型:"+getString(R.string.Uber_driver));
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK) {
                        car_type.setText("車型:"+getString(R.string.truck_driver));
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO) {
                        car_type.setText("車型:"+getString(R.string.cargo_driver));
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER) {
                        car_type.setText("車型:"+getString(R.string.trailer_driver));
                    }

                    car_brand.setText("車牌:"+driver.getCar_brand());
                }
            }
        });

        finishOrderBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Intent question = new Intent(ClientWaitCarActivity.this, MenuMainActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        };
        if (!runtime_permissions()&&!runtime_permissions_call()) {
            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);


            if (location != null) {
                onLocationChanged(location);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ORDER_TICKET_ID)) {
                String ticket_id = bundle.getString(Constants.ORDER_TICKET_ID);
                driverUid = bundle.getString(Constants.ACCOUNT_DRIVER_UID);
                RealmUtil database = new RealmUtil(this);
                order = database.queryOrder(Constants.ORDER_TICKET_ID,ticket_id);
                fee.setText("小費:\t"+order.getTip()+"元");
            }
        }
         this.setLister();
        if(finishOrderBroadcastReceiver!=null)
            registerReceiver((finishOrderBroadcastReceiver), new IntentFilter("finish_order"));

        sendDataRequest.getUserLocation(order,Integer.valueOf(driverUid));



    }

    private void findViews()
    {
        checkdriver = (ImageView) findViewById(R.id.name);
        phone = (ImageButton) findViewById(R.id.call);
        checklocation = (Button) findViewById(R.id.location);
        shareTakeInfo = (Button) findViewById(R.id.share);

        fee = (TextView) findViewById(R.id.fee);
        show_distance = (TextView) findViewById(R.id.distance);
        if(!between_distance.equals(""))
            show_distance.setText("距您 "+between_distance+" 公里");
        driver_name = (TextView) findViewById(R.id.driver_name);
        car_type = (TextView) findViewById(R.id.driver_type);
        car_brand = (TextView) findViewById(R.id.driver_brand);
    }


    private void setLister()
    {
        checkdriver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(ClientWaitCarActivity.this, DriverAccountActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                question.putExtras(b);
                startActivity(question);
            }
        });

        checklocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Utility info = new Utility(ClientWaitCarActivity.this);
                //AccountInfo accountInfo = info.getAccountInfo();

                //sendDataRequest.getUserLocation(order,Integer.valueOf(driverUid));
                /*Intent question = new Intent(ClientWaitCarActivity.this, MapsActivity.class);
                startActivity(question);*/
                Intent map = new Intent(ClientWaitCarActivity.this, MapsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.DISPLAY_USER_LOCATION);
                b.putDouble("lat", driver_lat);
                b.putDouble("lng", driver_lng);
                map.putExtras(b);
                startActivity(map);
            }
        });

        shareTakeInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ClientWaitCarActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ClientWaitCarActivity.this,
                android.R.layout.select_dialog_item);
        arrayAdapter.add(getString(R.string.txt_share_sms));
        arrayAdapter.add(getString(R.string.txt_share_line));
        arrayAdapter.add(getString(R.string.txt_share_email));


                builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                        switch (which){
                            case 0:
                                Intent question = new Intent(ClientWaitCarActivity.this, SupportAnswerActivity.class);
                                Bundle b = new Bundle();
                                b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.SMS_REPORT);
                                question.putExtras(b);
                                startActivity(question);
                                break;
                            case 1:
//                                Intent page = new Intent(ClientWaitCarActivity.this, BookmarksMapListActivity.class);
//                                startActivity(page);
                                //發送訊息到line
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setClassName(LINE_PACKAGE_NAME, LINE_CLASS_NAME);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, "我在2015年12月15日上午09:00搭乘車牌EHE-530車輛，從台中火車站前往台中市政府。");
                                startActivity(intent);

                                break;
                            case 2:
                                //發送訊息到email
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("message/rfc822");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"abc@email.com"});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "我在搭車");
                                emailIntent.putExtra(Intent.EXTRA_TEXT   , "我在2015年12月15日上午09:00搭乘車牌EHE-530車輛，從台中火車站前往台中市政府。");

                                try {
                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                    finish();
                                    //Log.i("Finished sending email...", "");
                                }
                                catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(ClientWaitCarActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
        builderSingle.show();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runtime_permissions_call()) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driverInfo.getName()));

                    if (ActivityCompat.checkSelfPermission(ClientWaitCarActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

       /* MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.txt_cancel_order));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                /*Intent question = new Intent(ClientWaitCarActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.CANCEL_ORDER_FEEDBACK);
                question.putExtras(b);
                startActivity(question);*/
                //cancel order

                //sendDataRequest.clientCancelOrder(order);
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if (finishOrderBroadcastReceiver != null&&finishOrderBroadcastReceiver.isOrderedBroadcast()){
            unregisterReceiver(finishOrderBroadcastReceiver);
            finishOrderBroadcastReceiver=null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driverInfo.getName()));

                if (ActivityCompat.checkSelfPermission(ClientWaitCarActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (finishOrderBroadcastReceiver != null&&finishOrderBroadcastReceiver.isOrderedBroadcast()){
            unregisterReceiver(finishOrderBroadcastReceiver);
            finishOrderBroadcastReceiver=null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e("Map:",""+latitude);
        Log.e("Map:",""+longitude);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    private boolean runtime_permissions_call() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE, android.Manifest.permission.CALL_PHONE,android.Manifest.permission.ACCESS_FINE_LOCATION,},100);

            return true;
        }
        return false;
    }
}
