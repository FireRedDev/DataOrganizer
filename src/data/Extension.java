package data;

/**
 * Extension
 * <br>
 * Dateiendung als String speichern und merken zu welchen Datentyp die
 * Dateiendung gehört.
 */
public final class Extension {

    private String extension;
    private DataType dataType;

    public Extension(String Extension) {
        setExtension(Extension);
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setDataTyp(DataType neu) {
        if (dataType != neu) {
            if (dataType != null) {
                dataType.removeExtension(this);
            }
            dataType = neu;

            if (neu != null) {
                neu.addExtension(this);
            }
        }
    }

    @Override
    public String toString() {
        return extension;
    }

}
