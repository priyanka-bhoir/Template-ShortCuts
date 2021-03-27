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
    static String targetName=null;
    static int convIndex = 0;

    public Accessibility(){
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.e(TAG, "onAccessibilityEvent: called"+ event);
        if (event == null){
            return;
        }
        AccessibilityNodeInfo rootNode = event.getSource();
        if (rootNode == null){
            return;
        }

        try{
            AccessibilityNodeInfo textBox = getNode(rootNode, chatBoxRefId);
            Bundle arguments = new Bundle();
            arguments.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,targetName);
            textBox.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT, arguments);
            Log.e(TAG, "onAccessibilityEvent: textBox.getText().toString()==:)  "+ textBox.getText().toString());
            if (!textBox.getText().toString().isEmpty()){
                Log.e(TAG, "onAccessibilityEvent: i don't know why you are getting called sendButtonRefId==::)) " + sendButtonRefId );
                AccessibilityNodeInfo sendButton = getNode(rootNode, sendButtonRefId);
                sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            Thread.sleep(1000);
            performGlobalAction (GLOBAL_ACTION_BACK);
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

    private boolean isAccessibilityOn (Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + clazz.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1){
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null){
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()){
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}