
package com.mycompany.sushilaww;

import java.util.ArrayList;

public class SushiLawwData {

    public static ArrayList<BahanBaku> daftarBahan = new ArrayList<>();
    public static ArrayList<Menu> daftarMenu = new ArrayList<>();

    public static LaporanPenjualan laporan = new LaporanPenjualan();

    // Mencegah data awal dibuat berulang kali
    public static void inisialisasi() {

    if (!daftarMenu.isEmpty()) {
        return;
    }

    BahanBaku nasi = new BahanBaku(
        "B01", "Nasi Sushi", "gram", 5000
    );

    BahanBaku salmon = new BahanBaku(
        "B02", "Salmon", "gram", 1500
    );

    BahanBaku tuna = new BahanBaku(
        "B03", "Tuna", "gram", 1200
    );

    daftarBahan.add(nasi);
    daftarBahan.add(salmon);
    daftarBahan.add(tuna);

    daftarMenu.add(new Menu("M01", "Salmon Roll", 35000, salmon, 80));
    daftarMenu.add(new Menu("M02", "Tuna Roll", 32000, tuna, 80));
    daftarMenu.add(new Menu("M03", "Nasi Sushi Polos", 12000, nasi, 150));
    }
}
    