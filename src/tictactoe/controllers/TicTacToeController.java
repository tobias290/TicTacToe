package tictactoe.controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tictactoe.ComputerAI;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Controller for the game.
 */
public class TicTacToeController {
    /**
     * Represents the grip panel where the 9 tic tac toe buttons are located. <br>
     * This is needed to we can get any/ all of hte 9 nine buttons without defining a separate variable for each one.
     */
    @FXML
    private GridPane grid;

    /**
     * Players 1's display which shows their name and score.
     */
    @FXML
    private Label playerOneDisplay;

    /**
     * Players 2's display which shows their name and score.
     */
    @FXML
    private Label playerTwoDisplay;

    /**
     * Player 1's name.
     */
    private String playerOneName;

    /**
     * Player 2's name.
     */
    private String playerTwoName;

    /**
     * Player 1's score.
     */
    private int playerOneScore;

    /**
     * Player 2's score.
     */
    private int playerTwoScore;

    /**
     * Will either be "x" or "o" or null depending whether the winner was player 1 or player 2 or it was a draw.
     */
    private String winner;

    /**
     * When this is true the user cannot click any buttons of the main game screen. <br>
     * This is true when either the start or end game window is open.
     */
    private boolean gameDisabled = false;

    /**
     * This is used to determine who's turn it is.
     */
    private boolean isPlayerOnesTurn = true;

    /**
     * This is used to know whether there is a second play or if the user is playing against the computer.
     */
    private boolean isPlayingComputer = false;

    /**
     * This is used to know how many goes have been played so we can work out when a draw has occurred.
     */
    private int numberOfGoes = 0;

    /**
     * Instance of the computer AI program
     */
    private ComputerAI ai;

    /**
     * This is similar to a constructor but has access to the FXML file.
     */
    @FXML
    public void initialize() {
        try {
            showSetNamesPopup();
        } catch (IOException e) {
            System.out.println("Error showing pop up: " + e.getMessage());
            e.printStackTrace();
        }

        // This gets all the button in the grid and cast them to the 'Button' class
        Node[] buttons = new Node[9];
        grid.getChildren().toArray(buttons);

        // This sets up the AI
        ai = new ComputerAI(Arrays.copyOf(buttons, buttons.length, Button[].class));
    }

    /**
     * This sets the names returned from the set up window.
     *
     * @param names List of names (player 1 and player 2).
     */
    void setNames(String[] names) {
        playerOneName = names[0];

        // If true the user is playing another player
        // If false the user is playing against the computer
        if(names[1] != null) {
            playerTwoName = names[1];
        } else {
            playerTwoName = "Computer";
            isPlayingComputer = true;
        }
        updateDisplayBoard();
    }

    /**
     * Called when any of the 9 game buttons where clicked.
     *
     * @param e This is the event this is used to get actual button clicked.
     */
    @FXML
    public void buttonClick(ActionEvent e) {
        // Below will only work if it's either players go and the computer is not playing
        // Or if the computer is playing then only work if it is player one's turn
        if(!isPlayingComputer || isPlayerOnesTurn) {
            Button button = (Button) e.getSource();

            // Returns early if the button has already been clicked or the window is disabled
            if (button.getText().equals("X") || button.getText().equals("O") || gameDisabled) return;

            // Sets the button text and gives it a style class as X and O are shown as different colours
            button.setText(isPlayerOnesTurn ? "X" : "O");
            button.getStyleClass().add(isPlayerOnesTurn ? "x" : "o");

            // Increase the number of goes had
            numberOfGoes++;

            // Switch players
            isPlayerOnesTurn = !isPlayerOnesTurn;

            // Check to see if the game is over
            if (isGameOver()) {
                gameDisabled = true;
                gameOver();
                return;
            }
        }

        // If the user is playing the computer this is will do the AI's turn
        if (isPlayingComputer)
            doComputersTurn();
    }

    /**
     * Called if the new game button was clicked.
     */
    @FXML
    public void newGameClick() {
        // Return early is the window is disabled
        if(!gameDisabled)
            reset(true);
    }

    /**
     * This loads the start up pop up which is used to get the player's names.
     *
     * @throws IOException Thrown if the method cannot find the set up fxml file.
     */
    private void showSetNamesPopup() throws IOException {
        // Create a new stage which is a new window
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        // Set the UI
        loader.setLocation(getClass().getResource("../fxml/game-set-up.fxml"));

        Parent root = loader.load();

        // Set stage properties
        stage.setTitle("Set Names");
        stage.getIcons().add(new Image(getClass().getResource("../../static/icon.png").toExternalForm()));
        stage.setScene(new Scene(root, 300, 200));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();

        // Get the game set up controller so necessary variables can be set
        GameSetUpController controller = loader.getController();
        controller.setGameController(this);
        controller.setStage(stage);
    }

    /**
     * This does the computer turn.
     */
    private void doComputersTurn() {
        // Disable to the UI while the computer does their turn
        gameDisabled = true;
        // Do the actual go
        ai.doGo();
        // Re-enable the UI
        gameDisabled = false;

        // Increase the number of turns after the computer has gone
        numberOfGoes++;

        // Check to see if the game is over
        if(isGameOver()) {
            gameDisabled = true;
            gameOver();
        }

        isPlayerOnesTurn = !isPlayerOnesTurn;
    }

    /**
     * This checks to see if the game is over.
     *
     * @return Returns true is the game is over and false it not.
     */
    private boolean isGameOver() {
        // Set all the buttons
        Node[] node_buttons = new Node[9];
        grid.getChildren().toArray(node_buttons);
        Button[] buttons = Arrays.copyOf(node_buttons, node_buttons.length, Button[].class);

        if (numberOfGoes >= 9) return true;

        // Loop twice to check for X and O
        for (String playerChar: new String[]{"X", "O"}) {
            if (
                // Check Horizontally for a match
                (buttons[0].getText().equals(playerChar) && (buttons[1]).getText().equals(playerChar) && (buttons[2]).getText().equals(playerChar)) ||
                (buttons[3].getText().equals(playerChar) && (buttons[4]).getText().equals(playerChar) && (buttons[5]).getText().equals(playerChar)) ||
                (buttons[6]).getText().equals(playerChar) && (buttons[7]).getText().equals(playerChar) && (buttons[8]).getText().equals(playerChar) ||

                // Check vertically for a match
                (buttons[0]).getText().equals(playerChar) && (buttons[3]).getText().equals(playerChar) && (buttons[6]).getText().equals(playerChar) ||
                (buttons[1]).getText().equals(playerChar) && (buttons[4]).getText().equals(playerChar) && (buttons[7]).getText().equals(playerChar) ||
                (buttons[2]).getText().equals(playerChar) && (buttons[5]).getText().equals(playerChar) && (buttons[8]).getText().equals(playerChar) ||

                // Check diagonally for a match
                (buttons[0]).getText().equals(playerChar) && (buttons[4]).getText().equals(playerChar) && (buttons[8]).getText().equals(playerChar) ||
                (buttons[2]).getText().equals(playerChar) && (buttons[4]).getText().equals(playerChar) && (buttons[6]).getText().equals(playerChar)
                ) {

                // Set the winner player so the game knows who has one
                winner = playerChar;

                return true;
            }
        }

        return false;
    }

    /**
     * Called when the game has finished.
     */
    private void gameOver() {
        // Create a new stage which is a new window
        Stage stage = new Stage();

        // Set the winning text
        String winnerText;

        // Checks to see if a player won or if the game was a draw
        if (winner == null)
            winnerText = "Game was a draw";
        else
            winnerText = String.format("%s (%s) wins", (winner.equals("X") ? playerOneName : playerTwoName), winner);

        String starterText = String.format("%s (%s) will start the next game", (isPlayerOnesTurn ? playerOneName : playerTwoName), (isPlayerOnesTurn ? "X" : "O"));

        // If the game wasn't a draw give the winning player a point
        if(winner != null) {
            if (winner.equals("X"))
                playerOneScore++;
            else
                playerTwoScore ++;
        }

        // Update the display so the user can see the changes
        updateDisplayBoard();

        try {
            // Load the end game window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../fxml/end-game.fxml"));

            Parent root = loader.load();
            // Get the controller so we can set listeners for the buttons
            EndGameController endGameController = loader.getController();
            endGameController.setData(winnerText, starterText);
            endGameController.setOnContinueActionListener(event -> reset(false));
            endGameController.setOnNewGameListener(event -> reset(true));

            // Set stage properties
            stage.setTitle("Game Over");
            stage.getIcons().add(new Image(getClass().getResource("../../static/icon.png").toExternalForm()));
            stage.setScene(new Scene(root, 350, 200));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading end game window: " + e.getMessage());
        }
    }

    /**
     * This is called when the game is going to be reset.
     *
     * @param isNewGame This is true if the whole game is being reset (names and scores reset) or false if it just a new round.
     */
    private void reset(boolean isNewGame) {
        // Get all the buttons
        Node[] buttons = new Node[9];
        grid.getChildren().toArray(buttons);

        // Set each button's text to be empty and remove dynamically given CSS classes
        for (Node btn : buttons) {
            ((Button)btn).setText("");
            btn.getStyleClass().remove("x");
            btn.getStyleClass().remove("o");
        }

        // Reset game details
        gameDisabled = false;
        winner = null;
        numberOfGoes = 0;

        // If false then it is just a new round otherwise everything will be reset
        if (isNewGame) {
            isPlayerOnesTurn = true;
            isPlayingComputer = false;

            playerOneName = null;
            playerOneScore = 0;

            playerTwoName = null;
            playerTwoScore = 0;

            try {
                showSetNamesPopup();
            } catch (IOException e) {
                System.out.println("Error showing names popup: " + e.getMessage());
            }
        } else if (isPlayingComputer && isPlayerOnesTurn) {
            doComputersTurn();
            isPlayerOnesTurn = true;
        }

        updateDisplayBoard();
    }

    /**
     * Updates the UI display.
     */
    private void updateDisplayBoard() {
        playerOneDisplay.setText(String.format("%s (X) - %d", playerOneName, playerOneScore));
        playerTwoDisplay.setText(String.format("%s (O) - %d", playerTwoName, playerTwoScore));
    }
}
