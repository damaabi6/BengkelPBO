package com.bengkel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.Class.Admin;
import com.bengkel.Class.Petugas;
import com.bengkel.Class.User;
import com.bengkel.Class.Admin.AdminCommands;
import com.bengkel.MasterData.Karyawan;
import com.bengkel.Utils.DBUtils;
import com.bengkel.Utils.TextUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.bengkel.Utils.DBUtils.getConnection;


public class Main {

    public static List<User> staticUsersList = new ArrayList<>();
    public static List<Karyawan> staticKaryawansList = new ArrayList<>();

    public static void main(String[] args) {

        DBUtils.dropTable("mekanik");
        // Jangan dihapus, membaca data user dari DB
        staticUsersList = Karyawan.getAllUsers();
        Application.launch(MainApp.class, args);
    }

    static void printMain(String tex, boolean Succeed) {
        String Color = Succeed ? TextUtils.BLUE : TextUtils.RED;
        TextUtils.printAddresed("[Main]", Color, tex);
    }

    static void printMain(String tex) {
        String Color = TextUtils.BLUE;
        TextUtils.printAddresed("[Main]", Color, tex);
    }

    static void printlnMain(String tex, boolean Succeed) {
        String Color = Succeed ? TextUtils.BLUE : TextUtils.RED;
        TextUtils.printlnAddresed("[Main]", Color, tex);
    }

    static void printlnMain(String tex) {
        String Color = TextUtils.BLUE;
        TextUtils.printlnAddresed("[Main]", Color, tex);
    }

    static User login(String inputLogin, String inputPassword) {
        // Query menggunakan OR untuk mengecek apakah input cocok dengan id OR nama
        String sql = "SELECT id, username, password, admin FROM users WHERE (id = ? OR username = ?) AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Parameter 1 untuk ID, Parameter 2 untuk Nama (keduanya diisi argumen inputLogin)
            pstmt.setString(1, inputLogin);
            pstmt.setString(2, inputLogin);
            pstmt.setString(3, inputPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");
                    String username = rs.getString("username");
                    boolean admin = rs.getBoolean("admin");

                    System.out.println("[Login] Berhasil masuk sebagai: '" + username + "' (ID: " + id + ")");
                    User user;
                    if(admin) {
                        user = new Admin(id, username, password);
                    }
                    else {
                        user = new Petugas(id, username, password);
                    }
                    return user; // return User pengguna
                }
            }

        } catch (SQLException e) {
            System.out.println("[Login Error] Gagal melakukan proses login: " + e.getMessage());
        }

        System.out.println("[Login] ID/Nama atau Password salah. Silakan coba lagi");
        return null;
    }

}