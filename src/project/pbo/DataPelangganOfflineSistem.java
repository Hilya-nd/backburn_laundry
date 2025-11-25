/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.pbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author prata
 */
public class DataPelangganOfflineSistem {
    public static void updateStatus(int idReservasi, String statusBaru) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE reservasi_offline SET status=? WHERE id_reservasi=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, statusBaru);
        pst.setInt(2, idReservasi);
        pst.executeUpdate();
    }
}
