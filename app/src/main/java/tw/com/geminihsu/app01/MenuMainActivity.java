package tw.com.geminihsu.app01;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrder;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.fragment.Fragment_ClientAirPlanePickUp;
import tw.com.geminihsu.app01.fragment.Fragment_MerchandiseDorkPickUp;
import tw.com.geminihsu.app01.fragment.Fragment_Support;
import tw.com.geminihsu.app01.fragment.Fragment_TrainPlanePickUp;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.MenuMainViewDelegateBase;
import tw.com.geminihsu.app01.delegate.customer.MenuMainViewDelegateCustomer;
import tw.com.geminihsu.app01.delegate.driver.MenuMainViewDelegateDriver;
import tw.com.geminihsu.app01.service.App01Service;
import tw.com.geminihsu.app01.utils.RealmBackupRestore;
import tw.com.geminihsu.app01.utils.Utility;

public class MenuMainActivity extends AppCompatActivity implements Fragment_BeginOrder.TabLayoutSetupCallback,Fragment_ClientAirPlanePickUp.TabLayoutSetupCallback,Fragment_TrainPlanePickUp.TabLayoutSetupCallback,Fragment_MerchandiseDorkPickUp.TabLayoutSetupCallback,
        Fragment_BeginOrderInteractive.OnListItemClickListener,LocationListener {
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=100;
    public final static int ERROR_NO_GPS = 2;
    private final String TAG=this.getClass().getSimpleName();
    private MenuMainViewDelegateBase viewDelegateBase;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView navigationView;
    private ShareActionProvider mShareActionProvider;
    private App01Service myBinder;
    private AccountInfo user;


    private ServiceConnection myLocalServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "NotifyAppServiceBinder_onServiceDisconnected");

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "NotifyAppServiceBinder_onServiceConnected");
            Utility info = new Utility(MenuMainActivity.this);
            myBinder = ((App01Service.App01ServiceServiceBinder) service).getService(info.getAccountInfo());
            //myBinder.setUserInfo(user);
            myBinder.requestServerContentDetail();
            myBinder.startToGetPutNotify();
            myBinder.App01ServiceCheckGPS();
        }
    };


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_main);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            alert(ERROR_NO_GPS);
        }

        Utility info = new Utility(MenuMainActivity.this);

        // 判斷要用哪一個Delegate
        if(info.getDriverAccountInfo()!=null) {

            viewDelegateBase = new MenuMainViewDelegateDriver(this);
        } else {

            viewDelegateBase = new MenuMainViewDelegateCustomer(this);
        }

         // 加入主頁面fragment
        viewDelegateBase.setContentLayoutFragment();


       /* if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS,INITIAL_REQUEST);
        }*/

        //RealmBackupRestore realmBackupRestore = new RealmBackupRestore(this);
        //realmBackupRestore.backup();

        if(!runtime_permissions()){
            Intent serviceIntent = new Intent(this, App01Service.class);
            bindService(serviceIntent, myLocalServiceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        this.setLister();

    }



    private void findViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        //calling sync state is necessary or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        viewDelegateBase.setNavigationItem(navigationView.getMenu());
        navigationView.setCheckedItem(0);
    }

    private void setLister()
    {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();


                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_begin:
                        viewDelegateBase.setNavigationItemOnClick_beginOrder();
                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_order_filter:
                        viewDelegateBase.setNavigationItemOnClick_orderFilter();
                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_wait:
                        viewDelegateBase.setNavigationItemOnClick_waitOrder();

                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_order_past:
                        viewDelegateBase.setNavigationItemOnClick_pastOrder();

                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_order:
                        // Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
                        viewDelegateBase.setNavigationItemOnClick_service();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.navigation_item_order_record:
                        viewDelegateBase.setNavigationItemOnClick_pastOrder();

                        return true;
                    case R.id.navigation_item_support:
                        viewDelegateBase.setNavigationItemOnClick_support();
                        return true;
                    case R.id.navigation_item_notification:
                        viewDelegateBase.setNavigationItemOnClick_notify();

                        return true;
                    case R.id.navigation_item_about:
                        viewDelegateBase.setNavigationItemOnClick_about();
                        return true;
                    case R.id.navigation_item_share:

                        // MenuItem item = menuItem.getSubMenu().findItem(R.id.navigation_item_share);
                        // Fetch and store ShareActionProvider
                        ;


                        mShareActionProvider =(ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
                        setShareIntent(createShareIntent());


                       /* Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg"); // might be text, sound, whatever
                        //share.putExtra(Intent.EXTRA_STREAM, pathToPicture);
                        startActivity(Intent.createChooser(share, "share"));*/
                        return true;
                    case R.id.navigation_item_account:
                        viewDelegateBase.setNavigationItemOnClick_account();
                        return true;
                    case R.id.navigation_item_bouns:
                        viewDelegateBase.setNavigationItemOnClick_bounds();
                        return true;
                    case R.id.navigation_item_logOut:
                        viewDelegateBase.setNavigationItemOnClick__logOut();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;


                }
            }
        });
    }
        @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myBinder.stopToGetNotifyInfo();
        unbindService(myLocalServiceConnection);
    }
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "http://stackandroid.com");
        return shareIntent;
    }

    @Override
    public void setupTabLayout(ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onListItemClick(View v) {


    }

    private void alert(int error)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.login_error_gps));

        if(error==ERROR_NO_GPS)
        {
            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_gps_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        }
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        Log.e(TAG,"Latitude:"+lat+",Longitude:"+lng);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Intent serviceIntent = new Intent(this, App01Service.class);
                bindService(serviceIntent, myLocalServiceConnection, Context.BIND_AUTO_CREATE);

            }else {
                runtime_permissions();
            }
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }


    @SuppressLint("NewApi")
    private boolean hasPermission(String perm) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }else
            return false;
    }
}