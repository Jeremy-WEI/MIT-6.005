package sat.formula;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormulaTest {    
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal d = PosLiteral.make("d");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    Literal nd = d.getNegation();
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
        Formula f = new Formula(make(a, b));
        f = f.addClause(make(c, d));
        f = f.not();
        System.out.print(new Formula(make(a)).addClause(make(b)).or(new Formula(make(c)).addClause(make(d))));
    }
    
    
    // Helper function for constructing a clause.  Takes
    // a variable number of arguments, e.g.
    //  clause(a, b, c) will make the clause (a or b or c)
    // @param e,...   literals in the clause
    // @return clause containing e,...
    private Clause make(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }
}