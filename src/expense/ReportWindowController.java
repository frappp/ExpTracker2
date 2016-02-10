package expense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by juddn on 2/2/2016.
 */
public class ReportWindowController {



    @FXML
    GridPane entryDateGridPane;
    @FXML
    GridPane expensesGridPane;
    @FXML
    GridPane incomesGridPane;
    @FXML
    GridPane reportMainPane;
    @FXML
    GridPane delButtonColumn;
    @FXML
    Button showDeleteButtonsButton;
    public static ArrayList<ExpenseEntry> expenseList = new ArrayList<>();

    public void initialize(){
//        System.out.println("hi");
//        reportMainPane.setGridLinesVisible(true);
//        incomesGridPane.setGridLinesVisible(true);
//        expensesGridPane.setGridLinesVisible(true);
//        entryDateGridPane.setGridLinesVisible(true);
        showExpenses();
    }

//    public void ReportWindowController(){
//        System.out.println("hi");
//        showExpenses();
//    }


    public void showExpenses(){
        Connection conn = null;
        expenseList = new ArrayList<>();
        int userID = UserManager.getUser().getUserID();
        String expDate;
        String itemName;
        int itemCost;
        String catName;
        String catType;
        int expID;
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();

            ResultSet results = stmt.executeQuery("select entrydate, itemname, itemcost, catname, cattype, expenseid " +
                    "from expenselog join categories on expenselog.catid = categories.catid " +
                    "where expenselog.userid = '"+userID+"'" +
                    "order by entrydate");
            while (results.next()){
                expDate = results.getString("entryDate");
                itemName = results.getString("itemName");
                itemCost = results.getInt("itemCost");
                catName = results.getString("catname");
                catType = results.getString("catType");
                expID = results.getInt("expenseid");
//                System.out.println(expDate+itemName+itemCost+catName+catType+expID);
                expenseList.add(new ExpenseEntry(expDate, itemName, itemCost, catName, catType, expID));

            }
            for (ExpenseEntry expent: expenseList){
                expent.getDeleteMeButton().setOnAction(e -> deleteExpense(expenseList.indexOf(expent),expent.getExpID()));
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

        displayExpenseEntries();


    }

    private void displayExpenseEntries() {
        String categoryTypeSelection;
        delButtonColumn.getChildren().clear();
        entryDateGridPane.getChildren().clear();
        expensesGridPane.getChildren().clear();
        incomesGridPane.getChildren().clear();
        int i = 0;
        for (ExpenseEntry entry: expenseList){
//            System.out.println(entry.getExpDate()+" "+entry.getItemName()+" "+entry.getItemCost()+" "+entry.getCatName()+" "+entry.getExpID()+" "+entry.getCatType());
            categoryTypeSelection = entry.getCatType();
            int j = 0;
            delButtonColumn.add(entry.getDeleteMeButton(),0, i);
            for (Label lab: entry.getExpenseLabelArray()){
                entryDateGridPane.add(new Label(entry.getExpDate()), 0, i);
                switch (categoryTypeSelection){
                    case "Expense":
                        expensesGridPane.add(lab,j, i);
                        incomesGridPane.add(new Label(" "), j, i);
                        break;
                    case "Income":
                        incomesGridPane.add(lab,j, i);
                        expensesGridPane.add(new Label(" "), j, i);
                        break;
                }
                j++;
            }
            i++;
        }
    }

    public void showDeleteButtons(ActionEvent ae) throws Exception{
        if (!delButtonColumn.isVisible()){
            delButtonColumn.setVisible(true);
            showDeleteButtonsButton.setText("o");
        } else {
            delButtonColumn.setVisible(false);
            showDeleteButtonsButton.setText("X");
        }



    }

    public void deleteExpense(int expArrayIndex, int expDBID){
        int arrayToDelete = expArrayIndex;
        String dbIdToDelete = Integer.toString(expDBID);
//        System.out.println("remove from expense array: "+arrayToDelete);
//        System.out.println("remove from database: "+dbIdToDelete);
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("delete from expenselog where " +
                    "expenseid='"+dbIdToDelete+"' and " +
                    "userid='"+Integer.toString(UserManager.getUser().getUserID())+"'");

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
        ReportWindowController.expenseList.remove(arrayToDelete);
        displayExpenseEntries();
    }

}
