package com.priyanka.templateshortcuts;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WidgetService extends Service implements View.OnClickListener {

    int LAYOUT_FLAG;
    View mFloatingView;
    WindowManager windowManager;
    ImageView closeImage;
    Button tvWidget;
    float height,width;
    String TAG="WidgetService";
    private View collapsedView;
    private View expandedView;
    ListView listView;
    List<String> items;

    public WidgetService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_widget,null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mFloatingView, params);

        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);
        listView=mFloatingView.findViewById(R.id.listview);

        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        items=new ArrayList<>();
        items.add("this is test");
        items.add("this is texts3");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });


    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }else {
//            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
//        }
//
//        //inflate
//        mFloatingView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_widget,null);
//
//        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,
//                LAYOUT_FLAG,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        //initial Position
//        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
//        layoutParams.x=0;
//        layoutParams.y=100;
//
//
//        //layout params for close
//        WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140,140,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT);
//
//        imageParams.gravity = Gravity.BOTTOM|Gravity.CENTER;
//        imageParams.y=100;
//
//        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
//        closeImage= new ImageView(this);
//        closeImage.setImageResource(R.drawable.close_white);
//        closeImage.setVisibility(View.INVISIBLE);
//        windowManager.addView(closeImage,imageParams);
//        windowManager.addView(mFloatingView,layoutParams);
//        mFloatingView.setVisibility(View.VISIBLE);
//
//        height = windowManager.getDefaultDisplay().getHeight();
//        width = windowManager.getDefaultDisplay().getWidth();
//
//        tvWidget = mFloatingView.findViewById(R.id.text_widget);
////        tvWidget.("Templte");
//
//        tvWidget.setOnTouchListener(new View.OnTouchListener() {
//            int initialx,initialy;
//            float intialTochx,intialTouchy;
//            long startClick;
//            final int MAX_CLICK_DURATION=200;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int X = (int) event.getRawX();
//                final int Y = (int) event.getRawY();
//                switch (event.getAction()){
//
////                    case MotionEvent.ACTION_DOWN:
////
//////                        startClick = Calendar.getInstance().getTimeInMillis();
//////                        closeImage.setVisibility(View.VISIBLE);
//////
//////                        initialx=layoutParams.x;
//////                        initialy=layoutParams.y;
//////
//////                        intialTochx= event.getRawX();
//////                        intialTouchy=event.getY();
////
////                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
////                        _xDelta = X - lParams.leftMargin;
////                        _yDelta = Y - lParams.topMargin;
////                        return true;
//
//                    /*case MotionEvent.ACTION_UP:
//
//                        long clickduration = Calendar.getInstance().getTimeInMillis()-startClick;
//                        closeImage.setVisibility(View.GONE);
//
//                        layoutParams.x=initialx+(int)(intialTochx-event.getRawX());
//                        layoutParams.y=initialy+(int)(event.getRawY()-intialTouchy);
//
//                        if (clickduration<MAX_CLICK_DURATION){
//                            Toast.makeText(WidgetService.this,"Time",Toast.LENGTH_SHORT);
//                        }else {
//                            if (layoutParams.y>(height*0.6)){
//                                stopSelf();
//                            }
//                        }
//                        return true;*/
//                    case MotionEvent.ACTION_MOVE:
//
////                        layoutParams.x=initialx+(int)(intialTochx-event.getRawX());
////                        layoutParams.y=initialy + (int)(event.getRawY()-intialTouchy);
////
////                        windowManager.updateViewLayout(mFloatingView,layoutParams);
////
////                        if (layoutParams.y>(height*0.6)){
////                            closeImage.setImageResource(R.drawable.ic_baseline_close_24);
////                        }else {
////                            closeImage.setImageResource(R.drawable.close_white);
////                        }
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
//                        layoutParams.leftMargin = X - _xDelta;
//                        layoutParams.topMargin = Y - _yDelta;
//                        layoutParams.rightMargin = -250;
//                        layoutParams.bottomMargin = -250;
//                        v.setLayoutParams(layoutParams);
//                        return true;
//                }
//                return false;
//            }
//        });
//
////        tvWidget.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.e(TAG, "onClick: you Clicked ME!(: " );
////            }
////        });
//
//        return START_STICKY;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView!=null){
            windowManager.removeView(mFloatingView);
        }

//        if (closeImage!=null){
//            windowManager.removeView(closeImage);
//        }
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: you clicked" );
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
                Log.e(TAG, "onClick: layoutExpanded" );
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.buttonClose:
                //closing the widget
                Log.e(TAG, "onClick: buttonClose" );
                stopSelf();
                break;
        }
    }
}
