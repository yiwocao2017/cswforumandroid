package com.zhejiangshegndian.csw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by LeiQ on 2017/3/9.
 */

public class SideBar extends View {

    private TextView textDialog;
    private OnSelectChangeListener changeListener;

    // 26个字母
    public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z", "#" };
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    public void setTextView(TextView textView) {
        this.textDialog = textView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.parseColor("#464646"));
//            paint.setColor(Color.rgb(33, 65, 98));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            // 选中状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#d23e3e"));
                paint.setFakeBoldText(true);
            }
            float x = width / 2 - paint.measureText(b[i]) / 2;
            float y = singleHeight * i + singleHeight;
            canvas.drawText(b[i], x, y, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        int c = (int) (y / getHeight() * b.length);
        switch (action) {
            case MotionEvent.ACTION_UP:
//                setBackgroundResource(R.color.alph);
                choose = -1;
                invalidate();
                if (textDialog != null)
                    textDialog.setVisibility(View.INVISIBLE);
                break;

            default:
                setBackgroundColor(Color.parseColor("#ffffff"));
                if (choose != c) {
                    if (c >= 0 && c < b.length) {
                        if (changeListener != null)
                            changeListener.onSelectChange(b[c]);
                        if (textDialog != null) {
                            textDialog.setText(b[c]);
                            textDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnSelectChangeListener(OnSelectChangeListener listener) {
        this.changeListener = listener;
    }

    public interface OnSelectChangeListener {
        void onSelectChange(String letter);
    }
}