package com.tomchua.accenturehackathon.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.Html;


import com.tomchua.accenturehackathon.Activity.MainActivity;
import com.tomchua.accenturehackathon.Activity.ShoppingActivity;
import com.tomchua.accenturehackathon.Models.NotificationVO;
import com.tomchua.accenturehackathon.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 200;
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String CHANNEL_ID = "1";
    private static final String URL = "url";
    private static final String Channel = "test";
    private static final String ACTIVITY = "activity";
    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;
    Date now = new Date();
    long uniqueId = now.getTime();

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("MainActivity", ShoppingActivity.class);
//        activityMap.put("SecondActivity", Home.class);
//        activityMap.put("ThirdActivity", Expiry.class);
    }

    /**
     * Displays notification based on parameters
     *
     * @param notificationVO
     * @param resultIntent
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayNotification(NotificationVO notificationVO, Intent resultIntent) {

        String message = notificationVO.getMessage();
        String title = notificationVO.getTitle();
        String iconUrl = notificationVO.getIconUrl();
        String action = notificationVO.getAction();
        String destination = notificationVO.getActionDestination();
        final int icon = R.mipmap.pill_logo;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,
                Channel, NotificationManager.IMPORTANCE_DEFAULT);

        Notification.Builder builder = new Notification.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_delete_sweep_black_24dp)
                .setContentTitle(title)
                .setContentText(message);

        Intent notificationIntent = new Intent(mContext, ShoppingActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager mNotificationManager =
                (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(androidChannel);

    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            java.net.URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
