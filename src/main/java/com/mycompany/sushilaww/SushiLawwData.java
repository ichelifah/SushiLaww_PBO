package com.mycompany.sushilaww;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

// Tempat menyimpan data yang dipakai bersama oleh GUI dan console
public class SushiLawwData {

    public static ArrayList<BahanBaku> daftarBahan = new ArrayList<>();
    public static ArrayList<Menu> daftarMenu = new ArrayList<>();
    public static LaporanPenjualan laporan = new LaporanPenjualan();
    public static Kasir kasir = new Kasir("Admin");

    // Mencegah data awal dibuat berulang kali
    public static void inisialisasi() {
        if (!daftarMenu.isEmpty()) {
            return;
        }
        // Kalau file data belum ada / gagal dibaca, pakai data awal
        if (!muatData()) {
            isiDataAwal();
        }
    }

    public static void isiDataAwal() {
        BahanBaku nasi = new BahanBaku("B01", "Nasi Sushi", "gram", 5000, 1000);
        BahanBaku nori = new BahanBaku("B02", "Nori", "lembar", 100, 20);
        BahanBaku salmon = new BahanBaku("B03", "Salmon", "gram", 2000, 300);
        BahanBaku tuna = new BahanBaku("B04", "Tuna", "gram", 1500, 300);
        BahanBaku timun = new BahanBaku("B05", "Timun", "gram", 1000);

        daftarBahan.add(nasi);
        daftarBahan.add(nori);
        daftarBahan.add(salmon);
        daftarBahan.add(tuna);
        daftarBahan.add(timun);

        Menu salmonRoll = new Menu("M01", "Salmon Roll", 25000);
        salmonRoll.tambahBahan(nasi, 80);
        salmonRoll.tambahBahan(nori, 1);
        salmonRoll.tambahBahan(salmon, 30);

        Menu tunaRoll = new Menu("M02", "Tuna Roll", 23000);
        tunaRoll.tambahBahan(nasi, 80);
        tunaRoll.tambahBahan(nori, 1);
        tunaRoll.tambahBahan(tuna, 30);

        Menu kappaMaki = new Menu("M03", "Kappa Maki", 15000);
        kappaMaki.tambahBahan(nasi, 60);
        kappaMaki.tambahBahan(nori, 1);
        kappaMaki.tambahBahan(timun, 25);

        daftarMenu.add(salmonRoll);
        daftarMenu.add(tunaRoll);
        daftarMenu.add(kappaMaki);
    }

    public static BahanBaku cariBahan(String kode) {
        for (BahanBaku b : daftarBahan) {
            if (b.getKode().equals(kode)) {
                return b;
            }
        }
        return null;
    }

    // Hasilnya bisa Menu atau BahanBaku, tipe datanya Produk (Polymorphism, Pert. 7)
    public static Produk cariProduk(String kode) {
        for (Menu m : daftarMenu) {
            if (m.getKode().equalsIgnoreCase(kode)) {
                return m;
            }
        }
        for (BahanBaku b : daftarBahan) {
            if (b.getKode().equalsIgnoreCase(kode)) {
                return b;
            }
        }
        return null;
    }

    // Kode baru = nomor kode terbesar + 1, biar tidak bentrok kalau ada data yang dihapus
    public static String buatKodeBaru(String huruf) {
        int terbesar = 0;
        ArrayList<Produk> semua = new ArrayList<>();
        semua.addAll(daftarMenu);
        semua.addAll(daftarBahan);
        for (Produk p : semua) {
            if (p.getKode().startsWith(huruf)) {
                int nomor = Integer.parseInt(p.getKode().substring(1));
                if (nomor > terbesar) {
                    terbesar = nomor;
                }
            }
        }
        return String.format("%s%02d", huruf, terbesar + 1);
    }

    // ================= FILE I/O (Pert. 11) =================

    // Serialization: simpan objek ke file .dat
    public static void simpanData() throws IOException {
        FileOutputStream fileKeluar = new FileOutputStream("sushilaww_data.dat");
        ObjectOutputStream mesinPembeku = new ObjectOutputStream(fileKeluar);
        mesinPembeku.writeObject(daftarBahan);
        mesinPembeku.writeObject(daftarMenu);
        mesinPembeku.writeObject(laporan);
        mesinPembeku.close();
    }

    // Deserialization: baca lagi objek dari file .dat
    public static boolean muatData() {
        try {
            FileInputStream fileMasuk = new FileInputStream("sushilaww_data.dat");
            ObjectInputStream mesinPembangkit = new ObjectInputStream(fileMasuk);
            daftarBahan = (ArrayList<BahanBaku>) mesinPembangkit.readObject();
            daftarMenu = (ArrayList<Menu>) mesinPembangkit.readObject();
            laporan = (LaporanPenjualan) mesinPembangkit.readObject();
            mesinPembangkit.close();

            // variabel static tidak ikut tersimpan, jadi diatur lagi
            Transaksi.totalTransaksi = laporan.getDaftarTransaksi().size();
            return true;
        } catch (Exception e) {
            System.out.println("Data belum ada, memakai data awal.");
            daftarBahan = new ArrayList<>();
            daftarMenu = new ArrayList<>();
            laporan = new LaporanPenjualan();
            return false;
        }
    }

    // Menulis riwayat stok ke file teks (true = ditambah di bawah, bukan ditimpa)
    public static void tulisRiwayat(String isi) throws IOException {
        FileWriter pipaTulis = new FileWriter("riwayat_stok.txt", true);
        pipaTulis.write(LocalDate.now() + " | " + isi + "\n");
        pipaTulis.close();
    }

    // Membaca file teks baris per baris pakai Scanner
    public static String bacaRiwayat() throws FileNotFoundException {
        File file = new File("riwayat_stok.txt");
        Scanner mesinBaca = new Scanner(file);
        String isi = "";
        while (mesinBaca.hasNextLine()) {
            isi = isi + mesinBaca.nextLine() + "\n";
        }
        mesinBaca.close();
        return isi;
    }

    public static void simpanLaporanKeFile(String isi) throws IOException {
        FileWriter pipaTulis = new FileWriter("laporan_penjualan.txt");
        pipaTulis.write(isi);
        pipaTulis.close();
    }
}
