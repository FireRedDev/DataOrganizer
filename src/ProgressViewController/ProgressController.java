package ProgressViewController;

import GeneralViewController.GeneralController;
import java.io.*;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;
import javafx.concurrent.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.*;

/**
 * ProgressController
 * <br>
 * Objekte dieser Klasse werden vom FXML-Loader erstellt. Dabei füllt dieser
 * auch die mit @FXML markiertenen Referenzen auf die Controls der View und
 * verknüpft die mit @FXML gekennzeichneten Methoden als Actions, etc.
 *
 * @author Isabella
 */
public class ProgressController {

    private static Stage stage;
    private final static String VIEWNAME = "progressV.fxml";

    private GeneralController Gcontroller;
    private ResourceBundle bundle;

    @FXML
    private ProgressBar progressbar;

    /**
     * Anzeige der View.
     * <br>
     * Diese Methode erstellt eine Instanz der View und dieses Controllers
     * (FXML-Loader) und richtet alles (also vor allem den Controller) so weit
     * ein, dass es angezeigt werden kann.
     *
     * @param parentStage Stage des Aufrufenden Controllers
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @param Gcontroller GeneralController
     * @param bundle ResourceBundle, wird benötigt für die Internationalisierung
     */
    public static void show(Stage parentStage, Stage stage, GeneralController Gcontroller, ResourceBundle bundle) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(ProgressController.class.getResource(VIEWNAME), bundle);
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.initStyle(StageStyle.UNIFIED);
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.getIcons().add(new Image(ProgressController.class.getResourceAsStream("../icon.png")));

            // Controller ermitteln
            ProgressController controller = (ProgressController) loader.getController();
            controller.bundle = bundle;

            // View initialisieren
            controller.init(stage, Gcontroller);

            // Anzeigen
            stage.show();
        } catch (IOException | SQLException ex) {
            System.err.println(bundle.getString("Fensterladefehler"));
            ex.printStackTrace(System.out);
            System.exit(1);
        }
    }

    /**
     * Initialisieren.
     * <br>
     * Diese Methode wird nur von show() verwendet, um den Controller zu
     * initialisieren. Das umfasst u.a. die Konfiguration der Controls, die
     * Verbindung der Controls mit den Model-Feldern, etc.
     *
     * Hier wird auch die Sortierfunktion aufgerufen.
     *
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @param Gcontroller GeneralController
     * @throws SQLException Exception
     */
    private void init(Stage stage, GeneralController Gcontroller) throws SQLException {
        this.Gcontroller = Gcontroller;
        this.stage = stage;

        progressbar.progressProperty().bind(Gcontroller.progress);

        Task<String> tkSort = new Task<String>() {
            @Override
            protected String call() throws Exception {
                // Beginn merken
                Instant beginn = Instant.now();

                Gcontroller.getMover().sort(new File(Gcontroller.getAusProp()).listFiles());
                // Laufzeit berechnen
                return "Calculation Time: " + Duration.between(beginn, Instant.now()).toMillis() + " ms";
            }
        };
        tkSort.setOnSucceeded((WorkerStateEvent t) -> {
            hide();
        });
        Thread thread = new Thread(tkSort);
        thread.start();
    }

    /**
     * abbrechen
     * <br>
     * Diese Methode wird aufgerufen, wenn der Abbrechen-Button gedrückt wurde.
     *
     * @param event
     */
    @FXML
    private void abbrechen(ActionEvent event) {
        Gcontroller.setAbbrechenProp(true);
        stage.hide();
    }

    /**
     * hide
     * <br>
     * Diese Methode wird aufgerufen um die Stage zu hidden.
     */
    public static void hide() {
        stage.hide();
    }
}
