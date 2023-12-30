package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.nju.mutest.visitor.collector.MethodDeclarationCollector;
import edu.nju.mutest.visitor.collector.UnaryExprCollector;

import java.util.ArrayList;
import java.util.List;

public class UOIMutator extends AbstractMutator{

    private List<UnaryExpr> mutPoints = new ArrayList<>();
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
        List<MethodDeclaration> methodDeclarations = MethodDeclarationCollector.collect(this.origCU);

        for (MethodDeclaration method : methodDeclarations) {
            method.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(ReturnStmt n, Void arg) {
                    super.visit(n, arg);
                    Expression expr = n.getExpression().orElse(null);
                    if (expr != null) {
                        if (expr instanceof BinaryExpr || expr instanceof NameExpr || expr instanceof MethodCallExpr || expr instanceof BooleanLiteralExpr) {
                            mutPoints.add(expr);
                        }
                    }
                }
            }, null);
        }

        if (mutPoints.isEmpty())
            throw new RuntimeException("cannot find mutation points!");
    }

}
