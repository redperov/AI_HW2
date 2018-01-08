import java.util.ArrayList;

/**
 * The class represents a node in the game.
 * It stores info about the board and its content.
 */
public class Node {

    //Game board.
    private char[][] board;

    //Board size.
    private int boardSize;

    //Node color.
    private char color;

    //Node cost.
    private int cost;

    //Black cells counter.
    private int blackCounter;

    //Black edge cells counter.
    private int blackEdgeCounter;

    //White cells counter.
    private int whiteCounter;

    //White edge cells counter.
    private int whiteEdgeCounter;

    //Empty cells counter.
    private int emptyCounter;

    /**
     * Constructor.
     *
     * @param board     game board
     * @param boardSize board size
     * @param color     player's color
     */
    public Node(char[][] board, int boardSize, char color) {

        this.board = board;
        this.boardSize = boardSize;
        this.color = color;
    }

    /**
     * Color getter.
     *
     * @return color
     */
    public char getColor() {
        return color;
    }

    /**
     * Black cells counter getter.
     *
     * @return counter value
     */
    public int getBlackCounter() {
        return blackCounter;
    }

    /**
     * Black edge cells counter getter.
     *
     * @return counter value
     */
    public int getBlackEdgeCounter() {
        return blackEdgeCounter;
    }

    /**
     * Cost getter
     *
     * @return cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Cost setter
     *
     * @param cost cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * White cells counter getter.
     *
     * @return counter value
     */
    public int getWhiteCounter() {
        return whiteCounter;
    }

    /**
     * White edge cells counter getter.
     *
     * @return counter value
     */
    public int getWhiteEdgeCounter() {
        return whiteEdgeCounter;
    }

    /**
     * Empty cells counter getter.
     *
     * @return counter value
     */
    public int getEmptyCounter() {
        return emptyCounter;
    }

    /**
     * Checks if the node is terminal, meaning all the board is full.
     *
     * @return boolean is terminal.
     */
    public boolean isTerminal() {

        //Go over all the board and look for an empty cell.
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {

                //Check if cell is empty.
                if (this.board[i][j] == 'E') {

                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Counts all the colors on the board.
     */
    public void countColors() {

        this.blackCounter = 0;
        this.blackEdgeCounter = 0;
        this.whiteCounter = 0;
        this.whiteEdgeCounter = 0;
        this.emptyCounter = 0;

        //Count the different colors.
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {

                //Check if cell black.
                if (this.board[i][j] == 'B') {

                    this.blackCounter++;

                    if (this.isEdgeCell(i, j)) {

                        this.blackEdgeCounter++;
                    }
                } else if (this.board[i][j] == 'W') {

                    this.whiteCounter++;

                    if (this.isEdgeCell(i, j)) {

                        this.whiteEdgeCounter++;
                    }
                } else {

                    this.emptyCounter++;
                }
            }
        }
    }

    /**
     * Checks if the given cell is an edge on the board.
     *
     * @param row    row
     * @param column column
     * @return is edge
     */
    private boolean isEdgeCell(int row, int column) {

        return (row == 0 || row == this.boardSize - 1 || column == 0 || column == this.boardSize - 1);
    }

    /**
     * Gets all the possible states from the current state.
     *
     * @param isMaximizing is a maximizing player
     * @return list of nodes
     */
    public ArrayList<Node> getSuccessors(boolean isMaximizing) {

        char childColor;

        if (isMaximizing) {
            childColor = 'B';
        } else {
            childColor = 'W';
        }

        //Perform all the possible moves from the current state.
        ArrayList<Node> successors = new ArrayList<>();

        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {

                //Check if the move is legal.
                if (Game.isLegalMove(i, j, this.board, this.boardSize)) {

                    //Perform the move on the board.
                    char[][] childBoard = Game.performMovement(i, j, childColor, this.board, this.boardSize);
                    Node     child      = new Node(childBoard, this.boardSize, childColor);

                    //Add the state to the list.
                    successors.add(child);
                }
            }
        }

        return successors;
    }
}
