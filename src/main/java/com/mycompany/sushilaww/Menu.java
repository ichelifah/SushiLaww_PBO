package com.mycompany.sushilaww;

import java.util.ArrayList;

// SUBCLASS dari Produk (Pert. 6)
public class Menu extends Produk {
    private double harga;
    // Composition (Pert. 11): ItemResep dibuat di dalam class Menu
    private ArrayList<ItemResep> resep = new ArrayList<>();

    public Menu(String kode, String nama, double harga) {
        super(kode, nama);
        setHarga(harga);
    }

    public double getHarga() {
        return this.harga;
    }

    // Validasi: harga harus lebih dari 0
    public void setHarga(double harga) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0!");
        }
        this.harga = harga;
    }

    public void tambahBahan(BahanBaku bahan, double takaranPerPorsi) {
        resep.add(new ItemResep(bahan, takaranPerPorsi));
    }

    public ArrayList<ItemResep> getResep() {
        return this.resep;
    }

    // Cek apakah bahan ini dipakai di resep menu
    public boolean memakaiBahan(BahanBaku bahan) {
        for (ItemResep item : resep) {
            if (item.getBahan() == bahan) {
                return true;
            }
        }
        return false;
    }

    public String getTeksResep() {
        String teks = "";
        for (int i = 0; i < resep.size(); i++) {
            teks = teks + resep.get(i).toString();
            if (i < resep.size() - 1) {
                teks = teks + ", ";
            }
        }
        return teks;
    }

    @Override
    public String getJenis() {
        return "Menu";
    }

    // Method Overriding
    @Override
    public String getInfo() {
        return super.getInfo() + String.format(" | Rp %,.0f | Resep: %s", harga, getTeksResep());
    }
}
