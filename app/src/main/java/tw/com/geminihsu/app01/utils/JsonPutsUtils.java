package tw.com.geminihsu.app01.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.geminihsu.app01.bean.AccountInfo;

/**
 * Created by geminihsu on 2016/11/20.
 */

public class JsonPutsUtils {
    private final Context mContext;
    private final String TAG = JsonPutsUtils.class.toString();
    private RequestQueue requestQueue;


    public JsonPutsUtils(Context mContext) {
        this.mContext = mContext;
        this.requestQueue = Volley.newRequestQueue(mContext);

    }

    public void registerAccount(AccountInfo user){

        JSONObject obj = new JSONObject();

        try {
            obj.put("method", "m_reg_form");
            obj.put("username", user.getPhoneNumber());
            obj.put("password", user.getPassword());
            obj.put("idcard", user.getIdentify());
            obj.put("realname", "哈哈");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://app01.cumi.co/api",obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG,jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
