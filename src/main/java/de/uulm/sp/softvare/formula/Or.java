package de.uulm.sp.softvare.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Or implements Node {
    private List<Node> children;

    private static final String OPERATOR = "/";

    public Or(List<Node> children) {
        this.children = children;
    }

    public Or(Node left, Node right) {
        children = new ArrayList<>();
        children.add(left);
        children.add(right);
    }

    @Override
    public String toString() {
        return children.stream()
                .map(child -> child.toString())
                .collect(Collectors.joining(OPERATOR));
    }
}
