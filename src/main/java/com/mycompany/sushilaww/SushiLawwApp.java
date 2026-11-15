
package com.mycompany.sushilaww;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SushiLawwApp extends Application {
    

    @Override
    public void start(Stage stage) {
        SushiLawwData.inisialisasi();

        // Judul aplikasi
        Label judul = new Label("SushiLaww");
        judul.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #243B53;"
        );

        Label deskripsi = new Label(
            "Sistem Pengelolaan Restoran Sushi"
        );

        // Form input
        Label labelNama = new Label("Nama Menu:");
        TextField inputNama = new TextField();
        inputNama.setPromptText("Masukkan nama sushi");

        Label labelHarga = new Label("Harga Menu:");
        TextField inputHarga = new TextField();
        inputHarga.setPromptText("Masukkan harga");

        // Mengatur form menggunakan GridPane
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);

        form.add(labelNama, 0, 0);
        form.add(inputNama, 1, 0);
        form.add(labelHarga, 0, 1);
        form.add(inputHarga, 1, 1);

        // Tombol simpan
        Button btnSimpan = new Button("Simpan Menu");
        
        // Tombol untuk melihat daftar menu
        Button btnLihatMenu = new Button("Lihat Daftar Menu");

        // Area untuk menampilkan data menu
        TextArea areaMenu = new TextArea();
        areaMenu.setEditable(false);
        areaMenu.setPromptText("Daftar menu akan tampil di sini...");
        areaMenu.setPrefHeight(150);
        
        // Event handling tombol daftar menu
    btnLihatMenu.setOnAction(event -> {

        StringBuilder hasil = new StringBuilder();

        hasil.append("===== DAFTAR MENU SUSHILAWW =====\n\n");

        for (Menu menu : SushiLawwData.daftarMenu) {

            hasil.append("Kode       : ")
                 .append(menu.getKode()).append("\n");

            hasil.append("Nama       : ")
                 .append(menu.getNama()).append("\n");

            hasil.append("Harga      : Rp ")
                 .append(String.format("%,.0f", menu.getHarga()))
                 .append("\n");

            hasil.append("Bahan      : ")
                 .append(menu.getBahan().getNama()).append("\n");

            hasil.append("Bahan/Porsi: ")
                 .append(menu.getBahanPerPorsi())
                 .append(" ")
                 .append(menu.getBahan().getSatuan())
                 .append("\n");

            hasil.append("------------------------------\n");
        }

        areaMenu.setText(hasil.toString());
    });
        
        // Event handling menggunakan setOnAction
        btnSimpan.setOnAction(event -> {

            String nama = inputNama.getText();
            String hargaText = inputHarga.getText();

            // Validasi kolom kosong
            if (nama.isEmpty() || hargaText.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText("Data belum lengkap");
                alert.setContentText(
                    "Nama menu dan harga wajib diisi!"
                );
                alert.showAndWait();

                return;
            }

            // Validasi format angka menggunakan try-catch
            try {

                double harga = Double.parseDouble(hargaText);

                if (harga <= 0) {
                    throw new NumberFormatException();
                }

                // TAHAP 2: buat objek Menu baru lalu masukkan ke daftar menu
                String kodeBaru = String.format("M%02d", SushiLawwData.daftarMenu.size() + 1);
                BahanBaku bahanDefault = SushiLawwData.daftarBahan.get(0); // Nasi Sushi
                Menu menuBaru = new Menu(kodeBaru, nama, harga, bahanDefault, 100);
                SushiLawwData.daftarMenu.add(menuBaru);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText("Data menu berhasil diproses");
                alert.setContentText(
                    "Nama Menu: " + nama +
                    "\nHarga: Rp " + harga
                );
                alert.showAndWait();

                // Mengosongkan input setelah berhasil
                inputNama.clear();
                inputHarga.clear();

            } catch (NumberFormatException e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Kesalahan Input");
                alert.setHeaderText("Harga tidak valid");
                alert.setContentText(
                    "Masukkan harga berupa angka lebih dari 0!"
                );
                alert.showAndWait();
            }
        });

        HBox tombol = new HBox(btnSimpan);
        tombol.setAlignment(Pos.CENTER);

        // Layout utama
        VBox layout = new VBox(
            15,
            judul,
            deskripsi,
            new Label("FORM TAMBAH MENU"),
            form,
            tombol,
            btnLihatMenu,
            areaMenu
    );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Scene scene = new Scene(layout, 600, 450);

        stage.setTitle("SushiLaww - Aplikasi Restoran");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
