package model;

import model.database.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) {
        try {
            landlord news = new landlord("khulekani","khulekaniszondo@gmail.com","that96");
            news.insert_information();
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
