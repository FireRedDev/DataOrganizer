package viewController;

import data.DataType;
import data.Extension;
import einstellungViewC.ErweitertC;
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
    @FXML
    private Button ordnerBt;
    @FXML
    private TextField ordnerTf;
    
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

    public void init() {
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
    
    public String getString(){
        filestring = selectedDirectory.toString();
        return filestring;
    }

    public File getDirectory(){
        return selectedDirectory;
    }
    
    private void sortieren() {
        try {
            // System.out.println("Log:");
            mover.sort();

            //System.out.println("Files Sorted");
        } catch (IOException ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
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
