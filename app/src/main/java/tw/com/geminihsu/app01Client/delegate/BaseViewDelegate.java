package tw.com.geminihsu.app01Client.delegate;

import android.app.Activity;
import android.view.Menu;

import tw.com.geminihsu.app01Client.R;

public class BaseViewDelegate {
	public void setNavigationItem(Menu menu)
	{
		//先全部隱藏
		menu.findItem(R.id.navigation_item_begin).setVisible(false);//駕駛開始接單頁面
		menu.findItem(R.id.navigation_item_order_filter).setVisible(false);//駕駛訂單篩選頁面
		menu.findItem(R.id.navigation_item_wait).setVisible(false);//駕駛待跑訂單頁面
		menu.findItem(R.id.navigation_item_order_past).setVisible(false);//駕駛過去訂單頁面
		menu.findItem(R.id.navigation_item_order).setVisible(false);// 客戶訂單選擇頁面
		menu.findItem(R.id.navigation_item_order_record).setVisible(false);//客戶訂單紀錄
		menu.findItem(R.id.navigation_item_wallet).setVisible(false);//客戶訂單紀錄
		menu.findItem(R.id.navigation_item_logOut).setVisible(false);//登出


	}
	final public void setActionBarTitle(Activity activity , String displayName ) {
		if (displayName ==null || displayName.equals(""))
			activity.setTitle(activity.getString(R.string.app_name));
		else
			activity.setTitle(displayName);
	}
}
