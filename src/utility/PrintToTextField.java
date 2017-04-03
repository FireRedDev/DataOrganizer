package utility;

import java.io.PrintStream;
import javafx.scene.control.TextField;

/**
 * PrintToTextField
 * <p>
 * Gibt Ausgaben anstelle auf der Kommandline im Messagefeld aus.
 * </p>
 * @author Isabella
 */
public abstract class PrintToTextField {

    public static void create(TextField textfield) {
        PrintStream stream = new PrintStream(System.out) {
            @Override
            public void print(String text) {
                textfield.setText(text + "\n");
                textfield.setStyle("-fx-text-inner-color: green;");
            }
        };
        System.setOut(stream);
    }
}
