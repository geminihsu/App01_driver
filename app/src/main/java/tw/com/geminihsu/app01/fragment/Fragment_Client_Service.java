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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.common.Constants;

public class Fragment_Client_Service extends Fragment {

    private LinearLayout linearLayout_taxi_service;
    private LinearLayout linearLayout_client_service;

    private ImageButton taxi;
    private ImageButton truck;
    private ImageButton cargo;
    private ImageButton take_ride;
    private ImageButton send_merchandise;
    private ImageButton air_plane;
    private ImageButton train;

    private MapView mapView;
    private GoogleMap googleMap;
    private BroadcastReceiver getCurrentGPSLocationBroadcastReceiver;

    private boolean test = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        googleMap = mapView.getMap();
        if(!runtime_permissions())
        {
            setMapView(37.37044279,-122.00614899);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.findViews();
        setLister();
        if(getCurrentGPSLocationBroadcastReceiver!=null)
            getActivity().registerReceiver((getCurrentGPSLocationBroadcastReceiver), new IntentFilter("location_update"));


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
        getActivity().setTitle("");
        super.onResume();
        test = false;
        if (mapView != null) {
            mapView.onResume();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentGPSLocationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

              //  if(!test) {
                    // textView.append("\n" +intent.getExtras().get("coordinates"));
                if (intent.getExtras().containsKey("longitude")) {
                    double longitude = (double) intent.getExtras().get("longitude");
                    double latitude = (double) intent.getExtras().get("latitude");
                    googleMap.clear();
                    setMapView(longitude, latitude);
                    //test = true;
                }
               // }
            }
        };
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
        if (getCurrentGPSLocationBroadcastReceiver != null&&getCurrentGPSLocationBroadcastReceiver.isOrderedBroadcast()){
            getActivity().unregisterReceiver(getCurrentGPSLocationBroadcastReceiver);
            getCurrentGPSLocationBroadcastReceiver=null;
        }

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
        if (getCurrentGPSLocationBroadcastReceiver != null&&getCurrentGPSLocationBroadcastReceiver.isOrderedBroadcast()){
            getActivity().unregisterReceiver(getCurrentGPSLocationBroadcastReceiver);
            getCurrentGPSLocationBroadcastReceiver=null;
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


        taxi = (ImageButton) getView().findViewById(R.id.normal);
        truck = (ImageButton) getView().findViewById(R.id.truck);
        cargo = (ImageButton) getView().findViewById(R.id.dock);

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
         taxi.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 linearLayout_taxi_service.setVisibility(View.VISIBLE);
                 linearLayout_client_service.setVisibility(View.GONE);
             }
         });

         truck.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Fragment newFragment = new Fragment_MerchandiseDorkPickUp();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();

                 transaction.replace(R.id.container, newFragment);
                 transaction.addToBackStack(null);

                 transaction.commit();
             }
         });
         cargo.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Fragment newFragment = new Fragment_MerchandiseDorkPickUp();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();

                 transaction.replace(R.id.container, newFragment);
                 transaction.addToBackStack(null);

                 transaction.commit();
             }
         });

         take_ride.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Intent question = new Intent(getActivity(), ClientTakeRideActivity.class);
                 Bundle b = new Bundle();
                 b.putInt(Constants.ARG_POSITION, ClientTakeRideActivity.TAKE_RIDE);
                 question.putExtras(b);
                 startActivity(question);
             }
         });

         send_merchandise.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Intent question = new Intent(getActivity(), ClientTakeRideActivity.class);
                 Bundle b = new Bundle();
                 b.putInt(Constants.ARG_POSITION, ClientTakeRideActivity.SEND_MERCHANDISE);
                 question.putExtras(b);
                 startActivity(question);
             }
         });
         air_plane.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Fragment newFragment = new Fragment_ClientAirPlanePickUp();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();

                 transaction.replace(R.id.container, newFragment);
                 transaction.addToBackStack(null);

                 transaction.commit();
             }
         });

         train.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Fragment newFragment = new Fragment_TrainPlanePickUp();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();

                 transaction.replace(R.id.container, newFragment);
                 transaction.addToBackStack(null);

                 transaction.commit();
             }
         });
     }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {

            setMapView(37.37044279,-122.00614899);
        } else {
            runtime_permissions();
            // }
        }
    }
}