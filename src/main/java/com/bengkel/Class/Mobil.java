package com.bengkel.Class;

public class Mobil extends Kendaraan {

    public Mobil(String platNum, String namaPemilik) {
        super(platNum, namaPemilik);
    }

    @Override 
    public void Servis() {
        System.out.println("diservis");
    }
}