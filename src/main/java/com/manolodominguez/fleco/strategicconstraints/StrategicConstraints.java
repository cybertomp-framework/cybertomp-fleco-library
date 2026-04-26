/* 
 *******************************************************************************
 * CyberTOMP® is a cybersecurity framework that helps organizations manage and 
 * assess security at a tactical and operational level by focusing on business 
 * assets. It provides structured processes, metrics, and roles to align people,
 * technology, and supply chains, enabling informed decision-making based on 
 * asset criticality. Compatible with standards like ISO 27001 and NIST, it also
 * incorporates practical tools and optimization techniques to deliver a 
 * cohesive, measurable, and efficient approach to cybersecurity.
 * 
 * Within CyberTOMP®, FLECO (Fast, Lightweight, and Efficient Cybersecurity 
 * Optimization) is an adaptive, constrained genetic algorithm designed to 
 * support asset cybersecurity teams in decision-making throughout the 
 * application of the CyberTOMP® framework. The cybertomp-fleco-library provides
 * FLECO as a dynamic Java library.
 *
 * Visit https://cybertomp.org to learn more about the CyberTOMP® framework, 
 * the collaborative project behind it, its components, and its research 
 * foundations, all of which continuously evolve based on empirical evidence and
 * solid, verifiable principles.
 *
 *******************************************************************************
 * Copyright (C) Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com.
 * 
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see 
 * https://www.gnu.org/licenses/lgpl-3.0.en.html.
 *******************************************************************************
 */
package com.manolodominguez.fleco.strategicconstraints;

import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.genetics.Alleles;
import com.manolodominguez.fleco.uleo.Categories;
import com.manolodominguez.fleco.uleo.Functions;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import java.util.EnumMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the set of strategic cybersecurity constraints used by the
 * CyberTOMP Framework to guide the evolution of the FLECO genetic algorithm.
 *
 * <p>
 * Constraints can be defined at gene, category, function or asset level and are
 * applied only when compatible with the asset's Implementation Group (IG).</p>
 *
 * <p>
 * This class preserves the original public contract and behavior. Parameter
 * checks have been added: when a method receives multiple parameters that
 * cannot be null they are validated in a single if-statement that logs an error
 * and throws {@link IllegalArgumentException}. Single-parameter null checks log
 * an error and throw {@link NullPointerException}.</p>
 *
 * @author Manuel Domínguez-Dorado
 */
public class StrategicConstraints {

    private final EnumMap<Genes, Constraint> geneConstraints;
    private final EnumMap<Categories, Constraint> categoryConstraints;
    private final EnumMap<Functions, Constraint> functionConstraints;
    private Constraint assetConstraint;
    private final ImplementationGroups implementationGroup;

    private final Logger logger = LoggerFactory.getLogger(StrategicConstraints.class);

    /**
     * Constructor. Creates a new, empty instance.
     *
     * @param implementationGroup the implementation group that applies due to
     * the criticality of the asset being considered.
     * @throws NullPointerException if implementationGroup is null
     */
    public StrategicConstraints(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Parameter 'implementationGroup' must not be null. implementationGroup={}", implementationGroup);
            throw new NullPointerException("implementationGroup must not be null");
        }
        this.geneConstraints = new EnumMap<>(Genes.class);
        this.categoryConstraints = new EnumMap<>(Categories.class);
        this.functionConstraints = new EnumMap<>(Functions.class);
        this.implementationGroup = implementationGroup;
        this.assetConstraint = null;
    }

    /**
     * Removes all strategic constraints at all levels.
     */
    public void removeAll() {
        this.geneConstraints.clear();
        this.categoryConstraints.clear();
        this.functionConstraints.clear();
        this.assetConstraint = null;
    }

    /**
     * Adds a constraint for a gene (expected outcome).
     *
     * <p>
     * Compact null-check for multiple parameters: logs and throws
     * {@link IllegalArgumentException} when either parameter is null.</p>
     *
     * @param gene the gene
     * @param constraint the constraint
     * @throws IllegalArgumentException if gene or constraint is null
     */
    public void addConstraint(Genes gene, Constraint constraint) {
        if (gene == null || constraint == null) {
            logger.error("Null gene or constraint passed to addConstraint(Genes, Constraint). gene={}, constraint={}", gene, constraint);
            throw new IllegalArgumentException("Gene and constraint cannot be null.");
        }
        if (gene.appliesToIG(implementationGroup)) {
            geneConstraints.put(gene, constraint);
        } else {
            logger.debug("Gene {} does not apply to IG {}; constraint ignored.", gene, implementationGroup);
        }
    }

    /**
     * Adds a constraint for a category.
     *
     * <p>
     * Compact null-check for multiple parameters: logs and throws
     * {@link IllegalArgumentException} when either parameter is null.</p>
     *
     * @param category the category
     * @param constraint the constraint
     * @throws IllegalArgumentException if category or constraint is null
     */
    public void addConstraint(Categories category, Constraint constraint) {
        if (category == null || constraint == null) {
            logger.error("Null category or constraint passed to addConstraint(Categories, Constraint). category={}, constraint={}", category, constraint);
            throw new IllegalArgumentException("Category and constraint cannot be null.");
        }
        if (category.appliesToIG(implementationGroup)) {
            categoryConstraints.put(category, constraint);
        } else {
            logger.debug("Category {} does not apply to IG {}; constraint ignored.", category, implementationGroup);
        }
    }

    /**
     * Adds a constraint for a function.
     *
     * <p>
     * Compact null-check for multiple parameters: logs and throws
     * {@link IllegalArgumentException} when either parameter is null.</p>
     *
     * @param function the function
     * @param constraint the constraint
     * @throws IllegalArgumentException if function or constraint is null
     */
    public void addConstraint(Functions function, Constraint constraint) {
        if (function == null || constraint == null) {
            logger.error("Null function or constraint passed to addConstraint(Functions, Constraint). function={}, constraint={}", function, constraint);
            throw new IllegalArgumentException("Function and constraint cannot be null.");
        }
        if (function.appliesToIG(implementationGroup)) {
            functionConstraints.put(function, constraint);
        } else {
            logger.debug("Function {} does not apply to IG {}; constraint ignored.", function, implementationGroup);
        }
    }

    /**
     * Sets a high-level (asset) constraint. Passing null removes the asset
     * constraint.
     *
     * @param constraint the asset constraint or null to remove
     */
    public void addConstraint(Constraint constraint) {
        if (constraint == null) {
            logger.info("Setting asset constraint to null (removal).");
        } else {
            logger.debug("Setting asset constraint: operator={}, value={}", constraint.getComparisonOperator(), constraint.getThreshold());
        }
        this.assetConstraint = constraint;
    }

    /**
     * Returns whether a constraint is defined for the specified gene.
     *
     * @param gene the gene
     * @return true if defined
     */
    public boolean hasDefinedConstraint(Genes gene) {
        return gene != null && geneConstraints.containsKey(gene);
    }

    /**
     * Returns whether a constraint is defined for the specified category.
     *
     * @param category the category
     * @return true if defined
     */
    public boolean hasDefinedConstraint(Categories category) {
        return category != null && categoryConstraints.containsKey(category);
    }

    /**
     * Returns whether a constraint is defined for the specified function.
     *
     * @param function the function
     * @return true if defined
     */
    public boolean hasDefinedConstraint(Functions function) {
        return function != null && functionConstraints.containsKey(function);
    }

    /**
     * Returns whether an asset-level constraint is defined.
     *
     * @return true if defined
     */
    public boolean hasDefinedConstraint() {
        return assetConstraint != null;
    }

    /**
     * Returns the constraint for a gene.
     *
     * @param gene the gene
     * @return the constraint or null
     */
    public Constraint getConstraint(Genes gene) {
        return geneConstraints.get(gene);
    }

    /**
     * Returns the constraint for a category.
     *
     * @param category the category
     * @return the constraint or null
     */
    public Constraint getConstraint(Categories category) {
        return categoryConstraints.get(category);
    }

    /**
     * Returns the constraint for a function.
     *
     * @param function the function
     * @return the constraint or null
     */
    public Constraint getConstraint(Functions function) {
        return functionConstraints.get(function);
    }

    /**
     * Returns the asset-level constraint.
     *
     * @return the asset constraint or null
     */
    public Constraint getConstraint() {
        return assetConstraint;
    }

    /**
     * Removes the asset-level constraint.
     */
    public void removeConstraint() {
        this.assetConstraint = null;
    }

    /**
     * Removes the constraint for a function. Null parameter is ignored with a
     * warning.
     *
     * @param function the function
     */
    public void removeConstraint(Functions function) {
        if (function == null) {
            logger.warn("removeConstraint(function) called with null; no action taken.");
            return;
        }
        functionConstraints.remove(function);
    }

    /**
     * Removes the constraint for a category. Null parameter is ignored with a
     * warning.
     *
     * @param category the category
     */
    public void removeConstraint(Categories category) {
        if (category == null) {
            logger.warn("removeConstraint(category) called with null; no action taken.");
            return;
        }
        categoryConstraints.remove(category);
    }

    /**
     * Removes the constraint for a gene. Null parameter is ignored with a
     * warning.
     *
     * @param gene the gene
     */
    public void removeConstraint(Genes gene) {
        if (gene == null) {
            logger.warn("removeConstraint(gene) called with null; no action taken.");
            return;
        }
        geneConstraints.remove(gene);
    }

    /**
     * Generates pre-candidate chromosomes based on defined constraints and the
     * provided initial status. The returned list always contains the
     * initialStatus as the first element.
     *
     * <p>
     * Behavior preserved: candidates are created according to the same
     * comparison operators and thresholds as the original implementation.
     * Candidates are added only when at least one allele has been modified to
     * satisfy constraints.</p>
     *
     * @param initialStatus the initial chromosome (must not be null)
     * @return list of candidate chromosomes (thread-safe)
     * @throws NullPointerException if initialStatus is null
     */
    public CopyOnWriteArrayList<Chromosome> generatePrecandidatesBasedOn(Chromosome initialStatus) {
        if (initialStatus == null) {
            logger.error("Parameter 'initialStatus' must not be null. initialStatus={}", initialStatus);
            throw new NullPointerException("initialStatus must not be null");
        }

        CopyOnWriteArrayList<Chromosome> candidateChromosomes = new CopyOnWriteArrayList<>();
        candidateChromosomes.add(initialStatus);

        // Gene-level constraints
        if (!geneConstraints.isEmpty()) {
            Chromosome candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            boolean created = false;
            for (Genes gene : geneConstraints.keySet()) {
                Constraint c = geneConstraints.get(gene);
                if (c == null) {
                    logger.debug("Null constraint for gene {}; skipping.", gene);
                    continue;
                }
                switch (c.getComparisonOperator()) {
                    case LESS:
                        if (Alleles.getLesser(c.getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getLesser(c.getThreshold()));
                            created = true;
                        }
                        break;
                    case LESS_OR_EQUAL:
                        if (Alleles.getLesserOrEqual(c.getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getLesserOrEqual(c.getThreshold()));
                            created = true;
                        }
                        break;
                    case EQUAL:
                        if (Alleles.getEqual(c.getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getEqual(c.getThreshold()));
                            created = true;
                        }
                        break;
                    case GREATER:
                        if (Alleles.getGreater(c.getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getGreater(c.getThreshold()));
                            created = true;
                        }
                        break;
                    case GREATER_OR_EQUAL:
                        if (Alleles.getGreaterOrEqual(c.getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getGreaterOrEqual(c.getThreshold()));
                            created = true;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }

        // Category-level constraints
        if (!categoryConstraints.isEmpty()) {
            Chromosome candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            boolean created = false;
            for (Categories category : categoryConstraints.keySet()) {
                Constraint c = categoryConstraints.get(category);
                if (c == null) {
                    logger.debug("Null constraint for category {}; skipping.", category);
                    continue;
                }
                if (!category.appliesToIG(implementationGroup)) {
                    logger.debug("Category {} does not apply to IG {}; skipping.", category, implementationGroup);
                    continue;
                }
                if (c.getComparisonOperator() == ComparisonOperators.EQUAL
                        || c.getComparisonOperator() == ComparisonOperators.GREATER_OR_EQUAL
                        || c.getComparisonOperator() == ComparisonOperators.LESS_OR_EQUAL) {

                    List<Genes> applicableGenes = Genes.getGenesFor(category, implementationGroup);
                    Alleles allele = (c.getThreshold() == Alleles.DLI_0.getDLI()) ? Alleles.DLI_0
                            : (c.getThreshold() == Alleles.DLI_100.getDLI() ? Alleles.DLI_100 : null);

                    if (allele != null && applicableGenes != null) {
                        for (Genes gene : applicableGenes) {
                            candidate.updateAllele(gene, allele);
                            created = true;
                        }
                    }
                }
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }

        // Function-level constraints
        if (!functionConstraints.isEmpty()) {
            Chromosome candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            boolean created = false;
            for (Functions function : functionConstraints.keySet()) {
                Constraint c = functionConstraints.get(function);
                if (c == null) {
                    logger.debug("Null constraint for function {}; skipping.", function);
                    continue;
                }
                if (!function.appliesToIG(implementationGroup)) {
                    logger.debug("Function {} does not apply to IG {}; skipping.", function, implementationGroup);
                    continue;
                }
                if (c.getComparisonOperator() == ComparisonOperators.EQUAL
                        || c.getComparisonOperator() == ComparisonOperators.GREATER_OR_EQUAL
                        || c.getComparisonOperator() == ComparisonOperators.LESS_OR_EQUAL) {

                    List<Genes> applicableGenes = Categories.getGenesFor(function, implementationGroup);
                    Alleles allele = (c.getThreshold() == Alleles.DLI_0.getDLI()) ? Alleles.DLI_0
                            : (c.getThreshold() == Alleles.DLI_100.getDLI() ? Alleles.DLI_100 : null);

                    if (allele != null && applicableGenes != null) {
                        for (Genes gene : applicableGenes) {
                            candidate.updateAllele(gene, allele);
                            created = true;
                        }
                    }
                }
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }

        // Asset-level constraint
        if (assetConstraint != null) {
            Chromosome candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            boolean created = false;
            if (assetConstraint.getComparisonOperator() == ComparisonOperators.EQUAL
                    || assetConstraint.getComparisonOperator() == ComparisonOperators.GREATER_OR_EQUAL
                    || assetConstraint.getComparisonOperator() == ComparisonOperators.LESS_OR_EQUAL) {

                Alleles allele = (assetConstraint.getThreshold() == Alleles.DLI_0.getDLI()) ? Alleles.DLI_0
                        : (assetConstraint.getThreshold() == Alleles.DLI_100.getDLI() ? Alleles.DLI_100 : null);

                if (allele != null) {
                    for (Genes gene : Genes.values()) {
                        if (gene.appliesToIG(implementationGroup)) {
                            candidate.updateAllele(gene, allele);
                            created = true;
                        }
                    }
                }
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }

        return candidateChromosomes;
    }

    /**
     * Returns the number of defined constraints.
     *
     * @return number of constraints
     */
    public int numberOfConstraints() {
        int number = 0;
        number += geneConstraints.size();
        number += categoryConstraints.size();
        number += functionConstraints.size();
        if (assetConstraint != null) {
            number++;
        }
        return number;
    }

    /**
     * Logs the defined constraints classified by level. Avoids NPEs when some
     * constraints are not defined.
     */
    public void print() {
        if (this.assetConstraint != null) {
            logger.info("\tAsset constraint...........: {} {}", this.assetConstraint.getComparisonOperator().name(), this.assetConstraint.getThreshold());
        } else {
            logger.info("\tAsset constraint...........: <none>");
        }
        if (this.functionConstraints.isEmpty()) {
            logger.info("\tFunction constraints........: <none>");
        } else {
            for (Functions function : this.functionConstraints.keySet()) {
                Constraint c = this.functionConstraints.get(function);
                if (c != null) {
                    logger.info("\tFunction constraint........: {} {} {}", function.name(), c.getComparisonOperator().name(), c.getThreshold());
                }
            }
        }
        if (this.categoryConstraints.isEmpty()) {
            logger.info("\tCategory constraints........: <none>");
        } else {
            for (Categories category : this.categoryConstraints.keySet()) {
                Constraint c = this.categoryConstraints.get(category);
                if (c != null) {
                    logger.info("\tCategory constraint........: {} {} {}", category.name(), c.getComparisonOperator().name(), c.getThreshold());
                }
            }
        }
        if (this.geneConstraints.isEmpty()) {
            logger.info("\tExpected outcome constraints: <none>");
        } else {
            for (Genes gene : this.geneConstraints.keySet()) {
                Constraint c = this.geneConstraints.get(gene);
                if (c != null) {
                    logger.info("\tExpected outcome constraint: {} {} {}", gene.name(), c.getComparisonOperator().name(), c.getThreshold());
                }
            }
        }
    }

    /**
     * This method returns the strategic constraints as JSON strings to be
     * treated automatically whenever needed.
     *
     * @return the strategic constraints as JSON strings
     */
    @Deprecated
    public String getConstraintsAsJSONString2() {
        String JSONString = "";
        int totalConstraints = categoryConstraints.size() + functionConstraints.size() + geneConstraints.size();
        if (this.assetConstraint != null) {
            totalConstraints++;
        }
        int constraintsNum = 0;
        if (constraintsNum < totalConstraints) {
            if (assetConstraint != null) {
                JSONString += "\t\t{\"asset\":\"ASSET\",\"operator\":\"" + assetConstraint.getComparisonOperator() + "\",\"value\":" + assetConstraint.getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString += ",\n";
                } else {
                    JSONString += "\n";
                }
                constraintsNum++;
            }
        }
        for (Categories category : categoryConstraints.keySet()) {
            if (constraintsNum < totalConstraints) {
                JSONString += "\t\t{\"category\":\"" + category + "\",\"operator\":\"" + categoryConstraints.get(category).getComparisonOperator() + "\",\"value\":" + categoryConstraints.get(category).getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString += ",\n";
                } else {
                    JSONString += "\n";
                }
                constraintsNum++;
            }
        }
        for (Functions function : functionConstraints.keySet()) {
            if (constraintsNum < totalConstraints) {
                JSONString += "\t\t{\"function\":\"" + function + "\",\"operator\":\"" + functionConstraints.get(function).getComparisonOperator() + "\",\"value\":" + functionConstraints.get(function).getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString += ",\n";
                } else {
                    JSONString += "\n";
                }
                constraintsNum++;
            }
        }
        for (Genes gene : geneConstraints.keySet()) {
            if (constraintsNum < totalConstraints) {
                JSONString += "\t\t{\"gene\":\"" + gene + "\",\"operator\":\"" + geneConstraints.get(gene).getComparisonOperator() + "\",\"value\":" + geneConstraints.get(gene).getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString += ",\n";
                } else {
                    JSONString += "\n";
                }
                constraintsNum++;
            }
        }
        return JSONString;
    }

    /**
     * Returns the constraints as JSON-like entries separated by commas and
     * newlines.
     *
     * @return JSON-like string
     */
    public String getConstraintsAsJSONString() {
        StringJoiner sj = new StringJoiner(",\n", "", "\n");

        if (assetConstraint != null) {
            sj.add("\t\t{\"asset\":\"ASSET\",\"operator\":\"" + assetConstraint.getComparisonOperator() + "\",\"value\":" + assetConstraint.getThreshold() + "}");
        }

        for (Categories category : categoryConstraints.keySet()) {
            Constraint c = categoryConstraints.get(category);
            if (c != null) {
                sj.add("\t\t{\"category\":\"" + category + "\",\"operator\":\"" + c.getComparisonOperator() + "\",\"value\":" + c.getThreshold() + "}");
            }
        }

        for (Functions function : functionConstraints.keySet()) {
            Constraint c = functionConstraints.get(function);
            if (c != null) {
                sj.add("\t\t{\"function\":\"" + function + "\",\"operator\":\"" + c.getComparisonOperator() + "\",\"value\":" + c.getThreshold() + "}");
            }
        }

        for (Genes gene : geneConstraints.keySet()) {
            Constraint c = geneConstraints.get(gene);
            if (c != null) {
                sj.add("\t\t{\"gene\":\"" + gene + "\",\"operator\":\"" + c.getComparisonOperator() + "\",\"value\":" + c.getThreshold() + "}");
            }
        }

        return sj.toString();
    }
}
