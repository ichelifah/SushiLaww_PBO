package com.mycompany.sushilaww;

// SUBCLASS dari Produk (Pert. 6: extends)
public class BahanBaku extends Produk {
    private String satuan;
    private double jumlahStok;

    public BahanBaku(String kode, String nama, String satuan, double jumlahStok) {
        super(kode, nama); // super() wajib jadi baris pertama
        this.satuan = satuan;
        setJumlahStok(jumlahStok); // memakai setter agar kena validasi
    }

    public String getSatuan() {
        return this.satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public double getJumlahStok() {
        return this.jumlahStok;
    }

    // Setter dengan validasi: stok tidak boleh negatif
    public void setJumlahStok(double jumlahStok) {
        if (jumlahStok >= 0) {
            this.jumlahStok = jumlahStok;
        } else {
            System.out.println("Stok tidak valid (tidak boleh negatif)! Diatur ke 0.");
            this.jumlahStok = 0;
        }
    }

    // Menambah stok (stok masuk)
    public void tambahStok(double jumlah) {
        if (jumlah > 0) {
            this.jumlahStok += jumlah;
        } else {
            System.out.println("Jumlah tambah stok harus lebih dari 0!");
        }
    }

    // Mengurangi stok. Mengembalikan false jika stok tidak cukup
    public boolean kurangiStok(double jumlah) {
        if (jumlah <= this.jumlahStok) {
            this.jumlahStok -= jumlah;
            return true;
        }
        return false;
    }

    // Method Overriding: menambah info milik Produk
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo(); // cetak kode dan nama dari superclass
        System.out.printf(" | Stok: %.1f %s%n", jumlahStok, satuan);
    }
}
