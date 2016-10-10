package tw.com.geminihsu.app01.tw.com.geminihsu.app01.delegate;


import tw.com.geminihsu.app01.fragment.Fragment_Account;

import android.view.View;
import android.widget.AdapterView;

public class Fragment_AccountDelegateBase {
	protected Fragment_Account fragment;

	public Fragment_AccountDelegateBase(Fragment_Account _fragment_FileExplorer) {
		fragment = _fragment_FileExplorer;
	}

	public void listViewOnItemClickListener(AdapterView<?> parent, View view, int position, long id) {


	}
}
