package project.pbo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfflineSistem {

    // ===== FIELD DATA (MODEL) =====
    private int id;
    private String nama;
    private Date tanggal;
    private String kategori;     // nama kategori (bukan id)
    private double berat;
    private double total;
    private String status;

    // ===== CONSTRUCTOR =====
    public OfflineSistem(int id, String nama, Date tanggal, String kategori,
                         double berat, double total, String status) {
        this.id = id;
        this.nama = nama;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.berat = berat;
        this.total = total;
        this.status = status;
    }

    // ===== GETTERS =====
    public int getId() { return id; }
    public String getNama() { return nama; }
    public Date getTanggal() { return tanggal; }
    public String getKategori() { return kategori; }
    public double getBerat() { return berat; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }


    // ===== DAPATKAN ID KATEGORI DARI NAMA KATEGORI =====
    public static int getIdKategori(String namaKategori) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT id_kategori FROM kategori WHERE nama_kategori = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, namaKategori);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_kategori");
        }
        return 0;
    }


    // ===== DAPATKAN HARGA PER KG DARI NAMA KATEGORI =====
    public static double getHargaKategori(String namaKategori) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT harga_perkg FROM kategori WHERE nama_kategori = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, namaKategori);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getDouble("harga_perkg");
        }
        return 0;
    }

    // ===== TAMBAH DATA =====
    public static void tambah(String nama, Date tanggal, int idKategori,
                              double berat, double total, String status) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();
        String sql = """
            INSERT INTO reservasi_offline 
            (nama_pelanggan, id_kategori, tanggal_reservasi, berat, total_harga, status) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, nama);
        pst.setInt(2, idKategori);
        pst.setDate(3, tanggal);
        pst.setDouble(4, berat);
        pst.setDouble(5, total);
        pst.setString(6, status);

        pst.executeUpdate();
    }

    // ===== LOAD DATA KE TABEL =====
    public static List<OfflineSistem> loadOffline() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = """
            SELECT r.id_reservasi,
                   r.nama_pelanggan,
                   r.tanggal_reservasi,
                   k.nama_kategori,
                   r.berat,
                   r.total_harga,
                   r.status
            FROM reservasi_offline r
            JOIN kategori k ON r.id_kategori = k.id_kategori
            ORDER BY r.id_reservasi DESC
        """;

        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        List<OfflineSistem> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new OfflineSistem(
                rs.getInt("id_reservasi"),
                rs.getString("nama_pelanggan"),
                rs.getDate("tanggal_reservasi"),
                rs.getString("nama_kategori"),
                rs.getDouble("berat"),
                rs.getDouble("total_harga"),
                rs.getString("status")
            ));
        }
        return list;
    }

}
