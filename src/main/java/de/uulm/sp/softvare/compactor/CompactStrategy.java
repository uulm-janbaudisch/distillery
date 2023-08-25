package de.uulm.sp.softvare.compactor;

import de.uulm.sp.softvare.distillery.DNF;
import de.uulm.sp.softvare.formula.Node;

public interface CompactStrategy {
    public Node compactDnf(DNF input);
}
