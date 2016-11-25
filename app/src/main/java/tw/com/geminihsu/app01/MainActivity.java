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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.App01libObjectKey;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.ConfigSharedPreferencesUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;

public class MainActivity extends Activity {
    public final static String TAG = MainActivity.class.toString();// from
    public final static String BUNDLE_ACCESS_KEY = "accesskey";// from

    public final static int ERROR_USER_INFO = 0;
    public final static int ERROR_NO_USER = 1;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

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
            if(args!=null){
               user = (AccountInfo)args.getSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO);
                //already has account so query account from database
                queryAccount();
            }else
            {
                RealmUtil realmUtil = new RealmUtil(MainActivity.this);
                user = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER, phone_number);

            }
                Constants.Driver = false;
                Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                /*Bundle b = new Bundle();
                b.putSerializable(BUNDLE_ACCESS_KEY, user.getAccessKey());
                intent.putExtras(b);*/
                startActivity(intent);
                finish();

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
                    queryAccount();

                }
            }
        });
    }

    private void queryAccount(){

        if(user!=null) {
            if(user.getPassword().equals(password)) {
                dialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);
                sendLoginRequest(user);
            }else
                alert(ERROR_USER_INFO);
        }else
            alert(ERROR_NO_USER);

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
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    private void sendLoginRequest(final AccountInfo user)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String versionName = BuildConfig.VERSION_NAME;


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_LOGIN_VERIFY);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_PASSWORD, user.getPassword());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_REGISTER_ID, user.getRegisterToken());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_PLATFORM, "android");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_APP_VERSION, versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    String accesskey = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY);
                    App01libObjectKey.APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_login_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_LOGIN_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_LOGIN_VERIFY_CODE_SUCCESS))
                    {
                       // dialog.cancel();
                        if(user!=null)
                           user.setAccessKey(accesskey);
                        RealmUtil realmUtil = new RealmUtil(MainActivity.this);
                        realmUtil.addAccount(user);
                        Constants.Driver = false;
                        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                        Bundle b = new Bundle();
                        b.putString(BUNDLE_ACCESS_KEY, accesskey);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}
