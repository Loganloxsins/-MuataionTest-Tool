package edu.nju.mutest.visitor.collector;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Collect variable declaration expressions
 * @see VariableDeclarationExpr
 */
public class VariableDeclarationExprCollector extends VoidVisitorAdapter<List<VariableDeclarationExpr>> {

    @Override
    public void visit(VariableDeclarationExpr n, List<VariableDeclarationExpr> arg) {
        super.visit(n, arg);
        arg.add(n);
    }

    public static List<VariableDeclarationExpr> collect(CompilationUnit cu) {
        VariableDeclarationExprCollector collector = new VariableDeclarationExprCollector();
        List<VariableDeclarationExpr> variableDeclarationExprList = new NodeList<>();
        collector.visit(cu, variableDeclarationExprList);
        return variableDeclarationExprList;
    }

}
