package game;

import gameoflife.GameOfLifeBoard;
import java.util.Scanner;

// with this class you can test your code, check out assignment
public class GameOfLifeTester {

    private static final Scanner READER = new Scanner(System.in);
    private GameOfLifeBoard board;

    public GameOfLifeTester(GameOfLifeBoard board) {
        this.board = board;
    }

    public void play() {
        draw();
        while (continueTurn()) {
            try {
                board.playTurn();
            } catch (Exception e) {
            }
            draw();
        }
    }

    private static boolean continueTurn() {
        printCommands();
        if ("".equals(READER.nextLine())) {
            return true;
        }

        System.out.println("Thanks!");

        return false;
    }

    private static void printCommands() {
        System.out.print("Press enter to continue, any other key quits: ");
    }

    public void draw() {
        if (this.board == null) {
            return;
        }

        System.out.println("");

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.isAlive(x, y)) {
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
            }

            System.out.println("");
        }
    }
}