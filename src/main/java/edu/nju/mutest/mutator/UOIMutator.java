package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.UnaryExpr;
import edu.nju.mutest.visitor.collector.UnaryExprCollector;

import static com.github.javaparser.ast.expr.UnaryExpr.Operator.*;

import java.util.List;

public class UOIMutator extends AbstractMutator {

    private List<UnaryExpr> mutPoints = null;
    private List<CompilationUnit> mutants = new NodeList<>();

    private UnaryExpr.Operator[] targetOps = {
            PREFIX_INCREMENT, PREFIX_DECREMENT, POSTFIX_INCREMENT, POSTFIX_DECREMENT
    };

    public UOIMutator(CompilationUnit cu) {
        super(cu);
    }

    @Override
    public void locateMutationPoints() {
        mutPoints = UnaryExprCollector.collect(this.origCU);
    }

    @Override
    public List<CompilationUnit> mutate() {
        // Sanity check.
        if (this.mutPoints == null)
            throw new RuntimeException("You must locate mutation points first!");

        // Modify each mutation points.
        for (UnaryExpr mp : mutPoints) {
            // This is a polluted operation. So we preserve the original
            // operator for recovering.
            UnaryExpr.Operator origOp = mp.getOperator();

            // Generate simple mutation. Each mutant contains only one
            // mutated point.
            for (UnaryExpr.Operator targetOp : targetOps) {
                // Skip self
                if (origOp.equals(targetOp))
                    continue;
                // Mutate
                mutants.add(mutateOnce(mp, targetOp));
            }

            // Recovering
            mp.setOperator(origOp);
        }

        return this.mutants;
    }

    /**
     * Replace the operator with a given one
     */
    private CompilationUnit mutateOnce(UnaryExpr mp, UnaryExpr.Operator op) {
        mp.setOperator(op);
        // Now the CU is a mutated one. Return its clone.
        return this.origCU.clone();
    }

    public List<CompilationUnit> getMutants() {
        if (mutants.isEmpty())
            System.out.println("Oops, seems no mutation has been conducted yet. Call mutate() first!");
        return mutants;
    }
}
