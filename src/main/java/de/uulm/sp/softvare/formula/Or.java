package de.uulm.sp.softvare.formula;

import java.util.List;
import java.util.stream.Collectors;

public class Or {
    private List<Node> children;

    private final static String OPERATOR = "/";

    public Or(List<Node> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return children.stream().map( child -> child.toString()).collect(Collectors.joining(OPERATOR));
    }
}
