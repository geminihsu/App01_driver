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
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.geminihsu.app01.R;

public class Fragment_service extends Fragment {
	private TextView article ;
	private ImageView mImageView ;
	private ProgressDialog PDLoading ;
	private int mCurrentPosition = -1;

	private Handler handler = new Handler();

	
	private byte[] imagebuffer;
	
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
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.findViews();
        getActivity().setTitle("");

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
           // updateArticleView(args.getInt(SyncStateContract.Constants.ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
        
     
		
		
    }
    
    @Override
	public void onStop() {
		super.onStop();
		/*
		 * Remove any pending posts of callbacks and sent messages whose obj is token. 
		 * If token is null, all callbacks and messages will be removed."
		 * */
		handler.removeCallbacksAndMessages(null);
	}

	private void findViews()
    {
    	article = (TextView) getView().findViewById(R.id.service_info);
    	//mImageView= (ImageView) getView().findViewById(R.id.mImageView);
    	//設定所有view 的font size
    	//View main_layout = (View)getView().findViewById(R.id.main_layout);
		//DisplayUtil displayUtil=new DisplayUtil();
		//displayUtil.setFontSize(main_layout, getResources().getDimension(R.dimen.default_text_size_px));
    }
    public void updateArticleView(int position) {
        
        article.setText("Camera"+position);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
       // outState.putInt(Constants.ARG_POSITION, mCurrentPosition);
    }
    
	

	



}