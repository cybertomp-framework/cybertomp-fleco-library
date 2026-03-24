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
package com.manolodominguez.fleco.events;

import com.manolodominguez.fleco.algorithm.FLECO;
import com.manolodominguez.fleco.genetics.Chromosome;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a progress event that will allow knowing the state of
 * the FLECO execution when it is running.
 *
 * @author Manuel Domínguez-Dorado
 */
@SuppressWarnings("serial")
public class ProgressEvent extends FLECOEvent {

    private final long totalTime;
    private final long currentTime;
    private final long currentGeneration;
    private Chromosome currentBestChromosome;
    private boolean converged;

    private final Logger logger = LoggerFactory.getLogger(ProgressEvent.class);

    private static final int ZERO = 0;

    /**
     * This method is the constrctor of the class.It creates a new instance of
     * ProgressEvent.
     *
     * @param eventID The unique event identifier.
     * @param eventGenerator The object that generates the event.
     * @param totalTime the total amount of time allowed before finishing the
     * FLECO execution, as milliseconds.
     * @param currentTime the time that has elapsed from the begining of FLECO
     * execution, as milliseconds.
     * @param currentGeneration the current generation of the FLECO's
     * pupolation.
     * @param currentBestChromosome the current best individual in FLECO's
     * population.
     * @param converged Whether the FLECO has converged (true) or not (false).
     */
    public ProgressEvent(FLECO eventGenerator, long eventID, long totalTime, long currentTime, long currentGeneration, Chromosome currentBestChromosome, boolean converged) {
        super(eventGenerator, eventID, Instant.now());
        this.totalTime = totalTime;
        this.currentTime = currentTime;
        this.currentGeneration = currentGeneration;
        this.currentBestChromosome = currentBestChromosome;
        this.converged = converged;
        if (currentBestChromosome == null) {
            logger.error("currentBestChromosome cannot be null");
            throw new IllegalArgumentException("currentBestChromosome cannot be null");
        }
    }

    /**
     * This method returns the progress percentage advertised by this event.
     *
     * @return The progress percentage that the event is carrying out and will
     * be received by the listener.
     */
    public float getProgressPercentage() {
        if (totalTime != ZERO) {
            return (float) currentTime / (float) totalTime;
        }
        return 1.0f;
    }

    /**
     * This method returns the current generation of FLECO's population.
     *
     * @return the current generation of FLECO's population.
     */
    public long getCurrentGeneration() {
        return currentGeneration;
    }

    /**
     * This method returns the current FLECO's population's best chromosome.
     *
     * @return the current FLECO's population's best chromosome.
     */
    public Chromosome getCurrentBestChromosome() {
        return this.currentBestChromosome;
    }

    /**
     * This method returns whether the FLECO's population has converged or not.
     *
     * @return true, if the FLECO's population has converged. Otherwise, returns
     * false.
     */
    public boolean hasConverged() {
        return this.converged;
    }

    /**
     * This method prints the information of the event if the current generation
     * is a multiple of 100 (to avoid being too much verbose).
     */
    public void print() {
        if ((currentGeneration % 100) == ZERO) {
            logger.info("Time: " + currentTime + "/" + totalTime + "(" + (float) currentTime / totalTime + "%) Generation: " + currentGeneration + " Current best solution: " + currentBestChromosome.getFitness());
        }
    }

    /**
     * This method return the type of this event. It is one of the enums defined
     * in EventTypes.
     *
     * @return The type of this event. It is one of the enums defined in
     * EventTypes.
     */
    @Override
    public EventTypes getType() {
        return EventTypes.PROGRESS;
    }
}
