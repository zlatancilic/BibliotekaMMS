package com.cilic.zlatan.mylib;

/**
 * Created by zlatan on 5/30/15.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyLib.db";
    public static final String BOOKS_TABLE_NAME = "Knjiga";
    public static final String BOOKS_COLUMN_ID = "id";
    public static final String BOOKS_COLUMN_TITLE = "naziv";
    public static final String BOOKS_COLUMN_AUTHOR = "autor";
    public static final String BOOKS_COLUMN_ISBN = "isbn";
    public static final String BOOKS_COLUMN_DATE = "datumObjave";
    public static final String BOOKS_COLUMN_NO_OF_PAGES = "brojStranica";
    public static final String BOOKS_COLUMN_DESCRIPTION = "opis";
    public static final String BOOKS_COLUMN_STATUS = "status";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "+ BOOKS_TABLE_NAME + " ("
            + BOOKS_COLUMN_ID + " integer primary key autoincrement, "
            + BOOKS_COLUMN_TITLE + " text, "
            + BOOKS_COLUMN_AUTHOR + " text, "
            + BOOKS_COLUMN_ISBN + " text, "
            + BOOKS_COLUMN_DATE + " text, "
            + BOOKS_COLUMN_NO_OF_PAGES + " integer, "
            + BOOKS_COLUMN_DESCRIPTION + " text, "
            + BOOKS_COLUMN_STATUS + " text);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOKS_TABLE_NAME);
        onCreate(db);
    }

    void dodajKnjigu(Knjiga k) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vrijednosti = new ContentValues();

        vrijednosti.put(BOOKS_COLUMN_TITLE, k.getNaziv());
        vrijednosti.put(BOOKS_COLUMN_AUTHOR, k.getAutor());
        vrijednosti.put(BOOKS_COLUMN_ISBN, k.getIsbn());
        vrijednosti.put(BOOKS_COLUMN_DATE, k.getDatumObjave());
        vrijednosti.put(BOOKS_COLUMN_NO_OF_PAGES, k.getBrojStranica());
        vrijednosti.put(BOOKS_COLUMN_DESCRIPTION, k.getOpis());
        vrijednosti.put(BOOKS_COLUMN_STATUS, k.getStatus());

        db.insert(BOOKS_TABLE_NAME, null, vrijednosti);
    }

    public List<Knjiga> vratiSveKnjige() {
        List<Knjiga> sveKnjige = new ArrayList<Knjiga>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME, null);

        if(cursor.moveToFirst()) {
            do {
                Knjiga k = new Knjiga();
                k.setId(Integer.parseInt(cursor.getString(0)));
                k.setNaziv(cursor.getString(1));
                k.setAutor(cursor.getString(2));
                k.setIsbn(cursor.getString(3));
                k.setDatumObjave(cursor.getString(4));
                k.setBrojStranica(Integer.parseInt(cursor.getString(5)));
                k.setOpis(cursor.getString(6));
                k.setStatus(cursor.getString(7));

                sveKnjige.add(k);
            } while(cursor.moveToNext());
        }
        return sveKnjige;
    }

    Knjiga dajKnjigu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(BOOKS_TABLE_NAME, new String[]{BOOKS_COLUMN_ID,
                        BOOKS_COLUMN_TITLE,
                        BOOKS_COLUMN_AUTHOR,
                        BOOKS_COLUMN_ISBN,
                        BOOKS_COLUMN_DATE,
                        BOOKS_COLUMN_NO_OF_PAGES,
                        BOOKS_COLUMN_DESCRIPTION,
                        BOOKS_COLUMN_STATUS}, BOOKS_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        Knjiga k = new Knjiga();
        k.setId(Integer.parseInt(cursor.getString(0)));
        k.setNaziv(cursor.getString(1));
        k.setAutor(cursor.getString(2));
        k.setIsbn(cursor.getString(3));
        k.setDatumObjave(cursor.getString(4));
        k.setBrojStranica(Integer.parseInt(cursor.getString(5)));
        k.setOpis(cursor.getString(6));
        k.setStatus(cursor.getString(7));

        return k;
    }

    public int dajBrojKnjiga() {
        String upit = "SELECT  * FROM " + BOOKS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(upit, null);

        return cursor.getCount();
    }

    public int promjeniKnjigu(Knjiga k) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vrijednosti = new ContentValues();

        vrijednosti.put(BOOKS_COLUMN_TITLE, k.getNaziv());
        vrijednosti.put(BOOKS_COLUMN_AUTHOR, k.getAutor());
        vrijednosti.put(BOOKS_COLUMN_ISBN, k.getIsbn());
        vrijednosti.put(BOOKS_COLUMN_DATE, k.getDatumObjave());
        vrijednosti.put(BOOKS_COLUMN_NO_OF_PAGES, k.getBrojStranica());
        vrijednosti.put(BOOKS_COLUMN_DESCRIPTION, k.getOpis());
        vrijednosti.put(BOOKS_COLUMN_STATUS, k.getStatus());

        return db.update(BOOKS_TABLE_NAME, vrijednosti, BOOKS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(k.getId())});
    }

    public void obrisiKnjigu(Knjiga k) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BOOKS_TABLE_NAME, BOOKS_COLUMN_ID + " = ?",
                new String[] { String.valueOf(k.getId()) });
    }
}
