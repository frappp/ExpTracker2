package expense;

import java.sql.*;

/**
 * Created by juddn on 2/2/2016.
 */
public class ReportWindowController {

    public void initialize(){
        System.out.println("hi");
        showExpenses();
    }

//    public void ReportWindowController(){
//        System.out.println("hi");
//        showExpenses();
//    }


    public void showExpenses(){
        Connection conn = null;
        int userID = UserManager.getUser().getUserID();
        String expDate;
        String itemName;
        int itemCost;
        String catName;
        String catType;

        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();

            ResultSet results = stmt.executeQuery("select entrydate, itemname, itemcost, catname, cattype " +
                    "from expenselog join categories on expenselog.catid = categories.catid " +
                    "where expenselog.userid = '"+userID+"'");
            while (results.next()){
                expDate = results.getString("entryDate");
                itemName = results.getString("itemName");
                itemCost = results.getInt("itemCost");
                catName = results.getString("catname");
                catType = results.getString("catType");
                System.out.println(expDate+itemName+itemCost+catName+catType);
            }
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
