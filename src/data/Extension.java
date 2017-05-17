package data;

/**
 * Extension
 * <p>
 * Dateiendung als String speichern und merken zu welchen Datentyp die
 * Dateiendung gehört.
 * </p>
 */
public final class Extension {

    private String extension;
    private DataType dataType;

    public Extension(String Extension) {
        setExtension(Extension);
    }

    /**
     * Get Extension
     *
     * @return extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Set the extension, jpg is possible
     *
     * @param extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Set DataTyp
     *
     * @param neu
     */
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

    /**
     * ToString
     *
     * @return Extension
     */
    @Override
    public String toString() {
        return extension;
    }

}
