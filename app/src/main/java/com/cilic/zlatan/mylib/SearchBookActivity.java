package com.cilic.zlatan.mylib;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SearchBookActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.cilic.zlatan.mylib.MESSAGE";

    List<Knjiga> primjerListe;;
    ArrayAdapter<Knjiga> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        DBHelper db = new DBHelper(this);
        primjerListe = db.vratiSveKnjige();
        ListView lw = (ListView) findViewById(R.id.book_list_search);
        adapter = new ArrayAdapter<Knjiga>(this, android.R.layout.simple_list_item_1, primjerListe);
        lw.setAdapter(adapter);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Knjiga k = (Knjiga) parent.getItemAtPosition(position);
                Integer idKnjige = k.getId();
                Intent intent = new Intent(SearchBookActivity.this, ChangeEraseBook.class);
                String message = String.valueOf(idKnjige);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });


        EditText searchbox = (EditText) findViewById(R.id.searchBoxET);
        searchbox.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                SearchBookActivity.this.adapter.getFilter().filter(s);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
