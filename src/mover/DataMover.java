package mover;

import data.DataType;
import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
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

    private File Ordner;
    private List<DataType> datatype;
    GeneralController controller;

    public DataMover(DataType datatyp, GeneralController controller) {
//        File dir = new File("ZusortierendeDateien");
        
        datatype = new LinkedList<>();

        //Ordner Erstellen
//        dir.mkdir();
//        setOrdner(dir);
        datatype.add(datatyp);
        this.controller = controller;
    }

    public void order() {
        for (DataType typ : datatype) {
            typ.order();
        }
    }

    /**
     * sortieren nach Dateityp
     *
     * @throws IOException
     */
    public void sort() throws IOException {
        File[] directoryListing = new File(controller.getAusProp()).listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                for (DataType type : datatype) {
                    for (int num = 0; num < type.getExtensionlist().size(); num++) {
                        if (FilenameUtils.getExtension(child.getName()).equals(type.getExtensionlist().get(num).getExtension())) {
                            try {
                                FileUtils.moveFileToDirectory(child, new File(controller.getZielProp() + "\\" + type.getOrdner()), true);
                            } catch (Exception ex) {
                                System.out.println("File exists. Continuing");
                            }
                        }
                    }
                }
            }
//            Platform.runLater(() -> {
//                Alert alConfirm = new Alert(Alert.AlertType.INFORMATION);
//                alConfirm.setHeaderText("Dateien wurden sortiert!");
//                alConfirm.show();
//            });
            System.out.println("Dateien sortiert!");
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

    public File getOrdner() {
        return Ordner;
    }

    private void setOrdner(File Ordner) {
        this.Ordner = Ordner;
    }

    public void addDataType(DataType typ) {
        datatype.add(typ);
    }

}
