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
import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a chromosome, an individual within FLECO's population.
 *
 * @author Manuel Domínguez-Dorado
 */
public class Chromosome {

    private EnumMap<Genes, Alleles> genes;
    private float fitness;
    private final ImplementationGroups implementationGroup;

    private static final Logger logger = LoggerFactory.getLogger(Chromosome.class);

    /**
     * Cached array of all possible alleles. Using this avoids repeated calls to
     * Alleles.values(), which creates a new array on each invocation. This does
     * not alter behavior in any way.
     */
    private static final Alleles[] ALLELES = Alleles.values();

    /**
     * This is the constructor of the class. It creates a new chromosome and set
     * all its chromosomes to the default allele. It also set the implementation
     * group that will apply.
     *
     * @param implementationGroup The implementation group that applies to the
     * asset that is being considered. According to CyberTOMP proposal,
     * depending on the implementation group, the number of genes in the
     * chromosome varies.
     */
    public Chromosome(ImplementationGroups implementationGroup) {
        genes = new EnumMap<>(Genes.class);
        for (Genes gene : Genes.values()) {
            genes.put(gene, Alleles.DLI_0);
        }
        fitness = 0.0f;
        this.implementationGroup = implementationGroup;
    }

    public ImplementationGroups getImplementationGroup() {
        return this.implementationGroup;
    }

    /**
     * This method returns the allele of the specified gene.
     *
     * @param gene the gene whose allele is being requested.
     * @return The allele of the specified gene.
     */
    public Alleles getAllele(Genes gene) {
        return this.genes.get(gene);
    }

    /**
     * This method set the genes and alleles of this chromosome to those
     * specified as a parameter.
     *
     * @param genes The genes and alleles to configure the chromosome.
     */
    public void setGenes(EnumMap<Genes, Alleles> genes) {
        this.genes.clear();
        this.genes.putAll(genes);
    }

    /**
     * This method returns the genes and alleles of this chromosome.
     *
     * @return The genes and alleles of this chromosome.
     */
    public EnumMap<Genes, Alleles> getGenes() {
        return genes;
    }

    /**
     * This method add or update a gene and its respective allele in the
     * chromosome.
     *
     * @param gene The gene to be added or udated.
     * @param allele The allele for the specified gene.
     */
    public void updateAllele(Genes gene, Alleles allele) {
        this.genes.put(gene, allele);
    }

    /**
     * This method assigns a random allele to every gene in the chromosome.
     */
    public void randomizeGenes() {
        for (Genes gene : Genes.values()) {
            if (gene.appliesToIG(implementationGroup)) {
                int randomAllele = ThreadLocalRandom.current().nextInt(0, ALLELES.length);
                genes.put(gene, ALLELES[randomAllele]);
            } else {
                genes.put(gene, Alleles.DLI_0);
            }
        }
    }

    /**
     * This method prints in console a beautified version of the chromosome.
     */
    public void print() {
        EnumMap<Genes, Float> genesValues = new EnumMap<>(Genes.class);
        EnumMap<Categories, Float> categoriesValues = new EnumMap<>(Categories.class);
        EnumMap<Functions, Float> functionsValues = new EnumMap<>(Functions.class);
        Float assetValue = 0.0f;

        float auxFunctionFitness = 0.0f;
        float auxCategoryFitness = 0.0f;
        int num = 0;
        for (Functions f : Functions.values()) {
            if (f.appliesToIG(implementationGroup)) {
                auxFunctionFitness = 0.0f;

                for (Categories c : f.getCategories(implementationGroup)) {
                    auxCategoryFitness = 0.0f;
                    for (Genes g : c.getGenes(implementationGroup)) {
                        // Gene raw value
                        genesValues.put(g, getAllele(g).getDLI());
                        // To compute category fitness
                        num++;
                        auxCategoryFitness += getAllele(g).getDLI() * g.getWeight(implementationGroup);
                    }
                    // Category raw value
                    categoriesValues.put(c, auxCategoryFitness);
                    // To compute Function fitness
                    auxCategoryFitness *= c.getWeight(implementationGroup);
                    if (auxCategoryFitness > c.getWeight(implementationGroup)) {
                        auxCategoryFitness = c.getWeight(implementationGroup);
                    }
                    auxFunctionFitness += auxCategoryFitness;
                }
                // Function raw value
                functionsValues.put(f, auxFunctionFitness);
                // To compute asset fitness
                auxFunctionFitness *= f.getWeight(implementationGroup);
                if (auxFunctionFitness > f.getWeight(implementationGroup)) {
                    auxFunctionFitness = f.getWeight(implementationGroup);
                }
                assetValue += auxFunctionFitness;
            }
        }
        logger.info("\tAsset: " + assetValue);
        for (Functions function : Functions.getFunctionsFor(implementationGroup)) {
            logger.info("\t\t" + function.name() + ": " + functionsValues.get(function));
            for (Categories category : function.getCategories(implementationGroup)) {
                logger.info("\t\t\t" + category.name() + ": " + categoriesValues.get(category));
                for (Genes gene : category.getGenes(implementationGroup)) {
                    logger.info("\t\t\t\t" + gene.name().substring(6) + ": " + getAllele(gene).getDLI());
                }
            }
        }
    }

    /**
     * This method returns the genes of this chromosome as JSON strings to be
     * treated automatically whenever needed.
     *
     * @return the genes of this chromosome as JSON strings.
     */
    public String getGenesAsJSONString() {
        String JSONString = "";
        int genesNum = 0;
        for (Genes gene : genes.keySet()) {
            if (gene.appliesToIG(implementationGroup)) {
                JSONString += "\t\t{\"gene\":\"" + gene.name() + "\",\"allele\":\"" + getAllele(gene).name() + "\"}";
                if (genesNum < (Genes.getGenesFor(implementationGroup).size() - 1)) {
                    JSONString += ",\n";
                } else {
                    JSONString += "\n";
                }
                genesNum++;
            }
        }
        return JSONString;
    }

    /**
     * This method prints in console a plain version of the chromosome showing
     * the value of every gene.
     */
    public void printGenes() {
        EnumMap<Genes, Float> genesValues = new EnumMap<>(Genes.class);
        EnumMap<Categories, Float> categoriesValues = new EnumMap<>(Categories.class);
        EnumMap<Functions, Float> functionsValues = new EnumMap<>(Functions.class);
        Float assetValue = 0.0f;

        float auxFunctionFitness = 0.0f;
        float auxCategoryFitness = 0.0f;
        int num = 0;
        for (Functions f : Functions.values()) {
            if (f.appliesToIG(implementationGroup)) {
                auxFunctionFitness = 0.0f;

                for (Categories c : f.getCategories(implementationGroup)) {
                    auxCategoryFitness = 0.0f;
                    for (Genes g : c.getGenes(implementationGroup)) {
                        // Gene raw value
                        genesValues.put(g, getAllele(g).getDLI());
                        // To compute category fitness
                        num++;
                        auxCategoryFitness += getAllele(g).getDLI() * g.getWeight(implementationGroup);
                    }
                    // Category raw value
                    if (auxCategoryFitness > 1.0f) {
                        auxCategoryFitness = 1.0f;
                    }
                    categoriesValues.put(c, auxCategoryFitness);
                    // To compute Function fitness
                    auxCategoryFitness *= c.getWeight(implementationGroup);
                    if (auxCategoryFitness > c.getWeight(implementationGroup)) {
                        auxCategoryFitness = c.getWeight(implementationGroup);
                    }
                    auxFunctionFitness += auxCategoryFitness;
                }
                // Function raw value
                if (auxFunctionFitness > 1.0f) {
                    auxFunctionFitness = 1.0f;
                }
                functionsValues.put(f, auxFunctionFitness);
                // To compute asset fitness
                auxFunctionFitness *= f.getWeight(implementationGroup);
                if (auxFunctionFitness > f.getWeight(implementationGroup)) {
                    auxFunctionFitness = f.getWeight(implementationGroup);
                }
                assetValue += auxFunctionFitness;
            }
        }
        for (Functions function : Functions.getFunctionsFor(implementationGroup)) {
            for (Categories category : function.getCategories(implementationGroup)) {
                for (Genes gene : category.getGenes(implementationGroup)) {
                    logger.info(gene.name() + "#" + getAllele(gene).getDLI());
                }
            }
        }
    }

    /**
     * This method prints in console a beautified version of the chromosome.It
     * includes information to compare the chromosome to a previous initial
     * state.
     *
     * @param initialStatus A chromosome representing an initial cybersecurity
     * status the current chromosome is going to be compared to.
     */
    public void print(Chromosome initialStatus) {
        EnumMap<Genes, Float> genesValues = new EnumMap<>(Genes.class);
        EnumMap<Categories, Float> categoriesValues = new EnumMap<>(Categories.class);
        EnumMap<Functions, Float> functionsValues = new EnumMap<>(Functions.class);
        Float assetValue = 0.0f;

        float auxFunctionFitness = 0.0f;
        float auxCategoryFitness = 0.0f;
        int num = 0;
        for (Functions f : Functions.values()) {
            if (f.appliesToIG(implementationGroup)) {
                auxFunctionFitness = 0.0f;

                for (Categories c : f.getCategories(implementationGroup)) {
                    auxCategoryFitness = 0.0f;
                    for (Genes g : c.getGenes(implementationGroup)) {
                        // Gene raw value
                        genesValues.put(g, getAllele(g).getDLI());
                        // To compute category fitness
                        num++;
                        auxCategoryFitness += getAllele(g).getDLI() * g.getWeight(implementationGroup);
                    }
                    // Category raw value
                    if (auxCategoryFitness > 1.0f) {
                        auxCategoryFitness = 1.0f;
                    }
                    categoriesValues.put(c, auxCategoryFitness);
                    // To compute Function fitness
                    auxCategoryFitness *= c.getWeight(implementationGroup);
                    if (auxCategoryFitness > c.getWeight(implementationGroup)) {
                        auxCategoryFitness = c.getWeight(implementationGroup);
                    }
                    auxFunctionFitness += auxCategoryFitness;
                }
                // Function raw value
                if (auxFunctionFitness > 1.0f) {
                    auxFunctionFitness = 1.0f;
                }
                functionsValues.put(f, auxFunctionFitness);
                // To compute asset fitness
                auxFunctionFitness *= f.getWeight(implementationGroup);
                if (auxFunctionFitness > f.getWeight(implementationGroup)) {
                    auxFunctionFitness = f.getWeight(implementationGroup);
                }
                assetValue += auxFunctionFitness;
            }
        }
        logger.info("\tAsset: " + assetValue);
        for (Functions function : Functions.getFunctionsFor(implementationGroup)) {
            logger.info("\t\t" + function.name() + ": " + functionsValues.get(function));
            for (Categories category : function.getCategories(implementationGroup)) {
                logger.info("\t\t\t" + category.name() + ": " + categoriesValues.get(category));
                for (Genes gene : category.getGenes(implementationGroup)) {
                    logger.info("\t\t\t\t" + gene.name().substring(6) + ": " + getAllele(gene).getDLI() + getSimilarityText(initialStatus.getAllele(gene), getAllele(gene)));
                }
            }
        }
    }

    /**
     * This method returns a text explaining the evolution between two alleles
     * specified as parameters.
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
     * This method returns the fitness value.
     *
     * @return the chromosome's fitness.
     */
    public float getFitness() {
        return fitness;
    }

    /**
     * This method computes the chromosome's fitness.
     *
     * @param initialStatus A chromosome representing an initial cybersecurity
     * status.
     * @param strategicConstraints A ser of strategic constraints to be takein
     * into consideration when optimizing the three optimization objectives.
     */
    public void computeFitness(Chromosome initialStatus, StrategicConstraints strategicConstraints) {
        fitness = computeFitnessConstraintsCoverage(strategicConstraints);
    }

    /**
     * This method returns the fitness related to the optimization objective 1
     * (compliance with the defined strategic constraints).
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
     */
    private float computeFitnessConstraintsCoverage(StrategicConstraints strategicConstraints) {

        float numberOfConstraints = strategicConstraints.numberOfConstraints();
        float satisfiedConstraints = 0.0f;

        // --- Compute raw metrics ---
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

        // --- Evaluate constraints on genes ---
        for (Genes gene : genesValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(gene)) {
                float value = genesValues.get(gene);
                Constraint constraint = strategicConstraints.getConstraint(gene);
                satisfiedConstraints += evaluateConstraint(value, constraint);
            }
        }

        // --- Evaluate constraints on categories ---
        for (Categories category : categoriesValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(category)) {
                float value = categoriesValues.get(category);
                Constraint constraint = strategicConstraints.getConstraint(category);
                satisfiedConstraints += evaluateConstraint(value, constraint);
            }
        }

        // --- Evaluate constraints on functions ---
        for (Functions function : functionsValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(function)) {
                float value = functionsValues.get(function);
                Constraint constraint = strategicConstraints.getConstraint(function);
                satisfiedConstraints += evaluateConstraint(value, constraint);
            }
        }

        // --- Evaluate global asset constraint ---
        if (strategicConstraints.hasDefinedConstraint()) {
            float value = assetValue;
            Constraint constraint = strategicConstraints.getConstraint();
            satisfiedConstraints += evaluateConstraint(value, constraint);
        }

        // --- Normalize result ---
        if (numberOfConstraints == 0.0f) {
            return 1.0f;
        }

        return satisfiedConstraints / numberOfConstraints;
    }

    /**
     * Evaluates how well a given value satisfies a specific constraint. Returns
     * a score between 0.0 and 1.0 (worst - best).
     *
     * @param value The computed value (gene/category/function/asset).
     * @param constraint The strategic constraint to evaluate.
     * @return A satisfaction score between 0.0 and 1.0.
     */
    private float evaluateConstraint(float value, Constraint constraint) {
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
