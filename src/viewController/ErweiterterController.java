package viewController;

import data.*;
import java.io.*;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.stage.*;
import mover.DataMover;

/**
 * ErweiterterController
 * <p>
 * Tableviewübersicht über alle Dateiendungen.
 * </p>
 *
 * @author Isabella
 */
public class ErweiterterController {

    private Stage stage;
    private final static String VIEWNAME = "Dateitypenwarten.fxml";
    private DataMover mover;
    private Statement statement;
    private ResourceBundle bundle;

    @FXML
    private TextField tfMsg;
    private ObservableList<Dateiendung> list;
    @FXML
    private TableView<Dateiendung> tvWarten;
    @FXML
    private TableColumn<Dateiendung, String> tcZiel;
    @FXML
    private TableColumn<Dateiendung, String> tcEndung;

    public static void show(Stage parentStage, Stage stage, DataMover mover, Statement statement, ResourceBundle bundle) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(ErweiterterController.class.getResource(VIEWNAME), bundle);
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));
            stage.getIcons().add(new Image(ErweiterterController.class.getResourceAsStream("icon.png")));
            stage.setResizable(false);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);

            // Controller ermitteln
            ErweiterterController controller = (ErweiterterController) loader.getController();
            controller.statement = statement;
            controller.bundle = bundle;

            // View initialisieren
            controller.init(stage, mover);

            // Anzeigen
            stage.show();
        } catch (IOException ex) {
            System.err.println(bundle.getString("Fensterladefehler"));
            ex.printStackTrace(System.out);
            System.exit(1);
        } catch (SQLException ex) {
            System.err.println(bundle.getString("Fensterladefehler"));
            ex.printStackTrace(System.out);
            System.exit(1);
        }
    }

    /**
     * Init
     *
     * @param stage
     * @param mover
     * @throws SQLException
     */
    private void init(Stage stage, DataMover mover) throws SQLException {
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
                        try {
                            int selectedIndex = tvWarten.getSelectionModel().getSelectedIndex();
                            DataType d = mover.getDataType(tvWarten.getItems().get(selectedIndex).getOrdner());

                            Dateiendung end = new Dateiendung(d);
                            end.editOrdner(statement, file);
                            end.setExtension(tvWarten.getItems().get(selectedIndex).getExtension());
                            tvWarten.getItems().set(selectedIndex, end);

                            showSuccessMessage(bundle.getString("Dateitypgespeichert"));
                        } catch (SQLException ex) {
                            showErrorMessage(bundle.getString("Fehler"));
                        } catch (IllegalArgumentException ex) {
                            showErrorMessage(ex.getMessage());
                        }
                    }
                }
            });
            return cell;
        });

        tcEndung.setOnEditCommit((TableColumn.CellEditEvent<Dateiendung, String> event) -> {
            try {
                ((Dateiendung) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())).editExtension(statement, event.getNewValue());
                showSuccessMessage(bundle.getString("Dateitypgespeichert"));
            } catch (SQLException ex) {
                showErrorMessage(bundle.getString("Fehler"));
            } catch (IllegalArgumentException ex) {
                showErrorMessage(ex.getMessage());
            }
        });

        // Benutzernachricht
        showSuccessMessage(bundle.getString("alleDaten"));
    }

    private String chooseFile() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(bundle.getString("DataOrganizer"));
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            return selectedDirectory.toString();
        }
        return null;
    }

    /**
     * Dateiendungen löschen
     * <p>
     * Ausgewählte Dateiendung löschen.
     * </p>
     *
     * @throws SQLException
     */
    private void handleDeleteDateiendung() throws SQLException {
        int selectedIndex = tvWarten.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String sql = "delete from Dateiendung where datatype = '" + tvWarten.getItems().get(selectedIndex).getOrdner() + "' ";

            // Datenbankzugriff
            statement.executeUpdate(sql);
            mover.removeDataType(mover.getDataType(tvWarten.getItems().get(selectedIndex).getOrdner()));

            tvWarten.getItems().remove(selectedIndex);

            showSuccessMessage(bundle.getString("Datatypgeloescht"));
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle(bundle.getString("keineZeile"));
            alert.setContentText(bundle.getString("Zeileauswaehlen"));

            alert.showAndWait();
        }
    }

    @FXML
    private void deleteDateiendung(ActionEvent event) throws SQLException {
        handleDeleteDateiendung();
    }

    @FXML
    private void addDateiendung(ActionEvent event) {
        AddDateitypController.show(stage, null, mover, this, statement, bundle);
    }

    public void addList(Dateiendung end) {
        list.add(end);
    }

    public void updateList() {
        list = FXCollections.observableArrayList();

        for (DataType e : mover.getDatatype()) {
            Dateiendung end = new Dateiendung(e);
            end.setExtension(e.Extensions());
            end.setOrdner(e.getOrdner().toString());
            list.add(end);
        }

        tvWarten.setItems(list);
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
