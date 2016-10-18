package tw.com.geminihsu.app01.tw.com.geminihsu.app01.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by geminihsu on 16/10/17.
 */
public class DrawView extends View {

    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
                 // 创建画笔
        Paint p = new Paint();
        p.setColor(Color.RED);// 设置红色

        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        canvas.drawCircle(120, 100, 100, p);// 大圆

    }
}