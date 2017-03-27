package viewController;

import data.DataType;
import data.Extension;
import einstellungViewC.ErweitertC;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import mover.DataMover;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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

    private Stage stage;
    private File selectedDirectory;
    private String filestring;

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
            System.err.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.err);
            System.exit(1);
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(System.err);
            System.exit(2);
        }
    }

    @FXML
    private void sort(ActionEvent event) {
        sortieren();
    }

    public void init() throws IOException {
       
        Scanner sc = new Scanner(System.in);
        System.out.println("DataOrganizer - Sortiert ihre Dateien via Dateitypen(Diese haben eine oder Mehrere Dateiendungen abgespeichert)");

        DataType bilder = new DataType("Bilder");
        //könnte sein das der punkt weggehört
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
        mover = new DataMover(bilder);
        mover.addDataType(documente);
        mover.addDataType(video);
        mover.addDataType(audio);
        System.out.println("Ordner Erstellt, Bitte gebe jetzt deine Dateien in den Ordner mit dem Namen 'zusortierend'.");
 ActionListenerVar listener = new ActionListenerVar(mover);
        initializeSystemTray(listener);
//        System.out.println("Hast du das gemacht, drücke Enter:");
//        sc.nextLine();
    }

    @FXML
    private void ordnerBtOnAction(ActionEvent event) {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        selectedDirectory = chooser.showDialog(stage);
        //Msg();
    }

    /*private void Msg(){
     ordnerTf.setText("hallo");
     }*/
    public String getString() {
        filestring = selectedDirectory.toString();
        return filestring;
    }

    public File getDirectory() {
        return selectedDirectory;
    }

    private void sortieren() {
        try {
            // System.out.println("Log:");
            mover.sort();
            Alert alConfirm = new Alert(Alert.AlertType.INFORMATION);
            alConfirm.setHeaderText("Dateien wurden sortiert!");
            alConfirm.show();
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
            //D:\\Schule\\OneDrive\\3tes Jahr\\SEW\\Programme\\dataorganizer\\src\\icon.png"
            System.out.print(new java.io.File(".").getCanonicalPath() + "\\src\\icon.png");
            // create a action listener to listen for default action executed on the tray icon
           
  
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

    @FXML
    private void erweitern(ActionEvent event) {
        erweitern();
    }

    private void erweitern() {
        ErweitertC.show(null, mover);
    }

    public DataMover getMover() {
        return mover;
    }

}
