package com.bengkel.Class;

public abstract class Kendaraan {
    private String platNum;
    private String pemilik;

    protected Kendaraan(String platNum, String namaPemilik) {
        this.platNum = platNum;
        this.pemilik = namaPemilik;
    }

    public String getPlatNomor() { return platNum; }
    public String getPemilik() { return pemilik; }

    public abstract void Servis();
}
