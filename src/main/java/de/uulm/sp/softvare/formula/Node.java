package de.uulm.sp.softvare.formula;

import java.util.List;

public interface Node {
    public int getNumberOfContainedLiterals();

    public Node flatten();

    public List<Node> getChildren();
}
