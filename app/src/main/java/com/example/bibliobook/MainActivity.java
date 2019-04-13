package com.example.bibliobook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button searchButton = findViewById(R.id.researchButton);
        final Button favButton = findViewById(R.id.favButton);
        final ImageView alarm = findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Notification activated",
                        Toast.LENGTH_LONG).show();
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,15);
                calendar.set(Calendar.MINUTE,36);
                calendar.set(Calendar.SECOND,35);
                Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(getBaseContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcast);

            }

        });
    }

    public void research(View v){
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void fav(View v){
        Intent intent = new Intent (this, Favorites.class);
        startActivity(intent);
    }
}