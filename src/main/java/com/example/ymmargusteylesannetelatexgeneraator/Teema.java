package com.example.ymmargusteylesannetelatexgeneraator;

import java.util.ArrayList;
import java.util.List;

public class Teema {
    private final String nimi;
    private final List<Ülesanne> arvutusÜlesanded = new ArrayList<>();
    private final List<Ülesanne> tõestusÜlesanded = new ArrayList<>();

    public Teema(String nimi) {
        this.nimi = nimi;
    }

    public void lisaÜlesanne(String ylNimi, String sisu, String tüüp) {
        if (tüüp.equals("t")) tõestusÜlesanded.add(new Ülesanne(ylNimi, sisu));
        else arvutusÜlesanded.add(new Ülesanne(ylNimi, sisu));
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
}