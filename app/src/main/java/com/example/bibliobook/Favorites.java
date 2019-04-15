package com.example.bibliobook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/*

    In this class we display the information stored in our database to our listview using the same layout as resultList

 */

public class Favorites extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    ArrayList<Livre> ListData = new ArrayList<>();
    ArrayList<String> ListID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        //When the user click on the back arrow we finish the activity
        ImageView imageview1 = findViewById(R.id.returnToSearchArrowG);
        imageview1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        TextView titre = findViewById(R.id.TopBarSearchText);
        titre.setText("Favoris");
        mListView = findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    //This function is used to fulfill our listview
    private void populateListView() {

        //We get the cursor and just read an element after the other
        Cursor data = mDatabaseHelper.getData();
        while(data.moveToNext()){
            Livre tmpLivre = new Livre (data.getString(1),data.getString(2),data.getString(3),data.getString(4),data.getString(5));
            ListData.add(tmpLivre);
            ListID.add(data.getString(6));
        }

        //Then we call the adapter
        LivreAdapter adapter = new LivreAdapter(Favorites.this, ListData);
        mListView.setAdapter(adapter);

        //We put an event listener to launch the deytail activity when the user click on a book
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Livre item = LivreAdapter.getItemC(ListData,position);
                String titre = item.getTitre();
                String idLivreClicked = ListID.get(position);

                //We create extras to perform our API Call
                Intent intent = new Intent(getBaseContext(), Details.class);
                intent.putExtra("TITRE", titre);
                intent.putExtra("ID", idLivreClicked);
                intent.putExtra("FROM","fav");
                //then we start the detail activity and finish this one
                startActivity(intent);
                finish();
            }
        });
    }

}
