package model.database.UPDATES;

import model.database.CRUD.Tenant;
import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Change_Unit extends ConnectionAccess {
    public String tenant_name;
    public Integer tenant_unit;
    public String tenant_property;
    public Integer TenantId ;

    public Change_Unit(String tenant_name, Integer tenant_unit , String tenant_property,Integer tenantId){
        this.tenant_name = tenant_name;
        this.tenant_unit = tenant_unit;
        this.tenant_property = tenant_property;
        this.TenantId = tenantId;
    }
    public void setTenant_name(String tenant_name) {
        this.tenant_name = tenant_name;
    }

    public void setTenantId(Integer tenantId) {
        this.TenantId = tenantId;
    }


    public void setTenant_unit(Integer tenantUnit){
        this.tenant_unit = tenantUnit;

    }
    public  void setTenant_property(String property_name){
        this.tenant_property = property_name;

    }

    public void vacant_unit(Integer new_vacant_unit) {
        // 1. Corrected SQL: Removed the comma before WHERE.
        // The placeholder '?' for property_unit is used to FIND the unit,
        // not to set a new value (which is NULL).
        String SQL = """
               UPDATE properties SET occupation='no',
               tenant_user_id =NULL WHERE property_unit = ? AND property_name = ?
               """;

        try (PreparedStatement pstm = this.connection.prepareStatement(SQL)) {
            // 2. Setting parameters to identify the row to be updated (WHERE clause)
            // pstm.setInt(1, this.tenant_unit) sets the first '?' (property_unit)
            pstm.setInt(1, this.tenant_unit);
            // pstm.setString(2, this.tenant_property) sets the second '?' (property_name)
            pstm.setString(2, this.tenant_property);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Warning: No property unit found to vacate for unit " + this.tenant_unit);
            }

        } catch (SQLException e) {
            // Consider logging the error for debugging
            throw new RuntimeException("Error vacating unit: " + this.tenant_unit + " in " + this.tenant_property, e);
        }
    }


    public void Change_unit(){
           String SQL = """
                   UPDATE properties SET tenant_user_id = ?, occupation='yes' WHERE property_unit = ? and property_name =  ?
                   """;

        try (PreparedStatement pstm = this.connection.prepareStatement(SQL)) {
            pstm.setInt(1, this.TenantId);
            pstm.setInt(2,  this.tenant_unit);
            pstm.setString(3, this.tenant_property);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public  static void main(String args[]){
        Tenant tenant = new Tenant();


        String tenant_name = "Sis Room 4";
        Integer tenantId = tenant.tenant_ID(tenant_name);
        System.out.println("Funk " +  tenantId);
        String propertyName = "Thornville_rooms";
        Integer unitID = 6;

        Change_Unit UPDATES = new Change_Unit(tenant_name,unitID,propertyName,tenantId);
      //  UPDATES.vacant_unit(4);
        UPDATES.Change_unit();

    }
}



