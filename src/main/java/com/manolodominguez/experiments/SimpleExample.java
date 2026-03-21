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

import com.manolodominguez.fleco.algorithm.FLECO;
import com.manolodominguez.fleco.strategicconstraints.StrategicConstraints;
import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.uleo.ImplementationGroups;
import com.manolodominguez.fleco.events.DefaultProgressEventListener;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.strategicconstraints.ComparisonOperators;
import com.manolodominguez.fleco.strategicconstraints.Constraint;
import com.manolodominguez.fleco.uleo.Categories;
import com.manolodominguez.fleco.uleo.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class utilizes FLECO to optimize the cybersecurity status of a specific
 * business asset.
 *
 * @author Manuel Domínguez-Dorado
 */
public class SimpleExample {

    /**
     * This methods run an experiment that takes a initial cybersecurity status
     * for a given asset and also a set of strategic cybersecurity constraints
     * and run FLECO to find a solution in the form of a desired cybersecurity
     * status.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SimpleExample.class);
        // *************************
        // Define FLECO's parameters 
        // *************************
        int initialPopulation = 30;
        int maxSeconds = 30;
        float crossoverProbability = 0.90f;
        ImplementationGroups implementationGroup = ImplementationGroups.IG3;
        // Create and define your asset's current cybersecurity status according
        // to CyberTOMP proposal.
        // 
        // You must configure each chromosome's allele's value individually to 
        // fit the real state of your asset's expected outcome. Thi should be 
        // done through squential calls to updateAllele(gene, allele). For 
        // instance:
        //
        // Chromosome initialStatus = new Chromosome(implementationGroup);
        // initialStatus.updateAllele(Genes.PR_AC_PR_AC_3, Alleles.DLI_67);
        // initialStatus.updateAllele(Genes.PR_AC_PR_AC_4, Alleles.DLI_67);
        // initialStatus.updateAllele(Genes.PR_AC_PR_AC_5, Alleles.DLI_0);
        // initialStatus.updateAllele(Genes.PR_AC_PR_AC_7, Alleles.DLI_67);
        // initialStatus.updateAllele(Genes.PR_AT_PR_AT_1, Alleles.DLI_0);
        // initialStatus.updateAllele(Genes.PR_DS_CSC_3_4, Alleles.DLI_100);
        // initialStatus.updateAllele(Genes.PR_DS_PR_DS_3, Alleles.DLI_67);
        // initialStatus.updateAllele(Genes.PR_IP_9D_9, Alleles.DLI_0);
        // initialStatus.updateAllele(Genes.PR_IP_CSC_11_1, Alleles.DLI_100);
        // initialStatus.updateAllele(Genes.PR_IP_CSC_4_3, Alleles.DLI_100); 
        // and so on...
        // 
        // However, for this example we will use a random initial status.
        // 
        Chromosome initialStatus = new Chromosome(implementationGroup);
        initialStatus.randomizeGenes();

        // The following step requires the creation and definition of the 
        // strategic cybersecurity goals/constaints. FLECO will work to find a
        // new cybersecurity state called "target state" that fulfil all them,
        // starting from the current cybersecurity state "initial state" and 
        // identifying the set of new actions that has to be carried out to 
        // achieve the target status. For example, a set of strategic goals 
        // could be:
        StrategicConstraints strategicConstraints = new StrategicConstraints(implementationGroup);
        // Asset constraint
        strategicConstraints.addConstraint(new Constraint(ComparisonOperators.GREATER, 0.65f));
        // Functions constraints
        strategicConstraints.addConstraint(Functions.IDENTIFY, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.6f));
        // Category constraints
        strategicConstraints.addConstraint(Categories.RC_CO, new Constraint(ComparisonOperators.LESS, 0.8f));
        strategicConstraints.addConstraint(Categories.PR_AC, new Constraint(ComparisonOperators.GREATER, 0.6f));
        strategicConstraints.addConstraint(Categories.ID_SC, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.5f));
        // Expected outcomes constraints
        strategicConstraints.addConstraint(Genes.RC_CO_RC_CO_3, new Constraint(ComparisonOperators.GREATER, 0.6f));
        strategicConstraints.addConstraint(Genes.RS_MI_RS_MI_3, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.3f));
        strategicConstraints.addConstraint(Genes.DE_DP_DE_DP_5, new Constraint(ComparisonOperators.EQUAL, 0.67f));
        strategicConstraints.addConstraint(Genes.DE_AE_DE_AE_5, new Constraint(ComparisonOperators.LESS, 0.6f));
        strategicConstraints.addConstraint(Genes.PR_PT_9D_7, new Constraint(ComparisonOperators.LESS_OR_EQUAL, 0.6f));
        strategicConstraints.addConstraint(Genes.ID_BE_ID_BE_3, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.7f));

        // Now, create an instance of FLECO algorithm that must be initialized 
        // using the previous definitions.
        
        FLECO fleco;
        fleco = new FLECO(initialPopulation, maxSeconds, crossoverProbability, implementationGroup, initialStatus, strategicConstraints);
        fleco.setProgressEventListener(new DefaultProgressEventListener());
        fleco.evolve();
        fleco.getBestChromosome().print();
    }
}
