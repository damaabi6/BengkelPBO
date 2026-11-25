package com.bengkel;

import com.bengkel.Class.Karyawan.User;
import com.bengkel.MasterData.DataKaryawan;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Sistem Manajemen Bengkel");
        showLogin();
        stage.show();
    }

    // ===================== LOGIN =====================
    private void showLogin() {
        Label title = new Label("SISTEM MANAJEMEN BENGKEL - LOGIN");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField tfLogin = new TextField();
        tfLogin.setPromptText("ID atau Nama User");

        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password");

        Label status = new Label();
        status.setStyle("-fx-text-fill: red;");

        Button btnLogin = new Button("Login");
        Button btnExit = new Button("Keluar");

        Runnable doLogin = () -> {
            String input = tfLogin.getText().trim();
            String pass = pfPass.getText().trim();

            if (input.isEmpty() || pass.isEmpty()) {
                status.setText("ID/Nama dan Password harus diisi.");
                return;
            }

            User user = Main.login(input, pass);
            if (user == null) {
                status.setText("ID/Nama atau Password salah.");
                pfPass.clear();
            } else if (user.isAdmin()) {
                showAdmin(user);
            } else {
                showPetugas(user);
            }
        };

        btnLogin.setOnAction(e -> doLogin.run());
        pfPass.setOnAction(e -> doLogin.run()); // tekan Enter di kolom password
        btnExit.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, btnLogin, btnExit);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, title, tfLogin, pfPass, buttons, status);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 400, 280));
    }

    // ===================== MENU PETUGAS =====================
    private void showPetugas(User user) {
        Label info = new Label("Selamat datang, " + user.getUsername() + ".\nHak Akses: Petugas / Staff");
        Label todo = new Label("(Menu petugas belum diimplementasi)");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> showLogin());

        VBox root = new VBox(15, info, todo, btnLogout);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 450, 250));
    }

    // ===================== MENU ADMIN =====================
    private void showAdmin(User admin) {
        Label header = new Label("Menu Admin - " + admin.getUsername());
        header.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        // --- Form insert user ---
        Button btnTabelUsers = new Button("Tabel Users");
        Button btnLogout = new Button("Logout");
        Label status = new Label();

        btnTabelUsers.setOnAction(e -> adminShowUsers(admin));
        btnLogout.setOnAction(e -> showLogin());

        VBox actions = new VBox(10, header, btnTabelUsers, btnLogout, status);
        actions.setPadding(new Insets(10, 0, 0, 0));
        actions.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(actions, 400, 280));
    }

    private void adminShowUsers(User admin) {
        Label header = new Label("Tabel Users - " + admin.getUsername());
        header.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        // --- Tabel user ---
        TableView<User> table = new TableView<>();

        TableColumn<User, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<User, String> colNama = new TableColumn<>("Username");
        colNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));

        TableColumn<User, String> colAdmin = new TableColumn<>("Admin");
        colAdmin.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isAdmin() ? "Ya" : "Tidak"));

        table.getColumns().addAll(colId, colNama, colAdmin);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        Runnable refresh = () -> {
            Main.staticUsersList = DataKaryawan.getAllUsers();
            table.setItems(FXCollections.observableArrayList(Main.staticUsersList));
        };
        refresh.run();

        // --- Form insert user ---
        TextField tfNama = new TextField();
        tfNama.setPromptText("Nama user");
        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password");
        CheckBox cbAdmin = new CheckBox("Admin");
        Button btnTambah = new Button("Tambah User");

        Label status = new Label();

        btnTambah.setOnAction(e -> {
            String nama = tfNama.getText().trim();
            String pass = pfPass.getText().trim();

            if (nama.isEmpty() || pass.isEmpty()) {
                setStatus(status, "Nama dan password tidak boleh kosong.", false);
                return;
            }

            DataKaryawan.insertUser(nama, pass, cbAdmin.isSelected());
            refresh.run();

            tfNama.clear();
            pfPass.clear();
            cbAdmin.setSelected(false);
            setStatus(status, "Perintah Insert User dijalankan untuk '" + nama + "'.", true);
        });

        HBox form = new HBox(10, tfNama, pfPass, cbAdmin, btnTambah);
        form.setAlignment(Pos.CENTER_LEFT);

        // --- Tombol delete / refresh / logout ---
        Button btnHapus = new Button("Hapus User Terpilih");
        Button btnRefresh = new Button("Refresh");
        Button btnMenu = new Button("Menu");

        btnHapus.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                setStatus(status, "Pilih user di tabel terlebih dahulu.", false);
                return;
            }

            String selectedId = String.valueOf(selected.getId());
            if (selectedId.equals(String.valueOf(admin.getId()))) {
                setStatus(status, "Tidak dapat Delete user aktif.", false);
                return;
            }
            String selectedUsername = String.valueOf(selected.getUsername());
            if (selectedUsername.equals(String.valueOf("Admin1")) && selectedId.equals(String.valueOf("20326491"))) {
                setStatus(status, "Tidak dapat Delete user Admin1.", false);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus user '" + selected.getUsername() + "'?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    DataKaryawan.deleteUser(selectedId);
                    refresh.run();
                    setStatus(status, "Perintah Delete User dijalankan untuk '" + selected.getUsername() + "'.", true);
                }
            });
        });

        btnRefresh.setOnAction(e -> {
            refresh.run();
            setStatus(status, "Data user diperbarui.", true);
        });

        btnMenu.setOnAction(e -> showAdmin(admin));

        HBox actions = new HBox(10, btnHapus, btnRefresh, btnMenu);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox bottom = new VBox(10, new Label("Insert User Baru:"), form, actions, status);
        bottom.setPadding(new Insets(10, 0, 0, 0));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 10, 0));
        root.setCenter(table);
        root.setBottom(bottom);

        stage.setScene(new Scene(root, 650, 480));
    }

    private void adminShowMekanik() {

    }

    private void setStatus(Label label, String text, boolean ok) {
        label.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        label.setText(text);
    }
}