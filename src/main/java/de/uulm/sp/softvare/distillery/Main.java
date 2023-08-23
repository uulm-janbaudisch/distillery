package de.uulm.sp.softvare.distillery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Expected exactly one argument, a path to the DNF file.");
            System.exit(1);
        }

        Path path = Paths.get(args[0]);

        if (!Files.exists(path)) {
            System.err.println("No such file: " + path);
            System.exit(1);
        }

        List<DNF> dnfs = DNF.parseFile(path);
    }
}
