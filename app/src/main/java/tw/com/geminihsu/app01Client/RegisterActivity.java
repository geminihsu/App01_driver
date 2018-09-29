package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.FormatUtils;
import tw.com.geminihsu.app01Client.utils.JsonPutsUtil;
import tw.com.geminihsu.app01Client.utils.Utility;

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

    private boolean isVerifyId,isVerifyPhone,isVerifyPassword;

    private Button verify;
    private CheckBox agree;
    private boolean debug = false;

    private JsonPutsUtil sendDataRequest;
    private ProgressDialog dialog;

    private boolean debug_test =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_activity);
        if(debug)
        {
            isVerifyId = true;
            isVerifyPhone = true;
            isVerifyPassword = true;
        }
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        sendDataRequest = new JsonPutsUtil(RegisterActivity.this);
        sendDataRequest.setClientRegisterDataManagerCallBackFunction(new JsonPutsUtil.ClientRegisterDataManagerCallBackFunction() {

            @Override
            public void registerClient(AccountInfo accountInfo) {
                if(dialog!=null)
                {
                    dialog.dismiss();
                    dialog = null;
                }
                Intent verify = new Intent(RegisterActivity.this, VerifyCodeActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(BUNDLE_ACCOUNT_INFO, accountInfo);
                verify.putExtras(b);
                startActivity(verify);
                finish();
            }

            @Override
            public void registerClientError(Boolean isError, String message) {
                if(dialog!=null)
                {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
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
        user_id.addTextChangedListener(checkIdentityFormat);
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_phone.addTextChangedListener(checkPhoneFormat);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password_confirm = (EditText) findViewById(R.id.edit_password_confirm);
        user_password_confirm.addTextChangedListener(checkPassword);
        recommend_code = (EditText) findViewById(R.id.code);

        if(debug_test)
        {
            user_name.setText("測試");
            user_id.setText("H234124556");
            user_phone.setText("0965833129");
            user_password.setText("gemini");
            user_password_confirm.setText("gemini");
        }
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

               if (checkColumn() && agree.isChecked()&&isVerifyPhone&&isVerifyPassword&&isVerifyId) {
                   Utility info = new Utility(RegisterActivity.this);
                   if (info.getAccountInfo() != null) {
                       //將就得資料清除
                       info.clearData(AccountInfo.class);
                   }
                   if (dialog == null){
                       dialog = ProgressDialog.show(RegisterActivity.this, "",
                               "Loading. Please wait...", true);

                   final AccountInfo user = new AccountInfo();
                   user.setId(id);
                   user.setName(user_name.getText().toString());
                   user.setPhoneNumber(user_phone.getText().toString());
                   user.setIdentify(user_id.getText().toString());
                   user.setPassword(user_password.getText().toString());
                   user.setConfirm_password(user_password_confirm.getText().toString());
                   user.setRecommend_id(recommend_code.getText().toString());
                   user.setRole(Constants.Identify.CLIENT.ordinal());

                   //String token = FirebaseInstanceId.getInstance().getToken();
                   double tokenNumber = Math.random();
                   Log.d("FCM", "Token:" +tokenNumber );
                   user.setRegisterToken(""+tokenNumber);


                   sendDataRequest.sendRegisterRequest(user);
               }

               }else
                   alert();
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
                isVerifyId = false;
                user_id.setError(getString(R.string.login_error_register_msg));
            }else
                isVerifyId =true;
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
           // if (!FormatUtils.isPhoneNumberValid(temp.toString())) {
            if (temp.toString().length()!=10) {
                user_phone.setError(getString(R.string.login_error_register_msg));
                isVerifyPhone = false;
            }else
                isVerifyPhone = true;
        }


    };

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
            if (!FormatUtils.isConfirmPassword(temp.toString(),user_password.getText().toString())) {
                user_password_confirm.setError(getString(R.string.login_error_register_msg));
                isVerifyPassword = false;
            }else
                isVerifyPassword = true;
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


    private boolean checkColumn(){
        //check column not null
        boolean check = false;
        if (!FormatUtils.isBlankField(user_name) && !FormatUtils.isBlankField(user_id) && !FormatUtils.isBlankField(user_phone) && !FormatUtils.isBlankField(user_password) && !FormatUtils.isBlankField(user_password_confirm))
        {
            //make sure user password the same as user password confirm
            if(user_password.getText().toString().equals(user_password_confirm.getText().toString())) {
                //check format
                check = true;
            }else {
                check = false;
            }
        }else {
            check =false;
        }

        return check;
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
