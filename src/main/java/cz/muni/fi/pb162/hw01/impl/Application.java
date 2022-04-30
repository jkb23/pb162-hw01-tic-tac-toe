package cz.muni.fi.pb162.hw01.impl;

import com.beust.jcommander.Parameter;
import cz.muni.fi.pb162.hw01.cmd.CommandLine;

import java.util.ArrayList;

/**
 * Application class represents the command line interface of this application.
 *
 * You are expected to implement the {@link Application#run()} method
 *
 * @author jcechace
 */
public class Application {
    @Parameter(names = {"--size", "-s"})
    private int size = 3;

    @Parameter(names = "--help", help = true)
    private boolean showUsage = false;

    @Parameter(names = {"--win", "-w"})
    private int winSize = 3;

    @Parameter(names = {"--history", "-h"})
    private int rewindCount = 1;

    @Parameter(names = {"--players", "-p"})
    private String players = "xo";

    /**
     * Application entry point
     *
     * @param args command line arguments of the application
     */
    public static void main(String[] args) {
        Application app = new Application();

        CommandLine cli = new CommandLine(app);
        cli.parseArguments(args);

        if (app.showUsage) {
            cli.showUsage();
        } else {
            app.run();
        }

    }

    /**
     * Application runtime logic
     */
    private void run() {
        int turnCounter = 1;
        char winner = ' ';
        Board board = new ArrayBoard(size);
        Game game = new Game(players, size, rewindCount);
        game.addToHistory(board);

        while (!board.isFull()){
            String input = game.printGameStatus(turnCounter, board);

            if (input.equals(":q")){
                turnCounter --;
                break;
            }

            ArrayList<Integer> nums = game.checkInput(input);

            if (nums == null){
                continue;
            }

            if (nums.size() == 1){
                Board copy = board.copy();
                board = game.rewind(nums.get(0));
                if (board == null){
                    board = copy;
                    continue;
                }
                turnCounter ++;
                continue;
            }

            if (!game.makeTurn(board, nums.get(0), nums.get(1), turnCounter)){
                continue;
            }

            Evaluator evaluator = new Evaluator(board, winSize, size);
            winner = evaluator.evaluate();
            if (winner != ' '){
                break;
            }

            game.addToHistory(board);
            turnCounter ++;
        }
        game.endGame(board, winner, turnCounter);
    }
}
