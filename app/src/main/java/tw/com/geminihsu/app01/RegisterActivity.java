package tw.com.geminihsu.app01;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class RegisterActivity extends Activity {

    private TextView clause;
    private Button verify;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        setLister();


    }

    private void findViews()
    {
        clause = (TextView) findViewById(R.id.agree);
        verify = (Button) findViewById(R.id.send_code);
        verify.setEnabled(false);
        agree  = (CheckBox) findViewById(R.id.txt_forget_password);
    }


    private void setLister()
    {
        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    verify.setEnabled(true);
                else
                    verify.setEnabled(false);
            }
        }
    );
        clause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(RegisterActivity.this, SupportAnswerActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, SupportAnswerActivity.CLAUSE);
                question.putExtras(b);
                startActivity(question);
            }
        });

       verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(RegisterActivity.this, VerifyCodeActivity.class);
                startActivity(question);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
