package tw.com.geminihsu.app01Client.delegate.customer;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import tw.com.geminihsu.app01Client.MenuMainActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.delegate.MenuMainViewDelegateBase;
import tw.com.geminihsu.app01Client.fragment.Fragment_Client_Service_test;

public class MenuMainViewDelegateCustomer extends MenuMainViewDelegateBase {
	private final String TAG= this.getClass().getSimpleName();
	
	public MenuMainViewDelegateCustomer(MenuMainActivity mainActivity) {
		super(mainActivity);

	}

	@Override
	public void setContentLayoutFragment() {

		Fragment newFragment = new Fragment_Client_Service_test();

		FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
		//if (mainActivity.getSupportFragmentManager().findFragmentByTag(MultiLiveView.PARENT_FRAGMENT_TAG_ID) == null) {
		ft.add(R.id.container, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// ft.addToBackStack(Fragment_Liveview.class.getSimpleName());
		ft.commit();
		//}
	}

	@Override
	public void setNavigationItemOnClick_beginOrder() {
        super.setNavigationItemOnClick_beginOrder();
	}

	@Override
	public void setNavigationItemOnClick_orderFilter() {
		super.setNavigationItemOnClick_orderFilter();
	}

	@Override
	public void setNavigationItemOnClick_waitOrder() {
		super.setNavigationItemOnClick_waitOrder();
	}

	@Override
	public void setNavigationItemOnClick_notify() {
		super.setNavigationItemOnClick_notify();
	}

	@Override
	public void setNavigationItemOnClick_about() {
		super.setNavigationItemOnClick_about();
	}

	@Override
	public void setNavigationItemOnClick_account() {
		super.setNavigationItemOnClick_account();
	}

	@Override
	public void setNavigationItemOnClick_service() {
		super.setNavigationItemOnClick_service();
	}

	@Override
	public void setNavigationItemOnClick_support() {
		super.setNavigationItemOnClick_support();
	}

	@Override
	public void setNavigationItemOnClick_pastOrder() {
		super.setNavigationItemOnClick_pastOrder();
	}
	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);
		//更改為司機和乘客都顯示同樣的選單
		menu.findItem(R.id.navigation_item_order).setVisible(true);
		menu.findItem(R.id.navigation_item_wait).setVisible(false);
		menu.findItem(R.id.navigation_item_begin).setVisible(false);

		menu.findItem(R.id.navigation_item_order).setVisible(true);
		//menu.findItem(R.id.navigation_item_order_record).setVisible(true);
		menu.findItem(R.id.navigation_item_logOut).setVisible(true);

	}
}
