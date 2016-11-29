package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.App01libObjectKey;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.RealmUtil;


public class VerifyCodeActivity extends Activity {
    public final static String TAG = VerifyCodeActivity.class.toString();// from


    private TextView error;
    private Button confirm;
    private EditText code;
    private SharedPreferences configSharedPreferences;
    private String phone_number;
    private String password;
    private AccountInfo accountInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_page_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();

        this.setLister();
        Bundle args = getIntent().getExtras();
        accountInfo = (AccountInfo)args.getSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO);




    }

    private void findViews()
    {
        configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        error = (TextView) findViewById(R.id.error);
        confirm = (Button) findViewById(R.id.login);
        //confirm.setEnabled(false);
        code = (EditText) findViewById(R.id.code);
        code.setText("1234");
    }




    private void setLister()
    {
        error.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendReSendRequest();

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Constants.Driver)
                {
                    Intent question = new Intent(VerifyCodeActivity.this, DriverIdentityActivity.class);
                    startActivity(question);

                }else
                {
                    if(!code.getText().toString().equals(""))
                    {
                        sendVerify();
                    }
                }
            }
        });
    }

    private void sendVerify()
    {

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_VERIFY);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_VERIFY_CODE, code.getText().toString());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_VERIFY_USERNAME, accountInfo.getPhoneNumber());

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

                    App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE connectResult =App01libObjectKey.conversion_verify_code_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_VERIFY_RESPONSE_CODE.K_APP_ACCOUNT_VERIFY_CODE_SUCCESS))
                    {
                        configSharedPreferences.edit().putString(getString(R.string.config_login_phone_number_key), accountInfo.getPhoneNumber()).commit();
                        configSharedPreferences.edit().putString(getString(R.string.config_login_password_key), accountInfo.getPassword()).commit();



                        //insert new account database
                        //RealmUtil data = new RealmUtil(VerifyCodeActivity.this);
                        //data.addAccount(accountInfo);
                        Intent question = new Intent(VerifyCodeActivity.this, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO, accountInfo);
                        question.putExtras(b);
                        question.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(question);

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

    private void sendReSendRequest()
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_RE_SEND_PASSWORD);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_USERNAME, accountInfo.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_IDCARD, accountInfo.getIdentify());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE connectResult =App01libObjectKey.conversion_resend_password_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_ACCOUNT_RE_SEND_PASSWORD_RESPONSE.K_APP_ACCOUNT_RE_SEND_PASSWORD_SUCCESS))
                    {
                        RealmUtil realmUtil = new RealmUtil(VerifyCodeActivity.this);
                        AccountInfo user = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,accountInfo.getPhoneNumber());

                        if(user==null)
                        {
                            user = new AccountInfo();
                            user.setPhoneNumber(user.getPhoneNumber());
                            user.setIdentify(user.getIdentify());
                        }
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerifyCodeActivity.this);
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(getString(R.string.txt_verify_info))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_get_on_car_comfirm),new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {


                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
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
