package com.fun.yuloleake.googlemapstesting;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yulo Leake on 9/26/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {


    private static final String NOTIFICATION_GROUP = "com.fun.yuloleake.googlemapstesting.notification_type";

    public AlarmReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        pushNotification(context, intent);

        completeWakefulIntent(intent);
    }

    private void pushNotification(Context context, Intent intent){

        NotificationCompat.Builder mBuilder = buildNotification(context, intent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }

    private NotificationCompat.Builder buildNotification(Context context, Intent intent){
        String name = intent.getStringExtra("record_name");
        LatLng latLng = intent.getParcelableExtra("record_latLong");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Marker " + name + " says")
                .setContentText("\"Hello world!\" from " + latLng.toString())
                .setAutoCancel(true)
                .setGroup(NOTIFICATION_GROUP)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH);   // Required to show heads-up notification

        Intent resultIntent = new Intent(context, MapsActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_INFO)
                .putExtra("name", name)
                .putExtra("latLong", latLng);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        return mBuilder;
    }

    public void setAlarm(Context context, MarkerRecord record){
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("record_name", record.getName());
        intent.putExtra("record_latLong", record.getMarker().getPosition());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10 * 1000, pendingIntent);
    }
}
