package schedule;

import model.database.CRUD.landlord;

import java.util.ArrayList;

public class PersonEmail {
    private Integer landlordId;
    private ArrayList<Integer> tenants_due;


    public PersonEmail(Integer landlordId,ArrayList<Integer> tenants_due){
        this.landlordId = landlordId;
        this.tenants_due = tenants_due;
    }

public String getUsername(){
        return new landlord().landlord_username(this.landlordId);
}


public String propertyName(){
        return new landlord().landlord_property_name(this.landlordId);
}

public String send_email(){
    StringBuilder email = new StringBuilder();
    ArrayList<Integer> due_units = this.tenants_due;
    email.append("Dear ").append(getUsername()).append(",\n\n");
    email.append("Subject: Rent Payment Reminder\n\n");

    // If propertyName() returns a String, remove the String.format
    email.append("Property: ").append(propertyName()).append("\n\n");

    for (Integer unit : due_units) {
        email.append("This is a friendly reminder that tenant for Unit ")
                .append(unit)
                .append(" has rent due today.\n");
    }

    email.append("\nA second reminder will be sent this afternoon if you have not logged in yet.\n ")
            .append("MYPROMAN will automatically start counting overdue days until the tenant has paid.\n\n");
    email.append("Thank you for your prompt attention.\n\n");
    email.append("Best regards,\n");
    email.append("PROMAN Team.\n");

    return email.toString();
}

}
