package com.example.bibliobook;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/*

    This class is our receiver and is used to launch a notification when it receives a message from the repeating broadcast

 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //We initialize our notification manager and then we will build our notif
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context,MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Whne we click on the notification it will launch the main activity
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //This way to build a notification is depreciated but not for the version targetted by the subject (API 23)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Revenez !")
                .setContentText("Il y a plein de nouveaux livres à découvrir")
                .setAutoCancel(true);

        //At the end we just launch our notification
        notificationManager.notify(100,builder.build());
    }
}
