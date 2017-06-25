package RegexViewController;

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

    /**
     * Anzeige der View.
     * <br>
     * Diese Methode erstellt eine Instanz der View und dieses Controllers
     * (FXML-Loader) und richtet alles (also vor allem den Controller) so weit
     * ein, dass es angezeigt werden kann.
     *
     * @param parentStage Stage des Aufrufenden Controllers
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @param mover Mover
     * @param statement Datenbankverbindung
     * @param bundle ResourceBundle, wird benötigt für die Internationalisierung
     */
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
            stage.initStyle(StageStyle.UNIFIED);
            stage.setScene(scene);
            stage.setTitle(bundle.getString("DataOrganizer"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.getIcons().add(new Image(ErweiterterControllerRegex.class.getResourceAsStream("../icon.png")));
            stage.setResizable(false);

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
     * Initialisieren.
     * <br>
     * Diese Methode wird nur von show() verwendet, um den Controller zu
     * initialisieren. Das umfasst u.a. die Konfiguration der Controls, die
     * Verbindung der Controls mit den Model-Feldern, etc.
     *
     * @param stage Stage, in der die View angezeigt werden soll; null, wenn
     * neue erstellt werden soll.
     * @param mover DataMover
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
                    int selectedIndex = tvWarten.getSelectionModel().getSelectedIndex();
                    DirectoryChooser chooser = new DirectoryChooser();
                    new File(tvWarten.getItems().get(selectedIndex).getOrdner()).mkdir();
                    String file = chooseFile(tvWarten.getItems().get(selectedIndex).getOrdner());
                    if (file != null) {
                        try {
                            RegexRule end = mover.getRegexRule(list.get(selectedIndex).getRegex());
                            end.editOrdner(statement, file);
                            end.setRegex(tvWarten.getItems().get(selectedIndex).getRegex());
                            tvWarten.getItems().set(selectedIndex, end);

                            showSuccessMessage(bundle.getString("OKRegex"));
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

        // Änderungen sichern
        tcFilter.setOnEditCommit((TableColumn.CellEditEvent<RegexRule, String> event) -> {
            try {
                ((RegexRule) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())).editRegex(statement, event.getNewValue());
                showSuccessMessage(bundle.getString("OKRegex"));
            } catch (SQLException ex) {
                showErrorMessage(bundle.getString("Fehler"));
            } catch (IllegalArgumentException ex) {
                showErrorMessage(ex.getMessage());
            }
        });

        // Benutzernachricht
        showSuccessMessage(bundle.getString("alleDaten"));
    }

    /**
     * Ordner auswählen
     * <br>
     * Ordner mithilfe des Directory-Chosser auswählen.
     *
     * @param value Ausgangsordner
     * @return Ordnerpfad
     */
    private String chooseFile(String value) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(bundle.getString("DataOrganizer"));
        chooser.setInitialDirectory(new File(value));
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
            mover.removeRegexRule(tvWarten.getItems().get(selectedIndex).getOrdner());

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

    /**
     * Regex-löschen
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void deleteRegex(ActionEvent event) throws SQLException {
        handleDeleteRegex();
    }

    /**
     * Regex hinzufügen
     *
     * @param event
     */
    @FXML
    private void addRegex(ActionEvent event) {
        AddRegexController.show(stage, null, mover, this, statement, bundle);
    }

    /**
     * in Liste hinzufügen
     *
     * @param end Regexrule
     */
    public void addList(RegexRule end) {
        list.add(end);
    }

    /**
     * Liste updaten
     */
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