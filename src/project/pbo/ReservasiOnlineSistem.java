package project.pbo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservasiOnlineSistem {

    // ====================================================================================
    // GET id_pelanggan berdasarkan id_user (FIX PENTING!)
    // ====================================================================================
    public static int getIdPelangganByUserId(int idUser) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT id_pelanggan FROM pelanggan WHERE id_user = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, idUser);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_pelanggan");
        }
        return -1; // belum terdaftar sebagai pelanggan
    }


    // ====================================================================================
    // TAMBAH RESERVASI BARU (DIPANGGIL DARI GUI SAAT USER KLIK "KIRIM")
    // ====================================================================================
    public static boolean tambahReservasi(int idUserLogin, String alamat, java.util.Date tanggal,
            String namaWilayah, String namaKategori) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseConnection.getConnection();

            // 1. KONVERSI id_user → id_pelanggan
            int idPelanggan = getIdPelangganByUserId(idUserLogin);
            if (idPelanggan == -1) {
                return false; // belum terdaftar
            }

            // 2. Cari ID wilayah berdasarkan nama
            int idWilayah = -1;
            for (WilayahSistem w : WilayahSistem.loadData()) {
                if (w.getNamaWilayah().equalsIgnoreCase(namaWilayah)) {
                    idWilayah = w.getIdWilayah();
                    break;
                }
            }

            // 3. Cari ID kategori berdasarkan nama
            int idKategori = KategoriSistem.getIdByName(namaKategori);

            if (idWilayah == -1 || idKategori == -1) {
                return false;
            }

            // 4. SQL INSERT reservasi
            String sql = "INSERT INTO reservasi_online "
                    + "(id_pelanggan, id_wilayah, id_kategori, alamat_lengkap, tanggal_reservasi, berat, total_harga, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);
            pst.setInt(1, idPelanggan);
            pst.setInt(2, idWilayah);
            pst.setInt(3, idKategori);
            pst.setString(4, alamat);

            Timestamp ts = new Timestamp(tanggal.getTime());
            pst.setTimestamp(5, ts);

            pst.setDouble(6, 0.0);   // berat belum diinput admin
            pst.setDouble(7, 0.0);   // total harga belum dihitung
            pst.setString(8, "diproses"); // status awal

            int affected = pst.executeUpdate();
            return affected > 0;

        } finally {
            if (pst != null) pst.close();
        }
    }


    // ====================================================================================
    // ADMIN UPDATE BERAT → TOTAL HARGA OTOMATIS
    // ====================================================================================
    public static boolean updateBeratDanHarga(int idReservasi, double berat) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        // 1. Get id_kategori dari reservasi
        String q = "SELECT id_kategori FROM reservasi_online WHERE id_reservasi = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, idReservasi);
        ResultSet rs = pst.executeQuery();

        if (!rs.next()) {
            return false;
        }
        int idKategori = rs.getInt("id_kategori");
        rs.close();
        pst.close();

        // 2. Ambil harga_perkg dari kategori
        String q2 = "SELECT harga_perkg FROM kategori WHERE id_kategori = ?";
        pst = conn.prepareStatement(q2);
        pst.setInt(1, idKategori);
        rs = pst.executeQuery();

        double hargaPerKg = 0.0;
        if (rs.next()) {
            hargaPerKg = rs.getDouble("harga_perkg");
        } else {
            return false;
        }
        rs.close();
        pst.close();

        // 3. Hitung total harga
        double total = berat * hargaPerKg;

        // 4. Update database
        String upd = "UPDATE reservasi_online SET berat = ?, total_harga = ?, status = ? WHERE id_reservasi = ?";
        pst = conn.prepareStatement(upd);
        pst.setDouble(1, berat);
        pst.setDouble(2, total);
        pst.setString(3, "diproses");
        pst.setInt(4, idReservasi);

        int aff = pst.executeUpdate();
        return aff > 0;
    }


    // ====================================================================================
    // GET RESERVASI MILIK USER (untuk halaman Riwayat)
    // ====================================================================================
    public static List<String> getReservasiByUser(int idUserLogin) throws SQLException {

        int idPelanggan = getIdPelangganByUserId(idUserLogin);
        if (idPelanggan == -1) {
            return new ArrayList<>();
        }

        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT id_reservasi, alamat_lengkap, tanggal_reservasi, status "
                   + "FROM reservasi_online WHERE id_pelanggan = ? ORDER BY id_reservasi DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, idPelanggan);

        ResultSet rs = pst.executeQuery();

        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(
                "ID:" + rs.getInt("id_reservasi") +
                " | " + rs.getString("alamat_lengkap") +
                " | " + rs.getTimestamp("tanggal_reservasi") +
                " | " + rs.getString("status")
            );
        }

        rs.close();
        pst.close();
        return list;
    }

}
