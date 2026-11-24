package com.mycompany.sushilaww;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

// Menyimpan semua transaksi dan membuat rekap pendapatan
// Aggregation (Pert. 11): objek Transaksi dibuat di luar lalu dimasukkan ke sini
public class LaporanPenjualan implements Serializable {
    private ArrayList<Transaksi> daftarTransaksi = new ArrayList<>();

    public void tambahTransaksi(Transaksi t) {
        Transaksi.totalTransaksi++;
        t.setNomor(Transaksi.totalTransaksi);
        daftarTransaksi.add(t);
    }

    public ArrayList<Transaksi> getDaftarTransaksi() {
        return this.daftarTransaksi;
    }

    public double getTotalPendapatan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            total += t.getTotal();
        }
        return total;
    }

    // Ambil tanggal-tanggal yang ada transaksinya (tidak dobel)
    public ArrayList<String> getDaftarTanggal() {
        ArrayList<String> daftarTanggal = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (!daftarTanggal.contains(t.getTanggal())) {
                daftarTanggal.add(t.getTanggal());
            }
        }
        return daftarTanggal;
    }

    // HashMap (Pert. 10): key = tanggal, value = pendapatan di tanggal itu
    public HashMap<String, Double> getPendapatanPerTanggal() {
        HashMap<String, Double> rekap = new HashMap<>();
        for (Transaksi t : daftarTransaksi) {
            double total = t.getTotal();
            if (rekap.containsKey(t.getTanggal())) {
                total = total + rekap.get(t.getTanggal());
            }
            rekap.put(t.getTanggal(), total);
        }
        return rekap;
    }

    public String buatLaporanHarian(String tanggal) {
        String teks = "===== LAPORAN PENJUALAN " + tanggal + " =====\n";
        int jumlah = 0;
        double pendapatan = 0;
        // key = nama menu, value = jumlah porsi terjual (wrapper class Integer)
        HashMap<String, Integer> porsiTerjual = new HashMap<>();

        for (Transaksi t : daftarTransaksi) {
            if (!t.getTanggal().equals(tanggal)) {
                continue;
            }
            jumlah++;
            pendapatan += t.getTotal();
            teks = teks + String.format("#%d | Rp %,.0f%n", t.getNomor(), t.getTotal());

            for (DetailTransaksi d : t.getDaftarDetail()) {
                String nama = d.getMenu().getNama();
                int porsi = d.getJumlah();
                if (porsiTerjual.containsKey(nama)) {
                    porsi = porsi + porsiTerjual.get(nama);
                }
                porsiTerjual.put(nama, porsi);
            }
        }

        teks = teks + "\nMenu terjual:\n";
        for (String nama : porsiTerjual.keySet()) {
            teks = teks + "- " + nama + ": " + porsiTerjual.get(nama) + " porsi\n";
        }
        teks = teks + "\nJumlah transaksi : " + jumlah + "\n";
        teks = teks + String.format("Total pendapatan : Rp %,.0f%n", pendapatan);
        return teks;
    }

    public String buatRekapSemua() {
        String teks = "===== REKAP SEMUA TANGGAL =====\n";
        HashMap<String, Double> rekap = getPendapatanPerTanggal();
        for (String tanggal : getDaftarTanggal()) {
            teks = teks + String.format("%s | Rp %,.0f%n", tanggal, rekap.get(tanggal));
        }
        teks = teks + "\nJumlah transaksi : " + daftarTransaksi.size() + "\n";
        teks = teks + String.format("Total pendapatan : Rp %,.0f%n", getTotalPendapatan());
        return teks;
    }

    public void tampilkanLaporan() {
        System.out.println("\n--- LAPORAN PENJUALAN ---");
        if (daftarTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }
        System.out.println(buatRekapSemua());
    }
}
