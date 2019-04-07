package com.example.bibliobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.List;

public class ResultList extends AppCompatActivity {

    private RequestQueue queue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        queue = Volley.newRequestQueue(this);
        final TextView text = (TextView) findViewById(R.id.TopBarSearchText);
        String searchedTerms = getIntent().getStringExtra("SEARCHED");
        text.setText(searchedTerms);
        ImageView imageview1 = findViewById(R.id.returnToSearchArrow);
        imageview1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                finish();

            }

        });
        //Log.e("JFL", "ici");
        //final TextView textCenter = (TextView) findViewById(R.id.textView3);
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + searchedTerms;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
          //              Log.e("JFL", "Reponse");

                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            TextView deb = (TextView) findViewById(R.id.debugLog);
                            deb.setText("");
            //                Log.e("JFL", "jsonArray:" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject livre = jsonArray.getJSONObject(i);
                                JSONObject info = livre.getJSONObject("volumeInfo");
                                String titre = info.getString("title");
                                Log.i("JFL", "info:" + info);
                                String auteur = "No author";
                                if (info.has("authors")) {
                                    JSONArray auteurArray = info.getJSONArray("authors");
                                    auteur = (String) auteurArray.get(0);
                                }
                                JSONObject images = info.getJSONObject("imageLinks");
                                String miniature = images.getString("thumbnail");
                                deb.append(titre + " de " + auteur +"\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //textCenter.setText("JSON Parsing Failed");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //textCenter.setText("Erreur lors de l'appel API");
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);

    }


}
