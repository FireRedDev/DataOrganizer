package viewController;

import data.RegexRule;
import java.io.*;
import java.sql.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.*;
import mover.DataMover;

/**
 * Regex-Filter hinzufügen
 * <br>
 * Fenster zum hinzufügen von Regex-Filtern
 *
 * @author Isabella
 */
public class AddRegexController {

    private Stage stage;
    private DataMover mover;
    private final static String VIEWNAME = "AddRegex.fxml";
    private ErweiterterControllerRegex ec;
    private Statement statement;
    private ResourceBundle bundle;

    @FXML
    private TextField tfMsg;
    @FXML
    private TextField ausOrdnerTyp;
    @FXML
    private TextField tfTyp;

    private File selectedDirectory;

    private final StringProperty ausOrdnerTypProp = new SimpleStringProperty();
    private final StringProperty typProp = new SimpleStringProperty();

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
     * @param mover Mover
     * @param statement Datenbankverbindung
     * @param bundle ResourceBundle, wird benötigt für die Internationalisierung
     * @param ec ErweiterterControllerRegegex
     */
    public static void show(Stage parentStage, Stage stage, DataMover mover, ErweiterterControllerRegex ec, Statement statement, ResourceBundle bundle) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(AddRegexController.class.getResource(VIEWNAME), bundle);
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
            stage.getIcons().add(new Image(AddRegexController.class.getResourceAsStream("icon.png")));
            stage.setResizable(false);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);

            // Controller ermitteln
            AddRegexController controller = (AddRegexController) loader.getController();

            controller.statement = statement;
            controller.bundle = bundle;

            // View initialisieren
            controller.init(stage, mover, ec);

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            System.err.println(bundle.getString("Fensterladefehler"));
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    /**
     * Initialisieren.
     * <br>
     * Diese Methode wird nur von show() verwendet, um den Controller zu
     * initialisieren. Das umfasst u.a. die Konfiguration der Controls, die
     * Verbindung der Controls mit den Model-Feldern, etc.
     *
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @param mover DataMover
     * @param ec ErweiterterController
     */
    private void init(Stage stage, DataMover mover, ErweiterterControllerRegex ec) {
        this.mover = mover;
        this.stage = stage;
        this.ec = ec;

        ausOrdnerTyp.setEditable(false);
        ausOrdnerTyp.textProperty().bindBidirectional(this.ausOrdnerTypProp);
        tfTyp.textProperty().bindBidirectional(typProp);
        showSuccessMessage(bundle.getString("RegexHinzufuegen"));
    }

    /**
     * Abbrechen
     * <br>
     * Fenster schließen.
     *
     * @param event
     */
    @FXML
    private void abbrechenD(ActionEvent event) {
        stage.close();
    }

    /**
     * Ordner auswählen
     * <br>
     * Ordner mithilfe des Directory-Chosser auswählen. Es wird der eingelesene
     * Pfad auf die Variable selectedDirectory gespeichert.
     *
     * @param event
     */
    @FXML
    private void ordnerAusTyp(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(bundle.getString("DataOrganizer"));
        selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            this.setAusOrdnerTyp(selectedDirectory.toString());
        }
    }

    /**
     * Speichern
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void speichernD(ActionEvent event) throws SQLException {
        speichernD();
    }

    /**
     * Speichern neuer RegexRule
     * <br>
     * Einfügen neuer RegexRule. Es wird die Regel auf Fehler überprüft.
     *
     * @throws SQLException
     */
    private void speichernD() throws SQLException {
        if (!"".equals(this.getTypProp()) && !"".equals(this.getAusOrdnerTyp()) && this.getTypProp().length() > 1 && this.getAusOrdnerTyp().length() > 1) {
            RegexRule vorhanden = null;
            List<RegexRule> regexrules = mover.getRegexrules();
            for (RegexRule type : regexrules) {

                if (type.getOrdner().equals(this.getAusOrdnerTyp())) {
                    vorhanden = type;
                }
            }
            RegexRule typ;
            if (vorhanden != null) {
                typ = vorhanden;
            } else {
                typ = new RegexRule(this.getAusOrdnerTyp(), this.getTypProp());
            }

            if (vorhanden != null) {
                typ.setRegex(typ.getRegex() + "," + this.getTypProp());
                String sql = "update regexrules set ordner = '" + typ.getOrdner() + "', regex = '" + typ.getRegex() + "' where ordner = '" + typ.getOrdner() + "' ";

                // Datenbankzugriff
                statement.executeUpdate(sql);
                ec.updateList();
            } else {
                mover.addRegexRule(typ);
                ec.addList(typ);

                String sql = "insert into regexrules (ordner,regex) values ( '" + this.getAusOrdnerTyp() + "', '" + this.getTypProp() + "')";

                // Datenbankzugriff
                statement.executeUpdate(sql);
            }
            showSuccessMessage(bundle.getString("neueRegexRule"));

        } else {
            showErrorMessage(bundle.getString("RegexAusgewaehlt"));
        }
    }

    public final void setAusOrdnerTyp(String value) {
        ausOrdnerTypProp.set(value);
    }

    private String getAusOrdnerTyp() {
        return ausOrdnerTypProp.getValueSafe();
    }

    private void setTypProp(String value) {
        typProp.set(value);
    }

    private String getTypProp() {
        return typProp.getValueSafe();
    }

    /**
     * Fehlermeldung anzeigen.
     *
     * @param message Nachrichtentext
     */
    public void showErrorMessage(String message) {
        tfMsg.setText(message);
        tfMsg.setStyle("-fx-text-inner-color: red;");
    }

    /**
     * Erfolgsmeldung anzeigen.
     *
     * @param message Nachrichtentext
     */
    public void showSuccessMessage(String message) {
        tfMsg.setText(message);
        tfMsg.setStyle("-fx-text-inner-color: green;");
    }

}
