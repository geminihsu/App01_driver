package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.plus.Account;
import com.google.firebase.iid.FirebaseInstanceId;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.realm.AccountInfo;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.util.RealmUtil;

public class MainActivity extends Activity {

    private TextView txt_forget_password;
    private Button btn_register;
    private Button btn_login;
    private EditText account_phone;
    private EditText account_password;

    private String phone_number;
    private String password;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        this.setLister();


    }

    private void findViews()
    {
        txt_forget_password = (TextView) findViewById(R.id.txt_forget_password);

        btn_register = (Button) findViewById(R.id.register);
        btn_login = (Button) findViewById(R.id.login);

        account_phone = (EditText) findViewById(R.id.account_phone);
        account_password = (EditText)findViewById(R.id.account_password);
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
                //String token = FirebaseInstanceId.getInstance().getToken();
                //Log.d("FCM", "Token:"+token);
                phone_number = account_phone.getText().toString();
                password = account_password.getText().toString();
                if(phone_number.isEmpty()||password.isEmpty())
                {
                    alert();
                }else {

                    RealmUtil realmUtil = new RealmUtil(MainActivity.this);
                    AccountInfo user = realmUtil.queryAccount("phoneNumber",phone_number);

                    if(user!=null) {
                        Constants.Driver = false;
                        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


    private void alert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.login_error_title));

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.login_error_msg))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                        startActivity(intent);
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


}
