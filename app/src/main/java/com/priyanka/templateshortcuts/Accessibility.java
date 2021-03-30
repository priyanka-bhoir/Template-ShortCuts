package com.priyanka.templateshortcuts;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

import static com.priyanka.templateshortcuts.Constant.chatBoxRefId;
import static com.priyanka.templateshortcuts.Constant.sendButtonRefId;

public class Accessibility extends AccessibilityService {

    String TAG="Accessibility";
    static String targetName="";
    static int convIndex = 0;

    public Accessibility(){
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.e(TAG, "onAccessibilityEvent: called =:::) " + event);
    if (!targetName.isEmpty()){
        if (targetName.equals("")){
            Log.e(TAG, "onAccessibilityEvent: targetName null" );
            return;
        }
        accesiblityEvent(event);
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
//        // Second one to leave whatsapp
//        try {
//            Thread.sleep (500); // hack for certain devices in which the immediate back click is too fast to handle
//            performGlobalAction (GLOBAL_ACTION_BACK);
//            Thread.sleep (500);  // same hack as above
//        } catch (InterruptedException ignored) {}
//        performGlobalAction (GLOBAL_ACTION_BACK);
    }

    private void accesiblityEvent(AccessibilityEvent event){
        if (event == null){
            return;
        }

        AccessibilityNodeInfo rootNode = event.getSource();
        if (rootNode == null){
            return;
        }
        try {
            String name = getName(rootNode);
            if (name == null) {
                return;
            }
            AccessibilityNodeInfo textBox = getNode(rootNode, chatBoxRefId);
            Log.e(TAG, "PACKAGE NAME =  " +   textBox.getPackageName().toString());


            Bundle arguments = new Bundle();
            arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, targetName);
            textBox.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            Log.e(TAG, "onAccessibilityEvent: textBox.getText().toString()==:)  " + textBox.getText().toString());
            if (!textBox.getText().toString().isEmpty()) {
                Log.e(TAG, "onAccessibilityEvent: i don't know why you are getting called sendButtonRefId==::)) " + sendButtonRefId);
                AccessibilityNodeInfo sendButton = getNode(rootNode, sendButtonRefId);
                sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                targetName="";
            }
//            Thread.sleep(1000);
//            performGlobalAction (GLOBAL_ACTION_BACK);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onAccessibilityEvent: Exception=:  " + e);
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

    @Override
    public void onInterrupt(){

    }
}