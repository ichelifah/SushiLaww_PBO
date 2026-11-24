package com.mycompany.sushilaww;

import java.io.Serializable;

// ABSTRACT CLASS (Pert. 9) sekaligus SUPERCLASS (Pert. 6) untuk Menu dan BahanBaku
// implements Serializable supaya bisa disimpan ke file (Pert. 11)
public abstract class Produk implements Serializable {
    // Encapsulation (Pert. 4): atribut dibuat private
    private String kode;
    private String nama;

    // Constructor (Pert. 3), this dipakai karena nama parameter sama dengan field
    public Produk(String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
    }

    // Getter dan Setter
    public String getKode() {
        return this.kode;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        if (nama.isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!"); // throw (Pert. 10)
        }
        this.nama = nama;
    }

    // Abstract method (Pert. 9): wajib diisi subclass
    public abstract String getJenis();

    // Method ini akan di-override oleh subclass (Method Overriding)
    public String getInfo() {
        return String.format("%-5s | %-16s", kode, nama);
    }

    // getInfo() yang dipanggil tergantung objek aslinya (dynamic binding, Pert. 7)
    public void tampilkanInfo() {
        System.out.println(getInfo());
    }

    // Teks yang tampil di ComboBox
    @Override
    public String toString() {
        return kode + " - " + nama;
    }
}
