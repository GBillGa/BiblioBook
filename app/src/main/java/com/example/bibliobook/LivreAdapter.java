package com.example.bibliobook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/*

    Here we have our Object Adapter that will be needed to inflate our ListView

 */

public class LivreAdapter extends ArrayAdapter<Livre> {
    public LivreAdapter(Context context, List<Livre> livres) {
        super(context, 0, livres);
    }

    public static Livre getItemC(List<Livre> livres,int position){
        //We just return the wanted object
        return livres.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card,parent, false);
        }

        LivreViewHolder viewHolder = (LivreViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new LivreViewHolder();
            viewHolder.titre = convertView.findViewById(R.id.titre);
            viewHolder.auteur = convertView.findViewById(R.id.auteur);
            viewHolder.genre = convertView.findViewById(R.id.genre);
            viewHolder.annee = convertView.findViewById(R.id.annee);
            viewHolder.couverture = convertView.findViewById(R.id.couverture);
            convertView.setTag(viewHolder);
        }

        //Here we just get the right object to pass data to our view
        Livre livre = getItem(position);

        //Here we just have to fulfill our view
        viewHolder.titre.setText(livre.getTitre());
        viewHolder.annee.setText(livre.getAnnee());
        viewHolder.auteur.setText(livre.getAuteur());
        viewHolder.genre.setText(livre.getGenre());
        if (livre.getUrl() != null){
            //We verify the existence of the URL otherwise Picasso will make the app stop
            Picasso.get().load(livre.getUrl()).into(viewHolder.couverture);
        }

        return convertView;
    }



    class LivreViewHolder{
        public TextView titre;
        public TextView auteur;
        public TextView genre;
        public TextView annee;
        public ImageView couverture;
    }
}

