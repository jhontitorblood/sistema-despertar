package com.sistema.despertar;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.ValueCallback;
import android.view.WindowManager;
import android.graphics.Color;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private WebView web;
    private ValueCallback<Uri[]> filePathCallback;
    private static final int FILE_CHOOSER_REQUEST = 4217;

    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        getWindow().setStatusBarColor(Color.rgb(7,9,16));
        getWindow().setNavigationBarColor(Color.rgb(7,9,16));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0);
        web = new WebView(this);
        web.setBackgroundColor(Color.rgb(7,9,16));
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setMediaPlaybackRequiresUserGesture(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (filePathCallback != null) filePathCallback.onReceiveValue(null);
                filePathCallback = callback;
                Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
                pick.addCategory(Intent.CATEGORY_OPENABLE);
                pick.setType("image/*");
                pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                try {
                    Intent chooser = Intent.createChooser(pick, "Selecciona tu foto de perfil");
                    startActivityForResult(chooser, FILE_CHOOSER_REQUEST);
                } catch (Exception e) {
                    filePathCallback = null;
                    callback.onReceiveValue(null);
                    return false;
                }
                return true;
            }
        });
        web.addJavascriptInterface(new NativeBridge(this), "AndroidBridge");
        setContentView(web);
        web.loadUrl("file:///android_asset/www/index.html");
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (filePathCallback == null) return;
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    ArrayList<Uri> picked = new ArrayList<>();
                    for (int i = 0; i < count; i++) picked.add(data.getClipData().getItemAt(i).getUri());
                    results = picked.toArray(new Uri[0]);
                } else if (data.getData() != null) {
                    results = new Uri[]{data.getData()};
                }
            }
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }

    public boolean notificationsAllowed(){return Build.VERSION.SDK_INT<33||checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED;}
    public void requestNotifications(){if(Build.VERSION.SDK_INT>=33&&!notificationsAllowed())requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},73);else sendPermissionResult(notificationsAllowed());}
    @Override public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] results){super.onRequestPermissionsResult(requestCode,permissions,results);if(requestCode==73)sendPermissionResult(results.length>0&&results[0]==PackageManager.PERMISSION_GRANTED);}
    private void sendPermissionResult(boolean granted){if(web!=null)web.post(()->web.evaluateJavascript("window.onAndroidPermissionResult && window.onAndroidPermissionResult("+granted+")",null));}
    @Override public void onBackPressed(){if(web!=null&&web.canGoBack())web.goBack();else super.onBackPressed();}
    public static class NativeBridge {
        private final MainActivity activity; NativeBridge(MainActivity a){activity=a;}
        @JavascriptInterface public void requestNotifications(){activity.runOnUiThread(activity::requestNotifications);}
        @JavascriptInterface public boolean hasNotificationsPermission(){return activity.notificationsAllowed();}
        @JavascriptInterface public void scheduleReminder(String id,String title,String time,int dayMask){ReminderScheduler.daily(activity,id,title,time,dayMask);}
        @JavascriptInterface public void scheduleInterval(String id,String title,int minutes){ReminderScheduler.interval(activity,id,title,minutes);}
        @JavascriptInterface public void cancelReminder(String id){ReminderScheduler.cancel(activity,id);}
    }
}
