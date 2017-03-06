/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package einstellungViewC;

import data.DataType;
import data.Extension;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mover.DataMover1;
import viewController.GeneralController;

/**
 *
 * @author sarah
 */
public class ErweitertC {

    private DataMover1 mover;
    private Label label;
    @FXML
    private Label lbBilder;
    @FXML
    private TextField tfBilder;
    @FXML
    private ChoiceBox<?> cbBilder;
    @FXML
    private Label lbVideos;
    @FXML
    private ChoiceBox<?> cbVideos;
    @FXML
    private ChoiceBox<?> cbAudio;
    @FXML
    private ChoiceBox<?> cbDokumente;
    @FXML
    private Label lbAudio;
    @FXML
    private Label lbDokumente;
    @FXML
    private TextField tfVideos;
    @FXML
    private TextField tfAudio;
    @FXML
    private TextField tfDokumente;
    @FXML
    private CheckBox checkbDays;
    @FXML
    private ChoiceBox<?> cbDays;
    @FXML
    private CheckBox checkbLetter;
    @FXML
    private Button btCancel;
    @FXML
    private Button btSave;
    @FXML
    private TextField tfMsg;

    private final static String VIEWNAME = "ErweitertV.fxml";

    public static void show(Stage stage, DataMover1 mover) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(ErweitertC.class.getResource(VIEWNAME));
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
            ErweitertC controller = (ErweitertC) loader.getController();
            controller.init(mover);

            // View initialisieren
            //controller.init();
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

    private void handleButtonAction(ActionEvent event) {

    }

    private void init(DataMover1 mover) {
        this.mover = mover;
    }

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    private void sortieren() {
        try {
            // System.out.println("Log:");
            mover.sort();
            mover.order();

            //System.out.println("Files Sorted");
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
