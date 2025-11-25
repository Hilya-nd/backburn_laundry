/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.pbo;

public class User {

    private int idUser;
    private String nama;
    private String email;
    private String password;
    private String noTelp;
    private String role;

    public User(int idUser, String nama, String email, String password, String noTelp, String role) {
        this.idUser = idUser;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.noTelp = noTelp;
        this.role = role;
    }

    public int getIdUser() { return idUser; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNoTelp() { return noTelp; }
    public String getRole() { return role; }
}