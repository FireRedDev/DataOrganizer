package viewController;

import data.*;
import java.io.IOException;
import javafx.fxml.*;
import javafx.scene.Parent;
import mover.DataMover;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

/**
 * GeneralController
 * <p>
 * Controller zur ersten einfachen View.
 * </p>
 */
public class GeneralController {

    private final static String VIEWNAME = "GeneralV.fxml";
    private Stage stage;
    private DataMover mover;
    private File selectedDirectory;
    // Verbindung zur Datenbank
    private Statement statement;
    private ResourceBundle bundle;

    Properties props = new Properties();

    private final BooleanProperty generalDisplay = new SimpleBooleanProperty();

    String propertyFile = "DOproperties.properties";

    private final StringProperty ausProp = new SimpleStringProperty();
    private final BooleanProperty dateNamingProp = new SimpleBooleanProperty();
    private final BooleanProperty orderByDateProp = new SimpleBooleanProperty();
    private final BooleanProperty verschiebenProp = new SimpleBooleanProperty();
    private final BooleanProperty sortSubFolderProp = new SimpleBooleanProperty();
    private final BooleanProperty sortviaRegexProp = new SimpleBooleanProperty();

    @FXML
    private TextField tfMsg;
    @FXML
    private TextField ausOrdner;
    @FXML
    private AnchorPane apGeneral;
    @FXML
    private AnchorPane apErweitert;
    @FXML
    private CheckBox dateNaming;
    @FXML
    private CheckBox orderByDate;
    @FXML
    private CheckBox verschieben;
    @FXML
    private CheckBox sortSubFolder;
    @FXML
    private CheckBox sortByName;

    public static void show(Stage stage, Statement statement, ResourceBundle bundle) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(GeneralController.class.getResource(VIEWNAME), bundle);
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

            controller.statement = statement;
            controller.bundle = bundle;

            // View initialisieren
            controller.init(stage);

            // Anzeigen
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    /**
     * Initialisierungsfunktion, Füllt einen DataMover mit einem StandardSet aus
     * Dateitypen.: Bilder,Dokumente,Videos,Audio
     *
     * @param stage
     * @throws IOException
     */
    public void init(Stage stage) throws IOException, SQLException {
        getApGeneral().visibleProperty().bind(GeneralDisplayProperty());
        getApErweitert().visibleProperty().bind(GeneralDisplayProperty().not());
        generalDisplay.set(true);

        ausOrdner.setEditable(false);
        ausOrdner.textProperty().bindBidirectional(this.ausProp);

        DateNamingPropProperty().bindBidirectional(dateNaming.selectedProperty());
        OrderByDatePropProperty().bindBidirectional(orderByDate.selectedProperty());
        VerschiebenPropProperty().bindBidirectional(verschieben.selectedProperty());
        sortviaRegexPropProperty().bindBidirectional(sortByName.selectedProperty());
        sortSubFolderPropProperty().bindBidirectional(sortSubFolder.selectedProperty());

        props.load(new FileInputStream(propertyFile));
        // Properties File laden
        String datenaming = props.getProperty("dateNaming");
        String orderbydate = props.getProperty("orderbydate");
        String verschieben = props.getProperty("verschieben");
        String unterordner = props.getProperty("unterordner");
        String regex = props.getProperty("regex");

        if ("true".equals(datenaming)) {
            DateNamingPropProperty().set(true);
        }
        if ("true".equals(orderbydate)) {
            OrderByDatePropProperty().set(true);
        }
        if ("true".equals(verschieben)) {
            VerschiebenPropProperty().set(true);
        }
        if ("true".equals(unterordner)) {
            sortSubFolderPropProperty().set(true);
        }
        if ("true".equals(regex)) {
            sortviaRegexPropProperty().set(true);
        }

        String sqlQuery = "select datatype, extension from dateiendung";

        ResultSet rSet = statement.executeQuery(sqlQuery);

        mover = new DataMover(this);
        while (rSet.next()) {
            String typ = rSet.getString("datatype");
            DataType datatype = new DataType(new File(typ).getAbsoluteFile());
            String[] array = rSet.getString("extension").split(",");

            for (String ex : array) {
                if (ex != null && !ex.equals("")) {
                    Extension extension = new Extension(ex);
                    datatype.addExtension(extension);
                }
            }
            mover.addDataType(datatype);
        }
        this.stage = stage;

        this.showSuccessMessage("DataOrganizer - Sortiert ihre Dateien via Dateitypen");
    }

    @FXML
    private void erweitern(ActionEvent event) {
        erweitern();
    }

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    @FXML
    private void ordnerBtAus(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            this.setAusProp(selectedDirectory.toString());
        }
    }

    @FXML
    private void tableView(ActionEvent event) {
        ErweiterterController.show(stage, null, mover, statement, bundle);
    }

    @FXML
    private void speichern(ActionEvent event) throws Exception {
        speichern();
    }

    @FXML
    private void abbrechen(ActionEvent event) {
        abbrechen();
    }

    private void erweitern() {
        generalDisplay.set(false);
    }

    private void speichern() throws Exception {
        Boolean date = this.isDateNamingProp();
        Boolean orderbydate = this.isOrderByDateProp();
        Boolean verschieben = this.isVerschiebenProp();
        Boolean unterordner = this.issortSubFolderProp();
        Boolean regex = this.issortviaRegexProp();

        props.setProperty("dateNaming", date.toString());
        props.setProperty("orderbydate", orderbydate.toString());
        props.setProperty("verschieben", verschieben.toString());
        props.setProperty("unterordner", unterordner.toString());
        props.setProperty("regex", regex.toString());

        FileOutputStream out = new FileOutputStream(this.propertyFile);

        props.store(out, null);

        generalDisplay.set(true);
    }

    private void abbrechen() {
        generalDisplay.set(true);
    }

    private void sortieren() {
        try {
            String dateNaming = props.getProperty("dateNaming");
            mover.sort(new File(this.getAusProp()).listFiles());
            if ("true".equals(dateNaming)) {
                mover.order();
            }
        } catch (IOException ex) {
            showErrorMessage("Fehler beim sortieren!");
        }
    }

    public DataMover getMover() {
        return mover;
    }

    public String getAusProp() {
        return ausProp.get();
    }

    public final void setAusProp(String value) {
        ausProp.set(value);
    }

    public File getDirectory() {
        return selectedDirectory;
    }

    public boolean isGeneralDisplay() {
        return generalDisplay.get();
    }

    public void setGeneralDisplay(boolean value) {
        generalDisplay.set(value);
    }

    public BooleanProperty GeneralDisplayProperty() {
        return generalDisplay;
    }

    public AnchorPane getApGeneral() {
        return apGeneral;
    }

    public AnchorPane getApErweitert() {
        return apErweitert;
    }

    public boolean isDateNamingProp() {
        return dateNamingProp.get();
    }

    public void setDateNamingProp(boolean value) {
        dateNamingProp.set(value);
    }

    public BooleanProperty DateNamingPropProperty() {
        return dateNamingProp;
    }

    public boolean isOrderByDateProp() {
        return orderByDateProp.get();
    }

    public void setOrderByDateProp(boolean value) {
        orderByDateProp.set(value);
    }

    public BooleanProperty OrderByDatePropProperty() {
        return orderByDateProp;
    }

    public boolean isVerschiebenProp() {
        return verschiebenProp.get();
    }

    public void setVerschiebenProp(boolean value) {
        verschiebenProp.set(value);
    }

    public BooleanProperty VerschiebenPropProperty() {
        return verschiebenProp;
    }

    public boolean issortSubFolderProp() {
        return sortSubFolderProp.get();
    }

    public void setsortSubFolderProp(boolean value) {
        sortSubFolderProp.set(value);
    }

    public BooleanProperty sortSubFolderPropProperty() {
        return sortSubFolderProp;
    }

    public boolean issortviaRegexProp() {
        return sortviaRegexProp.get();
    }

    public void setsortviaRegexProp(boolean value) {
        sortviaRegexProp.set(value);
    }

    public BooleanProperty sortviaRegexPropProperty() {
        return sortviaRegexProp;
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
