import java.util.ArrayList;

/**
 * The class represents a node in the game.
 * It performs the calculations of the different moves in the game.
 */
public class Node {

    private char[][] board;
    private int boardSize;
    private char color;
    private int cost;

    public Node(char[][] board, int boardSize, char color){//, char color, int row, int column){

        this.board = board;
        this.boardSize = boardSize;
        this.color = color;
        //this.performMove(color, row, color);
    }

    public ArrayList<Node> getSuccessors(){

        ArrayList<Node> successors = new ArrayList<>();

        for (int i = 0; i < this.boardSize; i++){
            for(int j = 0; j < this.boardSize; j++){

                if(Game.isLegalMove(i, j, this.board, this.boardSize)){

                    char[][] childBoard = Game.performMovement(i, j, this.color, this.board, this.boardSize);
                    Node child = new Node(childBoard, this.boardSize, color);
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
