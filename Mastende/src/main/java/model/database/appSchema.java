package model.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Foundation Schema for Rental Management Application
 *
 * Entity Relationship Model:
 *  User (Landlord) 1---* Property 1---* Tenant
 *
 * Tables:
 *  - Users: stores landlord (signed-up users) information
 *  - Property: stores properties owned by landlords
 *  - Tenants: stores tenant information tied to a property
 *
 * Relationships:
 *  - Users → Property : One landlord can own multiple properties
 *  - Property → Tenants : One property can have multiple tenants
 *
 * Foreign Keys:
 *  - Property.userId → Users.id
 *  - Tenants.propertyId → Property.id
 */

public class appSchema extends connectionAcess {
    public appSchema() throws SQLException {
        super();
        applicationSchema();
    }

    private void applicationSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            String Users = """
                         CREATE TABLE IF NOT EXISTS Users (
                                     id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                     user_name TEXT NOT NULL,
                                     user_email TEXT UNIQUE,
                                     password TEXT NOT NULL,
                                     propertyId INTEGER NULL,
                                     FOREIGN KEY (propertyId) REFERENCES property(id)
                                 );
                    """;

            String residenceTable = """
                         CREATE TABLE IF NOT EXISTS property (
                                     id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                     property_name TEXT NOT NULL UNIQUE,
                                     number_of_rooms INTEGER NOT NULL,
                                     rent INTEGER NOT NULL,
                                     address TEXT,
                                     contact TEXT,
                                     UserId INTEGER,
                                     FOREIGN KEY (UserId) REFERENCES Users(id)
                                     );
                    """;


            String tenantsTable = """
                           CREATE TABLE IF NOT EXISTS tenants (
                                       id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                       propertyId INTEGER NOT NULL,
                                       name TEXT,
                                       move_in DATE NOT NULL,
                                       move_out DATE,
                                       employment TEXT NOT NULL,
                                       cell_number TEXT NOT NULL,
                                       pay_day DATE NOT NULL,
                                       room_number INTEGER NOT NULL UNIQUE,
                                       Room_price INTEGER,
                                       debt INTEGER,
                                       kin_name Text,
                                       kin_number Text,
                                       FOREIGN KEY (propertyId) REFERENCES property(id)
                                   );
                    """;

            String grossIncome = """
                           CREATE TABLE IF NOT EXISTS gross_income (
                                       id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                       propertyId INTEGER NOT NULL,
                                       month_name TEXT,
                                       Profit TEXT,
                                       Loss TEXT,
                                       FOREIGN KEY (propertyId) REFERENCES Users(id)
                                   );
                    """;
            stmt.execute(Users);
            stmt.execute(tenantsTable);
            stmt.execute(residenceTable);
            stmt.execute(grossIncome);
        }
    }
}