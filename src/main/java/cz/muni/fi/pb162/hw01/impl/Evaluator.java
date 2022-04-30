package cz.muni.fi.pb162.hw01.impl;

/**
 * @author Matus Jakab
 */
public class Evaluator {
    private int check;
    private char lastSymbol;
    private char winner;
    private final Board board;
    private final int winSize;
    private final int size;

    /**
     *
     * @param board current board
     * @param winSize size of win
     * @param size size of board
     */
    public Evaluator(Board board, int winSize, int size){
        this.board = board;
        this.winSize = winSize;
        this.size = size;
    }

    /**
     * helps evaluate board to not use duplicate code
     * @param row selected row
     * @param col selected column
     * @return player's character if anyone won otherwise ' '
     */
    public char checkHelp(int row, int col){
        if (board.getCell(row, col) == lastSymbol && board.getCell(row, col) != ' '){
            check++;
        } else {
            check = 1;
        }

        if (check == winSize){
            return board.getCell(row, col);
        }
        return ' ';
    }

    /**
     * checks rows and columns if there is a winner
     * @param board current board
     * @return player's character if anyone won otherwise ' '
     */
    public char checkRowCol(Board board){

        for (int i = 0; i < 2; i++){
            for (int col = 0; col < size; col++){
                check = 1;
                lastSymbol = ' ';
                for (int row = 0; row < size; row++){
                    int tempCol = col;
                    int tempRow = row;

                    if (i == 1){
                        row = tempCol;
                        col = tempRow;
                    }

                    winner = checkHelp(row, col);
                    if (winner != ' '){
                        return winner;
                    }

                    lastSymbol = board.getCell(row, col);
                    row = tempRow;
                    col = tempCol;
                }
            }
        }
        return winner;
    }

    /**
     * checks diagonal if there is a winner
     * @param board current board
     * @return player's character if anyone won otherwise ' '
     */
    public char checkDiag(Board board){
        int col = 0;
        int row = 0;

        for (int i = 0; i < 2; i++){
            check = 1;
            lastSymbol = ' ';
            for (int j = 0; j < size; j++){
                System.out.println(row + " " + col);
                winner = checkHelp(row, col);
                if (winner != ' '){
                    return winner;
                }

                lastSymbol = board.getCell(row, col);
                if (i == 1){
                    col--;
                } else {
                    col++;
                }
                row++;
            }
            col = size - 1;
            row = 0;
        }
        return winner;
    }

    /**
     * evaluates board
     * @return player's character if anyone won otherwise ' '
     */
    public char evaluate(){
        winner = checkDiag(board);
        if (winner != ' '){
            return winner;
        }

        return checkRowCol(board);
    }
}
