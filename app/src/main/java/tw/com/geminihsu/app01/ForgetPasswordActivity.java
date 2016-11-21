package tw.com.geminihsu.app01;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MenuItem;
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

public class ForgetPasswordActivity extends Activity {
    public final static String TAG = ForgetPasswordActivity.class.toString();// from

    private EditText phone_number;
    private EditText id_card;
    private Button send;

    private RequestQueue requestQueue ;
    private JSONObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_page_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        setLister();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

    }


    private void findViews()
    {
        phone_number = (EditText) findViewById(R.id.phone);
        id_card = (EditText) findViewById(R.id.id);

        send = (Button) findViewById(R.id.resend);
    }


    private void setLister()
    {
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!phone_number.getText().toString().isEmpty()&&!id_card.getText().toString().isEmpty())
                    sendReSendRequest();

            }

        });
    }

    private void sendReSendRequest()
    {
        obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_RE_SEND_PASSWORD);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_USERNAME, phone_number.getText().toString());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_RE_SEND_PASSWORD_IDCARD, id_card.getText().toString());

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
                        RealmUtil realmUtil = new RealmUtil(ForgetPasswordActivity.this);
                        AccountInfo user = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,phone_number.getText().toString());

                        if(user==null)
                        {
                            user = new AccountInfo();
                            user.setPhoneNumber(phone_number.getText().toString());
                            user.setIdentify(id_card.getText().toString());
                        }
                        Constants.Driver = false;
                        Intent verify = new Intent(ForgetPasswordActivity.this, VerifyCodeActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO, user);
                        verify.putExtras(b);
                        startActivity(verify);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
