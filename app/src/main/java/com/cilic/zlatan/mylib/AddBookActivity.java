package com.cilic.zlatan.mylib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class AddBookActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_add_book);

        final String[] str={"Read","Not read","Currently reading"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        Spinner spinDate = (Spinner)findViewById(R.id.spinner);
        spinDate.setAdapter(spinnerArrayAdapter);

        if(intent.getExtras() != null) {
            if (intent.getExtras().containsKey(MainActivity.ISBN_EXTRA)) {
                String message = intent.getStringExtra(MainActivity.ISBN_EXTRA);
                EditText isbnET = (EditText) findViewById(R.id.isbnET);
                isbnET.setText(message);

                if(hasConnection()) {

                        try {
                            String s = new DataFetch().execute("http://isbndb.com/api/v2/json/3DYYMRFS/book/" + message).get();

                            parseAndSet(s);

                        }
                        catch(Exception e) {
                        Log.d("error: ", e.getMessage());
                    }

                }
            }
        }
    }

    private void parseAndSet(String s) {
        try {

            JSONObject response = new JSONObject(s);
            if(!response.has("error")) {
                JSONArray arrayData = response.getJSONArray("data");
                JSONObject allData = arrayData.getJSONObject(0);
                String title = allData.getString("title");

                EditText titleET = (EditText) findViewById(R.id.nazivET);
                titleET.setText(title);

                JSONArray authorArrayData = allData.getJSONArray("author_data");
                JSONObject authorData = authorArrayData.getJSONObject(0);
                String authorName = authorData.getString("name");

                EditText authorET = (EditText) findViewById(R.id.autorET);
                authorET.setText(authorName);

                String description = allData.getString("summary");
                if (description.length() > 500) {
                    description = description.substring(0, 499);
                }

                EditText descriptionET = (EditText) findViewById(R.id.opisET);
                descriptionET.setText(description);


                int day = 1;
                int month = 1;
                int year = 2000;
                String publishingInfo = allData.getString("publisher_text");
                if (publishingInfo != "") {
                    String numberOnly = publishingInfo.replaceAll("[^0-9]", "");
                    if (numberOnly.length() == 4) {
                        year = Integer.parseInt(numberOnly);
                        DatePicker dp = (DatePicker) findViewById(R.id.datumDP);
                        dp.updateDate(year, month - 1, day);
                    }
                }

                String isbn = allData.getString("isbn13");
                EditText isbnET = (EditText) findViewById(R.id.isbnET);
                isbnET.setText(isbn);
            }
            else {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Unfortunately, we cannot find that book.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }


        }
        catch (JSONException j) {
            Log.d("JSON error: ", j.getMessage());
        }
    }

    private class DataFetch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            InputStream io = null;
            String result = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(new HttpGet(urls[0]));
                io = httpResponse.getEntity().getContent();
                if(io != null) {
                    result = inputStreamToString(io);
                }
                else {
                    result = "Error fetching data";
                }
            }
            catch(Exception e) {
                Log.d("IO: ", e.getMessage());
            }
            return result;
        }
    }

    private String inputStreamToString(InputStream io) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(io));
        String line = "";
        String result = "";
        while((line = br.readLine()) != null) {
            result += line;
        }
        io.close();
        return  result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
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

    public void dodajKnjigu(View view) {
        final DBHelper db = new DBHelper(this);
        final Knjiga k = new Knjiga();
        EditText nazivET = (EditText) findViewById(R.id.nazivET);
        EditText autorET = (EditText) findViewById(R.id.autorET);
        EditText isbnET = (EditText) findViewById(R.id.isbnET);
        EditText opisET = (EditText) findViewById(R.id.opisET);
        Spinner statusET = (Spinner) findViewById(R.id.spinner);
        EditText brojStrET = (EditText) findViewById(R.id.brojStrET);
        DatePicker datumdP = (DatePicker) findViewById(R.id.datumDP);

        String regexEmpty = "";

        Boolean validno = true;
        if(nazivET.getText().toString().equals(regexEmpty)) {
            nazivET.setError("Enter the title");
            validno = false;
        }
        else {
            nazivET.setError(null);
        }

        if(autorET.getText().toString().equals(regexEmpty)) {
            autorET.setError("Enter the author");
            validno = false;
        }
        else {
            autorET.setError(null);
        }

        if(isbnET.getText().toString().equals(regexEmpty)) {
            isbnET.setError("Enter the ISBN");
            validno = false;
        }
        else {
            if(!isValidIsbn(isbnET.getText().toString())) {
                isbnET.setError("Enter valid ISBN");
                validno = false;
            }
            else {
                isbnET.setError(null);
            }
        }

        if(brojStrET.getText().toString().equals(regexEmpty)) {
            brojStrET.setError("Enter number of pages");
            validno = false;
        }
        else {
            brojStrET.setError(null);
        }



        if(validno) {

            k.setNaziv(nazivET.getText().toString());
            k.setAutor(autorET.getText().toString());
            k.setIsbn(isbnET.getText().toString());
            k.setOpis(opisET.getText().toString());
            k.setStatus(statusET.getSelectedItem().toString());
            k.setBrojStranica(Integer.parseInt(brojStrET.getText().toString()));
            int day = datumdP.getDayOfMonth();
            int month = datumdP.getMonth() + 1;
            int year = datumdP.getYear();
            k.setDatumObjave(day + "." + month + "." + year);
            final com.cilic.zlatan.mylib.AddBookActivity vanjski = this;

            new AlertDialog.Builder(this)
                    .setTitle("Add book?")
                    .setMessage("Are you sure you want to add this book?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.dodajKnjigu(k);
                            NavUtils.navigateUpFromSameTask(vanjski);
                        }
                    })
                    .setIcon(R.drawable.ic_action_done)
                    .show();
        }
    }

    private Boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile("(\\d-?){13}");
        Matcher matcher = pattern.matcher(isbn);
        return matcher.matches();
    }

    private Boolean hasConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
