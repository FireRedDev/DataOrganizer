package data;

/**
 * Extension
 * <p>
 * Dateiendung als String speichern und merken zu welchen Datentyp die
 * Dateiendung gehört.
 * </p>
 */
public class Extension {

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
        if (neu != dataType) {
            if (dataType != null) {
                dataType.removeExtension(this);
            }
            dataType = neu;
            if (neu != null) {
                neu.addExtension(this);
            }
        }
    }

}
