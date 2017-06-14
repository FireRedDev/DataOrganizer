package viewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgressController {

    private static Stage stage;
    private final static String VIEWNAME = "progressV.fxml";
    private GeneralController Gcontroller;
    private ResourceBundle bundle;

    @FXML
    private ProgressBar progressbar;

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
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.getIcons().add(new Image(ProgressController.class.getResourceAsStream("icon.png")));
            stage.setResizable(false);

            // Controller ermitteln
            ProgressController controller = (ProgressController) loader.getController();
            controller.bundle = bundle;

            // View initialisieren
            controller.init(stage, Gcontroller);

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            System.err.println(bundle.getString("Fensterladefehler"));
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (SQLException ex) {
            System.err.println(bundle.getString("Fensterladefehler"));
            ex.printStackTrace(System.out);
            System.exit(1);
        }
    }

    /**
     * Init
     *
     * @param stage
     * @param mover
     * @throws SQLException
     */
    private void init(Stage stage, GeneralController Gcontroller) throws SQLException {
        this.Gcontroller = Gcontroller;
        this.stage = stage;

        progressbar.progressProperty().bind(Gcontroller.progress);
    }

    public static void hide() {
        stage.close();
    }

    @FXML
    private void abbrechen(ActionEvent event) {
        abbrechen();
    }

    private void abbrechen() {
        Gcontroller.setAbbrechenProp(true);
    }
}
