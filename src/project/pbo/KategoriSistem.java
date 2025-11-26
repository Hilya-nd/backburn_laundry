/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.pbo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prata
 */
public class KategoriSistem {

    private int id;
    private String nama;
    private double hargaPerKg;

    public KategoriSistem(int id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.hargaPerKg = harga;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    // ===================== LOAD KATEGORI =====================
    public static List<KategoriSistem> loadKategori() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT id_kategori, nama_kategori, harga_perkg FROM kategori ORDER BY id_kategori DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        List<KategoriSistem> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new KategoriSistem(
                    rs.getInt("id_kategori"),
                    rs.getString("nama_kategori"),
                    rs.getDouble("harga_perkg")
            ));
        }
        return list;
    }

    // ===================== TAMBAH KATEGORI =====================
    public static void tambahKategori(String nama, double harga) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO kategori(nama_kategori, harga_perkg) VALUES (?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, nama);
        pst.setDouble(2, harga);
        pst.executeUpdate();
    }

    public static int getIdByName(String nama) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT id_kategori FROM kategori WHERE nama_kategori = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, nama);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

}   
    