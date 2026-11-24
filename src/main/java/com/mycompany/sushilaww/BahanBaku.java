package com.mycompany.sushilaww;

// SUBCLASS dari Produk (Pert. 6) dan implements interface KelolaStok (Pert. 9)
public class BahanBaku extends Produk implements KelolaStok {
    private String satuan;
    private double jumlahStok;
    private double stokMinimum;

    public BahanBaku(String kode, String nama, String satuan, double jumlahStok, double stokMinimum) {
        super(kode, nama); // super() wajib jadi baris pertama
        this.satuan = satuan;
        setJumlahStok(jumlahStok);
        setStokMinimum(stokMinimum);
    }

    // Constructor overloading (Pert. 4): stok minimum pakai nilai default dari interface
    public BahanBaku(String kode, String nama, String satuan, double jumlahStok) {
        super(kode, nama);
        this.satuan = satuan;
        setJumlahStok(jumlahStok);
        this.stokMinimum = STOK_MINIMUM_DEFAULT;
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
        if (jumlahStok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif!");
        }
        this.jumlahStok = jumlahStok;
    }

    public double getStokMinimum() {
        return this.stokMinimum;
    }

    public void setStokMinimum(double stokMinimum) {
        if (stokMinimum < 0) {
            throw new IllegalArgumentException("Stok minimum tidak boleh negatif!");
        }
        this.stokMinimum = stokMinimum;
    }

    // Stok masuk
    @Override
    public void tambahStok(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah stok masuk harus lebih dari 0!");
        }
        this.jumlahStok += jumlah;
    }

    // Stok keluar, kalau tidak cukup lempar exception
    @Override
    public void kurangiStok(double jumlah) throws StokTidakCukupException {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah stok keluar harus lebih dari 0!");
        }
        if (jumlah > this.jumlahStok) {
            throw new StokTidakCukupException("Stok " + getNama() + " tidak cukup! Sisa: "
                    + this.jumlahStok + " " + this.satuan);
        }
        this.jumlahStok -= jumlah;
    }

    @Override
    public boolean isStokMenipis() {
        return this.jumlahStok <= this.stokMinimum;
    }

    @Override
    public String getJenis() {
        return "Bahan Baku";
    }

    // Method Overriding: menambah info milik Produk
    @Override
    public String getInfo() {
        String info = super.getInfo() + String.format(" | Stok: %.1f %s (min %.1f)", jumlahStok, satuan, stokMinimum);
        if (isStokMenipis()) {
            info = info + " [MENIPIS]";
        }
        return info;
    }
}
