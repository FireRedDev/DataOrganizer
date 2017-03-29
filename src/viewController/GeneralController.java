package viewController;

import nuetzlich.ActionListenerVar;
import nuetzlich.PrintToTextField;
import data.DataType;
import data.Extension;
import einstellungViewC.ErweitertC;
import java.awt.*;
import java.io.IOException;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import mover.DataMover;
import java.io.File;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.*;

/**
 * GeneralController
 * <p>
 * Controller zur ersten einfachen View.
 * </p>
 */
public class GeneralController {

    private final static String VIEWNAME = "GeneralV.fxml";

    public DataMover mover;

    @FXML
    private Button sortBt;
    @FXML
    private Button erweiternBt;
    @FXML
    private TextField tfMsg;
    @FXML
    private TextField ausOrdner;
    @FXML
    private TextField zielOrdner;

    private Stage stage;
    private File selectedDirectory, selectOutDirectory;
    private String filestring, fileOutstring;

    private final StringProperty ausProp = new SimpleStringProperty();
    private final StringProperty zielProp = new SimpleStringProperty();

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
            controller.init();

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    public void init() throws IOException {
        ausOrdner.textProperty().bindBidirectional(this.ausProp);
        zielOrdner.textProperty().bindBidirectional(this.zielProp);

        PrintToTextField.create(tfMsg);

        System.out.println("DataOrganizer - Sortiert ihre Dateien via Dateitypen");

        DataType bilder = new DataType("Bilder");
        bilder.addExtension(new Extension("jpg"));
        bilder.addExtension(new Extension("png"));
        bilder.addExtension(new Extension("gif"));
        bilder.addExtension(new Extension("jpeg"));

        DataType documente = new DataType("Dokumente");
        documente.addExtension(new Extension("pdf"));
        documente.addExtension(new Extension("docx"));
        documente.addExtension(new Extension("vsd"));
        documente.addExtension(new Extension("xlsx"));
        documente.addExtension(new Extension("zip"));

        DataType video = new DataType("Video");
        video.addExtension(new Extension("mp4"));
        video.addExtension(new Extension("mpeg"));
        video.addExtension(new Extension("avi"));
        video.addExtension(new Extension("wmv"));

        DataType audio = new DataType("Audio");
        audio.addExtension(new Extension("mp3"));
        audio.addExtension(new Extension("wma"));
        audio.addExtension(new Extension("ogg"));
        audio.addExtension(new Extension("flac"));

        mover = new DataMover(bilder, this);
        mover.addDataType(documente);
        mover.addDataType(video);
        mover.addDataType(audio);

        ActionListenerVar listener = new ActionListenerVar(mover);
        initializeSystemTray(listener);
//        System.out.println("Hast du das gemacht, drücke Enter:");
//        sc.nextLine();
    }

    private void sortieren() {
        try {
            // System.out.println("Log:");
            mover.sort();
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initializeSystemTray(ActionListenerVar listener) throws IOException {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem("Sortieren");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "DataOrganizer", popup);
            trayIcon.setImageAutoSize(true);

            // set the TrayIcon properties
            trayIcon.addActionListener(listener);
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

    public String getZielProp() {
        return zielProp.get();
    }

    public final void setZielProp(String value) {
        zielProp.set(value);
    }

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
        ErweitertC.show(null, mover);
    }

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    private void ausOrdner(ActionEvent event) {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectOutDirectory = chooser.showDialog(stage);
        this.setZielProp(selectOutDirectory.toString());
        //Msg();
    }

    /*private void Msg(){
     ordnerTf.setText("hallo");
     }*/
    @FXML
    private void ordnerBtAus(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectedDirectory = chooser.showDialog(stage);
        this.setAusProp(selectedDirectory.toString());
        //Msg();
    }

    @FXML
    private void ordnerBtZiel(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectedDirectory = chooser.showDialog(stage);
        this.setZielProp(selectedDirectory.toString());
        //Msg();
    }
}
