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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;

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
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.Fragment_AccountDelegateBase;
import tw.com.geminihsu.app01.delegate.customer.Fragment_AccountDelegateCustomer;
import tw.com.geminihsu.app01.delegate.driver.Fragment_AccountDelegateDriver;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
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
    private ArrayList<String> driver_identity;
    private JsonPutsUtil sendDataRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
            }

        });
        /*if (Constants.Driver) {

            viewDelegateBase = new Fragment_AccountDelegateDriver(this);
        } else {

            viewDelegateBase = new Fragment_AccountDelegateCustomer(this);
        }*/
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.select_dialog_item);

        arrayAdapter.add(driver_identity.get(0));
        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        switch (which){
                            case 0:

                                sendDataRequest.driverWorkIdentity(driver);
                                break;
                            case 1:
                               break;
                                 /*   case 2:
                                        Intent question = new Intent(NewPostActivity.this, YoutubeActivity.class);
                                        startActivity(question);
                                        break;*/
                        }
                    }
                });
        builderSingle.show();
    }


    private void getDriverIdentity()
    {
        if(driver!=null) {
            driver_identity = new ArrayList<String>();
            String type = driver.getDtype();
            Constants.APP_REGISTER_DRIVER_TYPE dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(type));
            if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI)
                driver_identity.add(getString(R.string.taxi_driver));
            else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER)
                driver_identity.add(getString(R.string.Uber_driver));
            else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK)
                driver_identity.add(getString(R.string.truck_driver));
            else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO)
                driver_identity.add(getString(R.string.cargo_driver));
            else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER)
                driver_identity.add(getString(R.string.trailer_driver));
        }
    }

}