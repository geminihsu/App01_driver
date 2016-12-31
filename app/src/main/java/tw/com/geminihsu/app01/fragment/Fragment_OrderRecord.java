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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.SupportAnswerActivity;
import tw.com.geminihsu.app01.adapter.OrderRecordListItem;
import tw.com.geminihsu.app01.adapter.OrderRecordListItemAdapter;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class Fragment_OrderRecord extends Fragment {

    private ListView listView;
    private final List<OrderRecordListItem> mRecordOrderListData = new ArrayList<OrderRecordListItem>();;
    private OrderRecordListItemAdapter listViewAdapter;


    private JsonPutsUtil sendDataRequest;

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
        return inflater.inflate(R.layout.fragment_order_record, container, false);
    }

    @Override
    public void onStart() {
        sendDataRequest = new JsonPutsUtil(getActivity());
        super.onStart();
        this.findViews();
        setLister();
         //  getDataFromDB();
        getOrderList();
        // 建立ListItemAdapter
        listViewAdapter = new OrderRecordListItemAdapter(getActivity(), 0, mRecordOrderListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();



    }

    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.order_record_page_title));
        super.onResume();


    }

    @Override
	public void onStop() {
		super.onStop();

	}


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
       // outState.putInt(Constants.ARG_POSITION, mCurrentPosition);
    }


    private void findViews() {

        listView = (ListView) getView().findViewById(R.id.listView1);

        // 設定所有view 的font size
        // View main_layout = (View) getView().findViewById(R.id.main_layout);
        // DisplayUtil displayUtil = new DisplayUtil();
        // displayUtil.setFontSize(main_layout,
        // getResources().getDimension(R.dimen.default_text_size_px));
    }

    private void setLister()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final OrderRecordListItem order= mRecordOrderListData.get(position);


            }
        });
    }


    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_taxi);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_airport);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_shipping);

        mRecordOrderListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < 10; i++) {

                    if(i%3==0) {
                           //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                        OrderRecordListItem item = new OrderRecordListItem();
                        item.image = bm1;
                        item.order_status = "已取消";
                        item.order_status_fontColor = getResources().getColor(R.color.bg_gray);
                        item.time = "2015-12-18 上午07:04";
                        item.departure = "從台中市台灣大段一段一號";
                        item.destination = "到台中市政府";
                        item.pay_method = "照表收費";
                        item.car_status = "一般搭乘";
                        mRecordOrderListData.add(item);

                    }else if(i%3==1) {
                         //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                        OrderRecordListItem item = new OrderRecordListItem();
                        item.image = bm2;
                        item.order_status = "已完成";
                        item.order_status_fontColor = getResources().getColor(R.color.address_devicename_txt);
                        item.time = "2015-12-18 上午07:04";
                        item.departure = "從台中市台灣大段一段一號";
                        item.destination = "到台中市政府";
                        item.pay_method = "照表收費";
                        item.car_status = "";
                        mRecordOrderListData.add(item);

                    }
                    else

                    {
                        //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                        OrderRecordListItem item = new OrderRecordListItem();
                        item.image = bm3;
                        item.order_status = "進行中";
                        item.order_status_fontColor = getResources().getColor(R.color.btn_bouns_upgrade);
                        item.time = "2015-12-18 上午07:04";
                        item.departure = "從台中市台灣大段一段一號";
                        item.destination = "到台中市政府";
                        item.pay_method = "$500";
                        item.car_status = "";
                        mRecordOrderListData.add(item);

                    }
            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /* 從 xml 取得 OrderRecord 清單
    private void getDataFromDB() {
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_taxi);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_airport);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_shipping);

        mRecordOrderListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < MAXSIZE; i++) {

                if(i%3==0) {
                    //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                    OrderRecordListItem item = new OrderRecordListItem();
                    item.image = bm1;
                    item.order_status = "已取消";
                    item.order_status_fontColor = getResources().getColor(R.color.bg_gray);
                    item.time = "2015-12-18 上午07:04";
                    item.departure = "從台中市台灣大段一段一號";
                    item.destination = "到台中市政府";
                    item.pay_method = "照表收費";
                    item.car_status = "一般搭乘";
                    mRecordOrderListData.add(item);

                }else if(i%3==1) {
                    //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                    OrderRecordListItem item = new OrderRecordListItem();
                    item.image = bm2;
                    item.order_status = "已完成";
                    item.order_status_fontColor = getResources().getColor(R.color.address_devicename_txt);
                    item.time = "2015-12-18 上午07:04";
                    item.departure = "從台中市台灣大段一段一號";
                    item.destination = "到台中市政府";
                    item.pay_method = "照表收費";
                    item.car_status = "";
                    mRecordOrderListData.add(item);

                }
                else

                {
                    //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                    OrderRecordListItem item = new OrderRecordListItem();
                    item.image = bm3;
                    item.order_status = "進行中";
                    item.order_status_fontColor = getResources().getColor(R.color.btn_bouns_upgrade);
                    item.time = "2015-12-18 上午07:04";
                    item.departure = "從台中市台灣大段一段一號";
                    item.destination = "到台中市政府";
                    item.pay_method = "$500";
                    item.car_status = "";
                    mRecordOrderListData.add(item);

                }
            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/

    /* 從 xml 取得 OrderRecord 清單 */
    private void getOrderList() {
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_taxi);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_airport);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_shipping);

        Utility orders = new Utility(getActivity());
        RealmResults<NormalOrder> data=orders.getAccountOrderList();

        mRecordOrderListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (NormalOrder order : data) {


                    //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                    OrderRecordListItem item = new OrderRecordListItem();
                    Constants.APP_REGISTER_DRIVER_TYPE type = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));
                    if(type.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO))
                    {
                        item.image = bm3;
                        item.pay_method = "$"+order.getPrice()+"元";
                        item.car_status = "";
                    }
                    else if(type.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI)) {
                        item.image = bm1;
                        item.pay_method = "跳錶收費";
                        item.car_status = "一般搭乘";
                        item.car_status_Visibility = View.VISIBLE;
                    }

                    if(order.getTicket_status().equals("0"))
                       item.order_status = "配對中";
                    else
                    if(order.getTicket_status().equals("1"))
                       item.order_status = "進行中";
                    else
                    if(order.getTicket_status().equals("2"))
                        item.order_status = "已完成";


                item.order_status_fontColor = getResources().getColor(R.color.btn_bouns_upgrade);
                    item.time = order.getOrderdate();
                    item.departure = "從:"+order.getBegin_address();
                    item.destination = "到:"+order.getEnd_address();
                    item.normalOrder = order;
                    //item.car_status = "";
                    mRecordOrderListData.add(item);

            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}