import java.util.ArrayList;

/**
 * The class represents a node in the game.
 * It performs the calculations of the different moves in the game.
 */
public class Node {

    private char[][] board;
    private int      boardSize;
    private char     color;
    private int      cost;
    private int blackCounter;
    private int blackEdgeCounter;
    private int whiteCounter;
    private int whiteEdgeCounter;
    private int emptyCounter;

    public Node(char[][] board, int boardSize, char color) {//, char color, int row, int column){

        this.board = board;
        this.boardSize = boardSize;
        this.color = color;
        //this.performMove(color, row, color);
    }

    public char getColor() {
        return color;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getBlackCounter() {
        return blackCounter;
    }

    public int getBlackEdgeCounter() {
        return blackEdgeCounter;
    }


    public int getWhiteCounter() {
        return whiteCounter;
    }

    public int getWhiteEdgeCounter() {
        return whiteEdgeCounter;
    }

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
        for (int i = 0; i < this.boardSize - 1; i++) {
            for (int j = 0; j < this.boardSize - 1; j++) {

                //Check if cell is empty.
                if (this.board[i][j] == 'E') {

                    return false;
                }
            }
        }

        return true;
    }

    public void countColors(){

        this.blackCounter = 0;
        this.blackEdgeCounter = 0;
        this.whiteCounter = 0;
        this.whiteEdgeCounter = 0;
        this.emptyCounter = 0;

        //Count the different colors.
        for (int i = 0; i < this.boardSize - 1; i++) {
            for (int j = 0; j < this.boardSize - 1; j++) {

                //Check if cell black.
                if (this.board[i][j] == 'B') {

                    this.blackCounter++;

                    if(this.isEdgeCell(i, j)){

                        this.blackEdgeCounter++;
                    }
                } else if(this.board[i][j] == 'W') {

                    this.whiteCounter++;

                    if(this.isEdgeCell(i, j)){

                        this.whiteEdgeCounter++;
                    }
                }
                else{

                    this.emptyCounter++;
                }
            }
        }
    }

    private boolean isEdgeCell(int row, int column){

        return (row == 0 || row == this.boardSize - 1 || column == 0 || column == this.boardSize - 1);
    }

    public ArrayList<Node> getSuccessors() {

        ArrayList<Node> successors = new ArrayList<>();

        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {

                if (Game.isLegalMove(i, j, this.board, this.boardSize)) {

                    char[][] childBoard = Game.performMovement(i, j, this.color, this.board, this.boardSize);
                    Node     child      = new Node(childBoard, this.boardSize, color);
                    successors.add(child);
                }
            }
        }

        //for all empty cells
        //tempBoard =  performMoveToCell
        //create Node(tempBoard)
        //add node to list
        //return list
        return successors;
    }
}
