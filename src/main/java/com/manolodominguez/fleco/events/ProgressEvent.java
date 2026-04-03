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
package com.manolodominguez.fleco.events;

import com.manolodominguez.fleco.algorithm.FLECO;
import com.manolodominguez.fleco.genetics.Chromosome;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Progress event generated during the execution of FLECO. It provides
 * information about the current state of the evolutionary process, including
 * elapsed time, generation count, best chromosome, and convergence status.
 *
 * Implementations of {@link IFLECOProgressEventListener} can use this event to
 * monitor the evolution in real time.
 *
 * This class is immutable except for the inherited event ID.
 *
 * @author Manuel Domínguez-Dorado
 */
public class ProgressEvent extends FLECOEvent {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ProgressEvent.class);

    private final long totalTime;
    private final long currentTime;
    private final long currentGeneration;
    private final boolean converged;

    // Not serializable: events are not meant to be persisted
    private transient final Chromosome currentBestChromosome;

    /**
     * Creates a new progress event.
     *
     * @param eventGenerator the FLECO instance that generated the event.
     * @param eventID the unique event identifier.
     * @param totalTime total allowed execution time in milliseconds.
     * @param currentTime elapsed execution time in milliseconds.
     * @param currentGeneration current generation number.
     * @param currentBestChromosome best chromosome found so far.
     * @param converged whether the population has converged.
     */
    public ProgressEvent(
            FLECO eventGenerator,
            long eventID,
            long totalTime,
            long currentTime,
            long currentGeneration,
            Chromosome currentBestChromosome,
            boolean converged) {

        super(eventGenerator, eventID, Instant.now());

        if (totalTime < 0 || currentTime < 0 || currentGeneration < 0) {
            logger.error("Negative values are not allowed: totalTime={}, currentTime={}, currentGeneration={}",
                    totalTime, currentTime, currentGeneration);
            throw new IllegalArgumentException("Time and generation values must be non-negative.");
        }

        if (currentBestChromosome == null) {
            logger.error("currentBestChromosome cannot be null.");
            throw new IllegalArgumentException("currentBestChromosome cannot be null.");
        }

        this.totalTime = totalTime;
        this.currentTime = currentTime;
        this.currentGeneration = currentGeneration;
        this.currentBestChromosome = currentBestChromosome;
        this.converged = converged;
    }

    /**
     * Returns the progress percentage as a value between 0.0 and 1.0.
     *
     * @return the progress percentage of the FLECO execution.
     */
    public float getProgressPercentage() {
        return (totalTime == 0) ? 1.0f : (float) currentTime / totalTime;
    }

    /**
     * Returns the current generation number.
     *
     * @return the current generation of the evolutionary process.
     */
    public long getCurrentGeneration() {
        return currentGeneration;
    }

    /**
     * Returns the best chromosome found so far.
     *
     * @return the current best chromosome in the population.
     */
    public Chromosome getCurrentBestChromosome() {
        return currentBestChromosome;
    }

    /**
     * Returns whether the population has converged.
     *
     * @return true if the population has converged; false otherwise.
     */
    public boolean hasConverged() {
        return converged;
    }

    /**
     * Logs the event information every 100 generations to avoid excessive
     * verbosity.
     */
    public void print() {
        if (currentGeneration % 100 == 0) {
            logger.info(
                    "Time: {}/{} ({}%) | Generation: {} | Best fitness: {}",
                    currentTime,
                    totalTime,
                    getProgressPercentage() * 100.0f,
                    currentGeneration,
                    currentBestChromosome.getFitness()
            );
        }
    }

    /**
     * Returns the type of this event.
     *
     * @return the event type, always {@link EventTypes#PROGRESS}.
     */
    @Override
    public EventTypes getType() {
        return EventTypes.PROGRESS;
    }
}
