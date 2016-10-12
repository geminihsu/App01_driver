package tw.com.geminihsu.app01.tw.com.geminihsu.app01.driver;


import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.fragment.Fragment_OrderRecord;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate.MenuMainViewDelegateBase;

public class MenuMainViewDelegateDriver extends MenuMainViewDelegateBase {
	private final String TAG= this.getClass().getSimpleName();

	public MenuMainViewDelegateDriver(MenuMainActivity mainActivity) {
		super(mainActivity);

	}

	@Override
	public void setContentLayoutFragment() {


	}

	@Override
	public void setNavigationItemOnClick_pastOrder() {
         super.setNavigationItemOnClick_pastOrder();
	}

	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);
		menu.findItem(R.id.navigation_item_begin).setVisible(true);
		menu.findItem(R.id.navigation_item_order_filter).setVisible(true);
		menu.findItem(R.id.navigation_item_wait).setVisible(true);
		menu.findItem(R.id.navigation_item_order_past).setVisible(true);

	}

}
