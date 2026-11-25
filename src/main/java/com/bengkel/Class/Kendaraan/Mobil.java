package com.bengkel.Class.Kendaraan;

import java.util.ArrayList;
import java.util.List;

public class Mobil extends Kendaraan {

    private String tahun;
    private String model;
    private String merk;

    public Mobil(String Pemilik_ID, String tahun, String model, String merk) {
        super(Pemilik_ID);
        this.tahun = tahun;
        this.model = model;
        this.merk = merk;
    }

    @Override 
    public void Servis() {
        System.out.println("diservis");
    }
}