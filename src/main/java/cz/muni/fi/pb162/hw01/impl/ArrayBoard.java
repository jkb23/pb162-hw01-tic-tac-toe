package cz.muni.fi.pb162.hw01.impl;

/**
 * @author Matus Jakab
 */
public class ArrayBoard implements Board {
    private final Character[][] board;
    private final int size;

    /**
     *
     * @param size of board
     */
    public ArrayBoard(int size){
        this.size = size;
        board = new Character[size][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                board[i][j] = ' ';
            }
        }
    }

    /**
     * puts character ina cell
     * @param row selected row
     * @param col selected col
     * @param symbol symbol of a player who made turn
     */
    public void put(int row, int col, char symbol){
        board[row][col] = symbol;
    }

    /**
     *
     * @param x row
     * @param y column
     * @return character on selected cell
     */
    public Character getCell(int x, int y){
        return board[x][y];
    }

    /**
     *
     * @return size of board
     */
    public int size(){
        return this.size;
    }

    /**
     *
     * @return true if board is full otherwise false
     */
    public boolean isFull(){
        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){
                if (board[row][col] == ' '){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param row selected row
     * @param col selected column
     * @return true if selected cell is empty otherwise false
     */
    public boolean isEmpty(int row, int col){
        return board[row][col] == ' ';
    }

    /**
     *
     * @return deep copy of board
     */
    public Board copy(){
        Board newBoard = new ArrayBoard(size);
        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){
                newBoard.put(row, col, board[row][col]);
            }
        }
        return newBoard;
    }


}
