package backend.database;

import java.sql.SQLException;

public interface Property {
    //void numberofRooms(int room_numbers) throws SQLException;
    void addProperty_info(String property_name,int number_of_rooms, int rent, String address,String contact);
    void roomStatus(String name, int room_no, String move_in_date, String employment_status,
                    String cellphone,String pay_day,String room_price,int debt,
                    String kin_name,String kin_number);

}


