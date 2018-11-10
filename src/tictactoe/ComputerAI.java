package tictactoe;


import javafx.scene.control.Button;

/**
 * This class handles the functionality of the computer.
 */
public class ComputerAI {
    /**
     * This will be a list of all the game buttons.
     */
    private Button[] buttons;

    /**
     * This is all the indexes for the rows in the game.
     */
    private final int[][] ROWS = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};

    /**
     * This is all the indexes for the columns in the game
     */
    private final int[][] COLUMNS = {{0, 3, 6}, {1, 4, 7}, {3, 5, 8}};

    /**
     * This is all the indexes for the diagonals in the game.
     */
    private final int[][] DIAGONALS = {{0, 4, 8}, {2, 4, 6}};

    /**
     * AI constructor.
     *
     * @param buttons - List of buttons so the ai can do his turn.
     */
    public ComputerAI(Button[] buttons) {
        this.buttons = buttons;
    }

    /**
     * This is called by the game controller to do the computer's turn .
     */
    public void doGo() {
        // This is used to return early if a move was found
        boolean shouldReturn;

        // Look for winning move
        shouldReturn = findMove("O");
        if(shouldReturn) return;

        // Look for blocking move
        shouldReturn = findMove("X");
        if(shouldReturn) return;

        // Place in the center is it can't place there randomly place
        placeCenterOrRandomly();
    }

    /**
     * This finds move for either winning or blocking the player.
     *
     * @param player_char This will either be "X" or "O" depending whether the Ai is trying to find a winning move or a blocking move.
     * @return Returns true is a move was executed.
     */
    private boolean findMove(String player_char) {
        boolean shouldReturn;

        shouldReturn = checkBoard(ROWS, player_char);
        if(shouldReturn) return true;

        shouldReturn = checkBoard(COLUMNS, player_char);
        if(shouldReturn) return true;

        shouldReturn = checkBoard(DIAGONALS, player_char);

        return shouldReturn;
    }

    /**
     * This is called is neither a winning or blocking move was found. <br>
     * This will try and go in the center of the board. <br>
     * If it can't then it will go randomly.
     */
    private void placeCenterOrRandomly() {
        // Try and place in the center of the board
        if (!buttons[4].getText().equals("X") && !buttons[4].getText().equals("O")) {
            executeMoveIfPossible(4);
            return;
        }

        // Random number between 0 and 8
        int random_index = (int)(Math.random() * 9);

        boolean canPlaceThere = !buttons[random_index].getText().equals("X") && !buttons[random_index].getText().equals("O");

        // Keep looping if the random index already has a character
        while (!canPlaceThere) {
            // Random number between 0 and 8
            random_index = (int)(Math.random() * 9);

            canPlaceThere = !buttons[random_index].getText().equals("X") && !buttons[random_index].getText().equals("O");
        }

        executeMoveIfPossible(random_index);
    }

    /**
     * This checks the board for 2 or more of a certain player ("X" or "O").
     *
     * @param places This will either be the ROWS, COLUMNS or DIAGONALS.
     * @param player_char This will be the player char to look for 2 or more with ("X" or "O").
     * @return Returns the 'executeMoveIfPossible' method which returns true if the move was executed or false if the position has already been taken.
     */
    private boolean checkBoard(int[][] places, String player_char) {
        for (int[] indexes : places) {
            // All the if statements check the 3 possible places for 2 characters to be
            // Either 0,1 or 1,2 or 0,2
            if (buttons[indexes[0]].getText().equals(player_char) && buttons[indexes[1]].getText().equals(player_char)) {
                return executeMoveIfPossible(indexes[2]);
            } else if(buttons[indexes[1]].getText().equals(player_char) && buttons[indexes[2]].getText().equals(player_char)) {
                return executeMoveIfPossible(indexes[0]);
            } else if (buttons[indexes[0]].getText().equals(player_char) && buttons[indexes[2]].getText().equals(player_char)) {
                return executeMoveIfPossible(indexes[1]);
            }
        }

        // Return false if no move was found
        return false;
    }

    /**
     * This tries and executes a move if possible.
     * @param index Button index to attempt to make a move in.
     * @return Returns true if a move was successfully executed or false if the position has already been taken.
     */
    private boolean executeMoveIfPossible(int index) {
        // Check if this current position has already been taken
        if(buttons[index].getText().equals("X") || buttons[index].getText().equals("O"))
            return false;

        // If the current position is available the AI will make a move their then return true
        buttons[index].setText("O");
        buttons[index].getStyleClass().add("o");
        return true;
    }
}
