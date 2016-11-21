package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import tw.com.geminihsu.app01.utils.Utility;

public class RegisterActivity extends Activity {

    public final static String BUNDLE_ACCOUNT_INFO = "account";// from

    public final static String TAG = RegisterActivity.class.toString();// from


    private static int id = 1;
    private TextView clause;
    private EditText user_name;
    private EditText user_id;
    private EditText user_phone;
    private EditText user_password;
    private EditText user_password_confirm;
    private EditText recommend_code;

    private Button verify;
    private CheckBox agree;

    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        setLister();


    }

    private void findViews()
    {
        clause = (TextView) findViewById(R.id.agree);

        verify = (Button) findViewById(R.id.send_code);
        verify.setEnabled(false);

        agree  = (CheckBox) findViewById(R.id.txt_forget_password);


        user_name = (EditText) findViewById(R.id.id_name);
        user_id = (EditText) findViewById(R.id.identity);
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password_confirm = (EditText) findViewById(R.id.edit_password_confirm);
        recommend_code = (EditText) findViewById(R.id.code);

    }


    private void setLister()
    {
        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    verify.setEnabled(true);
                else
                    verify.setEnabled(false);
            }
        }
    );
        clause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(RegisterActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.CLAUSE);
                question.putExtras(b);
                startActivity(question);
            }
        });

       verify.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {

               if (checkColumn() && agree.isChecked()) {
                   final AccountInfo user = new AccountInfo();
                   user.setId(id);
                   user.setName(user_name.getText().toString());
                   user.setPhoneNumber(user_phone.getText().toString());
                   user.setIdentify(user_id.getText().toString());
                   user.setPassword(user_password.getText().toString());
                   user.setConfirm_password(user_password_confirm.getText().toString());
                   user.setRecommend_id(recommend_code.getText().toString());
                   user.setRole(Constants.Identify.CLIENT.ordinal());

                   String token = FirebaseInstanceId.getInstance().getToken();
                   Log.d("FCM", "Token:" + token);
                   user.setRegisterToken(token);


                   sendRegisterRequest(user);


               }else
                   alert();
           }

        });
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


    private boolean checkColumn(){
        //check column not null
        if (!Utility.isBlankField(user_name) && !Utility.isBlankField(user_id) && !Utility.isBlankField(user_phone) && !Utility.isBlankField(user_password) && !Utility.isBlankField(user_password_confirm) )
            {
            //make sure user password the same as user password confirm
            if(user_password.getText().toString().equals(user_password_confirm.getText().toString())) {
                //check format
                return true;
            }else
                return false;
        }else
            return false;

    }

    private void sendRegisterRequest(final AccountInfo user)
    {
        String versionName = BuildConfig.VERSION_NAME;
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_REGISTER);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_PASSWORD, user.getPassword());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_IDCARD, user.getIdentify());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_REALNAME, "哈哈");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_REGISTER_ID, user.getRegisterToken());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_PLATFORM, "android");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_REGISTER_APP_VERSION, versionName);
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
                    App01libObjectKey.APP_REGISTER_RESPONSE_CODE connectResult =App01libObjectKey.conversion_register_connect_result(Integer.valueOf(status));

                    if(connectResult.equals(App01libObjectKey.APP_REGISTER_RESPONSE_CODE.K_APP_REGISTER_RESPONSE_CODE_SUCCESS))
                    {
                        Constants.Driver = false;
                        Intent verify = new Intent(RegisterActivity.this, VerifyCodeActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(BUNDLE_ACCOUNT_INFO, user);
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
    /*private void addDataToRealm(AccountBean model) {
        realm.beginTransaction();

        AccountBean accountDetailsModel = realm.createObject(AccountBean.class);
        accountDetailsModel.setName(model.getName());
        accountDetailsModel.setIdentify(model.getIdentify());
        accountDetailsModel.setPhoneNumber(model.getPhoneNumber());
        accountDetailsModel.setLoginPassword(model.getLoginPassword());
        accountDetailsModel.setRecommendedId(model.getRecommendedId());
        accountDetailsModelArrayList.add(accountDetailsModel);

        realm.commitTransaction();

    }

    public void updatePersonDetails(AccountBean model,int position,String personID) {
        AccountBean editPersonDetails = realm.where(AccountBean.class).equalTo("id", personID).findFirst();
        realm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setIdentify(model.getIdentify());
        editPersonDetails.setPhoneNumber(model.getPhoneNumber());
        editPersonDetails.setLoginPassword(model.getLoginPassword());
        editPersonDetails.setRecommendedId(model.getRecommendedId());
        realm.commitTransaction();

        accountDetailsModelArrayList.set(position, editPersonDetails);
    }


    private void getAllUsers() {
        RealmResults<AccountBean> results = realm.where(AccountBean.class).findAll();
        accountDetailsModelArrayList.clear();
        realm.beginTransaction();

        for (int i = 0; i < results.size(); i++) {
            accountDetailsModelArrayList.add(results.get(i));
        }

        if(results.size()>0)
            id = realm.where(AccountBean.class).max("id").intValue() + 1;
        realm.commitTransaction();
        //personDetailsAdapter.notifyDataSetChanged();
    }*/

    private void alert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.register_error_title));
            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_register_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}
