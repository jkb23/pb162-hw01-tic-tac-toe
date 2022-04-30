package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.Utils;
import cz.muni.fi.pb162.hw01.cmd.Messages;

import java.util.ArrayList;

/**
 * @author Matus Jakab
 */
public class Game {
    private final String players;
    private final int size;
    private final int rewindCount;
    private final ArrayList<Board> history;
        public static final String PUT_SYMBOL_REGEX = ".+ .+";
    public static final String REWIND_REGEX = "<<.+";

    /**
     *
     * @param players players playing the game
     * @param size size of board
     * @param rewindCount int of how much we can rewind
     */
    public Game(String players, int size, int rewindCount){
        this.size = size;
        this.history = new ArrayList<>();
        this.rewindCount = rewindCount;
        this.players = players;
    }

    /**
     * adds board to history list
     * @param board board which we want to add
     */
    public void addToHistory(Board board){
        history.add(board.copy());
    }

    /**
     * helps function format
     * @param length length of row in table
     * @param board current board
     * @param row row index
     * @return board line of selected row
     */
    public StringBuilder addLine(int length, Board board, int row){
        StringBuilder ret = new StringBuilder();
        int col = 0;
        for (int i = 1; i <= length; i++){
            if (i % 2 == 0){
                ret.append(board.getCell(row, col));
                col++;
            } else {
                ret.append("|");
            }
        }
        return ret;
    }

    /**
     *
     * @param board board to be formatted
     * @return formatted output of board
     */
    public String format(Board board){
        int length = size * 2 + 1;
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < size; i++){
            formatted.append("-".repeat(length)).append('\n');
            formatted.append(addLine(length, board, i)).append("\n");
        }
        formatted.append("-".repeat(length));

        return formatted.toString();
    }

    /**
     *
     * @param turnCounter count of current turn
     * @param board current board
     * @return formatted output string that represents a round
     */
    public String printGameStatus(int turnCounter, Board board){
        System.out.printf(Messages.TURN_COUNTER, turnCounter);
        System.out.println(format(board));
        System.out.printf(Messages.TURN_PROMPT, players.charAt((turnCounter - 1) % players.length()));
        String input = Utils.readLineFromStdIn();
        System.out.println(Messages.TURN_DELIMITER);

        return input;
    }

    /**
     * checks if user input is correct
     * @param input user input
     * @return null if something went wrong | list of size 1 if user selected rewind
     * | list of size 2 if user selected put to board
     */
    public ArrayList<Integer> checkInput(String input){
        ArrayList<Integer> ret = new ArrayList<>();
        int rewind;

        if (input.matches(PUT_SYMBOL_REGEX)) {
            String[] nums = input.split(" ");

            if (nums.length != 2){
                System.out.println(Messages.ERROR_INVALID_COMMAND);
                return null;
            }

            try {
                ret.add(Integer.parseInt(nums[0]));
                ret.add(Integer.parseInt(nums[1]));
            } catch (NumberFormatException e){
                System.out.println(Messages.ERROR_INVALID_COMMAND);
                return null;
            }

            if (ret.get(0) >= size || ret.get(1) >= size){
                System.out.println(Messages.ERROR_ILLEGAL_PLAY);
                return null;
            }
        } else if (input.matches(REWIND_REGEX)) {
            String str = input.substring(2);

            try {
                rewind = Integer.parseInt(str);
            } catch (NumberFormatException e){
                System.out.println(Messages.ERROR_INVALID_COMMAND);
                return null;
            }

            if (rewind >= rewindCount){
                System.out.println(Messages.ERROR_REWIND);
                return null;
            }
            ret.add(rewind);
        } else {
            System.out.println(Messages.ERROR_INVALID_COMMAND);
            return null;
        }

        return ret;
    }

    /**
     * puts move in the board
     * @param board current board
     * @param row selected row
     * @param col selected column
     * @param turnCounter count of current turn
     * @return false if selected box is not free otherwise true
     */
    public boolean makeTurn(Board board, int row, int col, int turnCounter){
        if (!board.isEmpty(row, col)){
            System.out.println(Messages.ERROR_ILLEGAL_PLAY);
            return false;
        }

        board.put(row, col, players.charAt((turnCounter - 1) % players.length()));
        return true;
    }

    /**
     * rewinds the board selected turns
     * @param rewind int of selected rewind
     * @return rewind board
     */
    public Board rewind(int rewind){
        if (history.size() < rewind + 1){
            System.out.println(Messages.ERROR_REWIND);
            return null;
        }
        Board board = history.get(history.size() - 1 - rewind);
        history.subList(history.indexOf(board) + 1, history.size()).clear();
        return board;
    }

    /**
     *  prints end game messages
     * @param board last board
     * @param winner player who won
     * @param turnCounter count of current turn
     */
    public void endGame(Board board, char winner, int turnCounter){
        System.out.printf(Messages.GAME_OVER, turnCounter);
        System.out.println(format(board));
        if (winner != ' '){
            System.out.printf(Messages.GAME_WINNER, winner);
        }
    }
}
