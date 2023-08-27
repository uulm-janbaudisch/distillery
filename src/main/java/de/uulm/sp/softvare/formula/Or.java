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

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return children.stream()
                .map(child -> child.toString())
                .collect(Collectors.joining(OPERATOR));
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
    public Node flatten() {
        if (children.size() == 1) {
            return children.get(0);
        } else {
            List<Node> newParts = new ArrayList<>();
            for (Node child : children) {
                if (child instanceof Or) {
                    for (Node grandchild : child.getChildren()) {
                        newParts.add(grandchild.flatten());
                    }
                } else {
                    newParts.add(child.flatten());
                }
            }
            return new Or(newParts);
        }
    }
}
