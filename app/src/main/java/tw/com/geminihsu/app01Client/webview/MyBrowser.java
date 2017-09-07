package tw.com.geminihsu.app01Client.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by geminihsu on 16/10/3.
 */
public class MyBrowser extends WebViewClient {
    String url="";
    public MyBrowser(String _url){
        url=_url;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
