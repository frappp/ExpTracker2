package expense;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by juddn on 2/8/2016.
 */
public class ExpenseEntry {
    private String expDate;
    private String itemName;
    private int itemCost;
    private String catName;
    private String catType;
    private int expID;
    private ArrayList<Label> expenseLabelArray = new ArrayList<>();
    private Button deleteMeButton = new Button("X");
    public ExpenseEntry(String date, String iname, int cost, String cname, String type, int eid){
        expDate = date;
        itemName = iname;
        itemCost = cost;
        catName = cname;
        catType = type;
        expID = eid;
//        expenseLabelArray.add(new Label(expDate));
        expenseLabelArray.add(new Label(itemName));
        expenseLabelArray.add(new Label(Integer.toString(itemCost)));
        expenseLabelArray.add(new Label(catName));
//        deleteMeButton.setOnAction(e -> deleteExpense(this.getDeleteMeButton(),this.getExpID()));
        deleteMeButton.getStyleClass().add("delButtons");
    }

    public Button getDeleteMeButton(){
        return deleteMeButton;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemCost() {
        return itemCost;
    }

    public String getCatName() {
        return catName;
    }

    public String getCatType() {
        return catType;
    }

    public int getExpID() {
        return expID;
    }

    public ArrayList<Label> getExpenseLabelArray(){
        return expenseLabelArray;
    }



}
