package com.mycompany.sushilaww;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaksi implements Serializable {
    // Static (Pert. 4): dimiliki class, dipakai bersama semua objek
    public static int totalTransaksi = 0;

    private int nomor;
    private String tanggal;
    // Composition (Pert. 11): DetailTransaksi dibuat di dalam Transaksi
    private ArrayList<DetailTransaksi> daftarDetail = new ArrayList<>();

    // Constructor overloading (Pert. 4)
    public Transaksi() {
        this.tanggal = LocalDate.now().toString();
    }

    public Transaksi(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getNomor() {
        return this.nomor;
    }

    // Nomor baru dikasih waktu transaksi berhasil disimpan
    public void setNomor(int nomor) {
        this.nomor = nomor;
    }

    public String getTanggal() {
        return this.tanggal;
    }

    public ArrayList<DetailTransaksi> getDaftarDetail() {
        return this.daftarDetail;
    }

    public void tambahPesanan(Menu menu, int jumlah) {
        daftarDetail.add(new DetailTransaksi(menu, jumlah));
    }

    public void hapusPesananTerakhir() {
        if (!daftarDetail.isEmpty()) {
            daftarDetail.remove(daftarDetail.size() - 1);
        }
    }

    // Menghitung total harga seluruh pesanan
    public double getTotal() {
        double total = 0;
        for (DetailTransaksi d : daftarDetail) {
            total += d.getSubtotal();
        }
        return total;
    }

    // Menghitung total bahan yang dibutuhkan semua pesanan
    // HashMap (Pert. 10): key = kode bahan, value = jumlah yang dibutuhkan
    // contoh 2 Salmon Roll + 1 Tuna Roll -> nasi 240, nori 3, salmon 60, tuna 30
    public HashMap<String, Double> hitungKebutuhanBahan() {
        HashMap<String, Double> kebutuhan = new HashMap<>();
        for (DetailTransaksi d : daftarDetail) {
            for (ItemResep item : d.getMenu().getResep()) {
                String kode = item.getBahan().getKode();
                double butuh = item.getTakaranPerPorsi() * d.getJumlah();
                if (kebutuhan.containsKey(kode)) {
                    butuh = butuh + kebutuhan.get(kode);
                }
                kebutuhan.put(kode, butuh);
            }
        }
        return kebutuhan;
    }

    public String getTeksPesanan() {
        String teks = "";
        for (int i = 0; i < daftarDetail.size(); i++) {
            DetailTransaksi d = daftarDetail.get(i);
            teks = teks + String.format("%d. %-16s x%-3d Rp %,.0f%n", i + 1,
                    d.getMenu().getNama(), d.getJumlah(), d.getSubtotal());
        }
        teks = teks + String.format("TOTAL: Rp %,.0f", getTotal());
        return teks;
    }

    public String getTeksStruk() {
        return "===== STRUK TRANSAKSI #" + nomor + " (" + tanggal + ") =====\n" + getTeksPesanan();
    }

    public void cetakStruk() {
        System.out.println("\n" + getTeksStruk());
        System.out.println("========================================");
    }
}
