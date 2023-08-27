package de.uulm.sp.softvare.formula;

import java.util.ArrayList;
import java.util.List;

public class Literal implements Node {
    private int literal;
    private String id;

    public Literal(int literal, String id) {
        this.literal = literal;
        this.id = id;
    }

    @Override
    public String toString() {
        return (literal < 0 ? "-" : "")
                + id; // Currently uses id in formulas instead of literal index
    }

    public int getLiteral() {
        return literal;
    }

    public String getId() {
        return id;
    }

    @Override
    public int getNumberOfContainedLiterals() {
        return 1;
    }

    @Override
    public Node flatten() {
        return this;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }
}
