package tw.com.geminihsu.app01Client.delegate;


import tw.com.geminihsu.app01Client.fragment.Fragment_Account;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public class Fragment_AccountDelegateBase {
	protected Fragment_Account fragment;
	private ListAdapter adapter;

	public Fragment_AccountDelegateBase(Fragment_Account _fragment_FileExplorer) {
		fragment = _fragment_FileExplorer;
	}

	public void listViewOnItemClickListener(AdapterView<?> parent, View view, int position, long id) {


	}

	public String[] setListData(){
		String[] menu = null;
		return menu;
	}
}
