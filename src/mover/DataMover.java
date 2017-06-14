package mover;

import data.DataType;
import data.RegexRule;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.RegexFileFilter;
import viewController.GeneralController;

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

    GeneralController controller;
    private int anz;
    private int files = 0;

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

    /**
     * sortieren nach Monat aufrufen
     */
    public void order() {
        try {
            for (DataType typ : datatype) {
                typ.order(controller);
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
//        Instant start = Instant.now();
        boolean subfolder = controller.issortSubFolderProp();
        boolean verschieben = controller.isVerschiebenProp();
        FileFilter fileFilter;
        File dir = new File(controller.getAusProp());
        for (RegexRule rule : regexrules) {
            if (controller.isexpertenmodusProp()) {
                fileFilter = new RegexFileFilter(rule.getRegex());
            } else {
                fileFilter = new RegexFileFilter("^.*" + rule.getRegex() + ".*$");
            }
            File[] files = dir.listFiles(fileFilter);
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() && subfolder) {
                    this.sortbyRegex(files[i].listFiles());
                }
                this.verschiebenToDirectory(verschieben, files[i], new File(rule.getOrdner()));
                controller.progress.set((double) i * 1.0 / files.length);
            }
//            Instant end = Instant.now();
//            System.out.println("Sortierzeit: " + Duration.between(start, end).toNanos());
            controller.showSuccessMessage("Dateien von " + controller.getAusProp() + " sortiert!");
        }
    }

    /**
     * sortieren nach Dateityp
     *
     * @param directoryListing
     * @throws IOException
     */
    public void sort(File[] directoryListing) throws IOException {
        //Offene Fragen: Brauche ich eine FIlexistsexception?
        boolean rename = controller.isDateNamingProp();
        boolean verschieben = controller.isVerschiebenProp();
        boolean subfolder = controller.issortSubFolderProp();
        boolean sortviaRegex = controller.issortviaRegexProp();
        anz = 0;
        File f;

        if (sortviaRegex) {
            sortbyRegex(directoryListing);
        } else {
            String aus = controller.getAusProp();
            if (aus != null) {
//                Instant start = Instant.now();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        int length = directoryListing.length;
                        if (child.isDirectory() && subfolder) {
                            this.sort(child.listFiles());
                        } else {
                            for (DataType type : datatype) {
                                if (type.search(FilenameUtils.getExtension(child.getName()))) {
                                    try {
                                        //property adden nd vagessn
                                        //fehler

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
                                    } catch (FileExistsException ex) {
                                        f = new File(type.toString() + "\\" + child.getName().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                        anz++;
                                        verschieben(verschieben, child, f);
                                    } catch (IOException ex) {
                                        controller.showErrorMessage(controller.getBundle().getString("FehlerSort"));
                                    }
                                    break;
                                }

                            }
                        }
                        files++;
                        controller.progress.set((double) files * 1.0 / length);
                        
                    }
//                    Instant end = Instant.now();
//                    System.out.println("Sortierzeit: " + Duration.between(start, end).toNanos());
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

    private void verschiebenToDirectory(boolean verschieben, File child, File f) throws IOException {
        if (verschieben) {
            FileUtils.moveFileToDirectory(child, f, true);

        } else {
            FileUtils.copyFileToDirectory(child, f);
        }
    }

    private File exists(File f, DataType type, File child) {
        if (f.exists()) {
            f = new File(type.toString() + "\\" + f.getAbsoluteFile().toString().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
            anz++;
        }
        return f;
    }

    private File rename(boolean rename, File child, DataType type) throws IOException {
        File f;
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
        return f;
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
//            regex.setMover(this);
        }
    }

    public void addRegexRule(RegexRule regexRule) {
        if (!regexrules.contains(regexRule)) {
            regexrules.add(regexRule);
            regexRule.setMover(this);
        }
    }
}
