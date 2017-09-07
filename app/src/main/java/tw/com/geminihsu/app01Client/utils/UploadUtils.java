package tw.com.geminihsu.app01Client.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by geminihsu on 2016/12/18.
 */

public class UploadUtils {

    public final Context mContext;
    public UploadUtils(Context mContext) {

        this.mContext = mContext;

    }
    private void setUploadImageView(Bitmap bitmap,LinearLayout linearLayout)
    {
        ImageView image = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(layoutParams);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setPadding(0, 0, 0, 10);
        image.setAdjustViewBounds(true);
        image.setImageBitmap(bitmap);
        linearLayout.addView(image);
    }
}
