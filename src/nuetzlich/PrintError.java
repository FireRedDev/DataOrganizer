package nuetzlich;

import java.io.PrintStream;
import javafx.scene.control.TextField;

public abstract class PrintError {

    public static void create(TextField textfield) {
        PrintStream stream = new PrintStream(System.err) {
            @Override
            public void print(String text) {
                textfield.setText(text + "\n");
                textfield.setStyle("-fx-text-inner-color: red;");
            }
        };
        System.setOut(stream);
    }
}
