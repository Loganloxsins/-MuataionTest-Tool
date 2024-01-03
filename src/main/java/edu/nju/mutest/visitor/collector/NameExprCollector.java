package edu.nju.mutest.visitor.collector;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.javaparser.ast.expr.UnaryExpr.Operator.*;

public class NameExprCollector extends VoidVisitorAdapter<List<NameExpr>> {

    private UnaryExpr.Operator blacklist[] = {
            POSTFIX_DECREMENT, POSTFIX_INCREMENT, PREFIX_DECREMENT, PREFIX_INCREMENT
    };

    public static List<NameExpr> collect(CompilationUnit cu) {
        NameExprCollector collector = new NameExprCollector();
        List<NameExpr> singleVariableExprList = new ArrayList<>();
        collector.visit(cu, singleVariableExprList);
        return singleVariableExprList;
    }

    @Override
    public void visit(NameExpr n, List<NameExpr> arg) {
        super.visit(n, arg);

        if (n.getParentNode().isPresent()) {
            Node parent = n.getParentNode().get();
            if (parent instanceof AssignExpr) {
                if (((AssignExpr) parent).getTarget() != n) {
                    arg.add(n);
                }
            } else if (parent instanceof UnaryExpr) {
                if (!Arrays.asList(blacklist).contains(((UnaryExpr) parent).getOperator())) {
                    arg.add(n);
                }
            } else {
                arg.add(n);
            }
        }
    }

}