package com.fun.yuloleake.googlemapstesting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Yulo Leake on 9/26/2016.
 */

public class MarkerNotification extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Notification received!");

        Intent mainIntent = new Intent(this, MapsActivity.class);

        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Marker says")
                .setContentText("\"Hello world!\"")
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("ticker message")
                .setWhen(System.currentTimeMillis());

        notificationManager.notify(001, mBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
