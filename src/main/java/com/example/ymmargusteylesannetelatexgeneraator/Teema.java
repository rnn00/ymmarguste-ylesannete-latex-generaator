package com.example.ymmargusteylesannetelatexgeneraator;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;
public class Teema {
    private final String nimi;
    private List<Ülesanne> arvutusÜlesanded = new ArrayList<>();
    private List<Ülesanne> tõestusÜlesanded = new ArrayList<>();
    private boolean valitud = false;

    public Teema(String nimi) {
        this.nimi = nimi;
    }

    public void lisaÜlesanne(String ylNimi, String sisu, String tüüp) {
        if (tüüp.equals("t")) tõestusÜlesanded.add(new Ülesanne(ylNimi, sisu));
        else arvutusÜlesanded.add(new Ülesanne(ylNimi, sisu));
    }

    public List<Ülesanne> tagastaSuvalisedÜlesanded(int ülesanneteArv, String tüüp) {
        List<Ülesanne> tagastatavadÜlesanded = new ArrayList<>();
        if (tüüp.equals("t")) {
            shuffle(tõestusÜlesanded);
            for (int i = 0; i < ülesanneteArv; i++) {
                tagastatavadÜlesanded.add(tõestusÜlesanded.get(i));
            }
        } else {
            shuffle(arvutusÜlesanded);
            for (int i = 0; i < ülesanneteArv; i++) {
                tagastatavadÜlesanded.add(arvutusÜlesanded.get(i));
            }
        }
        return tagastatavadÜlesanded;
    }

    public String getNimi() {
        return nimi;
    }

    public List<Ülesanne> getArvutusÜlesanded() {
        return arvutusÜlesanded;
    }

    public List<Ülesanne> getTõestusÜlesanded() {
        return tõestusÜlesanded;
    }

    @Override
    public String toString() {
        return this.nimi;
    }

    public String toString2() {
        return String.format("'%s', sisaldab %s tõestusülesannet ja %s arvutusülesannet.", nimi, tõestusÜlesanded.size(), arvutusÜlesanded.size());
    }



    public void setValitud(boolean b) {
        this.valitud = b;
    }
}
