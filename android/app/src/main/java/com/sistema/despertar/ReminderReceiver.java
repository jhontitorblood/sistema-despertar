package com.sistema.despertar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.provider.Settings;
import android.media.AudioAttributes;
import java.util.Calendar;

public class ReminderReceiver extends BroadcastReceiver {
    // New channel ID: Android preserves old channel settings, so use a fresh ID to enable sound.
    private static final String CHANNEL = "sistema_missions_sound_v2";
    private static final long[] VIBRATION = {0, 450, 250, 450};

    @Override public void onReceive(Context c, Intent i) {
        String action = i.getAction();
        String title = i.getStringExtra(ReminderScheduler.EXTRA_TITLE);
        if (title == null || title.trim().isEmpty()) title = "Recordatorio del Sistema";

        if (ReminderScheduler.ACTION_SNOOZE.equals(action)) {
            ReminderScheduler.snooze(c, i.getStringExtra(ReminderScheduler.EXTRA_ID), title);
            return;
        }

        if (Build.VERSION.SDK_INT >= 33 && c.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) return;

        if (!ReminderScheduler.ACTION_SNOOZED.equals(action)) {
            Calendar now = Calendar.getInstance();
            int mask = i.getIntExtra(ReminderScheduler.EXTRA_DAYMASK, 127);
            int day = now.get(Calendar.DAY_OF_WEEK) - 1;
            if ((mask & (1 << day)) == 0) return;
            String rid = i.getStringExtra(ReminderScheduler.EXTRA_ID);
            if ("water-repeat".equals(rid) || "stretch-repeat".equals(rid)) {
                if (now.get(Calendar.HOUR_OF_DAY) < 8 || now.get(Calendar.HOUR_OF_DAY) > 21) return;
            }
        }

        NotificationManager nm = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(CHANNEL, "Recordatorios de SISTEMA (con sonido)", NotificationManager.IMPORTANCE_HIGH);
            ch.setDescription("Alarmas sonoras para misiones, hidratación y pausas activas");
            ch.setSound(Settings.System.DEFAULT_NOTIFICATION_URI,
                new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            ch.enableVibration(true);
            ch.setVibrationPattern(VIBRATION);
            ch.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            nm.createNotificationChannel(ch);
        }

        Intent open = new Intent(c, MainActivity.class);
        open.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent content = PendingIntent.getActivity(c, 1, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent snooze = new Intent(c, ReminderReceiver.class);
        snooze.setAction(ReminderScheduler.ACTION_SNOOZE);
        snooze.putExtra(ReminderScheduler.EXTRA_ID, i.getStringExtra(ReminderScheduler.EXTRA_ID));
        snooze.putExtra(ReminderScheduler.EXTRA_TITLE, title);
        PendingIntent snoozePi = PendingIntent.getBroadcast(c, ("sn_" + i.getStringExtra(ReminderScheduler.EXTRA_ID)).hashCode(), snooze, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder b = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(c, CHANNEL) : new Notification.Builder(c);
        b.setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("SISTEMA · Recordatorio")
            .setContentText(title)
            .setAutoCancel(true)
            .setCategory(Notification.CATEGORY_ALARM)
            .setPriority(Notification.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
            .setContentIntent(content)
            .addAction(android.R.drawable.ic_menu_recent_history, "Posponer 10 min", snooze);
        nm.notify(title.hashCode(), b.build());
    }
}
