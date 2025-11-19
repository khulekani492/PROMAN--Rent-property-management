package model.database.CRUD;

import java.util.ArrayList;
import java.util.Objects;


public class Get_date  {

    public ArrayList<String> tenant_current_date(ArrayList<String> date){
        String regex = "[-]";

        if (Objects.equals(date.get(1), "null")){
            date.set(1,"0");
            return  date;
        };
        ArrayList<String> usuku = new ArrayList<>();
        String[] ToArray;
        try{
             ToArray = date.getLast().split(regex);
             for ( String i : ToArray){
                usuku.add(i);

            }

             if (Integer.parseInt(date.getFirst()) >  Integer.parseInt(usuku.get(2))){
                 date.set(1,"0");
                 return  date;
             }else {
                 Integer total_Number_of_days_due = Integer.parseInt(usuku.get(2)) - Integer.parseInt(date.getFirst());
                 date.set(1, String.valueOf(total_Number_of_days_due ));
             }


            return date;
        } catch (NullPointerException e) {
            date.set(1, String.valueOf(0));
            return date;
        }


    }

    static  void main(String[] args){
        Get_date test = new Get_date();
        ArrayList<String> aibo = new ArrayList<>();
        aibo.add("25");
        aibo.add("2025-11-19");


        System.out.println(  test.tenant_current_date(aibo));
    }
}
