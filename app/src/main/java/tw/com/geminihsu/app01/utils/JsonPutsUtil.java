package tw.com.geminihsu.app01.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.App01libObjectKey;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.common.Constants;

/**
 * Created by geminihsu on 2016/11/20.
 */

public class JsonPutsUtil {
    private final Context mContext;
    private final String TAG = JsonPutsUtil.class.toString();
    private RequestQueue requestQueue;


    public JsonPutsUtil(Context mContext) {
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



    public void getPushNotification(AccountInfo user) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_GET_PUSH_NOTIFICATION);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_LOGIN_USERNAME, user.getPhoneNumber());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_ACCESSKEY, user.getAccessKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getPushImage(final Bitmap bitmap, final DriverIdentifyInfo info) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JSONObject obj = new JSONObject();

        try {
            obj.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_IMAGE);
            obj.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_USERNAME, info.getName());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_ACCESSKEY, info.getAccesskey());
            obj.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_TYPE, "a1");
            obj.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_IMAGE_DATA, bitmap);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, jsonObject.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_ACCOUNT_INFO_STATUS);
                    App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE connectResult = App01libObjectKey.conversion_get_put_notification_result(Integer.valueOf(status));

                    if (connectResult.equals(App01libObjectKey.APP_GET_PUSH_RESPONSE_CODE.K_APP_GET_PUSH_CODE_SUCCESS)) {
                        Log.e(TAG, "get push!!!!");
                    } else {
                        String message = jsonObject.getString(App01libObjectKey.APP_OBJECT_KEY_DEVICE_INFO_MESSAGE);

                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //取得圖檔的id
    public void postImageToServer(final Bitmap bitmap, final DriverIdentifyInfo info) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String url =Constants.SERVER_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, s.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, volleyError.getMessage().toString());

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = "test";

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD, App01libObjectKey.APP_OBJECT_KEY_PUTS_METHOD_UPLOAD_IMAGE);
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_USERNAME, info.getName());
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_ACCESSKEY, info.getAccesskey());
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_IMAGE_DATA, image);
                params.put(App01libObjectKey.APP_OBJECT_KEY_IMAGE_UPLOAD_TYPE, "a1");



                //returning parameters
                return params;
            }

            public String getStringImage(Bitmap bmp){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return encodedImage;
            }
        };

          //Adding request to the queue
        requestQueue.add(stringRequest);
    }

   /* public void RequestMultiPart(Response.ErrorListener errorListener, Response.Listener<String> listener, File imageFile ) {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        PhotoMultipartRequest imageUploadReq = new PhotoMultipartRequest(Constants.SERVER_URL, errorListener, listener, imageFile);
        mQueue.add(imageUploadReq);
    }*/


}
