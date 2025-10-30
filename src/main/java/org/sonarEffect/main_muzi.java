package org.sonarEffect;

import java.sql.SQLException;

import static API.umuziAPI.startServer;

public class main_muzi {
    static void main(String[] args) throws SQLException {
        startServer(6070);
    }
}
