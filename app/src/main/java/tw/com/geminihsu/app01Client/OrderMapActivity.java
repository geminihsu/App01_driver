package tw.com.geminihsu.app01Client;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.LocationAddress;
import tw.com.geminihsu.app01Client.common.Constants;

public class OrderMapActivity extends Activity implements  LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    protected MapView mMapView;

    private GoogleMap googleMap;
    private int provide_location;
    private double longitude;
    private double latitude;

    private LocationManager locationManager;
    private String provider;


    private CheckBox bookmark;
    private EditText bookmarkTitle;
    private EditText searchMap;

    private ImageButton search;
    private AlertDialog alertDialog;
    private String searchAddress;

    private static final int REQUEST_EXTERNAL_GPS = 1;
    private static String[] PERMISSIONS_GPS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean getLocation = true;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private LocationAddress result;
    private String curAddress = "";
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(tw.com.geminihsu.app01Client.R.layout.order_map_activity);
        mMapView = (MapView) findViewById(R.id.activity_embedded_map_view_mapview);
        mMapView.onCreate(savedInstanceState);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        Bundle args = getIntent().getExtras();
        if (args != null)
            provide_location = args.getInt(Constants.ARG_POSITION);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        result=new LocationAddress();
        mGoogleApiClient.connect();
        this.findViews();
        setLister();

        //if(getCurrentGPSLocationBroadcastReceiver!=null)
        //   registerReceiver((getCurrentGPSLocationBroadcastReceiver), new IntentFilter("location_update"));


    }

    private void findViews() {
        // Gets to GoogleMap from the MapView and does initialization stuff
        googleMap = mMapView.getMap();
        bookmark = (CheckBox) findViewById(R.id.addbookmark);
        bookmarkTitle = (EditText) findViewById(R.id.bookmark);
        searchMap = (EditText) findViewById(R.id.textView_title);

        search = (ImageButton) findViewById(R.id.search);
        send = (Button) findViewById(R.id.btn_save);
    }


    private void setLister() {
        /*bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked&&!bookmarkTitle.getText().toString().equals(""))
                {
                    LocationAddress location = coverGPSAddress(bookmarkTitle.getText().toString());
                    USerBookmark item = new USerBookmark();
                    item.setLat(""+location.getLatitude());
                    item.setLng(""+location.getLongitude());
                    item.setLocation(bookmarkTitle.getText().toString());
                    item.setStreetAddress(location.getAddress());

                    //新增地點到資料庫
                    RealmUtil database = new RealmUtil(OrderMapActivity.this);
                    database.addUserBookMark(item);
                }
            }
        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!searchMap.getText().toString().equals("")) {
                    if (locationManager != null)
                        if (ActivityCompat.checkSelfPermission(OrderMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                    Geocoder fwdGeocoder = new Geocoder(OrderMapActivity.this);

                    String streetAddress = searchMap.getText().toString();
                    List<Address> locations = null;
                    try {
                        locations = fwdGeocoder.getFromLocationName(streetAddress, 10);
                    } catch (IOException e) {
                    }

                    googleMap.clear();
                    latitude = locations.get(0).getLatitude();
                    longitude = locations.get(0).getLongitude();
                    searchAddress =  locations.get(0).getAddressLine(0);
                    setMapView(locations.get(0).getLongitude(), locations.get(0).getLatitude());
                } else {
                    alert();
                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude!=0||getLocation)
                    returnAddress();
            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;

            case ACTIONBAR_MENU_ITEM_SUMMIT:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }

    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();

    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
               // Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        /*if (getCurrentGPSLocationBroadcastReceiver != null){
            unregisterReceiver(getCurrentGPSLocationBroadcastReceiver);
            getCurrentGPSLocationBroadcastReceiver=null;
        }*/
        super.onDestroy();
    }


    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        /*if (getCurrentGPSLocationBroadcastReceiver != null){
            unregisterReceiver(getCurrentGPSLocationBroadcastReceiver);
            getCurrentGPSLocationBroadcastReceiver=null;
        }*/
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
                // Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        if(longitude!=0)
        {
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putSerializable(Constants.BUNDLE_LOCATION, coverGPSAddress(bookmarkTitle.getText().toString()));
            b.putBoolean(ClientTakeRideActivity.BUNDLE_KEEP_BOOMARK,bookmark.isChecked());
            b.putString(Constants.BUNDLE_MAP_LATITUDE, "" + latitude);
            b.putString(Constants.BUNDLE_MAP_LONGITUDE, "" + longitude);
            i.putExtras(b);
            if (provide_location == Constants.DEPARTURE_QUERY_GPS)
                setResult(Constants.DEPARTURE_QUERY_GPS, i);
            else if (provide_location == Constants.DESTINATION_QUERY_GPS)
                setResult(Constants.DESTINATION_QUERY_GPS, i);
            else if (provide_location == Constants.STOP_QUERY_GPS)
                setResult(Constants.STOP_QUERY_GPS, i);

        }

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /*MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.sure_take_spec));
       SpannableString spanString = new SpannableString(item.getTitle().toString());
       spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
       item.setTitle(spanString);
       item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
        return true;
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }
    /*********************************************************************
     * FUNCTION: setMapView
     * PURPOSE: display marker on the google map view
     *
     * PARAMETERS: double longitude,double latitude
     **********************************************************************/
    public void setMapView(double longitude,double latitude) {
        if (mMapView != null) {

            googleMap = mMapView.getMap();

            //Creating a LatLng Object to store Coordinates





            LatLng latLng = new LatLng(latitude, longitude);

            //Adding marker to map
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng) //setting position
                    .draggable(true) //Making the marker draggable
                    .title("Current Location")); //Adding a title
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

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
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(latitude, longitude));

            LatLng coordinate = new LatLng(latitude, longitude); //Store these lat lng values somewhere. These should be constant.
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    coordinate, 17);
            //googleMap.moveCamera(location);
            googleMap.animateCamera(location);

        }
    }


    /*********************************************************************
     * FUNCTION: onRequestPermissionsResult
     * PURPOSE: ask user for permission to access location and reverses address
     * mapping gps location
     *
     * PARAMETERS: int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
     **********************************************************************/
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){

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
        }else {
            runtime_permissions();
            // }
        }
    }*/

    private void returnAddress()
    {
        /*List<Address> addresses = null;
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            addresses = gc.getFromLocation(latitude,longitude, 10);
        } catch (IOException e) {
        }
*/

        //Toast.makeText(this, addresses.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();

        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable(Constants.BUNDLE_LOCATION, coverGPSAddress(bookmarkTitle.getText().toString()));
        b.putBoolean(ClientTakeRideActivity.BUNDLE_KEEP_BOOMARK,bookmark.isChecked());
        b.putString(Constants.BUNDLE_MAP_LATITUDE, "" + latitude);
        b.putString(Constants.BUNDLE_MAP_LONGITUDE, "" + longitude);
        if(getLocation)
            b.putString(Constants.BUNDLE_MAP_CURRENT_ADDRESS,curAddress);
        i.putExtras(b);
        if (provide_location == Constants.DEPARTURE_QUERY_GPS)
            setResult(Constants.DEPARTURE_QUERY_GPS, i);
        else if (provide_location == Constants.DESTINATION_QUERY_GPS)
            setResult(Constants.DESTINATION_QUERY_GPS, i);
        else if (provide_location == Constants.STOP_QUERY_GPS)
            setResult(Constants.STOP_QUERY_GPS, i);
        finish();

    }

    private LocationAddress coverGPSAddress(String bookmarkTitle)
    {
        LocationAddress result=new LocationAddress();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }

                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());

                result.setLatitude(latitude);
                result.setLongitude(longitude);

                result.setCountryName(address.getCountryName());
                result.setLocality(address.getLocality());
                result.setZipCode(address.getPostalCode());

                if(!bookmarkTitle.equals(""))
                    result.setLocation(bookmarkTitle);
                else
                    result.setLocation(sb.toString());

                if(!searchMap.getText().toString().equals("")) {
                    result.setLocation(searchMap.getText().toString()+"("+searchAddress+")");
                    result.setAddress(searchMap.getText().toString()+"("+searchAddress+")");
                }else {
                    if(address.getCountryCode().equals("TW")) {
                        result.setAddress(address.getAddressLine(0).substring(3, address.getAddressLine(0).length()));
                        result.setLocation(address.getAddressLine(0).substring(3, address.getAddressLine(0).length()));
                    }else{
                        result.setAddress(sb.toString());
                        result.setLocation(sb.toString());

                    }

                }

            }
        } catch (IOException e) {
        Log.e("", "Unable connect to Geocoder", e);
    }
        return  result;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            String strLocation =
                    DateFormat.getTimeInstance().format(location.getTime()) + "\n" +
                            "Latitude=" + location.getLatitude() + "\n" +
                            "Longitude=" + location.getLongitude();
            Log.e("TAG","find current:"+strLocation);
            if(getLocation) {
                setMapView(location.getLongitude(), location.getLatitude());
                getLocation = false;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getCurrentAddress(location.getLongitude(),location.getLatitude());
            }
        }

    }



    private void alert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.map_error_title));

            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_register_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.sure_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertDialog = null;
                        }
                    });

            // create alert dialog
            if (alertDialog == null) {
                alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_GPS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    private void getCurrentAddress(double longitude,double latitude)
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }

                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());

                result.setLatitude(latitude);
                result.setLongitude(longitude);

                result.setCountryName(address.getCountryName());
                result.setLocality(address.getLocality());
                result.setZipCode(address.getPostalCode());

                result.setLocation(sb.toString());

                if(address.getCountryCode().equals("TW")) {
                    result.setAddress(address.getAddressLine(0).substring(3, address.getAddressLine(0).length()));
                    result.setLocation(address.getAddressLine(0).substring(3, address.getAddressLine(0).length()));
                }else{
                    result.setAddress(sb.toString());
                    result.setLocation(sb.toString());

                }
                curAddress = result.getAddress();
            }
        }catch (IOException e) {
            Log.e("", "Unable connect to Geocoder", e);
        }
        //test = true;

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
