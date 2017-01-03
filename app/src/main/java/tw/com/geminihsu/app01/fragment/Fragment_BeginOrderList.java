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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import io.realm.RealmResults;
import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.MerchandiseOrderActivity;
import tw.com.geminihsu.app01.OrderProcesssActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.SupportAnswerActivity;
import tw.com.geminihsu.app01.adapter.BeginOrderListItem;
import tw.com.geminihsu.app01.adapter.BeginOrderListItemAdapter;
import tw.com.geminihsu.app01.adapter.OrderRecordListItem;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class Fragment_BeginOrderList extends Fragment {
    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from
    public final static String BUNDLE_ORDER_TICKET = "ticket";// from

    private ListView listView;
    private final List<BeginOrderListItem> mRecordOrderListData = new ArrayList<BeginOrderListItem>();;
    private BeginOrderListItemAdapter listViewAdapter;

    private boolean wait = false;
    private int option;
    private JsonPutsUtil sendDataRequest;
    private ProgressDialog progressDialog_loading;
    private ArrayList<NormalOrder> orders;
    private RealmUtil database;
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
        super.onStart();
        this.findViews();
        orders = new ArrayList<NormalOrder>();
        sendDataRequest = new JsonPutsUtil(getActivity());
        sendDataRequest.setDriverRequestTakeOverOrderManagerCallBackFunction(new JsonPutsUtil.DriverRequestTakeOverOrderManagerCallBackFunction() {


            @Override
            public void driverTakeOverOrder(NormalOrder order) {
                Intent question = new Intent(getActivity(), OrderProcesssActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION,OrderProcesssActivity.PASSENGER);
                b.putString(BUNDLE_ORDER_TICKET_ID, order.getTicket_id());
               // b.putSerializable(BUNDLE_ORDER_TICKET, order);
                question.putExtras(b);
                startActivity(question);
            }
        });
        sendDataRequest.setDriverRecommendationOrderListManagerCallBackFunction(new JsonPutsUtil.DriverRecommendationOrderListManagerCallBackFunction() {

            @Override
            public void getWaitOrderListSuccess(RealmResults<NormalOrder> orders) {
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
                }
                getDataFromServer(orders);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void getOrderListSuccess(RealmResults<NormalOrder> orders) {
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
                }
                getDataFromServer(orders);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void getOrderListFail(boolean error) {
                if(error)
                {
                    if(progressDialog_loading!=null) {
                        progressDialog_loading.dismiss();
                        progressDialog_loading=null;
                    }
                }
            }
        });
        Bundle data=getArguments();
        if ( data !=null && data.containsKey(Constants.ARG_POSITION) )
        {
            if(data.getBoolean(Constants.ARG_POSITION)==true)
                wait=true;
             else
                option=data.getInt(Constants.ARG_POSITION);

        }
        database = new RealmUtil(getActivity());
        Utility info = new Utility(getActivity());
        //if(info.getDriverAccountInfo()!=null&&!data.getBoolean(Constants.ARG_POSITION)) {
         if(!wait) {
             progressDialog_loading = ProgressDialog.show(getActivity(), "",
                     "Loading. Please wait...", true);
             info.clearData(NormalOrder.class);
             sendDataRequest.queryRecommendOrderList(info.getAccountInfo());
         }else {
             if(progressDialog_loading==null) {
                 progressDialog_loading = ProgressDialog.show(getActivity(), "",
                         "Loading. Please wait...", true);
             }

             info.clearData(NormalOrder.class);
             sendDataRequest.queryDriverOrderList(info.getDriverAccountInfo());
            //getDataFromDB();
        }
        // 建立ListItemAdapter
        listViewAdapter = new BeginOrderListItemAdapter(getActivity(), 0, mRecordOrderListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.



        setLister();
		
    }

    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.begin_order_page_title));
        super.onResume();


    }


    private void  setLister()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                final BeginOrderListItem orderItem = mRecordOrderListData.get(position);

                final Button takeLook = (Button) v.findViewById(R.id.take_look);

                takeLook.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent question = new Intent(getActivity(), MerchandiseOrderActivity.class);
                        Bundle b = new Bundle();
                        String cargo_type = orderItem.order.getCargo_type();
                        Constants.APP_REGISTER_ORDER_TYPE orderCargoType;

                        orderCargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargo_type));

                        if(orderCargoType != Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
                        b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
                        else
                            b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.MERCHANDISE);
                        b.putString(BUNDLE_ORDER_TICKET_ID,orderItem.order.getTicket_id());
                        //b.putSerializable(BUNDLE_ORDER_TICKET,orderItem.order);
                        question.putExtras(b);
                        startActivity(question);
                    }
                });
                final Button takeover = (Button) v.findViewById(R.id.take_over);

                takeover.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (!wait){
                         //   if (order.order_title.equals("一般搭乘(小費:50元)")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                // set title
                                alertDialogBuilder.setTitle(getString(R.string.menu_sure_take_over));

                                // set dialog message
                                alertDialogBuilder
                                        .setMessage(getString(R.string.menu_cancel_order_warn))
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.menu_sure_take_over), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // if this button is clicked, close
                                                // current activity
                                                sendDataRequest.driverTakeOverOrder(orderItem.order);
                                                /*  Intent question = new Intent(getActivity(), OrderProcesssActivity.class);
                                                Bundle b = new Bundle();
                                                b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
                                                question.putExtras(b);
                                                startActivity(question);*/


                                            }
                                        })
                                        .setNegativeButton(getString(R.string.menu_give_up_take_over), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                // show it
                                alertDialog.show();
                          /*  } else {

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.setContentView(R.layout.dialog_enter_change_price_layout);
                                dialog.setTitle(getString(R.string.order_change_price));
                                Button sure = (Button) dialog.findViewById(R.id.sure_action);
                                sure.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent question = new Intent(getActivity(), SupportAnswerActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.REPORT_PRICE);
                                        question.putExtras(b);
                                        startActivity(question);
                                    }
                                });

                                Button cancel = (Button) dialog.findViewById(R.id.cancel_action);
                                cancel.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });

                                dialog.show();
                            }*/
                     }else
                        {
                            Intent question = new Intent(getActivity(), OrderProcesssActivity.class);
                            Bundle b = new Bundle();
                            Constants.APP_REGISTER_ORDER_TYPE cargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(orderItem.order.getCargo_type()));

                            if(cargoType!=Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
                                b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
                            else
                                b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.MERCHANDISE);
                            b.putString(BUNDLE_ORDER_TICKET_ID, orderItem.order.getTicket_id());
                            question.putExtras(b);
                            startActivity(question);
                        }
                    }

                });

            }
        });
    }

    @Override
	public void onStop() {
		super.onStop();
        if(progressDialog_loading!=null) {
            progressDialog_loading.dismiss();
            progressDialog_loading =null;
        }
	}

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(progressDialog_loading!=null) {
            progressDialog_loading.dismiss();
            progressDialog_loading =null;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
       // outState.putInt(Constants.ARG_POSITION, mCurrentPosition);
    }


    private void findViews() {

        listView = (ListView) getView().findViewById(R.id.listView1);
        listView.setItemsCanFocus(true);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        listView.setClickable(false);
        // 設定所有view 的font size
        // View main_layout = (View) getView().findViewById(R.id.main_layout);
        // DisplayUtil displayUtil = new DisplayUtil();
        // displayUtil.setFontSize(main_layout,
        // getResources().getDimension(R.dimen.default_text_size_px));
    }


    /* 從 table 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        Utility orders = new Utility(getActivity());
        RealmResults<NormalOrder> data=orders.getWaitOrderList();
        mRecordOrderListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();

          /*  for (int i = 0; i < 10; i++) {
                BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
                if(i%2==0)
                    beginOrderListItem.order_title = "一般搭乘(小費:50元)";
                else
                    beginOrderListItem.order_title = "貨物快送(小費:80元)";
                beginOrderListItem.departure = "從:台中市大道一段1號";
                beginOrderListItem.destination = "到:台中市政府";


                if(!wait) {
                    if(option==0)
                        beginOrderListItem.order_time = "即時";
                    else
                        beginOrderListItem.order_time = "2015/12/08 上午07:04";
                    beginOrderListItem.button_information = getString(R.string.list_btn_take_over);
                    beginOrderListItem.button_take_look_visible = View.VISIBLE;
                }else
                {
                    beginOrderListItem.order_time = "2015-12-08 上午07:04";
                    beginOrderListItem.button_information = getString(R.string.list_btn_order_process);
                    beginOrderListItem.button_take_look_visible = View.GONE;

                }
                mRecordOrderListData.add(beginOrderListItem);
            }*/

            for (NormalOrder order: data) {
                BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
                //if(i%2==0)
                OrderRecordListItem item = new OrderRecordListItem();
                Constants.APP_REGISTER_ORDER_TYPE type = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getCargo_type()));

                if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)) {
                    beginOrderListItem.order_title = "貨物快送(費用:" + order.getPrice() + "元)";
                }else if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)) {
                    beginOrderListItem.order_title = "一般搭乘(照表收費)";
                }
                else if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)) {
                    beginOrderListItem.order_title = "機場接送(照表收費)";
                }else if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)) {
                    beginOrderListItem.order_title = "車站接送(照表收費)";
                }

                beginOrderListItem.departure = "從:"+order.getBegin_address();
                beginOrderListItem.destination = "到:"+order.getEnd_address();
                beginOrderListItem.order=order;


                {
                    beginOrderListItem.order_time = "2015-12-08 上午07:04";
                    beginOrderListItem.button_information = getString(R.string.list_btn_order_process);
                    beginOrderListItem.button_take_look_visible = View.GONE;

                }
                mRecordOrderListData.add(beginOrderListItem);
            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromServer(RealmResults<NormalOrder> orders) {

        mRecordOrderListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();

          /*  for (int i = 0; i < 10; i++) {
                BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
                if(i%2==0)
                    beginOrderListItem.order_title = "一般搭乘(小費:50元)";
                else
                    beginOrderListItem.order_title = "貨物快送(小費:80元)";
                beginOrderListItem.departure = "從:台中市大道一段1號";
                beginOrderListItem.destination = "到:台中市政府";


                if(!wait) {
                    if(option==0)
                        beginOrderListItem.order_time = "即時";
                    else
                        beginOrderListItem.order_time = "2015/12/08 上午07:04";
                    beginOrderListItem.button_information = getString(R.string.list_btn_take_over);
                    beginOrderListItem.button_take_look_visible = View.VISIBLE;
                }else
                {
                    beginOrderListItem.order_time = "2015-12-08 上午07:04";
                    beginOrderListItem.button_information = getString(R.string.list_btn_order_process);
                    beginOrderListItem.button_take_look_visible = View.GONE;

                }
                mRecordOrderListData.add(beginOrderListItem);
            }*/

            for (NormalOrder order: orders) {
                BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
                //if(i%2==0)
                OrderRecordListItem item = new OrderRecordListItem();
              //  Constants.APP_REGISTER_DRIVER_TYPE type = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));
                Constants.APP_REGISTER_ORDER_TYPE type = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getCargo_type()));
                Constants.APP_REGISTER_DRIVER_TYPE driverType = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));

                String addPlayment ="";

                if(driverType.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI))
                         addPlayment = "(照表收費)";
                else
                    addPlayment = "(費用:" + order.getPrice() + "元)";
                if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)) {
                    beginOrderListItem.order_title = "貨物快送"+addPlayment;
                }else if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)) {
                    beginOrderListItem.order_title = "一般搭乘"+addPlayment;
                }
                else if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)) {
                    beginOrderListItem.order_title = "機場接送"+addPlayment;
                }else if(type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)) {
                    beginOrderListItem.order_title = "車站接送"+addPlayment;
                }
                beginOrderListItem.departure = "從:"+order.getBegin_address();
                beginOrderListItem.destination = "到:"+order.getEnd_address();
                beginOrderListItem.order=order;

                if(!wait) {
                    if(option==0)
                        beginOrderListItem.order_time = "即時";
                    else
                        beginOrderListItem.order_time = "2015/12/08 上午07:04";
                    beginOrderListItem.button_information = getString(R.string.list_btn_take_over);
                    beginOrderListItem.button_take_look_visible = View.VISIBLE;
                }else
                {
                    beginOrderListItem.order_time = "訂單狀態：進行中";
                    beginOrderListItem.button_information = getString(R.string.list_btn_order_process);
                    beginOrderListItem.button_take_look_visible = View.GONE;

                }
                mRecordOrderListData.add(beginOrderListItem);
            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }



}