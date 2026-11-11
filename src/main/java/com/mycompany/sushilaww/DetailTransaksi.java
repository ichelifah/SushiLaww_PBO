package com.mycompany.sushilaww;

// Satu baris pesanan: menu apa dan berapa porsi
public class DetailTransaksi {
    private Menu menu;
    private int jumlah;

    public DetailTransaksi(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public int getJumlah() {
        return this.jumlah;
    }

    public double getSubtotal() {
        return menu.getHarga() * jumlah;
    }
}
