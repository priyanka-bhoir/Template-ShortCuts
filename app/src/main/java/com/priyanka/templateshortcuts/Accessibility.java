package com.priyanka.templateshortcuts;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

import static com.priyanka.templateshortcuts.Constant.chatBoxRefId;
import static com.priyanka.templateshortcuts.Constant.sendButtonRefId;

public class Accessibility extends AccessibilityService {

    String TAG="Accessibility";
    static String targetName="";
    static int convIndex = 0;
    private Handler mHandler;
    static AccessibilityEvent e;

    public Accessibility(){
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();

//        HandlerThread handlerThread = new HandlerThread("auto-handler");
//        handlerThread.start();
//        mHandler = new Handler(handlerThread.getLooper());

//    }

//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
//        info.notificationTimeout = 100;
//        info.packageNames= new String[]{"com.whatsapp"};
//        this.setServiceInfo(info);
//    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.e(TAG, "onAccessibilityEvent: called =:::) " + event);
        Log.e(TAG, "onAccessibilityEvent: getClassName()=:)) " + event.getClassName() );
        Log.e(TAG, "onAccessibilityEvent: getPackageName()=:)) " + event.getPackageName());
        Log.e(TAG, "onAccessibilityEvent: getSource()()=:)) " + event.getSource());
        Log.e(TAG, "getNode:nodeInfos.getRootInActiveWindow()---->>> " + getRootInActiveWindow());

        //checkpoint
        try {
            Log.e(TAG, "onAccessibilityEvent: event.getRecord(0);-->> "+event.getRecord(0) );

            AccessibilityNodeInfo nodeInfos = event.getSource().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            Log.e(TAG, "getNode:nodeInfos.getClass()---->>> " + nodeInfos.getClassName());

            AccessibilityManager manager;

//            final AccessibilityNodeInfo textNodeInfo = findTextViewNode(getRootInActiveWindow());
//
//            if (textNodeInfo == null) return;
//
//            Rect rect = new Rect();
//
//            textNodeInfo.getBoundsInScreen(rect);
            /*
            final AccessibilityNodeInfo textNodeInfo = findTextViewNode(getRootInActiveWindow());

        if (textNodeInfo == null) return;

        Rect rect = new Rect();

        textNodeInfo.getBoundsInScreen(rect);
             */
            if (nodeInfos.getClassName().equals("android.widget.EditText")){
                Log.e(TAG, "you got en edit text");
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
//        Log.e("vaibhavi==>","events EventType"+event.getEventType()+"-describeContents-"+event.describeContents()+"-getPackageName-: "+event.getPackageName()+" -getAction-"+event.getAction());
    if (!targetName.isEmpty()){
        accessibilityEvent(event);
//        AccessibilityNodeInfo source = event.getSource();
//        Log.e(TAG, "onAccessibilityEvent: source-->> "+source );
//        if(event.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
//            explore(source,event);
    }else {
        return;
    }
//-------------------------------------------------------------------------------------------END----------------------------------------------------------------------------------

//        if (getRootInActiveWindow () == null) {
//            return;
//        }
//
//        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());
//
//        // Whatsapp Message EditText id
//        List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/entry");
//        if (messageNodeList == null || messageNodeList.isEmpty ()) {
//            return;
//        }
//
//        // check if the whatsapp message EditText field is filled with text and ending with your suffix (explanation above)
//        AccessibilityNodeInfoCompat messageField = messageNodeList.get (0);
//        if (messageField.getText () == null || messageField.getText ().length () == 0
//                || !messageField.getText ().toString ().endsWith (getApplicationContext ().getString (R.string.whatsapp_suffix))) { // So your service doesn't process any message, but the ones ending your apps suffix
//            return;
//        }
//
//        // Whatsapp send button id
//        List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
//        if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
//            return;
//        }
//
//        AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get (0);
//        if (!sendMessageButton.isVisibleToUser ()) {
//            return;
//        }
//
//        // Now fire a click on the send button
//        sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);
//
//        // Now go back to your app by clicking on the Android back button twice:
//        // First one to leave the conversation screen
//         Second one to leave whatsapp
//        try {
//            Thread.sleep (500); // hack for certain devices in which the immediate back click is too fast to handle
//            performGlobalAction (GLOBAL_ACTION_BACK);
//            Thread.sleep (500);  // same hack as above
//        } catch (InterruptedException ignored) {}
//        performGlobalAction (GLOBAL_ACTION_BACK);
    }

    public void  accessibilityEvent(AccessibilityEvent event){
        if (event == null){
            return;
        }

        AccessibilityNodeInfo rootNode = event.getSource();
        Log.e(TAG, "accesiblityEvent: "+rootNode);
        if (rootNode == null){
            return;
        }
        try {
//            String name = getName(rootNode);
//            if (name == null) {
//                return;
//            }

//            AccessibilityNodeInfo source = event.getSource();
//            rootNode=source.getParent();

            Log.e(TAG, "accesiblityEvent: rootNode.getClassName()--->> "+rootNode.getClassName());

            Log.e(TAG, "onAccessibilityEvent: event.getSource().getViewIdResourceName()--->>> "+event.getSource().getViewIdResourceName());

            AccessibilityNodeInfo textBox = getNode(rootNode, chatBoxRefId);
            Log.e(TAG, "accesiblityEvent:textBox.isEditable()------->>> "+textBox.isEditable());
            Log.e(TAG, "accesiblityEvent: checkpoint----1---------->");
            Bundle arguments = new Bundle();
            Log.e(TAG, "accesiblityEvent: checkpoint----2---------->");
            arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, targetName);
            Log.e(TAG, "accesiblityEvent: checkpoint----3---------->");
            textBox.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
//            textBox.setFocusable(false);
            Log.e(TAG, "onAccessibilityEvent: textBox.getText().toString()==:)  " + textBox.getText().toString());
            if (!textBox.getText().toString().isEmpty()){
                Log.e(TAG, "onAccessibilityEvent: i don't know why you are getting called sendButtonRefId==::)) " + sendButtonRefId);
                AccessibilityNodeInfo sendButton = getNode(rootNode, sendButtonRefId);
                Log.e(TAG, "accesiblityEvent: checkpoint----4---------->");
                sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.e(TAG, "accesiblityEvent: checkpoint----5---------->");
                targetName="";
//                performGlobalAction (GLOBAL_ACTION_BACK);
            }
//            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "onAccessibilityEvent: Exception=:  " + e );
        }
    }

    private AccessibilityNodeInfo getNode(AccessibilityNodeInfo rootNode, String refId){

        Log.e(TAG, "getNode:rootNode.getWindowId();-->> "+ rootNode.getWindowId());
//        try {
            Log.e(TAG, "getNode:rootNode.getTraversalAfter();-->> "+rootNode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        rootNode.
        AccessibilityNodeInfo textBoxNode = null;

        List<AccessibilityNodeInfo> urlNodeInfo = rootNode.findAccessibilityNodeInfosByViewId(refId);
        Log.e(TAG, "getNode:urlNodeInfo-->>> " + urlNodeInfo);
        if (urlNodeInfo != null && !urlNodeInfo.isEmpty()){
            textBoxNode =urlNodeInfo.get(0);
            Log.e(TAG, "getNode: "+ textBoxNode );
            Log.e(TAG, "getNode: textBoxNode.isEditable()--->>> "+textBoxNode.isEditable() );
            return textBoxNode;
        }
        return textBoxNode;
    }

    private String getName(AccessibilityNodeInfo rootNode){
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

    private void explore(AccessibilityNodeInfo view, AccessibilityEvent event){
        int count = view.getChildCount();
        Log.e(TAG, "explore: view==> "+view );
        AccessibilityNodeInfo rootNode = event.getSource();



        Log.e(TAG, "explore:child-->>   " + view.getParent() );
        for(int i=0; i<count; i++){
//            AccessibilityNodeInfo child = view.getParent();
            Log.e(TAG, "explore:view.getChild(i) "+view.getChild(i) );
            try {
//            String name = getName(rootNode);
//            if (name == null) {
//                return;
//            }
                AccessibilityNodeInfo textBox = getNode(rootNode, chatBoxRefId);

                Log.e(TAG, "accesiblityEvent: checkpoint----1---------->");
                Bundle arguments = new Bundle();
                Log.e(TAG, "accesiblityEvent: checkpoint----2---------->");
                arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, targetName);
                Log.e(TAG, "accesiblityEvent: checkpoint----3---------->");
                textBox.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                textBox.setFocusable(false);
                Log.e(TAG, "onAccessibilityEvent: textBox.getText().toString()==:)  " + textBox.getText().toString());
                if (!textBox.getText().toString().isEmpty()){
                    Log.e(TAG, "onAccessibilityEvent: i don't know why you are getting called sendButtonRefId==::)) " + sendButtonRefId);
                    AccessibilityNodeInfo sendButton = getNode(rootNode, sendButtonRefId);
                    Log.e(TAG, "accesiblityEvent: checkpoint----4---------->");
                    sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e(TAG, "accesiblityEvent: checkpoint----5---------->" );
                    targetName="";
                }
//            Thread.sleep(1000);
//            performGlobalAction (GLOBAL_ACTION_BACK);
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "onAccessibilityEvent: Exception=:  " + e);
            }
            // explore(child);
            // child.recycle();
        }
    }

    @Override
    public void onInterrupt(){
        Log.e(TAG, "onInterrupt: interupted" );
    }
}