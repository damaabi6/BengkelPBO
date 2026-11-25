package com.bengkel.Class.Kendaraan;

import com.bengkel.Class.Inventaris.Sparepart;

import java.util.ArrayList;
import java.util.List;

public abstract class Kendaraan {
    private String pemilikID;
    private List<String> SparepartDibutuhkan = new ArrayList<>();

    protected Kendaraan(String Pemilik_ID) {
        this.pemilikID = Pemilik_ID;
    }

    public String getPemilikID() { return pemilikID; }

    public void AddNeededSparepart(Sparepart[] listTipe) {
        for (Sparepart tipe : listTipe) {
            SparepartDibutuhkan.add(tipe.toString());
        }
    }

    public abstract void Servis();
}
