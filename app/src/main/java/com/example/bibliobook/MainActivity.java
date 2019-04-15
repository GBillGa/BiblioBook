package com.example.bibliobook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

/*

    This class is used for the Main Activity (which is basically the homepage)
    It contains 2 functions to call other activities (favorites and search)
    It also contains a little clickable image of bell to launch repeating notification

 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView alarm = findViewById(R.id.alarm);       //This is the bell to launch notification
        alarm.setOnClickListener(new View.OnClickListener() {   //Since it is an image we just have to put on it an OnClickListener

            @Override
            public void onClick(View v) {
                // Here you can set the hour as you want but keep in mind its a 24 hour format
                int hour = 9;
                int minute = 30;
                int second = 0;

                //Then we display a message to confirm that notification have been activated
                Toast.makeText(getApplicationContext(), "Notification activated everyday at " + hour + ":" + minute + ":" + second,
                        Toast.LENGTH_LONG).show();

                //Here we use our SET_ALARM permission (the second asked permission in the subject
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                //Then we initialize the calendar (which is basically when the notification will be launched)
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,second);

                //Then we call an intent which will be needed for the broadcast just after
                Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(getBaseContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                //At the end we just launch a repeating broadcast that will be launched everyday because of the AlarmManager.INTERVAL_DAY
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcast);

            }

        });
    }

    //Our functions that will be called when we click on the buttons of the main page
    //It's basically just intents without extras
    public void research(View v){
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void fav(View v){
        Intent intent = new Intent (this, Favorites.class);
        startActivity(intent);
    }
}