package data;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.*;

/**
 * Hilfsklasse um Datentyp für View aufbereiten zu können.
 *
 * @author Isabella
 */
public class Dateiendung {

    private final StringProperty Extension = new SimpleStringProperty();
    private final DataType type;
    private final StringProperty Ordner = new SimpleStringProperty();

    /**
     * Konstruktor
     * 
     * @param type DataType
     */
    public Dateiendung(DataType type) {
        this.type = type;
    }

    /**
     * Extension bearbeiten
     * 
     * @param statement Datenbankstatement
     * @param value Value
     * @throws IllegalArgumentException Exception
     * @throws SQLException Exception
     */
    public void editExtension(Statement statement, String value) throws IllegalArgumentException,SQLException {
        if (value == null ||value.length() == 0) {
            throw new IllegalArgumentException("Extension muss eingegeben werden!");
        }

        if (value.length() < 2) {
            throw new IllegalArgumentException("Extension muss länger als 2 Zeichen sein!");
        }

        type.clearExtensionlist();
        this.setExtension(value);

        String[] array = Extension.get().split(",");

        for (String ex : array) {
            type.addExtension(new Extension(ex));
        }
        String sql = "update Dateiendung set datatype = '" + type.toString() + "', extension = '" + type.Extensions() + "' where datatype = '" + type.toString() + "' ";

        // Datenbankzugriff
        statement.executeUpdate(sql);

    }

    /**
     * Ordner bearbeiten
     * 
     * @param statement Datenbankstatement
     * @param value Value
     * @throws SQLException Exception
     * @throws IllegalArgumentException Exception
     */
    public void editOrdner(Statement statement, String value) throws SQLException, IllegalArgumentException {
        if (value == null ||value.length() == 0) {
            throw new IllegalArgumentException("Pfad muss eingegeben werden!");
        }

        if (value.length() < 2) {
            throw new IllegalArgumentException("Pfad muss länger als 2 Zeichen sein!");
        }

        this.setOrdner(value);
        type.setOrdner(new File(Ordner.getValue()));
        String sql = "update Dateiendung set datatype = '" + type.toString() + "', extension = '" + type.Extensions() + "' where extension = '" + type.Extensions() + "' ";

        // Datenbankzugriff
        statement.executeUpdate(sql);

    }

    /**
     * DataType löschen
     * 
     * @param d DataType
     */
    public void removeDataType(DataType d) {
        type.setExtensionlist(null);
    }
    
    public String getOrdner() {
        return Ordner.get();
    }

    public final void setOrdner(String value) {
        Ordner.set(value);
    }

    public StringProperty OrdnerProperty() {
        return Ordner;
    }

    public String getExtension() {
        return Extension.get();
    }

    public final void setExtension(String value) {
        Extension.set(value);
    }

    public StringProperty extensionProperty() {
        return Extension;
    }
}
