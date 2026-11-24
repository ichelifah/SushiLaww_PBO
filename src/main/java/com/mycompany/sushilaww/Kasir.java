package com.mycompany.sushilaww;

import java.util.HashMap;

// Association (Pert. 11): Kasir cuma memakai Transaksi dan LaporanPenjualan lewat parameter,
// tidak disimpan sebagai field
public class Kasir {
    private String nama;

    public Kasir(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return this.nama;
    }

    // Cek stok semua bahan dulu, kalau cukup baru stok dikurangi dan transaksi disimpan.
    // Kalau ada yang kurang, transaksi tidak disimpan dan stok tidak berubah.
    public void simpanTransaksi(Transaksi transaksi, LaporanPenjualan laporan) throws StokTidakCukupException {
        if (transaksi.getDaftarDetail().isEmpty()) {
            throw new IllegalArgumentException("Belum ada pesanan!");
        }

        HashMap<String, Double> kebutuhan = transaksi.hitungKebutuhanBahan();

        // 1. Cek stok
        for (String kode : kebutuhan.keySet()) {
            BahanBaku bahan = SushiLawwData.cariBahan(kode);
            if (kebutuhan.get(kode) > bahan.getJumlahStok()) {
                throw new StokTidakCukupException("Stok " + bahan.getNama() + " tidak cukup!\n"
                        + "Butuh: " + kebutuhan.get(kode) + " " + bahan.getSatuan()
                        + ", sisa: " + bahan.getJumlahStok() + " " + bahan.getSatuan());
            }
        }

        // 2. Kurangi stok
        for (String kode : kebutuhan.keySet()) {
            SushiLawwData.cariBahan(kode).kurangiStok(kebutuhan.get(kode));
        }

        // 3. Simpan transaksi
        laporan.tambahTransaksi(transaksi);
    }
}
