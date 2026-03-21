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
package com.manolodominguez.experiments.definitions.constraints;

import com.manolodominguez.fleco.strategicconstraints.Constraint;
import com.manolodominguez.fleco.strategicconstraints.ComparisonOperators;
import com.manolodominguez.fleco.strategicconstraints.StrategicConstraints;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.uleo.Categories;
import com.manolodominguez.fleco.uleo.Functions;
import com.manolodominguez.fleco.uleo.ImplementationGroups;

/**
 * This class implement a predefined set of strategic cybersecurity constraints
 * that are of very complex to solve because it contains a constraint related to
 * the overall asset's cybersecurity status, to the cybersecurity functions and
 * also to the cybersecurity categories and expected outcomes. It is intended
 * only to ease the experiments.
 *
 * @author Manuel Domínguez-Dorado
 */
public class AFCEOLevelStrategicConstraints extends StrategicConstraints {

    /**
     * This is the constuctor of the class. It creates a new instance and adds
     * constraints for the overall asset's cybersecurity status and also for
     * cybersecurity functions, cybersecurity categories and expected outcomes.
     *
     * @param implementationGroup The implementation groups that applies to the
     * asset being considered.
     */
    public AFCEOLevelStrategicConstraints(ImplementationGroups implementationGroup) {
        super(implementationGroup);
        // Asset constraint
        addConstraint(new Constraint(ComparisonOperators.GREATER, 0.65f));
        // Functions constraints
        addConstraint(Functions.IDENTIFY, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.6f));
        // Category constraints
        addConstraint(Categories.RC_CO, new Constraint(ComparisonOperators.LESS, 0.8f));
        addConstraint(Categories.PR_AC, new Constraint(ComparisonOperators.GREATER, 0.6f));
        addConstraint(Categories.ID_SC, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.5f));
        // Expected outcomes constraints
        addConstraint(Genes.RC_CO_RC_CO_3, new Constraint(ComparisonOperators.GREATER, 0.6f));
        addConstraint(Genes.RS_MI_RS_MI_3, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.3f));
        addConstraint(Genes.DE_DP_DE_DP_5, new Constraint(ComparisonOperators.EQUAL, 0.67f));
        addConstraint(Genes.DE_AE_DE_AE_5, new Constraint(ComparisonOperators.LESS, 0.6f));
        addConstraint(Genes.PR_PT_9D_7, new Constraint(ComparisonOperators.LESS_OR_EQUAL, 0.6f));
        addConstraint(Genes.ID_BE_ID_BE_3, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.7f));

        addConstraint(Genes.ID_AM_CSC_12_4, new Constraint(ComparisonOperators.EQUAL, 0.33f));
        addConstraint(Genes.ID_GV_CSC_17_4, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.2f));
        addConstraint(Genes.PR_AC_CSC_5_6, new Constraint(ComparisonOperators.GREATER, 0.6f));
        addConstraint(Genes.PR_IP_9D_8, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.3f));
        addConstraint(Genes.DE_AE_DE_AE_1, new Constraint(ComparisonOperators.EQUAL, 0.67f));
        addConstraint(Genes.RS_AN_RS_AN_1, new Constraint(ComparisonOperators.LESS, 0.6f));

        addConstraint(Genes.ID_AM_CSC_3_6, new Constraint(ComparisonOperators.LESS_OR_EQUAL, 0.6f));
        addConstraint(Genes.PR_MA_CSC_4_2, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.5f));
        addConstraint(Genes.DE_AE_DE_AE_3, new Constraint(ComparisonOperators.EQUAL, 0.33f));
        addConstraint(Genes.DE_CM_DE_CM_4, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.2f));
        addConstraint(Genes.RS_MI_CSC_1_2, new Constraint(ComparisonOperators.GREATER_OR_EQUAL, 0.2f));
    }

}
