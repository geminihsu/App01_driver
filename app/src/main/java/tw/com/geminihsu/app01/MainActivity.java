package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.ConfigSharedPreferencesUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;

public class MainActivity extends Activity {
    public final static String TAG = MainActivity.class.toString();// from
    public final static String BUNDLE_ACCESS_KEY = "accesskey";// from

    private static int id = 1;
    public final static int ERROR_USER_INFO = 0;
    public final static int ERROR_NO_USER = 1;

    public final static int ERROR_NO_GPS = 2;

    private TextView txt_forget_password;
    private Button btn_register;
    private Button btn_login;
    private EditText account_phone;
    private EditText account_password;

    private String phone_number="";
    private String password="";

    String token ="";

    private SharedPreferences configSharedPreferences;
    private ProgressDialog dialog;
    private AccountInfo user;
    private DriverIdentifyInfo driverIdentifyInfo;
    private AlertDialog alertDialog;
    private JsonPutsUtil sendDataRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

        sendDataRequest = new JsonPutsUtil(MainActivity.this);
        sendDataRequest.setClientLoginDataManagerCallBackFunction(new JsonPutsUtil.ClientLoginDataManagerCallBackFunction() {

            @Override
            public void loginClient(AccountInfo accountInfo) {
                Constants.Driver = false;
                Intent intent = new Intent(MainActivity.this, MenuMainActivity.class);
                //Bundle b = new Bundle();
                //b.putString(BUNDLE_ACCESS_KEY, accesskey);
                //intent.putExtras(b);
                startActivity(intent);
                finish();
            }

            @Override
            public void loginDriver(DriverIdentifyInfo driver) {
                Constants.Driver = true;
                Intent intent = new Intent(MainActivity.this, MenuMainActivity.class);
                //Bundle b = new Bundle();
                //b.putString(BUNDLE_ACCESS_KEY, accesskey);
                //intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

        }

    @Override
    protected void onPause()
    {
       super.onPause();
       if(alertDialog!=null)
       {
           alertDialog.dismiss();
           alertDialog = null;
       }

        if(dialog!=null)
        {
            dialog.dismiss();
            dialog = null;
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        //token = FirebaseInstanceId.getInstance().getToken();
        //Log.d("FCM", "Token:" + token);

        configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        phone_number = ConfigSharedPreferencesUtil.getUserName(this, configSharedPreferences);
        password = ConfigSharedPreferencesUtil.getPassword(this, configSharedPreferences);
        this.findViews();
        this.setLister();
        if (!phone_number.isEmpty() && !password.isEmpty())
        {
            Bundle args = getIntent().getExtras();
            //Log.e(TAG,"args"+args.toString());
            if(args!=null){
                if (args.containsKey(RegisterActivity.BUNDLE_ACCOUNT_INFO)) {
                    user = (AccountInfo) args.getSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO);
                    //already has account so query account from database
                    if (user != null) {
                        queryAccount();
                        Log.e(TAG, "command");
                    }
                }else
                {
                    changeActivity();

                }
            }else
            {

                changeActivity();
            }

        }

    }

    private void findViews()
    {

        txt_forget_password = (TextView) findViewById(R.id.txt_forget_password);

        btn_register = (Button) findViewById(R.id.register);
        btn_login = (Button) findViewById(R.id.login);

        account_phone = (EditText) findViewById(R.id.account_phone);
        //account_phone.setText(token);
        account_password = (EditText)findViewById(R.id.account_password);
        if (!phone_number.isEmpty() && !password.isEmpty()) {
            account_phone.setText(phone_number);
            account_password.setText(password);
        }
    }


    private void setLister()
    {
        txt_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone_number = account_phone.getText().toString();
                password = account_password.getText().toString();
                if(phone_number.isEmpty()||password.isEmpty())
                {
                    alert(ERROR_USER_INFO);
                }else {
                    if(user ==null)
                    {
                        user = new AccountInfo();
                        user.setId(id);
                        user.setPhoneNumber(phone_number);
                        user.setPassword(password);
                        String token = FirebaseInstanceId.getInstance().getToken();
                        user.setRegisterToken(token);

                    }
                    queryAccount();

                }
            }
        });
    }

    private void queryAccount(){

        //if(user!=null) {
         //   if(user.getPassword().equals(password)) {
                if(dialog == null)
                   dialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);
                sendDataRequest.sendLoginRequest(user);
       //     }else
       //         alert(ERROR_USER_INFO);
       // }else
       //     alert(ERROR_NO_USER);

    }

    private void alert(int error)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.login_error_title));

        if(error==ERROR_USER_INFO) {
            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                            startActivity(intent);
                        }
                    });
        }else if(error==ERROR_NO_USER)
        {
            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_user_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
        }
        else if(error==ERROR_NO_GPS)
        {
            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_gps))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.login_error_gps_msg), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertDialog.dismiss();
                            alertDialog = null;
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
        }
        // create alert dialog
        if(alertDialog==null) {
            alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
    }


    private void changeActivity()
    {
        RealmUtil realmUtil = new RealmUtil(MainActivity.this);
        //realmUtil.clearDB();
        user = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);
        driverIdentifyInfo = realmUtil.queryDriver(Constants.ACCOUNT_DRIVER_UID, user.getUid());
        RealmResults<DriverIdentifyInfo> drivers = realmUtil.queryAllDriver();
        Log.e(TAG,"got driver!!");
        if(driverIdentifyInfo==null)
            Constants.Driver = false;
        else
            Constants.Driver = true;
        Log.e(TAG,"database");
        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                /*Bundle b = new Bundle();
                b.putSerializable(BUNDLE_ACCESS_KEY, user.getAccessKey());
                intent.putExtras(b);*/
        startActivity(intent);
        finish();
    }

}
