package com.mycompany.sushilaww;

// Custom Exception (Pert. 10)
public class StokTidakCukupException extends Exception {
    public StokTidakCukupException(String pesan) {
        super(pesan);
    }
}
