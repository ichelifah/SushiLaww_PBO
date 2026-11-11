package com.mycompany.sushilaww;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Object Instantiation (Pert. 3): bahan baku awal
        BahanBaku nasi = new BahanBaku("B01", "Nasi Sushi", "gram", 5000);
        BahanBaku salmon = new BahanBaku("B02", "Salmon", "gram", 1500);
        BahanBaku tuna = new BahanBaku("B03", "Tuna", "gram", 1200);

        ArrayList<BahanBaku> daftarBahan = new ArrayList<>();
        daftarBahan.add(nasi);
        daftarBahan.add(salmon);
        daftarBahan.add(tuna);

        // Menu awal
        ArrayList<Menu> daftarMenu = new ArrayList<>();
        daftarMenu.add(new Menu("M01", "Salmon Roll", 35000, salmon, 80));
        daftarMenu.add(new Menu("M02", "Tuna Roll", 32000, tuna, 80));
        daftarMenu.add(new Menu("M03", "Nasi Sushi Polos", 12000, nasi, 150));

        LaporanPenjualan laporan = new LaporanPenjualan();
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
                        double tambah = bacaAngka(scanner);
                        daftarBahan.get(idxBahan).tambahStok(tambah);
                        System.out.println("Stok " + daftarBahan.get(idxBahan).getNama()
                                + " sekarang: " + daftarBahan.get(idxBahan).getJumlahStok());
                    } else {
                        System.out.println("Pilihan bahan tidak valid.");
                    }
                    break;

                case 4:
                    prosesTransaksi(scanner, daftarMenu, laporan);
                    break;

                case 5:
                    laporan.tampilkanLaporan();
                    break;

                case 6:
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
    private static void prosesTransaksi(Scanner scanner, ArrayList<Menu> daftarMenu,
                                        LaporanPenjualan laporan) {
        Transaksi transaksi = new Transaksi(LocalDate.now().toString());
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
                    // Bahan yang dibutuhkan = bahan per porsi x jumlah porsi
                    double butuh = menu.getBahanPerPorsi() * porsi;
                    if (menu.getBahan().kurangiStok(butuh)) {
                        transaksi.tambahDetail(new DetailTransaksi(menu, porsi));
                        System.out.println(porsi + " porsi " + menu.getNama() + " ditambahkan.");
                    } else {
                        System.out.println("Stok " + menu.getBahan().getNama() + " tidak cukup! Sisa: "
                                + menu.getBahan().getJumlahStok() + " " + menu.getBahan().getSatuan());
                    }
                }
            }
        }

        if (transaksi.getDaftarDetail().isEmpty()) {
            System.out.println("Transaksi dibatalkan (tidak ada pesanan).");
            Transaksi.totalTransaksi--; // transaksi kosong tidak dihitung
        } else {
            laporan.tambahTransaksi(transaksi);
            transaksi.cetakStruk();
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
