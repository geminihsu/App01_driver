package tw.com.geminihsu.app01Client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01Client.ClientTakeRideActivity;
import tw.com.geminihsu.app01Client.MenuMainActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.common.Constants;

public class Fragment_TrainPlanePickUp extends Fragment {

    public final static String BUNDLE_ORDER_CUR_ADDRESS = "address";// from

    private TabLayoutSetupCallback mToolbarSetupCallback;
    private List<String> mTabNamesList;
    private final int ACTIONBAR_MENU_ITEM_FIILTER = 0x0001;

    private int dType;//哪一種司機型態
    private int cargoType;//那一種訂單型態

    private static Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private static Constants.APP_REGISTER_ORDER_TYPE orderCargoType;

    private ViewPager viewPager;
    private static String currAddress = "";

    public Fragment_TrainPlanePickUp() {
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
    public void onStop()
    {
        super.onStop();
        //call to ViewPage to remove the pages
        viewPager.removeAllViews();

        //make this to update the pager
        viewPager.setAdapter(null);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        mTabNamesList = new ArrayList<>();
        mTabNamesList.add(getString(R.string.tab_send_train));
        mTabNamesList.add(getString(R.string.tab_pick_up_train));
        Bundle bundle = this.getArguments();
        if (bundle.containsKey(Constants.NEW_ORDER_DTYPE)) {
            dType = bundle.getInt(Constants.NEW_ORDER_DTYPE);
            dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(dType));
        }
        if (bundle.containsKey(Constants.NEW_ORDER_CARGO_TYPE)) {
            cargoType = bundle.getInt(Constants.NEW_ORDER_CARGO_TYPE);
            orderCargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargoType));
        }

        if (bundle.containsKey(BUNDLE_ORDER_CUR_ADDRESS)) {
            currAddress = bundle.getString(BUNDLE_ORDER_CUR_ADDRESS);

        }
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beginorder, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //viewPager.setId(View.generateViewId());
        viewPager.setAdapter(new ItemsPagerAdapter(getChildFragmentManager(), mTabNamesList));
        mToolbarSetupCallback.setupTabLayout(viewPager);

        return view;
    }
    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.client_train_pick_up));
        super.onResume();


    }

    public static class ItemsPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> mTabs = new ArrayList<>();

        public ItemsPagerAdapter(FragmentManager fm, List<String> tabNames) {
            super(fm);

            mTabs = tabNames;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {//初始子View方法
            return super.instantiateItem(container, position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//销毁子View
            super.destroyItem(container, position, object);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position)
            {
                case 0:
                    fragment = new Fragment_PickUpTrain();
                    Bundle args2 = new Bundle();
                    args2.putInt(Constants.ARG_POSITION, 0);
                    args2.putString(BUNDLE_ORDER_CUR_ADDRESS, currAddress);

                    //args2.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
                    //args2.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
                    fragment.setArguments(args2);
                    break;
                case 1:

                    fragment = new Fragment_PickUpTrain();
                    Bundle args3 = new Bundle();
                    args3.putInt(Constants.ARG_POSITION, 1);
                    args3.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
                    args3.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
                    args3.putString(BUNDLE_ORDER_CUR_ADDRESS, currAddress);

                    fragment.setArguments(args3);
                    break;


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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_FIILTER, Menu.NONE, getString(R.string.menu_take));
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

                // set dialog message
                alertDialogBuilder
                        .setMessage("2015/12/08 上午07:04\n從:台中火車站\n停:繼光街口\n到:台中市政府")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent question = new Intent(getActivity(), ClientTakeRideSearchActivity.class);
                                startActivity(question);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
