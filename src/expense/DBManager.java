package expense;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by juddn on 2016/01/21.
 */
public class DBManager {

    private static Connection conn;

    public static ArrayList<String> submitSQLQuery(String statement) {
        String sqlStatement = statement;
        conn = null;
        Statement stmt=null;
        ResultSet sqlResults=null;
        ArrayList<String> theResults = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            stmt = conn.createStatement();
            System.out.println(sqlStatement);
            sqlResults = stmt.executeQuery(sqlStatement);
//            while (sqlResults.next()) {
//                theResults.add(sqlResults.);
//            }
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(sqlResults!=null){
                    sqlResults.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return theResults;



    }

    public static void submitSQLUpdate(String statement) {
        String sqlStatement = statement;
        conn = null;
        System.out.println("submitting: " + sqlStatement);
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlStatement);

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
