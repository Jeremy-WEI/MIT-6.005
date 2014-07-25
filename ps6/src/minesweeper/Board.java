package minesweeper;

import java.util.Random;

public class Board {
    // in boardState, 0 rep UNTOUCHED, 1 rep FLAGGED, 2 rep DUG
    private static final int UNTOUCHED = 0;
    private static final int FLAGGED = 1;
    private static final int DUG = 2;
    int[][] boardState;
    boolean[][] hasBomb;
    int[][] neighborBombs;
    int bombCount;
    int boardSize;

    public Board(int n) {
        boardSize = n;
        boardState = new int[n][n];
        hasBomb = new boolean[n][n];
        neighborBombs = new int[n][n];
        Random randomGen = new Random();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int x = randomGen.nextInt(4);
                if (x == 0) {
                    hasBomb[i][j] = true;
                    bombCount++;
                }
            }
        updateNeighborBombs();
    }
    public synchronized int getBombNumber() {
        return bombCount;
    }
    public synchronized void flag(int row, int col) {
        if (isValidGrid(row, col) && boardState[row][col] == UNTOUCHED)
            boardState[row][col] = FLAGGED;
    }

    public synchronized void deflag(int row, int col) {
        if (isValidGrid(row, col) && boardState[row][col] == FLAGGED)
            boardState[row][col] = UNTOUCHED;
    }

    public synchronized boolean dig(int row, int col) {
        if (!isValidGrid(row, col) || boardState[row][col] != UNTOUCHED)
            return false;
        boolean returnValue = false;
        if (hasBomb[row][col]) {
            hasBomb[row][col] = false;
            bombCount--;
            returnValue = true;
            updateNeighborBombs(row, col);
        }
        setNeighborGrids(row, col);
        return returnValue;
    }

    private synchronized void setNeighborGrids(int row, int col) {
        if (!isValidGrid(row, col) || boardState[row][col] != UNTOUCHED)
            return;
        if (!hasBomb[row][col]) {
            boardState[row][col] = DUG;
            if (neighborBombs[row][col] != 0)
                return;
            else {
                setNeighborGrids(row - 1, col - 1);
                setNeighborGrids(row - 1, col);
                setNeighborGrids(row - 1, col + 1);
                setNeighborGrids(row, col - 1);
                setNeighborGrids(row, col + 1);
                setNeighborGrids(row + 1, col - 1);
                setNeighborGrids(row + 1, col);
                setNeighborGrids(row + 1, col + 1);
            }
        }
        return;
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                switch (boardState[i][j]) {
                case UNTOUCHED:
                    sb.append("- ");
                    break;
                case FLAGGED:
                    sb.append("F ");
                    break;
                case DUG:
                     sb.append(neighborBombs[i][j] == 0 ? "  "
                     : neighborBombs[i][j] + " ");
//                    sb.append(neighborBombs[i][j]);
                    break;
                }
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private synchronized void updateNeighborBombs() {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                neighborBombs[i][j] = countNeighborBombs(i, j);
    }

    private synchronized void updateNeighborBombs(int row, int col) {
        if (isValidGrid(row - 1, col - 1))
            neighborBombs[row - 1][col - 1] = countNeighborBombs(row - 1,
                    col - 1);
        if (isValidGrid(row - 1, col))
            neighborBombs[row - 1][col] = countNeighborBombs(row - 1, col);
        if (isValidGrid(row - 1, col + 1))
            neighborBombs[row - 1][col + 1] = countNeighborBombs(row - 1,
                    col + 1);
        if (isValidGrid(row, col - 1))
            neighborBombs[row][col - 1] = countNeighborBombs(row, col - 1);
        if (isValidGrid(row, col + 1))
            neighborBombs[row][col + 1] = countNeighborBombs(row, col + 1);
        if (isValidGrid(row + 1, col - 1))
            neighborBombs[row + 1][col - 1] = countNeighborBombs(row + 1,
                    col - 1);
        if (isValidGrid(row + 1, col))
            neighborBombs[row + 1][col] = countNeighborBombs(row + 1, col);
        if (isValidGrid(row + 1, col + 1))
            neighborBombs[row + 1][col + 1] = countNeighborBombs(row + 1,
                    col + 1);
    }

    private synchronized int countNeighborBombs(int row, int col) {
        int count = 0;
        if (hasBomb(row - 1, col - 1))
            count++;
        if (hasBomb(row - 1, col))
            count++;
        if (hasBomb(row - 1, col + 1))
            count++;
        if (hasBomb(row, col - 1))
            count++;
        if (hasBomb(row, col + 1))
            count++;
        if (hasBomb(row + 1, col - 1))
            count++;
        if (hasBomb(row + 1, col))
            count++;
        if (hasBomb(row + 1, col + 1))
            count++;
        return count;
    }

    private boolean hasBomb(int row, int col) {
        if (isValidGrid(row, col))
            return hasBomb[row][col];
        return false;
    }

    private boolean isValidGrid(int row, int col) {
        if (row < 0 || col < 0 || row >= boardSize || col >= boardSize)
            return false;
        return true;
    }

    public static void main(String[] args) {
        Board board = new Board(5);
         System.out.println(board);
         board.flag(0, 0);
         System.out.println(board);
        System.out.println(board.bombCount);
//        for (int i = 0; i < board.boardSize; ++i)
//            for (int j = 0; j < board.boardSize; ++j) 
//                board.dig(i, j);
//        System.out.println(board);
    }
}
