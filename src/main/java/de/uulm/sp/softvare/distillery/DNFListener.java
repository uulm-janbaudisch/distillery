package de.uulm.sp.softvare.distillery;

import de.uulm.sp.softvare.distillery.parser.DNFBaseListener;
import de.uulm.sp.softvare.distillery.parser.DNFParser;
import java.util.ArrayList;
import java.util.List;

public class DNFListener extends DNFBaseListener {
    public final List<DNF> dnfs;
    private DNF currentDNF;
    private DNF.Product currentProduct;

    public DNFListener() {
        this.dnfs = new ArrayList<>();
    }

    @Override
    public void enterDnf(DNFParser.DnfContext ctx) {
        this.currentDNF = new DNF();
    }

    @Override
    public void exitDnf(DNFParser.DnfContext ctx) {
        this.dnfs.add(this.currentDNF);
    }

    @Override
    public void enterProduct(DNFParser.ProductContext ctx) {
        this.currentProduct = new DNF.Product();
    }

    @Override
    public void exitProduct(DNFParser.ProductContext ctx) {
        this.currentDNF.products.add(this.currentProduct);
    }

    @Override
    public void enterLiteral(DNFParser.LiteralContext ctx) {
        String literal = ctx.getText();
        Integer key =
                this.currentDNF.literals.putIfAbsent(literal, this.currentDNF.literals.size());

        this.currentProduct.literals.add(key);
    }
}
