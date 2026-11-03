package com.bengkel.Class;

public class User {
    private String userID;
    private String password;
    private String nama;
    private boolean adminAccess;
    
    protected User(String UserID, String Password, String nama, boolean admin) {
        this.userID = UserID;
        this.password = Password;
        this.nama = nama;
        this.adminAccess = admin;
    }

    public String getId() { return userID; }
    public String getPassword() { return password; }
    public String getNama() { return nama; }
    public boolean isAdmin() { return adminAccess; }
}
