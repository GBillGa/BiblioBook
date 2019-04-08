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

public class LivreAdapter extends ArrayAdapter<Livre> {

    //tweets est la liste des models à afficher
    public LivreAdapter(Context context, List<Livre> livres) {
        super(context, 0, livres);
    }

    public static Livre getItemC(List<Livre> livres,int position){
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
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.auteur = (TextView) convertView.findViewById(R.id.auteur);
            viewHolder.genre = (TextView) convertView.findViewById(R.id.genre);
            viewHolder.annee = (TextView) convertView.findViewById(R.id.annee);
            viewHolder.couverture = (ImageView) convertView.findViewById(R.id.couverture);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Livre livre = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.titre.setText(livre.getTitre());
        viewHolder.annee.setText(livre.getAnnee());
        viewHolder.auteur.setText(livre.getAuteur());
        viewHolder.genre.setText(livre.getGenre());
        if (livre.getUrl() != null){
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

