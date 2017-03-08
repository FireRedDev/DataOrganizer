package benutzeroberflaeche.model;
// Test mit git

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Javadoc bitte schreiben
 * 
 * Isabella
 */
public class Erweiterung {

    private final StringProperty bilderPfad = new SimpleStringProperty();
    private final StringProperty videoPfad = new SimpleStringProperty();
    private final StringProperty audioPfad = new SimpleStringProperty();
    private final StringProperty dokumentPfad = new SimpleStringProperty();

    public String getDokumentPfad() {
        return dokumentPfad.get();
    }

    public void setDokumentPfad(String value) {
        dokumentPfad.set(value);
    }

    public StringProperty dokumentPfadProperty() {
        return dokumentPfad;
    }

    public String getAudioPfad() {
        return audioPfad.get();
    }

    public void setAudioPfad(String value) {
        audioPfad.set(value);
    }

    public StringProperty audioPfadProperty() {
        return audioPfad;
    }

    public String getVideoPfad() {
        return videoPfad.get();
    }

    public void setVideoPfad(String value) {
        videoPfad.set(value);
    }

    public StringProperty videoPfadProperty() {
        return videoPfad;
    }

    public String getBilderPfad() {
        return bilderPfad.get();
    }

    public void setBilderPfad(String value) {
        bilderPfad.set(value);
    }

    public StringProperty bilderPfadProperty() {
        return bilderPfad;
    }
    
}
