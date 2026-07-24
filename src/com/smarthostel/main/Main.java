package com.smarthostel.main;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Welcome to Smart Hostel Management System");
            ConsoleMenu menu = new ConsoleMenu();
            menu.start();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
