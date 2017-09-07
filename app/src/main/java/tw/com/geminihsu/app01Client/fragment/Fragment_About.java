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
package tw.com.geminihsu.app01Client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.serverbean.ServerContents;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.RealmUtil;


public class Fragment_About extends Fragment {

    private ListView listView;
    private WebView browser;
    private TextView about;
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
        return inflater.inflate(R.layout.support_answer_activity, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.findViews();
        this.setLister();

    }

    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.about_page_title));
        super.onResume();


    }

    @Override
	public void onStop() {
		super.onStop();

	}

    private void findViews()
    {
        browser = (WebView) getView().findViewById(R.id.webview);
        //browser.setWebViewClient(new MyBrowser("www.google.com"));
        //browser.setVisibility(View.VISIBLE);

        about = (TextView) getView().findViewById(R.id.content);
        about.setVisibility(View.VISIBLE);
        RealmUtil data = new RealmUtil(getActivity());
        ServerContents info = data.queryServerContent(Constants.SERVER_CONTENT_CODE,"about");
        about.setText("\t\t\t\t"+info.getContent());
        /*String url = "www.google.com";

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(url);*/
    }


    private void setLister()
    {

        browser.loadUrl("http://www.tutorialspoint.com");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }
    
	

	



}