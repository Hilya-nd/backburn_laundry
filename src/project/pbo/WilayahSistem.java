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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WilayahSistem {

    // ====== Atribut (Model) ======
    private int idWilayah;
    private String namaWilayah;
    private double jarak;

    // ====== Constructor (Penerapan PBO) ======
    public WilayahSistem(int idWilayah, String namaWilayah, double jarak) {
        this.idWilayah = idWilayah;
        this.namaWilayah = namaWilayah;
        this.jarak = jarak;
    }

    // ===== Getter =====
    public int getIdWilayah() { return idWilayah; }
    public String getNamaWilayah() { return namaWilayah; }
    public double getJarak() { return jarak; }

    // =====================================================================
    // ========================   LOGIC DATABASE   ==========================
    // =====================================================================

    // ====== LOAD DATA WILAYAH ======
    public static List<WilayahSistem> loadData() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT id_wilayah, nama_wilayah, jarak FROM wilayah";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        List<WilayahSistem> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new WilayahSistem(
                    rs.getInt("id_wilayah"),
                    rs.getString("nama_wilayah"),
                    rs.getDouble("jarak")
            ));
        }

        return list;
    }

    // ====== TAMBAH WILAYAH ======
    public static void tambah(int id, String nama, double jarak) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO wilayah (id_wilayah, nama_wilayah, jarak) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, id);
        pst.setString(2, nama);
        pst.setDouble(3, jarak);

        pst.executeUpdate();
    }

    // ====== HAPUS WILAYAH ======
    public static void hapus(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM wilayah WHERE id_wilayah=?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, id);
        pst.executeUpdate();
    }
}