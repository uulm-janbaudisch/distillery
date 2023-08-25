package de.uulm.sp.softvare.formula;

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
}
