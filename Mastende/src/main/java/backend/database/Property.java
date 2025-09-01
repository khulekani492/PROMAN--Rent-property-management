package backend.database;

import java.sql.SQLException;

public interface Property {
    void numberofRooms(int room_numbers) throws SQLException;
    void roomStatus(String name, int room_no, int move_in_date, String employment_status);

}


