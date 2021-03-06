package com.example.simpltestapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.example.simpltestapp.R;
import com.example.simpltestapp.utils.CustomFontCache;

import androidx.appcompat.widget.AppCompatButton;

public class SimplButton extends AppCompatButton {

    private static final String TAG = "SimplButton";

    public SimplButton(Context context) {
        super(context);
    }

    public SimplButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public SimplButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.com_example_simpletestapp_view_SimplTextView);
        try {
            String customFont = a.getString(R.styleable.com_example_simpletestapp_view_SimplTextView_customFontTypeFace);
            Typeface tf = CustomFontCache.lookForTypeface(ctx, customFont);
            if (tf != null) {
                setTypeface(tf);
            }
        } catch (Exception e) {
            Log.e(TAG, "unable to set custom font");
        } finally {
            a.recycle();
        }
    }


}
