package expense;

import javafx.scene.control.Alert;

import java.sql.*;

public class UserManager {

    private static User user;

    public static void createNewUser(String first, String last, String name) {
        String fir = first;
        String las = last;
        String nam = name;
        submitNewUserToDB(fir, las, nam);
    }

    private static void submitNewUserToDB(String firstName, String lastName, String userName) {
        String last = lastName;
        String first = firstName;
        String userNam = userName;

        Connection conn = null;
        String existingUserCheck = null;
//        Check DB for existing username
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select username from users where username='"+userNam+"'");
            while (results.next()){
                existingUserCheck = results.getString("username");
            }
//            If username entered does NOT exist, enter it as a new row to users table
            if (existingUserCheck == null){
                stmt.executeUpdate("insert into users (LastName, FirstName, UserName) Values ('"+last+"', '"+first+"','"+userNam+"')");
            } else {
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Duplicate User");
                warning.setContentText("That UserName Already Exists.  Please Enter A Different UserName");
                warning.showAndWait();
            }
            System.out.println("check:  "+existingUserCheck);
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

    public static User getUser(){
        return user;
    }

    public static void userLogin(String attemptedUserName) {
        String userToQuery = attemptedUserName;
        Connection conn = null;
        String existingUserCheck = null;
//        Check database if submitted username exists
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select username from users where username='"+userToQuery+"'");
            while (results.next()){
                existingUserCheck = results.getString("username");
            }
            if (existingUserCheck == null){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Non Existing User");
                warning.setContentText("That UserName Does Not Exist.  Please Create A New User");
                warning.showAndWait();
            } else {
//                if username exists, pulls the details of the row and creates User object
                ResultSet existUser = stmt.executeQuery("select lastname, firstname, username, userID from users where username='"+userToQuery+"'");
                String lastName = null;
                String firstName = null;
                String userName = null;
                int userID=0;
                while (existUser.next()){
                    firstName = existUser.getString("firstname");
                    lastName = existUser.getString("lastname");
                    userName = existUser.getString("username");
                    userID = existUser.getInt("userID");
                }
                user = new User(firstName, lastName, userName, userID);
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
