package com.example.bibliobook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

import java.util.ArrayList;

public class Details extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private RequestQueue queue;
    private String idLExt = "";
    private String pageNumberExt = "No";
    private String descriptionExt = "";
    private boolean liked = false;
    private String from;

    public void AddData(String titre, String auteur, String genre, String annee, String url, String gid){
        boolean insertedData = mDatabaseHelper.addData(titre,auteur,genre,annee,url,gid);
        if (insertedData){
            Toast.makeText(getApplicationContext(), "Data successfully inserted",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteData(String gid){
        boolean insertedData = mDatabaseHelper.deleteData(gid);
        if (insertedData){
            Toast.makeText(getApplicationContext(), "Data successfully removed",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        queue = Volley.newRequestQueue(this);
        String titreExtra = getIntent().getStringExtra("TITRE");
        final String idExtra = getIntent().getStringExtra("ID");
        ImageView imageview1 = findViewById(R.id.returnToSearchArrowG);
        from = getIntent().getStringExtra("FROM");
        imageview1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                finish();
                if (from.equals("fav")){
                    Intent intent = new Intent (getBaseContext(), Favorites.class);
                    startActivity(intent);
                }
            }

        });
        HttpGetRequest getRequest = new HttpGetRequest();
        getRequest.execute("");
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.e("JFL", "STARTED");
            String titreExtra = getIntent().getStringExtra("TITRE");
            final String idExtra = getIntent().getStringExtra("ID");
            mDatabaseHelper = new DatabaseHelper(getBaseContext());
            liked = mDatabaseHelper.exists(idExtra);
            if(liked){
                ImageView heartImg = findViewById(R.id.like);
                heartImg.setImageResource(R.drawable.like_red);
            }
            TextView toolbarText = findViewById(R.id.TopBarSearchTextG);
            final TextView titreText = findViewById(R.id.titreG);
            final TextView auteurText = findViewById(R.id.auteurG);
            final TextView genreText = findViewById(R.id.genreG);
            final TextView anneeText = findViewById(R.id.anneeG);
            final TextView pageText = findViewById(R.id.pageG);
            final TextView descText = findViewById(R.id.descriptionG);
            final ImageView couvertureImg = findViewById(R.id.couvertureG);

            toolbarText.setText("Details");
            String urlC = "https://www.googleapis.com/books/v1/volumes?q=\"" + idExtra + "\"+intitle:\"" + titreExtra.replaceAll(" ","%20") + "\"";
            final Livre livreInfo = new Livre("","","","","");
            Log.e("JFL", "PRECALL");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlC, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("JFL", "Reponse");

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
                                        idLExt = livre.getString("id");
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
                                Log.e("JFL","CATCH");
                                //textCenter.setText("JSON Parsing Failed");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("JFL","ERROR");
                            // TODO: Handle error
                            //textCenter.setText("Erreur lors de l'appel API");
                            error.printStackTrace();
                        }
                    });
            queue.add(jsonObjectRequest);
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
