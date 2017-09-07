package tw.com.geminihsu.app01Client.delegate.driver;


import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;

import tw.com.geminihsu.app01Client.ChangePasswordActivity;
import tw.com.geminihsu.app01Client.CommentActivity;
import tw.com.geminihsu.app01Client.DriverAccountActivity;
import tw.com.geminihsu.app01Client.DriverIdentityActivity;
import tw.com.geminihsu.app01Client.DriverLoginActivity;
import tw.com.geminihsu.app01Client.PhotoVerifyActivity;
import tw.com.geminihsu.app01Client.R;
import tw.com.geminihsu.app01Client.RecommendActivity;
import tw.com.geminihsu.app01Client.fragment.Fragment_Account;
import tw.com.geminihsu.app01Client.delegate.Fragment_AccountDelegateBase;

public class Fragment_AccountDelegateDriver extends Fragment_AccountDelegateBase {
	protected Fragment_Account fragment;

	public Fragment_AccountDelegateDriver(Fragment_Account _fagment_Account) {
		super(_fagment_Account);
		fragment = _fagment_Account;
	}

	@Override
	public void listViewOnItemClickListener(AdapterView<?> parent, View view, int position, long id) {
		if(position == 0) {
			Intent question = new Intent(fragment.getActivity(), ChangePasswordActivity.class);
			fragment.startActivity(question);
		}else  if(position == 1) {
			Intent question = new Intent(fragment.getActivity(), DriverAccountActivity.class);
			fragment.startActivity(question);
		}else  if(position == 2) {
			Intent question = new Intent(fragment.getActivity(), CommentActivity.class);
			fragment.startActivity(question);
		}else  if(position == 3) {
			Intent question = new Intent(fragment.getActivity(), DriverIdentityActivity.class);
			fragment.startActivity(question);
		}else  if(position == 4) {
			Intent question = new Intent(fragment.getActivity(), DriverLoginActivity.class);
			fragment.startActivity(question);
		}else  if(position == 5) {
			Intent question = new Intent(fragment.getActivity(), PhotoVerifyActivity.class);
			fragment.startActivity(question);
		}else  if(position == 6) {
			Intent question = new Intent(fragment.getActivity(), RecommendActivity.class);
			fragment.startActivity(question);
		}

	}

	@Override
	public String[] setListData() {
		{

			Resources res = fragment.getResources();
			String[] menu = null;
			menu = res.getStringArray(R.array.driver_myaccount);

			return menu;
		}
	}
}
