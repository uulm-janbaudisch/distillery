package de.uulm.sp.softvare.compactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.uulm.sp.softvare.distillery.DNF;
import de.uulm.sp.softvare.distillery.DNF.Product;
import de.uulm.sp.softvare.formula.And;
import de.uulm.sp.softvare.formula.Literal;
import de.uulm.sp.softvare.formula.Node;
import de.uulm.sp.softvare.formula.Or;

public class GreedyCompactor implements CompactStrategy {

    private Map<Integer, String> idLookUp;

    private class SolutionsPartition {
        List<Product> affectedSolutions;
        List<Product> unaffectedSolutions;

        public SolutionsPartition(List<Product> affectedSolutions, List<Product> unaffectSolutions) {
            this.affectedSolutions = affectedSolutions;
            this.unaffectedSolutions = unaffectSolutions;
        }

    }

    @Override
    public Node compactDnf(DNF input) {
        this.idLookUp = (Map<Integer, String>) input.literals.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)); // TODO: ouh ouh workaround for now
        return getPartialNode(input.products);
    }

    // Recursive implementation for prototyping
    private Node getPartialNode(List<Product> solutions) {
        if (solutions.size() == 1) {
            List<Node> literals = solutions.get(0).literals.stream()
                    .map(literal -> new Literal(literal, idLookUp.get(literal))).collect(Collectors.toList());
            return new And(literals);
        }
        List<Node> topLevelParts = new ArrayList<>();
        Map<Integer, Integer> occurences = getOccurenceMap(solutions); // TODO: Implement updates instead of recomputing
                                                                       // everytime
        topLevelParts.addAll(getLiteralsAppearingInAll(occurences, solutions.size()));
        Literal maxLiteral = getMaxLiteral(occurences);
        topLevelParts.add(maxLiteral);
        SolutionsPartition partition = getSolutionsPartition(maxLiteral.getLiteral(), solutions);
        topLevelParts.add(new Or(getPartialNode(partition.affectedSolutions), getPartialNode(partition.unaffectedSolutions)));
        return new And(topLevelParts);
    }

    private Map<Integer, Integer> getOccurenceMap(List<Product> solutions) {
        Map<Integer, Integer> occurences = new HashMap<>();
        for (Product solution : solutions) {
            for (Integer literal : solution.literals) {
                occurences.put(literal, occurences.containsKey(literal) ? occurences.get(literal) + 1 : 1);
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
    private List<Node> getLiteralsAppearingInAll(Map<Integer, Integer> occurences, int numberOf) {
        List<Node> indicesOfLiteralsAppearingInAll = new ArrayList<>();
        for (Integer key : occurences.keySet()) { // optimize later
            if (occurences.get(key) == numberOf) {
                indicesOfLiteralsAppearingInAll.add(new Literal(key, idLookUp.get(key)));
                occurences.remove(key);
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
            if (solution.literals.contains(splitLiteral)) {
                solution.literals.remove(splitLiteral);
                affectedSolutions.add(solution);
            } else {
                unaffectedSolutions.add(solution);
            }
        }
        return new SolutionsPartition(affectedSolutions, unaffectedSolutions);
    }

}
