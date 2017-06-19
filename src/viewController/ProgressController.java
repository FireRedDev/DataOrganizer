package viewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.*;

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
            stage.initStyle(StageStyle.UNIFIED);
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.getIcons().add(new Image(ProgressController.class.getResourceAsStream("icon.png")));

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

    @FXML
    private void abbrechen(ActionEvent event) {
        Gcontroller.setAbbrechenProp(true);
        stage.hide();
    }

    public static void hide() {
        stage.hide();
    }
}
