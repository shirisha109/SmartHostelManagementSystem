package com.smarthostel.model;

import com.smarthostel.config.EnvConfig;
import com.smarthostel.main.WebServer;

public class Main {
    public static void main(String[] args) {
        try {
            EnvConfig.load();
            WebServer.start();
        } catch (Exception e) {
            System.err.println("Unable to start Smart Hostel web application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
