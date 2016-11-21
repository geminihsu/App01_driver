package tw.com.geminihsu.app01.delegate.customer;


import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;

import tw.com.geminihsu.app01.ChangePasswordActivity;
import tw.com.geminihsu.app01.CommentActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.RecommendActivity;
import tw.com.geminihsu.app01.fragment.Fragment_Account;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.Fragment_AccountDelegateBase;

public class Fragment_AccountDelegateCustomer extends Fragment_AccountDelegateBase {
	protected Fragment_Account fragment;

	public Fragment_AccountDelegateCustomer(Fragment_Account _fagment_Account) {
		super(_fagment_Account);
		fragment=_fagment_Account;
	}

	@Override
	public void listViewOnItemClickListener(AdapterView<?> parent, View view, int position, long id) {

		if(position == 0) {
			Intent question = new Intent(fragment.getActivity(), ChangePasswordActivity.class);
			fragment.startActivity(question);
		}else  if(position == 1) {
			Intent question = new Intent(fragment.getActivity(), CommentActivity.class);
			fragment.startActivity(question);
		}else  if(position == 2) {
			Intent question = new Intent(fragment.getActivity(), RecommendActivity.class);
			fragment.startActivity(question);
		}
	}

	@Override
	public String[] setListData() {
		{

			Resources res = fragment.getResources();
			String[] menu = null;
			menu = res.getStringArray(R.array.myaccount);

			return menu;
		}
	}
}
