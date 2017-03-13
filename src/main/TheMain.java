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
            initializeSystemTray();
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

    private static void initializeSystemTray() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("Settings.jpg");
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    // ...
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem("item");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Tray Demo", popup);
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
}
