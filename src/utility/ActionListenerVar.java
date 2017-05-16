package utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * ActionListener, ruft sortierfunktion eines DataMover auf.
 */
public class ActionListenerVar implements ActionListener {

    private Stage stage;

    public ActionListenerVar(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Anzeigen
        if (!stage.isShowing()) {
            Platform.runLater(() -> {
                stage.show();
            });
        } else {
            Platform.runLater(() -> {
                Alert alConfirm = new Alert(Alert.AlertType.INFORMATION);
                alConfirm.setHeaderText("Fenster ist schon geöffnet!");
                alConfirm.show();
            });
        }

    }

}
