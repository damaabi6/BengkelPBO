package com.bengkel.Class.Karyawan;

public class User {
    private String userID;
    private String password;
    private String username;
    private boolean adminAccess;
    
    protected User(String UserID, String username, String Password, boolean admin) {
        this.userID = UserID;
        this.username = username;
        this.password = Password;
        this.adminAccess = admin;
    }

    public String getId() { return userID; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public boolean isAdmin() { return adminAccess; }
}
