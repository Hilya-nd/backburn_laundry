package project.pbo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Fadilah nurhasani
 */

public class RegisterSistem {

    public static boolean registerPelanggan(String nama, String email, String noTelp, String password) {

        if (nama.isEmpty() || email.isEmpty() || noTelp.isEmpty() || password.isEmpty()) {
            return false;
        }

        return UserManager.registerUser(nama, email, noTelp, password);
    }
}
