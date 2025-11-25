/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.pbo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                // LOAD DRIVER -- WAJIB
                Class.forName("com.mysql.cj.jdbc.Driver");

                // KONEKSI KE DATABASE
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/backburnlaundry",
                        "root",
                        ""
                );

                System.out.println("Koneksi Berhasil!");

            } catch (Exception e) {
                System.out.println("Koneksi Gagal: " + e.getMessage());
            }
        }

        return conn;
    }
}
