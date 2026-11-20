package com.sistema.despertar;
import android.app.Activity;import android.os.Bundle;import android.webkit.WebView;import android.webkit.WebViewClient;import android.webkit.WebChromeClient;import android.view.WindowManager;import android.graphics.Color;import android.os.Build;import android.Manifest;import android.content.pm.PackageManager;import android.webkit.JavascriptInterface;
public class MainActivity extends Activity {
 private WebView web;
 @Override public void onCreate(Bundle saved){super.onCreate(saved);getWindow().setStatusBarColor(Color.rgb(7,9,16));getWindow().setNavigationBarColor(Color.rgb(7,9,16));getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,0);web=new WebView(this);web.setBackgroundColor(Color.rgb(7,9,16));web.getSettings().setJavaScriptEnabled(true);web.getSettings().setDomStorageEnabled(true);web.getSettings().setAllowFileAccess(true);web.getSettings().setAllowContentAccess(true);web.getSettings().setMediaPlaybackRequiresUserGesture(true);web.setWebViewClient(new WebViewClient());web.setWebChromeClient(new WebChromeClient());web.addJavascriptInterface(new NativeBridge(this),"AndroidBridge");setContentView(web);web.loadUrl("file:///android_asset/www/index.html");}
 public boolean notificationsAllowed(){return Build.VERSION.SDK_INT<33||checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED;}
 public void requestNotifications(){if(Build.VERSION.SDK_INT>=33&&!notificationsAllowed())requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},73);else sendPermissionResult(notificationsAllowed());}
 @Override public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] results){super.onRequestPermissionsResult(requestCode,permissions,results);if(requestCode==73)sendPermissionResult(results.length>0&&results[0]==PackageManager.PERMISSION_GRANTED);}
 private void sendPermissionResult(boolean granted){if(web!=null)web.post(()->web.evaluateJavascript("window.onAndroidPermissionResult && window.onAndroidPermissionResult("+granted+")",null));}
 @Override public void onBackPressed(){if(web!=null&&web.canGoBack())web.goBack();else super.onBackPressed();}
 public static class NativeBridge {
  private final MainActivity activity;NativeBridge(MainActivity a){activity=a;}
  @JavascriptInterface public void requestNotifications(){activity.runOnUiThread(activity::requestNotifications);}
  @JavascriptInterface public boolean hasNotificationsPermission(){return activity.notificationsAllowed();}
  @JavascriptInterface public void scheduleReminder(String id,String title,String time,int dayMask){ReminderScheduler.daily(activity,id,title,time,dayMask);}
  @JavascriptInterface public void scheduleInterval(String id,String title,int minutes){ReminderScheduler.interval(activity,id,title,minutes);}
  @JavascriptInterface public void cancelReminder(String id){ReminderScheduler.cancel(activity,id);}
 }
}
