package main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import viewController.GeneralController;

/**
 * TheMain
 */
public class TheMain extends Application {

    public static void main(String[] args) {
        final boolean test = true;
        try {
            if (test) {
                //Inhalt der Ordner wird gelöscht
                //Ordner bleiben jedoch bestehend
                test();
            }

            launch(args);
        } catch (IOException ex) {
            Logger.getLogger(TheMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        GeneralController.show(stage);
    }

    /**
     * deleteDir
     * <p>
     * Löscht Inhalt eines Ordners samt Unterordner.
     * </p>
     *
     * @param path Ordnerpfad
     */
    private static void deleteDir(File path) {
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            }
            file.delete();
        }
        path.delete();
    }

    /**
     * Klasse für Testzwecke, Löscht die Daten aus den Directorys und kopiert
     * sie zurück in Zusortierend
     *
     * @throws IOException
     */
    private static void test() throws IOException {
        File audio = new File("Audio");
        if (audio.exists()) {
            deleteDir(audio);
        }
        File bilder = new File("Bilder");
        if (bilder.exists()) {
            deleteDir(bilder);
        }
        File dokumente = new File("Dokumente");
        if (dokumente.exists()) {
            deleteDir(dokumente);
        }
        File video = new File("Video");
        if (video.exists()) {
            deleteDir(video);
        }
//        File sort = new File(FileUtils.getUserDirectoryPath() + "\\sortiert");
//        if (sort.exists()) {
//            deleteDir(sort);
//        }
        //Dateien werden in den ZusortierendeDateien Ordner verschoben.
        //default Ordner wird erstellt
//        FileUtils.copyDirectoryToDirectory(new File("ZusortierendeDateien"), new File(FileUtils.getUserDirectoryPath()));
//        FileUtils.copyFileToDirectory(new File("bild.jpg"), new File(FileUtils.getUserDirectoryPath() + "\\ZusortierendeDateien"));
//        FileUtils.copyFileToDirectory(new File("Screenshot.png"), new File(FileUtils.getUserDirectoryPath() + "\\ZusortierendeDateien"));
//        FileUtils.copyFileToDirectory(new File("dok.docx"), new File(FileUtils.getUserDirectoryPath() + "\\ZusortierendeDateien"));
//        FileUtils.copyFileToDirectory(new File("dokument.pdf"), new File(FileUtils.getUserDirectoryPath() + "\\ZusortierendeDateien"));
//        FileUtils.copyFileToDirectory(new File("pdf.pdf"), new File(FileUtils.getUserDirectoryPath() + "\\ZusortierendeDateien"));
//        FileUtils.copyFileToDirectory(new File("word.docx"), new File(FileUtils.getUserDirectoryPath() + "\\ZusortierendeDateien"));
    }
}
