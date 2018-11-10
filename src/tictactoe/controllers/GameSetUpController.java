package tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is the controller for setting up the game.
 */
public class GameSetUpController {
    /**
     * Main game controller, needed to return set names.
     */
    private TicTacToeController gameController;

    /**
     * Main stage so we can close this window.
     */
    private Stage stage;

    /**
     * Used to determines whether player 1 or 2's name is being inputted.
     */
    private boolean inputtingPlayerOnesName = true;

    /**
     * This is store the set names.
     */
    private String[] names = new String[2];

    /**
     * Label to inform the user which player name to input.
     */
    @FXML
    private Label label;

    /**
     * This is the actual text input field used to get the inputted name.
     */
    @FXML
    private TextField nameInput;

    @FXML
    private Button playComputerButton;

    /**
     * Sets the game controller.
     *
     * @param gameController TicTacToeController instance.
     */
    void setGameController(TicTacToeController gameController) {
        this.gameController = gameController;
    }

    /**
     * Sets the stage.
     * @param stage Stage instance.
     */
    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Called when the OK button is clicked
     */
    @FXML
    public void okButtonClick() {
        // Don't allow empty input
        if(nameInput.getText().equals("")) return;

        // If true the user has just inputted the first player's name
        // Else the user has inputted the second player's name therefore we should give them to the game controller and exit the set up window
        if (inputtingPlayerOnesName) {
            names[0] = nameInput.getText();
            nameInput.clear();
            label.setText("Enter Player 2's Name (O)");
            inputtingPlayerOnesName = false;
            playComputerButton.setDisable(false);
        } else {
            names[1] = nameInput.getText();
            gameController.setNames(names);
            stage.close();
            inputtingPlayerOnesName = true;
        }
    }

    /**
     * This sets the game up in one player mode against the computer
     */
    @FXML
    public void playComputerClick() {
        gameController.setNames(names);
        stage.close();
    }

    /**
     * Called if the enter key is pressed
     */
    @FXML
    public void onEnter() {
        okButtonClick();
    }
}
