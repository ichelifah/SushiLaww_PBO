# SushiLaww - Java Console Application

## Kebutuhan
- JDK 17 atau lebih baru
- Apache NetBeans dengan dukungan Maven (opsional untuk membuka project)

## Menjalankan dari terminal
Buka terminal pada folder project (folder yang berisi pom.xml), lalu:

```bash
javac -d target/classes src/main/java/com/mycompany/sushilaww/*.java
java -cp target/classes com.mycompany.sushilaww.Main
```

Windows PowerShell / CMD juga dapat menggunakan perintah yang sama.

## Menjalankan di Apache NetBeans
1. File > Open Project.
2. Pilih folder `SushiLaww_NetBeans`.
3. Klik kanan project > Run.
4. Jika diminta memilih main class, pilih `com.mycompany.sushilaww.Main`.

Project ini merupakan aplikasi console, sehingga hasilnya tampil di tab Output/terminal NetBeans, bukan browser.
