package data;

import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mover.DataMover;

/**
 * C.G
 */
public class RegexRule {

    private final StringProperty Regex = new SimpleStringProperty();
    private final StringProperty Ordner = new SimpleStringProperty();
    private DataMover mover;

    public RegexRule(String ordner, String rule) {
        setOrdner(ordner);
        setRegex(rule);
    }

    public RegexRule() {
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

    public void editRegex(Statement statement) throws SQLException {
        if (Regex.get() == null || Regex.get().length() == 0) {
            throw new IllegalArgumentException("Extension muss eingegeben werden!");
        }

        if (Regex.get().length() < 2) {
            throw new IllegalArgumentException("Extension muss länger als 2 Zeichen sein!");
        }
        this.setRegex(Regex.get());

        String sql = "update regexrules set Ordner = '" + Ordner.getValue() + "', regex = '" + Regex.getValue() + "' where Ordner = '" + Ordner.getValue() + "' ";

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

//        setOrdner(Ordner.getValue());
        String sql = "update regexrules set Ordner = '" + Ordner.getValue() + "', regex = '" + Regex.getValue() + "' where regex = '" + Regex.getValue() + "' ";

        // Datenbankzugriff
        statement.executeUpdate(sql);

    }

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

}
