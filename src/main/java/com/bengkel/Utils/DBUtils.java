package com.bengkel.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    
    public static Connection getConnection() {
        try {
            String currentDir = System.getProperty("user.dir");
            
            File dbFile = new File(currentDir, "databaseBengkel.db");
            
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            
            // Debug untuk cek path database
            // System.out.println("[DB Info] Mengakses database di: " + dbFile.getAbsolutePath());

            return DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println("[DBUtils Error] Gagal terhubung ke database: " + e.getMessage());
            return null;
        }
    }

    public static void dropTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("[DBUtils] Tabel '" + tableName + "' berhasil dihapus dari database.");
        } catch (SQLException e) {
            System.out.println("[DBUtils Error] Gagal menghapus tabel: " + e.getMessage());
        }
    }

    // Jangan pernah panggil, kopas dan sesuaiin di main klo mau buat tabel
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     " id TEXT PRIMARY KEY, " +
                     " password TEXT NOT NULL, " +
                     " nama TEXT NOT NULL, " +
                     " admin BOOLEAN)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("[Helper] Tabel 'users' berhasil disiapkan.");
        } catch (SQLException e) {
            System.out.println("[Helper Error] Gagal membuat tabel: " + e.getMessage());
        }
    }

}
