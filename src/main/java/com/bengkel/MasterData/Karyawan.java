package com.bengkel.MasterData;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bengkel.Class.User;
import com.bengkel.Utils.DBUtils;
import com.bengkel.Main;
import com.bengkel.Class.Admin;
import com.bengkel.Class.Petugas;

public class Karyawan {
    
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomId() {
        // Menghasilkan angka acak diformat menjadi 8 digit
        int randomNumber = RANDOM.nextInt(100000000);
        return String.format("%08d", randomNumber);
    }

    public static List<User> getAllUsers() {
        List<User> daftarUser = new ArrayList<>();
        String sql = "SELECT id, username, password, admin FROM users";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Looping setiap baris hasil query database
            while (rs.next()) {
                String id = rs.getString("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                boolean admin = rs.getBoolean("admin");

                // Buat objek User baru dan masukkan ke list
                User user;
                if (admin) {
                    user = new Admin(id, username, password);
                } else {
                    user = new Petugas(id, username, password);
                }

                daftarUser.add(user);
            }
            System.out.println("[KaryawanDB] Berhasil memuat/memperbarui data user dari database.");

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Gagal mengambil data user: " + e.getMessage());
        }

        return daftarUser;
    }

    public static void insertUser(String username, String password, boolean admin) {
    // Query untuk insert user baru
    String insertSql = "INSERT INTO users (id, username, password, admin) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection()) {


            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, generateRandomId());
                insertStmt.setString(2, username);
                insertStmt.setString(3, password);
                insertStmt.setBoolean(4, admin);

                insertStmt.executeUpdate();
                System.out.println("[KaryawanDB] Data user " + username + " berhasil ditambahkan.");
                
                // Update static list users di Main
                Main.staticUsersList = getAllUsers();
            }

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Gagal insert data: " + e.getMessage());
        }
    }

    public static void insertMekanik(String nama) {
        String sql = "INSERT INTO mekanik (id, nama) VALUES (?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, generateRandomId());
            pstmt.setString(2, nama);

            pstmt.executeUpdate();
            System.out.println("[KaryawanDB] Data mekanik " + nama + " berhasil ditambahkan.");

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Gagal insert data: " + e.getMessage());
        }
    }

    public static void deleteUser(String inputUser) {
        if(inputUser.equals("Admin1")) {
            System.out.println("[KaryawanDB Error] User 'Admin1' tidak dapat dihapus");
            return;
        }
        String sql = "DELETE FROM users WHERE (id = ? OR nama = ?)";

        try (Connection conn = DBUtils.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, inputUser);
            pstmt.setString(2, inputUser);
            
            int barisTerhapus = pstmt.executeUpdate();
            
            if (barisTerhapus > 0) {
                System.out.println("[KaryawanDB] Berhasil menghapus user dengan ID/Nama: " + inputUser);
                // Update static list users di Main
                Main.staticUsersList = getAllUsers();
            } else {
                System.out.println("[KaryawanDB] Data dengan ID/Nama tersebut tidak ditemukan.");
            }

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Gagal menghapus data: " + e.getMessage());
        }
    }

     public static void deleteMekanik(String idMekanik) {
        String sql = "DELETE FROM mekanik WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idMekanik);
            
            int barisTerhapus = pstmt.executeUpdate();
            
            if (barisTerhapus > 0) {
                System.out.println("[KaryawanDB] Berhasil menghapus mekanik dengan ID: " + idMekanik);
            } else {
                System.out.println("[KaryawanDB] Data dengan ID tersebut tidak ditemukan.");
            }

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Gagal menghapus data: " + e.getMessage());
        }
    }

    public static void tampilkanDataUsers() {
        String sql = "SELECT * FROM users";

        try (Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n----------- USERS LIST -----------");
            while (rs.next()) {
                System.out.println("Nama: " + rs.getString("nama"));
                System.out.println("ID  : " + rs.getString("id"));
                System.out.println("Pass: " + rs.getString("password"));
                String aksesAdmin =  rs.getBoolean("admin") ? "TRUE" : "FALSE";
                System.out.println("Admin Access: " + aksesAdmin);
                System.out.println("----------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Error saat mengambil data: " + e.getMessage());
        }
    }

    public static void tampilkanDataMekanik() {
        String sql = "SELECT * FROM mekanik";

        try (Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Nama: " + rs.getString("nama"));
                System.out.println("ID  : " + rs.getString("id"));
            }

        } catch (SQLException e) {
            System.out.println("[KaryawanDB Error] Error saat mengambil data: " + e.getMessage());
        }
    }

}
