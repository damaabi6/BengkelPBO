package com.bengkel.Utils;


public class TextUtils {
    public static final String RESETCOLOR = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    public static void printCol(String Color, String tex) {
        System.out.print(Color + tex + RESETCOLOR);
    }

    public static void printAddresed(String Address, String Color,String tex) {
        System.out.print(Color + Address + RESETCOLOR + " " + tex);
    }
    
    public static void printlnCol(String Color, String tex) {
        System.out.println(Color + tex + RESETCOLOR);
    }

    public static void printlnAddresed(String Address, String Color,String tex) {
        System.out.println(Color + Address + RESETCOLOR + " " + tex);
    }
    
}
