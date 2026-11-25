package com.bengkel.Class.Inventaris;

import com.bengkel.Enums.TipeSparepartUmum;
public class SparepartUmum extends Sparepart {
    private String tipe;
    private int stok;

    public SparepartUmum(TipeSparepartUmum tipe, int stok) {
        super(tipe.toString(), stok);
    }
}
