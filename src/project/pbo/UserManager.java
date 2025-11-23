    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
   package project.pbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserManager {

    // REGISTER USER BARU
    public static boolean registerUser(String nama, String email, String noTelp, String password) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // CEK EMAIL SUDAH ADA
            String check = "SELECT * FROM user WHERE email = ?";
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, email);
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next()) {
                System.out.println("Email sudah ada!");
                return false;
            }

            // INSERT KE USER
            String sqlUser = "INSERT INTO user (nama, email, password, no_telp, alamat, role) "
                    + "VALUES (?, ?, ?, ?, '', 'pelanggan')";

            PreparedStatement pstUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            pstUser.setString(1, nama);
            pstUser.setString(2, email);
            pstUser.setString(3, password);
            pstUser.setString(4, noTelp);
            pstUser.executeUpdate();

            // Ambil ID User
            ResultSet rs = pstUser.getGeneratedKeys();
            int idUser = 0;
            if (rs.next()) idUser = rs.getInt(1);

            System.out.println("User inserted. ID: " + idUser);

            // INSERT KE PELANGGAN
            String sqlPelanggan = "INSERT INTO pelanggan (id_user, alamat, wilayah, tgl_registrasi) "
                    + "VALUES (?, '', '', CURDATE())";

            PreparedStatement pstPelanggan = conn.prepareStatement(sqlPelanggan);
            pstPelanggan.setInt(1, idUser);
            pstPelanggan.executeUpdate();

            System.out.println("Pelanggan inserted.");

            return true;

        } catch (Exception e) {
            System.out.println("Error register: " + e.getMessage()); // tampilkan pesan jelas
            return false;
        }
    }
}
    