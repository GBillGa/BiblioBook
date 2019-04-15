package com.example.bibliobook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*

        Database Helper to store the books in a SQLite local db
        It stores the information that need to be displayed when we want to see our favorites Listview ( title , author , literary kind, publication adate and URL)

 */

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "book_table";      //Name of our table
    private static final String COL1 = "ID";                    //Names of our columns
    private static final String COL2 = "Titre";
    private static final String COL3 = "Auteur";
    private static final String COL4 = "Genre";
    private static final String COL5 = "Annee";
    private static final String COL6 = "URL";
    private static final String COL7 = "GID";

    public DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //Database creation using previous declared names
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " TEXT)";
        Log.e("ERROR", createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Boolean addData(String titre, String auteur, String genre, String annee, String url, String gid){
        SQLiteDatabase db = this.getWritableDatabase();

        //Here we just add our passed data to the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,titre);
        contentValues.put(COL3,auteur);
        contentValues.put(COL4,genre);
        contentValues.put(COL5,annee);
        contentValues.put(COL6,url);
        contentValues.put(COL7,gid);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        //This function only return the whole database (used to inflate the Favorites ListView
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public boolean exists(String id){
        //This function is used to tell if a book is in the favorites database or not (to show the full red heart or the empty one in details activity)
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql ="SELECT * FROM book_table WHERE GID = '" + id + "'";
        Log.d("ERROR","String : " + sql);
        cursor= db.rawQuery(sql,null);
        Log.d("ERROR","Cursor Count : " + cursor.getCount());

        if(cursor.getCount()>0){ //Here we have element so the book is in the database
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public boolean deleteData(String name)
    {
        //This function is used to remove a book from the favorites database
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL7 + " = '" + name + "'", null);
        if (result == -1){
            return false;
        } else {
            return true;
        }
    }
}
