/**
 * Created by Danny on 01/01/2018.
 */
public class Game {

    /**
     * Private constructor.
     */
    private Game() {

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
