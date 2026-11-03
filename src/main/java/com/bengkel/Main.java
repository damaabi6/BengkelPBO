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


public class Main {

    // List static user dari DB, jika tabel users terjadi perubahan list ini harus diupdate (untuk insertUser dan deleteUser dari Karyawan sudah otomatis)
    public static List<User> staticUsersList = new ArrayList<>();
    public static void main(String[] args) {
       
        // Jangan dihapus, membaca data user dari DB
        staticUsersList = Karyawan.getAllUsers();

        Scanner scanner = new Scanner(System.in);

        // debug list user
        // System.out.println("\n--- ISI DARI STATIC USERS LIST ---");
        // Karyawan.tampilkanDataUsers();
        
        boolean loginBerhasil = false;

        System.out.println("\n==========================================");
        System.out.println("     SISTEM MANAJEMEN BENGKEL - LOGIN     ");
        System.out.println("==========================================");

        while (!loginBerhasil) {
            System.out.print("\n[Main] Masukkan ID atau Nama User (ketik 'exit' untuk keluar): ");
            String inputLogin = scanner.nextLine().trim();

            // Opsi keluar dari program jika user mengetik 'exit'
            if (inputLogin.equalsIgnoreCase("exit")) {
                System.out.println("[Main] Program dihentikan");
                break;
            }

            System.out.print("[Main] Masukkan Password: ");
            String inputPassword = scanner.nextLine().trim();

            User userAktif = login(inputLogin, inputPassword);

            if (userAktif != null) {
                loginBerhasil = true; // stop while loop
                System.out.println("\n-----------------------------------------");
                System.out.println("Login Sukses! Selamat datang, " + userAktif.getNama() + ".");
                
                if (userAktif.isAdmin()) {
                    System.out.println("Hak Akses: Admin access");
                    System.out.println("-----------------------------------------");
                    MenuAdmin(scanner, userAktif);
                } else {
                    System.out.println("Hak Akses: Petugas / Staff");
                    System.out.println("-----------------------------------------");
                }

            }
        }

        scanner.close();

    }


    static User login(String inputLogin, String inputPassword) {
        // Query menggunakan OR untuk mengecek apakah input cocok dengan id OR nama
        String sql = "SELECT id, password, nama, admin FROM users WHERE (id = ? OR nama = ?) AND password = ?";

        try (Connection conn = DBUtils.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Parameter 1 untuk ID, Parameter 2 untuk Nama (keduanya diisi argumen inputLogin)
            pstmt.setString(1, inputLogin);
            pstmt.setString(2, inputLogin);
            pstmt.setString(3, inputPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");
                    String nama = rs.getString("nama");
                    boolean admin = rs.getBoolean("admin");

                    System.out.println("[Login] Berhasil masuk sebagai: '" + nama + "' (ID: " + id + ")");
                    User user;
                    if(admin) {
                        user = new Admin(id, password, nama);
                    }
                    else {
                        user = new Petugas(id, password, nama);
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

    static void MenuPetugas() {

    }

    static void MenuAdmin(Scanner scanner, User admin) {
        System.out.println("\n[Main] Menu Admin " + admin.getNama());
        while (true) {
            System.out.print("[Main] Masukkan command Admin (ketik 'help' untuk list command): ");
            String inputCommand = scanner.nextLine().trim();
            // Opsi keluar dari program jika user mengetik 'exit'
            if (inputCommand.equalsIgnoreCase("exit")) {
                System.out.println("[Main] Program dihentikan.");
                break;
            }
            
            if (inputCommand.equalsIgnoreCase(AdminCommands.help)) {
                System.out.println("[Main] Command List: ");
                System.out.println(AdminCommands.showUsers + " : Tampilkan seluruh user pada database");
                System.out.println(AdminCommands.insertUser + " : Insert user baru ke database");
                System.out.println(AdminCommands.deleteUser + " : Delete user baru ke database");
                System.out.println(AdminCommands.help + " : Tampilkan seluruh command admin");
                System.out.println("exit : Keluar dari program");
            }

            if (inputCommand.equalsIgnoreCase(AdminCommands.insertUser)) {
                System.out.println("[Main] Command 'Insert User' terdeteksi");
                
                // Input nama user
                String namaUser = "";
                while (namaUser.isEmpty() || namaUser.isBlank()) {
                    System.out.print("[Main] Masukkan nama user: ");
                    namaUser = scanner.nextLine().trim();
                }
                
                // Input password user
                String passUser = "";
                while (passUser.isEmpty() || passUser.isBlank()) {
                    System.out.print("[Main] Masukkan password: ");
                    passUser = scanner.nextLine().trim();
                }
                
                // Input hak akses admin (1 true, 0 false)
                boolean adminAcccesUser = false;
                while (true) {
                    System.out.print("[Main] Masukkan status admin (1 = Ya, 0 = Tidak): ");
                    String inputStatus = scanner.nextLine().trim();
                    if (inputStatus.equals("1")) {
                        adminAcccesUser = true;
                        break;
                    } else if (inputStatus.equals("0")) {
                        adminAcccesUser = false;
                        break;
                    } else {
                        System.out.println("[Main Error] Masukkan angka 1 atau 0!");
                    }
                }
                
                // insert user ke database
                Karyawan.insertUser(namaUser, passUser, adminAcccesUser);
            }

            if (inputCommand.equalsIgnoreCase(AdminCommands.deleteUser)) {
                System.out.println("[Main] Command 'Delete User' terdeteksi");
                
                // Input nama user
                String inputUser = "";
                while (inputUser.isEmpty() || inputUser.isBlank()) {
                    System.out.print("[Main] Masukkan nama/id user: ");
                    inputUser = scanner.nextLine().trim();
                }

                if (inputUser.equals(admin.getNama()) || inputUser.equals(admin.getId())) {
                    System.out.println("[Main Error] Tidak dapat Delete user aktif");
                } else {
                    Karyawan.deleteUser(inputUser);
                }
            }

            if (inputCommand.equalsIgnoreCase(AdminCommands.showUsers)) {
                Karyawan.tampilkanDataUsers();
            }
        }
    }
}