package at.limpidness.juliaset;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("julia.png")));

        stage.setTitle("Julia Set");
        stage.setScene(new Scene(loader.load()));
        stage.getIcons().add(icon);

        stage.show();
    }
}
