package tw.com.geminihsu.app01;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.common.Constants;

public class DriverIdentityActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    private Button taxi_register;
    private Button uber_register;
    private Button truck_register;
    private Button cargo_register;
    private Button trailer_register;
    private DriverIdentifyInfo driverIdentifyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_identity_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle args = getIntent().getExtras();
        if(args!=null)
            driverIdentifyInfo = (DriverIdentifyInfo) args.getSerializable(DriverLoginActivity.BUNDLE_DRIVER_ACCOUNT_INFO);

        this.findViews();
       // Bundle bundle = this.getIntent().getExtras();


        this.setLister();




    }

    private void findViews()
    {
        taxi_register =  (Button) findViewById(R.id.taxi);
        uber_register =  (Button) findViewById(R.id.uber);
        truck_register = (Button) findViewById(R.id.truck);
        cargo_register = (Button) findViewById(R.id.cargo);
        trailer_register = (Button) findViewById(R.id.trailer);

        if(driverIdentifyInfo!=null) {
            String type = driverIdentifyInfo.getDtype();
            Constants.APP_REGISTER_DRIVER_TYPE dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(type));
            if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                taxi_register.setEnabled(false);
                taxi_register.setBackground(getDrawable(R.color.bg_gray));
            }
            else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER) {
                uber_register.setEnabled(false);
                uber_register.setBackground(getDrawable(R.color.bg_gray));
            }else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK) {
                truck_register.setEnabled(false);
                truck_register.setBackground(getDrawable(R.color.bg_gray));
            }else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO) {
                cargo_register.setEnabled(false);
                cargo_register.setBackground(getDrawable(R.color.bg_gray));
            }else if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER) {
                trailer_register.setEnabled(false);
                trailer_register.setBackground(getDrawable(R.color.bg_gray));
            }
        }
    }




    private void setLister()
    {
        taxi_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(DriverIdentityActivity.this, DriverLoginActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI.ordinal()+1);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        });
        uber_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(DriverIdentityActivity.this, DriverLoginActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER.ordinal()+1);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        });
        truck_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(DriverIdentityActivity.this, DriverLoginActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK.ordinal()+1);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        });
        cargo_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(DriverIdentityActivity.this, DriverLoginActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO.ordinal()+1);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        });
        trailer_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(DriverIdentityActivity.this, DriverLoginActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER.ordinal()+1);
                question.putExtras(b);
                startActivity(question);
                finish();
            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                //將表單資料送出後回到主畫面
                this.finish();
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
