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

import com.manolodominguez.fleco.events.ProgressEvent;
import com.manolodominguez.fleco.events.RotaryIDGenerator;
import com.manolodominguez.fleco.strategicconstraints.StrategicConstraints;
import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import java.time.Duration;
import java.time.Instant;
import com.manolodominguez.fleco.events.IFLECOProgressEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implements the FLECO (Fast, Lightweight, and EfficientCybersecurity
 * Optimization) adaptive and constrained genetic algorithm. This genetic
 * algorithm is designed to assist the asset cybersecurity team in making
 * decisions during the application of CyberTOMP® framework.
 *
 * @author Manuel Domínguez Dorado
 */
public class FLECO {

    private static final Logger logger = LoggerFactory.getLogger(FLECO.class);

    private final int maxAvailableSeconds;
    private final int initialPopulation;

    private float mutationProbability;
    private float crossoverProbability;
    private Population population;
    private float usedTime;
    private int usedGenerations;
    private IFLECOProgressEventListener progressEventListener;
    private final RotaryIDGenerator rotaryIDGenerator;

    private static final float STAGNATION_THRESHOLD_PERCENTAGE = 0.025f;
    private static final float DEEP_STAGNATION_THRESHOLD_FACTOR = 1.25f;
    private static final int DEFAULT_MUTATION_INCREASING_FACTOR = 1;
    private static final int HIGHER_MUTATION_INCREASING_FACTOR = 20;
    private static final float POPULATION_INCREASING_FACTOR = 1.50f;
    private static final int BEST_CHROMOSOME_INDEX = 0;

    /**
     * This is the constructor of the class. It creates a new instance of FLECO
     * (Fast, Lightweight, and Efficient Cybersecurity Optimization) Adaptive,
     * Constrained, and Multi-Objectives Genetic Algorithm with the parameters
     * specified.
     *
     * @param initialPopulation The initial number of chromosomes in the
     * population.
     * @param maxAvailableSeconds The max number of seconds before finishing the
     * population's evolution.
     * @param crossoverProbability The probability of crossing over a couple of
     * chromosomes during population's evolution.
     * @param implementationGroup The applicable implementation group as defined
     * in CyberTOMP. It can be IG1, IG2 and IG3 depending on whether the asset
     * criticality is LOW, MEDIUM or HIGH.
     * @param initialStatus A chromosome representing the initial cybersecurity
     * status of the asset, as defined in CyberTOMP.
     * @param strategicConstraints A set of constraints over the asset,
     * functions, categories or expected outcomes.
     */
    public FLECO(int initialPopulation, int maxAvailableSeconds, float crossoverProbability,
            ImplementationGroups implementationGroup, Chromosome initialStatus,
            StrategicConstraints strategicConstraints) {

        if (initialPopulation <= 0 || maxAvailableSeconds <= 0) {
            logger.error(
                    "Invalid constructor arguments: initialPopulation={}, maxAvailableSeconds={}",
                    initialPopulation, maxAvailableSeconds
            );
            throw new IllegalArgumentException("Population size and time must be positive.");
        }

        if (implementationGroup == null || initialStatus == null || strategicConstraints == null) {
            logger.error(
                    "Null argument detected: implementationGroup={}, initialStatus={}, strategicConstraints={}",
                    implementationGroup, initialStatus, strategicConstraints
            );
            throw new IllegalArgumentException("Arguments must not be null.");
        }

        this.initialPopulation = initialPopulation;
        this.maxAvailableSeconds = maxAvailableSeconds;
        this.crossoverProbability = crossoverProbability;

        // Mutation probability depends on the number of genes for the implementation group
        this.mutationProbability = 1.0f / Genes.getGenesFor(implementationGroup).size();

        this.population = new Population(initialPopulation, implementationGroup, initialStatus, strategicConstraints);
        this.rotaryIDGenerator = new RotaryIDGenerator();

        // Explicit initializations preserved for the sake of clarity
        this.usedTime = 0.0f;
        this.usedGenerations = 0;
        this.progressEventListener = null;
    }

    /**
     * This method sets the progress event listener for FLECO.
     *
     * @param progressEventListener the progress event listener.
     */
    public void setProgressEventListener(IFLECOProgressEventListener progressEventListener) {
        if (this.progressEventListener != null) {
            logger.error("Attempt to set a second progress listener. Existing: {}", this.progressEventListener);
            throw new IllegalStateException("Only one progress listener is allowed.");
        }
        this.progressEventListener = progressEventListener;
    }

    /**
     * The population is developed according to FLECO principles using this
     * approach, until either the algorithm reaches convergence or the maximum
     * number of generations is attained.
     */
    public void evolve() {
        int currentGeneration = 0;
        float currentBestFitness = 0.0f;
        int mutationIncreasingFactor = DEFAULT_MUTATION_INCREASING_FACTOR;

        float stagnationThreshold = maxAvailableSeconds * STAGNATION_THRESHOLD_PERCENTAGE;

        boolean seemsALocalMinimum = false;
        boolean isDeeplyStagnated = false;

        Instant begin = Instant.now();
        Instant latestBestFitnessChange = begin;

        usedTime = 0.0f;

        while (!hasToFinish(begin, isDeeplyStagnated)) {

            // Cache the best chromosome to avoid repeated lookups
            Chromosome best = population.get(BEST_CHROMOSOME_INDEX);
            float bestFitness = best.getFitness();

            // Update the best fitness observed so far
            if (bestFitness > currentBestFitness) {
                currentBestFitness = bestFitness;
                latestBestFitnessChange = Instant.now();
            }

            // Compute stagnation time in seconds
            long stagnationSeconds
                    = Duration.between(latestBestFitnessChange, Instant.now()).getSeconds();

            // Determine whether the algorithm seems to be stuck in a local minimum
            seemsALocalMinimum = stagnationSeconds > stagnationThreshold;

            // Determine whether stagnation is severe (deep stagnation)
            isDeeplyStagnated
                    = stagnationSeconds > (stagnationThreshold * DEEP_STAGNATION_THRESHOLD_FACTOR);

            // Adjust mutation factor depending on stagnation
            mutationIncreasingFactor = seemsALocalMinimum
                    ? HIGHER_MUTATION_INCREASING_FACTOR
                    : DEFAULT_MUTATION_INCREASING_FACTOR;

            // Calculate the fitness and arrange the population accordingly
            population.selectBestAdapted();

            // Spread progress event
            notifyProgress(begin, currentGeneration, best);

            // Escape strategies when stagnation is detected
            if (seemsALocalMinimum) {

                // If deeply stagnated and not converged, perform a soft reset
                if (isDeeplyStagnated && !population.hasConverged()) {
                    population.softReset();
                    currentBestFitness = population.get(BEST_CHROMOSOME_INDEX).getFitness();
                    latestBestFitnessChange = Instant.now();
                }

                // Inject additional random chromosomes to increase diversity
                population.populateRandomly((int) (initialPopulation * POPULATION_INCREASING_FACTOR));
            }

            // Apply mutation with an adaptive probability
            population.mutate(mutationProbability * mutationIncreasingFactor);

            // Perform a crossover on the population
            population.crossover(crossoverProbability);

            // Complete the population adding random individuals if needed
            population.populateRandomly();

            // Reduce the population to the default number of chromosomes
            population.reduceTo(initialPopulation);

            // Increase the generation number
            currentGeneration++;
        }

        // Compute total execution time
        Duration duration = Duration.between(begin, Instant.now());
        usedTime = duration.toMillis() / 1000f;
        usedGenerations = currentGeneration;
    }

    /**
     * Notifies the registered progress event listener about the current
     * progress.
     *
     * @param begin The starting point used as reference of time.
     * @param currentGeneration The generation currently being evolved.
     * @param best The best chromosome of the current population.
     */
    private void notifyProgress(Instant begin, int currentGeneration, Chromosome best) {
        if (progressEventListener == null) {
            return;
        }

        long totalTime = maxAvailableSeconds * 1000L;
        long currentTime = Instant.now().toEpochMilli() - begin.toEpochMilli();

        ProgressEvent event = new ProgressEvent(
                this,
                rotaryIDGenerator.getNextIdentifier(),
                totalTime,
                currentTime,
                currentGeneration,
                best,
                population.hasConverged()
        );

        progressEventListener.onProgressEventReceived(event);
    }

    /**
     * This method returns the number of seconds the execution of FLECO has
     * lasted.
     *
     * @return the number of seconds the execution of FLECO has lasted.
     */
    public float getUsedTime() {
        return usedTime;
    }

    /**
     * This method returns the number of generations the execution of FLECO has
     * required.
     *
     * @return the number of generations the execution of FLECO has required.
     */
    public int getUsedGenerations() {
        return usedGenerations;
    }

    /**
     * This method checks whether the conditions to finish FLECO algorithm exist
     * or not.
     *
     * @param begin the time when the algorithm started to evolve the
     * population.
     * @return true, if the conditions to finish FLECO execution exist.
     * Otherwise return false.
     */
    private boolean hasToFinish(Instant begin, boolean isDeeplyStagnated) {
        long elapsed = Duration.between(begin, Instant.now()).getSeconds();

        if (population.hasConverged()) {
            return true;
        }
        if (elapsed > maxAvailableSeconds) {
            return true;
        }
        return isDeeplyStagnated && population.hasConverged();
    }

    /**
     * This method returns whether FLECO algorithm has converged or not.
     *
     * @return true if FLECO algorithm has converged to a high quality solution.
     * Otherwise, return false.
     */
    public boolean hasConverged() {
        return population.hasConverged();
    }

    /**
     * This method returns the chromosome with the best fitness in the
     * population.
     *
     * @return The chromosome with the best fitness in the population.
     */
    public Chromosome getBestChromosome() {
        return population.get(BEST_CHROMOSOME_INDEX);
    }
}
