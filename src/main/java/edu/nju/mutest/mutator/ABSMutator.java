package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import edu.nju.mutest.visitor.collector.BinaryExprCollector;

import static com.github.javaparser.ast.expr.BinaryExpr.Operator.*;

import java.util.List;

public class ABSMutator extends AbstractMutator {

    private List<BinaryExpr> mutPoints = null;
    private List<CompilationUnit> mutants = new NodeList<>();

    private BinaryExpr.Operator[] targetOps = {
            PLUS, MINUS, MULTIPLY, DIVIDE
    };

    public ABSMutator(CompilationUnit cu) {
        super(cu);
    }

    @Override
    public void locateMutationPoints() {
        mutPoints = BinaryExprCollector.collect(this.origCU);
    }

    @Override
    public List<CompilationUnit> mutate() {
        // Sanity check.
        if (this.mutPoints == null)
            throw new RuntimeException("You must locate mutation points first!");

        // Modify each mutation point.
        for (BinaryExpr mp : mutPoints) {
            // This is a polluted operation. So we preserve the original
            // operator for recovering.
            BinaryExpr.Operator origOp = mp.getOperator();

            // Generate simple mutation. Each mutant contains only one
            // mutated point.
            for (BinaryExpr.Operator targetOp : targetOps) {
                // Skip self
                if (origOp.equals(targetOp))
                    continue;
                // Mutate
                mutants.add(mutateOnce(mp, targetOp));
            }

            // ABS mutation
            mutants.add(mutateToAbs(mp));
            mutants.add(mutateToNegAbs(mp));
            mutants.add(mutateToFailOnZero(mp));

            // Recovering
            mp.setOperator(origOp);
        }

        return this.mutants;
    }

    /**
     * Replace the operator with a given one
     */
    private CompilationUnit mutateOnce(BinaryExpr mp, BinaryExpr.Operator op) {
        mp.setOperator(op);
        // Now the CU is a mutated one. Return its clone.
        return this.origCU.clone();
    }

    /**
     * Replace the expression with abs(expression)
     */
    private CompilationUnit mutateToAbs(BinaryExpr mp) {
        MethodCallExpr absMethod = new MethodCallExpr(new NameExpr("Math"), "abs");
        absMethod.addArgument(mp.getRight());
        mp.setRight(absMethod);
        // Now the CU is a mutated one. Return its clone.
        return this.origCU.clone();
    }

    /**
     * Replace the expression with -abs(expression)
     */
    private CompilationUnit mutateToNegAbs(BinaryExpr mp) {
        MethodCallExpr absMethod = new MethodCallExpr(new NameExpr("Math"), "abs");
        absMethod.addArgument(mp.getRight());
        mp.setRight(new BinaryExpr(new NameExpr("-"), absMethod, DIVIDE));
        // Now the CU is a mutated one. Return its clone.
        return this.origCU.clone();
    }

    /**
     * Replace the expression with failOnZero(expression)
     */
    private CompilationUnit mutateToFailOnZero(BinaryExpr mp) {
        MethodCallExpr failOnZeroMethod = new MethodCallExpr(null, "failOnZero");
        failOnZeroMethod.addArgument(mp.getRight());
        mp.setRight(failOnZeroMethod);
        // Now the CU is a mutated one. Return its clone.
        return this.origCU.clone();
    }


    public List<CompilationUnit> getMutants() {
        if (mutants.isEmpty())
            System.out.println("Oops, seems no mutation has been conducted yet. Call mutate() first!");
        return mutants;
    }
}
