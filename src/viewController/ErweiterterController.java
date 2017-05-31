package viewController;

import data.*;
import java.io.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.*;
import javafx.stage.*;
import mover.DataMover;

/**
 *
 * @author Isabella
 */
public class ErweiterterController {

    private Stage stage;
    private final static String VIEWNAME = "Dateitypenwarten.fxml";
    private DataMover mover;

    @FXML
    private TextField tfMsg;
    private ObservableList<Dateiendung> list;
    @FXML
    private TableView<Dateiendung> tvWarten;
    @FXML
    private TableColumn<Dateiendung, String> tcZiel;
    @FXML
    private TableColumn<Dateiendung, String> tcEndung;

    public static void show(Stage parentStage, Stage stage, DataMover mover) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(ErweiterterController.class.getResource(VIEWNAME));
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.setScene(scene);
            stage.setTitle("DataOrganizer");

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);

            // Controller ermitteln
            ErweiterterController controller = (ErweiterterController) loader.getController();

            // View initialisieren
            controller.init(stage, mover);

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            System.err.println("Something wrong with " + VIEWNAME + "!");
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    private void init(Stage stage, DataMover mover) {
        this.mover = mover;
        this.stage = stage;

        list = FXCollections.observableArrayList();

        for (DataType e : mover.getDatatype()) {
            Dateiendung end = new Dateiendung(e);
            end.setExtension(e.Extensions());
            end.setOrdner(e.getOrdner().toString());
            list.add(end);
        }

        tvWarten.setItems(list);

        tcZiel.setCellValueFactory(new PropertyValueFactory<>("Ordner"));
        tcEndung.setCellValueFactory(new PropertyValueFactory<>("Extension"));

        // Spalten konfigurieren
        tcZiel.setSortType(TableColumn.SortType.ASCENDING);

        // Spalten editierbar machen
        tcEndung.setCellFactory(TextFieldTableCell.<Dateiendung>forTableColumn());

        tcZiel.setCellFactory(tc -> {
            TableCell<Dateiendung, String> cell = new TableCell<Dateiendung, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    String userId = cell.getItem();
                    DirectoryChooser chooser = new DirectoryChooser();
                    String file = chooseFile();
                    if (file != null) {
                        int selectedIndex = tvWarten.getSelectionModel().getSelectedIndex();
                        DataType d = mover.getDataType(tvWarten.getItems().get(selectedIndex).getOrdner());

                        Dateiendung end = new Dateiendung(d);
                        end.setOrdner(file);
                        end.editOrdner();
                        end.setExtension(tvWarten.getItems().get(selectedIndex).getExtension());
                        tvWarten.getItems().set(selectedIndex, end);

                        showSuccessMessage("Ok, Dateityp gespeichert!");
                    }
                }
            });
            return cell;
        });

        tcEndung.setOnEditCommit((TableColumn.CellEditEvent<Dateiendung, String> event) -> {
            ((Dateiendung) event.getTableView().getItems().get(
                    event.getTablePosition().getRow())).setExtension(event.getNewValue());
            ((Dateiendung) event.getTableView().getItems().get(
                    event.getTablePosition().getRow())).editExtension();
            showSuccessMessage("Ok, Dateityp gespeichert!");
        });

        // Benutzernachricht
        showSuccessMessage("Ok, alle Daten angezeigt!");
    }

    private String chooseFile() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Data Organizer");
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            return selectedDirectory.toString();
        }
        return null;
    }

    private void handleDeleteDateiendung() {
        int selectedIndex = tvWarten.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {

            mover.removeDataType(mover.getDataType(tvWarten.getItems().get(selectedIndex).getOrdner()));

            tvWarten.getItems().remove(selectedIndex);

            showSuccessMessage("Ok, Dateityp(en) gelöscht!");
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("Keine Zeile ausgewählt!");
            alert.setContentText("Bitte wähle eine Zeile zum löschen aus.");

            alert.showAndWait();
        }
    }

    @FXML
    private void deleteDateiendung(ActionEvent event) {
        handleDeleteDateiendung();
    }

    @FXML
    private void addDateiendung(ActionEvent event) {
        AddDateitypController.show(stage, null, mover, this);
    }

    public void addList(Dateiendung end) {
        list.add(end);
    }

    public void removeList(Dateiendung end) {
        list.remove(end);
    }

    /**
     * Fehlermeldung anzeigen.
     *
     * @param message Nachrichtentext
     */
    public void showErrorMessage(String message) {
        tfMsg.setText(message);
        tfMsg.setStyle("-fx-text-inner-color: red;");
    }

    /**
     * Erfolgsmeldung anzeigen.
     *
     * @param message Nachrichtentext
     */
    public void showSuccessMessage(String message) {
        tfMsg.setText(message);
        tfMsg.setStyle("-fx-text-inner-color: green;");
    }
}
