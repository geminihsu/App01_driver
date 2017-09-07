package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.FormatUtils;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.RealmUtil;
import tw.com.geminihsu.app01Client.utils.Utility;

public class ChangePasswordActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private EditText name_edit;
    private EditText id_edit;
    private EditText phone_edit;
    private EditText old_password;
    private EditText new_password;
    private EditText new_password_confirm;
    private JsonPutsUtil sendDataRequest;
    private AccountInfo accountInfo;

    private boolean isVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
               // choice = bundle.getInt(Constants.ARG_POSITION);
                findViews();
            }else
            {
                //Error!!!!
            }
        }
        else
        {
            //Error!!!!
        }
        this.setLister();

        sendDataRequest = new JsonPutsUtil(ChangePasswordActivity.this);
        sendDataRequest.setAccountChangePasswordDataManagerCallBackFunction(new JsonPutsUtil.AccountChangePasswordDataManagerCallBackFunction() {


            @Override
            public void modifyPassword(boolean status) {
                if(status)
                {

                    Toast.makeText(ChangePasswordActivity.this,
                            "修改成功",
                            Toast.LENGTH_LONG).show();

                    RealmUtil realmUtil = new RealmUtil(ChangePasswordActivity.this);
                    //AccountInfo userInfo = realmUtil.queryAccount(Constants.ACCOUNT_PHONE_NUMBER,driver.getName());
                    if(accountInfo!=null)
                    {
                        //更新資料庫帳號裡面的目前營業身份的欄位
                        AccountInfo new_user = new AccountInfo();
                        new_user.setId(accountInfo.getId());
                        new_user.setUid(accountInfo.getUid());
                        new_user.setName(accountInfo.getName());
                        new_user.setPhoneNumber(accountInfo.getPhoneNumber());
                        new_user.setIdentify(accountInfo.getDriver_type());
                        new_user.setPassword(accountInfo.getPassword());
                        new_user.setConfirm_password(accountInfo.getConfirm_password());
                        new_user.setRecommend_id(accountInfo.getRecommend_id());
                        new_user.setDriver_type(accountInfo.getDriver_type());
                        new_user.setRole(accountInfo.getRole());
                        new_user.setAccessKey(accountInfo.getAccessKey());
                        new_user.setPassword(new_password.getText().toString());
                        realmUtil.updateAccount(new_user);

                    }
                }
            }
        });



        }

    private void findViews()
    {
        //linearLayout_form = (LinearLayout) findViewById(R.id.form);

        /*String url = "www.google.com";

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(url);*/
        name_edit = (EditText) findViewById(R.id.name_edit);
        id_edit = (EditText) findViewById(R.id.id_edit);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        Utility account = new Utility(this);
        accountInfo = account.getAccountInfo();
        name_edit.setText(accountInfo.getName());
        name_edit.setEnabled(false);
        id_edit.setText(accountInfo.getIdentify());
        id_edit.setEnabled(false);
        phone_edit.setText(accountInfo.getPhoneNumber());
        phone_edit.setEnabled(false);


        old_password = (EditText) findViewById(R.id.edit_old_password);
        new_password = (EditText) findViewById(R.id.edit_new_password);
        new_password_confirm = (EditText) findViewById(R.id.edit_new_password_confirm);
        new_password_confirm.addTextChangedListener(checkPassword);


    }


    private TextWatcher checkPassword= new TextWatcher() {
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
            if (!FormatUtils.isConfirmPassword(new_password.getText().toString(),new_password_confirm.getText().toString())) {
                new_password_confirm.setError(getString(R.string.login_error_register_msg));
                isVerifyPassword = false;
            }else
                isVerifyPassword = true;
        }


    };


    private void setLister()
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.driver_account_save));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                //將表單資料送出後回到主畫面
                //this.finish();
                sendDataRequest.sendModifyPasswordRequest(accountInfo,new_password.getText().toString());
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
