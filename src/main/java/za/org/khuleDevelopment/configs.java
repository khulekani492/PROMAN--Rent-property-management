package za.org.khuleDevelopment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class configs {
      public  static Connection established_connection(){
          String url = "jdbc:sqlite3:Mastende.db";
          try {
               return DriverManager.getConnection(url);
          } catch (SQLException e) {
              throw new RuntimeException(e);

          }
      }

}
