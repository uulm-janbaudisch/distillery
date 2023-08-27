package de.uulm.sp.softvare.compactor;

import de.uulm.sp.softvare.distillery.DNF;
import de.uulm.sp.softvare.distillery.DNF.Product;
import de.uulm.sp.softvare.formula.And;
import de.uulm.sp.softvare.formula.Literal;
import de.uulm.sp.softvare.formula.Node;
import de.uulm.sp.softvare.formula.Or;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GreedyCompactor implements CompactStrategy {

    private List<String> idLookUp;

    private class SolutionsPartition {
        List<Product> affectedSolutions;
        List<Product> unaffectedSolutions;

        public SolutionsPartition(
                List<Product> affectedSolutions, List<Product> unaffectSolutions) {
            this.affectedSolutions = affectedSolutions;
            this.unaffectedSolutions = unaffectSolutions;
        }
    }

    @Override
    public Node compactDnf(DNF input) {
        this.idLookUp = input.literals;
        Node result = getPartialNode(input.products, null);
        return result;
    }

    // Recursive implementation for prototyping
    private Node getPartialNode(List<Product> solutions, Literal decision) {
        if (solutions.size() == 1) {
            List<Node> literals =
                    solutions.get(0).literals.stream()
                            .map(literal -> new Literal(literal, idLookUp.get(literal)))
                            .collect(Collectors.toList());
            return new And(literals);
        }
        List<Node> topLevelParts = new ArrayList<>();
        if (decision != null) {
            applyDecision(decision, solutions);
            topLevelParts.add(decision);
        }
        Map<Integer, Integer> occurences =
                getOccurenceMap(solutions); // TODO: Implement updates instead of recomputing
        // everytime
        topLevelParts.addAll(getLiteralsAppearingInAll(occurences, solutions));
        Literal maxLiteral = getMaxLiteral(occurences);
        SolutionsPartition partition = getSolutionsPartition(maxLiteral.getLiteral(), solutions);
        topLevelParts.add(
                new Or(
                        getPartialNode(partition.affectedSolutions, maxLiteral),
                        getPartialNode(partition.unaffectedSolutions, null)));
        return new And(topLevelParts);
    }

    private void applyDecision(Literal decision, List<Product> solutions) {
        for (Product solution : solutions) {
            solution.literals.removeIf(lit -> lit == decision.getLiteral());
        }
    }

    private Map<Integer, Integer> getOccurenceMap(List<Product> solutions) {
        Map<Integer, Integer> occurences = new HashMap<>();
        for (Product solution : solutions) {
            for (Integer literal : solution.literals) {
                occurences.put(
                        literal, occurences.containsKey(literal) ? occurences.get(literal) + 1 : 1);
            }
        }
        return occurences;
    }

    /**
     * Careful this also removes the literals from the occurence map
     *
     * @param occurences
     * @param numberOf
     * @return
     */
    private List<Node> getLiteralsAppearingInAll(
            Map<Integer, Integer> occurences, List<Product> solutions) {
        int numberOf = solutions.size();
        List<Node> indicesOfLiteralsAppearingInAll = new ArrayList<>();
        Set<Integer> literals = new HashSet<>();
        literals.addAll(occurences.keySet());
        for (Integer key : literals) { // optimize later
            if (occurences.get(key) == numberOf) {
                indicesOfLiteralsAppearingInAll.add(new Literal(key, idLookUp.get(key)));
                occurences.remove(key);
                for (Product solution : solutions) {
                    solution.literals.remove(key);
                }
            }
        }
        return indicesOfLiteralsAppearingInAll;
    }

    private Literal getMaxLiteral(Map<Integer, Integer> occurences) {
        Integer maxOccurence = 0;
        Integer maxKey = -1;
        for (Integer key : occurences.keySet()) { // optimize later
            if (occurences.get(key) > maxOccurence) {
                maxKey = key;
                maxOccurence = occurences.get(key);
            }
        }
        return new Literal(maxKey, idLookUp.get(maxKey));
    }

    private SolutionsPartition getSolutionsPartition(int splitLiteral, List<Product> solutions) {
        List<Product> affectedSolutions = new ArrayList<>();
        List<Product> unaffectedSolutions = new ArrayList<>();

        for (Product solution : solutions) {
            if (solution.literals.size() == 0) {
                continue;
            }
            if (solution.literals.contains(splitLiteral)) {
                affectedSolutions.add(solution);
            } else {
                unaffectedSolutions.add(solution);
            }
        }
        return new SolutionsPartition(affectedSolutions, unaffectedSolutions);
    }
}
