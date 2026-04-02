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
package com.manolodominguez.experiments;

import com.manolodominguez.experiments.definitions.constraints.AFCEOLevelStrategicConstraints;
import com.manolodominguez.experiments.definitions.constraints.AFCLevelStrategicConstraints;
import com.manolodominguez.experiments.definitions.constraints.AFLevelStrategicConstraints;
import com.manolodominguez.experiments.definitions.constraints.ALevelStrategicConstraints;
import com.manolodominguez.fleco.algorithm.FLECO;
import com.manolodominguez.fleco.strategicconstraints.StrategicConstraints;
import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class utilizes FLECO to optimize the cybersecurity status of a specific
 * business asset.
 *
 * @author Manuel Domínguez-Dorado
 */
public class Complete {

    /**
     * This methods run an experiment that takes a initial cybersecurity status
     * for a given asset and also a set of strategic cybersecurity constraints
     * and run FLECO one hundred times to find one hundred solutions in the form
     * of desired cybersecurity statuses.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Complete.class);

        // *************************
        // Define FLECO's parameters 
        // *************************
        int initialPopulation = 30;
        int maxSeconds = 30;
        float crossoverProbability = 0.90f;
        ImplementationGroups implementationGroup = ImplementationGroups.IG3;
        // Define current cybersecurity status according to CyberTOMP. While it
        // is possible to create a custom status by configuring a Chromosome, 
        // some preconfigured statuses are available for quick use. 
        Chromosome initialStatus = new Chromosome(implementationGroup);
        StrategicConstraints strategicConstraints = new ALevelStrategicConstraints(implementationGroup);

        for (ImplementationGroups value : ImplementationGroups.values()) {
            for (int constraints = 0; constraints < 4; constraints++) {
                implementationGroup = value;
                initialStatus = new Chromosome(implementationGroup);
                for (int seed = 0; seed < 15; seed++) {
                    initialStatus.randomizeGenes();
                    for (int execution = 0; execution < 15; execution++) {
                        switch (constraints) {
                            case 0:
                                strategicConstraints = new ALevelStrategicConstraints(implementationGroup);
                                break;
                            case 1:
                                strategicConstraints = new AFLevelStrategicConstraints(implementationGroup);
                                break;
                            case 2:
                                strategicConstraints = new AFCLevelStrategicConstraints(implementationGroup);
                                break;
                            case 3:
                            default:
                                strategicConstraints = new AFCEOLevelStrategicConstraints(implementationGroup);
                                break;
                        }
                        FLECO fleco;
                        Chromosome bestChromosome;
                        fleco = new FLECO(initialPopulation, maxSeconds, crossoverProbability, implementationGroup, initialStatus, strategicConstraints);
                        fleco.evolve();
                        bestChromosome = fleco.getBestChromosome();
                        if (fleco.hasConverged()) {
                            logger.info(implementationGroup.name() + "#Constraints:" + constraints + "#Seed:" + seed + "#Execution:" + execution + "#CONVERGED#" + fleco.getUsedTime() + "#" + fleco.getUsedGenerations() + "#" + bestChromosome.getFitness() + "#" + bestChromosome.getFitnessConstraintsCoverage());
                        } else {
                            logger.info(implementationGroup.name() + "#Constraints:" + constraints + "#Seed:" + seed + "#Execution:" + execution + "#!CONVERGED#" + fleco.getUsedTime() + "#" + fleco.getUsedGenerations() + "#" + bestChromosome.getFitness() + "#" + bestChromosome.getFitnessConstraintsCoverage());
                        }
                    }
                }
                logger.info("");
            }
        }
    }
}
