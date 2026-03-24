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
package com.manolodominguez.fleco.strategicconstraints;

import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.genetics.Alleles;
import com.manolodominguez.fleco.uleo.Categories;
import com.manolodominguez.fleco.uleo.Functions;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import java.util.EnumMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a set of strategic cybersecurity constraints. They are
 * defined as target values of metrics coming from CyberTOMP proposal. The
 * purpose of this class is to guide the evolution of FLECO towards a solution
 * that fulfills all strategic constraints.
 *
 * @author Manuel Domínguez-Dorado
 */
public class StrategicConstraints {

    private EnumMap<Genes, Constraint> geneConstraints;
    private EnumMap<Categories, Constraint> categoryConstraints;
    private EnumMap<Functions, Constraint> functionConstraints;
    private Constraint assetConstraint;
    private ImplementationGroups implementationGroup;

    private final Logger logger = LoggerFactory.getLogger(StrategicConstraints.class);
    
    
    /**
     * This is the constructor of the class. It creates a new, empty instance.
     *
     * @param implementationGroup the implementation group that applies due to
     * the criticlity of the asset being considered.
     */
    public StrategicConstraints(ImplementationGroups implementationGroup) {
        geneConstraints = new EnumMap<>(Genes.class);
        categoryConstraints = new EnumMap<>(Categories.class);
        functionConstraints = new EnumMap<>(Functions.class);
        this.implementationGroup = implementationGroup;
        this.assetConstraint = null;
    }

    /**
     * This method removes all the strategic constraint defined at whatever
     * levels.
     */
    public void removeAll() {
        geneConstraints = new EnumMap<>(Genes.class);
        categoryConstraints = new EnumMap<>(Categories.class);
        functionConstraints = new EnumMap<>(Functions.class);
        this.assetConstraint = null;
    }

    /**
     * This method set a new constraint defined for a given cybersecurity
     * expected outcome/gene.
     *
     * @param gene The gene/expected outcome the constraint is defined for.
     * @param constraint The defined constraint.
     */
    public void addConstraint(Genes gene, Constraint constraint) {
        if (gene.appliesToIG(implementationGroup)) {
            geneConstraints.put(gene, constraint);
        }
    }

    /**
     * This method set a new constraint defined for a given cybersecurity
     * category.
     *
     * @param category The cybersecurity categroy the constraint is defined for.
     * @param constraint The defined constraint.
     */
    public void addConstraint(Categories category, Constraint constraint) {
        if (category.appliesToIG(implementationGroup)) {
            categoryConstraints.put(category, constraint);
        }
    }

    /**
     * This method set a new constraint defined for a given cybersecurity
     * function.
     *
     * @param function The cybersecurity function the constraint is defined for.
     * @param constraint The defined constraint.
     */
    public void addConstraint(Functions function, Constraint constraint) {
        if (function.appliesToIG(implementationGroup)) {
            functionConstraints.put(function, constraint);
        }
    }

    /**
     * This method set a new hig-level constraint defined for the global asset's
     * cybersecurity status.
     *
     * @param constraint The defined constraint.
     */
    public void addConstraint(Constraint constraint) {
        this.assetConstraint = constraint;
    }

    /**
     * This method check whether a specific gene/expected outcome has a
     * constraint associated to it or not.
     *
     * @param gene The gene that is bein queried.
     * @return true, if the specified gene has a constraint associated to it.
     * Otherwise, false.
     */
    public boolean hasDefinedConstraint(Genes gene) {
        return geneConstraints.containsKey(gene);
    }

    /**
     * This method check whether a specific cybersecurity category has a
     * constraint associated to it or not.
     *
     * @param category The cybersecurity category that is bein queried.
     * @return true, if the specified cybersecurity category has a constraint
     * associated to it. Otherwise, false.
     */
    public boolean hasDefinedConstraint(Categories category) {
        return categoryConstraints.containsKey(category);
    }

    /**
     * This method check whether a specific cybersecurity function has a
     * constraint associated to it or not.
     *
     * @param function The cybersecurity function that is bein queried.
     * @return true, if the specified cybersecurity function has a constraint
     * associated to it. Otherwise, false.
     */
    public boolean hasDefinedConstraint(Functions function) {
        return functionConstraints.containsKey(function);
    }

    /**
     * This method check whether the global asset's cybersecurity status has a
     * constraint associated to it or not.
     *
     * @return true, if the global asset's cybersecurity status has a constraint
     * associated to it. Otherwise, false.
     */
    public boolean hasDefinedConstraint() {
        return assetConstraint != null;
    }

    /**
     * This method returns the constraint associated to the gene/expected
     * outcome specified as a parameter.
     *
     * @param gene The gene whose constraint is being requested.
     * @return the constraint associated to the gene/expected outcome specified
     * as a parameter.
     */
    public Constraint getConstraint(Genes gene) {
        return geneConstraints.get(gene);
    }

    /**
     * This method returns the constraint associated to the cybersecurity
     * category specified as a parameter.
     *
     * @param category The cybersecurity category whose constraint is being
     * requested.
     * @return the constraint associated to the cybersecurity category specified
     * as a parameter.
     */
    public Constraint getConstraint(Categories category) {
        return categoryConstraints.get(category);
    }

    /**
     * This method returns the constraint associated to the cybersecurity
     * function specified as a parameter.
     *
     * @param function The cybersecurity function whose constraint is being
     * requested.
     * @return the constraint associated to the cybersecurity function specified
     * as a parameter.
     */
    public Constraint getConstraint(Functions function) {
        return functionConstraints.get(function);
    }

    /**
     * This method returns the constraint associated to the global asset's
     * cybersecurity status.
     *
     * @return the constraint associated to the global asset's cybersecurity
     * status.
     */
    public Constraint getConstraint() {
        return assetConstraint;
    }

    /**
     * This method removes the strategic constraint defined at asset level.
     */
    public void removeConstraint() {
        assetConstraint = null;
    }

    /**
     * This method removes the strategic constraint defined at function level
     * and associated to the specified function.
     *
     * @param function the specified function.
     */
    public void removeConstraint(Functions function) {
        functionConstraints.remove(function);
    }

    /**
     * This method removes the strategic constraint defined at category level
     * and associated to the specified category.
     *
     * @param category the specified category.
     */
    public void removeConstraint(Categories category) {
        categoryConstraints.remove(category);
    }

    /**
     * This method removes the strategic constraint defined at expected outcome
     * or gene level and associated to the specified gene.
     *
     * @param gene the specified category.
     */
    public void removeConstraint(Genes gene) {
        geneConstraints.remove(gene);
    }

    /**
     * This method generate some individuals of high quality based on the set of
     * defined strategic constraints and also depending on the initial status of
     * cybersecurity. They can be used as part of the starting population for
     * FLECO.
     *
     * @param initialStatus The initial status of cybersecurity of the asset
     * being protected.
     * @return the constraint associated to the global asset's cybersecurity
     * status.
     */
    public CopyOnWriteArrayList<Chromosome> generatePrecandidatesBasedOn(Chromosome initialStatus) {
        CopyOnWriteArrayList<Chromosome> candidateChromosomes = new CopyOnWriteArrayList<>();
        Chromosome candidate;
        candidateChromosomes.add(initialStatus);
        if (!geneConstraints.isEmpty()) {
            candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            for (Genes gene : geneConstraints.keySet()) {
                switch (geneConstraints.get(gene).getComparisonOperator()) {
                    case LESS:
                        if (Alleles.getLesser(geneConstraints.get(gene).getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getLesser(geneConstraints.get(gene).getThreshold()));
                        }
                        break;
                    case LESS_OR_EQUAL:
                        if (Alleles.getLesserOrEqual(geneConstraints.get(gene).getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getLesserOrEqual(geneConstraints.get(gene).getThreshold()));
                        }
                        break;
                    case EQUAL:
                        if (Alleles.getEqual(geneConstraints.get(gene).getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getEqual(geneConstraints.get(gene).getThreshold()));
                        }
                        break;
                    case GREATER:
                        if (Alleles.getGreater(geneConstraints.get(gene).getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getGreater(geneConstraints.get(gene).getThreshold()));
                        }
                        break;
                    case GREATER_OR_EQUAL:
                        if (Alleles.getGreaterOrEqual(geneConstraints.get(gene).getThreshold()) != null) {
                            candidate.updateAllele(gene, Alleles.getGreaterOrEqual(geneConstraints.get(gene).getThreshold()));
                        }
                        break;
                    default:
                        break;
                }
            }
            candidateChromosomes.add(candidate);
        }
        if (!categoryConstraints.isEmpty()) {
            boolean created = false;
            candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            for (Categories category : categoryConstraints.keySet()) {
                if (category.appliesToIG(implementationGroup)) {
                    if ((categoryConstraints.get(category).getComparisonOperator() == ComparisonOperators.EQUAL) || (categoryConstraints.get(category).getComparisonOperator() == ComparisonOperators.GREATER_OR_EQUAL) || (categoryConstraints.get(category).getComparisonOperator() == ComparisonOperators.LESS_OR_EQUAL)) {
                        CopyOnWriteArrayList<Genes> applicableGenes;
                        if (categoryConstraints.get(category).getThreshold() == Alleles.DLI_0.getDLI()) {
                            applicableGenes = Genes.getGenesFor(category, implementationGroup);
                            for (Genes gene : applicableGenes) {
                                candidate.updateAllele(gene, Alleles.DLI_0);
                                created = true;
                            }
                        } else if (categoryConstraints.get(category).getThreshold() == Alleles.DLI_100.getDLI()) {
                            applicableGenes = Genes.getGenesFor(category, implementationGroup);
                            for (Genes gene : applicableGenes) {
                                candidate.updateAllele(gene, Alleles.DLI_100);
                                created = true;
                            }
                        }
                    }
                }
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }
        if (!functionConstraints.isEmpty()) {
            boolean created = false;
            candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            for (Functions function : functionConstraints.keySet()) {
                if (function.appliesToIG(implementationGroup)) {
                    if ((functionConstraints.get(function).getComparisonOperator() == ComparisonOperators.EQUAL) || (functionConstraints.get(function).getComparisonOperator() == ComparisonOperators.GREATER_OR_EQUAL) || (functionConstraints.get(function).getComparisonOperator() == ComparisonOperators.LESS_OR_EQUAL)) {
                        CopyOnWriteArrayList<Genes> applicableGenes;
                        if (functionConstraints.get(function).getThreshold() == Alleles.DLI_0.getDLI()) {
                            applicableGenes = Categories.getGenesFor(function, implementationGroup);
                            for (Genes gene : applicableGenes) {
                                candidate.updateAllele(gene, Alleles.DLI_0);
                                created = true;
                            }
                        } else if (functionConstraints.get(function).getThreshold() == Alleles.DLI_100.getDLI()) {
                            applicableGenes = Categories.getGenesFor(function, implementationGroup);
                            for (Genes gene : applicableGenes) {
                                candidate.updateAllele(gene, Alleles.DLI_100);
                                created = true;
                            }
                        }
                        candidateChromosomes.add(candidate);
                    }
                }
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }
        if (assetConstraint != null) {
            boolean created = false;
            candidate = new Chromosome(implementationGroup);
            candidate.setGenes(initialStatus.getGenes());
            if ((assetConstraint.getComparisonOperator() == ComparisonOperators.EQUAL) || (assetConstraint.getComparisonOperator() == ComparisonOperators.GREATER_OR_EQUAL) || (assetConstraint.getComparisonOperator() == ComparisonOperators.LESS_OR_EQUAL)) {
                if (assetConstraint.getThreshold() == Alleles.DLI_0.getDLI()) {
                    for (Genes gene : Genes.values()) {
                        if (gene.appliesToIG(implementationGroup)) {
                            candidate.updateAllele(gene, Alleles.DLI_0);
                            created = true;
                        }
                    }
                } else if (assetConstraint.getThreshold() == Alleles.DLI_100.getDLI()) {
                    for (Genes gene : Genes.values()) {
                        if (gene.appliesToIG(implementationGroup)) {
                            candidate.updateAllele(gene, Alleles.DLI_100);
                            created = true;
                        }
                    }
                }
                candidateChromosomes.add(candidate);
            }
            if (created) {
                candidateChromosomes.add(candidate);
            }
        }

        return candidateChromosomes;
    }

    /**
     * This method returns the number of strategic constraints that has been
     * defined.
     *
     * @return the number of strategic constraints that has been defined.
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
     * This method prints the strategic constraints that has been defined,
     * classifying them in asset constraints, functions constraints, categories
     * constraints and expected outcomes constraints.
     */
    public void print() {
        logger.info("\tAsset constraint...........: " + this.assetConstraint.getComparisonOperator().name() + " " + this.assetConstraint.getThreshold());
        for (Functions function : this.functionConstraints.keySet()) {
            logger.info("\tFunction constraint........: " + function.name() + " " + this.functionConstraints.get(function).getComparisonOperator().name() + " " + this.functionConstraints.get(function).getThreshold());
        }
        for (Categories category : this.categoryConstraints.keySet()) {
            logger.info("\tCategory constraint........: " + category.name() + " " + this.categoryConstraints.get(category).getComparisonOperator().name() + " " + this.categoryConstraints.get(category).getThreshold());
        }
        for (Genes gene : this.geneConstraints.keySet()) {
            logger.info("\tExpected outcome constraint: " + gene.name() + " " + this.geneConstraints.get(gene).getComparisonOperator().name() + " " + this.geneConstraints.get(gene).getThreshold());
        }
    }

    /**
     * This method returns the strategic constraints as JSON strings to be
     * treated automatically whenever needed.
     *
     * @return the strategic constraints as JSON strings
     */
    public String getConstraintsAsJSONString() {
        String JSONString = "";
        int totalConstraints = categoryConstraints.size() + functionConstraints.size() + geneConstraints.size();
        if (this.assetConstraint != null) {
            totalConstraints++;
        }
        int constraintsNum = 0;
        if (constraintsNum < totalConstraints) {
            if (assetConstraint != null) {
                JSONString+="\t\t{\"asset\":\"ASSET\",\"operator\":\""+assetConstraint.getComparisonOperator()+"\",\"value\":" + assetConstraint.getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString+=",\n";
                } else {
                    JSONString+="\n";
                }
                constraintsNum++;
            }
        }
        for (Categories category : categoryConstraints.keySet()) {
            if (constraintsNum < totalConstraints) {
                JSONString += "\t\t{\"category\":\""+category+"\",\"operator\":\""+categoryConstraints.get(category).getComparisonOperator()+"\",\"value\":" + categoryConstraints.get(category).getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString+=",\n";
                } else {
                    JSONString+="\n";
                }
                constraintsNum++;
            }
        }
        for (Functions function : functionConstraints.keySet()) {
            if (constraintsNum < totalConstraints) {
                JSONString += "\t\t{\"function\":\""+function+"\",\"operator\":\""+functionConstraints.get(function).getComparisonOperator()+"\",\"value\":" + functionConstraints.get(function).getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString+=",\n";
                } else {
                    JSONString+="\n";
                }
                constraintsNum++;
            }
        }
        for (Genes gene : geneConstraints.keySet()) {
            if (constraintsNum < totalConstraints) {
                JSONString += "\t\t{\"gene\":\""+gene+"\",\"operator\":\""+geneConstraints.get(gene).getComparisonOperator()+"\",\"value\":"+ geneConstraints.get(gene).getThreshold() + "}";
                if (constraintsNum < (totalConstraints - 1)) {
                    JSONString+=",\n";
                } else {
                    JSONString+="\n";
                }
                constraintsNum++;
            }
        }
        return JSONString;
    }

}
