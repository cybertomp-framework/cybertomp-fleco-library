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
package com.manolodominguez.fleco.genetics;

import com.manolodominguez.fleco.strategicconstraints.Constraint;
import com.manolodominguez.fleco.strategicconstraints.StrategicConstraints;
import com.manolodominguez.fleco.uleo.Categories;
import com.manolodominguez.fleco.uleo.Functions;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a chromosome, an individual within FLECO's population.
 *
 * It stores the mapping between genes and alleles, the fitness value for
 * optimization objective 1, and the implementation group that determines which
 * genes, categories and functions apply.
 *
 * @author Manuel Domínguez-Dorado
 */
public class Chromosome {

    /**
     * Mapping between each gene and its assigned allele.
     */
    private EnumMap<Genes, Alleles> genes;

    /**
     * Fitness value for optimization objective 1 (strategic constraints
     * coverage).
     */
    private float fitness;

    /**
     * Implementation group that applies to this chromosome.
     */
    private final ImplementationGroups implementationGroup;

    /**
     * Logger used for debugging and structured chromosome output.
     */
    private static final Logger logger = LoggerFactory.getLogger(Chromosome.class);

    /**
     * Cached array of all possible alleles. Using this avoids repeated calls to
     * Alleles.values(), which creates a new array on each invocation. This does
     * not alter behavior in any way.
     */
    private static final Alleles[] cachedAlleles = Alleles.values();

    private static final SecureRandom sRandom = new SecureRandom();

    /**
     * Immutable container for all intermediate metrics derived from the
     * chromosome: - raw gene values - raw category values - raw function values
     * - aggregated asset value
     *
     * This class has no behavior and is only used to avoid code duplication.
     */
    private static final class ChromosomeMetrics {

        final EnumMap<Genes, Float> genesValues;
        final EnumMap<Categories, Float> categoriesValues;
        final EnumMap<Functions, Float> functionsValues;
        final float assetValue;

        ChromosomeMetrics(EnumMap<Genes, Float> genesValues,
                EnumMap<Categories, Float> categoriesValues,
                EnumMap<Functions, Float> functionsValues,
                float assetValue) {
            this.genesValues = genesValues;
            this.categoriesValues = categoriesValues;
            this.functionsValues = functionsValues;
            this.assetValue = assetValue;
        }
    }

    /**
     * Constructor. Creates a new chromosome and sets all its genes to the
     * default allele (DLI_0). It also sets the implementation group that will
     * apply.
     *
     * @param implementationGroup The implementation group that applies to the
     * asset that is being considered.
     * @throws IllegalArgumentException if implementationGroup is null.
     */
    public Chromosome(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Null implementation group passed to Chromosome constructor.");
            throw new IllegalArgumentException("Implementation group cannot be null.");
        }
        this.implementationGroup = implementationGroup;
        this.genes = new EnumMap<>(Genes.class);
        for (Genes gene : Genes.values()) {
            genes.put(gene, Alleles.DLI_0);
        }
        this.fitness = 0.0f;
    }

    /**
     * Returns the implementation group associated with this chromosome.
     *
     * @return the implementation group.
     */
    public ImplementationGroups getImplementationGroup() {
        return this.implementationGroup;
    }

    /**
     * Returns the allele of the specified gene.
     *
     * @param gene the gene whose allele is being requested.
     * @return The allele of the specified gene.
     * @throws IllegalArgumentException if gene is null.
     */
    public Alleles getAllele(Genes gene) {
        if (gene == null) {
            logger.error("Null gene passed to getAllele().");
            throw new IllegalArgumentException("Gene cannot be null.");
        }
        return this.genes.get(gene);
    }

    /**
     * Sets the genes and alleles of this chromosome to those specified as a
     * parameter. Partial maps are allowed (not all genes need to be present),
     * but: - The map itself cannot be null. - No allele value can be null.
     *
     * @param genes The genes and alleles to configure the chromosome.
     * @throws IllegalArgumentException if genes is null or contains null
     * alleles.
     */
    public void setGenes(EnumMap<Genes, Alleles> genes) {
        if (genes == null) {
            logger.error("Null genes map passed to setGenes().");
            throw new IllegalArgumentException("Genes map cannot be null.");
        }
        for (Map.Entry<Genes, Alleles> entry : genes.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                logger.error("Null key or value found in genes map passed to setGenes().");
                throw new IllegalArgumentException("Genes map cannot contain null keys or null alleles.");
            }
        }
        this.genes.clear();
        this.genes.putAll(genes);
    }

    /**
     * Returns the genes and alleles of this chromosome.
     *
     * @return The genes and alleles of this chromosome.
     */
    public EnumMap<Genes, Alleles> getGenes() {
        return genes;
    }

    /**
     * Adds or updates a gene and its respective allele in the chromosome.
     *
     * @param gene The gene to be added or updated.
     * @param allele The allele for the specified gene.
     * @throws IllegalArgumentException if gene or allele is null.
     */
    public void updateAllele(Genes gene, Alleles allele) {
        if (gene == null || allele == null) {
            logger.error("Null gene or allele passed to updateAllele().");
            throw new IllegalArgumentException("Gene and allele cannot be null.");
        }
        this.genes.put(gene, allele);
    }

    /**
     * Assigns a random allele to every gene in the chromosome. Genes that do
     * not apply to the implementation group are forced to DLI_0.
     */
    public void randomizeGenes() {
        for (Genes gene : Genes.values()) {
            if (gene.appliesToIG(implementationGroup)) {
                int randomAllele = sRandom.nextInt(cachedAlleles.length);
                genes.put(gene, cachedAlleles[randomAllele]);
            } else {
                genes.put(gene, Alleles.DLI_0);
            }
        }
    }

    /**
     * Computes all intermediate metrics derived from the chromosome: - raw gene
     * values - raw category values - raw function values - aggregated asset
     * value
     *
     * This method does not modify the chromosome and has no side effects.
     *
     * @return a ChromosomeMetrics object containing all computed values.
     */
    private ChromosomeMetrics computeMetrics() {
        EnumMap<Genes, Float> genesValues = new EnumMap<>(Genes.class);
        EnumMap<Categories, Float> categoriesValues = new EnumMap<>(Categories.class);
        EnumMap<Functions, Float> functionsValues = new EnumMap<>(Functions.class);
        float assetValue = 0.0f;

        float auxFunctionFitness;
        float auxCategoryFitness;

        for (Functions function : Functions.values()) {
            if (function.appliesToIG(implementationGroup)) {
                auxFunctionFitness = 0.0f;

                for (Categories category : function.getCategories(implementationGroup)) {
                    auxCategoryFitness = 0.0f;

                    for (Genes gene : category.getGenes(implementationGroup)) {
                        float geneValue = getAllele(gene).getDLI();
                        genesValues.put(gene, geneValue);
                        auxCategoryFitness += geneValue * gene.getWeight(implementationGroup);
                    }

                    categoriesValues.put(category, auxCategoryFitness);

                    auxCategoryFitness *= category.getWeight(implementationGroup);
                    if (auxCategoryFitness > category.getWeight(implementationGroup)) {
                        auxCategoryFitness = category.getWeight(implementationGroup);
                    }

                    auxFunctionFitness += auxCategoryFitness;
                }

                functionsValues.put(function, auxFunctionFitness);

                auxFunctionFitness *= function.getWeight(implementationGroup);
                if (auxFunctionFitness > function.getWeight(implementationGroup)) {
                    auxFunctionFitness = function.getWeight(implementationGroup);
                }

                assetValue += auxFunctionFitness;
            }
        }

        return new ChromosomeMetrics(genesValues, categoriesValues, functionsValues, assetValue);
    }

    /**
     * Prints in console a beautified version of the chromosome.
     */
    public void print() {
        ChromosomeMetrics m = computeMetrics();

        logger.info("\tAsset: " + m.assetValue);
        for (Functions function : Functions.getFunctionsFor(implementationGroup)) {
            logger.info("\t\t" + function.name() + ": " + m.functionsValues.get(function));
            for (Categories category : function.getCategories(implementationGroup)) {
                logger.info("\t\t\t" + category.name() + ": " + m.categoriesValues.get(category));
                for (Genes gene : category.getGenes(implementationGroup)) {
                    logger.info("\t\t\t\t" + gene.name().substring(6) + ": " + getAllele(gene).getDLI());
                }
            }
        }
    }

    /**
     * Returns the genes of this chromosome as JSON strings to be treated
     * automatically whenever needed.
     *
     * @return the genes of this chromosome as JSON strings.
     */
    public String getGenesAsJSONString() {
        StringBuilder sb = new StringBuilder();
        int genesNum = 0;
        int totalGenes = Genes.getGenesFor(implementationGroup).size();

        for (Genes gene : genes.keySet()) {
            if (gene.appliesToIG(implementationGroup)) {
                sb.append("\t\t{\"gene\":\"")
                        .append(gene.name())
                        .append("\",\"allele\":\"")
                        .append(getAllele(gene).name())
                        .append("\"}");
                if (genesNum < (totalGenes - 1)) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
                genesNum++;
            }
        }
        return sb.toString();
    }

    /**
     * Prints in console a plain version of the chromosome showing the value of
     * every gene.
     */
    public void printGenes() {
        ChromosomeMetrics m = computeMetrics();

        for (Functions function : Functions.getFunctionsFor(implementationGroup)) {
            for (Categories category : function.getCategories(implementationGroup)) {
                for (Genes gene : category.getGenes(implementationGroup)) {
                    logger.info(gene.name() + "#" + getAllele(gene).getDLI());
                }
            }
        }
    }

    /**
     * Prints in console a beautified version of the chromosome. It includes
     * information to compare the chromosome to a previous initial state.
     *
     * @param initialStatus A chromosome representing an initial cybersecurity
     * status the current chromosome is going to be compared to.
     * @throws IllegalArgumentException if initialStatus is null.
     */
    public void print(Chromosome initialStatus) {
        if (initialStatus == null) {
            logger.error("Null initialStatus passed to print(Chromosome).");
            throw new IllegalArgumentException("initialStatus cannot be null.");
        }

        ChromosomeMetrics m = computeMetrics();

        logger.info("\tAsset: " + m.assetValue);
        for (Functions function : Functions.getFunctionsFor(implementationGroup)) {
            logger.info("\t\t" + function.name() + ": " + m.functionsValues.get(function));
            for (Categories category : function.getCategories(implementationGroup)) {
                logger.info("\t\t\t" + category.name() + ": " + m.categoriesValues.get(category));
                for (Genes gene : category.getGenes(implementationGroup)) {
                    logger.info(
                            "\t\t\t\t" + gene.name().substring(6) + ": "
                            + getAllele(gene).getDLI()
                            + getSimilarityText(initialStatus.getAllele(gene), getAllele(gene))
                    );
                }
            }
        }
    }

    /**
     * Returns a text explaining the evolution between two alleles specified as
     * parameters.
     *
     * @param initialAllele The initial allele to be compared.
     * @param currentAllele The current/final allele to be compared.
     * @return A text explaining the evolution between two alleles specified as
     * parameters.
     */
    private String getSimilarityText(Alleles initialAllele, Alleles currentAllele) {
        if (initialAllele == currentAllele) {
            return " (UNCHANGED)";
        }
        return " (PREVIOUSLY " + initialAllele.getDLI() + ")";
    }

    /**
     * Returns the fitness value.
     *
     * @return the chromosome's fitness.
     */
    public float getFitness() {
        return fitness;
    }

    /**
     * Computes the chromosome's fitness.
     *
     * @param initialStatus A chromosome representing an initial cybersecurity
     * status.
     * @param strategicConstraints A set of strategic constraints to be taken
     * into consideration when optimizing the three optimization objectives.
     * @throws IllegalArgumentException if initialStatus or strategicConstraints
     * is null.
     */
    public void computeFitness(Chromosome initialStatus, StrategicConstraints strategicConstraints) {
        if (initialStatus == null || strategicConstraints == null) {
            logger.error("Null parameters passed to computeFitness().");
            throw new IllegalArgumentException("initialStatus and strategicConstraints cannot be null.");
        }
        fitness = computeFitnessConstraintsCoverage(strategicConstraints);
    }

    /**
     * Returns the fitness related to the optimization objective 1 (compliance
     * with the defined strategic constraints).
     *
     * @return the fitness related to the optimization objective 1 (compliance
     * with the defined strategic constraints).
     */
    public float getFitnessConstraintsCoverage() {
        return this.fitness;
    }

    /**
     * Computes the fitness related to optimization objective 1: compliance with
     * the defined strategic constraints.
     *
     * The method: 1. Computes raw values for genes, categories, functions, and
     * the asset. 2. Evaluates each defined constraint
     * (gene/category/function/asset). 3. Assigns a score of 1.0 if fully
     * satisfied, or a linear partial score otherwise. 4. Returns the normalized
     * ratio of satisfied constraints.
     *
     * @param strategicConstraints The set of strategic constraints to evaluate.
     * @return A normalized fitness value between 0.0 and 1.0.
     * @throws IllegalArgumentException if strategicConstraints is null.
     */
    private float computeFitnessConstraintsCoverage(StrategicConstraints strategicConstraints) {

        if (strategicConstraints == null) {
            logger.error("Null strategicConstraints passed to computeFitnessConstraintsCoverage().");
            throw new IllegalArgumentException("strategicConstraints cannot be null.");
        }

        float numberOfConstraints = strategicConstraints.numberOfConstraints();
        float satisfiedConstraints = 0.0f;

        ChromosomeMetrics m = computeMetrics();
        EnumMap<Genes, Float> genesValues = m.genesValues;
        EnumMap<Categories, Float> categoriesValues = m.categoriesValues;
        EnumMap<Functions, Float> functionsValues = m.functionsValues;
        float assetValue = m.assetValue;

        // Evaluate constraints on genes
        for (Genes gene : genesValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(gene)) {
                float value = genesValues.get(gene);
                Constraint constraint = strategicConstraints.getConstraint(gene);
                satisfiedConstraints += evaluateConstraint(value, constraint);
            }
        }

        // Evaluate constraints on categories
        for (Categories category : categoriesValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(category)) {
                float value = categoriesValues.get(category);
                Constraint constraint = strategicConstraints.getConstraint(category);
                satisfiedConstraints += evaluateConstraint(value, constraint);
            }
        }

        // Evaluate constraints on functions
        for (Functions function : functionsValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(function)) {
                float value = functionsValues.get(function);
                Constraint constraint = strategicConstraints.getConstraint(function);
                satisfiedConstraints += evaluateConstraint(value, constraint);
            }
        }

        // Evaluate global asset constraint
        if (strategicConstraints.hasDefinedConstraint()) {
            float value = assetValue;
            Constraint constraint = strategicConstraints.getConstraint();
            satisfiedConstraints += evaluateConstraint(value, constraint);
        }

        if (numberOfConstraints == 0.0f) {
            return 1.0f;
        }

        return satisfiedConstraints / numberOfConstraints;
    }

    /**
     * Evaluates how well a given value satisfies a specific constraint. Returns
     * a score between 0.0 and 1.0 (worst - best), preserving the original
     * piecewise-linear behavior.
     *
     * @param value The computed value (gene/category/function/asset).
     * @param constraint The strategic constraint to evaluate.
     * @return A satisfaction score between 0.0 and 1.0.
     * @throws IllegalArgumentException if constraint is null.
     */
    private float evaluateConstraint(float value, Constraint constraint) {
        if (constraint == null) {
            logger.error("Null constraint passed to evaluateConstraint().");
            throw new IllegalArgumentException("constraint cannot be null.");
        }

        float threshold = constraint.getThreshold();

        switch (constraint.getComparisonOperator()) {

            case LESS:
                if (value < threshold) {
                    return 1.0f;
                }
                if (value == 1.0f && threshold == 1.0f) {
                    return 0.99f;
                }
                return (-0.99f / (1.0f - threshold)) * value
                        + (0.99f - (-0.99f / (1.0f - threshold)) * threshold);

            case LESS_OR_EQUAL:
                if (value <= threshold) {
                    return 1.0f;
                }
                return (-1.0f / (1.0f - threshold)) * value
                        + (1.0f - (-1.0f / (1.0f - threshold)) * threshold);

            case EQUAL:
                if (value == threshold) {
                    return 1.0f;
                }
                if (value > threshold) {
                    return (-1.0f / (1.0f - threshold)) * value
                            + (1.0f - (-1.0f / (1.0f - threshold)) * threshold);
                }
                return value / threshold;

            case GREATER:
                if (value > threshold) {
                    return 1.0f;
                }
                if (value == 0.0f && threshold == 0.0f) {
                    return 0.99f;
                }
                return (0.99f * value) / threshold;

            case GREATER_OR_EQUAL:
                if (value >= threshold) {
                    return 1.0f;
                }
                return value / threshold;

            default:
                return 0.0f;
        }
    }
}