package viewController;

import data.*;
import java.io.*;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Tableviewübersicht über alle RegexRegeln.
 * </p>
 *
 * @author Isabella
 */
public class ErweiterterControllerRegex {

    private Stage stage;
    private final static String VIEWNAME = "Regexwarten.fxml";
    private DataMover mover;
    private Statement statement;
    private ResourceBundle bundle;

    @FXML
    private TextField tfMsg;
    private ObservableList<RegexRule> list;
    @FXML
    private TableView<RegexRule> tvWarten;
    @FXML
    private TableColumn<RegexRule, String> tcZiel;
    @FXML
    private TableColumn<RegexRule, String> tcFilter;

    public static void show(Stage parentStage, Stage stage, DataMover mover, Statement statement, ResourceBundle bundle) {
        try {
            // View und Controller erstellen
            FXMLLoader loader = new FXMLLoader(ErweiterterControllerRegex.class.getResource(VIEWNAME), bundle);
            Parent root = (Parent) loader.load();

            // Scene erstellen
            Scene scene = new Scene(root);

            // Stage: Entweder übergebene Stage verwenden (Primary Stage) oder neue erzeugen
            if (stage == null) {
                stage = new Stage();
            }
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.getIcons().add(new Image(ErweiterterControllerRegex.class.getResourceAsStream("icon.png")));

            // Controller ermitteln
            ErweiterterControllerRegex controller = (ErweiterterControllerRegex) loader.getController();
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

        for (RegexRule e : mover.getRegexrules()) {

            list.add(e);
        }

        tvWarten.setItems(list);

        tcZiel.setCellValueFactory(new PropertyValueFactory<>("Ordner"));
        tcFilter.setCellValueFactory(new PropertyValueFactory<>("Regex"));

        // Spalten konfigurieren
        tcZiel.setSortType(TableColumn.SortType.ASCENDING);

        // Spalten editierbar machen
        tcFilter.setCellFactory(TextFieldTableCell.<RegexRule>forTableColumn());

        tcZiel.setCellFactory(tc -> {
            TableCell<RegexRule, String> cell = new TableCell<RegexRule, String>() {
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

                            RegexRule end = mover.getRegexRule(list.get(selectedIndex).getRegex());
                            end.setOrdner(file);

                            end.editOrdner(statement);
                            end.setRegex(tvWarten.getItems().get(selectedIndex).getRegex());
                            tvWarten.getItems().set(selectedIndex, end);

                            showSuccessMessage(bundle.getString("OKRegex"));
                        } catch (SQLException ex) {
                            Logger.getLogger(ErweiterterControllerRegex.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            return cell;
        });

        tcFilter.setOnEditCommit((TableColumn.CellEditEvent<RegexRule, String> event) -> {
            try {
                ((RegexRule) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())).setRegex(event.getNewValue());
                ((RegexRule) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())).editRegex(statement);
                showSuccessMessage(bundle.getString("OKRegex"));
            } catch (SQLException ex) {
                Logger.getLogger(ErweiterterControllerRegex.class.getName()).log(Level.SEVERE, null, ex);
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
    private void handleDeleteRegex() throws SQLException {
        int selectedIndex = tvWarten.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String sql = "delete from regexrules where ordner = '" + tvWarten.getItems().get(selectedIndex).getOrdner() + "' ";

            // Datenbankzugriff
            statement.executeUpdate(sql);
            mover.removeRegexRule(mover.getRegexRule(tvWarten.getItems().get(selectedIndex).getOrdner()));

            tvWarten.getItems().remove(selectedIndex);

            showSuccessMessage(bundle.getString("Regexgeloescht"));
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle(bundle.getString("keineZeile"));
            alert.setContentText(bundle.getString("Zeileauswaehlen"));

            alert.showAndWait();
        }
    }

    @FXML
    private void deleteRegex(ActionEvent event) throws SQLException {
        handleDeleteRegex();
    }

    @FXML
    private void addRegex(ActionEvent event) {
        AddRegexController.show(stage, null, mover, this, statement, bundle);
    }

    public void addList(RegexRule end) {
        list.add(end);
    }

    public void updateList() {
        list = FXCollections.observableArrayList();

        for (RegexRule e : mover.getRegexrules()) {
            list.add(e);
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
