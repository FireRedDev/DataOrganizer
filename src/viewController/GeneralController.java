/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController;

import data.DataType;
import data.Extension;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.TheMain;
import mover.DataMover;

/**
 * FXML Controller class
 *
 * @author Isabella
 */
public class GeneralController {

    private final static String VIEWNAME = "GeneralV.fxml";
    private DataMover mover;

    public static void show(Stage stage) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(GeneralController.class.getResource(VIEWNAME));
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.setScene(scene);
            stage.setTitle("DataOrganizer");

            // Controller ermitteln
            GeneralController controller = (GeneralController) loader.getController();

            // View initialisieren
            controller.init();

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.err);
            System.exit(1);
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(System.err);
            System.exit(2);
        }
    }
    @FXML
    private Button sortBt;

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    private void init() {
        Scanner sc = new Scanner(System.in);
        System.out.println("DataOrganizer - Sortiert ihre Dateien via Dateitypen(Diese haben eine oder Mehrere Dateiendungen abgespeichert)");

        DataType bilder = new DataType("Bilder");
        //könnte sein das der punkt weggehört
        bilder.addExtension(new Extension("jpg"));
        bilder.addExtension(new Extension("png"));
        bilder.addExtension(new Extension("gif"));
        bilder.addExtension(new Extension("jpeg"));
        DataType documente = new DataType("Dokumente");
        documente.addExtension(new Extension("pdf"));
        documente.addExtension(new Extension("docx"));
        documente.addExtension(new Extension("vsd"));
        documente.addExtension(new Extension("xlsx"));
        documente.addExtension(new Extension("zip"));
        DataType video = new DataType("Video");
        video.addExtension(new Extension("mp4"));
        video.addExtension(new Extension("mpeg"));
        video.addExtension(new Extension("avi"));
        video.addExtension(new Extension("wmv"));
        DataType audio = new DataType("Audio");
        audio.addExtension(new Extension("mp3"));
        audio.addExtension(new Extension("wma"));
        audio.addExtension(new Extension("ogg"));
        audio.addExtension(new Extension("flac"));
        mover = new DataMover(bilder);
        mover.addDataType(documente);
        mover.addDataType(video);
        mover.addDataType(audio);
        System.out.println("Ordner Erstellt, Bitte gebe jetzt deine Dateien in den Ordner mit dem Namen 'zusortierend'.");
        System.out.println("Hast du das getan, drücke Enter:");
        sc.nextLine();
    }

    private void sortieren() {
        try {
           // System.out.println("Log:");
            mover.sort();
            //System.out.println("Files Sorted");
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
