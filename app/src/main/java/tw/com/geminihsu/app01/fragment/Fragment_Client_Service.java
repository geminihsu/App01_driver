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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.SupportAnswerActivity;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class Fragment_Client_Service extends Fragment {
	private TextView article ;
	private ImageButton take_ride;
	
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
        return inflater.inflate(R.layout.fragment_client_service, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.findViews();
        setLister();

		
    }
    
    @Override
	public void onStop() {
		super.onStop();

	}

	private void findViews()
    {
    	article = (TextView) getView().findViewById(R.id.service_info);
        take_ride = (ImageButton) getView().findViewById(R.id.normal);
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
                 startActivity(question);
             }
         });
     }
	



}