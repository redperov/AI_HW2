import java.util.ArrayList;

/**
 * Created by Danny on 01/01/2018.
 */
public class Game {

    private static final int MAX_DEPTH = 3;
    private static final int BOARD_SIZE = 5;

    /**
     * Private constructor.
     */
    private Game() {

    }

    public static char play(char[][] board){

        Node node = new Node(board, BOARD_SIZE, 'B');
        boolean isMaximizing = true;

        while(!node.isTerminal()){

            node = getBestMove(node, isMaximizing);
            isMaximizing = !isMaximizing;
        }

        node.countColors();

        //TODO should I add a check for isDraw?

        if(node.getColor() == 'B'){

            if(isWon(node, true)){
                return 'B';
            }
            else{
                return 'W';
            }
        }
        else{

            if(isWon(node, false)){

                return 'W';
            }
            else{
                return 'B';
            }
        }
    }

    private static Node getBestMove(Node node, boolean isMaximizing) {

        Node bestChild = null;
        int  bestValue;
        int  tempValue;

        if (isMaximizing) {

            bestValue = Integer.MIN_VALUE;
        } else {
            bestValue = Integer.MAX_VALUE;
        }

        ArrayList<Node> children = node.getSuccessors();

        for (Node child : children) {

            tempValue = minimax(child, MAX_DEPTH, isMaximizing);

            if (isMaximizing) {

                if (tempValue > bestValue) {

                    bestValue = tempValue;
                    bestChild = child;
                }
            } else {

                if (tempValue < bestValue) {

                    bestValue = tempValue;
                    bestChild = child;
                }
            }
        }

        return bestChild;
    }

    public static int minimax(Node node, int depth, boolean maximizingPlayer) {

        if (depth == 0 || node.isTerminal()) {

            return heuristic(node);
        }

        if (maximizingPlayer) {

            int             bestValue = Integer.MIN_VALUE;
            ArrayList<Node> children  = node.getSuccessors();

            for (Node child : children) {

                int v = minimax(child, depth - 1, false);
                bestValue = Math.max(bestValue, v);
            }

            return bestValue;
        } else {

            int             bestValue = Integer.MAX_VALUE;
            ArrayList<Node> children  = node.getSuccessors();

            for (Node child : children) {

                int v = minimax(child, depth - 1, true);
                bestValue = Math.min(bestValue, v);
            }

            return bestValue;
        }
    }

    public static int heuristic(Node node) {

        //Count all the cells colors in advance.
        node.countColors();

        //Check if the node is terminal.
        boolean isTerminal = node.isTerminal();

        //Check if the cell is black.
        if (node.getColor() == 'B') {

            //Return maximum player heuristic.
            return maxHeuristic(node, isTerminal);
        } else {

            //Return minimum player heuristic.
            return minHeuristic(node, isTerminal);
        }
    }

    private static int maxHeuristic(Node node, boolean isTerminal) {

        //Check if node is terminal
        if (isTerminal) {

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

    private static int minHeuristic(Node node, boolean isTerminal) {

        //Check if node is terminal
        if (isTerminal) {

            //Check if the game ended with a draw.
            if (isDraw(node)) {

                return 0;
            }

            //Check if the player won the game.
            if (isWon(node, false)) {

                return Integer.MIN_VALUE;
            }

            return Integer.MAX_VALUE;
        }

        int blackCounter     = node.getBlackCounter();
        int blackEdgeCounter = node.getBlackEdgeCounter();
        int whiteCounter     = node.getWhiteCounter();
        int whiteEdgeCounter = node.getWhiteEdgeCounter();

        //Calculate the minimum player heuristic value.
        int heuristicValue = (whiteCounter - blackCounter) + (whiteEdgeCounter - blackEdgeCounter);

        return heuristicValue;
    }

    private static boolean isDraw(Node node) {

        int blackCounter = node.getBlackCounter();
        int whiteCounter = node.getWhiteCounter();
        int emptyCounter = node.getEmptyCounter();

        return (blackCounter == whiteCounter) && (emptyCounter == 0);

    }

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

        board[row][column] = value;

        //Try replacing the pieces in all the possible direction to the current players value.
        board = fillRight(row, column, value, board, boardSize);
        board = fillBottomRight(row, column, value, board, boardSize);
        board = fillBottom(row, column, value, board, boardSize);
        board = fillBottomLeft(row, column, value, board, boardSize);
        board = fillLeft(row, column, value, board, boardSize);
        board = fillTopLeft(row, column, value, board, boardSize);
        board = fillTop(row, column, value, board, boardSize);
        board = fillTopRight(row, column, value, board, boardSize);

        return board;
    }

    private static char[][] fillRight(int row, int column, char value, char[][] board, int boardSize) {

        //Check if not on the board boundary.
        if (column == boardSize - 1) {
            return board;
        }

        //Create a working board.
        char[][] workBoard = board;

        //Fill the working board in the current direction.
        for (int i = column + 1; i < boardSize; i++) {

            //If current cell is empty, fill it with player value.
            if (workBoard[row][i] == 'E') {
                workBoard[row][i] = value;
            } else {

                //If found an opponent value in the current direction, confirm changes to original board.
                if (workBoard[row][i] != value) {

                    return workBoard;
                }

                break;
            }
        }

        //No changes were made at the current direction.
        return board;
    }

    private static char[][] fillBottomRight(int row, int column, char value, char[][] board, int boardSize) {

        //int     counter       = 0;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (column == boardSize - 1 || row == boardSize - 1) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = row + 1, j = column + 1; i < boardSize && j < boardSize; i++, j++) {

            if (workBoard[i][j] == 'E') {
                workBoard[i][j] = value;
            } else {

                if (workBoard[i][j] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }

    private static char[][] fillBottom(int row, int column, char value, char[][] board, int boardSize) {

        //int     counter       = 0;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (row == boardSize - 1) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = row + 1; i < boardSize; i++) {

            if (workBoard[i][column] == 'E') {
                workBoard[i][column] = value;
            } else {

                if (board[i][column] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }

    private static char[][] fillBottomLeft(int row, int column, char value, char[][] board, int boardSize) {

        //int     counterI      = 0;
        //int     counterJ      = column;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (row == boardSize - 1 || column == 0) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = row + 1, j = column - 1; i < boardSize && j > 0; i++, j--) {

            if (workBoard[i][j] == 'E') {
                workBoard[i][j] = value;
            } else {

                if (workBoard[i][j] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }

    private static char[][] fillLeft(int row, int column, char value, char[][] board, int boardSize) {

        //int     counter       = 0;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (column == 0) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = column - 1; i < boardSize; i--) {

            if (workBoard[row][i] == 'E') {
                workBoard[row][i] = value;
            } else {

                if (workBoard[row][i] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }

    private static char[][] fillTopLeft(int row, int column, char value, char[][] board, int boardSize) {

        //int     counter       = 0;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (column == 0 || row == 0) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = row - 1, j = column - 1; i < boardSize; i--, j--) {

            if (workBoard[i][j] == 'E') {
                workBoard[i][j] = value;
            } else {

                if (workBoard[i][j] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }

    private static char[][] fillTop(int row, int column, char value, char[][] board, int boardSize) {

        //int     counter       = 0;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (row == 0) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = row - 1; i < boardSize; i--) {

            if (workBoard[i][column] == 'E') {
                workBoard[i][column] = value;
            } else {

                if (workBoard[i][column] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }

    private static char[][] fillTopRight(int row, int column, char value, char[][] board, int boardSize) {

        //int     counter       = 0;
        //boolean foundOpponent = false;

        //Check if not on the board boundary.
        if (column == boardSize - 1 || row == 0) {
            return board;
        }

        char[][] workBoard = board;

        //Look for an opponent piece at the current direction.
        for (int i = row - 1, j = column + 1; i < boardSize; i--, j++) {

            if (workBoard[i][j] == 'E') {
                workBoard[i][j] = value;
            } else {

                if (workBoard[i][j] != value) {
                    //foundOpponent = true;
                    return workBoard;
                }

                break;
            }
        }

        return board;
    }
}
