package tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate.customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.fragment.Fragment_Bouns;
import tw.com.geminihsu.app01.fragment.Fragment_OrderRecord;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate.MenuMainViewDelegateBase;

public class MenuMainViewDelegateCustomer extends MenuMainViewDelegateBase {
	private final String TAG= this.getClass().getSimpleName();
	
	public MenuMainViewDelegateCustomer(MenuMainActivity mainActivity) {
		super(mainActivity);

	}

	@Override
	public void setContentLayoutFragment() {
		

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
	public void setNavigationItemOnClick_pastOrder() {
		super.setNavigationItemOnClick_pastOrder();
	}
	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);
		menu.findItem(R.id.navigation_item_order).setVisible(true);
		menu.findItem(R.id.navigation_item_order_record).setVisible(true);
	}
}
