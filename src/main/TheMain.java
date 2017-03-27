package main;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import viewController.GeneralController;

/**
 * TheMain
 */
public class TheMain extends Application {

    private static final boolean test = true;

    public static void main(String[] args) {
        try {
            if (test) {
                //Inhalt der Ordner wird gelöscht
                //Ordner bleiben jedoch bestehend
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
                //Dateien werden in den ZusortierendeDateien Ordner verschoben.
                FileUtils.copyFileToDirectory(new File("bild.jpg"), new File("ZusortierendeDateien"));
                FileUtils.copyFileToDirectory(new File("Screenshot.png"), new File("ZusortierendeDateien"));
                FileUtils.copyFileToDirectory(new File("dok.docx"), new File("ZusortierendeDateien"));
                FileUtils.copyFileToDirectory(new File("dokument.pdf"), new File("ZusortierendeDateien"));
                FileUtils.copyFileToDirectory(new File("pdf.pdf"), new File("ZusortierendeDateien"));
                FileUtils.copyFileToDirectory(new File("word.docx"), new File("ZusortierendeDateien"));
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
    public static void deleteDir(File path) {
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            }
            file.delete();
        }
        path.delete();
    }

 
}
