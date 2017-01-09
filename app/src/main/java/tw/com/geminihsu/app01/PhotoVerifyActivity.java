package tw.com.geminihsu.app01;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.IOException;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.DriverIdentifyInfo;
import tw.com.geminihsu.app01.bean.ImageBean;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.ImageUtils;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.URICovertStringPathUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil.ServerRequestDataManagerCallBackFunction;
import tw.com.geminihsu.app01.utils.JsonPutsUtil.DriverRegisterUploadPhotoManagerCallBackFunction;
import tw.com.geminihsu.app01.utils.Utility;

public class PhotoVerifyActivity extends Activity implements Response.ErrorListener,Response.Listener{
    private String TAG = PhotoVerifyActivity.class.toString();
    private static final String[] INITIAL_PERMS={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    //上傳圖片類型 utype
    private static final String PHOTO_TYPE_ID="a1";
    private static final String PHOTO_TYPE_HEALTH="a2";
    private static final String PHOTO_TYPE_DRIVER="a3";
    private static final String PHOTO_TYPE_CAR="a4";
    private static final String PHOTO_TYPE_LICENSE="a5";
    private static final String PHOTO_TYPE_CAR_BODY="a6";
    private static final String PHOTO_TYPE_TAXI_INSURANCE="a7";


    private static final int INITIAL_REQUEST=1337;
    private JsonPutsUtil sendDataRequest;

    private static final int CAMERA=1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private static final int ID_CAMERA=3;
    private static final int PICK_ID_IMAGE_REQUEST = 4;

    private static final int LICENCE_CAMERA=5;
    private static final int PICK_LICENCE_IMAGE_REQUEST = 6;

    private static final int WORK_LICENCE_CAMERA=7;
    private static final int PICK_WORK_LICENCE_IMAGE_REQUEST = 8;

    private static final int CAR_CAMERA=9;
    private static final int PICK_CAR_IMAGE_REQUEST = 10;

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
    private ImageView test_image;

    private String imageContentURI;

    private int scaleWidth;
    private int scaleHeight;

    private DriverIdentifyInfo driverInfo;


    private BroadcastReceiver getRegisterDriverBroadcastReceiver;
    private JsonPutsUtil post_image;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_verify_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        post_image = new JsonPutsUtil(PhotoVerifyActivity.this);
        sendDataRequest = new JsonPutsUtil(PhotoVerifyActivity.this);
        sendDataRequest.setServerRequestDataManagerCallBackFunction(new ServerRequestDataManagerCallBackFunction() {
            @Override
            public void registerDriver(DriverIdentifyInfo driverIdentifyInfo) {

                Log.e(TAG, "get request from server");
                if (!driverIdentifyInfo.getDid().equals("")) {
                    if(dialog!=null)
                    {
                        dialog.dismiss();
                        dialog = null;
                    }
                    notifyMessage();
                    // JsonPutsUtil changeStatus = new JsonPutsUtil(PhotoVerifyActivity.this);
                    // changeStatus.driverWorkIdentity(driverIdentifyInfo);
                    //Utility user = new Utility(PhotoVerifyActivity.this);
                    //AccountInfo driver = user.getAccountInfo();
                    //driver.setRole(1);
                    //database.updateAccount(driver);

                }
            }

        });
        post_image.setDriverRegisterUploadPhotoManagerCallBackFunction(new DriverRegisterUploadPhotoManagerCallBackFunction() {


            @Override
            public void uploadStatusSuccess(ImageBean photo) {
                if(dialog!=null)
                {
                    dialog.dismiss();
                    dialog = null;
                }
                if(photo.getUploadtype().equals(PHOTO_TYPE_ID))
                    btn_car_work_image.setImageResource(R.drawable.ic_file_cloud_done);
                else  if(photo.getUploadtype().equals(PHOTO_TYPE_DRIVER))
                    btn_car_driver_id.setImageResource(R.drawable.ic_file_cloud_done);
                else  if(photo.getUploadtype().equals(PHOTO_TYPE_CAR))
                    btn_car_driver_licence.setImageResource(R.drawable.ic_file_cloud_done);
                else  if(photo.getUploadtype().equals(PHOTO_TYPE_LICENSE))
                    btn_car_work_licence_image.setImageResource(R.drawable.ic_file_cloud_done);
                else  if(photo.getUploadtype().equals(PHOTO_TYPE_CAR_BODY))
                    btn_car_image.setImageResource(R.drawable.ic_file_cloud_done);


            }

            @Override
            public void uploadFail(boolean error) {
                if(dialog!=null)
                {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle args = getIntent().getExtras();
        if(args!=null)
         driverInfo = (DriverIdentifyInfo) args.getSerializable(DriverLoginActivity.BUNDLE_DRIVER_ACCOUNT_INFO);

        this.setLister();
        if(getRegisterDriverBroadcastReceiver!=null)
            registerReceiver((getRegisterDriverBroadcastReceiver), new IntentFilter("register_driver"));




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
        test_image = (ImageView) findViewById(R.id.test_image);


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
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        if(checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.CAMERA)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent intent = new Intent(PhotoVerifyActivity.this, CameraActivity.class);
                                        startActivityForResult(intent, CAMERA);
                                        break;
                                    case 1:
                                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {

                                        // Should we show an explanation?
                                        if (shouldShowRequestPermissionRationale(
                                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                            // Explain to the user why we need to read the contacts
                                        }

                                        requestPermissions(INITIAL_PERMS,
                                                INITIAL_REQUEST);

                                        // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                        // app-defined int constant
                                    }
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);
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

        btn_car_driver_id.setOnClickListener(new View.OnClickListener() {

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
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        if(checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.CAMERA)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent intent = new Intent(PhotoVerifyActivity.this, CameraActivity.class);
                                        startActivityForResult(intent, ID_CAMERA);
                                        break;
                                    case 1:
                                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_ID_IMAGE_REQUEST);
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
        btn_car_driver_licence.setOnClickListener(new View.OnClickListener() {

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
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        if(checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.CAMERA)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent intent = new Intent(PhotoVerifyActivity.this, CameraActivity.class);
                                        startActivityForResult(intent, LICENCE_CAMERA);
                                        break;
                                    case 1:
                                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_LICENCE_IMAGE_REQUEST);
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
        btn_car_work_licence_image.setOnClickListener(new View.OnClickListener() {

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
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        if(checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.CAMERA)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent intent = new Intent(PhotoVerifyActivity.this, CameraActivity.class);
                                        startActivityForResult(intent, WORK_LICENCE_CAMERA);
                                        break;
                                    case 1:
                                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_WORK_LICENCE_IMAGE_REQUEST);
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
        btn_car_image.setOnClickListener(new View.OnClickListener() {

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
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        if(checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.CAMERA)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        car_work_image.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(PhotoVerifyActivity.this, CameraActivity.class);
                                        startActivityForResult(intent, CAR_CAMERA);
                                        break;
                                    case 1:
                                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            // Should we show an explanation?
                                            if (shouldShowRequestPermissionRationale(
                                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                // Explain to the user why we need to read the contacts
                                            }

                                            requestPermissions(INITIAL_PERMS,
                                                    INITIAL_REQUEST);

                                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                                            // app-defined int constant
                                        }
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_CAR_IMAGE_REQUEST);
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
    //Get the size of the Image view after the
    //Activity has completely loaded
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        scaleWidth=car_driver_id.getWidth();
        scaleHeight=car_work_image.getHeight();
    }


    @Override
    public void onDestroy() {

        if (getRegisterDriverBroadcastReceiver != null&&getRegisterDriverBroadcastReceiver.isOrderedBroadcast()){
            unregisterReceiver(getRegisterDriverBroadcastReceiver);
            getRegisterDriverBroadcastReceiver=null;
        }

        super.onDestroy();
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
                /*Intent question = new Intent(PhotoVerifyActivity.this, MenuMainActivity.class);
                question.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(question);*/
                RealmUtil database = new RealmUtil(PhotoVerifyActivity.this);
                RealmResults<ImageBean> file =database.queryImage(Constants.ACCOUNT_USERNAME, driverInfo.getName());
                String car_imgs = "";
                for(ImageBean image : file)
                {
                    if(image.getUploadtype().equals(PHOTO_TYPE_CAR_BODY))
                        driverInfo.setCar_imgs(image.getFile_id());
                    else {
                        car_imgs+= image.getFile_id()+",";
                       // driverInfo.setCar_files(car_imgs);
                    }
                    Log.e(TAG,"image id:"+image.getFile_id());
                }
                int index=car_imgs.lastIndexOf(",");
                String tmp = car_imgs.substring(0,index);
                car_imgs = tmp;
                driverInfo.setCar_files(car_imgs);
                //driverInfo.setCar_files("61,62,63,64");
                Log.e(TAG,"driverInfo info:"+driverInfo.getDtype());
                //JsonPutsUtil sendRegister = new JsonPutsUtil(PhotoVerifyActivity.this);
                if(dialog == null)
                    dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                            "Loading. Please wait...", true);

                sendDataRequest.registerDriverAccount(driverInfo);
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA:
                String path = data.getStringExtra("image");
                File imgFile = new File(path);

                if (imgFile.exists()) {
                    Bitmap myBitmap = ImageUtils.decodeSampledBitmapFromResource(path, scaleWidth, scaleHeight);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    imageContentURI = path;
                    car_work_image.setImageBitmap(myBitmap);
                }

                if(dialog == null)
                    dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                            "Loading. Please wait...", true);

                //File img = new File(realPath);
                post_image.postImageToServer(driverInfo,car_work_image,PHOTO_TYPE_ID);

                break;
            case PICK_IMAGE_REQUEST:
                String realPath;
                Log.e(TAG, data.toString());
                Uri uri = data.getData();

                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = URICovertStringPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = URICovertStringPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath = URICovertStringPathUtil.getRealPathFromURI_API19(this, uri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    bitmap = ImageUtils.decodeSampledBitmapFromResource(realPath, 300, 400);
                    // Log.d(TAG, String.valueOf(bitmap));

                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);

                    imageContentURI = realPath;
                    car_work_image.setImageBitmap(bitmap);
                    //car_work_image.setVisibility(View.VISIBLE);
                    //car_work_image.setImageResource(R.drawable.sunshine);
                   // JsonPutsUtil post_image = new JsonPutsUtil(PhotoVerifyActivity.this);
                    //File img = new File(realPath);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_work_image,PHOTO_TYPE_ID);
                    //Log.e(TAG,"USER:"+driverInfo.getAccesskey());
                    // post_image.RequestMultiPart(this,this,img);
                    //post_image.postImageToServer2(driverInfo,bitmap,PHOTO_TYPE_ID);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ID_CAMERA:
                String path1 = data.getStringExtra("image");
                File imgFile1 = new File(path1);
                if (imgFile1.exists()) {
                    Bitmap myBitmap = ImageUtils.decodeSampledBitmapFromResource(path1, scaleWidth, scaleHeight);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    imageContentURI = path1;
                    car_driver_id.setImageBitmap(myBitmap);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_driver_id,PHOTO_TYPE_DRIVER);

                }
                break;
            case PICK_ID_IMAGE_REQUEST:
                String realPath1;
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath1 = URICovertStringPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath1 = URICovertStringPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath1 = URICovertStringPathUtil.getRealPathFromURI_API19(this, data.getData());

                Uri uri1 = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                    bitmap = ImageUtils.decodeSampledBitmapFromResource(realPath1, 300, 400);
                    // Log.d(TAG, String.valueOf(bitmap));

                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Log.e(TAG, uri1.toString());
                    imageContentURI = realPath1;
                    car_driver_id.setImageBitmap(bitmap);
                    //car_driver_id.setImageResource(R.drawable.ic_camera_72x72);
                    //car_driver_id.setVisibility(View.VISIBLE);
                    //JsonPutsUtil post_image = new JsonPutsUtil(PhotoVerifyActivity.this);
                    //File img = new File(realPath);
                    //post_image.postImageToServer(bitmap,driverInfo);
                    // post_image.RequestMultiPart(this,this,img);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_driver_id,PHOTO_TYPE_DRIVER);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LICENCE_CAMERA:
                String path2 = data.getStringExtra("image");
                File imgFile2 = new File(path2);
                if (imgFile2.exists()) {
                    Bitmap myBitmap = ImageUtils.decodeSampledBitmapFromResource(path2, scaleWidth, scaleHeight);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    imageContentURI = path2;
                    car_driver_licence.setImageBitmap(myBitmap);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_driver_licence,PHOTO_TYPE_CAR);

                }
                break;
            case PICK_LICENCE_IMAGE_REQUEST:
                String realPath2;
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath2 = URICovertStringPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath2 = URICovertStringPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath2 = URICovertStringPathUtil.getRealPathFromURI_API19(this, data.getData());

                Uri uri2 = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                    bitmap = ImageUtils.decodeSampledBitmapFromResource(realPath2, 300, 400);
                    // Log.d(TAG, String.valueOf(bitmap));

                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Log.e(TAG, uri2.toString());
                    imageContentURI = realPath2;
                    car_driver_licence.setImageBitmap(bitmap);
                    //car_driver_licence.setImageResource(R.drawable.ic_camera_72x72);
                    //car_driver_licence.setVisibility(View.VISIBLE);
                    //JsonPutsUtil post_image = new JsonPutsUtil(PhotoVerifyActivity.this);
                    //File img = new File(realPath);
                    //post_image.postImageToServer(bitmap,driverInfo);
                    // post_image.RequestMultiPart(this,this,img);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_driver_licence,PHOTO_TYPE_CAR);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case WORK_LICENCE_CAMERA:
                String path3 = data.getStringExtra("image");
                File imgFile3 = new File(path3);
                if (imgFile3.exists()) {
                    Bitmap myBitmap = ImageUtils.decodeSampledBitmapFromResource(path3, scaleWidth, scaleHeight);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    imageContentURI = path3;
                    car_work_licence_image.setImageBitmap(myBitmap);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_work_licence_image,PHOTO_TYPE_LICENSE);

                }
                break;
            case PICK_WORK_LICENCE_IMAGE_REQUEST:
                String realPath3;
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath3 = URICovertStringPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath3 = URICovertStringPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath3 = URICovertStringPathUtil.getRealPathFromURI_API19(this, data.getData());

                Uri uri3 = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
                    bitmap = ImageUtils.decodeSampledBitmapFromResource(realPath3, 300, 400);
                    // Log.d(TAG, String.valueOf(bitmap));

                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Log.e(TAG, uri3.toString());
                    imageContentURI = realPath3;
                    car_work_licence_image.setImageBitmap(bitmap);
                    //car_work_licence_image.setImageResource(R.drawable.ic_camera_72x72);
                    //car_work_licence_image.setVisibility(View.VISIBLE);
                    //JsonPutsUtil post_image = new JsonPutsUtil(PhotoVerifyActivity.this);
                    //File img = new File(realPath);
                   // post_image.postImageToServer(bitmap,driverInfo);
                    // post_image.RequestMultiPart(this,this,img);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_work_licence_image,PHOTO_TYPE_LICENSE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case CAR_CAMERA:
                String path4 = data.getStringExtra("image");
                File imgFile4 = new File(path4);
                if (imgFile4.exists()) {
                    Bitmap myBitmap = ImageUtils.decodeSampledBitmapFromResource(path4, scaleWidth, scaleHeight);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    imageContentURI = path4;
                    car_image.setImageBitmap(myBitmap);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_image,PHOTO_TYPE_CAR_BODY);

                }
                break;
            case PICK_CAR_IMAGE_REQUEST:
                String realPath4;
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath4 = URICovertStringPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath4 = URICovertStringPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath4 = URICovertStringPathUtil.getRealPathFromURI_API19(this, data.getData());

                Uri uri4 = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri4);
                    bitmap = ImageUtils.decodeSampledBitmapFromResource(realPath4, 300, 400);
                    // Log.d(TAG, String.valueOf(bitmap));

                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Log.e(TAG, uri4.toString());
                    imageContentURI = realPath4;
                    //car_image.setVisibility(View.VISIBLE);
                    //car_image.setImageResource(R.drawable.ic_camera_72x72);
                    car_image.setImageBitmap(bitmap);

                    //JsonPutsUtil post_image = new JsonPutsUtil(PhotoVerifyActivity.this);
                    //File img = new File(realPath);
                    //post_image.postImageToServer(bitmap,driverInfo);
                    // post_image.RequestMultiPart(this,this,img);
                    if(dialog == null)
                        dialog = ProgressDialog.show(PhotoVerifyActivity.this, "",
                                "Loading. Please wait...", true);

                    post_image.postImageToServer(driverInfo,car_image,"a6");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("",error.toString());
    }

    @Override
    public void onResponse(Object response) {
        Log.e("",response.toString());
    }

    private void notifyMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.finish_register_title));


        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.finish_register))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                        //Bundle b = new Bundle();
                        //b.putString(BUNDLE_ACCESS_KEY, accesskey);
                        //intent.putExtras(b);
                        startActivity(intent);
                        //finish();
                    }
                });
    }
}
