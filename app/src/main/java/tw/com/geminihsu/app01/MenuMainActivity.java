package tw.com.geminihsu.app01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.widget.Button;
import android.widget.Toast;

import tw.com.geminihsu.app01.adapter.BeginOrderRecyclerAdapter;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrder;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.fragment.Fragment_ClientAirPlanePickUp;
import tw.com.geminihsu.app01.fragment.Fragment_MerchandiseDorkPickUp;
import tw.com.geminihsu.app01.fragment.Fragment_Support;
import tw.com.geminihsu.app01.fragment.Fragment_TrainPlanePickUp;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate.MenuMainViewDelegateBase;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate.customer.MenuMainViewDelegateCustomer;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.driver.MenuMainViewDelegateDriver;

public class MenuMainActivity extends AppCompatActivity implements Fragment_BeginOrder.TabLayoutSetupCallback,Fragment_ClientAirPlanePickUp.TabLayoutSetupCallback,Fragment_TrainPlanePickUp.TabLayoutSetupCallback,Fragment_MerchandiseDorkPickUp.TabLayoutSetupCallback,
        Fragment_BeginOrderInteractive.OnListItemClickListener{
    private final String TAG=this.getClass().getSimpleName();
    private MenuMainViewDelegateBase viewDelegateBase;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView navigationView;
    private ShareActionProvider mShareActionProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_main);
         // 判斷要用哪一個Delegate
        if (Constants.Driver) {

            viewDelegateBase = new MenuMainViewDelegateDriver(this);
        } else {

            viewDelegateBase = new MenuMainViewDelegateCustomer(this);
        }

         // 加入主頁面fragment
        viewDelegateBase.setContentLayoutFragment();


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
                        Fragment_Support support = new Fragment_Support();
                        android.support.v4.app.FragmentTransaction support_historyTransaction = getSupportFragmentManager().beginTransaction();
                        support_historyTransaction.replace(R.id.container, support);
                        support_historyTransaction.commit();
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
                    case R.id.navigation_item_driver:
                        viewDelegateBase.setNavigationItemOnClick_driver();
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


}