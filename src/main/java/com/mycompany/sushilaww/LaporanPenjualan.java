package com.mycompany.sushilaww;

import java.util.ArrayList;

// Menyimpan semua transaksi dan membuat rekap pendapatan
public class LaporanPenjualan {
    private ArrayList<Transaksi> daftarTransaksi = new ArrayList<>();

    public void tambahTransaksi(Transaksi t) {
        daftarTransaksi.add(t);
    }

    public void tampilkanLaporan() {
        System.out.println("\n--- LAPORAN PENJUALAN ---");
        if (daftarTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }
        double totalPendapatan = 0;
        for (int i = 0; i < daftarTransaksi.size(); i++) {
            Transaksi t = daftarTransaksi.get(i);
            System.out.printf("#%d | %s | Rp %,.0f%n", t.getNomor(), t.getTanggal(), t.getTotal());
            totalPendapatan += t.getTotal();
        }
        System.out.printf("Jumlah transaksi : %d%n", Transaksi.totalTransaksi);
        System.out.printf("Total pendapatan : Rp %,.0f%n", totalPendapatan);
    }
}
