package com.example.melik.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class AlarmReceiver extends BroadcastReceiver{
    private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";
    public static final String NOTIFICATION_BREAKFAST_ID = "breakfast_id";
    public static final String NOTIFICATION_LUNCH_ID = "lunch_id";
    public static final String NOTIFICATION_DINNER_ID = "dinner_id";

    public static final String CHANNEL_BREAKFAST_NAME = "Breakfast Channel";
    public static final String CHANNEL_LUNCH_NAME = "Lunch Channel";
    public static final String CHANNEL_DINNER_NAME = "Dinner Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        /*Notification.Builder builder = new Notification.Builder(context);

        @SuppressLint("WrongConstant") Notification notification = builder.setContentTitle("Demo App Notification")
                .setContentText(intent.getStringExtra("msg"))
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("Oteller İçin Sanal Yardımcı")
                .setContentIntent(pendingIntent).build();

*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(intent.getAction().equals("Breakfast")){
                builder.setChannelId();
            }
            builder.setChannelId(CHANNEL_ID);
        }*/

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if(intent.getAction().equals("Breakfast")){
                NotificationChannel Breakfastchannel = new NotificationChannel(
                        NOTIFICATION_BREAKFAST_ID,
                        CHANNEL_BREAKFAST_NAME,
                        IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(Breakfastchannel);
                NotificationCompat.Builder notificationCompatBuilder=new NotificationCompat.Builder(context, NOTIFICATION_BREAKFAST_ID);
                notificationCompatBuilder.setContentText(intent.getStringExtra("msg"))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Oteller İçin Sanal Yardımcı")
                        .setContentIntent(pendingIntent).build();

                notificationManager.notify(0, notificationCompatBuilder.build());



            }else if(intent.getAction().equals("Lunch")){
                NotificationChannel Lunchchannel = new NotificationChannel(
                        NOTIFICATION_LUNCH_ID,
                        CHANNEL_LUNCH_NAME,
                        IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(Lunchchannel);
                NotificationCompat.Builder notificationCompatBuilder=new NotificationCompat.Builder(context, NOTIFICATION_LUNCH_ID);
                notificationCompatBuilder.setContentText(intent.getStringExtra("msg"))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Oteller İçin Sanal Yardımcı")
                        .setContentIntent(pendingIntent).build();

                notificationManager.notify(0, notificationCompatBuilder.build());

            }else if(intent.getAction().equals("Dinner")){
                NotificationChannel Dinnerchannel = new NotificationChannel(
                        NOTIFICATION_DINNER_ID,
                        CHANNEL_DINNER_NAME,
                        IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(Dinnerchannel);
                NotificationCompat.Builder notificationCompatBuilder=new NotificationCompat.Builder(context, NOTIFICATION_DINNER_ID);
                notificationCompatBuilder.setContentText(intent.getStringExtra("msg"))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Oteller İçin Sanal Yardımcı")
                        .setContentIntent(pendingIntent).build();

                notificationManager.notify(0, notificationCompatBuilder.build());
            }
        }

    }
}
