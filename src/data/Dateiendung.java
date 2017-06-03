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

    public Dateiendung(DataType type) {
        this.type = type;
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

    public void editExtension(Statement statement) throws SQLException {
        if (Extension.get() == null || Extension.get().length() == 0) {
            throw new IllegalArgumentException("Extension muss eingegeben werden!");
        }

        if (Extension.get().length() < 2) {
            throw new IllegalArgumentException("Extension muss länger als 2 Zeichen sein!");
        }

        type.clearExtensionlist();

        String[] array = Extension.get().split(",");

        for (String ex : array) {
            type.addExtension(new Extension(ex));
        }
        String sql = "update Dateiendung set datatype = '" + type.toString() + "', extension = '" + type.Extensions() + "' where datatype = '" + type.toString() + "' ";

        // Datenbankzugriff
        statement.executeUpdate(sql);

    }

    public void editOrdner(Statement statement) throws SQLException {
        if (Ordner.get() == null || Ordner.get().length() == 0) {
            throw new IllegalArgumentException("Pfad muss eingegeben werden!");
        }

        if (Ordner.get().length() < 2) {
            throw new IllegalArgumentException("Pfad muss länger als 2 Zeichen sein!");
        }

        type.setOrdner(new File(Ordner.getValue()));
        String sql = "update Dateiendung set datatype = '" + type.toString() + "', extension = '" + type.Extensions() + "' where extension = '" + type.Extensions() + "' ";

        // Datenbankzugriff
        statement.executeUpdate(sql);

    }

    public void removeDataType(DataType d) {
        type.setExtensionlist(null);
    }
}
