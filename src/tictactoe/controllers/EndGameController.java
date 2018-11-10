package tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the end game window.
 */
public class EndGameController {
    /**
     * Label which will display who has won.
     */
    @FXML
    private Label winnerTextLabel;

    /**
     * Label to display who will start the next round.
     */
    @FXML
    private Label starterTextLabel;

    /**
     * Event listener for when the "Continue" button is pressed.
     */
    private EventHandler<ActionEvent> onContinueListener;

    /**
     * Event listener for when the "New Game" button is pressed.
     */
    private EventHandler<ActionEvent> onNewGameListener;

    /**
     * Sets the onContinue listener.
     * @param onContinueListener Listener instance.
     */
    void setOnContinueActionListener(EventHandler<ActionEvent> onContinueListener) {
        this.onContinueListener = onContinueListener;
    }

    /**
     * Sets the onNewGame listener.
     * @param onNewGameListener Listener instance.
     */
    void setOnNewGameListener(EventHandler<ActionEvent> onNewGameListener) {
        this.onNewGameListener = onNewGameListener;
    }

    /**
     * Sets display labels.
     *
     * @param winnerText String which says who has won.
     * @param starterText String which says who will start the next round.
     */
    @FXML
    void setData(String winnerText, String starterText) {
        winnerTextLabel.setText(winnerText);
        starterTextLabel.setText(starterText);
    }

    /**
     * Called if the "Continue" button is pressed.
     * @param e Action event which information on the button click.
     */
    @FXML
    public void continueButtonClick(ActionEvent e) {
        // Closes this scene
        ((Stage)((Button)e.getSource()).getScene().getWindow()).close();
        // Callback to the TicTacToeController
        onContinueListener.handle(e);
    }

    /**
     * Called if the "New Game" button is pressed.
     * @param e Action event which information on the button click.
     */
    @FXML
    public void newGameButtonClick(ActionEvent e) {
        // Closes this scene
        ((Stage)((Button)e.getSource()).getScene().getWindow()).close();
        // Callback to the TicTacToeController
        onNewGameListener.handle(e);
    }
}
