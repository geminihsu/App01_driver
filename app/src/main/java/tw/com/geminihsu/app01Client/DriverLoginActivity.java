package tw.com.geminihsu.app01Client;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.bean.AccountInfo;
import tw.com.geminihsu.app01Client.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.utils.Utility;

public class DriverLoginActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    public final static String BUNDLE_DRIVER_ACCOUNT_INFO = "driver_account";// from

    private LinearLayout linearLayout_form;

    final public static int QUESTION = 0;

    final public static int CLAUSE = 1;
    final public static int SUGGESTION= 2;
    private EditText car_brand_name;
    private EditText car_cc_number;
    private EditText car_release_year;
    private EditText car_type_id;
    private EditText car_licence_number;

    private DriverIdentifyInfo driverIdentifyInfo;
    private int choice = 0;
    private boolean debug_test =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_login_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        driverIdentifyInfo = new DriverIdentifyInfo();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                choice = bundle.getInt(Constants.ARG_POSITION);
                displayLayout(choice);
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




    }

    private void findViews()
    {
        linearLayout_form = (LinearLayout) findViewById(R.id.form);

        car_brand_name = (EditText) findViewById(R.id.car_brand_name);
        car_cc_number = (EditText) findViewById(R.id.car_cc_number);
        car_release_year = (EditText) findViewById(R.id.car_release_year);
        car_type_id = (EditText) findViewById(R.id.car_type_id);
        car_licence_number = (EditText) findViewById(R.id.car_work_licence_number);

        if(debug_test)
        {
            car_brand_name.setText("Audil奧迪");
            car_cc_number.setText("1800");
            car_release_year.setText("2005");
            car_type_id.setText("ABY-7121");
            car_licence_number.setText("A12345678");
        }


    }



    private void displayLayout(int identify)
    {

    }

    private void setLister()
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.btn_finish));
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
                Utility info = new Utility(DriverLoginActivity.this);
                AccountInfo user = info.getAccountInfo();
                driverIdentifyInfo.setUid(user.getUid());
                driverIdentifyInfo.setName(user.getPhoneNumber());
                driverIdentifyInfo.setAccesskey(user.getAccessKey());
                driverIdentifyInfo.setCar_brand(car_brand_name.getText().toString());
                driverIdentifyInfo.setCar_cc(car_cc_number.getText().toString());
                driverIdentifyInfo.setCar_born(car_release_year.getText().toString());
                driverIdentifyInfo.setCar_number(car_type_id.getText().toString());
                driverIdentifyInfo.setCar_reg(car_licence_number.getText().toString());
                driverIdentifyInfo.setDtype(""+choice);
                Intent question = new Intent(this, PhotoVerifyActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(BUNDLE_DRIVER_ACCOUNT_INFO, driverIdentifyInfo);
                question.putExtras(b);
                startActivity(question);
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
