package expense;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowController {
    private static Parent root;
    private static Stage primaryStage;

    //    loads initial window - the screen to enter /create username  - marked as static to be accessable from other classes
//    this window is hidden when logged in
    public static void start(Stage primaryStageTemp) throws Exception {
        root = FXMLLoader.load(WindowController.class.getResource("/fxml/WelcomePage.fxml"));
        primaryStage = primaryStageTemp;
        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(new Scene(root, 500, 250));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    //shows expense input window - displays upon logging in
    private static Stage expenseStage = new Stage();

    public static void loadExpenseInputWindow() throws Exception {
        Parent expenseInputWindow;
        expenseInputWindow = FXMLLoader.load(WindowController.class.getResource("/fxml/ExpenseInputWindow.fxml"));
        expenseStage.setTitle("Expense Tracker");
        expenseStage.setScene(new Scene(expenseInputWindow, 420, 400));
        expenseStage.show();

    }

    public static Stage getExpenseStage() {
        return expenseStage;
    }


    private static Stage reportStage = new Stage();

    public static void loadReportWindow() throws Exception {
        Parent reportViewerWindow;
        reportViewerWindow = FXMLLoader.load(WindowController.class.getResource("/fxml/ReportWindow.fxml"));
        reportStage.setTitle("Expense Tracker - Report");
        reportStage.setScene(new Scene(reportViewerWindow, 400, 500));
        reportStage.show();
    }

    public static Stage getReportStage(){
        return reportStage;
    }
}