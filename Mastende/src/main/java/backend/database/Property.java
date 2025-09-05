package backend.database;

import java.sql.SQLException;

public interface Property {
    //void numberofRooms(int room_numbers) throws SQLException;
    void addProperty_info(String property_name,String number_of_rooms, String rent, String address,String contact);
    void roomStatus(String name, int room_no, String move_in_date, String employment_status, String cellphone,String pay_day);


}


