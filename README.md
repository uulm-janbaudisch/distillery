# distillery

> DNF compactor

This project parses DNFs in a specific format and compacts them into a denser format.

## DNF Format

The format used for reading in DNFs is the following:

```
[A,B];[B,C]
[A,E,Y];[Z,W,I]
```

Each line denotes one set of DNFs that should be compacted.
Per set of DNFs each of them is written between `[` and `]` and they are separated by a semicolon.

## Build

This is a [Maven][maven] project.
It includes an ANTLR4 grammar that needs to be compiled in order to be used.
To compile the project, run:

```
mvn compile
```

## Verify

This project enforces the [AOSP Java style][aosp-style] and the spotless ANTLR4 style using [Spotless][spotless].
To verify whether the code is complying to these styles, verify the project:

```
mvn verify
```

In case Spotless reports violations, format the code with:

```
mvn spotless:apply
```

[maven]: https://maven.apache.org
[aosp-style]: https://github.com/google/google-java-format
[spotless]: https://github.com/diffplug/spotless
