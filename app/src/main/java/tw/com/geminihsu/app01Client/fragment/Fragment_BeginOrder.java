package tw.com.geminihsu.app01Client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01Client.MenuMainActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.common.Constants;

public class Fragment_BeginOrder extends Fragment implements Fragment_BeginOrderList.BeginOrderRefreshTabListener {

    private final String TAG = Fragment_BeginOrder.class.toString();
    private TabLayoutSetupCallback mToolbarSetupCallback;
    private List<String> mTabNamesList;
    private final int ACTIONBAR_MENU_ITEM_FIILTER = 0x0001;
    private ViewPager viewPager;
    private ItemsPagerAdapter itemsPagerAdapter;

    public Fragment_BeginOrder() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MenuMainActivity) {
            mToolbarSetupCallback = (TabLayoutSetupCallback) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement TabLayoutSetupCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mTabNamesList = new ArrayList<>();
        mTabNamesList.add(getString(R.string.tab_real_time));
        mTabNamesList.add(getString(R.string.tab_reservation));
        //mTabNamesList.add(getString(R.string.tab_company));

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beginorder, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //viewPager.setId(View.generateViewId());

        itemsPagerAdapter = new ItemsPagerAdapter(getChildFragmentManager(), mTabNamesList);
        viewPager.setAdapter(itemsPagerAdapter);
        mToolbarSetupCallback.setupTabLayout(viewPager);

        return view;
    }
    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.begin_order_page_title));
        super.onResume();
        Log.e(TAG,"onResume");
        //viewPager.setAdapter(new ItemsPagerAdapter(getChildFragmentManager(), mTabNamesList));
        //mToolbarSetupCallback.setupTabLayout(viewPager);
        if(itemsPagerAdapter !=null)
        {
            mTabNamesList.clear();
            mTabNamesList.add(getString(R.string.tab_real_time));
            mTabNamesList.add(getString(R.string.tab_reservation));
            //viewPager.setAdapter(itemsPagerAdapter);
            //itemsPagerAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onStart()
    {
        super.onStart();
        Log.e(TAG,"onStart");


    }
    @Override
    public void onStop()
    {
        super.onStop();
        Log.e(TAG,"onStop");
        //call to ViewPage to remove the pages
        //viewPager.removeAllViews();

        //make this to update the pager
        //viewPager.setAdapter(null);
        if(itemsPagerAdapter !=null)
        {
            mTabNamesList.clear();
            itemsPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshTabListener(boolean wait) {
        if(itemsPagerAdapter !=null)
        {
            mTabNamesList.clear();
            mTabNamesList.add(getString(R.string.tab_real_time));
            mTabNamesList.add(getString(R.string.tab_reservation));
            viewPager.setAdapter(itemsPagerAdapter);
            itemsPagerAdapter.notifyDataSetChanged();
        }
    }

    public class ItemsPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> mTabs = new ArrayList<>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {//初始子View方法
            return super.instantiateItem(container, position);

        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//销毁子View
            super.destroyItem(container, position, object);
        }
        public ItemsPagerAdapter(FragmentManager fm, List<String> tabNames) {
            super(fm);

            mTabs = tabNames;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position)
            {
                case 0:

                    /*Fragment_BeginOrderList.BeginOrderRefreshTabListener beginOrderRefreshTabListener;
                    beginOrderRefreshTabListener = new Fragment_BeginOrderList.BeginOrderRefreshTabListener() {
                        @Override
                        public void refreshTabListener(boolean wait) {
                            if(itemsPagerAdapter !=null)
                            {
                               /* mTabNamesList.clear();
                                mTabNamesList.add(getString(R.string.tab_real_time));
                                mTabNamesList.add(getString(R.string.tab_reservation));
                                //viewPager.setAdapter(itemsPagerAdapter);
                                //itemsPagerAdapter.notifyDataSetChanged();*/
                                /*onResume();
                            }

                        }
                    };*/
                    fragment = new Fragment_BeginOrderList();
                    Bundle args2 = new Bundle();
                    args2.putInt(Constants.ARG_POSITION, Fragment_BeginOrderList.REALTIME_ORDERLIST);
                    //args2.putSerializable(Constants.INTERFACE,(Fragment_BeginOrderList.BeginOrderRefreshTabListener)beginOrderRefreshTabListener);
                    fragment.setArguments(args2);
                    break;
                case 1:
                    /*Fragment_BeginOrderList.BeginOrderRefreshTabListener beginOrderRefreshTabListener2;
                    beginOrderRefreshTabListener2 = new Fragment_BeginOrderList.BeginOrderRefreshTabListener() {
                        @Override
                        public void refreshTabListener(boolean wait) {
                            /*if(itemsPagerAdapter !=null)
                            {
                                mTabNamesList.clear();
                                mTabNamesList.add(getString(R.string.tab_real_time));
                                mTabNamesList.add(getString(R.string.tab_reservation));
                                //viewPager.setAdapter(itemsPagerAdapter);
                                //itemsPagerAdapter.notifyDataSetChanged();
                            }*/
                            /*onResume();

                        }
                    };*/
                    fragment = new Fragment_BeginOrderList();
                    Bundle args3 = new Bundle();
                    args3.putInt(Constants.ARG_POSITION, Fragment_BeginOrderList.RESERVATION_ORDERLIST);
                    //args3.putSerializable(Constants.INTERFACE,(Fragment_BeginOrderList.BeginOrderRefreshTabListener)beginOrderRefreshTabListener2);
                    fragment.setArguments(args3);
                    break;
                /*case 2:
                    fragment = new Fragment_BeginOrderList();
                    Bundle args4 = new Bundle();
                    args4.putInt(Constants.ARG_POSITION, Fragment_BeginOrderList.CAR_COMPANY_ORDERLIST);
                    fragment.setArguments(args4);
                    break;*/

            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position);
        }
    }

    public interface TabLayoutSetupCallback {
        void setupTabLayout(ViewPager viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_FIILTER, Menu.NONE, getString(R.string.order_filter_page_title));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_FIILTER:
                // Create new fragment and transaction
                Fragment newFragment = new Fragment_OrderFilter();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
