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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.common.Constants;

public class Fragment_Taxi_Client_Service extends Fragment {
	private TextView article ;
	private ImageButton take_ride;
    private ImageButton send_merchandise;
    private ImageButton air_plane;
    private ImageButton train;
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
        return inflater.inflate(R.layout.fragment_taxi_client_service, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.findViews();
        setLister();

		
    }

    @Override
    public void onResume() {
        getActivity().setTitle("");
        super.onResume();


    }
    @Override
	public void onStop() {
		super.onStop();

	}

	private void findViews()
    {
    	article = (TextView) getView().findViewById(R.id.service_info);
        take_ride = (ImageButton) getView().findViewById(R.id.normal);
        send_merchandise = (ImageButton) getView().findViewById(R.id.truck);
        air_plane = (ImageButton) getView().findViewById(R.id.airplane);
        train = (ImageButton) getView().findViewById(R.id.train);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
       // outState.putInt(Constants.ARG_POSITION, mCurrentPosition);
    }
    
	
     private void setLister()
     {
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
	



}