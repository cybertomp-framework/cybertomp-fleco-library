/* 
 *******************************************************************************
 * FLECO (Fast, Lightweight, and Efficient Cybersecurity Optimization) (1) 
 * Adaptive, Constrained, and Multi-objective Genetic Algorithm is a genetic 
 * algorithm designed to assist the Asset's Cybersecurity Committee (ACC) in 
 * making decisions during the application of CyberTOMP (2), aimed at managing 
 * comprehensive cybersecurity at both tactical and operational levels.
 *
 * (1) Domínguez-Dorado, M.; Cortés-Polo, D.; Carmona-Murillo, J.; 
 * Rodríguez-Pérez, F.J.; Galeano-Brajones, J. Fast, Lightweight, and Efficient 
 * Cybersecurity Optimization for Tactical–Operational Management. Appl. Sci. 
 * 2023, 13, 6327. https://doi.org/10.3390/app13106327
 *
 * (2) Dominguez-Dorado, M., Carmona-Murillo, J., Cortés-Polo, D., and
 * Rodríguez-Pérez, F. J. (2022). CyberTOMP: A novel systematic framework to
 * manage asset-focused cybersecurity from tactical and operational levels. IEEE
 * Access, 10, 122454-122485. https://doi.org/10.1109/ACCESS.2022.3223440
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
                Alleles[] allelesArray = Alleles.values();
                int randomAllele = ThreadLocalRandom.current().nextInt(0, allelesArray.length);
                genes.put(gene, allelesArray[randomAllele]);
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
     * This method returns the fitness related to the optimization objective 1
     * (compliance with the defined strategic constraints). This method goes
     * constraint by constraint checking whether the constraint is satisfied by
     * the current chromosome. If this is the case, the constraint is considered
     * as satisfied (+1.0). Otherwise, a linear value between 0.0 and 1.0 is
     * asigned depending on the degree of compliance regarding the constraint.
     * Finally a value representing the number of constraints satisfied by this
     * chromosome in relation to the total number of constraints is returned as
     * a normalized value between 0.0 and 1.0.
     *
     * @return the fitness related to the optimization objective 1 (compliance
     * with the defined strategic constraints).
     */
    private float computeFitnessConstraintsCoverage(StrategicConstraints strategicConstraints) {
        float numberOfConstraints = strategicConstraints.numberOfConstraints();
        float satisfiedConstraints = 0.0f;
        EnumMap<Genes, Float> genesValues = new EnumMap<>(Genes.class);
        EnumMap<Categories, Float> categoriesValues = new EnumMap<>(Categories.class);
        EnumMap<Functions, Float> functionsValues = new EnumMap<>(Functions.class);
        Float assetValue = 0.0f;

        float auxFunctionFitness = 0.0f;
        float auxCategoryFitness = 0.0f;
        int counter = 0;
        for (Functions function : Functions.values()) {
            if (function.appliesToIG(implementationGroup)) {
                auxFunctionFitness = 0.0f;

                for (Categories category : function.getCategories(implementationGroup)) {
                    auxCategoryFitness = 0.0f;
                    for (Genes gene : category.getGenes(implementationGroup)) {
                        // Gene raw value
                        genesValues.put(gene, getAllele(gene).getDLI());
                        // To compute category fitness
                        counter++;
                        auxCategoryFitness += getAllele(gene).getDLI() * gene.getWeight(implementationGroup);
                    }
                    // Category raw value
                    categoriesValues.put(category, auxCategoryFitness);
                    // To compute Function fitness
                    auxCategoryFitness *= category.getWeight(implementationGroup);
                    if (auxCategoryFitness > category.getWeight(implementationGroup)) {
                        auxCategoryFitness = category.getWeight(implementationGroup);
                    }
                    auxFunctionFitness += auxCategoryFitness;
                }
                // Function raw value
                functionsValues.put(function, auxFunctionFitness);
                // To compute asset fitness
                auxFunctionFitness *= function.getWeight(implementationGroup);
                if (auxFunctionFitness > function.getWeight(implementationGroup)) {
                    auxFunctionFitness = function.getWeight(implementationGroup);
                }
                assetValue += auxFunctionFitness;
            }
        }

        for (Genes gene : genesValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(gene)) {
                Constraint constraint = strategicConstraints.getConstraint(gene);
                switch (constraint.getComparisonOperator()) {

                    case LESS:
                        if (genesValues.get(gene) < constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if ((genesValues.get(gene) == 1.0f) && (constraint.getThreshold() == 1.0f)) {
                            satisfiedConstraints += 0.99f;
                        } else {
                            satisfiedConstraints += (-0.99f / (1.0f - constraint.getThreshold())) * genesValues.get(gene) + (0.99f - (-0.99f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        }
                        break;

                    case LESS_OR_EQUAL:
                        if (genesValues.get(gene) <= constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else {
                            satisfiedConstraints += (-1.0f / (1.0f - constraint.getThreshold())) * genesValues.get(gene) + (1.0f - (-1.0f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        }
                        break;

                    case EQUAL:
                        if (genesValues.get(gene) == constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if (genesValues.get(gene) > constraint.getThreshold()) {
                            satisfiedConstraints += (-1.0f / (1.0f - constraint.getThreshold())) * genesValues.get(gene) + (1.0f - (-1.0f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        } else {
                            satisfiedConstraints += ((genesValues.get(gene) / constraint.getThreshold()));
                        }
                        break;

                    case GREATER:
                        if (genesValues.get(gene) > constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if ((genesValues.get(gene) == 0.0f) && (constraint.getThreshold() == 0.0f)) {
                            satisfiedConstraints += 0.99f;
                        } else {
                            satisfiedConstraints += ((0.99f * genesValues.get(gene)) / constraint.getThreshold());
                        }
                        break;

                    case GREATER_OR_EQUAL:
                        if (genesValues.get(gene) >= constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else {
                            satisfiedConstraints += ((genesValues.get(gene) / constraint.getThreshold()));
                        }
                        break;

                    default:
                        break;
                }
            }
        }

        for (Categories category : categoriesValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(category)) {
                Constraint constraint = strategicConstraints.getConstraint(category);
                switch (constraint.getComparisonOperator()) {
                    case LESS:
                        if (categoriesValues.get(category) < constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if ((categoriesValues.get(category) == 1.0f) && (constraint.getThreshold() == 1.0f)) {
                            satisfiedConstraints += 0.99f;
                        } else {
                            satisfiedConstraints += (-0.99f / (1.0f - constraint.getThreshold())) * categoriesValues.get(category) + (0.99f - (-0.99f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        }
                        break;
                    case LESS_OR_EQUAL:
                        if (categoriesValues.get(category) <= constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else {
                            satisfiedConstraints += (-1.0f / (1.0f - constraint.getThreshold())) * categoriesValues.get(category) + (1.0f - (-1.0f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        }
                        break;
                    case EQUAL:
                        if (categoriesValues.get(category) == constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if (categoriesValues.get(category) > constraint.getThreshold()) {
                            satisfiedConstraints += (-1.0f / (1.0f - constraint.getThreshold())) * categoriesValues.get(category) + (1.0f - (-1.0f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        } else {
                            satisfiedConstraints += ((categoriesValues.get(category) / constraint.getThreshold()));
                        }
                        break;
                    case GREATER:
                        if (categoriesValues.get(category) > constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if ((categoriesValues.get(category) == 0.0f) && (constraint.getThreshold() == 0.0f)) {
                            satisfiedConstraints += 0.99f;
                        } else {
                            satisfiedConstraints += ((0.99f * categoriesValues.get(category)) / constraint.getThreshold());
                        }
                        break;
                    case GREATER_OR_EQUAL:
                        if (categoriesValues.get(category) >= constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else {
                            satisfiedConstraints += ((categoriesValues.get(category) / constraint.getThreshold()));
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        for (Functions function : functionsValues.keySet()) {
            if (strategicConstraints.hasDefinedConstraint(function)) {
                Constraint constraint = strategicConstraints.getConstraint(function);
                switch (constraint.getComparisonOperator()) {
                    case LESS:
                        if (functionsValues.get(function) < constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if ((functionsValues.get(function) == 1.0f) && (constraint.getThreshold() == 1.0f)) {
                            satisfiedConstraints += 0.99f;
                        } else {
                            satisfiedConstraints += (-0.99f / (1.0f - constraint.getThreshold())) * functionsValues.get(function) + (0.99f - (-0.99f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        }
                        break;
                    case LESS_OR_EQUAL:
                        if (functionsValues.get(function) <= constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else {
                            satisfiedConstraints += (-1.0f / (1.0f - constraint.getThreshold())) * functionsValues.get(function) + (1.0f - (-1.0f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        }
                        break;
                    case EQUAL:
                        if (functionsValues.get(function) == constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if (functionsValues.get(function) > constraint.getThreshold()) {
                            satisfiedConstraints += (-1.0f / (1.0f - constraint.getThreshold())) * functionsValues.get(function) + (1.0f - (-1.0f / (1.0f - constraint.getThreshold())) * constraint.getThreshold());
                        } else {
                            satisfiedConstraints += ((functionsValues.get(function) / constraint.getThreshold()));
                        }
                        break;
                    case GREATER:
                        if (functionsValues.get(function) > constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else if ((functionsValues.get(function) == 0.0f) && (constraint.getThreshold() == 0.0f)) {
                            satisfiedConstraints += 0.99f;
                        } else {
                            satisfiedConstraints += ((0.99f * functionsValues.get(function)) / constraint.getThreshold());
                        }
                        break;
                    case GREATER_OR_EQUAL:
                        if (functionsValues.get(function) >= constraint.getThreshold()) {
                            satisfiedConstraints++;
                        } else {
                            satisfiedConstraints += ((functionsValues.get(function) / constraint.getThreshold()));
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (strategicConstraints.hasDefinedConstraint()) {
            Constraint condition = strategicConstraints.getConstraint();
            switch (condition.getComparisonOperator()) {
                case LESS:
                    if (assetValue < condition.getThreshold()) {
                        satisfiedConstraints++;
                    } else if ((assetValue == 1.0f) && (condition.getThreshold() == 1.0f)) {
                        satisfiedConstraints += 0.99f;
                    } else {
                        satisfiedConstraints += (-0.99f / (1.0f - condition.getThreshold())) * assetValue + (0.99f - (-0.99f / (1.0f - condition.getThreshold())) * condition.getThreshold());
                    }
                    break;
                case LESS_OR_EQUAL:
                    if (assetValue <= condition.getThreshold()) {
                        satisfiedConstraints++;
                    } else {
                        satisfiedConstraints += (-1.0f / (1.0f - condition.getThreshold())) * assetValue + (1.0f - (-1.0f / (1.0f - condition.getThreshold())) * condition.getThreshold());
                    }
                    break;
                case EQUAL:
                    if (assetValue == condition.getThreshold()) {
                        satisfiedConstraints++;
                    } else if (assetValue > condition.getThreshold()) {
                        satisfiedConstraints += (-1.0f / (1.0f - condition.getThreshold())) * assetValue + (1.0f - (-1.0f / (1.0f - condition.getThreshold())) * condition.getThreshold());
                    } else {
                        satisfiedConstraints += ((assetValue / condition.getThreshold()));
                    }
                    break;
                case GREATER:
                    if (assetValue > condition.getThreshold()) {
                        satisfiedConstraints++;
                    } else if ((assetValue == 0.0f) && (condition.getThreshold() == 0.0f)) {
                        satisfiedConstraints += 0.99f;
                    } else {
                        satisfiedConstraints += ((0.99f * assetValue) / condition.getThreshold());
                    }
                    break;
                case GREATER_OR_EQUAL:
                    if (assetValue >= condition.getThreshold()) {
                        satisfiedConstraints++;
                    } else {
                        satisfiedConstraints += ((assetValue / condition.getThreshold()));
                    }
                    break;
                default:
                    break;
            }
        }
        if (numberOfConstraints == 0.0f) {
            return 1.0f;
        } else {
            return (satisfiedConstraints / numberOfConstraints);
        }
    }

}
