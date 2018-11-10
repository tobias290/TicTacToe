package tictactoe;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application class which also defines the entry point.
 */
public class TicTacToe extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/tictactoe.fxml"));

        primaryStage.setTitle("Noughts and Crosses");
        primaryStage.getIcons().add(new Image(getClass().getResource("../static/icon.png").toExternalForm()));
        primaryStage.setScene(new Scene(root, 450, 650));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
