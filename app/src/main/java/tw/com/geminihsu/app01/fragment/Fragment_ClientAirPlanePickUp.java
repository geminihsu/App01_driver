package tw.com.geminihsu.app01.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;

public class Fragment_ClientAirPlanePickUp extends Fragment {

    private TabLayoutSetupCallback mToolbarSetupCallback;
    private List<String> mTabNamesList;
    private final int ACTIONBAR_MENU_ITEM_FIILTER = 0x0001;

    public Fragment_ClientAirPlanePickUp() {
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
        mTabNamesList.add(getString(R.string.tab_send));
        mTabNamesList.add(getString(R.string.tab_pick_up));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beginorder, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ItemsPagerAdapter(getChildFragmentManager(), mTabNamesList));
        mToolbarSetupCallback.setupTabLayout(viewPager);

        return view;
    }
    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.begin_order_page_title));
        super.onResume();


    }

    public static class ItemsPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> mTabs = new ArrayList<>();

        public ItemsPagerAdapter(FragmentManager fm, List<String> tabNames) {
            super(fm);

            mTabs = tabNames;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment_BeginOrderInteractive.newInstance();
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
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_FIILTER, Menu.NONE, getString(R.string.order_filter_page_title));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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