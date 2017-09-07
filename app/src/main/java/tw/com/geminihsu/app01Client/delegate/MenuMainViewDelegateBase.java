package tw.com.geminihsu.app01Client.delegate;

import tw.com.geminihsu.app01Client.DriverIdentityActivity;
import tw.com.geminihsu.app01Client.MainActivity;
import tw.com.geminihsu.app01Client.MenuMainActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.fragment.Fragment_About;
import tw.com.geminihsu.app01Client.fragment.Fragment_Account;
import tw.com.geminihsu.app01Client.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01Client.fragment.Fragment_Bouns;
import tw.com.geminihsu.app01Client.fragment.Fragment_Client_Service_test;
import tw.com.geminihsu.app01Client.fragment.Fragment_NotifyList;
import tw.com.geminihsu.app01Client.fragment.Fragment_OrderFilter;
import tw.com.geminihsu.app01Client.fragment.Fragment_OrderRecord;
import tw.com.geminihsu.app01Client.common.Constants;
import tw.com.geminihsu.app01Client.fragment.Fragment_Support;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class MenuMainViewDelegateBase extends BaseViewDelegate{
	protected MenuMainActivity mainActivity;

	public MenuMainViewDelegateBase(MenuMainActivity _mainActivity) {
		mainActivity = _mainActivity;
	}

	public void setContentLayoutFragment() {

	}


	public void setNavigationItemOnClick_beginOrder() {
		FragmentTransaction fragTran;
		Fragment_BeginOrderList frag2 = new Fragment_BeginOrderList();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_BeginOrderList.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_BeginOrderList.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_orderFilter() {
		FragmentTransaction fragTran;
		Fragment_OrderFilter frag2 = new Fragment_OrderFilter();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_OrderFilter.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_OrderFilter.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_waitOrder() {
		FragmentTransaction fragTran;
		Fragment_BeginOrderList frag2 = new Fragment_BeginOrderList();
		Bundle args2 = new Bundle();
		args2.putBoolean(Constants.ARG_POSITION, true);
		frag2.setArguments(args2);
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_BeginOrderList.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_BeginOrderList.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_pastOrder() {
		FragmentTransaction fragTran;
		Fragment_OrderRecord frag2 = new Fragment_OrderRecord();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_OrderRecord.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_OrderRecord.class.getSimpleName());
		fragTran.commit();
	}


	public void setNavigationItemOnClick_service() {
		FragmentTransaction fragTran;
		Fragment_Client_Service_test frag2 = new Fragment_Client_Service_test();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Client_Service_test.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Client_Service_test.class.getSimpleName());
		fragTran.commit();

	}

	public void setNavigationItemOnClick_support() {

		FragmentTransaction fragTran;
		Fragment_Support frag2 = new Fragment_Support();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Support.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Support.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_notify() {
		FragmentTransaction fragTran;
		Fragment_NotifyList frag2 = new Fragment_NotifyList();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_NotifyList.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_NotifyList.class.getSimpleName());
		fragTran.commit();
	}
	
	public void setNavigationItemOnClick_about() {

		FragmentTransaction fragTran;
		Fragment_About frag2 = new Fragment_About();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_About.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_About.class.getSimpleName());
		fragTran.commit();
	}
    public void setNavigationItemOnClick__logOut() {
		SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);

		configSharedPreferences.edit().putString(mainActivity.getString(R.string.config_login_phone_number_key), "").commit();
		configSharedPreferences.edit().putString(mainActivity.getString(R.string.config_login_password_key), "").commit();

		Intent i = new Intent();
		i.setClass(mainActivity, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mainActivity.startActivity(i);
	}


	public void setNavigationItemOnClick_account() {

		FragmentTransaction fragTran;
		Fragment_Account frag2 = new Fragment_Account();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Account.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Account.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_wallet() {


	}
	public void setNavigationItemOnClick_bounds() {
		FragmentTransaction fragTran;
		Fragment_Bouns frag2 = new Fragment_Bouns();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Bouns.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Bouns.class.getSimpleName());
		fragTran.commit();

	}

	public void setNavigationItemOnClick_driver() {
		Intent question = new Intent(mainActivity, DriverIdentityActivity.class);
		//question.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mainActivity.startActivity(question);

	}


	/*更新actionbar 的item*/
	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);

	}



}
