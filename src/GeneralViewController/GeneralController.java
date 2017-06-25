package GeneralViewController;

import RegexViewController.ErweiterterControllerRegex;
import DataTypViewController.ErweiterterController;
import data.*;
import java.io.IOException;
import javafx.fxml.*;
import javafx.scene.Parent;
import mover.DataMover;
import java.io.File;
import java.io.*;
import java.sql.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import ProgressViewController.ProgressController;

/**
 * GeneralController
 * <br>
 * Objekte dieser Klasse werden vom FXML-Loader erstellt. Dabei füllt dieser
 * auch die mit @FXML markiertenen Referenzen auf die Controls der View und
 * verknüpft die mit @FXML gekennzeichneten Methoden als Actions, etc.
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
    private final BooleanProperty abbrechen = new SimpleBooleanProperty();
    public DoubleProperty progress = new SimpleDoubleProperty();

    String propertyFile = "DOproperties.properties";

    private final StringProperty ausProp = new SimpleStringProperty();
    private final BooleanProperty dateNamingProp = new SimpleBooleanProperty();
    private final BooleanProperty orderByDateProp = new SimpleBooleanProperty();
    private final BooleanProperty verschiebenProp = new SimpleBooleanProperty();
    private final BooleanProperty sortSubFolderProp = new SimpleBooleanProperty();
    private final BooleanProperty sortviaRegexProp = new SimpleBooleanProperty();
    private final BooleanProperty expertenmodus = new SimpleBooleanProperty();

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
    @FXML
    private CheckBox regex;

    /**
     * Anzeige der View.
     * <br>
     * Diese Methode erstellt eine Instanz der View und dieses Controllers
     * (FXML-Loader) und richtet alles (also vor allem den Controller) so weit
     * ein, dass es angezeigt werden kann.
     *
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @param statement Datenbankverbindung
     * @param bundle ResourceBundle, wird benötigt für die Internationalisierung
     */
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
            stage.initStyle(StageStyle.UNIFIED);
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));
            stage.getIcons().add(new Image(GeneralController.class.getResourceAsStream("../icon.png")));
            stage.setResizable(false);

            // Controller ermitteln
            GeneralController controller = (GeneralController) loader.getController();

            controller.statement = statement;
            controller.bundle = bundle;

            // View initialisieren
            controller.init(stage);

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
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @throws IOException Exception
     */
    private void init(Stage stage) throws IOException, SQLException {
        getApGeneral().visibleProperty().bind(GeneralDisplayProperty());
        getApErweitert().visibleProperty().bind(GeneralDisplayProperty().not());

        generalDisplay.set(true);

        ausOrdner.setEditable(false);
        ausOrdner.textProperty().bindBidirectional(this.ausProp);

        sortviaRegexPropProperty().bindBidirectional(regex.visibleProperty());
        DateNamingPropProperty().bindBidirectional(dateNaming.selectedProperty());
        OrderByDatePropProperty().bindBidirectional(orderByDate.selectedProperty());
        VerschiebenPropProperty().bindBidirectional(verschieben.selectedProperty());
        sortviaRegexPropProperty().bindBidirectional(sortByName.selectedProperty());
        sortSubFolderPropProperty().bindBidirectional(sortSubFolder.selectedProperty());
        expertenmodusProperty().bindBidirectional(regex.selectedProperty());

        //Existiert PropertyDatei?
        if (new File(propertyFile).exists()) {
            props.load(new FileInputStream(propertyFile));

            // Properties File laden
            String datenaming = props.getProperty("dateNaming");
            String orderbydate = props.getProperty("orderbydate");
            String move = props.getProperty("verschieben");
            String unterordner = props.getProperty("unterordner");
            String dateinamenSortieren = props.getProperty("regex");
            String experte = props.getProperty("experte");

            //ausgelesene Werte setzen
            if ("true".equals(datenaming)) {
                DateNamingPropProperty().set(true);
            }
            if ("true".equals(orderbydate)) {
                OrderByDatePropProperty().set(true);
            }
            if ("true".equals(move)) {
                VerschiebenPropProperty().set(true);
            }
            if ("true".equals(unterordner)) {
                sortSubFolderPropProperty().set(true);
            }
            if ("true".equals(dateinamenSortieren)) {
                sortviaRegexPropProperty().set(true);
            }
            if ("true".equals(experte)) {
                expertenmodusProperty().set(true);
            }
        }

        String sqlQuery = "select datatype, extension from dateiendung";
        ResultSet rSet = statement.executeQuery(sqlQuery);

        //Ausgelesene Werte verwenden
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

        sqlQuery = "select ordner, regex from regexrules";

        rSet = statement.executeQuery(sqlQuery);

        while (rSet.next()) {
            String ordner = new File(rSet.getString("ordner")).getAbsolutePath();
            String rule = rSet.getString("regex");
            RegexRule regexRule = new RegexRule(ordner, rule);

            mover.addRegexRule(regexRule);
        }
        this.stage = stage;

        this.showSuccessMessage(bundle.getString("DataOrganizer"));
    }

    /**
     * Erweitern aufrufen
     * <br>
     * Das Erweitere-Einstellungen-Fenster wird geöffnet.
     *
     * @param event
     */
    @FXML
    private void erweitern(ActionEvent event) {
        generalDisplay.set(false);
    }

    /**
     * Sort aufrufen
     *
     * @param event
     */
    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    /**
     * Ordner auswählen
     * <br>
     * Ordner mithilfe des Directory-Chosser auswählen. Es wird die AusProp
     * gesetzt.
     *
     * @param event
     */
    @FXML
    private void ordnerBtAus(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(bundle.getString("DataOrganizer"));
        selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            this.setAusProp(selectedDirectory.toString());
        }
    }

    /**
     * Table-View aufrufen
     * <br>
     * Aufrufen der TableView je nachdem ob sortieren nach Namen ausgewählt ist,
     * oder nicht, wird eine andere View aufgerufen.
     *
     * @param event
     */
    @FXML
    private void tableView(ActionEvent event) {
        if (issortviaRegexProp()) {
            ErweiterterControllerRegex.show(stage, null, mover, statement, bundle);
        } else {
            ErweiterterController.show(stage, null, mover, statement, bundle);
        }
    }

    /**
     * Speichern
     * <br>
     * Wenn speichern aufgerufen ist, wird das Sortierfenster geschlossen und
     * speichern aufgerufen.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void speichern(ActionEvent event) throws Exception {
        speichern();
        generalDisplay.set(true);
    }

    /**
     * Abbrechen
     * <br>
     * Wenn abbrechen aufgerufen ist, wird das Sortierfenster geschlossen.
     *
     * @param event
     */
    @FXML
    private void abbrechen(ActionEvent event) {
        generalDisplay.set(true);
    }

    /**
     * speichern
     * <br>
     * Die Einstellungen werden ausgelesen und in die Property-Datei
     * geschrieben.
     *
     * @throws Exception Exception
     */
    private void speichern() throws Exception {
        Boolean date = this.isDateNamingProp();
        Boolean orderbydate = this.isOrderByDateProp();
        Boolean move = this.isVerschiebenProp();
        Boolean unterordner = this.issortSubFolderProp();
        Boolean sortierennachdateinamen = this.issortviaRegexProp();
        Boolean expertenmodusVerwenden = this.isexpertenmodusProp();

        props.setProperty("dateNaming", date.toString());
        props.setProperty("orderbydate", orderbydate.toString());
        props.setProperty("verschieben", move.toString());
        props.setProperty("unterordner", unterordner.toString());
        props.setProperty("regex", sortierennachdateinamen.toString());
        props.setProperty("experte", expertenmodusVerwenden.toString());

        FileOutputStream out = new FileOutputStream(this.propertyFile);

        props.store(out, null);
    }

    /**
     * sortieren
     * <br>
     * Mithilfe dieser Funktion werden Dateien sortiert und der
     * Fortschrittsbalken aufgerufen.
     */
    private void sortieren() {
        if (this.getAusProp() != null) {
            setAbbrechenProp(false);
            ProgressController.show(stage, null, this, bundle);
        } else {
            showErrorMessage(bundle.getString("FehlerSort"));
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

    public boolean isexpertenmodusProp() {
        return expertenmodus.get();
    }

    public void setexpertenmodusProp(boolean value) {
        expertenmodus.set(value);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public BooleanProperty expertenmodusProperty() {
        return expertenmodus;
    }

    public boolean isAbbrechenProp() {
        return abbrechen.get();
    }

    public void setAbbrechenProp(boolean value) {
        abbrechen.set(value);
    }

    public BooleanProperty AbbrechenPropProperty() {
        return abbrechen;
    }

    public Double getProgressProp() {
        return progress.get();
    }

    public final void setProgressProp(Double value) {
        progress.set(value);
    }

    public Stage getStage() {
        return stage;
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
