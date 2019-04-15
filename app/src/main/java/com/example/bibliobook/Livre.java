package com.example.bibliobook;

/*

    Object representing the information to display in a result list (title, author, type of book, year of publication and an url to the image)
    It also contains getters and setters

 */

public class Livre {
    private String titre;
    private String auteur;
    private String genre;
    private String annee;
    private String url;

    public Livre(String titre, String auteur, String genre, String annee, String url) {
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.annee = annee;
        this.url = url;
    }

    public Livre (Livre l){
        this.titre = l.titre;
        this.auteur = l.auteur;
        this.genre = l.genre;
        this.annee = l.annee;
        this.url = l.url;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
