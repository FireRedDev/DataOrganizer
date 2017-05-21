package utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * ActionListener, ruft sortierfunktion eines DataMover auf.
 */
public class ActionListenerVar implements ActionListener {

    private Stage s;

    public ActionListenerVar(Stage s) {
        this.s = s;
    }

    /**
     * Sorts.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(() -> {
            s.show();
        });

    }

}
