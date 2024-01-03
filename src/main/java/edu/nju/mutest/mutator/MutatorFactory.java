package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;

public class MutatorFactory {
    public static Mutator createMutator(String opt, CompilationUnit cu) {
        switch (opt) {
            case "BIN":
                return new BinaryMutator(cu);
            case "ABS":
                return new ABSMutator(cu);
            case "AOR":
                return new AORMutator(cu);
            case "LCR":
                return new LCRMutator(cu);
            case "ROR":
                return new RORMutator(cu);
            case "UOI":
                return new UOIMutator(cu);
            case "UOR":
                return new UORMutator(cu);
            default:
                throw new IllegalArgumentException("This mutator is not supported.");
        }
    }
}

