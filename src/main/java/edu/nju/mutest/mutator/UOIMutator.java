package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import edu.nju.mutest.visitor.collector.NameExprCollector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.github.javaparser.ast.expr.UnaryExpr.Operator.*;

public class UOIMutator extends AbstractMutator {
    private List<NameExpr> mutPoints = null;  // 变异点列表，即可以发生变异的表达式的名称列表
    private List<CompilationUnit> mutants = new NodeList<>();  // 变异后的编译单元列表
    private HashMap<NameExpr, Integer> mutPointsMap = new HashMap<>();  // 记录每个变异点在mutPoints中出现的次数

    private UnaryExpr.Operator[] targetOps = {  // 可变异的一元表达式操作符数组
            LOGICAL_COMPLEMENT, PLUS, MINUS, PREFIX_INCREMENT, PREFIX_DECREMENT, POSTFIX_INCREMENT, POSTFIX_DECREMENT
    };

    private UnaryExpr.Operator blacklist[] = {
            POSTFIX_DECREMENT, POSTFIX_INCREMENT, PREFIX_DECREMENT, PREFIX_INCREMENT
    };

    public UOIMutator(CompilationUnit cu) {
        super(cu);
    }

    @Override
    public void locateMutationPoints() {
        mutPoints = NameExprCollector.collect(this.origCU);
    }


    @Override
    public List<CompilationUnit> mutate() {
        // Sanity check.
        if (this.mutPoints == null)
            throw new RuntimeException("You must locate mutation points first!");
        // Modify each mutation points.
        for (NameExpr mp : mutPoints) {
            // This is a polluted operation. So we preserve the original
            // Expression for recovering.
            NameExpr origExpr = mp.clone();

            // HashMap Append
            if (mutPointsMap.containsKey(mp))
                mutPointsMap.put(mp, mutPointsMap.get(mp) + 1);
            else
                mutPointsMap.put(mp, 1);

            // Generate simple mutation. Each mutant contains only one
            // mutated point.
            for (UnaryExpr.Operator targetOp : targetOps) {


                // Mutate
                Expression targetExpr = new EnclosedExpr(new UnaryExpr(origExpr, targetOp));
                System.out.println("UOIMutator: " + origExpr + " => " + targetExpr);
                mutants.add(mutateOnce(mp, targetExpr));
            }

            // Recovering
            mp.replace(origExpr);
        }

        return this.mutants;
    }


    /**
     * Replace the operator with a given one
     */
    private CompilationUnit mutateOnce(Expression mp, Expression expr) {
        CompilationUnit mutCU = this.origCU.clone();
        // X是mp在mutPointsMap中出现的次数
        int count = 0;
        for (NameExpr nameExpr : mutCU.findAll(NameExpr.class)) {
            if (nameExpr.equals(mp)) {
                //剔除黑名单

                if (nameExpr.getParentNode().isPresent()) {
                    Node parent = nameExpr.getParentNode().get();
                    if (parent instanceof AssignExpr) {
                        if (((AssignExpr) parent).getTarget() == nameExpr) {
                            continue;
                        }
                    } else if (parent instanceof UnaryExpr) {
                        if (Arrays.asList(blacklist).contains(((UnaryExpr) parent).getOperator())) {
                            continue;
                        }
                    }
                }
                count++;
                if (count == mutPointsMap.get(mp)) {
                    nameExpr.replace(expr);
                    break;
                }
            }
        }
        // Now the CU is a mutated one. Return it.
        return mutCU;
    }

    public List<CompilationUnit> getMutants() {
        if (mutants.isEmpty())
            System.out.println("Oops, seems no mutation has been conducted yet. Call mutate() first!");
        return mutants;
    }
}
