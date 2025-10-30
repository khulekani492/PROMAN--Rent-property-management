package org.sonarEffect;
import java.sql.SQLException;

import static API.umuziAPI.startServer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        startServer(7070);
    }
}