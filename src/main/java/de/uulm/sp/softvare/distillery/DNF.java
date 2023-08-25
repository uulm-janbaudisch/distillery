package de.uulm.sp.softvare.distillery;

import de.uulm.sp.softvare.distillery.parser.DNFLexer;
import de.uulm.sp.softvare.distillery.parser.DNFParser;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class DNF {
    public static class Product {
        public final List<Integer> literals;

        public Product() {
            this.literals = new ArrayList<>();
        }
    }

    public final List<String> literals;
    public final List<Product> products;

    public DNF() {
        this.literals = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public static List<DNF> parseFile(Path path) throws IOException {
        // Completely read file.
        CharStream input = CharStreams.fromPath(path);

        // Setup ANTLR components.
        DNFLexer lexer = new DNFLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DNFParser parser = new DNFParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        DNFListener listener = new DNFListener();

        // Walk the file to parse the DNFs.
        walker.walk(listener, parser.file());
        return listener.dnfs;
    }
}
