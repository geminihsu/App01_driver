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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//import com.newrelic.agent.android.NewRelic;

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
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.serverbean.ServerSpecial;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class Fragment_BeginOrderList extends Fragment implements
        BeginOrderListItemAdapter.TakeLookButtonListener,BeginOrderListItemAdapter.TakeOverButtonListner {
    private final String TAG = Fragment_BeginOrderList.class.toString();

    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from
    public final static String BUNDLE_ORDER_TICKET = "ticket";// from
    public final static String BUNDLE_DRIVER_PHONE = "driver_phone_number";// from


    final public static int REALTIME_ORDERLIST = 0;
    final public static int RESERVATION_ORDERLIST =1;
    final public static int CAR_COMPANY_ORDERLIST =2;


    private ListView listView;
    private final List<BeginOrderListItem> mRecordOrderListData = new ArrayList<BeginOrderListItem>();;
    private BeginOrderListItemAdapter listViewAdapter;

    private boolean wait = false;
    private int option;
    private JsonPutsUtil sendDataRequest;
    private ProgressDialog progressDialog_loading;
    private ArrayList<NormalOrder> orders;
    private RealmUtil database;
    private SwipeRefreshLayout loadOrderList;
    private String driverPhoneNumber = "";//in order to take over order, this one used to record driver phone number
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

    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
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
                b.putString(BUNDLE_DRIVER_PHONE, driverPhoneNumber);
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
                loadOrderList.setRefreshing(false);
                getDataFromServer(orders,option);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void getOrderListSuccess(RealmResults<NormalOrder> orders) {
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
                }
                getDataFromServer(orders,option);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void getOrderListFail(boolean error,String message) {
                if(error)
                {
                    loadOrderList.setRefreshing(false);
                    if(progressDialog_loading!=null) {
                        progressDialog_loading.dismiss();
                        progressDialog_loading=null;
                    }
                    if(message.equals("836"))
                    {
                        Utility info = new Utility(getActivity());
                        if(info.getAccountInfo().getDriver_type().equals("0"))
                            Toast.makeText(getActivity(), "目前暫停營業中", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), "司機身份已送出審核", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        sendDataRequest.setClientLoginDataManagerCallBackFunction(new JsonPutsUtil.ClientLoginDataManagerCallBackFunction() {

            @Override
            public void loginClient(AccountInfo accountInfo) {

            }

            @Override
            public void loginDriver(DriverIdentifyInfo driver) {

            }

            @Override
            public void findDriverInfo(final AccountInfo accountInfo, ArrayList<DriverIdentifyInfo> driver) {

                String dataType = accountInfo.getDriver_type();
                loadOrderList.setRefreshing(false);


                if(dataType==null&&driver.size()==0)
                {
                    if(progressDialog_loading!=null) {
                        progressDialog_loading.dismiss();
                        progressDialog_loading=null;
                    }
                    //表示不是司機
                    Toast.makeText(getActivity(), "未註冊司機前無法接單", Toast.LENGTH_LONG).show();
                }else
                if(dataType==null)
                {
                    if(progressDialog_loading!=null) {
                        progressDialog_loading.dismiss();
                        progressDialog_loading=null;
                    }
                    String enable="";
                    //有可能沒有驗證成功，或是還未選擇營運身份，或是選擇不營運
                    for(int i=0;i<driver.size();i++)
                    {
                        DriverIdentifyInfo driverIdentifyInfo = driver.get(i);

                        if (driverIdentifyInfo.getEnable().equals("0"))
                        {
                            Toast.makeText(getActivity(),
                                    "司機資格審核中",
                                    Toast.LENGTH_LONG).show();

                        }else
                        {
                            enable = driverIdentifyInfo.getEnable();
                        }
                    }
                    if(enable.equals("100"))
                    {Toast.makeText(getActivity(),
                            "司機身份未選擇",
                            Toast.LENGTH_LONG).show();
                    }/*else if(enable.equals("100"))
                   {
                       sendDataRequest.queryRecommendOrderList(accountInfo);
                   }*/
                }else{
                    final Utility info = new Utility(getActivity());
                    driverPhoneNumber = accountInfo.getPhoneNumber();
                    if(!wait) {


                        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
                            @Override
                            public void run() {
                                if(info!=null)
                                    info.clearData(NormalOrder.class);
                                //boolean attributeSet = NewRelic.setAttribute("Query Recommendation List", accountInfo.getAccessKey());
                                sendDataRequest.queryRecommendOrderList(accountInfo);
                            }
                        }));
                    }else {


                        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
                            @Override
                            public void run() {
                                if(info!=null)
                                info.clearData(NormalOrder.class);
                                sendDataRequest.queryDriverOrderList(info.getDriverAccountInfo());
                            }
                        }));
                    }
                }
            }

            @Override
            public void loginError(boolean error) {
                loadOrderList.setRefreshing(false);
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
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

       /* Utility info = new Utility(getActivity());
        //if(info.getDriverAccountInfo()!=null&&!data.getBoolean(Constants.ARG_POSITION)) {
         if(!wait) {
             progressDialog_loading = ProgressDialog.show(getActivity(), "",
                     "Loading. Please wait...", true);
             info.clearData(NormalOrder.class);
             //sendDataRequest.queryRecommendOrderList(info.getAccountInfo());
             sendDataRequest.getUserInfo(info.getAccountInfo(),true);

         }else {
             if(progressDialog_loading==null) {
                 progressDialog_loading = ProgressDialog.show(getActivity(), "",
                         "Loading. Please wait...", true);
             }

             info.clearData(NormalOrder.class);
             sendDataRequest.queryDriverOrderList(info.getDriverAccountInfo());
            //getDataFromDB();
        }*/
        // 建立ListItemAdapter
        if(listViewAdapter == null) {
            listViewAdapter = new BeginOrderListItemAdapter(getActivity(), 0, mRecordOrderListData);
            listViewAdapter.setTakeLookButtonListner(Fragment_BeginOrderList.this);
            listViewAdapter.setTakeOverButtonListner(Fragment_BeginOrderList.this);
            listView.setAdapter(listViewAdapter);
        }
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

        if (progressDialog_loading==null) {
            progressDialog_loading = ProgressDialog.show(getActivity(), "",
                    "Loading. Please wait...", true);
        }
        loadOrderList.setRefreshing(true);


        //sendDataRequest.queryRecommendOrderList(info.getAccountInfo());
        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                if(getActivity()!=null) {
                    Utility info = new Utility(getActivity());
                    info.clearData(NormalOrder.class);
                    sendDataRequest.getUserInfo(info.getAccountInfo(), true);
                }
            }
        }));
    }


    private void  setLister()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                /*final BeginOrderListItem orderItem = mRecordOrderListData.get(position);

                final Button takeLook = (Button) v.findViewById(R.id.take_look);

                final NormalOrder order = orderItem.order;
                if(order.isValid()) {
                    final String cargo_type = order.getCargo_type();
                    final Constants.APP_REGISTER_ORDER_TYPE[] orderCargoType = new Constants.APP_REGISTER_ORDER_TYPE[1];


                    takeLook.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                        }
                    });
                    final Button takeover = (Button) v.findViewById(R.id.take_over);

                    takeover.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {



                        }

                    });
                }*/

            }
        });

        loadOrderList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                /*progressDialog_loading = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);*/
                final Utility info = new Utility(getActivity());
                info.clearData(NormalOrder.class);
                //sendDataRequest.queryRecommendOrderList(info.getAccountInfo());
                ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
                    @Override
                    public void run() {
                        sendDataRequest.getUserInfo(info.getAccountInfo(),true);
                    }
                }));

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
        loadOrderList = (SwipeRefreshLayout) getView().findViewById(R.id.refreshlayout);

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
    private void getDataFromServer(RealmResults<NormalOrder> orders,int filter) {

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

                Log.e(TAG,"order.getTimebegin():"+order.getTimebegin());
                if(filter==REALTIME_ORDERLIST&&order.getTimebegin().equals("0")) {
                    //取得即時訂單
                    BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
                    //if(i%2==0)
                    OrderRecordListItem item = new OrderRecordListItem();
                    //  Constants.APP_REGISTER_DRIVER_TYPE type = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));
                    Constants.APP_REGISTER_ORDER_TYPE type = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getCargo_type()));
                    Constants.APP_REGISTER_DRIVER_TYPE driverType = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));

                    String addPlayment = "";

                    if (driverType.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI))
                        addPlayment = "(照表收費)";
                    else
                        addPlayment = "(費用:" + order.getPrice() + "元)";
                    if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)) {
                        beginOrderListItem.order_title = "貨物快送" + addPlayment;
                    } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)) {
                        beginOrderListItem.order_title = "一般搭乘" + addPlayment;
                    } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)) {
                        beginOrderListItem.order_title = "機場接送" + addPlayment;
                    } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)) {
                        beginOrderListItem.order_title = "車站接送" + addPlayment;
                    }
                    beginOrderListItem.departure = "從:" + order.getBegin_address();
                    beginOrderListItem.destination = "到:" + order.getEnd_address();
                    beginOrderListItem.order = order;

                    if (!wait) {
                        if (option == 0)
                            beginOrderListItem.order_time = "即時";
                        else
                            beginOrderListItem.order_time = "2015/12/08 上午07:04";
                        beginOrderListItem.button_information = getString(R.string.list_btn_take_over);
                        beginOrderListItem.button_take_look_visible = View.VISIBLE;
                    } else {
                        beginOrderListItem.order_time = "訂單狀態：進行中";
                        beginOrderListItem.button_information = getString(R.string.list_btn_order_process);
                        beginOrderListItem.button_take_look_visible = View.GONE;

                    }
                    mRecordOrderListData.add(beginOrderListItem);
                }else if(filter==RESERVATION_ORDERLIST&&!order.getTimebegin().equals("0")) {
                    //取得即時訂單
                    BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
                    //if(i%2==0)
                    OrderRecordListItem item = new OrderRecordListItem();
                    //  Constants.APP_REGISTER_DRIVER_TYPE type = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));
                    Constants.APP_REGISTER_ORDER_TYPE type = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getCargo_type()));
                    Constants.APP_REGISTER_DRIVER_TYPE driverType = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));

                    String addPlayment = "";

                    if (driverType.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI))
                        addPlayment = "(照表收費)";
                    else
                        addPlayment = "(費用:" + order.getPrice() + "元)";
                    if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)) {
                        beginOrderListItem.order_title = "貨物快送" + addPlayment;
                    } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)) {
                        beginOrderListItem.order_title = "一般搭乘" + addPlayment;
                    } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)) {
                        beginOrderListItem.order_title = "機場接送" + addPlayment;
                    } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)) {
                        beginOrderListItem.order_title = "車站接送" + addPlayment;
                    }
                    beginOrderListItem.departure = "從:" + order.getBegin_address();
                    beginOrderListItem.destination = "到:" + order.getEnd_address();
                    beginOrderListItem.order = order;

                    if (!wait) {
                        if (option == 0)
                            beginOrderListItem.order_time = "即時";
                        else
                            beginOrderListItem.order_time = "2015/12/08 上午07:04";
                        beginOrderListItem.button_information = getString(R.string.list_btn_take_over);
                        beginOrderListItem.button_take_look_visible = View.VISIBLE;
                    } else {
                        beginOrderListItem.order_time = "訂單狀態：進行中";
                        beginOrderListItem.button_information = getString(R.string.list_btn_order_process);
                        beginOrderListItem.button_take_look_visible = View.GONE;

                    }
                    mRecordOrderListData.add(beginOrderListItem);
                }
            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void ontakeLookButtonListener(int position) {
        final BeginOrderListItem orderItem = mRecordOrderListData.get(position);

         final NormalOrder order = orderItem.order;
        if (order.isValid()) {
           /* final String cargo_type = order.getCargo_type();
            final Constants.APP_REGISTER_ORDER_TYPE[] orderCargoType = new Constants.APP_REGISTER_ORDER_TYPE[1];

            Intent question = new Intent(getActivity(), MerchandiseOrderActivity.class);
            Bundle b = new Bundle();

            orderCargoType[0] = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargo_type));

            //if (orderCargoType[0] != Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
                b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
            //else
            //    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.MERCHANDISE);
            b.putString(BUNDLE_ORDER_TICKET_ID, orderItem.order.getTicket_id());
            //b.putSerializable(BUNDLE_ORDER_TICKET,orderItem.order);

            question.putExtras(b);
            startActivity(question);*/
            orderDetail(order);
        }
    }

    @Override
    public void ontakeOverButtonListener(int position) {
        final BeginOrderListItem orderItem = mRecordOrderListData.get(position);

        final NormalOrder order = orderItem.order;

        if (order.isValid()) {
            final String cargo_type = order.getCargo_type();
            final Constants.APP_REGISTER_ORDER_TYPE[] orderCargoType = new Constants.APP_REGISTER_ORDER_TYPE[1];

            if (!wait) {
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
                                if (order.isValid()) {

                                    sendDataRequest.driverTakeOverOrder(orderItem.order,driverPhoneNumber);

                                }
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
            } else {
                Intent question = new Intent(getActivity(), OrderProcesssActivity.class);
                Bundle b = new Bundle();
                if (orderCargoType[0] != Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
                    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.PASSENGER);
                else
                    b.putInt(Constants.ARG_POSITION, OrderProcesssActivity.MERCHANDISE);
                b.putString(BUNDLE_ORDER_TICKET_ID, orderItem.order.getTicket_id());
                b.putString(BUNDLE_DRIVER_PHONE, driverPhoneNumber);

                question.putExtras(b);
                startActivity(question);
            }
        }
    }

    private void orderDetail(NormalOrder order)
    {
        String departure = "從："+order.getBegin_address()+"\n";
        String stop = "";
        String spec = "";
        ServerSpecial specContent;

        String type = "";
        Constants.APP_REGISTER_ORDER_TYPE dataType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getCargo_type()));
        if (dataType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)
            type = getString(R.string.client_take_ride_title);
        else if (dataType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)
            type = getString(R.string.client_train_pick_up);
        else if (dataType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)
            type = getString(R.string.client_airplane_pick_up);
        else if (dataType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
            type = getString(R.string.client_merchanse_send_title);


        if(!order.getStop_address().equals("0"))
           stop = "停靠："+order.getStop_address()+"\n";
        String destination ="到："+ order.getEnd_address()+"\n";
        String orderType = "時間：即時"+"\n";
        spec = "特殊需求：";
        String remark= "";
        String spec_detail = "";
        if(order.getCar_special()!=null) {
            String[] spec_array = order.getCar_special().split(",");
            if(!order.getCar_special().equals("")){
            if(spec_array.length == 0) {
                RealmUtil specQuery = new RealmUtil(getActivity());
                specContent = specQuery.queryServerSpecialItem(Constants.SPEC_ID, order.getCar_special());
                spec +=  specContent.getContent();
            }else {

                for (String spec_id : spec_array) {
                    RealmUtil specQuery = new RealmUtil(getActivity());
                    specContent = specQuery.queryServerSpecialItem(Constants.SPEC_ID, spec_id);
                    spec_detail += specContent.getContent() + ",";

                }
            }

                spec_detail = spec_detail.substring(0,spec_detail.length()-1);
                spec += spec_detail+"\n";
            }else
                spec = "";
        }

        if(!order.getRemark().equals(""))
            remark = "備註：" +order.getRemark();
        else
            remark = "";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle(getString(R.string.order_info));

        // set dialog message
        alertDialogBuilder
                .setMessage("訂單類型:"+type+"\n"+"客戶電話:"+order.getUser_name()+"\n"+departure+stop+destination+orderType+spec+remark)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sure_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}