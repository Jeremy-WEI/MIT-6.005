package sat;

import immutable.*;
import sat.env.Environment;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(), new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty()) return env;
        int minSize = Integer.MAX_VALUE;
        Clause minClause = new Clause();
        for (Clause c : clauses) {
            if (c.size() < minSize) {
                minSize = c.size();
                minClause = c;
            }
            if (c.isEmpty()) return null;
        }
        Environment newEnv;
        if (minClause.size() == 1) {
            Literal l = minClause.chooseLiteral();
//            System.out.println(l.getVariable());
            if (l instanceof NegLiteral) newEnv = solve(substitute(clauses, l), env.putFalse(l.getVariable()));
            else newEnv = solve(substitute(clauses, l), env.putTrue(l.getVariable())); 
        }
        else {
            Literal l = minClause.chooseLiteral();
//            System.out.println(l);
            if (l instanceof NegLiteral) {
                newEnv  = solve(substitute(clauses, l), env.putFalse(l.getVariable()));
                if (newEnv == null) 
                    newEnv = solve(substitute(clauses, l.getNegation()), env.putTrue(l.getVariable()));
            }
            else {
                newEnv  = solve(substitute(clauses, l), env.putTrue(l.getVariable()));
                if (newEnv == null) 
                    newEnv = solve(substitute(clauses, l.getNegation()), env.putFalse(l.getVariable()));
            }
        }
        return newEnv;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        ImList<Clause> newClauses = new EmptyImList<Clause>();
        for (Clause c : clauses) {
            Clause cr = c.reduce(l);
            if (cr != null) 
                newClauses = newClauses.add(cr);
        }
        return newClauses;
    }
}
