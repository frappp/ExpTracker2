package expense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import sun.java2d.pipe.SpanShapeRenderer;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ExpenseInputController {
    @FXML
    GridPane expenseInputPaneMain;
    @FXML
    GridPane expCategoryList;
    @FXML
    GridPane incCategoryList;
    @FXML
    GridPane newCategoryPrompt;
    @FXML
    Label displayUser;
    @FXML
    ToggleButton expenseCategoriesToggleButton;
    @FXML
    ToggleButton incomeCategoriesToggleButton;
    @FXML
    ToggleGroup expenseTypeGroup;
    @FXML
    ToggleGroup expenseCategoryGroup;
    @FXML
    ToggleGroup incomeCategoryGroup;
    @FXML
    Button addCategoryButton;
    @FXML
    TextField newCategoryField;
    @FXML
    TextField itemName;
    @FXML
    TextField itemCost;
    @FXML
    TextArea itemDesc;
    @FXML
    DatePicker entryCalendar;

    private ArrayList<Category> catContainer;
    public void initialize(){
//        WindowController.getPrimaryStage().hide();
//        expenseInputPaneMain.setGridLinesVisible(true);
//        expCategoryList.setGridLinesVisible(true);
        displayUser.setText("Welcome "+UserManager.getUser().getFirstName()+"!");
        loadExpenseCategories();
        entryCalendar.setValue(LocalDate.now());
    }

    //  toggles between list of expense and income categories
    public void toggleIncomeExpense(ActionEvent ae) throws Exception{
        if (expenseCategoriesToggleButton.isSelected()){
            incCategoryList.setVisible(false);
            expCategoryList.setVisible(true);
        } else if (incomeCategoriesToggleButton.isSelected()){
            expCategoryList.setVisible(false);
            incCategoryList.setVisible(true);
        }
    }


//    public ExpenseInputController(){
//
//
//    }

    private void loadExpenseCategories() {
        catContainer = new ArrayList<>();
        Connection conn = null;
//        ||Pull income/expense categories from database, creates Category object for each one and adds to arraylist container
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select catname, cattype, catid, categories.userid, hidden from categories join users on categories.userid = users.userid where categories.userid='"+UserManager.getUser().getUserID()+"' or categories.userid=0");
            while (results.next()){
                String categoryName = results.getString("catname");
                String categoryType = results.getString("cattype");
                int categoryID = results.getInt("catid");
                int userID = results.getInt("userid");
                String hiddenProperty = results.getString("hidden");
                Category tempCategory = new Category(categoryName, categoryType, categoryID, userID, hiddenProperty);
                catContainer.add(tempCategory);
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
//        || populates expense categories in to array of buttons
        populateCategoryLists();

    }

    private void populateCategoryLists() {
        expCategoryList.getChildren().clear();
        incCategoryList.getChildren().clear();
        expenseCategoryGroup.getToggles().clear();
        incomeCategoryGroup.getToggles().clear();
        ArrayList<Category> hiddenCats = new ArrayList<>();
        for (Category cat: catContainer){
            if (cat.getHiddenProp().equals("yes")){
                hiddenCats.add(cat);
            }
        }
        for (Category cat: hiddenCats){
//            System.out.println(cat.getCategoryName());
            for (int i = 0; i < catContainer.size(); i++){
                if (cat.getCategoryName().equals(catContainer.get(i).getCategoryName())){
                    catContainer.remove(i);
                }
            }
        }

        int i = 0;
        for (Category cat: catContainer) {
            if (cat.getCategoryType().equals("Expense")) {
                expCategoryList.add(cat.getCategoryRadioButton(), 0, i);
                cat.getCategoryRadioButton().setToggleGroup(expenseCategoryGroup);
            }
            i++;
        }

        i = 0;
        for (Category cat: catContainer){
            if (cat.getCategoryType().equals("Income")){
                incCategoryList.add(cat.getCategoryRadioButton(), 0, i);
                cat.getCategoryRadioButton().setToggleGroup(incomeCategoryGroup);
            }
            i++;
        }
//        System.out.println("Total categories loaded: " + catContainer.size());
    }

    //  shows the gridpane to enter a new category
    public void showAddCategory(ActionEvent ae) throws Exception{
        addCategoryToggle();


    }

    private void addCategoryToggle() {
        newCategoryField.setText("");
        if(!newCategoryPrompt.isVisible())
        {
            newCategoryPrompt.setVisible(true);
            addCategoryButton.setText("-");
        }
        else if(newCategoryPrompt.isVisible()){
            newCategoryPrompt.setVisible(false);
            addCategoryButton.setText("+");
        }
    }

    //    handles submit button press for new category
    public void newCategoryClick(ActionEvent ae) throws Exception{
        if (!newCategoryField.getText().equals("")){
            submitNewCategory(newCategoryField.getText());
        }
    }

//    submits new category to database when new-category submit button is pressed
    private void submitNewCategory(String text) {
        String newCategory = text;
        Connection conn = null;
        String existingCatCheck = null;
        String defineCategoryType="unspecified";
//        check if category is expense or income
        if (expCategoryList.isVisible()){
            defineCategoryType = "Expense";
        } else if (incCategoryList.isVisible()){
            defineCategoryType = "Income";
        }

        int userID = UserManager.getUser().getUserID();
        try {
            boolean genericOwned=false;
            boolean userOwned=false;
            boolean hidden=false;
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select catname, UserID, hidden from categories where catname='"+newCategory+"' and (userid = '"+userID+"' or userid=0)");
            while (results.next()){
                existingCatCheck = results.getString("catname");
                int tempUserIDCheck = -1;
                tempUserIDCheck=results.getInt("userid");
                String hide=results.getString("hidden");
                if (hide != null){
                    if(hide.equals("yes")){
                        hidden=true;
                    }
                }
                if (tempUserIDCheck == 0){
                    genericOwned = true;
                } else if(tempUserIDCheck > 0){
                    userOwned = true;
                }
            }

//            System.out.println("generic: "+genericOwned+" -userowned: "+userOwned+" -hidden: "+hidden);
//            inserts new category to DB if the same named category does not exist
            if (existingCatCheck == null){
                stmt.executeUpdate("insert into categories (CatName, CatType, UserID) Values ('"+newCategory+"', '"+defineCategoryType+"','"+userID+"')");

            } else if (((!genericOwned) && (userOwned) && (!hidden)) || ((genericOwned) && (!userOwned) && (!hidden))) {
//                prevents duplicate category from being entered -
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Duplicate Category");
                warning.setContentText("That Category Already Exists.  Please Enter A Different Category Name");
                warning.showAndWait();
                newCategoryField.setText("");
            } else if ((!genericOwned) && (userOwned) && (hidden)){
//                checks if there is a hidden category that is a user-created category and un-hides it instead of creating a new one
                stmt.executeUpdate("update categories set hidden=\"\" where catname='"+newCategory+"' and userid='"+userID+"'");
            } else if((genericOwned) && (userOwned) && (hidden)){
//                checks if there is a generic owned category and also a hidden user-owned one of the same name, and if so deletes the duplicate user one to unhide the generic one
                stmt.executeUpdate("delete from categories where catname='"+newCategory+"' and userid='"+userID+"' and hidden=\"yes\"");
            }

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(conn!=null) {
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        loadExpenseCategories();
        addCategoryToggle();

    }

//    pulls the selected toggle (radio button) from the category list and prompts the user if they want to remove it
    public void promptDeleteCategory(ActionEvent ae) throws Exception{
        Toggle selectedCategory=null;
        if (expCategoryList.isVisible()) {
            selectedCategory = expenseCategoryGroup.getSelectedToggle();
        } else if (incCategoryList.isVisible()){
            selectedCategory = incomeCategoryGroup.getSelectedToggle();
        }

        if(selectedCategory!=null){
            Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
            warning.setTitle("Confirm Category Deletion");
            warning.setContentText("Are you sure you want to delete the category: "+((RadioButton)selectedCategory).getText());
            Optional<ButtonType> confirmationCheck = warning.showAndWait();
            if(confirmationCheck.get() == ButtonType.OK){
//                System.out.println("DELETE");
                deleteCategory(selectedCategory);
            } else {
//                System.out.println("NOT DELETE");
            }
        }
    }

    private void deleteCategory(Toggle selectedCategory) {
        RadioButton toBeDeleted = (RadioButton)selectedCategory;
        Category tempcat=null;
//        matches selected radio button with category object
        for (Category cat: catContainer){
            if(toBeDeleted == cat.getCategoryRadioButton()){
//                System.out.println("DELETE "+cat.getCategoryName());
                tempcat=cat;
            }
        }
//        for starters, the category is removed from the category container array and toggle groups are emptied
        if (tempcat!=null){
            catContainer.remove(tempcat);
        }

//          DB removal starts here
        Connection conn = null;
        int catID = tempcat.getCategoryID();
        int ownerid = tempcat.getUserID();
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();

//            checks if ownerid of the category is 0 (default user / shared categories) and if so enteres duplicate category for that user with 'hidden' flag
//            also checks if there are expenses logged against the category being deleted, and if so adds 'hidden' flag to that category
//            otherwise deletes the category if no expenses are logged against it
            int existingCategoryEntries = 0;
            if (ownerid!=0){
                ResultSet checkCategoryEntries = stmt.executeQuery("select expenseid from expenselog where catid='"+catID+"'");
                while (checkCategoryEntries.next()){
                    existingCategoryEntries = checkCategoryEntries.getInt("expenseid");
//                    System.out.println("entries: "+ existingCategoryEntries);
                }
                if (existingCategoryEntries!=0){
                    stmt.executeUpdate("update categories set hidden=\"yes\" where catid='"+catID+"'");
                } else{
                    stmt.executeUpdate("delete from categories where catid = '"+catID+"'");
                }
            } else if (ownerid == 0){
                String expName = tempcat.getCategoryName();
                String expType = tempcat.getCategoryType();
                int userID = UserManager.getUser().getUserID();
                stmt.executeUpdate("insert into categories (catname, cattype, userid, hidden) values('"+expName+"', '"+expType+"', '"+userID+"', \"yes\")");
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
        populateCategoryLists();
    }

    public void submitExpense(ActionEvent ae) throws Exception {
        int itemExpenseCatID=0;
        String itemEntryDescription;
        String itemEntryName;
        String itemEntryCost;
        Toggle tempCategoryToggle = null;
        String itemEntryDate;
        int itemEntryUserID;
        if (expCategoryList.isVisible()){
            tempCategoryToggle = expenseCategoryGroup.getSelectedToggle();
        } else if (incCategoryList.isVisible()){
            tempCategoryToggle = incomeCategoryGroup.getSelectedToggle();
        }
        for (Category cat: catContainer){
            if (cat.getCategoryRadioButton()==((RadioButton)tempCategoryToggle)){
                itemExpenseCatID=cat.getCategoryID();
            }
        }

        itemEntryCost = itemCost.getText();
        itemEntryName = itemName.getText();
        itemEntryDescription = itemDesc.getText();
        itemEntryUserID = UserManager.getUser().getUserID();
//        Pulls date value from date-picker and if no date is selected defaults to todays date.
        if (entryCalendar.getValue() == null){
            GregorianCalendar todaysCalendar = new GregorianCalendar();
            int todayYear = todaysCalendar.get(Calendar.YEAR);
            int tempMonth = todaysCalendar.get(Calendar.MONTH)+1;
            int todayDay = todaysCalendar.get(Calendar.DATE);
            String todayMonth = Integer.toString(tempMonth);
            if (tempMonth < 10){
                todayMonth = "0"+todayMonth;

            }
            String todaysDate = todayYear+"-"+todayMonth+"-"+todayDay;
//            System.out.println(todaysDate);
            itemEntryDate=todaysDate;
        } else {
            itemEntryDate = entryCalendar.getValue().toString();
        }

        submitExpenseToDB(itemEntryName, itemEntryCost, itemEntryDescription, itemEntryDate, itemExpenseCatID, itemEntryUserID);

        itemName.setText("");
        itemCost.setText("");
        itemDesc.setText("");
        entryCalendar.setValue(LocalDate.now());
        expenseCategoryGroup.getToggles().clear();
        incomeCategoryGroup.getToggles().clear();

    }

    private void submitExpenseToDB(String itemEntryName, String itemEntryCost, String itemEntryDescription, String itemEntryDate, int itemExpenseCatID, int itemEntryUserID) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            Statement stmt = conn.createStatement();

                stmt.executeUpdate("insert into expenselog (ItemName, ItemCost, ItemDescription, EntryDate, CatID, UserID) " +
                                    "values('"+itemEntryName+"', '"+itemEntryCost+"', '"+itemEntryDescription+"',  '"+itemEntryDate+"', '"+itemExpenseCatID+"', '"+itemEntryUserID+"')");
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

    public void exit(ActionEvent ae) throws Exception{
        WindowController.getExpenseStage().hide();
        WindowController.getPrimaryStage().show();
    }

}
