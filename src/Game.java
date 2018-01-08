import java.util.ArrayList;

/**
 * The class performs the game's logic.
 * Contains static functions to use on given game boards.
 */
public class Game {

    //Max searching depth in the Minimax algorithm.
    private static final int MAX_DEPTH  = 3;

    //Board size.
    private static final int BOARD_SIZE = 5;

    /**
     * Private constructor.
     */
    private Game() {

    }

    /**
     * Plays the game and finds the winning player.
     * @param board game board
     * @return winning color
     */
    public static char play(char[][] board) {

        Node    node         = new Node(board, BOARD_SIZE, 'B');
        boolean isMaximizing = true;

        //Run until a solution is found.
        while (!node.isTerminal()) {

            node = minimax(node, MAX_DEPTH, isMaximizing);

            //System.out.println("Is maximizing: " + isMaximizing);
            //printBoard(node.getBoard());
            //System.out.println();

            isMaximizing = !isMaximizing;
        }

        node.countColors();

        //Check who is the winner.
        if (node.getColor() == 'B') {

            if (isWon(node, true)) {
                return 'B';
            } else {
                return 'W';
            }
        } else {

            if (isWon(node, false)) {

                return 'W';
            } else {
                return 'B';
            }
        }
    }

    /**
     * Performs the Minimax algorithm.
     * @param node starting node
     * @param depth maximum search depth
     * @param maximizingPlayer is a maximizing player
     * @return next node
     */
    private static Node minimax(Node node, int depth, boolean maximizingPlayer) {

        //Check if can't search any further.
        if (depth == 0 || node.isTerminal()) {

            node.setCost(heuristic(node));
            return node;
        }

        //Check is a maximizing player.
        if (maximizingPlayer) {

            int             bestValue = Integer.MIN_VALUE;
            Node            bestChild = null;
            ArrayList<Node> children  = node.getSuccessors(true);

            for (Node child : children) {

                //Make a recursive call to Minimax with depth - 1 as a maximizing player.
                Node v = minimax(child, depth - 1, false);

                if(bestChild == null){
                    bestChild = v;
                }

                //Check if received value is greater than the current bestValue.
                if (v.getCost() > bestValue) {
                    bestValue = v.getCost();
                    bestChild = v;
                }
            }

            return bestChild;
        } else {

            int             bestValue = Integer.MAX_VALUE;
            Node            bestChild = null;
            ArrayList<Node> children  = node.getSuccessors(false);

            for (Node child : children) {

                //Make a recursive call to Minimax with depth - 1 as a minimizing player.
                Node v = minimax(child, depth - 1, true);

                if(bestChild == null){
                    bestChild = v;
                }

                //Check if received value is greater than the current bestValue.
                if (v.getCost() < bestValue) {
                    bestValue = v.getCost();
                    bestChild = v;
                }
            }

            return bestChild;
        }
    }

    /**
     * Returns the heuristic value of a given node.
     * @param node node
     * @return heuristic value
     */
    private static int heuristic(Node node) {

        //Count all the cells colors in advance.
        node.countColors();

        //Check if node is terminal
        if (node.isTerminal()) {
            //Check if the game ended with a draw.
            if (isDraw(node)) {
                return 0;
            }

            //Check if the player won the game.
            if (isWon(node, true)) {
                return Integer.MAX_VALUE;
            }

            return Integer.MIN_VALUE;
        }

        int blackCounter     = node.getBlackCounter();
        int blackEdgeCounter = node.getBlackEdgeCounter();
        int whiteCounter     = node.getWhiteCounter();
        int whiteEdgeCounter = node.getWhiteEdgeCounter();

        //Calculate the maximum player heuristic value.
        int heuristicValue = (blackCounter - whiteCounter) + (blackEdgeCounter - whiteEdgeCounter);

        return heuristicValue;
    }

    /**
     * Checks if there is a draw in the game.
     * @param node node
     * @return is a draw
     */
    private static boolean isDraw(Node node) {

        int blackCounter = node.getBlackCounter();
        int whiteCounter = node.getWhiteCounter();
        int emptyCounter = node.getEmptyCounter();

        return (blackCounter == whiteCounter) && (emptyCounter == 0);

    }

    /**
     * Checks if there is a winner in the game.
     * @param node node
     * @param isMaximizing is a maximizing player
     * @return is a win
     */
    private static boolean isWon(Node node, boolean isMaximizing) {

        int blackCounter = node.getBlackCounter();
        int whiteCounter = node.getWhiteCounter();

        if (isMaximizing) {

            return blackCounter > whiteCounter;
        }

        return blackCounter < whiteCounter;
    }

    /**
     * Checks if the move is legal according to the game's rules.
     *
     * @param row       movement row
     * @param column    movement column
     * @param board     game board
     * @param boardSize board size
     * @return boolean is the move legal
     */
    public static boolean isLegalMove(int row, int column, char[][] board, int boardSize) {

        //Check if there isn't already a game tool at the desired position on the board.
        if (board[row][column] != 'E') {

            return false;
        }

        //Check if cell has a right neighbour.
        if ((column < boardSize - 1) && board[row][column + 1] != 'E') {
            return true;
        }

        //Check if cell has a bottom-right neighbour.
        if (row < boardSize - 1 && column < boardSize - 1 && board[row + 1][column + 1] != 'E') {

            return true;
        }

        //Check if cell has a bottom neighbour.
        if (row < boardSize - 1 && board[row + 1][column] != 'E') {

            return true;
        }

        //Check if cell has a bottom-left neighbour.
        if (row < boardSize - 1 && column > 0 && board[row + 1][column - 1] != 'E') {

            return true;
        }

        //Check if cell has a left neighbour.
        if (column > 0 && board[row][column - 1] != 'E') {

            return true;
        }

        //Check if cell has a top-left neighbour.
        if (row > 0 && column > 0 && board[row - 1][column - 1] != 'E') {

            return true;
        }

        //Check if cell has a top neighbour.
        if (row > 0 && board[row - 1][column] != 'E') {

            return true;
        }

        //Check if cell has a top-right neighbour.
        if (row > 0 && column < boardSize - 1 && board[row - 1][column + 1] != 'E') {

            return true;
        }

        return false;
    }

    /**
     * Fills the cells around the given cell, if possible.
     *
     * @param row       row number
     * @param column    column number
     * @param value     player color
     * @param board     game board
     * @param boardSize board size
     * @return game board after changes
     */
    public static char[][] performMovement(int row, int column, char value, char[][] board, int boardSize) {

        char[][] workBoard = copyBoard(board, boardSize);
        workBoard[row][column] = value;

        //Try replacing the pieces in all the possible direction to the current players value.
        workBoard = fillRight(row, column, value, workBoard, boardSize);
        workBoard = fillBottomRight(row, column, value, workBoard, boardSize);
        workBoard = fillBottom(row, column, value, workBoard, boardSize);
        workBoard = fillBottomLeft(row, column, value, workBoard, boardSize);
        workBoard = fillLeft(row, column, value, workBoard, boardSize);
        workBoard = fillTopLeft(row, column, value, workBoard, boardSize);
        workBoard = fillTop(row, column, value, workBoard, boardSize);
        workBoard = fillTopRight(row, column, value, workBoard, boardSize);

        return workBoard;
    }

    /**
     * Creates a copy of the given board.
     * @param board game board
     * @param boardSize board size
     * @return copied game board
     */
    private static char[][] copyBoard(char[][] board, int boardSize) {

        char[][] copiedArray = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                copiedArray[i][j] = board[i][j];
            }
        }

        return copiedArray;
    }

    /**
     * Fills the cells in the right direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillRight(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (column == boardSize - 1) {
            return board;
        }

        //Create a working board.
        char[][] workBoard = copyBoard(board, boardSize);

        //Fill the working board in the current direction.
        for (int i = column + 1; i < boardSize; i++) {

            //If current cell is empty, fill it with player value.
            if (workBoard[row][i] != value) {
                workBoard[row][i] = value;
            } else {

                return workBoard;
            }
        }

        //No changes were made at the current direction.
        return board;
    }

    /**
     * Fills the cells in the bottom-right direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillBottomRight(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (column == boardSize - 1 || row == boardSize - 1) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = row + 1, j = column + 1; i < boardSize && j < boardSize; i++, j++) {

            if (workBoard[i][j] != value) {
                workBoard[i][j] = value;
            } else {

                return workBoard;
            }
        }

        return board;
    }

    /**
     * Fills the cells in the bottom direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillBottom(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (row == boardSize - 1) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = row + 1; i < boardSize; i++) {

            if (workBoard[i][column] != value) {
                workBoard[i][column] = value;
            } else {

                return workBoard;
            }
        }

        return board;
    }

    /**
     * Fills the cells in the bottom left direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillBottomLeft(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (row == boardSize - 1 || column == 0) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = row + 1, j = column - 1; i < boardSize && j >= 0; i++, j--) {

            if (workBoard[i][j] != value) {
                workBoard[i][j] = value;
            } else {

                return workBoard;
            }
        }

        return board;
    }

    /**
     * Fills the cells in the left direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillLeft(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (column == 0) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = column - 1; i >= 0; i--) {

            if (workBoard[row][i] != value) {
                workBoard[row][i] = value;
            } else {

                return workBoard;
            }
        }

        return board;
    }

    /**
     * Fills the cells in the upper-left direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillTopLeft(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (column == 0 || row == 0) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = row - 1, j = column - 1; i >= 0 && j >= 0; i--, j--) {

            if (workBoard[i][j] != value) {
                workBoard[i][j] = value;
            } else {

                return workBoard;
            }
        }

        return board;
    }

    /**
     * Fills the cells in the top direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillTop(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (row == 0) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = row - 1; i >= 0; i--) {

            if (workBoard[i][column] != value) {
                workBoard[i][column] = value;
            } else {

                //foundOpponent = true;
                return workBoard;
            }
        }

        return board;
    }

    /**
     * Fills the cells in the upper-right direction.
     * @param row row
     * @param column column
     * @param value color
     * @param board game board
     * @param boardSize board size
     * @return game board
     */
    private static char[][] fillTopRight(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (column == boardSize - 1 || row == 0) {
            return board;
        }

        char[][] workBoard = copyBoard(board, boardSize);

        //Look for an opponent piece at the current direction.
        for (int i = row - 1, j = column + 1; i >= 0 && j < boardSize; i--, j++) {

            if (workBoard[i][j] != value) {
                workBoard[i][j] = value;
            } else {

                return workBoard;
            }
        }

        return board;
    }
}
