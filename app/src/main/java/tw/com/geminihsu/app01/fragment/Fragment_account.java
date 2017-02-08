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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.newrelic.agent.android.NewRelic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.CameraActivity;
import tw.com.geminihsu.app01.ChangePasswordActivity;
import tw.com.geminihsu.app01.CommentActivity;
import tw.com.geminihsu.app01.DriverAccountActivity;
import tw.com.geminihsu.app01.DriverIdentityActivity;
import tw.com.geminihsu.app01.DriverLoginActivity;
import tw.com.geminihsu.app01.OrderProcesssActivity;
import tw.com.geminihsu.app01.PhotoVerifyActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.RecommendActivity;
import tw.com.geminihsu.app01.SupportAnswerActivity;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.Fragment_AccountDelegateBase;
import tw.com.geminihsu.app01.delegate.customer.Fragment_AccountDelegateCustomer;
import tw.com.geminihsu.app01.delegate.driver.Fragment_AccountDelegateDriver;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.UploadUtils;
import tw.com.geminihsu.app01.utils.Utility;


public class Fragment_Account extends Fragment {

    //private ListView listView;
    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_LOGOUT = 0x0001;

    private LinearLayout linearLayout_accountMenu;
    private Fragment_AccountDelegateBase viewDelegateBase;
    private Button btn_change_password;
    private Button btn_obtain_comment;
    private Button btn_recommend_friend;
    private Button btn_apply_driver;
    private Button btn_driver_identity;
    private DriverIdentifyInfo driver;
    private DriverIdentifyInfo change_driver;
    private HashMap<String,Integer> driver_identity;
    private HashMap<String,DriverIdentifyInfo> driver_mapping_value;

    private JsonPutsUtil sendDataRequest;
    private RealmResults<DriverIdentifyInfo> driverIdentifyInfos;



    private int changeDriverType;
    private ProgressDialog progressDialog_loading;

    private Map<String, Object> attributes = new HashMap<String, Object>();
    private String sessionId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sessionId = NewRelic.currentSessionId();
        NewRelic.startInteraction("AccountBehavior");
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
        sendDataRequest = new JsonPutsUtil(getActivity());
        sendDataRequest.setDriverChangeWorkIdentityManagerCallBackFunction(new JsonPutsUtil.DriverChangeWorkIdentityManagerCallBackFunction() {


            @Override
            public void driverChangeWorkIdentity(DriverIdentifyInfo driver) {
                Log.e("change","dataType:"+driver.getDtype());
                NewRelic.setUserId(driver.getName());
                RealmUtil realmUtil = new RealmUtil(getActivity());
                AccountInfo userInfo = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,driver.getName());
                if(userInfo!=null)
                {
                    //更新資料庫帳號裡面的目前營業身份的欄位
                    AccountInfo new_user = new AccountInfo();
                    new_user.setId(userInfo.getId());
                    new_user.setUid(userInfo.getUid());
                    new_user.setName(userInfo.getName());
                    new_user.setPhoneNumber(userInfo.getPhoneNumber());
                    new_user.setIdentify(driver.getDtype());
                    new_user.setPassword(userInfo.getPassword());
                    new_user.setConfirm_password(userInfo.getConfirm_password());
                    new_user.setRecommend_id(userInfo.getRecommend_id());
                    new_user.setDriver_type(""+changeDriverType);
                    new_user.setRole(userInfo.getRole());
                    new_user.setAccessKey(userInfo.getAccessKey());
                    //user.setPassword(newPassword);
                    realmUtil.updateAccount(new_user);
                    attributes.put("driver", changeDriverType);

                }
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
                    Toast.makeText(getActivity(),
                            "更改成功",
                            Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void changeIdentityError(boolean error) {
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading = null;
                }
                Toast.makeText(getActivity(),
                        "更改失敗",
                        Toast.LENGTH_LONG).show();
            }

        });
        /*if (Constants.Driver) {

            viewDelegateBase = new Fragment_AccountDelegateDriver(this);
        } else {

            viewDelegateBase = new Fragment_AccountDelegateCustomer(this);
        }*/
        Utility info = new Utility(getActivity());
        //if(info.getDriverAccountInfo()!=null)
        //    sendDataRequest.getDriverInfo(info.getAccountInfo());
        driverIdentifyInfos = info.getAllDriverAccountInfo();
        this.findViews();
        this.setLister();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.myaccount_page_title));
        super.onResume();


    }
    
    @Override
	public void onStop() {
		super.onStop();
        boolean eventRecorded = NewRelic.recordEvent("AccountInfo", attributes);
        NewRelic.endInteraction(sessionId);
	}

	private void findViews()
    {
        /*listView = (ListView) getView().findViewById(R.id.listView1);


        String[] menu=viewDelegateBase.setListData();
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < menu.length; ++i) {
            list.add(menu[i]);
        }
        ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,menu);

        listView.setAdapter(adapter);*/
        linearLayout_accountMenu = (LinearLayout) getView().findViewById(R.id.info);
        linearLayout_accountMenu.setVisibility(View.VISIBLE);
        btn_change_password = (Button) getView().findViewById(R.id.changePassword);
        btn_obtain_comment = (Button) getView().findViewById(R.id.obtain_comment);
        btn_recommend_friend = (Button) getView().findViewById(R.id.recommend_friend);
        btn_apply_driver = (Button) getView().findViewById(R.id.apply_driver);
        btn_driver_identity = (Button) getView().findViewById(R.id.driver_identity);

        Utility account = new Utility(getActivity());
        driver = account.getDriverAccountInfo();
        if(driver==null)
            btn_driver_identity.setVisibility(View.GONE);
    }

    private void setLister(){

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                viewDelegateBase.listViewOnItemClickListener(parent,v,position,id);

            }
        });*/
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent question = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(question);

            }
        });

        btn_obtain_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent question = new Intent(getActivity(), CommentActivity.class);
                startActivity(question);

            }
        });

        btn_recommend_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent question = new Intent(getActivity(), RecommendActivity.class);
                startActivity(question);

            }
        });

        btn_apply_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent question = new Intent(getActivity(), DriverIdentityActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(DriverLoginActivity.BUNDLE_DRIVER_ACCOUNT_INFO, driver);
                question.putExtras(b);
                startActivity(question);

            }
        });

        btn_driver_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIdentity();


            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       /* MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_LOGOUT, Menu.NONE, getString(R.string.btn_logout));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_LOGOUT:
                //將表單資料送出後回到主畫面
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

	private void selectIdentity()
    {
        getDriverIdentity();

        if(!driver_identity.isEmpty()) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.select_dialog_item);

            for (String type : driver_identity.keySet()) {
                arrayAdapter.add(type);
            }

            builderSingle.setAdapter(
                    arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);

                            progressDialog_loading = ProgressDialog.show(getActivity(), "",
                                    "Loading. Please wait...", true);
                            change_driver = driver_mapping_value.get(strName);
                            changeDriverType = driver_identity.get(strName);
                            attributes.put("select driver", strName);
                            sendDataRequest.driverWorkIdentity(change_driver);

                        }
                    });
            builderSingle.show();
        }
    }


    private void getDriverIdentity()
    {   driver_identity = new HashMap<String,Integer>();
        driver_mapping_value = new HashMap<String,DriverIdentifyInfo>();
        if(driver!=null) {
            Utility info = new Utility(getActivity());
            AccountInfo user = info.getAccountInfo();

            if(driverIdentifyInfos.size()>1)
            {
                String currentType = user.getDriver_type();

                if(currentType==null)
                    currentType="0";
                Constants.APP_REGISTER_DRIVER_TYPE driverCurrentType = Constants.conversion_register_driver_account_result(Integer.valueOf(currentType));


                if(Integer.valueOf(currentType)>0) {
                    DriverIdentifyInfo no_workDriverIdentifyInfo = new DriverIdentifyInfo();
                    no_workDriverIdentifyInfo.setUid(user.getUid());
                    no_workDriverIdentifyInfo.setDid("0");
                    no_workDriverIdentifyInfo.setAccesskey(user.getAccessKey());
                    no_workDriverIdentifyInfo.setName(user.getPhoneNumber());
                    no_workDriverIdentifyInfo.setDtype("0");
                    driver_identity.put(getString(R.string.no_work), 0);
                    driver_mapping_value.put(getString(R.string.no_work), no_workDriverIdentifyInfo);
                }

                for (DriverIdentifyInfo driverIdentifyInfo:driverIdentifyInfos)
                {
                    String type = driverIdentifyInfo.getDtype();
                    Constants.APP_REGISTER_DRIVER_TYPE dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(type));
                    if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI&&dataType!=driverCurrentType) {
                        driver_identity.put(getString(R.string.taxi_driver), 1);
                        driver_mapping_value.put(getString(R.string.taxi_driver),driverIdentifyInfo);
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER&&dataType!=driverCurrentType) {
                        driver_identity.put(getString(R.string.Uber_driver), 2);
                        driver_mapping_value.put(getString(R.string.Uber_driver),driverIdentifyInfo);

                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK&&dataType!=driverCurrentType) {
                        driver_identity.put(getString(R.string.truck_driver), 3);
                        driver_mapping_value.put(getString(R.string.truck_driver),driverIdentifyInfo);
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO&&dataType!=driverCurrentType) {
                        driver_identity.put(getString(R.string.cargo_driver), 4);
                        driver_mapping_value.put(getString(R.string.cargo_driver),driverIdentifyInfo);
                    }
                    else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER&&dataType!=driverCurrentType) {
                        driver_identity.put(getString(R.string.trailer_driver), 5);
                        driver_mapping_value.put(getString(R.string.trailer_driver),driverIdentifyInfo);

                    }
                }


            }else {

                String currentType = user.getDriver_type();
                if(currentType==null)
                    currentType="0";
                if(Integer.valueOf(currentType)>0) {
                    DriverIdentifyInfo no_workDriverIdentifyInfo = new DriverIdentifyInfo();
                    no_workDriverIdentifyInfo.setUid(driver.getUid());
                    no_workDriverIdentifyInfo.setAccesskey(driver.getAccesskey());
                    no_workDriverIdentifyInfo.setName(driver.getName());
                    no_workDriverIdentifyInfo.setDtype("0");
                    no_workDriverIdentifyInfo.setDid("0");
                    driver_identity.put(getString(R.string.no_work), 0);
                    driver_mapping_value.put(getString(R.string.no_work), no_workDriverIdentifyInfo);
                }

                String type = driver.getDtype();

                if (currentType.equals("0")) {
                    Constants.APP_REGISTER_DRIVER_TYPE dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(type));
                    if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                        driver_identity.put(getString(R.string.taxi_driver), 1);
                        driver_mapping_value.put(getString(R.string.taxi_driver), driver);
                    } else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER) {
                        driver_identity.put(getString(R.string.Uber_driver), 2);
                        driver_mapping_value.put(getString(R.string.Uber_driver), driver);
                    } else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK) {
                        driver_identity.put(getString(R.string.truck_driver), 3);
                        driver_mapping_value.put(getString(R.string.truck_driver), driver);
                    } else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO) {
                        driver_identity.put(getString(R.string.cargo_driver), 4);
                        driver_mapping_value.put(getString(R.string.cargo_driver), driver);
                    } else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER)
                        driver_identity.put(getString(R.string.trailer_driver), 5);
                    driver_mapping_value.put(getString(R.string.trailer_driver), driver);
                }
            }

        }
    }

}