package model;

import model.database.Tenant;
import model.database.appSchema;
import model.database.landlord;
import model.database.residence;

import java.sql.Connection;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) throws SQLException {
        appSchema checkschema = new appSchema();
        landlord mastede = new landlord("Khulekani","khulekaniszondo6@gmail.com","AlphaOmega");
        mastede.insert_information();
        int asibone = mastede.UniqueID();
        residence mastede_property = new residence("Alexandra",20,1000,"Njabulani","0822690384",asibone);
        mastede_property.insert_information();

    }
}
