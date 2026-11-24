# SushiLaww - Aplikasi Restoran Sushi

Aplikasi pencatatan penjualan dan stok bahan baku restoran sushi UMKM.
Project akhir Pemrograman Berorientasi Objek.

## Kebutuhan
- JDK 21
- Apache NetBeans (Maven)

## Menjalankan Aplikasi
Versi GUI (JavaFX):
1. File > Open Project, pilih folder `SushiLaww_NetBeans`.
2. Klik kanan project > Run.
3. Atau lewat terminal: `mvn javafx:run`

Versi console:
Klik kanan `Main.java` > Run File.

## Fitur
- Transaksi penjualan, stok bahan berkurang otomatis sesuai resep menu
- Daftar menu (tambah menu beserta resep, hapus menu)
- Stok bahan baku (tambah, ubah, hapus, stok masuk/keluar, riwayat stok)
- Laporan penjualan harian dan total pendapatan

Data tersimpan di file `sushilaww_data.dat`, riwayat stok di `riwayat_stok.txt`.
