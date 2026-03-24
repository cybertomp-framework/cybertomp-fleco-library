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
