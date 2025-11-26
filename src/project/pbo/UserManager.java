package project.pbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserManager {

    private static User loggedUser;  // <== SIMPAN USER YANG LOGIN

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
            String sqlUser = "INSERT INTO user (nama, email, password, no_telp, role) "
                    + "VALUES (?, ?, ?, ?, 'pelanggan')";

            PreparedStatement pstUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            pstUser.setString(1, nama);
            pstUser.setString(2, email);
            pstUser.setString(3, password);
            pstUser.setString(4, noTelp);
            pstUser.executeUpdate();

            // Ambil ID User
            ResultSet rs = pstUser.getGeneratedKeys();
            int idUser = 0;
            if (rs.next()) {
                idUser = rs.getInt(1);
            }

            System.out.println("User inserted. ID: " + idUser);

            // INSERT KE PELANGGAN
            String sqlPelanggan = "INSERT INTO pelanggan (id_user, tgl_registrasi) VALUES (?, CURDATE())";
            PreparedStatement pstPelanggan = conn.prepareStatement(sqlPelanggan);
            pstPelanggan.setInt(1, idUser);
            pstPelanggan.executeUpdate();

            System.out.println("Pelanggan inserted.");

            return true;

        } catch (Exception e) {
            System.out.println("Error register: " + e.getMessage());
            return false;
        }
    }

    // LOGIN USER
    public static User loginUser(String email, String password) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                loggedUser = new User(
                        rs.getInt("id_user"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("no_telp"),
                        rs.getString("role")
                );
                return loggedUser;
            }

            return null;

        } catch (Exception e) {
            System.out.println("Error login: " + e.getMessage());
            return null;
        }
    }

    // ========= GETTER USER YANG LOGIN =========

    public static int getLoggedInUserId() {
        if (loggedUser != null) {
            return loggedUser.getIdUser();  // return id_user
        }
        return -1;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }


    // ========= GET id_pelanggan dari id_user =========

    public static int getLoggedInPelangganId() {
        int userId = getLoggedInUserId(); // id_user

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT id_pelanggan FROM pelanggan WHERE id_user = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_pelanggan");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; 
    }

}
