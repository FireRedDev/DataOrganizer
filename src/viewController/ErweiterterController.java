/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Isabella
 */
public class ErweiterterController {
    
    private Stage stage;
    private final static String VIEWNAME = "Dateitypenwarten.fxml";
    
    public static void show(Stage parentStage, Stage stage) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(ErweiterterController.class.getResource(VIEWNAME));
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.setScene(scene);
            stage.setTitle("DataOrganizer");
            
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);

            // Controller ermitteln
            ErweiterterController controller = (ErweiterterController) loader.getController();

            // View initialisieren
            controller.init(stage);

            // Anzeigen
            stage.show();

            // Anzeigen
//           Platform.runLater(() -> {
//                Alert alConfirm = new Alert(Alert.AlertType.INFORMATION);
//                alConfirm.setHeaderText("Programm läuft im Hintergrund!\nÖffnen über SystemTray!");
//                alConfirm.show();
//            });
        } catch (IOException ex) {
            System.err.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }
    @FXML
    private TextField tfMsg;
    
    private void init(Stage stage) {
        this.stage = stage;
    }
}
