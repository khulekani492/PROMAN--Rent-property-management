package model;

import model.database.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) {
        try {
            residence newoens = new  residence("Sunset Villas",20,4,5000,"lase","03283833",1);
            newoens.insert_information();
            Tenant newss = new Tenant(1,  "KILR",  Date.valueOf("2025-10-06"),   "yes",  "0823343233",  Date.valueOf("2020-10-06"), 5, 4500,  "d","09883888394"
            );
            newss.insert_information();
            System.out.println("Tenant inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
