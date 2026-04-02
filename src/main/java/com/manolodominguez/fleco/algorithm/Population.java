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
package com.manolodominguez.fleco.algorithm;

import com.manolodominguez.fleco.strategicconstraints.StrategicConstraints;
import com.manolodominguez.fleco.genetics.Alleles;
import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a population. A set of chromosomes and the
 * corresponding methods to make the required operations.
 *
 * @author Manuel
 */
public class Population extends CopyOnWriteArrayList<Chromosome> {

    private static final long serialVersionUID = 1L;
    private static final int BEST_CHROMOSOME_INDEX = 0;

    private final int initialNumberOfChromosomes;
    private final transient ImplementationGroups implementationGroup;
    private float fitnessAverage;
    private Chromosome initialStatus;
    private StrategicConstraints strategicConstraints;
    private boolean converged;

    private static final Logger logger = LoggerFactory.getLogger(Population.class);

    /**
     * Constructor. Initializes the population and generates initial
     * chromosomes, including those derived from strategic constraints.
     *
     * @param initialNumberOfChromosomes The initial number of chromosomes in
     * the population.
     * @param implementationGroup The implementation group that applies to the
     * business asset being considered.
     * @param initialStatus A Chromosome indicating the initial cybersecurity
     * status of the asset.
     * @param strategicConstraints A set of strategic cybersecurity constraints.
     */
    public Population(int initialNumberOfChromosomes, ImplementationGroups implementationGroup,
            Chromosome initialStatus, StrategicConstraints strategicConstraints) {

        super();

        if (initialNumberOfChromosomes <= 0) {
            logger.error("Invalid initialNumberOfChromosomes: {}", initialNumberOfChromosomes);
            throw new IllegalArgumentException("initialNumberOfChromosomes must be greater than zero.");
        }

        if (implementationGroup == null || initialStatus == null || strategicConstraints == null) {
            logger.error(
                    "Null argument detected: implementationGroup={}, initialStatus={}, strategicConstraints={}",
                    implementationGroup, initialStatus, strategicConstraints
            );
            throw new IllegalArgumentException("Arguments must not be null.");
        }

        this.initialNumberOfChromosomes = initialNumberOfChromosomes;
        this.implementationGroup = implementationGroup;
        this.initialStatus = initialStatus;
        this.strategicConstraints = strategicConstraints;

        // Add the initial cybersecurity status as a chromosome
        super.add(initialStatus);

        // Add precandidates derived from strategic constraints
        super.addAll(strategicConstraints.generatePrecandidatesBasedOn(initialStatus));

        computeFitnessAndSort();
        fitnessAverage = 0.0f;
        converged = false;

        // Complete population with random chromosomes
        populateRandomly();

        // Ensure exact initial size
        reduceTo(this.initialNumberOfChromosomes);
    }

    /**
     * Inserts random chromosomes until the population reaches its initial size.
     */
    public final void populateRandomly() {
        while (size() < initialNumberOfChromosomes) {
            Chromosome chromosome = new Chromosome(implementationGroup);
            chromosome.randomizeGenes();
            add(chromosome);
        }
        computeFitnessAndSort();
    }

    /**
     * Inserts a specific number of random chromosomes, regardless of final
     * size.
     *
     * @param additionalChromosomes the number of random chromosomes to add to
     * the population.
     */
    public void populateRandomly(int additionalChromosomes) {
        int targetSize = size() + additionalChromosomes;
        while (size() < targetSize) {
            Chromosome chromosome = new Chromosome(implementationGroup);
            chromosome.randomizeGenes();
            add(chromosome);
        }
        computeFitnessAndSort();
    }

    /**
     * Selects the best-adapted individuals, removes twins, and keeps the top
     * 20%.
     */
    public void selectBestAdapted() {

        // Compute fitness and sort population
        computeFitnessAndSort();

        // Remove twins (duplicate chromosomes)
        CopyOnWriteArrayList<Chromosome> twinsFree = new CopyOnWriteArrayList<>(this);

        for (Chromosome chromosome : this) {
            int instances = 0;

            for (Chromosome other : twinsFree) {
                if (areTwins(chromosome, other)) {
                    instances++;
                    if (instances > 1) {
                        twinsFree.remove(other);
                    }
                }
            }
        }

        if (!twinsFree.isEmpty()) {
            clear();
            addAll(twinsFree);
        }

        sort(new ChromosomeComparator());

        // Select top 20% of the population
        int threshold = size() / 5;
        CopyOnWriteArrayList<Chromosome> bestAdapted = new CopyOnWriteArrayList<>();

        for (int i = 0; i <= threshold && i < size(); i++) {
            bestAdapted.add(get(i));
        }

        if (!bestAdapted.isEmpty()) {
            clear();
            addAll(bestAdapted);
        }

        sort(new ChromosomeComparator());
    }

    /**
     * Helper method to determine whether two chromosomes are identical.
     */
    private boolean areTwins(Chromosome a, Chromosome b) {
        for (Genes gene : a.getGenes().keySet()) {
            if (a.getAllele(gene) != b.getAllele(gene)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates mutated chromosomes based on mutation probability.
     *
     * @param mutationProbability the probability that a chromosome is mutated.
     */
    public void mutate(float mutationProbability) {

        Alleles[] allelesArray = Alleles.values();
        CopyOnWriteArrayList<Chromosome> mutatedChromosomes = new CopyOnWriteArrayList<>();

        for (Chromosome chromosome : this) {

            Chromosome mutated = new Chromosome(implementationGroup);
            mutated.setGenes(chromosome.getGenes());

            boolean mutatedFlag = false;

            for (Genes gene : mutated.getGenes().keySet()) {
                if (gene.appliesToIG(implementationGroup)) {

                    if (ThreadLocalRandom.current().nextFloat() < mutationProbability) {
                        mutatedFlag = true;

                        // Ensure allele actually changes
                        Alleles newAllele;
                        do {
                            newAllele = allelesArray[ThreadLocalRandom.current().nextInt(allelesArray.length)];
                        } while (newAllele == mutated.getAllele(gene));

                        mutated.updateAllele(gene, newAllele);
                    }
                }
            }

            if (mutatedFlag) {
                mutatedChromosomes.add(mutated);
            }
        }

        if (!mutatedChromosomes.isEmpty()) {
            addAll(mutatedChromosomes);
        }
    }

    /**
     * Applies crossover to the population.
     *
     * FIXED (2026/04/02): Corrected bug where random selection always returned
     * 0.
     *
     * @param crossoverProbability Probability (0.0–1.0) that a crossover is
     * applied to each chromosome pair during evolution.
     */
    public void crossover(float crossoverProbability) {

        CopyOnWriteArrayList<Chromosome> crossedChromosomes = new CopyOnWriteArrayList<>();

        for (int i = 0; i < size() - 1; i += 2) {

            if (ThreadLocalRandom.current().nextFloat() < crossoverProbability) {

                Chromosome chromosomeA = new Chromosome(implementationGroup);
                Chromosome chromosomeB = new Chromosome(implementationGroup);

                chromosomeA.setGenes(get(i).getGenes());
                chromosomeB.setGenes(get(i + 1).getGenes());

                CopyOnWriteArrayList<Genes> genesForCrossover = new CopyOnWriteArrayList<>();

                for (Genes gene : chromosomeA.getGenes().keySet()) {
                    if (gene.appliesToIG(implementationGroup)) {
                        genesForCrossover.add(gene);
                    }
                }

                int crossoverPoint = ThreadLocalRandom.current().nextInt(genesForCrossover.size());

                // FIXED: Now correctly chooses between 0 and 1 (before, it was nextInt(1))
                boolean beginningIsAnchor = ThreadLocalRandom.current().nextInt(2) == 0;

                if (beginningIsAnchor) {
                    for (int j = crossoverPoint; j < genesForCrossover.size(); j++) {
                        Genes g = genesForCrossover.get(j);
                        chromosomeA.updateAllele(g, get(i + 1).getAllele(g));
                        chromosomeB.updateAllele(g, get(i).getAllele(g));
                    }
                } else {
                    for (int j = 0; j < crossoverPoint; j++) {
                        Genes g = genesForCrossover.get(j);
                        chromosomeA.updateAllele(g, get(i + 1).getAllele(g));
                        chromosomeB.updateAllele(g, get(i).getAllele(g));
                    }
                }

                crossedChromosomes.add(chromosomeA);
                crossedChromosomes.add(chromosomeB);
            }
        }

        if (!crossedChromosomes.isEmpty()) {
            addAll(crossedChromosomes);
        }
    }

    /**
     * Computes fitness for all chromosomes and sorts the population.
     */
    private void computeFitnessAndSort() {

        fitnessAverage = 0.0f;

        for (Chromosome chromosome : this) {
            chromosome.computeFitness(initialStatus, strategicConstraints);
            fitnessAverage += chromosome.getFitness();
        }

        fitnessAverage /= size();

        sort(new ChromosomeComparator());

        if (!isEmpty()) {
            converged = get(BEST_CHROMOSOME_INDEX).getFitnessConstraintsCoverage() >= 1.0f;
        }
    }

    /**
     * Performs a soft reset by removing the best half of the population.
     */
    public void softReset() {

        fitnessAverage = 0.0f;
        converged = false;

        int half = size() / 2;

        for (int i = 0; i < half; i++) {
            if (!isEmpty()) {
                remove(BEST_CHROMOSOME_INDEX);
            }
        }

        populateRandomly();
        reduceTo(this.initialNumberOfChromosomes);
        computeFitnessAndSort();
    }

    public boolean hasConverged() {
        return converged;
    }

    public float getFitnessAverage() {
        return fitnessAverage;
    }

    /**
     * Reduces the population to the specified number of best chromosomes.
     *
     * @param finalNumber Number of highestfitness chromosomes to keep after
     * reducing the population.
     */
    public final void reduceTo(int finalNumber) {

        computeFitnessAndSort();

        if (size() > finalNumber) {
            CopyOnWriteArrayList<Chromosome> aux
                    = new CopyOnWriteArrayList<>(subList(0, finalNumber));

            clear();
            addAll(aux);
        }
    }

    /**
     * Prints the population and its most relevant information.
     */
    public void print() {
        int i = 0;
        logger.info("Final population:");
        for (Chromosome chromosome : this) {
            logger.info("\t" + i + "#" + chromosome.getFitness() + "#" + chromosome.getFitnessConstraintsCoverage());
            i++;
        }
    }
}
