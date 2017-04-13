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

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.ClientTakeRideSearchActivity;
import tw.com.geminihsu.app01.MainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.LocationAddress;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.bean.OrderLocationBean;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.DateTimeUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class Fragment_Client_Service_test extends Fragment  implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final int ACTIONBAR_MENU_ITEM_FIILTER = 0x0001;

    private LinearLayout linearLayout_taxi_service;
    private LinearLayout linearLayout_client_service;

    private LinearLayout linearLayout_take_ride;
    private LinearLayout linearLayout_send_merchandise;
    private LinearLayout linearLayout_air_plane;
    private LinearLayout linearLayout_train;

    private ImageButton taxi;
    private ImageButton uber;
    private ImageButton cargo;
    private ImageButton truck;
    private ImageButton trailer;


    private ImageButton take_ride;
    private ImageButton send_merchandise;
    private ImageButton air_plane;
    private ImageButton train;

    private MapView mapView;
    private GoogleMap googleMap;

    private Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private Constants.APP_REGISTER_ORDER_TYPE orderCargoType;
    private boolean isShowOneKey;
    private String curAddress = "";

    private JsonPutsUtil sendDataRequest;
    private LocationAddress departure_detail;
    private ProgressDialog progressDialog_loading;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean getLocation = true;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private TimerTask task;
    private Timer timer;

    private LocationAddress result;

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
        //return inflater.inflate(R.layout.fragment_client_service, container, false);
        View view = inflater.inflate(R.layout.fragment_client_service, container, false);
        mapView = (MapView) view.findViewById(R.id.fragment_embedded_map_view_mapview);
        mapView.onCreate(savedInstanceState);
        //setMapView(37.37044279,-122.00614899);
        setMapView(25.0477,121.518);
        googleMap = mapView.getMap();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        result=new LocationAddress();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        this.findViews();
        setLister();

        if(task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    getLocation = true;
                }
            };

            timer = new Timer();
        }


        timer.schedule(task, 1000, 2000);
    }
        @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();



    }


    public void setMapView(double longitude,double latitude) {
        if (mapView != null) {

            googleMap = mapView.getMap();

            //Creating a LatLng Object to store Coordinates
            LatLng latLng = new LatLng(latitude, longitude);

            //Adding marker to map
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng) //setting position
                    .draggable(true) //Making the marker draggable
                    .title("Current Location")); //Adding a title
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            MapsInitializer.initialize(getActivity());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(latitude, longitude));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.service_info));
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sendDataRequest = new JsonPutsUtil(getActivity());
        sendDataRequest.setServerRequestOrderManagerCallBackFunction(new JsonPutsUtil.ServerRequestOrderManagerCallBackFunction() {

            @Override
            public void createNormalOrder(NormalOrder order) {

                if(progressDialog_loading!=null) {
                    progressDialog_loading.cancel();
                    progressDialog_loading = null;
                }
                Intent intent = new Intent(getActivity(), ClientTakeRideSearchActivity.class);

                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Integer.valueOf(order.getTicket_id()));
                intent.putExtras(b);
                startActivity(intent);
                //finish();
            }

            @Override
            public void cancelNormalOrder(NormalOrder order) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
	public void onStop() {
		super.onStop();
        if(progressDialog_loading!=null) {
            progressDialog_loading.cancel();
            progressDialog_loading = null;
        }
        mGoogleApiClient.disconnect();

        if(task!=null) {
            task.cancel();
            task = null;
        }
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }

        //isShowOneKey = false;
        //getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            try {
                mapView.onDestroy();
            } catch (NullPointerException e) {
               // Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }

         if(task!=null) {
             task.cancel();
             task = null;
         }
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }


	private void findViews()
    {
        linearLayout_taxi_service = (LinearLayout) getView().findViewById(R.id.taxi_linearLayout);
        linearLayout_client_service = (LinearLayout) getView().findViewById(R.id.camera1_linearLayout);

        linearLayout_take_ride = (LinearLayout) getView().findViewById(R.id.taxi_take_ride_linearLayout);
        linearLayout_send_merchandise = (LinearLayout) getView().findViewById(R.id.taxi_send_merchandise_linearLayout);
        linearLayout_air_plane = (LinearLayout) getView().findViewById(R.id.taxi_send_pick_airport_linearLayout);
        linearLayout_train = (LinearLayout) getView().findViewById(R.id.taxi_send_pick_train_linearLayout);



        taxi = (ImageButton) getView().findViewById(R.id.normal);
        uber = (ImageButton) getView().findViewById(R.id.uber);
        truck = (ImageButton) getView().findViewById(R.id.truck);
        cargo = (ImageButton) getView().findViewById(R.id.dock);
        trailer = (ImageButton) getView().findViewById(R.id.trailer);


        take_ride = (ImageButton) getView().findViewById(R.id.taxi_normal);
        send_merchandise = (ImageButton) getView().findViewById(R.id.taxi_truck);
        air_plane = (ImageButton) getView().findViewById(R.id.airplane);
        train = (ImageButton) getView().findViewById(R.id.train);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
        // Save the current article selection in case we need to recreate the fragment
       // outState.putInt(Constants.ARG_POSITION, mCurrentPosition);
    }


     private void setLister()
     {
         //第一層
         taxi.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 linearLayout_taxi_service.setVisibility(View.VISIBLE);
                 linearLayout_client_service.setVisibility(View.GONE);
                 //linearLayout_send_merchandise.setVisibility(View.GONE);
                 dataType = Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI;
                 isShowOneKey = true;
                 getActivity().invalidateOptionsMenu();
             }
         });

         uber.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 linearLayout_taxi_service.setVisibility(View.VISIBLE);
                 linearLayout_client_service.setVisibility(View.GONE);
                 dataType = Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER;

             }
         });

         truck.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 linearLayout_taxi_service.setVisibility(View.VISIBLE);
                 linearLayout_client_service.setVisibility(View.GONE);
                 linearLayout_take_ride.setVisibility(View.GONE);
                 linearLayout_air_plane.setVisibility(View.GONE);
                 linearLayout_train.setVisibility(View.GONE);
                 dataType = Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK;



             }
         });
         cargo.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 linearLayout_taxi_service.setVisibility(View.VISIBLE);
                 linearLayout_client_service.setVisibility(View.GONE);
                 linearLayout_take_ride.setVisibility(View.GONE);
                 linearLayout_air_plane.setVisibility(View.GONE);
                 linearLayout_train.setVisibility(View.GONE);
                 dataType = Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO;
          }
         });

         trailer.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 linearLayout_taxi_service.setVisibility(View.VISIBLE);
                 linearLayout_client_service.setVisibility(View.GONE);
                 linearLayout_take_ride.setVisibility(View.GONE);
                 linearLayout_air_plane.setVisibility(View.GONE);
                 linearLayout_train.setVisibility(View.GONE);
                 dataType = Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER;

             }
         });

         //第二層
         take_ride.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {


                 orderCargoType =  Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE;

                 Intent question = new Intent(getActivity(), ClientTakeRideActivity.class);
                 Bundle b = new Bundle();
                 b.putInt(Constants.ARG_POSITION, ClientTakeRideActivity.TAKE_RIDE);
                 b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
                 b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
                 b.putString(ClientTakeRideActivity.BUNDLE_ORDER_CUR_ADDRESS, curAddress);

                 question.putExtras(b);
                 startActivity(question);
             }
         });

         send_merchandise.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                // if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER) {
                     orderCargoType =  Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE;


                     Intent question = new Intent(getActivity(), ClientTakeRideActivity.class);
                     Bundle b = new Bundle();
                     b.putInt(Constants.ARG_POSITION, ClientTakeRideActivity.SEND_MERCHANDISE);
                     b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
                     b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
                     b.putString(ClientTakeRideActivity.BUNDLE_ORDER_CUR_ADDRESS, curAddress);


                     question.putExtras(b);
                     startActivity(question);
                 /*}else {
                     Fragment newFragment = new Fragment_MerchandiseDorkPickUp();
                     FragmentTransaction transaction = getFragmentManager().beginTransaction();

                     transaction.replace(R.id.container, newFragment);
                     transaction.addToBackStack(null);

                     transaction.commit();
                 }*/
             }
         });
         air_plane.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 orderCargoType =  Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT;

                 Fragment newFragment = new Fragment_ClientAirPlanePickUp();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 Bundle b = new Bundle();
                 b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
                 b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
                 b.putString(ClientTakeRideActivity.BUNDLE_ORDER_CUR_ADDRESS, curAddress);

                 newFragment.setArguments(b);
                 transaction.replace(R.id.container, newFragment);
                 transaction.addToBackStack(null);

                 transaction.commit();
             }
         });

         train.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 orderCargoType =  Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN;

                 Bundle b = new Bundle();
                 b.putInt(Constants.ARG_POSITION, ClientTakeRideActivity.TAKE_RIDE);
                 b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
                 b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
                 b.putString(ClientTakeRideActivity.BUNDLE_ORDER_CUR_ADDRESS, curAddress);


                 Fragment newFragment = new Fragment_TrainPlanePickUp();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 newFragment.setArguments(b);
                 transaction.replace(R.id.container, newFragment);
                 transaction.addToBackStack(null);

                 transaction.commit();
             }
         });
     }

    //計程車司機快速叫車
    private void createQuickTaxiOrder()
    {

        Utility info = new Utility(getActivity());
        AccountInfo driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude(""+result.getLatitude());
        begin_location.setLongitude(""+result.getLongitude());
        //String zip = departure_detail.getAddress().substring(0,3);
        begin_location.setZipcode(result.getZipCode());
        begin_location.setAddress(result.getAddress());







        //long unixTime = System.currentTimeMillis() / 1000L;


        final NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());

        Date dateIfo=new Date();
        String curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo);

        //Log.e(TAG,"time stamp:"+time.getText().toString());
        long order_timeStamp = DateTimeUtil.convertString_yyyymmddToMillisecondsTime(curDate);

        order.setTimebegin(""+order_timeStamp);
        order.setDtype(""+1);
        order.setCargo_type(""+1);
        order.setBegin(begin_location);
        order.setBegin_address(begin_location.getAddress());
        order.setTicket_status("0");
        order.setOrderdate(curDate);
        order.setTarget("1");

        order.setPrice("0");
        order.setTip("0");




        //sendDataRequest.putCreateQuickTaxiOrder(order);
        if(progressDialog_loading==null) {
            progressDialog_loading = ProgressDialog.show(getActivity(), "",
                    "Loading. Please wait...", true);
        }
        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                sendDataRequest.putCreateQuickTaxiOrder(order);
            }
        }));


    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_FIILTER, Menu.NONE, getString(R.string.order_call_taxi_page_title));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);
        Utility driver = new Utility(getActivity());

        if(isShowOneKey&&driver.getAllDriverAccountInfo().isEmpty())
            item.setVisible(true);
        else
            item.setVisible(false);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_FIILTER:
                createQuickTaxiOrder();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCurrentAddress(double longitude,double latitude)
    {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            String strLocation =
                    DateFormat.getTimeInstance().format(location.getTime()) + "\n" +
                            "Latitude=" + location.getLatitude() + "\n" +
                            "Longitude=" + location.getLongitude();
            Log.e("TAG",strLocation);
            if(getLocation) {
                setMapView(location.getLongitude(), location.getLatitude());
                getLocation = false;
                getCurrentAddress(location.getLongitude(),location.getLatitude());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, this);
                    }catch(SecurityException e){
                        Toast.makeText(getActivity(),
                                "SecurityException:\n" + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}