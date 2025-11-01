package model.database;

import java.sql.SQLException;

//TODO logic for saving the money_made by the property in a particular month
//TODO Involves View -- controller holding information about tenant payment set a trigger to be the first day of the month
// Controller trigger-- send the updates to the Model --View update the dashboard
// --Model is this class grossincome stores the month that ended gross Income
public class grossIncome extends ConnectionAccess {
    public grossIncome() throws SQLException {
        super();
    }
}
