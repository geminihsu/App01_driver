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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.BoundsRecordActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.adapter.PrizeListItem;
import tw.com.geminihsu.app01.adapter.PrizeListItemAdapter;
import tw.com.geminihsu.app01.common.Constants;


public class Fragment_Bouns extends Fragment {
    private ListView listView;
    private final List<PrizeListItem> mPrizeListData = new ArrayList<PrizeListItem>();;
    private PrizeListItemAdapter listViewAdapter;
    private Button record;

    private int MAXSIZE=10;
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
        return inflater.inflate(R.layout.fragment_bouns, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.findViews();
        this.setListView();
        getDataFromDB();
        // 建立ListItemAdapter
        listViewAdapter = new PrizeListItemAdapter(getActivity(), 0, mPrizeListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();



    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle(getString(R.string.prize_record_page_title));

    }

    @Override
	public void onStop() {
		super.onStop();

	}

    private void findViews()
    {
        listView = (ListView) getView().findViewById(R.id.listView1);
        record = (Button) getView().findViewById(R.id.bouns_record);

    }

    private void setListView()
    {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position,long id) {
               final int prize_position = position;

                Button take_prize = (Button) v.findViewById(R.id.btn_take);

                take_prize.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        });

        record.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(getActivity(), BoundsRecordActivity.class);
                startActivity(question);
            }
        });
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }



    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        mPrizeListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < MAXSIZE; i++) {
                // for listview 要用的資料
                Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera_72x72);
                //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                PrizeListItem item = new PrizeListItem();
                item.prize_title="50嵐珍珠奶茶兌換卷";
                item.take="領取";
                mPrizeListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }



}