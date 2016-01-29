package expense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));
//        primaryStage.setTitle("ExpenseTracker");
//        primaryStage.setScene(new Scene(root, 500, 250));
//        primaryStage.show();
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowController.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
