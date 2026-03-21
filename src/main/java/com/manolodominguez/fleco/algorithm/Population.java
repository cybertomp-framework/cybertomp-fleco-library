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
 * @author Manuel Domínguez-Dorado
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

    private final Logger logger = LoggerFactory.getLogger(Population.class);

    /**
     * This is the constructor of the class, which initializes the population
     * parameters and generates some initial chromosomes, including specialized
     * ones derived from the initial cybersecurity status and the defined
     * strategic objectives.
     *
     * @param initialNumberOfChromosomes The initial number of chromosomes in
     * the population.
     * @param implementationGroup The implementation group that applies to the
     * business asset being considered.
     * @param initialStatus A Chromosome indicating the initial cybersecurity
     * status of the asset.
     * @param strategicConstraints A set of strategic cybersecurity constraints.
     */
    public Population(int initialNumberOfChromosomes, ImplementationGroups implementationGroup, Chromosome initialStatus, StrategicConstraints strategicConstraints) {
        super();
        this.initialNumberOfChromosomes = initialNumberOfChromosomes;
        this.implementationGroup = implementationGroup;
        this.initialStatus = initialStatus;
        this.strategicConstraints = strategicConstraints;
        // Add the initial cybersecurity status as a chromosome in the 
        // population
        add(initialStatus);
        // Depending on the strategic constraints, several additional 
        // chromosomes can be inferred that enhances the quality of the 
        // population.
        addAll(strategicConstraints.generatePrecandidatesBasedOn(initialStatus));
        computeFitnessAndSort();
        fitnessAverage = 0.0f;
        converged = false;
        // Complete the population with random chromosomes until the initial 
        // number of chromosomes are reached.
        populateRandomly();
        // Reduce the population, if neccesary, to have exactly the initial 
        // number of chromosomes.
        reduceTo(this.initialNumberOfChromosomes);
    }

    /**
     * This class insert new random chromosomes in the population until the
     * defined initial number of chromosomes are reached.
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
     * This class randomly generates the specified number of chromosomes, and
     * insert them in the population, independently on whether the resulting
     * population's size is greater than the initial number of chromosomes or
     * not. It is used to enlarge the population's size under certain
     * circumstances.
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
     * This method select the established percentage of the population's best
     * individuals as parents for the next generation, removing the twins if
     * they exist. It discards the rest.
     */
    public void selectBestAdapted() {
        // First it compute fitness and sort the population based on it.
        computeFitnessAndSort();
        // Remove twins
        CopyOnWriteArrayList<Chromosome> twinsFree = new CopyOnWriteArrayList<>();
        twinsFree.addAll(this);
        for (Chromosome chromosome : toArray(new Chromosome[0])) {
            boolean isTwin = true;
            int instances = 0;
            for (Chromosome otherChromosome : twinsFree.toArray(new Chromosome[0])) {
                isTwin = true;
                for (Genes gene : chromosome.getGenes().keySet()) {
                    if (chromosome.getAllele(gene) != otherChromosome.getAllele(gene)) {
                        isTwin = false;
                        break;
                    }
                }
                if (isTwin) {
                    instances++;
                    if (instances > 1) {
                        twinsFree.remove(otherChromosome);
                    }
                }
            }
        }
        if (!twinsFree.isEmpty()) {
            clear();
            addAll(twinsFree);
        }
        sort(new ChromosomeComparator());
        // 1/5 of the current population is selected for reproduction in the 
        // next generation (4/5 are discarded).
        int thresshold = size() * 1 / 5;
        CopyOnWriteArrayList<Chromosome> bestAdapted = new CopyOnWriteArrayList<>();
        for (int i = 0; i <= thresshold; i++) {
            bestAdapted.add(get(i));
        }
        if (!bestAdapted.isEmpty()) {
            clear();
            addAll(bestAdapted);
        }
        sort(new ChromosomeComparator());
    }

    /**
     * This method generate mutated chromosomes from the current population. It
     * goes across all genes of each chromosome applying a mutation when
     * applicable due to the defiend mutation rate.
     * 
     * @param mutationProbablity the probability that a chromosome is mutated.
     */
    public void mutate(float mutationProbablity) {
        Alleles[] allelesArray = Alleles.values();
        CopyOnWriteArrayList<Chromosome> mutatedChromosomes = new CopyOnWriteArrayList<>();
        for (Chromosome chromosome : toArray(new Chromosome[0])) {
            // A new chromosome is created as a copy the current one.
            Chromosome mutatedChromosome = new Chromosome(implementationGroup);
            mutatedChromosome.setGenes(chromosome.getGenes());
            int randomAllele = 0;
            boolean mutated = false;
            // All genes of such chromosome are reviewed
            for (Genes gene : mutatedChromosome.getGenes().keySet()) {
                if (gene.appliesToIG(implementationGroup)) {
                    // If the mutation probability recommends to mutate the 
                    // chromosome
                    if (Math.random() < mutationProbablity) {
                        // Tag the chromosome as mutated. Non mutated 
                        // chromosomes are discarded ath the end because they ç
                        // are twins.
                        mutated = true;
                        // Repeat until mutation is effectively done.
                        while (chromosome.getAllele(gene) == mutatedChromosome.getAllele(gene)) {
                            // Select the allele
                            randomAllele = ThreadLocalRandom.current().nextInt(0, allelesArray.length);
                            // Update the allele for the mutated gene
                            mutatedChromosome.updateAllele(gene, allelesArray[randomAllele]);
                        }
                    }
                }
            }
            if (mutated) {
                mutatedChromosomes.add(mutatedChromosome);
            }
        }
        if (!mutatedChromosomes.isEmpty()) {
            addAll(mutatedChromosomes);
        }
    }

    /**
     * This method generate chromosomes by applying a crossover int the current
     * population. For each time a crossover applies, a new couple of cromosome
     * are created whose genes are exchanged from a random point to the end of
     * the chromosomes or from the beginning of the chromosome to the crossing
     * point, randomly.
     *
     * @param crossoverProbability
     */
    public void crossover(float crossoverProbability) {
        Chromosome chromosomeA = new Chromosome(implementationGroup);
        Chromosome chromosomeB = new Chromosome(implementationGroup);
        CopyOnWriteArrayList<Chromosome> crossedChromosomes = new CopyOnWriteArrayList<>();
        for (int i = 0; i < (size() - 1); i += 2) {
            // For each chromosome in the population, if the possibility of 
            // being applied a crossover is met, a couple of chromosomes are 
            // cloned from two chromosomes of the current population.
            if (Math.random() < crossoverProbability) {
                CopyOnWriteArrayList<Genes> genesForTheNewChromosome = new CopyOnWriteArrayList<>();
                chromosomeA.setGenes(get(i).getGenes());
                chromosomeB.setGenes(get(i + 1).getGenes());
                for (Genes gene : chromosomeA.getGenes().keySet()) {
                    if (gene.appliesToIG(implementationGroup)) {
                        genesForTheNewChromosome.add(gene);
                    }
                }
                // Randomly select one crossing point in the chromosome.
                int crossoverPoint = ThreadLocalRandom.current().nextInt(0, genesForTheNewChromosome.size());
                // Ramdomly select whether the crossover will be from the 
                // beginning of the chromosome to the crossing point or from the 
                // crossing point to the end of the chromosome.
                boolean beginningIsTheAnchorPoint = (ThreadLocalRandom.current().nextInt(0, 1) == 0);
                if (beginningIsTheAnchorPoint) {
                    // Genes from chromosome A and B are exchanged from the 
                    // beginning of the chromosome to the crossing point.
                    for (int j = crossoverPoint; j < genesForTheNewChromosome.size(); j++) {
                        chromosomeA.updateAllele(genesForTheNewChromosome.get(j), get(i + 1).getAllele(genesForTheNewChromosome.get(j)));
                        chromosomeB.updateAllele(genesForTheNewChromosome.get(j), get(i).getAllele(genesForTheNewChromosome.get(j)));
                    }
                    crossedChromosomes.add(chromosomeA);
                    crossedChromosomes.add(chromosomeB);
                } else {
                    // Genes from chromosome A and B are exchanged from the 
                    // crossing point to the end of the chromosome.
                    for (int j = 0; j < crossoverPoint; j++) {
                        chromosomeA.updateAllele(genesForTheNewChromosome.get(j), get(i + 1).getAllele(genesForTheNewChromosome.get(j)));
                        chromosomeB.updateAllele(genesForTheNewChromosome.get(j), get(i).getAllele(genesForTheNewChromosome.get(j)));
                    }
                    crossedChromosomes.add(chromosomeA);
                    crossedChromosomes.add(chromosomeB);
                }
            }
        }
        if (!crossedChromosomes.isEmpty()) {
            addAll(crossedChromosomes);
        }
    }

    /**
     * This method computes the fitness for every chromosome in the population
     * and also the average fitness of all them.
     */
    private void computeFitnessAndSort() {
        fitnessAverage = 0.0f;
        for (Chromosome chromosome : toArray(new Chromosome[0])) {
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
     * This method perform a soft reset of the algorithm by replacing the best
     * fitted individual with random ones and recomputing the fitness
     * accordingly.
     */
    public void softReset() {
        fitnessAverage = 0.0f;
        converged = false;
        int oneThird = size() / 2;
        for (int i = 0; i < oneThird; i++) {
            if (!isEmpty()) {
                remove(BEST_CHROMOSOME_INDEX);
            }
        }
        populateRandomly();
        reduceTo(this.initialNumberOfChromosomes);
        computeFitnessAndSort();
    }

    /**
     * This method returns whether the population contains a best individual
     * with enough quality, or not.
     *
     * @return true, if the population contains a best individual with enough
     * quality. Otherwise, false.
     */
    public boolean hasConverged() {
        return converged;
    }

    /**
     * This method returns the average fitness of all chromosomes in the
     * population.
     *
     * @return The average fitness of all chromosomes in the population.
     */
    public float getFitnessAverage() {
        return fitnessAverage;
    }

    /**
     * This method reduces the number of chromosomes in the population
     * maintaining only the best ones especified as a parameter.
     *
     * @param finalNumber The number of best chromosomes that will survive to
     * the pupulation reduction.
     */
    public final void reduceTo(int finalNumber) {
        computeFitnessAndSort();
        CopyOnWriteArrayList<Chromosome> auxPopulation = new CopyOnWriteArrayList<>();
        if (size() >= finalNumber) {
            for (int i = 0; i < finalNumber; i++) {
                auxPopulation.add(get(i));
            }
            clear();
            addAll(auxPopulation);
        }
    }

    /**
     * This method prints the population and the most important information
     * related to it.
     */
    public void print() {
        int i = 0;
        logger.info("Final population:");
        for (Chromosome chromosome : toArray(new Chromosome[0])) {
            logger.info("\t" + i + "#" + chromosome.getFitness() + "#" + chromosome.getFitnessConstraintsCoverage());
            i++;
        }
    }
}
