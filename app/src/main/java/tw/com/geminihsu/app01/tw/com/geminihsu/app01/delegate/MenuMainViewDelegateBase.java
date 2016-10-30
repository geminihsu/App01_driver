package tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate;

import tw.com.geminihsu.app01.DriverAccountActivity;
import tw.com.geminihsu.app01.DriverIdentityActivity;
import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.fragment.Fragment_About;
import tw.com.geminihsu.app01.fragment.Fragment_Account;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrder;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01.fragment.Fragment_Bouns;
import tw.com.geminihsu.app01.fragment.Fragment_NotifyList;
import tw.com.geminihsu.app01.fragment.Fragment_OrderFilter;
import tw.com.geminihsu.app01.fragment.Fragment_OrderRecord;
import tw.com.geminihsu.app01.fragment.Fragment_Client_Service;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

import android.content.Intent;
import android.os.Bundle;
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
		Fragment_BeginOrder frag2 = new Fragment_BeginOrder();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_BeginOrder.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_BeginOrder.class.getSimpleName());
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
		fragTran.addToBackStack(Fragment_BeginOrderInteractive.class.getSimpleName());
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
		Fragment_Client_Service frag2 = new Fragment_Client_Service();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Client_Service.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Client_Service.class.getSimpleName());
		fragTran.commit();

	}

	public void setNavigationItemOnClick_Support() {


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
    public void setNavigationItemOnClick_share() {

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
