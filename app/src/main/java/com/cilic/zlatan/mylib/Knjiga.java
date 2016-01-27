package com.cilic.zlatan.mylib;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by zlatan on 5/30/15.
 */
public class Knjiga {
    private int id;
    private String naziv;
    private String autor;
    private String isbn;
    //private Bitmap slika;
    private String datumObjave;
    private int brojStranica;
    private String opis;
    private String status;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDatumObjave() {
        return datumObjave;
    }

    public void setDatumObjave(String datumObjave) {
        this.datumObjave = datumObjave;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
