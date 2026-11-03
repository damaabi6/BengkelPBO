package com.bengkel.Class;

public class Motor extends Kendaraan {

    public Motor(String platNum, String namaPemilik) {
        super(platNum, namaPemilik);
    }

    @Override 
    public void Servis() {
        System.out.println("diservis");
    }
}