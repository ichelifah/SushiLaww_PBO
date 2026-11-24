package com.mycompany.sushilaww;

import java.io.Serializable;

// Satu bahan di dalam resep menu, contoh: Salmon 30 gram per porsi
public class ItemResep implements Serializable {
    private BahanBaku bahan; // Aggregation (Pert. 11): BahanBaku dibuat di luar
    private double takaranPerPorsi;

    public ItemResep(BahanBaku bahan, double takaranPerPorsi) {
        this.bahan = bahan;
        this.takaranPerPorsi = takaranPerPorsi;
    }

    public BahanBaku getBahan() {
        return this.bahan;
    }

    public double getTakaranPerPorsi() {
        return this.takaranPerPorsi;
    }

    @Override
    public String toString() {
        return bahan.getNama() + " " + takaranPerPorsi + " " + bahan.getSatuan();
    }
}
