package com.example.ymmargusteylesannetelatexgeneraator;

public record Ülesanne(String nimi, String sisu) {

    @Override
    public String toString() {
        return String.format("%s: %s", this.nimi, this.sisu);
    }
}
