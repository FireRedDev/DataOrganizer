package main;

import java.io.File;
import java.sql.*;
import java.util.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import viewController.GeneralController;

/**
 * TheMain
 */
public class TheMain extends Application {

    String baseName;
    private static final String dbURL = "jdbc:derby:Dataorganizer;create=true;user=Dataorganizer;password=passme";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            baseName = "resources.dataorganizer";
//            Locale locale = new Locale("en", "UK");
//            Locale locale = new Locale("ge", "GE");
//            ResourceBundle bundle = ResourceBundle.getBundle(baseName,locale);
            ResourceBundle bundle = ResourceBundle.getBundle(baseName);
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());

            new File("gui").mkdir();
            new File("imag").mkdir();
            new File("Bilder").mkdir();
            new File("Dokumente").mkdir();
            new File("Video").mkdir();
            new File("Audio").mkdir();

            Connection connection = DriverManager.getConnection(dbURL);
            Statement statement = connection.createStatement();
            if (tableExist(connection, "regexrules")) {
                String sqlQuery2 = "create table regexrules ("
                        + "    ordner varchar(100) not null,"
                        + "    regex varchar(132) not null"
                        + ")";
                statement.executeUpdate(sqlQuery2);
                statement.executeUpdate("insert into regexrules(ordner,regex) values ('gui','gui')");
                statement.executeUpdate("insert into regexrules(ordner,regex) values ('imag','imag')");
            }
            if (tableExist(connection, "dateiendung")) {
                String sqlQuery = "create table dateiendung ("
                        + "    datatype varchar(100) not null,"
                        + "    extension varchar(132) not null"
                        + ")";
                statement.executeUpdate(sqlQuery);
                statement.executeUpdate("insert into dateiendung (datatype,extension) values ('Bilder','jpg,jpeg,gif,png')");
                statement.executeUpdate("insert into dateiendung (datatype,extension) values ('Dokumente','pdf,docx,xlxs,zip')");
                statement.executeUpdate("insert into dateiendung (datatype,extension) values ('Video','mp4,avi,wmv')");
                statement.executeUpdate("insert into dateiendung (datatype,extension) values ('Audio','mp3,wma,ogg,flac')");
            }
            GeneralController.show(stage, statement, bundle);

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static boolean tableExist(Connection conn, String tableName) throws SQLException {
        boolean tExists = false;
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    break;
                }
            }
        }
        return tExists;
    }
}
