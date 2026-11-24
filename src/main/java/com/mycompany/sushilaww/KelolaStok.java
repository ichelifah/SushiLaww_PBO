package com.mycompany.sushilaww;

// INTERFACE (Pert. 9): kontrak untuk benda yang punya stok
public interface KelolaStok {
    // Konstanta interface (otomatis public static final)
    double STOK_MINIMUM_DEFAULT = 100;

    void tambahStok(double jumlah);

    void kurangiStok(double jumlah) throws StokTidakCukupException;

    boolean isStokMenipis();
}
