/**
 * Author: dnj, Hank Huang
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sudoku;

import java.io.*;
import java.util.Arrays;

import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.*;

/**
 * Sudoku is an immutable abstract datatype representing instances of Sudoku.
 * Each object is a partially completed Sudoku puzzle.
 */
public class Sudoku {
    // dimension: standard puzzle has dim 3
    private final int dim;
    // number of rows and columns: standard puzzle has size 9
    private final int size;
    // known values: square[i][j] represents the square in the ith row and jth
    // column,
    // contains -1 if the digit is not present, else i>=0 to represent the digit
    // i+1
    // (digits are indexed from 0 and not 1 so that we can take the number k
    // from square[i][j] and
    // use it to index into occupies[i][j][k])
    private final int[][] square;
    // occupies [i,j,k] means that kth symbol occupies entry in row i, column j
    private final Variable[][][] occupies;

    // Rep invariant
    // TODO: write your rep invariant here
    // 
    private void checkRep() {
        // TODO: implement this.
        throw new RuntimeException("not yet implemented.");
    }

    /**
     * create an empty Sudoku puzzle of dimension dim.
     * 
     * @param dim
     *            size of one block of the puzzle. For example, new Sudoku(3)
     *            makes a standard Sudoku puzzle with a 9x9 grid.
     */
    public Sudoku(int dim) {
        this.dim = dim;
        this.size = dim * dim;
        this.square = new int[size][size];
        for (int i = 0; i < size; i++)
            Arrays.fill(this.square[i], -1);
        this.occupies = new Variable[size][size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    occupies[i][j][k] =new Variable(String.format("occupies(%d, %d, %d)", i, j, k));
    }

    /**
     * create Sudoku puzzle
     * 
     * @param square
     *            digits or blanks of the Sudoku grid. square[i][j] represents
     *            the square in the ith row and jth column, contains 0 for a
     *            blank, else i to represent the digit i. So { { 0, 0, 0, 1 }, {
     *            2, 3, 0, 4 }, { 0, 0, 0, 3 }, { 4, 1, 0, 2 } } represents the
     *            dimension-2 Sudoku grid: 
     *            
     *            ...1 
     *            23.4 
     *            ...3
     *            41.2
     * 
     * @param dim
     *            dimension of puzzle Requires that dim*dim == square.length ==
     *            square[i].length for 0<=i<dim.
     */
    public Sudoku(int dim, int[][] square) {
        int rows = square.length, cols = square[0].length;
        assert(dim * dim == rows && rows == cols);
        this.dim = dim;
        this.size = dim * dim;
        this.square = new int[size][size];
        this.occupies = new Variable[size][size][size];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                this.square[i][j] = square[i][j] - 1;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    occupies[i][j][k] =new Variable(String.format("occupies(%d, %d, %d)", i, j, k));
    }

    /**
     * Reads in a file containing a Sudoku puzzle.
     * 
     * @param dim
     *            Dimension of puzzle. Requires: at most dim of 3, because
     *            otherwise need different file format
     * @param filename
     *            of file containing puzzle. The file should contain one line
     *            per row, with each square in the row represented by a digit,
     *            if known, and a period otherwise. With dimension dim, the file
     *            should contain dim*dim rows, and each row should contain
     *            dim*dim characters.
     * @return Sudoku object corresponding to file contents
     * @throws IOException
     *             if file reading encounters an error
     * @throws ParseException
     *             if file has error in its format
     */
    public static Sudoku fromFile(int dim, String filename) throws IOException,
            ParseException {
        assert(dim <= 3);
        Sudoku sudoku = new Sudoku(dim);
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int i = -1;
        while (++i < sudoku.size) {
            String str = br.readLine();
            for (int j = 0; j < sudoku.size; j++)
                if (str.charAt(j) == '.') sudoku.square[i][j] = -1;
                else sudoku.square[i][j] = str.charAt(j) - '1';
        }
        return sudoku;
    }

    /**
     * Exception used for signaling grammatical errors in Sudoku puzzle files
     */
    @SuppressWarnings("serial")
    public static class ParseException extends Exception {
        public ParseException(String msg) {
            super(msg);
        }
    }

    /**
     * Produce readable string representation of this Sukoku grid, e.g. for a 4
     * x 4 sudoku problem: 
     *   12.4 
     *   3412 
     *   2.43 
     *   4321
     * 
     * @return a string corresponding to this grid
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square[0].length; j++)
                if (square[i][j] < 0) sb.append('.');
                else sb.append(square[i][j] + 1);
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * @return a SAT problem corresponding to the puzzle, using variables with
     *         names of the form occupies(i,j,k) to indicate that the kth symbol
     *         occupies the entry in row i, column j
     */
    public Formula getProblem() {
        Formula f = new Formula();
        for (int i =0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Clause rrClause = new Clause();
                Clause ccClause = new Clause();
                if (square[i][j] != -1) f = f.addClause(new Clause(PosLiteral.make(occupies[i][j][square[i][j]])));
                for (int k = 0; k < size; k++) {
                    Clause clause = new Clause(NegLiteral.make(occupies[i][j][k]));
                    Clause rClause = new Clause(NegLiteral.make(occupies[i][k][j]));
                    Clause cClause = new Clause(NegLiteral.make(occupies[k][i][j]));
                    rrClause = rrClause.add(PosLiteral.make(occupies[i][k][j]));
                    ccClause = ccClause.add(PosLiteral.make(occupies[k][i][j]));
                    for (int m = 0; m < k; m++) {
                        f = f.addClause(clause.add(NegLiteral.make(occupies[i][j][m])));
                        f = f.addClause(rClause.add(NegLiteral.make(occupies[i][m][j])));
                        f = f.addClause(cClause.add(NegLiteral.make(occupies[m][i][j])));
                    }
                }
                f = f.addClause(rrClause);
                f = f.addClause(ccClause);
            }
        }
        for (int ix = 0; ix < dim; ix++) {
            for (int jx = 0; jx < dim; jx++) {
                Clause[] clauseArray = new Clause[size];
                for (int tmp = 0; tmp < size; tmp++)
                    clauseArray[tmp] = new Clause();
                for (int k = 0; k < size; k++) {
                    for (int i = dim * ix; i < dim * (ix + 1); i++) {
                        for (int j =dim * jx; j < dim * (jx + 1); j++) {
                            clauseArray[k] = clauseArray[k].add(PosLiteral.make(occupies[i][j][k]));
                        }
                    }
                }
                for (int tmp = 0; tmp < size; tmp++)
                    f = f.addClause(clauseArray[tmp]);
            }
        }
//        System.out.println("size = " + f.getSize());
        // code below is only needed for diagnal sudoku.
        /*
        for (int k = 0; k < size; k++) {
            Clause clause = new Clause();
            for (int i = 0; i < size; i++) {
                clause = clause.add(PosLiteral.make(occupies[i][i][k]));
            }
            f = f.addClause(clause);
        }
        */
//        System.out.println(f);
        return f;
    }

    /**
     * Interpret the solved SAT problem as a filled-in grid.
     * 
     * @param e
     *            Assignment of variables to values that solves this puzzle.
     *            Requires that e came from a solution to this.getProblem().
     * @return a new Sudoku grid containing the solution to the puzzle, with no
     *         blank entries.
     */
    public Sudoku interpretSolution(Environment e) {
        Sudoku sudoku = new Sudoku(dim);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++) {
                    if (e.get(occupies[i][j][k]).equals(Bool.TRUE)) {
                        sudoku.square[i][j] = k;
//                        System.out.println(occupies[i][j][k] + "=" + Bool.TRUE);
                    }
                }
        return sudoku;
    }
}
