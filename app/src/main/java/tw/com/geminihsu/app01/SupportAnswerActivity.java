package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import tw.com.geminihsu.app01.serverbean.ServerContents;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.Utility;
import tw.com.geminihsu.app01.webview.MyBrowser;

public class SupportAnswerActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private WebView browser;
    private LinearLayout linearLayout_form;
    private LinearLayout linearLayout_cancel_order;
    private LinearLayout linearLayout_sms_report;
    private LinearLayout linearLayout_driver_report;
    private LinearLayout linearLayout_app_report;
    private LinearLayout linearLayout_fee_report;
    private TextView manual;
    private TextView content;
    private Spinner spinner_reason;
    private ArrayAdapter arrayAdapter_reason;

    final public static int QUESTION = 0;

    final public static int CLAUSE = 1;
    final public static int SUGGESTION= 2;
    final public static int MANUAL= 3;
    final public static int CANCEL_FEEDBACK= 4;
    final public static int SMS_REPORT= 5;
    final public static int INSURANCE_INFO =6;
    final public static int MERCHANDISE_RESTRICT= 7;
    final public static int REPORT_DRIVER= 8;
    final public static int REPORT_APP= 9;
    final public static int ONLINE_CHECK= 10;
    final public static int REPORT_PRICE= 11;

    private int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_answer_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                choice = bundle.getInt(Constants.ARG_POSITION);
                displayLayout();
            }else
            {
                //Error!!!!
            }
        }
        else
        {
            //Error!!!!
        }
        this.setLister();




    }

    private void findViews()
    {
        browser = (WebView) findViewById(R.id.webview);
        browser.setWebViewClient(new MyBrowser("www.google.com"));
        linearLayout_form = (LinearLayout) findViewById(R.id.form);
        linearLayout_cancel_order= (LinearLayout) findViewById(R.id.order_cancel);
        linearLayout_sms_report = (LinearLayout) findViewById(R.id.order_sms);
        linearLayout_driver_report =(LinearLayout) findViewById(R.id.driver_report);
        linearLayout_app_report =(LinearLayout) findViewById(R.id.comment_report);
        linearLayout_fee_report =(LinearLayout) findViewById(R.id.app_comment_report);


        manual = (TextView) findViewById(R.id.manual);
        content = (TextView) findViewById(R.id.content);

        spinner_reason = (Spinner)findViewById(R.id.reason);

        String[] location = {"司機太慢導致無法準時送貨", "報價不合理", "司機無法溝通", "貨物數量太大無法運送", "距離太遠"};
        arrayAdapter_reason = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        spinner_reason.setAdapter(arrayAdapter_reason);

    }



    private void displayLayout() {
        if (choice == SupportAnswerActivity.SUGGESTION) {
            getActionBar().setTitle(getString(R.string.suggestion_page_title));
            linearLayout_form.setVisibility(View.VISIBLE);
        } else if (choice == SupportAnswerActivity.CLAUSE){
            getActionBar().setTitle(getString(R.string.clause_page_title));
            content.setVisibility(View.VISIBLE);
            RealmUtil data = new RealmUtil(this);
            ServerContents info = data.queryServerContent(Constants.SERVER_CONTENT_CODE,"reg01");
            ServerContents privacy = data.queryServerContent(Constants.SERVER_CONTENT_CODE,"reg02");

            content.setText("\t\t\t\t"+info.getContent()+"\n\n"+"\t\t\t\t"+privacy.getContent());
            //browser.setVisibility(View.VISIBLE);
        } else if(choice == SupportAnswerActivity.QUESTION){
            getActionBar().setTitle(getString(R.string.question_page_title));
            //browser.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            RealmUtil data = new RealmUtil(this);
            ServerContents info = data.queryServerContent(Constants.SERVER_CONTENT_CODE,"qanda");
            content.setText("\t\t\t\t"+info.getContent());

        }else if(choice == SupportAnswerActivity.INSURANCE_INFO){
            getActionBar().setTitle(getString(R.string.client_merchandise_insurance_title));
            //browser.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            RealmUtil data = new RealmUtil(this);
            ServerContents info = data.queryServerContent(Constants.SERVER_CONTENT_CODE,"safe1");
            content.setText("\t\t\t\t"+info.getContent());

        }else if(choice == SupportAnswerActivity.MERCHANDISE_RESTRICT){
            getActionBar().setTitle(getString(R.string.client_merchandise_restrict_title));
            //browser.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            RealmUtil data = new RealmUtil(this);
            ServerContents info = data.queryServerContent(Constants.SERVER_CONTENT_CODE,"lim01");
            content.setText("\t\t\t\t"+info.getContent());

        }
        else if(choice == SupportAnswerActivity.MANUAL){
            getActionBar().setTitle(getString(R.string.order_pannel_info));
            manual.setVisibility(View.VISIBLE);
            manual.setText("使用說明細節");
        } else if(choice == SupportAnswerActivity.CANCEL_FEEDBACK){
            getActionBar().setTitle(getString(R.string.menu_cancel_order));
            linearLayout_cancel_order.setVisibility(View.VISIBLE);

        }else if(choice == SupportAnswerActivity.SMS_REPORT){
            getActionBar().setTitle(getString(R.string.menu_send_sms_txt_content));
            linearLayout_sms_report.setVisibility(View.VISIBLE);

        }else if(choice == SupportAnswerActivity.REPORT_DRIVER){
            getActionBar().setTitle(getString(R.string.driver_report));
            linearLayout_driver_report.setVisibility(View.VISIBLE);

        }else if(choice == SupportAnswerActivity.REPORT_APP){
            getActionBar().setTitle(getString(R.string.app_report));
            linearLayout_app_report.setVisibility(View.VISIBLE);
            linearLayout_fee_report.setVisibility(View.VISIBLE);
        }else if(choice == SupportAnswerActivity.ONLINE_CHECK){
            getActionBar().setTitle(getString(R.string.driver_online_check));

        }else if(choice == SupportAnswerActivity.REPORT_PRICE){
            getActionBar().setTitle(getString(R.string.driver_report_price));
            manual.setVisibility(View.VISIBLE);
            manual.setText("您的報價已提供\n等待客戶審核中");
        }
    }

    private void setLister()
    {
        browser.loadUrl("http://www.tutorialspoint.com");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(choice== SupportAnswerActivity.SUGGESTION || choice== SupportAnswerActivity.SMS_REPORT ||choice== SupportAnswerActivity.ONLINE_CHECK){
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.btn_send));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);

            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }else if(choice== SupportAnswerActivity.CANCEL_FEEDBACK ||choice== SupportAnswerActivity.REPORT_PRICE){
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_sure_cancel_order));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                if(choice== SupportAnswerActivity.SUGGESTION) {
                    Intent question = new Intent(SupportAnswerActivity.this, MerchandiseOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.ARG_POSITION, Constants.CONTROL_PANNEL_MANUAL);
                    question.putExtras(b);
                    startActivity(question);
                }else if(choice== SupportAnswerActivity.SMS_REPORT || choice== SupportAnswerActivity.CANCEL_FEEDBACK)
                {
                    Intent question = new Intent(SupportAnswerActivity.this, MenuMainActivity.class);
                    question.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(question);
                }else if(choice== SupportAnswerActivity.REPORT_PRICE)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    // set title
                    alertDialogBuilder.setTitle(getString(R.string.dialog_cancel_report));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(getString(R.string.dialog_cancel_report_msg))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.dialog_btn_cancel_report),new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    Intent question = new Intent(SupportAnswerActivity.this, MenuMainActivity.class);
                                    question.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(question);

                                }
                            })
                            .setNegativeButton(getString(R.string.dialog_btn_give_up_report),new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
