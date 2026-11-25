package com.bengkel.Class;

public class Mekanik {
    private String mekanikID;
    private String nama;

    public Mekanik(String id, String nama) {
        this.mekanikID = id;this.nama = nama;
    }
    public String getMekanikID() {return mekanikID;}
    public String getNama() {return nama;}
}
