package main;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import viewController.GeneralController;

/**
 * TheMain
 */
public class TheMain extends Application {

    public static void main(String[] args) {
        final boolean test = true;
        try {
            if (test) {
                //Inhalt der Ordner wird gelöscht
                //Ordner bleiben jedoch bestehend
                test();
            }

            launch(args);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void start(Stage stage) {
        try {
            String url = "jdbc:derby://localhost:1527/Dataorganizer";
            String user = "Dataorganizer";
            String pwd = "passme";
            Connection connection = DriverManager.getConnection(url, user, pwd);
            Statement statement = connection.createStatement();
            GeneralController.show(stage, statement);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * deleteDir
     * <p>
     * Löscht Inhalt eines Ordners samt Unterordner.
     * </p>
     *
     * @param path Ordnerpfad
     */
    private static void deleteDir(File path) {
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            }
            file.delete();
        }
        path.delete();
    }

    /**
     * Klasse für Testzwecke, Löscht die Daten aus den Directorys und kopiert
     * sie zurück in Zusortierend
     *
     * @throws IOException
     */
    private static void test() throws IOException {
        File audio = new File("Audio");
        if (audio.exists()) {
            deleteDir(audio);
        }
        File bilder = new File("Bilder");
        if (bilder.exists()) {
            deleteDir(bilder);
        }
        File dokumente = new File("Dokumente");
        if (dokumente.exists()) {
            deleteDir(dokumente);
        }
        File video = new File("Video");
        if (video.exists()) {
            deleteDir(video);
        }
    }
}
