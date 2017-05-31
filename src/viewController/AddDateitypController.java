package viewController;

import data.*;
import java.io.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import mover.DataMover;

/**
 * FXML Controller class
 *
 * TODO * Wenn Datentyp vorhanden ist soll dieser in der Tabelle nicht neu
 * angezeigt werden.
 *
 * @author Isabella
 */
public class AddDateitypController {

    private Stage stage;
    private DataMover mover;
    private final static String VIEWNAME = "AddDateityp.fxml";
    private ErweiterterController ec;

    @FXML
    private TextField tfMsg;
    @FXML
    private AnchorPane apDateityp;
    @FXML
    private TextField ausOrdnerTyp;
    @FXML
    private TextField tfTyp;
    @FXML
    private Button speichernD;
    @FXML
    private Button abbrechenD;

    private File selectedDirectory;

    private final StringProperty ausOrdnerTypProp = new SimpleStringProperty();
    private final StringProperty typProp = new SimpleStringProperty();

    public static void show(Stage parentStage, Stage stage, DataMover mover, ErweiterterController ec) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(AddDateitypController.class.getResource(VIEWNAME));
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
            AddDateitypController controller = (AddDateitypController) loader.getController();

            // View initialisieren
            controller.init(stage, mover, ec);

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            System.err.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    private void init(Stage stage, DataMover mover, ErweiterterController ec) {
        this.mover = mover;
        this.stage = stage;
        this.ec = ec;

        ausOrdnerTyp.setEditable(false);
        ausOrdnerTyp.textProperty().bindBidirectional(this.ausOrdnerTypProp);
        tfTyp.textProperty().bindBidirectional(typProp);
        showSuccessMessage("Hier kannst du Datentypen hinzufügen!");
    }

    @FXML
    private void abbrechenD(ActionEvent event) {
    }

    @FXML
    private void ordnerAusTyp(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            this.setAusOrdnerTyp(selectedDirectory.toString());
        }
    }

    @FXML
    private void speichernD(ActionEvent event) {
        speichernD();
    }

    private void speichernD() {
        if (!"".equals(this.getTypProp()) && !"".equals(this.getAusOrdnerTyp()) && this.getTypProp().length() > 1 && this.getAusOrdnerTyp().length() > 1) {
            boolean eingabefehler = false;
            Extension extension = new Extension(this.getTypProp());

            List<DataType> datatype = mover.getDatatype();
            for (DataType type : datatype) {
                LinkedList<Extension> extensionlist = type.getExtensionlist();
                for (Extension e : extensionlist) {
                    if (extension.getExtension().equals(e.getExtension())) {
                        eingabefehler = true;
                        showErrorMessage("Extension wird bereits anders sortiert!");
                    }
                }
                if (type.getOrdner().equals(new File(this.getAusOrdnerTyp()))) {
                    type.addExtension(new Extension(this.getTypProp()));
                    mover.addDataType(type);

                    Dateiendung end = new Dateiendung(type);

                    end.setOrdner(this.getAusOrdnerTyp());
                    end.setExtension(type.Extensions());

                    ec.removeList(end);
                    ec.addList(end);

                    //Da Extension schon gespeichert, brauchen keine weiteren Überprüfungen mehr durchgeführt werden.
                    eingabefehler = true;
                    showSuccessMessage("Neue Extension wurde gespeichert!");
                }

            }
            if (!eingabefehler) {
                DataType typ = new DataType(new File(this.getAusOrdnerTyp()));

                if (typ.contains(extension)) {
                    eingabefehler = true;
                    showErrorMessage("Fehler! Extension konnte nicht eingefügt werden!");

                }
                if (datatype.contains(typ)) {
                    eingabefehler = true;
                    showErrorMessage("Fehler! Extension konnte nicht eingefügt werden!");
                }
                if (!eingabefehler) {
                    typ.addExtension(new Extension(this.getTypProp()));
                    mover.addDataType(typ);
                    Dateiendung end = new Dateiendung(typ);
                    end.setOrdner(this.getAusOrdnerTyp());
                    end.setExtension(this.getTypProp());
                    ec.addList(end);

                    showSuccessMessage("Neue Extension wurde gespeichert!");
                }
            }
        } else {
            showErrorMessage("Du hast den Ordner oder/und die Extension nicht ausgewählt!");
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
