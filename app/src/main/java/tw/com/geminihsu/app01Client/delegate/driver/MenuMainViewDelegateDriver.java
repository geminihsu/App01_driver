package tw.com.geminihsu.app01Client.delegate.driver;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;

import tw.com.geminihsu.app01Client.MenuMainActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.delegate.MenuMainViewDelegateBase;
import tw.com.geminihsu.app01Client.fragment.Fragment_BeginOrder;

public class MenuMainViewDelegateDriver extends MenuMainViewDelegateBase {
	private final String TAG= this.getClass().getSimpleName();
	private ShareActionProvider mShareActionProvider;

	public MenuMainViewDelegateDriver(MenuMainActivity mainActivity) {
		super(mainActivity);

	}

	@Override
	public void setContentLayoutFragment() {
        // Instantiate a new fragment.
		Fragment newFragment = new Fragment_BeginOrder();

		FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
		//if (mainActivity.getSupportFragmentManager().findFragmentByTag(MultiLiveView.PARENT_FRAGMENT_TAG_ID) == null) {
			ft.add(R.id.container, newFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			// ft.addToBackStack(Fragment_Liveview.class.getSimpleName());
			ft.commit();
		//}

	}

	@Override
	public void setNavigationItemOnClick_pastOrder() {
         super.setNavigationItemOnClick_pastOrder();
	}

	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);

		menu.findItem(R.id.navigation_item_order).setVisible(false);
		//menu.findItem(R.id.navigation_item_order_record).setVisible(true);
		menu.findItem(R.id.navigation_item_begin).setVisible(true);
		//menu.findItem(R.id.navigation_item_order_filter).setVisible(true);
		menu.findItem(R.id.navigation_item_wait).setVisible(true);
		//menu.findItem(R.id.navigation_item_order_past).setVisible(true);
		menu.findItem(R.id.navigation_item_wallet).setVisible(true);
		menu.findItem(R.id.navigation_item_logOut).setVisible(true);

	}

	private Intent setIntent(){

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		sendIntent.putExtra(Intent.EXTRA_TEXT, "分享的文字");


		return sendIntent;

	}

	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}


}
