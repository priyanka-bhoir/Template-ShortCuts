package com.priyanka.templateshortcuts;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.priyanka.templateshortcuts.Constant.chatBoxRefId;
import static com.priyanka.templateshortcuts.Constant.sendButtonRefId;

public class WidgetService extends AccessibilityService implements View.OnClickListener {

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
    private final static int NONE = 0;
    private final static int DRAG = 1;
    private int m_mode = NONE;

    static String targetName=null;
    static int convIndex = 0;


    public WidgetService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.e(TAG, "onAccessibilityEvent: called");
        if (event == null){
            return;
        }
        AccessibilityNodeInfo rootNode = event.getSource();
        if (rootNode == null){
            return;
        }
        try {
            AccessibilityNodeInfo textBox = getNode(rootNode, chatBoxRefId);
            Bundle arguments = new Bundle();
            arguments.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,targetName);
            textBox.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT, arguments);
            AccessibilityNodeInfo sendButton = getNode(rootNode, sendButtonRefId);

            if (!textBox.getText().toString().isEmpty()){
                Log.e(TAG, "onAccessibilityEvent: i don't know why you are getting called" );
                sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "onAccessibilityEvent: Exception=:  " + e );
        }
    }

    private AccessibilityNodeInfo getNode(AccessibilityNodeInfo rootNode, String refId){
        AccessibilityNodeInfo textBoxNode = null;
        List<AccessibilityNodeInfo> urlNodeInfo = rootNode.findAccessibilityNodeInfosByViewId(refId);
        if (urlNodeInfo != null && !urlNodeInfo.isEmpty()){
            textBoxNode =urlNodeInfo.get(0);
            Log.e(TAG, "getNode: "+ textBoxNode );
            return textBoxNode;
        }
        return textBoxNode;
    }

    private String getName(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> urlNodeInfo = rootNode.findAccessibilityNodeInfosByViewId(Constant.nameRefId);

        if (urlNodeInfo != null && !urlNodeInfo.isEmpty()){
            AccessibilityNodeInfo  urlNode = urlNodeInfo.get(0);
            CharSequence charArray = urlNode.getText();
            if (charArray != null && charArray.length()>0){
                Log.e(TAG, "getName: "+charArray.toString());
                return charArray.toString();
            }
        }
        Log.e(TAG, "getName: Name not found");
        return null;
    }

    @Override
    public void onInterrupt() {

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

//        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        items=new ArrayList<>();
        items.add("this is test");
        items.add("this is texts3");
        items.add("Gesture recognition and handling touch events is an important part of developing user interactions. Handling standard events such as clicks, long clicks, key presses, etc are very basic and handled in other guides");
        items.add("\n" +
                "1\n" +
                "\n" +
                "put the button on overlay layer.then set that button android:background=\"@null\" it block touch event of view below it..hope it solve your problem\n");


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        mFloatingView.findViewById(R.id.relativeLayoutParent);
        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        m_mode = NONE;

                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        if(m_mode == DRAG){
                            Log.e(TAG, "onTouch: this is the drag mode==:) ACTION_UP");
                            // code for attaching of bubble on the left side of screen

                            break;
                        }
                        layoutCollapsed();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers

                        m_mode = DRAG;

                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick: "+items.get(position) );

                setName(items.get(position));

                PackageManager packageManager = getPackageManager();
                Log.e(TAG, "onItemClick: "+packageManager.getPackageInstaller() );
//                Intent i = new Intent(Intent.ACTION_VIEW);
//
//                try {
//                    String url = "https://api.whatsapp.com/send?phone=" +"&text=" + URLEncoder.encode(items.get(position), "UTF-8");
//                    i.setPackage("com.whatsapp");
//                    i.setData(Uri.parse(url));
//                    Log.e(TAG, "onItemClick: into the try block "+ url );
//                    Log.e(TAG, "onItemClick:packageManager==::)) "+packageManager );
//                    if (i.resolveActivity(packageManager) != null) {
//                        Log.e(TAG, "onItemClick: into the if block" );
//                        getApplicationContext().startActivity(i);
//
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                    Log.e(TAG, "onItemClick: exception==:::) "+e );
//                }
            }
        });

    }

    private void setName(String s) {
        targetName= s;
        convIndex=0;
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
                layoutExpand();
                break;

//            case R.id.buttonClose:
//                //closing the widget
//                Log.e(TAG, "onClick: buttonClose" );
//                stopSelf();
//                break;
        }
    }

    public void layoutExpand(){
        collapsedView.setVisibility(View.VISIBLE);
        expandedView.setVisibility(View.GONE);
    }

    public void layoutCollapsed(){
        collapsedView.setVisibility(View.GONE);
        expandedView.setVisibility(View.VISIBLE);
    }
}
