package model.database;

import java.sql.Connection;

//TODO logic for saving the money_made by the propertY in a particular month
//TODO Involves View -- controller holding information aboout tenant payment set a trigger to be the first day of the month
// Controller trigger-- send the update to the Model
// --Model is this class grossincome stores the month that ended gross Income
public class grossIncome extends  connectionAcess{
    public grossIncome(Connection connect) {
        super(connect);
    }
}
