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
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import tw.com.geminihsu.app01.SupportAnswerActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;


public class Fragment_support extends Fragment {

    private ListView listView;
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
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.findViews();
        this.setLister();

    }

    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.support_page_title));
        super.onResume();


    }
    @Override
	public void onStop() {
		super.onStop();

	}

	private void findViews()
    {
        listView = (ListView) getView().findViewById(R.id.listView1);



        Resources res =getResources();
        String[] menu=res.getStringArray(R.array.support_menu);
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < menu.length; ++i) {
            list.add(menu[i]);
        }
        ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,menu);
        //使用ListAdapter來顯示你輸入的文字

        listView.setAdapter(adapter);

    }

    private void setLister(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent question = new Intent(getActivity(), SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, position);
                question.putExtras(b);
                startActivity(question);


            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }
    
	

	



}