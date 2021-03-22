package com.priyanka.templateshortcuts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FloatingView extends View implements View.OnClickListener {


    private static final String TAG = "FloatingView";

    public FloatingView(@NonNull Context context) {
        super(context);
    }

    public FloatingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: you clicked me " );

    }
}
