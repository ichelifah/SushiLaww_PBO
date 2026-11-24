package com.mycompany.sushilaww;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Versi console SushiLaww (versi GUI ada di SushiLawwApp)
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Data sama dengan versi GUI
        SushiLawwData.inisialisasi();
        ArrayList<BahanBaku> daftarBahan = SushiLawwData.daftarBahan;
        ArrayList<Menu> daftarMenu = SushiLawwData.daftarMenu;

        boolean isRunning = true;

        System.out.println("========================================");
        System.out.println("      SELAMAT DATANG DI SUSHILAWW       ");
        System.out.println("========================================");

        // Control flow: while + switch (Pert. 2)
        while (isRunning) {
            System.out.println("\nMenu Utama:");
            System.out.println("1. Lihat Menu");
            System.out.println("2. Lihat Stok Bahan Baku");
            System.out.println("3. Tambah Stok Bahan Baku");
            System.out.println("4. Transaksi Baru");
            System.out.println("5. Laporan Penjualan");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu 1-6: ");

            int pilihan = bacaAngka(scanner);

            switch (pilihan) {
                case 1:
                    System.out.println("\n--- DAFTAR MENU ---");
                    for (Menu m : daftarMenu) {
                        m.tampilkanInfo(); // versi override milik Menu
                    }
                    break;

                case 2:
                    System.out.println("\n--- STOK BAHAN BAKU ---");
                    for (BahanBaku b : daftarBahan) {
                        b.tampilkanInfo(); // versi override milik BahanBaku
                    }
                    break;

                case 3:
                    System.out.println("\n--- TAMBAH STOK ---");
                    for (int i = 0; i < daftarBahan.size(); i++) {
                        System.out.println((i + 1) + ". " + daftarBahan.get(i).getNama());
                    }
                    System.out.print("Pilih bahan: ");
                    int idxBahan = bacaAngka(scanner) - 1;
                    if (idxBahan >= 0 && idxBahan < daftarBahan.size()) {
                        System.out.print("Jumlah yang ditambah: ");
                        try {
                            double tambah = Double.parseDouble(scanner.nextLine().trim());
                            daftarBahan.get(idxBahan).tambahStok(tambah);
                            System.out.println("Stok " + daftarBahan.get(idxBahan).getNama()
                                    + " sekarang: " + daftarBahan.get(idxBahan).getJumlahStok());
                            simpan();
                        } catch (NumberFormatException e) {
                            System.out.println("Jumlah harus angka!");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Pilihan bahan tidak valid.");
                    }
                    break;

                case 4:
                    prosesTransaksi(scanner, daftarMenu);
                    break;

                case 5:
                    SushiLawwData.laporan.tampilkanLaporan();
                    break;

                case 6:
                    simpan();
                    System.out.println("Terima kasih telah menggunakan SushiLaww!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Pilihan tidak valid. Masukkan angka 1-6.");
            }
        }
        scanner.close();
    }

    // Membuat satu transaksi: pilih menu, cek stok, kurangi stok, simpan
    private static void prosesTransaksi(Scanner scanner, ArrayList<Menu> daftarMenu) {
        Transaksi transaksi = new Transaksi();
        boolean tambahLagi = true;

        while (tambahLagi) {
            System.out.println("\n--- PILIH MENU ---");
            for (int i = 0; i < daftarMenu.size(); i++) {
                System.out.printf("%d. %s (Rp %,.0f)%n", i + 1,
                        daftarMenu.get(i).getNama(), daftarMenu.get(i).getHarga());
            }
            System.out.print("Pilih menu (0 = selesai): ");
            int idx = bacaAngka(scanner);

            if (idx == 0) {
                tambahLagi = false;
            } else if (idx < 1 || idx > daftarMenu.size()) {
                System.out.println("Pilihan menu tidak valid.");
            } else {
                Menu menu = daftarMenu.get(idx - 1);
                System.out.print("Jumlah porsi: ");
                int porsi = bacaAngka(scanner);

                if (porsi <= 0) {
                    System.out.println("Jumlah porsi harus lebih dari 0.");
                } else {
                    transaksi.tambahPesanan(menu, porsi);
                    System.out.println(porsi + " porsi " + menu.getNama() + " ditambahkan.");
                }
            }
        }

        if (transaksi.getDaftarDetail().isEmpty()) {
            System.out.println("Transaksi dibatalkan (tidak ada pesanan).");
            return;
        }

        try {
            SushiLawwData.kasir.simpanTransaksi(transaksi, SushiLawwData.laporan);
            transaksi.cetakStruk();
            simpan();
        } catch (StokTidakCukupException e) {
            System.out.println(e.getMessage());
            System.out.println("Transaksi tidak disimpan.");
        }
    }

    private static void simpan() {
        try {
            SushiLawwData.simpanData();
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    // Membaca angka dengan aman agar program tidak error jika user mengetik huruf
    private static int bacaAngka(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
