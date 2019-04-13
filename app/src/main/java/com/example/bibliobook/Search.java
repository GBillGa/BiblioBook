package com.example.bibliobook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private ArrayList<ToggleButton> tList = new ArrayList<>();
    Intent intent;
    private int i;

    public void unselectAll(){
        for (ToggleButton t : tList){
            t.setChecked(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageView imageview1 = findViewById(R.id.searchIconImage);
        intent = new Intent(getBaseContext(), ResultList.class);
        intent.putExtra("TAG", "");
        imageview1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Toast.makeText(Search.this, "Recherche lancée",Toast.LENGTH_SHORT).show();
                EditText edit = (EditText)findViewById(R.id.searchField);
                intent.putExtra("SEARCHED", edit.getText().toString());
                startActivity(intent);

            }

        });
        tList.add((ToggleButton)findViewById(R.id.JFiction));
        tList.add((ToggleButton)findViewById(R.id.Fiction));
        tList.add((ToggleButton)findViewById(R.id.Drama));
        tList.add((ToggleButton)findViewById(R.id.SScience));
        tList.add((ToggleButton)findViewById(R.id.Philo));
        tList.add((ToggleButton)findViewById(R.id.History));
        for (i = 0; i < tList.size(); i ++){
           tList.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked){
                       unselectAll();
                       buttonView.setChecked(true);
                       intent.removeExtra("TAG");
                       intent.putExtra("TAG",buttonView.getResources().getResourceEntryName(buttonView.getId()));
                   } else {
                       unselectAll();
                   }
               }
           });
        }
    }


}
