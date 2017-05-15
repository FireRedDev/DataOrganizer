package einstellungViewC;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mover.DataMover;
import utility.PrintToTextField;
import viewController.GeneralController;

/**
 * ErweitertC
 * <p>
 * Controller für das Einstellungsfenster. Der Sortierbutton sortiert nach
 * Dateiendungen und danach nach Monat.
 * </p>
 */
public class ErweitertC {

    private DataMover mover;
    @FXML
    private Button sortBt;
    @FXML
    private Button erweiternBt;
    @FXML
    private TextField tfMsg;

    private final static String VIEWNAME = "ErweitertV.fxml";

    public static void show(Stage stage, DataMover mover) {
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

    private void init(DataMover mover) {
        this.mover = mover;
        PrintToTextField.create(tfMsg);
    }

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    /**
     * sortieren
     * <p>
     * Sortieren und in Datumsordner verschieben.
     * </p>
     */
    private void sortieren() {
        try {
            // System.out.println("Log:");
            mover.sort();
            mover.order();
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
