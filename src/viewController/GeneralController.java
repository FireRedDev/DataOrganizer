package viewController;

import data.DataType;
import data.Extension;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import mover.DataMover;
import java.io.File;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
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

    public DataMover mover;

    private final BooleanProperty generalDisplay = new SimpleBooleanProperty();
    private final BooleanProperty erweitertDisplay = new SimpleBooleanProperty();
    private final BooleanProperty dateiDisplay = new SimpleBooleanProperty();
    @FXML
    private Button sortBt;
    @FXML
    private Button erweiternBt;
    @FXML
    private TextField tfMsg;
    @FXML
    private TextField ausOrdner;
//    @FXML
//    private TextField zielOrdner;
    @FXML
    private AnchorPane apGeneral;
    @FXML
    private AnchorPane apErweitert;
    @FXML
    private AnchorPane apDateityp;
    @FXML
    private CheckBox dateNaming;
    @FXML
    private CheckBox orderByDate;
    @FXML
    private TextField ausOrdnerTyp;
    @FXML
    private TextField tfTyp;
    @FXML
    private CheckBox verschieben;

    private final StringProperty ausOrdnerTypProp = new SimpleStringProperty();
    private final StringProperty typProp = new SimpleStringProperty();
    private LinkedList<DataType> typeList;

    private File selectedDirectory, selectOutDirectory;
    private String filestring, fileOutstring;

    private final StringProperty ausProp = new SimpleStringProperty();
//    private final StringProperty zielProp = new SimpleStringProperty();
    private final BooleanProperty dateNamingProp = new SimpleBooleanProperty();
    private final BooleanProperty orderByDateProp = new SimpleBooleanProperty();
    private final BooleanProperty verschiebenProp = new SimpleBooleanProperty();

    public static void show(Stage stage) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(GeneralController.class.getResource(VIEWNAME));
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
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    /**
     * Initialisierungsfunktion, Füllt einen DataMover mit einem StandardSet aus
     * Dateitypen.: Bilder,Dokumente,Videos,Audio
     *
     * @throws IOException
     */
    public void init(Stage stage) throws IOException {
        getApGeneral().visibleProperty().bind(GeneralDisplayProperty());
        getApErweitert().visibleProperty().bind(ErweitertDisplayProperty());
        getApDateityp().visibleProperty().bind(DateiDisplayProperty());

        stage.setOnCloseRequest((WindowEvent we) -> {
            stage.hide();
        });

        generalDisplay.set(true);
        erweitertDisplay.set(false);
        dateiDisplay.set(false);

        ausOrdner.setEditable(false);
        ausOrdner.textProperty().bindBidirectional(this.ausProp);
//        zielOrdner.setEditable(false);
//        zielOrdner.textProperty().bindBidirectional(this.zielProp);

        DateNamingPropProperty().bind(dateNaming.selectedProperty());
        OrderByDatePropProperty().bind(orderByDate.selectedProperty());
        VerschiebenPropProperty().bind(verschieben.selectedProperty());

        this.showSuccessMessage("DataOrganizer - Sortiert ihre Dateien via Dateitypen");

        DataType bilder = new DataType(new File("Bilder").getAbsoluteFile());
        bilder.addExtension(new Extension("jpg"));
        bilder.addExtension(new Extension("png"));
        bilder.addExtension(new Extension("gif"));
        bilder.addExtension(new Extension("jpeg"));

        DataType documente = new DataType(new File("Dokumente").getAbsoluteFile());
        documente.addExtension(new Extension("pdf"));
        documente.addExtension(new Extension("docx"));
        documente.addExtension(new Extension("vsd"));
        documente.addExtension(new Extension("xlsx"));
        documente.addExtension(new Extension("zip"));

        DataType video = new DataType(new File("Video").getAbsoluteFile());
        video.addExtension(new Extension("mp4"));
        video.addExtension(new Extension("mpeg"));
        video.addExtension(new Extension("avi"));
        video.addExtension(new Extension("wmv"));

        DataType audio = new DataType(new File("Audio").getAbsoluteFile());
        audio.addExtension(new Extension("mp3"));
        audio.addExtension(new Extension("wma"));
        audio.addExtension(new Extension("ogg"));
        audio.addExtension(new Extension("flac"));

        mover = new DataMover(bilder, this);
        mover.addDataType(documente);
        mover.addDataType(video);
        mover.addDataType(audio);
        this.stage = stage;

        ausOrdnerTyp.setEditable(false);
        ausOrdnerTyp.textProperty().bindBidirectional(this.ausOrdnerTypProp);
        tfTyp.textProperty().bindBidirectional(typProp);

        initializeSystemTray();
//        System.out.println("Hast du das gemacht, drücke Enter:");
//        sc.nextLine();
    }

    private void sortieren() {
        try {
            // System.out.println("Log:");
            if (isOrderByDateProp()) {
                mover.sort();
                mover.order();
            } else {
                mover.sort();
            }
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialisiert ein Icon im Systemtray(Taskleiste), von diesem aus kann
     * mann sortieren.
     *
     * @param listener
     * @throws IOException
     */
    private void initializeSystemTray() throws IOException {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            final SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem("Programm öffnen");

            popup.add(defaultItem);
            MenuItem close = new MenuItem("Programm schließen");

            popup.add(close);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "DataOrganizer", popup);
            trayIcon.setImageAutoSize(true);

            // set the TrayIcon properties
            trayIcon.addActionListener((java.awt.event.ActionEvent e) -> {
                Platform.runLater(() -> {
                    stage.show();
                });
            });

            final TrayIcon i = trayIcon;

            defaultItem.addActionListener((java.awt.event.ActionEvent e) -> {
                Platform.runLater(() -> {
                    stage.show();
                });
            });

            close.addActionListener((java.awt.event.ActionEvent e) -> {
                tray.remove(i);
            });
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        } else {
            // disable tray option in your application or
            // perform other actions

        }
        // ...
        // some time later
        // the application state has changed - update the image
        if (trayIcon != null) {
            // trayIcon.setImage(updatedImage);
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

//    public String getZielProp() {
//        return zielProp.get();
//    }
//
//    public final void setZielProp(String value) {
//        zielProp.set(value);
//    }
    public String getString() {
        filestring = selectedDirectory.toString();
        return filestring;
    }

    public String getSelectOutDirectory() {
        fileOutstring = selectOutDirectory.toString();
        return fileOutstring;
    }

    public File getDirectory() {
        return selectedDirectory;
    }

    public File getOutDirectory() {
        return selectOutDirectory;
    }

    @FXML
    private void erweitern(ActionEvent event) {
        erweitern();
    }

    private void erweitern() {
        //ErweitertC.show(null, mover);
        generalDisplay.set(false);
        erweitertDisplay.set(true);
        dateiDisplay.set(false);
    }

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    /*private void Msg(){
     ordnerTf.setText("hallo");
     }*/
    @FXML
    private void ordnerBtAus(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            this.setAusProp(selectedDirectory.toString());
        }
        //Msg();
    }

//    @FXML
//    private void ordnerBtZiel(ActionEvent event) {
//        DirectoryChooser chooser = new DirectoryChooser();
//        chooser.setTitle("Data Organizer");
//        selectedDirectory = chooser.showDialog(stage);
//        this.setZielProp(selectedDirectory.toString());
//        //Msg();
//    }
    @FXML
    private void speichern(ActionEvent event) {
        speichern();
    }

    private void speichern() {
        generalDisplay.set(true);
        erweitertDisplay.set(false);
        dateiDisplay.set(false);
    }

    @FXML
    private void abbrechen(ActionEvent event) {
        abbrechen();
    }

    private void abbrechen() {
        generalDisplay.set(true);
        erweitertDisplay.set(false);
        dateiDisplay.set(false);
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

    public boolean isErweitertDisplay() {
        return erweitertDisplay.get();
    }

    public void setErweitertDisplay(boolean value) {
        erweitertDisplay.set(value);
    }

    public BooleanProperty ErweitertDisplayProperty() {
        return erweitertDisplay;
    }

    public boolean isDateiDisplay() {
        return dateiDisplay.get();
    }

    public void setDateiDisplay(boolean value) {
        dateiDisplay.set(value);
    }

    public BooleanProperty DateiDisplayProperty() {
        return dateiDisplay;
    }

    public AnchorPane getApGeneral() {
        return apGeneral;
    }

    public AnchorPane getApErweitert() {
        return apErweitert;
    }

    @FXML
    private void erweiternDateityp(ActionEvent event) {
        erweiternDateityp();
    }

    private void erweiternDateityp() {
        generalDisplay.set(false);
        erweitertDisplay.set(false);
        dateiDisplay.set(true);
    }

    public AnchorPane getApDateityp() {
        return apDateityp;
    }

    @FXML
    private void abbrechenD(ActionEvent event) {
        generalDisplay.set(false);
        erweitertDisplay.set(true);
        dateiDisplay.set(false);
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
        if (this.getTypProp() != null && this.getAusOrdnerTyp() != null) {
            boolean eingabefehler = false;
            Extension extension = new Extension(this.getTypProp());

            java.util.List<DataType> datatype = mover.getDatatype();
            for (DataType type : datatype) {
                if (type.getOrdner().equals(new File(this.getAusOrdnerTyp()))) {
                    type.addExtension(extension);
                    mover.addDataType(type);
                    eingabefehler = true;
                    showSuccessMessage("Neue Extension wurde gespeichert!");
                }
                LinkedList<Extension> extensionlist = type.getExtensionlist();
                for (Extension e : extensionlist) {
                    if (extension.getExtension().equals(e.getExtension())) {
                        eingabefehler = true;
                        showErrorMessage("Extension wird bereits anders sortiert!");
                    }
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
                    typ.addExtension(extension);
                    mover.addDataType(typ);
                    showSuccessMessage("Neue Extension wurde gespeichert!");
                }
            }
        } else {
            showErrorMessage("Eingabefehler!");
        }
//        if (!typeList.contains(new DataType(typProp.toString()))) {
//            DataType newType = new DataType(typProp.toString());
//            typeList.add(newType);
//            if (!newType.getExtensionlist().contains(new Extension(typProp.toString()))) {
//                newType.addExtension(new Extension(typProp.toString()));
//            } else {
//                System.out.println("Diese Extension ist in diesem Datentypen bereits vorhanden.");
//            }
//        } else {
//            System.out.println("Dieser Datentyp existiert bereits.");
//        }
    }

    private void erase(ActionEvent event) {
        DataType typ = new DataType(new File(this.getAusOrdnerTyp()));
        Extension extension = new Extension(this.getTypProp());
        typ.removeExtension(extension);
    }

    public final void setAusOrdnerTyp(String value) {
        ausOrdnerTypProp.set(value);
    }

    private void setTypProp(String value) {
        typProp.set(value);
    }

    private String getAusOrdnerTyp() {
        return ausOrdnerTypProp.get();
    }

    private String getTypProp() {
        return typProp.get();
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
