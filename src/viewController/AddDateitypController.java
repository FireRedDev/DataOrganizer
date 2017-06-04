package viewController;

import data.*;
import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Dateityp hinzufügen
 * <p>
 * Fenster zum hinzufügen von Datentypen
 * </p>
 * @author Isabella
 */
public class AddDateitypController {

    private Stage stage;
    private DataMover mover;
    private final static String VIEWNAME = "AddDateityp.fxml";
    private ErweiterterController ec;
    private Statement statement;

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

    public static void show(Stage parentStage, Stage stage, DataMover mover, ErweiterterController ec, Statement statement) {
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
            controller.statement = statement;

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
    private void speichernD(ActionEvent event) throws SQLException {
        speichernD();
    }

    /**
     * Speichern neuer Dateiendung
     * <p>
     * Einfügen neuer Dateiendungen.
     * </p>
     * @throws SQLException 
     */
    private void speichernD() throws SQLException {
        if (!"".equals(this.getTypProp()) && !"".equals(this.getAusOrdnerTyp()) && this.getTypProp().length() > 1 && this.getAusOrdnerTyp().length() > 1) {
            boolean eingabefehler = false;
            DataType vorhanden = null;
            String[] array = this.getTypProp().split(",");
            LinkedList<Extension> extensionlistget = new LinkedList<>();
            for (String ex : array) {
                if (ex != null && ex != "") {
                    extensionlistget.add(new Extension(ex));
                }
            }

            List<DataType> datatype = mover.getDatatype();
            for (DataType type : datatype) {
                LinkedList<Extension> extensionlist = type.getExtensionlist();
                for (Extension e : extensionlist) {
                    for (Extension eget : extensionlistget) {
                        if (eget.getExtension().equals(e.getExtension())) {
                            eingabefehler = true;
                            showErrorMessage("Extension wird bereits anders sortiert!");
                        }
                    }
                }
                if (type.getOrdner().equals(new File(this.getAusOrdnerTyp()))) {
                    vorhanden = type;
                }

            }
            if (!eingabefehler) {
                DataType typ;
                if (vorhanden != null) {
                    typ = vorhanden;
                } else {
                    typ = new DataType(new File(this.getAusOrdnerTyp()));
                }
                if (!eingabefehler) {
                    String[] arrayE = this.getTypProp().split(",");

                    for (String ex : arrayE) {
                        typ.addExtension(new Extension(ex));
                    }

                    Dateiendung end = new Dateiendung(typ);
                    end.setOrdner(this.getAusOrdnerTyp());
                    end.setExtension(typ.Extensions());
                    if (vorhanden != null) {
                        String sql = "update Dateiendung set datatype = '" + typ.toString() + "', extension = '" + typ.Extensions() + "' where datatype = '" + typ.toString() + "' ";

                        // Datenbankzugriff
                        statement.executeUpdate(sql);
                        ec.updateList();
                    } else {
                        mover.addDataType(typ);
                        ec.addList(end);

                        String sql = "insert into Dateiendung (datatype,extension) values ( '" + this.getAusOrdnerTyp() + "', '" + this.getTypProp() + "')";

                        // Datenbankzugriff
                        statement.executeUpdate(sql);
                    }
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
