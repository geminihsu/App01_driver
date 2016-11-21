package tw.com.geminihsu.app01.utils;

import android.content.Context;
import android.content.SharedPreferences;

import tw.com.geminihsu.app01.R;

public class ConfigSharedPreferencesUtil {
	private final static String TAG = ConfigSharedPreferencesUtil.class.getSimpleName();




	public static String getUserName(Context c, SharedPreferences configSharedPreferences) {
		// SharedPreferences configSharedPreferences =
		// PreferenceManager.getDefaultSharedPreferences(c);
		String preference_Username = configSharedPreferences.getString(c.getString(R.string.config_login_phone_number_key), "");

		return preference_Username;
	}

	public static String getPassword(Context c, SharedPreferences configSharedPreferences) {
		// SharedPreferences configSharedPreferences =
		// PreferenceManager.getDefaultSharedPreferences(c);
		String preference_Password = configSharedPreferences.getString(c.getString(R.string.config_login_password_key), "");

		return preference_Password;
	}

}
