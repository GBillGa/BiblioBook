package com.example.bibliobook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*

    This class will be called either after clicking on a book from result list either when a favorites book is clicked
    It will make another Async API Call to get the summary of the book and the number of pages

 */

public class Details extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private RequestQueue queue;
    private String pageNumberExt = "No";
    private String descriptionExt = "";
    private boolean liked = false;
    private String from;

    //Function used to add a book to the favorites SQLite database
    public void AddData(String titre, String auteur, String genre, String annee, String url, String gid){
        boolean insertedData = mDatabaseHelper.addData(titre,auteur,genre,annee,url,gid);
        if (insertedData){
            Toast.makeText(getApplicationContext(), "Successfully added to favorites !",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong during adding",
                    Toast.LENGTH_LONG).show();
        }
    }

    //Same but to remove a book from database
    public void DeleteData(String gid){
        boolean insertedData = mDatabaseHelper.deleteData(gid);
        if (insertedData){
            Toast.makeText(getApplicationContext(), "Successfully removed from favorites",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong during deletion",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //We initialize our queue and our EXTRA
        queue = Volley.newRequestQueue(this);
        from = getIntent().getStringExtra("FROM");

        //We create an Event Listener to finish the activity when clicking on the return arrow
        ImageView arrow = findViewById(R.id.returnToSearchArrowG);
        arrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                if (from.equals("fav")){
                    //We use the EXTRA to know from where the Details activity has been called
                    //This system is usefull to refresh data from the favorite list if for example a book has been removed
                    Intent intent = new Intent (getBaseContext(), Favorites.class);
                    startActivity(intent);
                }
            }
        });

        //Then we just make our API Call
        HttpGetRequest getRequest = new HttpGetRequest();
        getRequest.execute("");
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String>{

        //In this Async Task we will perform a search using the extras of the intent
        @Override
        protected String doInBackground(String... strings) {

            //First of all we get the extras
            String titreExtra = getIntent().getStringExtra("TITRE");
            final String idExtra = getIntent().getStringExtra("ID");

            //Then we check if the book is in the database to know which image to display
            mDatabaseHelper = new DatabaseHelper(getBaseContext());
            liked = mDatabaseHelper.exists(idExtra);
            if(liked){
                ImageView heartImg = findViewById(R.id.like);
                heartImg.setImageResource(R.drawable.like_red);
            }

            //We create our different fields
            TextView toolbarText = findViewById(R.id.TopBarSearchTextG);
            final TextView titreText = findViewById(R.id.titreG);
            final TextView auteurText = findViewById(R.id.auteurG);
            final TextView genreText = findViewById(R.id.genreG);
            final TextView anneeText = findViewById(R.id.anneeG);
            final TextView pageText = findViewById(R.id.pageG);
            final TextView descText = findViewById(R.id.descriptionG);
            final ImageView couvertureImg = findViewById(R.id.couvertureG);

            toolbarText.setText("Details");

            //Then we make our API Call using the url where the space have been replaced by %20
            String urlC = "https://www.googleapis.com/books/v1/volumes?q=\"" + idExtra + "\"+intitle:\"" + titreExtra.replaceAll(" ","%20") + "\"";

            //We create a new book that will be fulfilled on the response of our call
            final Livre livreInfo = new Livre("","","","","");

            //API Call
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlC, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            //Here we just verify the received data and add them to the book
                            try {
                                JSONArray jsonArray = response.getJSONArray("items");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject livre = jsonArray.getJSONObject(i);
                                    JSONObject info = livre.getJSONObject("volumeInfo");
                                    livreInfo.setTitre(info.getString("title"));
                                    livreInfo.setAuteur("No author");
                                    if (info.has("authors")) {
                                        JSONArray auteurArray = info.getJSONArray("authors");
                                        livreInfo.setAuteur((String) auteurArray.get(0));
                                    }
                                    livreInfo.setGenre("No genre");
                                    if (info.has("categories")) {
                                        JSONArray genreArray = info.getJSONArray("categories");
                                        livreInfo.setGenre((String)genreArray.get(0));
                                    }
                                    livreInfo.setAnnee("No publication date");
                                    if (info.has("publishedDate")){
                                        livreInfo.setAnnee(info.getString("publishedDate"));
                                    }
                                    if (livre.has("id")){
                                    }
                                    if (info.has("pageCount")){
                                        pageNumberExt = info.getString("pageCount");
                                    }
                                    if (info.has("description")){
                                        descriptionExt = info.getString("description");
                                    }
                                    String miniature = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
                                    livreInfo.setUrl(miniature);
                                    if (info.has("imageLinks")){
                                        JSONObject images = info.getJSONObject("imageLinks");
                                        livreInfo.setUrl(images.getString("thumbnail"));
                                    }
                                }

                                //Then if all the needed information where received we display the information
                                titreText.setText(livreInfo.getTitre());
                                auteurText.setText(livreInfo.getAuteur());
                                genreText.setText(livreInfo.getGenre());
                                anneeText.setText(livreInfo.getAnnee());
                                pageText.setText(pageNumberExt + " pages");
                                descText.setText(descriptionExt);
                                descText.setMovementMethod(new ScrollingMovementMethod());
                                Picasso.get().load(livreInfo.getUrl()).into(couvertureImg);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            queue.add(jsonObjectRequest);

            //Then we have to call the 2 functions when the user clicks on the heart
            final ImageView heart= findViewById(R.id.like);
            heart.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    if (!liked){
                        heart.setImageResource(R.drawable.like_red);
                        liked = true;
                        AddData(livreInfo.getTitre(),livreInfo.getAuteur(),livreInfo.getGenre(),livreInfo.getAnnee(), livreInfo.getUrl(), idExtra);
                    } else {
                        heart.setImageResource(R.drawable.like_white);
                        liked = false;
                        DeleteData(idExtra);
                    }
                }

            });
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

}
