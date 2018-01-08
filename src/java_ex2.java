import java.io.*;

/**
 * The main class of the program.
 * It reads the data from the file and applies the Minimax algorithm on the board.
 * Afterwards, writes the solution into the output file.
 */
public class java_ex2 {

    public static void main(String args[]) {

        //Input file path.
        final String inputFilePath = "input.txt";

        //Output file path.
        final String outputFilePath = "output.txt";

        //Board size.
        final int boardSize = 5;

        //Read the data from the input file into the board.
        char[][] board = readInput(inputFilePath, boardSize);

        //Get the winning player in the game.
        char solution = Game.play(board);

        //Write the solution to the output file.
        writeOutput(outputFilePath, solution);
    }

    /**
     * Reads the the board values from the input file.
     *
     * @param filePath  input file path
     * @param boardSize board size
     * @return board with filled values
     */
    public static char[][] readInput(String filePath, int boardSize) {

        char[][] board = new char[boardSize][boardSize];

        //Read data from input file.
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String         rowRead;

            //Read lines containing board values..
            for (int row = 0; row < boardSize; row++) {

                rowRead = bufferedReader.readLine();

                //Fill next board line with values.
                for (int column = 0; column < boardSize; column++) {

                    board[row][column] = rowRead.charAt(column);
                }
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: file reading error.");
            e.printStackTrace();
        }

        return board;
    }

    /**
     * Writes the solution to the output file.
     *
     * @param filePath output file path
     * @param solution solution
     */
    public static void writeOutput(String filePath, char solution) {

        //Write solution to output file.
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "utf-8"))) {

            //Write string to file.
            writer.write(solution);
            writer.close();

        } catch (IOException e) {
            System.out.println("Error: writing to file error.");
            e.printStackTrace();
        }
    }
}
