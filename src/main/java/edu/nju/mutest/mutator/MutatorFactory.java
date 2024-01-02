package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;

public class MutatorFactory {
    public static Mutator createMutator(String opt, CompilationUnit cu) {
        switch (opt) {
            case "BIN":
                return new BinaryMutator(cu);
            case "ABS":
                return null;
            case "AOR":
                return null;
            case "LCR":
                return null;
            case "ROR":
                return null;
            case "UOI":
                return new UOIMutator(cu);
            case "AIR":
                return null;
            default:
                throw new IllegalArgumentException("This mutator is not supported.");
        }
    }
}

