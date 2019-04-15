package com.example.bibliobook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*

    This class is used to display the results of the search
    It's basically the same as Favorites just we use the extras to filter the search (extra represent the toggle button

 */

public class ResultList extends AppCompatActivity {

    private RequestQueue queue;
    private ListView mListView;
    private ArrayList<Livre> livreArrayList = new ArrayList<>();
    private ArrayList<String> idLivre = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        queue = Volley.newRequestQueue(this);

        //We display the searched terms on the toolbar
        final TextView text = findViewById(R.id.TopBarSearchText);
        String searchedTerms = getIntent().getStringExtra("SEARCHED");
        text.setText(searchedTerms);

        //Return arrow finish the activity
        ImageView arrow = findViewById(R.id.returnToSearchArrowG);
        arrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });

        //Here we perform our API Call
        HttpGetRequest getRequest = new HttpGetRequest();
        getRequest.execute("");

    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){

            //Firstly we get the values of the extras
            String searchedTerms = getIntent().getStringExtra("SEARCHED");
            String tag = getIntent().getStringExtra("TAG");

            //Then we adapt the URL in depending of if a toggle button has been clicked
            String urlC = "https://www.googleapis.com/books/v1/volumes?q=" + searchedTerms.replaceAll(" ","%20");
            String post = "";
            switch (tag){
                case "Fiction":
                    post = "Fiction";
                    break;

                case "JFiction":
                    post = "Juvenile Fiction";
                    break;

                case "SScience":
                    post = "Social Science";
                    break;

                case "Philo":
                    post = "Philosophy";
                    break;

                case "History":
                    post = "History";
                    break;

                case "Drama":
                    post = "Drama";
                    break;

            }
            if(!post.equals("")){
                urlC = "https://www.googleapis.com/books/v1/volumes?q=" + searchedTerms.replaceAll(" ","%20") +"+subject:\"" + post + "\"";
            }
            mListView = findViewById(R.id.listView);

            //API Call
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlC, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {


                            try {
                                //We just display the information that we juste received
                                JSONArray jsonArray = response.getJSONArray("items");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject livre = jsonArray.getJSONObject(i);
                                    JSONObject info = livre.getJSONObject("volumeInfo");
                                    String titre = info.getString("title");

                                    //For each that could be empty we verify if the returned JSON contain a value for those fields
                                    String auteur = "No author";
                                    if (info.has("authors")) {
                                        JSONArray auteurArray = info.getJSONArray("authors");
                                        auteur = (String) auteurArray.get(0);
                                    }
                                    String genre = "No genre";
                                    if (info.has("categories")) {
                                        JSONArray genreArray = info.getJSONArray("categories");
                                        genre = (String)genreArray.get(0);
                                    }
                                    String date = "No publication date";
                                    if (info.has("publishedDate")){
                                        date = info.getString("publishedDate");
                                    }
                                    String idL = "";
                                    if (livre.has("id")){
                                        idL = livre.getString("id");
                                    }
                                    String miniature = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
                                    if (info.has("imageLinks")){
                                        JSONObject images = info.getJSONObject("imageLinks");
                                        miniature = images.getString("thumbnail");
                                    }

                                    //And the nwe had it to our ArrayList of Livre
                                    Livre livreLoop = new Livre(titre,auteur,genre,date,miniature);
                                    livreArrayList.add(livreLoop);
                                    idLivre.add(idL);
                                }
                                LivreAdapter adapter = new LivreAdapter(ResultList.this, livreArrayList);
                                mListView.setAdapter(adapter);
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

            //Here we create event listener on each element of our listview to launch detail activity on click
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Livre item = LivreAdapter.getItemC(livreArrayList,position);
                    String titre = item.getTitre();
                    String idLivreClicked = idLivre.get(position);

                    //We create extras to perform our API Call
                    Intent intent = new Intent(getBaseContext(), Details.class);
                    intent.putExtra("TITRE", titre);
                    intent.putExtra("ID", idLivreClicked);
                    intent.putExtra("FROM","result");
                    //based on item add info to intent
                    startActivity(intent);
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
