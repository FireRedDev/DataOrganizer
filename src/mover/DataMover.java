package mover;

import data.DataType;
import data.RegexRule;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.RegexFileFilter;
import viewController.*;

/**
 * DataMover
 * <p>
 * DataMover ist das Herzstück von Dataorganizer, da hier Dateien sortiert
 * werden. DataMover kennt eine Dateitypliste. Es werden Dateien aus dem Ordner
 * ZusortieredeDateien sortiert. Alle Dateien aus diesem Ordner werden je nach
 * Extension und somit Dateityp in einen eigenen Ordner (Dokumente,Bilder,..)
 * verschoben.
 * </p>
 */
public class DataMover {

    private List<DataType> datatype;
    private List<RegexRule> regexrules;

    public DoubleProperty progress = new SimpleDoubleProperty();
    private final BooleanProperty abbrechen = new SimpleBooleanProperty();

    GeneralController controller;
    private int anz;
    private int files = 0;

    private static Stage stage;
    private final static String VIEWNAME = "progressV.fxml";

    @FXML
    private ProgressBar progressbar;

    /**
     * Standardkonstruktor, Benötigt einen Controller und einen Datentyp
     *
     * @param controller GenerallController
     */
    public DataMover(GeneralController controller) {
        datatype = new LinkedList<>();
        regexrules = new LinkedList<>();

        this.controller = controller;
    }

    public static void show(Stage parentStage, Stage stage, ResourceBundle bundle) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(DataMover.class.getResource(VIEWNAME), bundle);
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.getIcons().add(new Image(DataMover.class.getResourceAsStream("icon.png")));
            stage.setResizable(false);

            // Controller ermitteln
            DataMover controller = (DataMover) loader.getController();

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
     * Init
     *
     * @param stage
     * @param mover
     * @throws SQLException
     */
    private void init(Stage stage) throws SQLException {
        this.stage = stage;

        progressbar.progressProperty().bindBidirectional(progress);
    }

    public static void hide() {
        stage.close();
    }

    @FXML
    private void abbrechen(ActionEvent event) {
        abbrechen();
    }

    private void abbrechen() {
        setAbbrechenProp(true);
    }

    /**
     * sortieren nach Monat aufrufen
     */
    public void order() {
        try {
            if (controller.issortviaRegexProp()) {
                for (RegexRule r : regexrules) {
                    r.order(controller);
                }
            } else {
                for (DataType typ : datatype) {
                    typ.order(controller);
                }
            }
        } catch (IOException ex) {
            controller.showErrorMessage("Fehler beim sortieren nach Datum!");
        }
    }

    /**
     * sortieren nach Regex
     *
     * @param directoryListing
     * @throws IOException
     */
    public void sortbyRegex(File[] directoryListing) throws IOException {
        boolean subfolder = controller.issortSubFolderProp();
        boolean verschieben = controller.isVerschiebenProp();
        FileFilter fileFilter;
        File f;
        File dir = new File(controller.getAusProp());

        for (RegexRule rule : regexrules) {
            if (controller.isexpertenmodusProp()) {
                fileFilter = new RegexFileFilter(rule.getRegex());
            } else {
                fileFilter = new RegexFileFilter("^.*" + rule.getRegex() + ".*$");
            }
            File[] filelist = dir.listFiles(fileFilter);
            for (int i = 0; i < filelist.length; i++) {
                if (isAbbrechenProp()) {
                    break;
                }
                if (filelist[i].isDirectory() && subfolder) {
                    this.sortbyRegex(filelist[i].listFiles());
                }
                if (controller.isDateNamingProp()) {
                    String filename = datum(filelist[i]);
                    filename = rule.getOrdner() + "\\" + filename + "." + FilenameUtils.getExtension(filelist[i].getName());
                    if (new File(filename).exists()) {
                        f = new File(filename.substring(0, filename.indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(filelist[i].getName()));
                        anz++;
                    } else {
                        f = new File(filename);
                    }

                } else {
                    f = new File(rule.getOrdner() + "\\" + filelist[i].getName());
                }
                if (f.exists()) {
                    f = new File(rule.getOrdner() + "\\" + f.getAbsoluteFile().toString().substring(0, filelist[i].getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(filelist[i].getName()));
                    anz++;
                }
                verschieben(verschieben, filelist[i], f);
                progress.set((double) i * 1.0 / filelist.length);
            }
            controller.showSuccessMessage(controller.getBundle().getString("DataVon") + controller.getAusProp() + controller.getBundle().getString("sortiert"));
        }
    }

    /**
     * sortieren nach Dateityp
     *
     * @param directoryListing
     */
    public void sort(File[] directoryListing) {
        this.setAbbrechenProp(false);
        show(controller.getStage(), null, controller.getBundle());

        sortieren(directoryListing);
        hide();
        this.setProgressProp(0.0);
    }

    private void sortieren(File[] directoryListing) {
        boolean rename = controller.isDateNamingProp();
        boolean verschieben = controller.isVerschiebenProp();
        boolean subfolder = controller.issortSubFolderProp();
        boolean sortviaRegex = controller.issortviaRegexProp();
        anz = 0;
        File f;

        if (sortviaRegex) {
            try {
                sortbyRegex(directoryListing);
            } catch (IOException ex) {
                controller.showErrorMessage(controller.getBundle().getString("FehlerSort"));
            }
        } else {
            String aus = controller.getAusProp();
            if (aus != null) {
//                Instant start = Instant.now();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        if (isAbbrechenProp()) {
                            break;
                        }
                        int length = directoryListing.length;
                        if (child.isDirectory() && subfolder) {
                            this.sortieren(child.listFiles());
                        } else {
                            for (DataType type : datatype) {
                                if (type.search(FilenameUtils.getExtension(child.getName()))) {
                                    try {
                                        if (rename) {
                                            String filename = datum(child);
                                            filename = type.toString() + "\\" + filename + "." + FilenameUtils.getExtension(child.getName());
                                            if (new File(filename).exists()) {
                                                f = new File(filename.substring(0, filename.indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                                anz++;
                                            } else {
                                                f = new File(filename);
                                            }

                                        } else {
                                            f = new File(type.toString() + "\\" + child.getName());
                                        }
                                        if (f.exists()) {
                                            f = new File(type.toString() + "\\" + f.getAbsoluteFile().toString().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                            anz++;
                                        }
                                        verschieben(verschieben, child, f);
                                    } catch (IOException ex) {
                                        controller.showErrorMessage(controller.getBundle().getString("FehlerSort"));
                                    }
                                    break;
                                }

                            }
                        }
                        files++;
                        this.setProgressProp((double) files * 1.0 / length);

                    }
                    if (controller.isOrderByDateProp()) {
                        order();
                    }
                    controller.showSuccessMessage(controller.getBundle().getString("DataVon") + controller.getAusProp() + controller.getBundle().getString("sortiert"));
                } else {
                    controller.showErrorMessage(controller.getBundle().getString("AusgangsordnerUndefiniert"));
                }
            }
        }
    }

    private void verschieben(boolean verschieben, File child, File f) throws IOException {
        if (verschieben) {
            FileUtils.moveFile(child, f);
        } else {
            FileUtils.copyFile(child, f);
        }
    }

    public List<RegexRule> getRegexrules() {
        return regexrules;
    }

    public void setRegexrules(List<RegexRule> regexrules) {
        this.regexrules = regexrules;
    }

    /**
     * Datum auslesen
     *
     * @param child File
     * @return YYYY_MM_DD
     * @throws IOException Exception
     */
    private String datum(File child) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class
        );
        Instant creationTime = attr.lastModifiedTime().toInstant();
        LocalDateTime date = LocalDateTime.ofInstant(creationTime, ZoneOffset.UTC);
        String monat = Integer.toString(date.getMonthValue());
        if (date.getMonthValue() < 10) {
            monat = "0" + date.getMonthValue();
        }
        String tag = Integer.toString(date.getDayOfMonth());
        if (date.getDayOfMonth() < 10) {
            tag = "0" + date.getDayOfMonth();
        }
        String filename = date.getYear() + "_" + monat + "_" + tag;
        return filename;
    }

    /**
     * Add DataTyp
     *
     * @param typ DataType
     */
    public void addDataType(DataType typ) {
        if (!datatype.contains(typ)) {
            datatype.add(typ);
            typ.setMover(this);
        }
    }

    public void removeDataType(DataType typ) {
        if (datatype.contains(typ)) {
            datatype.remove(typ);
            typ.setMover(this);
        }
    }

    public List<DataType> getDatatype() {
        return datatype;
    }

    public boolean contains(DataType d) {
        return datatype.contains(d);
    }

    public DataType getDataType(String d) {
        for (DataType type : datatype) {
            if (type.toString().equals(d)) {
                return type;
            }
        }
        return null;
    }

    public RegexRule getRegexRule(String d) {
        for (RegexRule rule : this.regexrules) {
            if (rule.getRegex().equals(d)) {
                return rule;
            }
        }
        return null;
    }

    public void removeRegexRule(RegexRule regex) {
        if (regexrules.contains(regex)) {
            regexrules.remove(regex);
            regex.setMover(this);
        }
    }

    public void addRegexRule(RegexRule regexRule) {
        if (!regexrules.contains(regexRule)) {
            regexrules.add(regexRule);
            regexRule.setMover(this);
        }
    }

    public Double getProgressProp() {
        return progress.get();
    }

    public final void setProgressProp(Double value) {
        progress.set(value);
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
}
