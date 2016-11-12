package tw.com.geminihsu.app01;


import android.app.Activity;
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

import java.util.ArrayList;

//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.RealmResults;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;
//import tw.com.geminihsu.app01.tw.com.geminihsu.app01.realm.AccountBean;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.realm.AccountInfo;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.util.Utility;

public class RegisterActivity extends Activity {

    //private static ArrayList<AccountBean> accountDetailsModelArrayList = new ArrayList<>();
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
    private static RegisterActivity instance;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        mRealm = Realm.getInstance(
                new RealmConfiguration.Builder(this)
                        .name("data.realm")
                        .build()
        );
    }
    public static RegisterActivity getInstance() {
        return instance;
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
               mRealm.beginTransaction();

               AccountInfo user = mRealm.createObject(AccountInfo.class);
               user.setId(id);
               user.setName(user_name.getText().toString());
               user.setPhoneNumber(user_phone.getText().toString());
               user.setIdentify(user_id.getText().toString());
               user.setPassword(user_password.getText().toString());
               user.setConfirm_password(user_password_confirm.getText().toString());
               user.setRecommend_id(recommend_code.getText().toString());
               mRealm.commitTransaction();

                Constants.Driver = false;
                Intent question = new Intent(RegisterActivity.this, VerifyCodeActivity.class);
                startActivity(question);
               //addAccountDetails(null,-1);
               //getAllUsers();
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

   /* public void addAccountDetails(final AccountBean model,final int position) {

        // if (checkColumn()) {
        AccountBean accountBean = new AccountBean();
        accountBean.setName(user_name.getText().toString());
        accountBean.setIdentify(user_id.getText().toString());
        accountBean.setPhoneNumber(user_phone.getText().toString());
        accountBean.setLoginPassword(user_password.getText().toString());
        accountBean.setRecommendedId(recommend_code.getText().toString());

        if (model == null)
            addDataToRealm(accountBean);
        else
            updatePersonDetails(accountBean, position, model.getCode());


        //  }
    }

    private boolean checkColumn(){
        //check column not null
        if (!Utility.isBlankField(user_name) && !Utility.isBlankField(user_id) && !Utility.isBlankField(user_phone) && !Utility.isBlankField(user_password) && !Utility.isBlankField(user_password_confirm) && !Utility.isBlankField(recommend_code))
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
}
