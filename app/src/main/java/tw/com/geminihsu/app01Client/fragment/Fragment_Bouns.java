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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.geminihsu.app01Client.BoundsRecordActivity;
import tw.com.geminihsu.app01Client.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01Client.adapter.PrizeListItem;
import tw.com.geminihsu.app01Client.adapter.PrizeListItemAdapter;
import tw.com.geminihsu.app01Client.bean.AccountTreeInfo;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01Client.utils.Utility;


public class Fragment_Bouns extends Fragment {
    private final String TAG = Fragment_Bouns.class.toString();

    private ListView listView;
    private final List<PrizeListItem> mPrizeListData = new ArrayList<PrizeListItem>();;
    private PrizeListItemAdapter listViewAdapter;
    private Button record;
    private Button upgrade;
    private TextView next_time;

    private AccountTreeInfo treeInfo = null;
    private JsonPutsUtil sendDataRequest;

    private CountDownTimer countDownTimer;

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
        Utility info = new Utility(getActivity());
        RealmUtil data = new RealmUtil(getActivity());
        Log.e(TAG,"Account Id:"+info.getAccountInfo().getId());
            treeInfo  = data.queryUserTreeInfo(Constants.ACCOUNT_USER_ID,info.getAccountInfo().getId());
        if(treeInfo == null)
            treeInfo  = data.queryUserTreeInfo(Constants.ACCOUNT_USER_ID,Integer.valueOf(info.getAccountInfo().getUid()));
        sendDataRequest = new JsonPutsUtil(getActivity());

        sendDataRequest.setCustomerTreeWateringFeedBackManagerCallBackFunction(new JsonPutsUtil.CustomerTreeWateringCallBackFunction() {


            @Override
            public void sendStatus(boolean status) {
                    if(status)
                    {
                        Toast.makeText(getActivity(),
                                getString(R.string.txt_upgrade)+ Integer.valueOf(treeInfo.getLv()+1) +"級成功",
                                Toast.LENGTH_LONG).show();
                        /*if(countDownTimer == null)
                        {
                            wateringTimer();
                        }*/
                        if(countDownTimer!=null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        next_time.setText("倒數時間: 2 小時 00 分 00 秒");


                        if (countDownTimer == null) {
                            long tsLong = System.currentTimeMillis()/1000;

                            wateringTimer(tsLong, 7200);

                        }else
                            countDownTimer.start();

            }
            }
        });

        if(treeInfo.getLv() <4)
            listView.setVisibility(View.GONE);
        getDataFromDB();
        // 建立ListItemAdapter
        listViewAdapter = new PrizeListItemAdapter(getActivity(), 0, mPrizeListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        if (treeInfo.getNext() == 0) {
            next_time.setText("倒數時間: 0");
        }

        else if (countDownTimer == null) {


            long limit = treeInfo.getLast_watering();
            long next = treeInfo.getNext();

            wateringTimer(limit,next);


        }


    }

    private void wateringTimer(final long limit, final long next)
    {
        countDownTimer = new CountDownTimer(limit, 1000) {

            @Override
            public void onFinish() {
                next_time.setText(getString(R.string.txt_now_upgrade));
                countDownTimer = null;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //String countdownTime = DateTimeUtil.convertMillisecondsTimeToString_yyyymmdd_hhmmss(millisUntilFinished);
                //next_time.setText(countdownTime);
                //next_time.setText("倒數時間:" + new SimpleDateFormat("HH 時 mm 分 ss 秒").format(treeInfo.getLast_watering()  - (limit - treeInfo.getNext() - millisUntilFinished)));

                if(limit < 3600)
                    next_time.setText("倒數時間:" + new SimpleDateFormat(" 00 時 mm 分 ss 秒").format(limit  - ((limit + 72000) - next - millisUntilFinished)));
                else if(limit > 3600)
                    next_time.setText("倒數時間:" + new SimpleDateFormat(" 01 時 mm 分 ss 秒").format(limit  - ((limit + 72000) - next - millisUntilFinished)));


            }

        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle(getString(R.string.prize_record_page_title));

    }

    @Override
	public void onStop() {
		super.onStop();
        if(countDownTimer!=null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

	}

    private void findViews()
    {
        listView = (ListView) getView().findViewById(R.id.listView1);
        record = (Button) getView().findViewById(R.id.bouns_record);
        upgrade = (Button) getView().findViewById(R.id.bouns_upgrade);
        next_time = (TextView) getView().findViewById(R.id.next_time);

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

                        Utility info = new Utility(getActivity());
                        sendDataRequest.sendCustomerGainBound(info.getAccountInfo());

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

        upgrade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
                    @Override
                    public void run() {
                        Utility info = new Utility(getActivity());
                        sendDataRequest.sendCustomerTreeWatering(info.getAccountInfo());
                    }
                }));

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
                for (int i = 0; i < 1; i++) {
                    // for listview 要用的資料
                    Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera_72x72);
                    //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                    PrizeListItem item = new PrizeListItem();
                    if(treeInfo.getLv() == 4)
                        item.prize_title = "積分200點";
                    item.take = "領取";
                    mPrizeListData.add(item);


                }

            } catch (Throwable t) {
                Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
            }

    }



}
