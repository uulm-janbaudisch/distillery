package de.uulm.sp.softvare.formula;

import java.util.ArrayList;
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

    @Override
    public Node flatten() {
        if (children.size() == 1) {
            return children.get(0);
        } else {
            List<Node> newParts = new ArrayList<>();
            for (Node child : children) {
                if (child instanceof And) {
                    for (Node grandchild : child.getChildren()) {
                        newParts.add(grandchild.flatten());
                    }
                } else {
                    newParts.add(child.flatten());
                }
            }
            return new And(newParts);
        }
    }

    @Override
    public int getNumberOfContainedLiterals() {
        int result = 0;
        for (Node child : children) {
            result += child.getNumberOfContainedLiterals();
        }
        return result;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }
}
