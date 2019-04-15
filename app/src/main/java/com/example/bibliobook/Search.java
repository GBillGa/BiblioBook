package com.example.bibliobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/*

    Here we have our java class for our Search Activity
    We have an EditText , an image to click to launch the search but we also have filters as toggle buttons
    It also contains an arraylist of the different Toggle Buttons that we use to unselect them all when one is clicked
    We decide to use this system because each book in google api has only one literary genre

 */

public class Search extends AppCompatActivity {

    private ArrayList<ToggleButton> tList = new ArrayList<>();

    //Here we have our function to unselect all the togglebuttons which is only a for loop
    public void unselectAll(){
        for (ToggleButton t : tList){
            t.setChecked(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Here we create the intent that will be used if we click on the search image
        //We also create an empty extra in case no Toggle Button is clicked
        final Intent intent = new Intent(getBaseContext(), ResultList.class);
        intent.putExtra("TAG", "");

        //Then we put an Event Listener to launch the search when we click on the image
        ImageView searchIcon = findViewById(R.id.searchIconImage);
        searchIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //First we display a waiting message because when we launch our research the user will have to wait for the async API call to finish
                Toast.makeText(Search.this, "Research launched, please wait !",Toast.LENGTH_SHORT).show();

                //We create a second extra that will be used to perform the search
                EditText edit = findViewById(R.id.searchField);
                intent.putExtra("SEARCHED", edit.getText().toString());

                //And finally we launch the result list activity
                startActivity(intent);

            }

        });

        //Here we just add all the Toggle Buttons to our List after finding them using their ID
        tList.add((ToggleButton)findViewById(R.id.JFiction));
        tList.add((ToggleButton)findViewById(R.id.Fiction));
        tList.add((ToggleButton)findViewById(R.id.Drama));
        tList.add((ToggleButton)findViewById(R.id.SScience));
        tList.add((ToggleButton)findViewById(R.id.Philo));
        tList.add((ToggleButton)findViewById(R.id.History));

        //
        for (int i = 0; i < tList.size(); i ++){
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
