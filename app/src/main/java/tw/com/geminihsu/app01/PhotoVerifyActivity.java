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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import tw.com.geminihsu.app01.common.Constants;

public class PhotoVerifyActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    private ImageButton btn_car_work_image;
    private ImageButton btn_car_driver_id;
    private ImageButton btn_car_driver_licence;
    private ImageButton btn_car_work_licence_image;
    private ImageButton btn_car_image;

    private ImageView car_work_image;
    private ImageView car_driver_id;
    private ImageView car_driver_licence;
    private ImageView car_work_licence_image;
    private ImageView car_image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_verify_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();

        this.setLister();




    }

    private void findViews()
    {
        btn_car_work_image = (ImageButton) findViewById(R.id.btn_car_work_image);
        btn_car_driver_id = (ImageButton) findViewById(R.id.btn_car_driver_id);
        btn_car_driver_licence = (ImageButton) findViewById(R.id.btn_car_driver_licence);
        btn_car_work_licence_image = (ImageButton) findViewById(R.id.btn_car_work_licence_image);
        btn_car_image = (ImageButton) findViewById(R.id.btn_car_image);

        car_work_image = (ImageView) findViewById(R.id.car_work_image);
        car_driver_id = (ImageView) findViewById(R.id.car_driver_id);
        car_driver_licence = (ImageView) findViewById(R.id.car_driver_licence);
        car_work_licence_image = (ImageView) findViewById(R.id.car_work_licence_image);
        car_image = (ImageView) findViewById(R.id.car_image);



    }




    private void setLister()
    {
        btn_car_work_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(PhotoVerifyActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        PhotoVerifyActivity.this,
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pick_picture_camera));
                arrayAdapter.add(getString(R.string.pick_picture_gallery));
                //arrayAdapter.add("YouTube");

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        //Intent intent = new Intent(PhotoVerifyActivity.this, CameraActivity.class);
                                        //startActivityForResult(intent, CAMERA);
                                        break;
                                    case 1:
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                       // startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);
                                        break;
                                 /*   case 2:
                                        Intent question = new Intent(NewPostActivity.this, YoutubeActivity.class);
                                        startActivity(question);
                                        break;*/
                                }
                            }
                        });
                builderSingle.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.btn_finish));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:
                Constants.Driver = true;
                Intent question = new Intent(PhotoVerifyActivity.this, MenuMainActivity.class);
                question.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(question);

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
