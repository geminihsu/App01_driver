package tw.com.geminihsu.app01Client;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.newrelic.agent.android.Agent;
import com.packetzoom.speed.PacketZoomClient;

import java.util.ArrayList;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.ConfigSharedPreferencesUtil;
import tw.com.geminihsu.app01Client.utils.FileUtil;
import tw.com.geminihsu.app01Client.utils.FormatUtils;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.ThreadPoolUtil;
import com.newrelic.agent.android.NewRelic;

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
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tw.com.geminihsu.app01Client.R.layout.login_page_activity);
        NewRelic.withApplicationToken(
                "AAd366f2a60eb34eb4f3b07073a52201c9b1055f16"
        ).start(this.getApplication());

        String filePath = Environment.getExternalStorageDirectory()+ Constants.SDACRD_DIR_APP_ROOT;
        FileUtil.checkSdCard(filePath);// 檢查S是否有 SD卡,並建立會用到的 SD卡路徑

        checkStoragePermissions(this);

        PacketZoomClient.init(this, "94b97bbd9abc75566da2986103633926", "fd768b12976c15dc82dd18006bb802e3922bcb8a");
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

            @Override
            public void findDriverInfo(AccountInfo accountInfo, ArrayList<DriverIdentifyInfo> driver) {

            }

            @Override
            public void loginError(boolean error) {
                if(error)
                {
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
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Token:" + token);

        configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        phone_number = ConfigSharedPreferencesUtil.getUserName(this, configSharedPreferences);
        password = ConfigSharedPreferencesUtil.getPassword(this, configSharedPreferences);
        this.findViews();
        this.setLister();

        Log.e(TAG,"Model Number:"+ Build.MODEL);
        if (!phone_number.isEmpty() && !password.isEmpty())
        {
                    changeActivity();

        }

    }

    private void findViews()
    {

        txt_forget_password = (TextView) findViewById(R.id.txt_forget_password);

        btn_register = (Button) findViewById(R.id.register);
        btn_login = (Button) findViewById(R.id.login);

        account_phone = (EditText) findViewById(R.id.account_phone);
        account_phone.addTextChangedListener(checkPhoneFormat);

        //account_phone.setText(token);
        account_password = (EditText)findViewById(R.id.account_password);
        //account_password.addTextChangedListener(checkIdentityFormat);

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
                        RealmUtil info = new RealmUtil(MainActivity.this);
                        user = info.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);
                        if(user == null) {
                            //發現資料庫沒有資料，直接去跟Server下登入命令要資料
                            user = new AccountInfo();
                            user.setId(id);
                            user.setPhoneNumber(phone_number);
                            user.setPassword(password);
                            String token = FirebaseInstanceId.getInstance().getToken();
                            user.setRegisterToken(token);
                            queryAccount();
                        }else
                        {

                            configSharedPreferences.edit().putString(getString(R.string.config_login_phone_number_key), user.getPhoneNumber()).commit();
                            configSharedPreferences.edit().putString(getString(R.string.config_login_password_key), user.getPassword()).commit();

                            //發現資料庫有資料直接登入
                            changeActivity();
                        }

                    }else
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
        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                sendDataRequest.sendLoginRequest(user,false);
            }
        }));


       //     }else
       //         alert(ERROR_USER_INFO);
       // }else
       //     alert(ERROR_NO_USER);

    }
    private TextWatcher checkIdentityFormat= new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            //判斷是否為身分證格式
            if (!FormatUtils.isIdNoFormat(temp.toString())) {
                //TODO:身份證錯誤處理
                account_password.setError(getString(R.string.login_error_register_msg));
            }
        }


    };

    private TextWatcher checkPhoneFormat= new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            if (s.length()<10 || s.length()>10) {
                account_phone.setError(getString(R.string.login_error_register_msg));
            }
        }
    };
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
        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                /*Bundle b = new Bundle();
                b.putSerializable(BUNDLE_ACCESS_KEY, user.getAccessKey());
                intent.putExtras(b);*/
        startActivity(intent);
        finish();
    }


    private void checkStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    String filePath = Environment.getExternalStorageDirectory()+Constants.SDACRD_DIR_APP_ROOT;
                    FileUtil.checkSdCard(filePath);// 檢查S是否有 SD卡,並建立會用到的 SD卡路徑

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


}
