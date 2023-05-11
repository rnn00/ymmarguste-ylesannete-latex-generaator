package com.example.ymmargusteylesannetelatexgeneraator;

public record Ülesanne(String nimi, String sisu) {

    @Override
    public String toString() {
        return this.nimi + ": " + this.sisu;
    }
}