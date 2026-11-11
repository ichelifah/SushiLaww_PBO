package com.mycompany.sushilaww;

// SUBCLASS dari Produk (Pert. 6)
public class Menu extends Produk {
    private double harga;
    private BahanBaku bahan;        // bahan baku utama menu ini
    private double bahanPerPorsi;   // jumlah bahan yang terpakai tiap 1 porsi

    public Menu(String kode, String nama, double harga, BahanBaku bahan, double bahanPerPorsi) {
        super(kode, nama);
        setHarga(harga);
        this.bahan = bahan;
        this.bahanPerPorsi = bahanPerPorsi;
    }

    public double getHarga() {
        return this.harga;
    }

    // Validasi: harga harus lebih dari 0
    public void setHarga(double harga) {
        if (harga > 0) {
            this.harga = harga;
        } else {
            System.out.println("Harga tidak valid! Diatur ke 10000.");
            this.harga = 10000;
        }
    }

    public BahanBaku getBahan() {
        return this.bahan;
    }

    public double getBahanPerPorsi() {
        return this.bahanPerPorsi;
    }

    // Method Overriding
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf(" | Rp %,.0f | Bahan: %s (%.0f %s/porsi)%n",
                harga, bahan.getNama(), bahanPerPorsi, bahan.getSatuan());
    }
}
