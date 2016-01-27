package com.cilic.zlatan.mylib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.*;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.cilic.zlatan.mylib.MESSAGE";
    public final static String ISBN_EXTRA = "com.cilic.zlatan.mylib.ISBN";

    //ArrayList<Knjiga> primjerListe = new ArrayList<Knjiga>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Knjiga k1 = new Knjiga();
        k1.setNaziv("Hamina");
        k1.setAutor("hamo");
        k1.setIsbn("dksldksdl");
        k1.setDatumObjave("saddsad");
        k1.setBrojStranica(300);
        k1.setOpis("odlicna");
        k1.setStatus("procitana");

        Knjiga k2 = new Knjiga();
        k2.setNaziv("Kemina");
        k2.setAutor("kemo");
        k2.setIsbn("dksldksdl");
        k2.setDatumObjave("saddsad");
        k2.setBrojStranica(300);
        k2.setOpis("odlicna");
        k2.setStatus("procitana");

        Knjiga k3 = new Knjiga();
        k3.setNaziv("Mirsina");
        k3.setAutor("Mirso");
        k3.setIsbn("dksldksdl");
        k3.setDatumObjave("saddsad");
        k3.setBrojStranica(300);
        k3.setOpis("odlicna");
        k3.setStatus("procitana");*/

        DBHelper db = new DBHelper(this);
/*
        Log.d("Insert:", "Inserting...");
        db.dodajKnjigu(k1);
        db.dodajKnjigu(k2);
        db.dodajKnjigu(k3);*/

        Log.d("Reading:", "Reading all contacts...");
        //List<Knjiga> sveKnjige = db.vratiSveKnjige();
        //List<Knjiga> jednaKnjiga = new ArrayList<Knjiga>();
        //Knjiga k = db.dajKnjigu(2);
        //k.setNaziv("Kemina noA");
        //db.promjeniKnjigu(k);
        //db.obrisiKnjigu(k);
        List<Knjiga> primjerListe =  db.vratiSveKnjige();

        /*for(Knjiga k1 : sveKnjige) {
            primjerListe.add(k1.getNaziv());
        }*/
        //primjerListe.add(String.valueOf(db.dajBrojKnjiga()));
        ListView lw = (ListView) findViewById(R.id.book_list);
        ArrayAdapter<Knjiga> adapter = new ArrayAdapter<Knjiga>(this, android.R.layout.simple_list_item_1, primjerListe);
        lw.setAdapter(adapter);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Knjiga k = (Knjiga) parent.getItemAtPosition(position);
                Integer idKnjige = k.getId();
                Intent intent = new Intent(MainActivity.this, ChangeEraseBook.class);
                String message = String.valueOf(idKnjige);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            openAddActivity();
            return true;
        }

        if(id == R.id.action_search) {
            openSearchActivity();
            return true;
        }

        if(id == R.id.action_scan) {
            openScanActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openAddActivity() {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    public void openSearchActivity() {
        Intent intent = new Intent(this, SearchBookActivity.class);
        startActivity(intent);
    }

    public void openScanActivity() {
        IntentIntegrator.initiateScan(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Parsing bar code reader result
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                    String message = String.valueOf(result.getContents());

                    Intent intent_isbn = new Intent(MainActivity.this, AddBookActivity.class);
                    intent_isbn.putExtra(ISBN_EXTRA, message);
                    startActivity(intent_isbn);

                }
                break;
        }
    }
}
