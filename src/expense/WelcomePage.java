package expense;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class WelcomePage {

    @FXML
    GridPane mainPane;
    @FXML
    GridPane secondPane;
    @FXML
    GridPane existingUserPane;
    @FXML
    GridPane newUserPane;
    @FXML
    TextField userName;
    @FXML
    TextField newFirstName;
    @FXML
    TextField newLastName;
    @FXML
    TextField newUserName;
    @FXML
    ToggleGroup newOrExistUser;
    @FXML
    ToggleButton existUserTog;
    @FXML
    ToggleButton newUserTog;
    @FXML
    Button submitUser;

//    public WelcomePage(){
//    }

    public void initialize(){
        mainPane.setGridLinesVisible(true);
    }

    //        Attempts to log in with entered user name
    public void userLoginClick(ActionEvent ae) throws Exception {
//        System.out.println(userName.getText());
        if (!userName.getText().equals("")&&(!userName.getText().equals("DEFAULTUSER"))){
            UserManager.userLogin(userName.getText());
            if (UserManager.getUser() != null) {
//                System.out.println("User name " + UserManager.getUser().getUserName());
//                System.out.println("first name " + UserManager.getUser().getFirstName());
//                System.out.println("last name "+ UserManager.getUser().getLastName());
                WindowController.loadExpenseInputWindow();
                WindowController.getPrimaryStage().hide();
            }
        }

    }

//    Switches between user-login and user-creation pane
    public void toggleNewUser(ActionEvent ae){
        if (newUserTog.isSelected()){
            newUserPane.setVisible(true);
            existingUserPane.setVisible(false);
        } else if (existUserTog.isSelected()){
            newUserPane.setVisible(false);
            existingUserPane.setVisible(true);
        }
    }

//  Attempts to create new user
    public void submitNewUser(ActionEvent ae){
        String newLast = newLastName.getText();
        String newFirst = newFirstName.getText();
        String newUser = newUserName.getText();
        if (newFirst.equals("") || newLast.equals("") || newUser.equals("")) {
            Alert warning = new Alert(Alert.AlertType.INFORMATION);
            warning.setTitle("Blank Entries");
            warning.setContentText("Please enter all fields");
            warning.showAndWait();
        } else {
            UserManager.createNewUser(newLastName.getText(), newFirstName.getText(), newUserName.getText());
            userName.setText(newUser);
        }
    }


//    public void testQuery(ActionEvent ae)throws Exception{
//        ResultSet testResult = DBManager.submitSQLQuery("select firstname from users");
//
////        try {
//        while (testResult.next()){
//            System.out.println(testResult.getString("firstname"));
//        }
////
////        } catch (SQLException se) {
////            se.printStackTrace();
////        }
//
//    }
//
//    public void testUpdate(ActionEvent ae)throws Exception{
//
//
//    }


}
