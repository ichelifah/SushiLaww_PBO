package com.mycompany.sushilaww;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SushiLawwApp extends Application {

    // Satu Scene dipakai terus, yang diganti cuma isinya (root)
    private Scene scene;

    // Transaksi yang sedang diisi di halaman Transaksi
    private Transaksi transaksiAktif = new Transaksi();

    // Resep sementara di halaman Daftar Menu sebelum menu disimpan
    private ArrayList<BahanBaku> resepBahan = new ArrayList<>();
    private ArrayList<Double> resepTakaran = new ArrayList<>(); // wrapper class Double (Pert. 10)

    // Array pilihan satuan (Pert. 2)
    private String[] daftarSatuan = {"gram", "kg", "lembar", "ml", "pcs"};

    @Override
    public void start(Stage stage) {
        SushiLawwData.inisialisasi();

        scene = new Scene(buatHalamanAwal(), 900, 620);

        stage.setTitle("SushiLaww - Aplikasi Restoran");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // ===================== HALAMAN AWAL =====================
    private VBox buatHalamanAwal() {

        Label judul = new Label("SushiLaww");
        judul.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #243B53;"
        );

        Label deskripsi = new Label("Sistem Pengelolaan Restoran Sushi");

        // Cek berapa bahan yang stoknya menipis
        int jumlahMenipis = 0;
        for (BahanBaku b : SushiLawwData.daftarBahan) {
            if (b.isStokMenipis()) {
                jumlahMenipis++;
            }
        }
        Label infoStok = new Label();
        if (jumlahMenipis > 0) {
            infoStok.setText("Perhatian: ada " + jumlahMenipis + " bahan baku yang stoknya menipis!");
            infoStok.setStyle("-fx-text-fill: red;");
        } else {
            infoStok.setText("Stok bahan baku aman.");
            infoStok.setStyle("-fx-text-fill: green;");
        }

        Button btnTransaksi = new Button("Transaksi");
        Button btnDaftarMenu = new Button("Daftar Menu");
        Button btnStokBahan = new Button("Stok Bahan");
        Button btnLaporan = new Button("Laporan Penjualan");
        btnTransaksi.setPrefWidth(200);
        btnDaftarMenu.setPrefWidth(200);
        btnStokBahan.setPrefWidth(200);
        btnLaporan.setPrefWidth(200);

        // Pindah halaman
        btnTransaksi.setOnAction(event -> scene.setRoot(buatHalamanTransaksi()));
        btnDaftarMenu.setOnAction(event -> scene.setRoot(buatHalamanDaftarMenu()));
        btnStokBahan.setOnAction(event -> scene.setRoot(buatHalamanStokBahan()));
        btnLaporan.setOnAction(event -> scene.setRoot(buatHalamanLaporan()));

        // Cari produk berdasarkan kode
        TextField inputKode = new TextField();
        inputKode.setPromptText("Kode, contoh: M01 / B03");
        Button btnCari = new Button("Cari");

        btnCari.setOnAction(event -> {
            String kode = inputKode.getText().trim();
            if (kode.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Masukkan kode dulu!");
                return;
            }
            // Tipe datanya Produk, isinya bisa Menu atau BahanBaku (Polymorphism, Pert. 7)
            Produk hasil = SushiLawwData.cariProduk(kode);
            if (hasil == null) {
                tampilkanAlert(AlertType.WARNING, "Tidak Ditemukan", "Kode " + kode + " tidak ditemukan.");
            } else {
                tampilkanAlert(AlertType.INFORMATION, hasil.getJenis(), hasil.getInfo());
            }
        });

        HBox barisCari = new HBox(10, inputKode, btnCari);
        barisCari.setAlignment(Pos.CENTER);

        VBox layout = new VBox(
            15,
            judul,
            deskripsi,
            infoStok,
            btnTransaksi,
            btnDaftarMenu,
            btnStokBahan,
            btnLaporan,
            new Label("Cari menu / bahan baku:"),
            barisCari
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        return layout;
    }

    // ===================== HALAMAN TRANSAKSI =====================
    private VBox buatHalamanTransaksi() {

        Label judul = buatJudul("Transaksi Penjualan");

        // Form input pesanan
        ComboBox<Menu> pilihMenu = new ComboBox<>();
        pilihMenu.getItems().addAll(SushiLawwData.daftarMenu);
        pilihMenu.setPromptText("Pilih menu");
        pilihMenu.setPrefWidth(200);

        TextField inputJumlah = new TextField();
        inputJumlah.setPromptText("Contoh: 2");

        Label infoMenu = new Label("");
        infoMenu.setWrapText(true);
        infoMenu.setPrefWidth(350);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.add(new Label("Menu:"), 0, 0);
        form.add(pilihMenu, 1, 0);
        form.add(new Label("Jumlah Porsi:"), 0, 1);
        form.add(inputJumlah, 1, 1);

        Button btnTambah = new Button("Tambah Pesanan");
        Button btnHapus = new Button("Hapus Pesanan Terakhir");
        Button btnSimpan = new Button("Simpan Transaksi");
        btnSimpan.setPrefWidth(200);
        btnSimpan.setStyle("-fx-font-weight: bold;");

        Label labelTotal = new Label();
        labelTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Area rincian pesanan dan kebutuhan bahan
        TextArea areaPesanan = buatTextArea(220);
        TextArea areaBahan = buatTextArea(150);

        perbaruiTransaksi(areaPesanan, areaBahan, labelTotal);

        // Tampilkan harga dan resep waktu menu dipilih
        pilihMenu.setOnAction(event -> {
            Menu m = pilihMenu.getValue();
            if (m != null) {
                infoMenu.setText(String.format("Harga: Rp %,.0f%nResep: %s", m.getHarga(), m.getTeksResep()));
            }
        });

        btnTambah.setOnAction(event -> {
            Menu menu = pilihMenu.getValue();
            String jumlahText = inputJumlah.getText().trim();

            if (menu == null || jumlahText.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih menu dan isi jumlah porsi!");
                return;
            }

            try {
                int porsi = Integer.parseInt(jumlahText);
                if (porsi < 1 || porsi > 100) {
                    tampilkanAlert(AlertType.WARNING, "Peringatan", "Jumlah porsi harus 1 sampai 100!");
                    return;
                }
                transaksiAktif.tambahPesanan(menu, porsi);
                inputJumlah.clear();
                perbaruiTransaksi(areaPesanan, areaBahan, labelTotal);

            } catch (NumberFormatException e) {
                tampilkanAlert(AlertType.ERROR, "Kesalahan Input", "Jumlah porsi harus angka bulat!");
            }
        });

        btnHapus.setOnAction(event -> {
            transaksiAktif.hapusPesananTerakhir();
            perbaruiTransaksi(areaPesanan, areaBahan, labelTotal);
        });

        btnSimpan.setOnAction(event -> {
            try {
                // Stok dicek dulu, kalau cukup baru dikurangi otomatis
                SushiLawwData.kasir.simpanTransaksi(transaksiAktif, SushiLawwData.laporan);
                simpanData();
                tulisRiwayat(String.format("Transaksi #%d Rp %,.0f (stok dikurangi otomatis)",
                        transaksiAktif.getNomor(), transaksiAktif.getTotal()));

                tampilkanAlert(AlertType.INFORMATION, "Transaksi Berhasil",
                        transaksiAktif.getTeksStruk() + "\nKasir: " + SushiLawwData.kasir.getNama());

                // Siapkan transaksi baru
                transaksiAktif = new Transaksi();
                pilihMenu.setValue(null);
                infoMenu.setText("");
                perbaruiTransaksi(areaPesanan, areaBahan, labelTotal);

            } catch (StokTidakCukupException e) {
                tampilkanAlert(AlertType.ERROR, "Transaksi Gagal", e.getMessage() + "\n\nTransaksi tidak disimpan.");
            } catch (IllegalArgumentException e) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", e.getMessage());
            }
        });

        Button btnKembali = buatTombolKembali();

        VBox kiri = new VBox(15, form, infoMenu, btnTambah, btnHapus, labelTotal, btnSimpan);
        kiri.setPrefWidth(380);

        VBox kanan = new VBox(10, new Label("Rincian Pesanan:"), areaPesanan,
                new Label("Kebutuhan Bahan:"), areaBahan);
        kanan.setPrefWidth(450);

        HBox isi = new HBox(20, kiri, kanan);

        VBox layout = new VBox(15, judul, isi, btnKembali);
        layout.setPadding(new Insets(20));

        return layout;
    }

    // Menampilkan ulang rincian pesanan, kebutuhan bahan, dan total
    private void perbaruiTransaksi(TextArea areaPesanan, TextArea areaBahan, Label labelTotal) {
        if (transaksiAktif.getDaftarDetail().isEmpty()) {
            areaPesanan.setText("Belum ada pesanan.");
        } else {
            areaPesanan.setText(transaksiAktif.getTeksPesanan());
        }
        labelTotal.setText(String.format("Total: Rp %,.0f", transaksiAktif.getTotal()));

        HashMap<String, Double> kebutuhan = transaksiAktif.hitungKebutuhanBahan();
        String teks = "";
        for (String kode : kebutuhan.keySet()) {
            BahanBaku b = SushiLawwData.cariBahan(kode);
            String status = "OK";
            if (kebutuhan.get(kode) > b.getJumlahStok()) {
                status = "KURANG";
            }
            teks = teks + String.format("%-12s butuh %.1f, stok %.1f %s -> %s%n",
                    b.getNama(), kebutuhan.get(kode), b.getJumlahStok(), b.getSatuan(), status);
        }
        areaBahan.setText(teks);
    }

    // ===================== HALAMAN DAFTAR MENU =====================
    private VBox buatHalamanDaftarMenu() {

        Label judul = buatJudul("Daftar Menu");

        resepBahan.clear();
        resepTakaran.clear();

        // Form input
        Label labelKode = new Label(SushiLawwData.buatKodeBaru("M"));
        TextField inputNama = new TextField();
        inputNama.setPromptText("Masukkan nama sushi");
        TextField inputHarga = new TextField();
        inputHarga.setPromptText("Contoh: 25000");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.add(new Label("Kode Menu:"), 0, 0);
        form.add(labelKode, 1, 0);
        form.add(new Label("Nama Menu:"), 0, 1);
        form.add(inputNama, 1, 1);
        form.add(new Label("Harga Menu:"), 0, 2);
        form.add(inputHarga, 1, 2);

        // Input resep per porsi
        ComboBox<BahanBaku> pilihBahan = new ComboBox<>();
        pilihBahan.getItems().addAll(SushiLawwData.daftarBahan);
        pilihBahan.setPromptText("Pilih bahan");
        TextField inputTakaran = new TextField();
        inputTakaran.setPromptText("Takaran");
        inputTakaran.setPrefWidth(80);
        Button btnTambahBahan = new Button("Tambah Bahan");

        HBox barisResep = new HBox(10, pilihBahan, inputTakaran, btnTambahBahan);

        TextArea areaResep = buatTextArea(90);
        areaResep.setText("Resep masih kosong.");

        btnTambahBahan.setOnAction(event -> {
            BahanBaku bahan = pilihBahan.getValue();
            String takaranText = inputTakaran.getText().trim();

            if (bahan == null || takaranText.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih bahan dan isi takarannya!");
                return;
            }
            try {
                double takaran = Double.parseDouble(takaranText);
                if (takaran <= 0) {
                    tampilkanAlert(AlertType.WARNING, "Peringatan", "Takaran harus lebih dari 0!");
                    return;
                }
                resepBahan.add(bahan);
                resepTakaran.add(takaran);

                String teks = "";
                for (int i = 0; i < resepBahan.size(); i++) {
                    teks = teks + (i + 1) + ". " + resepBahan.get(i).getNama() + " "
                            + resepTakaran.get(i) + " " + resepBahan.get(i).getSatuan() + "\n";
                }
                areaResep.setText(teks);
                inputTakaran.clear();

            } catch (NumberFormatException e) {
                tampilkanAlert(AlertType.ERROR, "Kesalahan Input", "Takaran harus angka! (desimal pakai titik)");
            }
        });

        // Tombol simpan
        Button btnSimpan = new Button("Simpan Menu");

        // Event handling menggunakan setOnAction
        btnSimpan.setOnAction(event -> {

            String nama = inputNama.getText().trim();
            String hargaText = inputHarga.getText().trim();

            // Validasi kolom kosong
            if (nama.isEmpty() || hargaText.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Nama menu dan harga wajib diisi!");
                return;
            }

            // Validasi harga hanya angka, supaya 25.000 tidak terbaca 25
            if (!hargaText.matches("\\d+")) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Harga hanya boleh angka tanpa titik! Contoh: 25000");
                return;
            }

            if (resepBahan.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Resep menu belum diisi!");
                return;
            }

            // Validasi format angka menggunakan try-catch
            try {
                double harga = Double.parseDouble(hargaText);

                // Buat objek Menu baru lalu masukkan resepnya
                Menu menuBaru = new Menu(labelKode.getText(), nama, harga);
                for (int i = 0; i < resepBahan.size(); i++) {
                    menuBaru.tambahBahan(resepBahan.get(i), resepTakaran.get(i));
                }
                SushiLawwData.daftarMenu.add(menuBaru);
                simpanData();

                tampilkanAlert(AlertType.INFORMATION, "Berhasil",
                        "Menu " + nama + " berhasil disimpan!\nResep: " + menuBaru.getTeksResep());

                // Muat ulang halaman supaya form kosong lagi
                scene.setRoot(buatHalamanDaftarMenu());

            } catch (NumberFormatException e) {
                tampilkanAlert(AlertType.ERROR, "Kesalahan Input", "Harga tidak valid!");
            } catch (IllegalArgumentException e) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", e.getMessage());
            }
        });

        Button btnReset = new Button("Reset");
        btnReset.setOnAction(event -> scene.setRoot(buatHalamanDaftarMenu()));

        // Hapus menu
        ComboBox<Menu> pilihHapus = new ComboBox<>();
        pilihHapus.getItems().addAll(SushiLawwData.daftarMenu);
        pilihHapus.setPromptText("Pilih menu");
        Button btnHapus = new Button("Hapus Menu");

        btnHapus.setOnAction(event -> {
            Menu menu = pilihHapus.getValue();
            if (menu == null) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih menu yang mau dihapus!");
                return;
            }
            if (konfirmasi("Yakin mau hapus menu " + menu.getNama() + "?")) {
                SushiLawwData.daftarMenu.remove(menu);
                simpanData();
                scene.setRoot(buatHalamanDaftarMenu());
            }
        });

        // Area untuk menampilkan data menu
        TextArea areaMenu = buatTextArea(430);
        String teksMenu = "===== DAFTAR MENU SUSHILAWW =====\n\n";
        for (Menu menu : SushiLawwData.daftarMenu) {
            teksMenu = teksMenu + "Kode  : " + menu.getKode() + "\n";
            teksMenu = teksMenu + "Nama  : " + menu.getNama() + "\n";
            teksMenu = teksMenu + String.format("Harga : Rp %,.0f%n", menu.getHarga());
            teksMenu = teksMenu + "Resep per porsi:\n";
            for (ItemResep item : menu.getResep()) {
                teksMenu = teksMenu + "  - " + item + "\n";
            }
            teksMenu = teksMenu + "------------------------------\n";
        }
        areaMenu.setText(teksMenu);

        Button btnKembali = buatTombolKembali();

        HBox tombol = new HBox(10, btnSimpan, btnReset);
        HBox barisHapus = new HBox(10, pilihHapus, btnHapus);

        VBox kiri = new VBox(12,
            new Label("FORM TAMBAH MENU"),
            form,
            new Label("Resep per 1 porsi:"),
            barisResep,
            areaResep,
            tombol,
            new Label("HAPUS MENU"),
            barisHapus
        );
        kiri.setPrefWidth(400);

        VBox kanan = new VBox(10, new Label("Daftar Menu:"), areaMenu);
        kanan.setPrefWidth(430);

        HBox isi = new HBox(20, kiri, kanan);

        VBox layout = new VBox(15, judul, isi, btnKembali);
        layout.setPadding(new Insets(20));

        return layout;
    }

    // ===================== HALAMAN STOK BAHAN =====================
    private VBox buatHalamanStokBahan() {

        Label judul = buatJudul("Stok Bahan Baku");

        // ---------- Form data bahan (tambah, ubah, hapus) ----------
        ComboBox<BahanBaku> pilihBahan = new ComboBox<>();
        pilihBahan.getItems().addAll(SushiLawwData.daftarBahan);
        pilihBahan.setPromptText("Pilih untuk ubah/hapus");
        pilihBahan.setPrefWidth(220);

        Label labelKode = new Label(SushiLawwData.buatKodeBaru("B"));
        TextField inputNama = new TextField();
        inputNama.setPromptText("Contoh: Nori");
        ComboBox<String> pilihSatuan = new ComboBox<>();
        pilihSatuan.getItems().addAll(daftarSatuan);
        pilihSatuan.setPromptText("Pilih satuan");
        TextField inputStok = new TextField();
        inputStok.setPromptText("Contoh: 2000");
        TextField inputMinimum = new TextField();
        inputMinimum.setPromptText("Boleh kosong");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Pilih Bahan:"), 0, 0);
        form.add(pilihBahan, 1, 0);
        form.add(new Label("Kode:"), 0, 1);
        form.add(labelKode, 1, 1);
        form.add(new Label("Nama Bahan:"), 0, 2);
        form.add(inputNama, 1, 2);
        form.add(new Label("Satuan:"), 0, 3);
        form.add(pilihSatuan, 1, 3);
        form.add(new Label("Jumlah Stok:"), 0, 4);
        form.add(inputStok, 1, 4);
        form.add(new Label("Stok Minimum:"), 0, 5);
        form.add(inputMinimum, 1, 5);

        // Kalau bahan dipilih, isi form dengan datanya
        pilihBahan.setOnAction(event -> {
            BahanBaku b = pilihBahan.getValue();
            if (b != null) {
                labelKode.setText(b.getKode());
                inputNama.setText(b.getNama());
                pilihSatuan.setValue(b.getSatuan());
                inputStok.setText(String.valueOf(b.getJumlahStok()));
                inputMinimum.setText(String.valueOf(b.getStokMinimum()));
            }
        });

        Button btnTambah = new Button("Tambah");
        Button btnUbah = new Button("Ubah");
        Button btnHapus = new Button("Hapus");
        Button btnReset = new Button("Reset");

        btnTambah.setOnAction(event -> {
            String nama = inputNama.getText().trim();
            String satuan = pilihSatuan.getValue();
            String stokText = inputStok.getText().trim();
            String minimumText = inputMinimum.getText().trim();

            if (nama.isEmpty() || satuan == null || stokText.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Nama, satuan, dan jumlah stok wajib diisi!");
                return;
            }
            for (BahanBaku b : SushiLawwData.daftarBahan) {
                if (b.getNama().equalsIgnoreCase(nama)) {
                    tampilkanAlert(AlertType.WARNING, "Peringatan", "Bahan " + nama + " sudah ada!");
                    return;
                }
            }

            try {
                double stok = Double.parseDouble(stokText);
                BahanBaku baru;
                if (minimumText.isEmpty()) {
                    baru = new BahanBaku(SushiLawwData.buatKodeBaru("B"), nama, satuan, stok);
                } else {
                    double minimum = Double.parseDouble(minimumText);
                    baru = new BahanBaku(SushiLawwData.buatKodeBaru("B"), nama, satuan, stok, minimum);
                }
                SushiLawwData.daftarBahan.add(baru);
                simpanData();
                tulisRiwayat("Tambah bahan " + nama + ", stok awal " + stok + " " + satuan);

                tampilkanAlert(AlertType.INFORMATION, "Berhasil", "Bahan " + nama + " berhasil ditambahkan!");
                scene.setRoot(buatHalamanStokBahan());

            } catch (NumberFormatException e) {
                tampilkanAlert(AlertType.ERROR, "Kesalahan Input", "Stok harus angka! (desimal pakai titik)");
            } catch (IllegalArgumentException e) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", e.getMessage());
            }
        });

        btnUbah.setOnAction(event -> {
            BahanBaku bahan = pilihBahan.getValue();
            if (bahan == null) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih bahan yang mau diubah!");
                return;
            }
            String nama = inputNama.getText().trim();
            if (nama.isEmpty() || pilihSatuan.getValue() == null || inputStok.getText().trim().isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Nama, satuan, dan jumlah stok wajib diisi!");
                return;
            }

            try {
                double stok = Double.parseDouble(inputStok.getText().trim());
                double minimum = Double.parseDouble(inputMinimum.getText().trim());

                // Ubah lewat setter supaya kena validasi (Encapsulation)
                bahan.setNama(nama);
                bahan.setSatuan(pilihSatuan.getValue());
                bahan.setJumlahStok(stok);
                bahan.setStokMinimum(minimum);
                simpanData();
                tulisRiwayat("Ubah data " + nama + ", stok jadi " + stok + " " + bahan.getSatuan());

                tampilkanAlert(AlertType.INFORMATION, "Berhasil", "Data " + nama + " berhasil diubah!");
                scene.setRoot(buatHalamanStokBahan());

            } catch (NumberFormatException e) {
                tampilkanAlert(AlertType.ERROR, "Kesalahan Input", "Stok dan stok minimum harus angka!");
            } catch (IllegalArgumentException e) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", e.getMessage());
            }
        });

        btnHapus.setOnAction(event -> {
            BahanBaku bahan = pilihBahan.getValue();
            if (bahan == null) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih bahan yang mau dihapus!");
                return;
            }
            // Bahan yang masih dipakai resep tidak boleh dihapus
            for (Menu m : SushiLawwData.daftarMenu) {
                if (m.memakaiBahan(bahan)) {
                    tampilkanAlert(AlertType.WARNING, "Tidak Bisa Dihapus",
                            bahan.getNama() + " masih dipakai di menu " + m.getNama() + "!");
                    return;
                }
            }
            if (konfirmasi("Yakin mau hapus bahan " + bahan.getNama() + "?")) {
                SushiLawwData.daftarBahan.remove(bahan);
                simpanData();
                tulisRiwayat("Hapus bahan " + bahan.getNama());
                scene.setRoot(buatHalamanStokBahan());
            }
        });

        btnReset.setOnAction(event -> scene.setRoot(buatHalamanStokBahan()));

        HBox tombolBahan = new HBox(10, btnTambah, btnUbah, btnHapus, btnReset);

        // ---------- Stok masuk / keluar ----------
        ComboBox<BahanBaku> pilihBahanStok = new ComboBox<>();
        pilihBahanStok.getItems().addAll(SushiLawwData.daftarBahan);
        pilihBahanStok.setPromptText("Pilih bahan");
        TextField inputJumlah = new TextField();
        inputJumlah.setPromptText("Jumlah");
        inputJumlah.setPrefWidth(100);

        RadioButton rbMasuk = new RadioButton("Stok Masuk");
        RadioButton rbKeluar = new RadioButton("Stok Keluar");
        ToggleGroup grup = new ToggleGroup();
        rbMasuk.setToggleGroup(grup);
        rbKeluar.setToggleGroup(grup);
        rbMasuk.setSelected(true);

        Button btnCatat = new Button("Catat Stok");

        HBox barisStok = new HBox(10, pilihBahanStok, inputJumlah);
        HBox barisRadio = new HBox(15, rbMasuk, rbKeluar, btnCatat);

        // ---------- Daftar stok ----------
        CheckBox cekMenipis = new CheckBox("Tampilkan yang menipis saja");
        TextArea areaStok = buatTextArea(400);
        areaStok.setText(teksDaftarStok(false));

        cekMenipis.setOnAction(event -> areaStok.setText(teksDaftarStok(cekMenipis.isSelected())));

        btnCatat.setOnAction(event -> {
            BahanBaku bahan = pilihBahanStok.getValue();
            String jumlahText = inputJumlah.getText().trim();

            if (bahan == null || jumlahText.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih bahan dan isi jumlahnya!");
                return;
            }

            try {
                double jumlah = Double.parseDouble(jumlahText);
                double stokLama = bahan.getJumlahStok();

                if (rbMasuk.isSelected()) {
                    bahan.tambahStok(jumlah);
                    tulisRiwayat("Stok masuk " + bahan.getNama() + " " + jumlah + " " + bahan.getSatuan()
                            + " (" + stokLama + " -> " + bahan.getJumlahStok() + ")");
                } else {
                    bahan.kurangiStok(jumlah);
                    tulisRiwayat("Stok keluar " + bahan.getNama() + " " + jumlah + " " + bahan.getSatuan()
                            + " (" + stokLama + " -> " + bahan.getJumlahStok() + ")");
                }
                simpanData();

                tampilkanAlert(AlertType.INFORMATION, "Berhasil", "Stok " + bahan.getNama() + " sekarang: "
                        + bahan.getJumlahStok() + " " + bahan.getSatuan());
                inputJumlah.clear();
                areaStok.setText(teksDaftarStok(cekMenipis.isSelected()));

            } catch (StokTidakCukupException e) {
                tampilkanAlert(AlertType.ERROR, "Stok Tidak Cukup", e.getMessage());
            } catch (NumberFormatException e) {
                tampilkanAlert(AlertType.ERROR, "Kesalahan Input", "Jumlah harus angka! (desimal pakai titik)");
            } catch (IllegalArgumentException e) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", e.getMessage());
            }
        });

        Button btnDaftar = new Button("Lihat Daftar Stok");
        btnDaftar.setOnAction(event -> areaStok.setText(teksDaftarStok(cekMenipis.isSelected())));

        Button btnRiwayat = new Button("Lihat Riwayat Stok");
        btnRiwayat.setOnAction(event -> {
            try {
                areaStok.setText("===== RIWAYAT STOK =====\n" + SushiLawwData.bacaRiwayat());
            } catch (FileNotFoundException e) {
                areaStok.setText("Belum ada riwayat stok.");
            }
        });

        Button btnKembali = buatTombolKembali();

        VBox kiri = new VBox(10,
            new Label("DATA BAHAN BAKU"),
            form,
            tombolBahan,
            new Label("STOK MASUK / KELUAR"),
            barisStok,
            barisRadio
        );
        kiri.setPrefWidth(400);

        VBox kanan = new VBox(10, cekMenipis, areaStok, new HBox(10, btnDaftar, btnRiwayat));
        kanan.setPrefWidth(430);

        HBox isi = new HBox(20, kiri, kanan);

        VBox layout = new VBox(15, judul, isi, btnKembali);
        layout.setPadding(new Insets(20));

        return layout;
    }

    private String teksDaftarStok(boolean hanyaMenipis) {
        String teks = "===== STOK BAHAN BAKU =====\n";
        for (BahanBaku b : SushiLawwData.daftarBahan) {
            if (hanyaMenipis && !b.isStokMenipis()) {
                continue; // lewati yang stoknya aman
            }
            teks = teks + b.getInfo() + "\n";
        }
        return teks;
    }

    // ===================== HALAMAN LAPORAN =====================
    private VBox buatHalamanLaporan() {

        Label judul = buatJudul("Laporan Penjualan");

        LaporanPenjualan laporan = SushiLawwData.laporan;

        Label labelJumlah = new Label("Jumlah Transaksi : " + laporan.getDaftarTransaksi().size());
        Label labelPendapatan = new Label(String.format("Total Pendapatan : Rp %,.0f", laporan.getTotalPendapatan()));
        labelJumlah.setStyle("-fx-font-size: 16px;");
        labelPendapatan.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> pilihTanggal = new ComboBox<>();
        pilihTanggal.getItems().addAll(laporan.getDaftarTanggal());
        pilihTanggal.setPromptText("Pilih tanggal");

        Button btnHarian = new Button("Laporan Harian");
        Button btnRekap = new Button("Rekap Semua");
        Button btnSimpanFile = new Button("Simpan ke File");

        TextArea areaLaporan = buatTextArea(380);
        if (laporan.getDaftarTransaksi().isEmpty()) {
            areaLaporan.setText("Belum ada transaksi.");
        } else {
            areaLaporan.setText(laporan.buatRekapSemua());
        }

        btnHarian.setOnAction(event -> {
            String tanggal = pilihTanggal.getValue();
            if (tanggal == null) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Pilih tanggal dulu!");
                return;
            }
            areaLaporan.setText(laporan.buatLaporanHarian(tanggal));
        });

        btnRekap.setOnAction(event -> areaLaporan.setText(laporan.buatRekapSemua()));

        btnSimpanFile.setOnAction(event -> {
            try {
                SushiLawwData.simpanLaporanKeFile(areaLaporan.getText());
                tampilkanAlert(AlertType.INFORMATION, "Berhasil", "Laporan disimpan ke file laporan_penjualan.txt");
            } catch (IOException e) {
                tampilkanAlert(AlertType.ERROR, "Gagal", "Laporan gagal disimpan: " + e.getMessage());
            } finally {
                System.out.println("Proses simpan laporan selesai.");
            }
        });

        HBox barisTombol = new HBox(10, pilihTanggal, btnHarian, btnRekap, btnSimpanFile);

        Button btnKembali = buatTombolKembali();

        VBox layout = new VBox(15, judul, labelJumlah, labelPendapatan, barisTombol, areaLaporan, btnKembali);
        layout.setPadding(new Insets(20));

        return layout;
    }

    // ===================== METHOD BANTUAN =====================

    private Label buatJudul(String teks) {
        Label judul = new Label(teks);
        judul.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #243B53;"
        );
        return judul;
    }

    private TextArea buatTextArea(double tinggi) {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefHeight(tinggi);
        area.setStyle("-fx-font-family: 'Monospaced';");
        return area;
    }

    private Button buatTombolKembali() {
        Button btnKembali = new Button("Kembali");
        btnKembali.setOnAction(event -> scene.setRoot(buatHalamanAwal()));
        return btnKembali;
    }

    // Helper method untuk menampilkan Alert (Pert. 12)
    private void tampilkanAlert(AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    // Alert konfirmasi, hasilnya true kalau user klik OK
    private boolean konfirmasi(String pesan) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    private void simpanData() {
        try {
            SushiLawwData.simpanData();
        } catch (IOException e) {
            tampilkanAlert(AlertType.ERROR, "Gagal", "Data gagal disimpan ke file!");
        }
    }

    private void tulisRiwayat(String isi) {
        try {
            SushiLawwData.tulisRiwayat(isi);
        } catch (IOException e) {
            System.out.println("Gagal menulis riwayat: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
