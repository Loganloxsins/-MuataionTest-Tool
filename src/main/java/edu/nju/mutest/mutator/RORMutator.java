package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BinaryExpr;
import edu.nju.mutest.visitor.collector.BinaryExprCollector;

import static com.github.javaparser.ast.expr.BinaryExpr.Operator.*;

import java.util.List;

public class RORMutator extends AbstractMutator {

    private List<BinaryExpr> mutPoints = null;
    private List<CompilationUnit> mutants = new NodeList<>();

    private BinaryExpr.Operator[] targetOps = {
            PLUS, MINUS, MULTIPLY, DIVIDE
    };

    // New operators for ROR mutation
    private BinaryExpr.Operator[] rorOps = {
            LESS, LESS_EQUALS, GREATER, GREATER_EQUALS, EQUALS, NOT_EQUALS
    };

    private BinaryExpr.Operator falseOp = EQUALS; // You can change this to any other operator
    private BinaryExpr.Operator trueOp = NOT_EQUALS; // You can change this to any other operator

    public RORMutator(CompilationUnit cu) {
        super(cu);
    }

    @Override
    public void locateMutationPoints() {
        mutPoints = BinaryExprCollector.collect(this.origCU);
    }

    @Override
    public List<CompilationUnit> mutate() {

        if (this.mutPoints == null)
            throw new RuntimeException("You must locate mutation points first!");

        for (BinaryExpr mp : mutPoints) {

            BinaryExpr.Operator origOp = mp.getOperator();
            for (BinaryExpr.Operator targetOp : targetOps) {

                if (origOp.equals(targetOp))
                    continue;

                mutants.add(mutateOnce(mp, targetOp));
            }

            // ROR mutation
            for (BinaryExpr.Operator rorOp : rorOps) {

                if (origOp.equals(rorOp))
                    continue;

                mutants.add(mutateOnce(mp, rorOp));
            }

            mutants.add(mutateOnce(mp, falseOp));
            mutants.add(mutateOnce(mp, trueOp));
            mp.setOperator(origOp);
        }

        return this.mutants;
    }

    /**
     * Replace the operator with a given one
     */
    private CompilationUnit mutateOnce(BinaryExpr mp, BinaryExpr.Operator op) {
        mp.setOperator(op);
        return this.origCU.clone();
    }

    public List<CompilationUnit> getMutants() {
        if (mutants.isEmpty())
            System.out.println("Oops, seems no mutation has been conducted yet. Call mutate() first!");
        return mutants;
    }
}
