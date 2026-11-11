package com.mycompany.sushilaww;

// SUPERCLASS (Pert. 6): menyimpan data umum yang dimiliki Menu dan BahanBaku
public class Produk {
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

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    // Method ini akan di-override oleh subclass (Method Overriding)
    public void tampilkanInfo() {
        System.out.printf("%-6s | %-18s", kode, nama);
    }
}
