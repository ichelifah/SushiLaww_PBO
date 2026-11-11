package com.mycompany.sushilaww;

import java.util.ArrayList;

public class Transaksi {
    // Static (Pert. 4): dimiliki class, dipakai bersama semua objek
    public static int totalTransaksi = 0;

    private int nomor;
    private String tanggal;
    private ArrayList<DetailTransaksi> daftarDetail = new ArrayList<>();

    public Transaksi(String tanggal) {
        totalTransaksi++;
        this.nomor = totalTransaksi;
        this.tanggal = tanggal;
    }

    public int getNomor() {
        return this.nomor;
    }

    public String getTanggal() {
        return this.tanggal;
    }

    public ArrayList<DetailTransaksi> getDaftarDetail() {
        return this.daftarDetail;
    }

    public void tambahDetail(DetailTransaksi detail) {
        daftarDetail.add(detail);
    }

    // Menghitung total harga seluruh pesanan
    public double getTotal() {
        double total = 0;
        for (DetailTransaksi d : daftarDetail) {
            total += d.getSubtotal();
        }
        return total;
    }

    public void cetakStruk() {
        System.out.println("\n===== STRUK TRANSAKSI #" + nomor + " (" + tanggal + ") =====");
        for (DetailTransaksi d : daftarDetail) {
            System.out.printf("%-18s x%-3d Rp %,.0f%n",
                    d.getMenu().getNama(), d.getJumlah(), d.getSubtotal());
        }
        System.out.printf("TOTAL: Rp %,.0f%n", getTotal());
        System.out.println("========================================");
    }
}
