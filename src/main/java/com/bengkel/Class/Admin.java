package com.bengkel.Class;

public class Admin extends User {

    public static class AdminCommands {
        public static final String help = "help"; 
        public static final String insertUser = "insertUser"; 
        public static final String deleteUser = "deleteUser";
        public static final String showUsers = "showUsers";
    }

    public Admin(String UserID, String Password, String nama) {
        super(UserID, Password, nama, true);
    }
}
