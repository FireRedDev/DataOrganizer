package utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import mover.DataMover;

/**
 * C.G
 */
public class ActionListenerVar implements ActionListener {

    private DataMover mover;

    public ActionListenerVar(DataMover page) {
        this.mover = page;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            mover.sort();
        } catch (IOException ex) {
            System.out.println("Fehler beim sortieren!");
        }

    }

    public DataMover getMover() {
        return mover;
    }

    public void setMover(DataMover mover) {
        this.mover = mover;
    }

}
