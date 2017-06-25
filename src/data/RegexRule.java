package data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;
import java.time.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mover.DataMover;
import org.apache.commons.io.FileUtils;
import GeneralViewController.GeneralController;

/**
 * Regex-Regel
 *
 * @author Isabella
 */
public class RegexRule {

    private final StringProperty Regex = new SimpleStringProperty();
    private final StringProperty Ordner = new SimpleStringProperty();
    private DataMover mover;

    public RegexRule(String ordner, String rule) {
        setOrdner(ordner);
        setRegex(rule);
    }

    /**
     * Order
     * <p>
     * Mithilfe dieser Funktion werden Dateien in den Regexpordnern in
     * Monatsordner sortiert.
     * </p>
     *
     * @param controller
     * @throws java.io.IOException
     */
    public void order(GeneralController controller) throws IOException {
        File[] directoryListing;
        directoryListing = new File(Ordner.get()).listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                String filename = datum(child);
                File diry = new File(new File(Ordner.get()).getAbsolutePath(), filename);
                diry.mkdir();
                try {
                    FileUtils.moveFileToDirectory(child, diry, true);
                } catch (java.io.IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * Regex-bearbeiten
     *
     * @param statement Datenbankstatement
     * @param value Value
     * @throws SQLException Exception
     * @throws IllegalArgumentException Exception
     */
    public void editRegex(Statement statement, String value) throws SQLException, IllegalArgumentException {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException("Regex muss eingegeben werden!");
        }

        if (value.length() < 2) {
            throw new IllegalArgumentException("Regex muss länger als 2 Zeichen sein!");
        }
        this.setRegex(value);

        String sql = "update regexrules set Ordner = '" + Ordner.getValue() + "', regex = '" + Regex.getValue() + "' where Ordner = '" + Ordner.getValue() + "' ";
        // Datenbankzugriff
        statement.executeUpdate(sql);
    }

    /**
     * Ordner-bearbeiten
     *
     * @param statement Datenbankstatement
     * @param value value
     * @throws SQLException Exception
     * @throws IllegalArgumentException Exception
     */
    public void editOrdner(Statement statement, String value) throws SQLException, IllegalArgumentException {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException("Pfad muss eingegeben werden!");
        }

        if (value.length() < 2) {
            throw new IllegalArgumentException("Pfad muss länger als 2 Zeichen sein!");
        }
        this.setOrdner(value);
//        setOrdner(Ordner.getValue());
        String sql = "update regexrules set Ordner = '" + Ordner.getValue() + "', regex = '" + Regex.getValue() + "' where regex = '" + Regex.getValue() + "' ";

        // Datenbankzugriff
        statement.executeUpdate(sql);

    }

    /**
     * Mover setzen
     *
     * @param neu zu setzender DataMover
     */
    public void setMover(DataMover neu) {
        if (mover != neu) {
            if (mover != null) {
                mover.removeRegexRule(this);
            }
            mover = neu;

            if (neu != null) {
                neu.addRegexRule(this);
            }
        }
    }

    /**
     * Datum für Ordnername
     * <br>
     * Mithilfe dieser Funktion bekommt man das Datum als String YYYY_MM zurück
     *
     * @param child File zum auslesen des Datums
     * @return String YYYY_MM
     * @throws IOException
     */
    private String datum(File child) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
        Instant creationTime = attr.lastModifiedTime().toInstant();
        LocalDateTime date = LocalDateTime.ofInstant(creationTime, ZoneOffset.UTC);
        String monat = Integer.toString(date.getMonthValue());
        if (date.getMonthValue() < 10) {
            monat = "0" + date.getMonthValue();
        }
        String filename = date.getYear() + "_" + monat;
        return filename;
    }

    public String getOrdner() {
        return Ordner.getValue();
    }

    public final void setOrdner(String value) {
        Ordner.set(value);
    }

    public StringProperty OrdnerProperty() {
        return Ordner;
    }

    public String getRegex() {
        return Regex.getValue();
    }

    public final void setRegex(String value) {
        Regex.set(value);
    }

    public StringProperty regexProperty() {
        return Regex;
    }
}
