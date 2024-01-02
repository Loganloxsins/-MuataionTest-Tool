package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.nju.mutest.visitor.collector.ExpressionCollector;
import edu.nju.mutest.visitor.collector.MethodDeclarationCollector;
import edu.nju.mutest.visitor.collector.UnaryExprCollector;
import edu.nju.mutest.visitor.collector.cond.BooleanCond;
import edu.nju.mutest.visitor.collector.cond.IntegerCond;
import edu.nju.mutest.visitor.collector.cond.NumericCond;

import java.util.ArrayList;
import java.util.List;

public class UOIMutator extends AbstractMutator{

    private List<Expression> mutPoints = new ArrayList<>();
    private List<CompilationUnit> mutants = new ArrayList<>();

    public UOIMutator(CompilationUnit cu) {
        super(cu);
    }

    //uoi是插入一元运算到表达式前，而不是找一元表达式
    //    @Override
//    public void locateMutationPoints() {
//        mutPoints = UnaryExprCollector.collect(this.origCU);
//    }
    @Override
    public void locateMutationPoints() {
        mutPoints = ExpressionCollector.collect(this.origCU);
    }

    @Override
    public List<CompilationUnit> mutate() {
        if (this.mutPoints == null)
            throw new RuntimeException("You must locate mutation points first!");

        for (Expression mp : mutPoints) {
            if (new IntegerCond().willCollect(mp)) {
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.BITWISE_COMPLEMENT));
            }
            if (new BooleanCond().willCollect(mp)) {
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.LOGICAL_COMPLEMENT));
            }
            if (mp instanceof NameExpr) {
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.PREFIX_INCREMENT));
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.PREFIX_DECREMENT));
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.POSTFIX_INCREMENT));
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.POSTFIX_DECREMENT));
            }
            if (new NumericCond().willCollect(mp)) {
                replaceNode(mp, new UnaryExpr(new EnclosedExpr(mp.clone()), UnaryExpr.Operator.MINUS));
            }
        }
        return mutants;
    }

    private void replaceNode(Expression oldExpr, Expression newExpr) {
        newExpr = new EnclosedExpr(newExpr);
        oldExpr.replace(newExpr);
        CompilationUnit mutCU = this.origCU.clone();
        newExpr.replace(oldExpr);
        mutants.add(mutCU);
    }
//    @Override
//    public void locateMutationPoints() {
//        List<MethodDeclaration> methodDeclarations = MethodDeclarationCollector.collect(this.origCU);
//
//        for (MethodDeclaration method : methodDeclarations) {
//            method.accept(new VoidVisitorAdapter<Void>() {
//                @Override
//                public void visit(ReturnStmt n, Void arg) {
//                    super.visit(n, arg);
//                    Expression expr = n.getExpression().orElse(null);
//                    if (expr != null) {
//                        if (expr instanceof BinaryExpr || expr instanceof NameExpr || expr instanceof MethodCallExpr || expr instanceof BooleanLiteralExpr) {
//                            mutPoints.add(expr);
//                        }
//                    }
//                }
//            }, null);
//        }
//
//        if (mutPoints.isEmpty())
//            throw new RuntimeException("cannot find mutation points!");
//    }

}
