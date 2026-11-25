package com.bengkel.Class.Inventaris;

import com.bengkel.Enums.TipeSparepartMesin;
public class SparepartMesin extends Sparepart {
    private String tipe;
    private int stok;
    public SparepartMesin(TipeSparepartMesin tipe, int stok) {
        super(tipe.toString(), stok);
    }
}
