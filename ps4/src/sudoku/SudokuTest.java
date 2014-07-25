package sudoku;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import sudoku.Sudoku.ParseException;


public class SudokuTest {
    

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    // TODO: put your test cases here
    @Test
    public void testCase1() {
        Sudoku sudoku = new Sudoku(2, new int[][]{
                {0, 0, 0, 1}, 
                {2, 3, 0, 4}, 
                {0, 0, 0, 3}, 
                {4, 1, 0, 2}
        });
       assertEquals("...1\n23.4\n...3\n41.2\n", sudoku.toString());
       System.out.println(new Sudoku(3));
    }
    @Test
    public void testCase2() throws IOException, ParseException {
        Sudoku sudoku = Sudoku.fromFile(2, "samples\\sudoku_4x4.txt");
        assertEquals(".234\n341.\n214.\n.321\n", sudoku.toString());
    }
}