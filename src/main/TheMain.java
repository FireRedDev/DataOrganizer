package main;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import viewController.GeneralController;

/**
 * TheMain
 */
public class TheMain extends Application {

    String baseName;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            baseName = "resources.dataorganizer";
//            Locale locale = new Locale("en", "UK");
//            Locale locale = new Locale("ge", "GE");
//            ResourceBundle bundle = ResourceBundle.getBundle(baseName,locale);
            ResourceBundle bundle = ResourceBundle.getBundle(baseName);

            String url = "jdbc:derby://localhost:1527/Dataorganizer";
            String user = "Dataorganizer";
            String pwd = "passme";

            Connection connection = DriverManager.getConnection(url, user, pwd);
            Statement statement = connection.createStatement();

            GeneralController.show(stage, statement, bundle);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
