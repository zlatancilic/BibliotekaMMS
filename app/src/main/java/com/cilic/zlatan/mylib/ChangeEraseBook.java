package com.cilic.zlatan.mylib;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChangeEraseBook extends ActionBarActivity {
    Knjiga k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_erase_book);

        final String[] str={"Read","Not read","Currently reading"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinDate = (Spinner)findViewById(R.id.spinner_chg);
        spinDate.setAdapter(spinnerArrayAdapter);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        DBHelper db = new DBHelper(this);
        k = db.dajKnjigu(Integer.parseInt(message));

        EditText nazivET = (EditText) findViewById(R.id.nazivET_chg);
        EditText autorET = (EditText) findViewById(R.id.autorET_chg);
        EditText isbnET = (EditText) findViewById(R.id.isbnET_chg);
        EditText opisET = (EditText) findViewById(R.id.opisET_chg);
        Spinner statusET = (Spinner) findViewById(R.id.spinner_chg);
        EditText brojStrET = (EditText) findViewById(R.id.brojStrET_chg);
        DatePicker datumDP = (DatePicker) findViewById(R.id.datumDP_chg);

        nazivET.setText(k.getNaziv());
        autorET.setText(k.getAutor());
        isbnET.setText(k.getIsbn());
        opisET.setText(k.getOpis());
        brojStrET.setText(String.valueOf(k.getBrojStranica()));

        String status = k.getStatus();
        //postaviStatus(statusET, status1);
        if(status.equals("Read"))
            statusET.setSelection(0);
        else if(status.equals("Not read"))
            statusET.setSelection(1);
        else
            statusET.setSelection(2);


        String datum = k.getDatumObjave();
        postaviDatum(datumDP, datum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_erase_book, menu);
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

    private void postaviDatum(DatePicker dp, String datum) {
        if(datum.charAt(1) == '.') {
            if(datum.charAt(3) == '.') {
                //1.1.2001
                Integer dan = Integer.parseInt(datum.substring(0, 1));
                Integer mjesec = Integer.parseInt(datum.substring(2, 3));
                Integer godina = Integer.parseInt(datum.substring(4));
                dp.updateDate(godina, mjesec - 1, dan);
            }
            else {
                //1.12.2001
                Integer dan = Integer.parseInt(datum.substring(0, 1));
                Integer mjesec = Integer.parseInt(datum.substring(2, 4));
                Integer godina = Integer.parseInt(datum.substring(5));
                dp.updateDate(godina, mjesec - 1, dan);
            }
        }
        else {
            if(datum.charAt(4) == '.') {
                //15.1.2001
                Integer dan = Integer.parseInt(datum.substring(0, 2));
                Integer mjesec = Integer.parseInt(datum.substring(3, 4));
                Integer godina = Integer.parseInt(datum.substring(5));
                dp.updateDate(godina, mjesec - 1, dan);
            }
            else {
                //15.11.2001
                Integer dan = Integer.parseInt(datum.substring(0, 2));
                Integer mjesec = Integer.parseInt(datum.substring(3, 5));
                Integer godina = Integer.parseInt(datum.substring(6));
                dp.updateDate(godina, mjesec - 1, dan);
            }
        }
    }

    private void postaviStatus(Spinner s, String status) {
        switch (status) {
            case "Read":
                s.setSelection(0);
            case "Not read":
                s.setSelection(1);
            case "Currently reading":
                s.setSelection(2);
            default:
                s.setSelection(0);
        }
    }

    public void obrisiKnjigu(View view) {
        final DBHelper db = new DBHelper(this);
        final com.cilic.zlatan.mylib.ChangeEraseBook vanjski = this;

            new AlertDialog.Builder(this)
                    .setTitle("Erase book?")
                    .setMessage("Are you sure you want to erase this book?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.obrisiKnjigu(k);
                            NavUtils.navigateUpFromSameTask(vanjski);
                        }
                    })
                    .setIcon(R.drawable.ic_action_delete)
                    .show();

    }

    public void promijeniKnjigu(View view) {
        final DBHelper db = new DBHelper(this);
        final Knjiga k1 = k;
        EditText nazivET = (EditText) findViewById(R.id.nazivET_chg);
        EditText autorET = (EditText) findViewById(R.id.autorET_chg);
        EditText isbnET = (EditText) findViewById(R.id.isbnET_chg);
        EditText opisET = (EditText) findViewById(R.id.opisET_chg);
        Spinner statusET = (Spinner) findViewById(R.id.spinner_chg);
        EditText brojStrET = (EditText) findViewById(R.id.brojStrET_chg);
        DatePicker datumdP = (DatePicker) findViewById(R.id.datumDP_chg);

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

            k1.setNaziv(nazivET.getText().toString());
            k1.setAutor(autorET.getText().toString());
            k1.setIsbn(isbnET.getText().toString());
            k1.setOpis(opisET.getText().toString());
            k1.setStatus(statusET.getSelectedItem().toString());
            k1.setBrojStranica(Integer.parseInt(brojStrET.getText().toString()));
            int day = datumdP.getDayOfMonth();
            int month = datumdP.getMonth() + 1;
            int year = datumdP.getYear();
            k1.setDatumObjave(day + "." + month + "." + year);
            final com.cilic.zlatan.mylib.ChangeEraseBook vanjski = this;

            new AlertDialog.Builder(this)
                    .setTitle("Change book?")
                    .setMessage("Are you sure you want to change this book?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.promjeniKnjigu(k1);
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


}
