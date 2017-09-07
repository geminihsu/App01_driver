package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.utils.FormatUtils;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;

public class ForgetPasswordActivity extends Activity {
    public final static String TAG = ForgetPasswordActivity.class.toString();// from

    private EditText phone_number;
    private EditText id_card;
    private Button send;

   // private RequestQueue requestQueue ;
    private JsonPutsUtil forgot_password;
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
       // requestQueue = Volley.newRequestQueue(getApplicationContext());
        forgot_password = new JsonPutsUtil(ForgetPasswordActivity.this);
        forgot_password.setmClientSmsVerifyDataManagerCallBackFunctionn(new JsonPutsUtil.ClientSmsVerifyDataManagerCallBackFunction() {


            @Override
            public void verifyClient(AccountInfo accountInfo) {

            }

            @Override
            public void reSendSMS(AccountInfo accountInfo) {
                Intent intent = new Intent(getApplicationContext(), VerifyCodeActivity.class);
                Bundle b=new Bundle();
                b.putSerializable(RegisterActivity.BUNDLE_ACCOUNT_INFO,accountInfo);
                b.putInt(VerifyCodeActivity.BUNDLE_NEW_PASSWORD,VerifyCodeActivity.VERIFY_NEW_PASSWORD);
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void modifyPassword(AccountInfo accountInfo) {

            }
        });
    }


    private void findViews()
    {
        phone_number = (EditText) findViewById(R.id.phone);
        phone_number.addTextChangedListener(checkPhoneFormat);
        id_card = (EditText) findViewById(R.id.id);
        id_card.addTextChangedListener(checkIdentityFormat);

        send = (Button) findViewById(R.id.resend);
    }


    private void setLister()
    {
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!phone_number.getText().toString().isEmpty()&&!id_card.getText().toString().isEmpty()) {
                    AccountInfo accountInfo = new AccountInfo();
                    accountInfo.setPhoneNumber(phone_number.getText().toString());
                    accountInfo.setIdentify(id_card.getText().toString());
                    forgot_password.sendReSendPasswordRequest(accountInfo);
                }

            }

        });
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
                id_card.setError(getString(R.string.login_error_register_msg));
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
                phone_number.setError(getString(R.string.login_error_register_msg));
            }
        }
    };
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
