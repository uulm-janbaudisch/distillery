package de.uulm.sp.softvare.formula;

import java.util.List;
import java.util.stream.Collectors;

public class And implements Node {

    private List<Node> children;

    private static final String OPERATOR = "+";

    public And(List<Node> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "("
                + children.stream()
                        .map(child -> child.toString())
                        .collect(Collectors.joining(OPERATOR))
                + ")";
    }
}
