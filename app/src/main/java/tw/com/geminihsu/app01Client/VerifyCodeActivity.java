package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;


public class VerifyCodeActivity extends Activity {
    public final static String TAG = VerifyCodeActivity.class.toString();// from
    public final static String BUNDLE_NEW_PASSWORD = "new_password";// from
    final public static int VERIFY_PASSWORD = 0;
    final public static int VERIFY_NEW_PASSWORD = 1;


    private TextView error;
    private Button confirm;
    private EditText code;
    private EditText new_password;
    private SharedPreferences configSharedPreferences;
    private String phone_number;
    private String password;
    private AccountInfo accountInfo;
    private JsonPutsUtil sendDataRequest;
    private LinearLayout linearLayout_new_password;
    private LinearLayout info;
    private int new_password_request;
    private CountDownTimer checkEnterTimer;
    private TextView txt_countDownMsg;
    private boolean isExpired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_page_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(VerifyCodeActivity.this);
        sendDataRequest.setmClientSmsVerifyDataManagerCallBackFunctionn(new JsonPutsUtil.ClientSmsVerifyDataManagerCallBackFunction() {

            @Override
            public void verifyClient(AccountInfo accountInfo) {
                configSharedPreferences.edit().putString(getString(R.string.config_login_phone_number_key), accountInfo.getPhoneNumber()).commit();
                configSharedPreferences.edit().putString(getString(R.string.config_login_password_key), accountInfo.getPassword()).commit();



                //insert new account database
                //RealmUtil data = new RealmUtil(VerifyCodeActivity.this);
                //data.addAccount(accountInfo);
                Intent question = new Intent(VerifyCodeActivity.this, MainActivity.class);
                //Bundle b = new Bundle();
                //b.putSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO, accountInfo);
                //question.putExtras(b);
                question.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(question);
                finish();
            }

            @Override
            public void reSendSMS(final AccountInfo accountInfo) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerifyCodeActivity.this);
                // set dialog message
                alertDialogBuilder
                        .setMessage(getString(R.string.txt_verify_info))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_get_on_car_comfirm),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent question = new Intent(VerifyCodeActivity.this, MainActivity.class);
                                //Bundle b = new Bundle();
                                //b.putSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO, accountInfo);
                                //question.putExtras(b);
                                question.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(question);
                                finish();


                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }

            @Override
            public void modifyPassword(AccountInfo accountInfo) {
                configSharedPreferences.edit().putString(getString(R.string.config_login_phone_number_key), accountInfo.getPhoneNumber()).commit();
                configSharedPreferences.edit().putString(getString(R.string.config_login_password_key), accountInfo.getPassword()).commit();



                //insert new account database
                //RealmUtil data = new RealmUtil(VerifyCodeActivity.this);
                //data.addAccount(accountInfo);
                Intent question = new Intent(VerifyCodeActivity.this, MainActivity.class);
                //Bundle b = new Bundle();
                //b.putSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO, accountInfo);
                //question.putExtras(b);
                question.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(question);
                finish();
            }
        });

        checkEnterTimer = new CountDownTimer(60000,1000){

            @Override
            public void onFinish() {
                txt_countDownMsg.setText("驗證已失效");
                confirm.setText("重新取得驗證碼");
                code.setVisibility(View.GONE);
                txt_countDownMsg.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                isExpired = true;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                String value= String.valueOf(millisUntilFinished/1000);
                String message = getResources().getString(R.string.txt_verify_code_warm);
                String result = String.format(message, value);
                txt_countDownMsg.setText(result);
            }

        }.start();

        }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();

        this.setLister();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            accountInfo = (AccountInfo) bundle.getSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO);
            if (bundle.containsKey(BUNDLE_NEW_PASSWORD)) {
                //判斷是驗證密碼還是重新發送密碼
                new_password_request = bundle.getInt(BUNDLE_NEW_PASSWORD);
                if (new_password_request == VERIFY_NEW_PASSWORD) {
                    error.setVisibility(View.GONE);
                    linearLayout_new_password.setVisibility(View.VISIBLE);
                }

            }

        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        if(checkEnterTimer!=null)
        {
            checkEnterTimer.cancel();
            checkEnterTimer = null;
        }

    }

    private void findViews()
    {
        configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        linearLayout_new_password = (LinearLayout)findViewById(R.id.linearLayout_new_password);

        info = (LinearLayout)findViewById(R.id.info);

        error = (TextView) findViewById(R.id.error);
        confirm = (Button) findViewById(R.id.login);
        //confirm.setEnabled(false);
        code = (EditText) findViewById(R.id.code);
        new_password = (EditText)findViewById(R.id.edit_new_password);
        txt_countDownMsg = (TextView)findViewById(R.id.txt_forget_password);
    }




    private void setLister()
    {
        error.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendDataRequest.sendReSendPasswordRequest(accountInfo);

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    if(!code.getText().toString().equals("")) {
                        if (new_password_request == VERIFY_NEW_PASSWORD)
                            sendDataRequest.sendForgetModify(accountInfo, code.getText().toString(), new_password.getText().toString());
                        else
                            sendDataRequest.sendVerify(code.getText().toString(), accountInfo);
                    }

                    if(isExpired)
                    {
                        sendDataRequest.sendReSendPasswordRequest(accountInfo);
                    }
            }
        });


    }






}
