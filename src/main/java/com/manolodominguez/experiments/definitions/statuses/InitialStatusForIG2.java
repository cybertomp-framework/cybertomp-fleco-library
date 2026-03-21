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
package com.manolodominguez.experiments.definitions.statuses;

import com.manolodominguez.fleco.genetics.Alleles;
import com.manolodominguez.fleco.genetics.Chromosome;
import com.manolodominguez.fleco.genetics.Genes;
import com.manolodominguez.fleco.uleo.ImplementationGroups;

/**
 * This class implement a predefined initial cybersecurity status for a
 * ficticious asset where the applicable implementation group is IG2.
 *
 * @author Manuel Domínguez-Dorado
 */
public class InitialStatusForIG2 extends Chromosome {

    /**
     * This is the constuctor of the class. It creates a new initial
     * cybersecurity status for a ficticious asset where the applicable
     * implementation group is IG2. It is intended only to ease the experiments.
     */
    public InitialStatusForIG2() {
        super(ImplementationGroups.IG2);
        updateAllele(Genes.ID_AM_CSC_1_1, Alleles.DLI_33);
        updateAllele(Genes.ID_AM_CSC_12_4, Alleles.DLI_33);
        updateAllele(Genes.ID_AM_CSC_14_1, Alleles.DLI_67);
        updateAllele(Genes.ID_AM_CSC_2_2, Alleles.DLI_0);
        updateAllele(Genes.ID_AM_CSC_3_1, Alleles.DLI_0);
        updateAllele(Genes.ID_AM_CSC_3_2, Alleles.DLI_33);
        updateAllele(Genes.ID_AM_CSC_3_6, Alleles.DLI_0);
        updateAllele(Genes.ID_AM_CSC_3_7, Alleles.DLI_67);
        updateAllele(Genes.ID_AM_ID_AM_1, Alleles.DLI_67);
        updateAllele(Genes.ID_AM_ID_AM_2, Alleles.DLI_0);
        updateAllele(Genes.ID_AM_ID_AM_3, Alleles.DLI_33);
        updateAllele(Genes.ID_BE_9D_1, Alleles.DLI_33);
        updateAllele(Genes.ID_GV_CSC_17_4, Alleles.DLI_33);
        updateAllele(Genes.ID_GV_ID_GV_1, Alleles.DLI_100);
        updateAllele(Genes.ID_GV_ID_GV_2, Alleles.DLI_33);
        updateAllele(Genes.ID_RA_9D_1, Alleles.DLI_0);
        updateAllele(Genes.ID_RA_CSC_18_2, Alleles.DLI_100);
        updateAllele(Genes.ID_RA_CSC_3_7, Alleles.DLI_33);
        updateAllele(Genes.ID_RA_ID_RA_1, Alleles.DLI_0);
        updateAllele(Genes.ID_RM_9D_8, Alleles.DLI_67);
        updateAllele(Genes.ID_SC_ID_SC_1, Alleles.DLI_67);
        updateAllele(Genes.ID_SC_ID_SC_2, Alleles.DLI_100);
        updateAllele(Genes.ID_SC_ID_SC_3, Alleles.DLI_67);
        updateAllele(Genes.ID_SC_ID_SC_5, Alleles.DLI_67);
        updateAllele(Genes.PR_AC_CSC_12_5, Alleles.DLI_33);
        updateAllele(Genes.PR_AC_CSC_12_6, Alleles.DLI_0);
        updateAllele(Genes.PR_AC_CSC_13_4, Alleles.DLI_0);
        updateAllele(Genes.PR_AC_CSC_4_7, Alleles.DLI_33);
        updateAllele(Genes.PR_AC_CSC_5_2, Alleles.DLI_33);
        updateAllele(Genes.PR_AC_CSC_5_6, Alleles.DLI_67);
        updateAllele(Genes.PR_AC_PR_AC_1, Alleles.DLI_33);
        updateAllele(Genes.PR_AC_PR_AC_3, Alleles.DLI_67);
        updateAllele(Genes.PR_AC_PR_AC_4, Alleles.DLI_67);
        updateAllele(Genes.PR_AC_PR_AC_5, Alleles.DLI_0);
        updateAllele(Genes.PR_AC_PR_AC_7, Alleles.DLI_67);
        updateAllele(Genes.PR_AT_CSC_14_9, Alleles.DLI_67);
        updateAllele(Genes.PR_AT_CSC_15_4, Alleles.DLI_67);
        updateAllele(Genes.PR_AT_PR_AT_1, Alleles.DLI_0);
        updateAllele(Genes.PR_AT_PR_AT_2, Alleles.DLI_67);
        updateAllele(Genes.PR_DS_CSC_3_4, Alleles.DLI_100);
        updateAllele(Genes.PR_DS_PR_DS_1, Alleles.DLI_33);
        updateAllele(Genes.PR_DS_PR_DS_2, Alleles.DLI_33);
        updateAllele(Genes.PR_DS_PR_DS_3, Alleles.DLI_67);
        updateAllele(Genes.PR_DS_PR_DS_6, Alleles.DLI_67);
        updateAllele(Genes.PR_DS_PR_DS_7, Alleles.DLI_33);
        updateAllele(Genes.PR_IP_9D_3, Alleles.DLI_33);
        updateAllele(Genes.PR_IP_9D_5, Alleles.DLI_67);
        updateAllele(Genes.PR_IP_9D_8, Alleles.DLI_33);
        updateAllele(Genes.PR_IP_9D_9, Alleles.DLI_0);
        updateAllele(Genes.PR_IP_CSC_11_1, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_CSC_16_1, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_CSC_2_5, Alleles.DLI_67);
        updateAllele(Genes.PR_IP_CSC_2_6, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_CSC_4_3, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_PR_IP_1, Alleles.DLI_33);
        updateAllele(Genes.PR_IP_PR_IP_10, Alleles.DLI_0);
        updateAllele(Genes.PR_IP_PR_IP_11, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_PR_IP_12, Alleles.DLI_0);
        updateAllele(Genes.PR_IP_PR_IP_2, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_PR_IP_4, Alleles.DLI_100);
        updateAllele(Genes.PR_IP_PR_IP_6, Alleles.DLI_33);
        updateAllele(Genes.PR_IP_PR_IP_7, Alleles.DLI_33);
        updateAllele(Genes.PR_IP_PR_IP_9, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_9D_5, Alleles.DLI_0);
        updateAllele(Genes.PR_MA_9D_9, Alleles.DLI_67);
        updateAllele(Genes.PR_MA_CSC_12_1, Alleles.DLI_100);
        updateAllele(Genes.PR_MA_CSC_12_3, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_13_5, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_18_3, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_4_2, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_4_6, Alleles.DLI_100);
        updateAllele(Genes.PR_MA_CSC_4_8, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_4_9, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_7_3, Alleles.DLI_0);
        updateAllele(Genes.PR_MA_CSC_8_1, Alleles.DLI_0);
        updateAllele(Genes.PR_MA_CSC_8_10, Alleles.DLI_100);
        updateAllele(Genes.PR_MA_CSC_8_3, Alleles.DLI_33);
        updateAllele(Genes.PR_MA_CSC_8_9, Alleles.DLI_0);
        updateAllele(Genes.PR_PT_9D_4, Alleles.DLI_33);
        updateAllele(Genes.PR_PT_CSC_4_4, Alleles.DLI_67);
        updateAllele(Genes.PR_PT_CSC_4_5, Alleles.DLI_33);
        updateAllele(Genes.PR_PT_CSC_9_5, Alleles.DLI_0);
        updateAllele(Genes.PR_PT_PR_PT_1, Alleles.DLI_67);
        updateAllele(Genes.PR_PT_PR_PT_2, Alleles.DLI_67);
        updateAllele(Genes.PR_PT_PR_PT_5, Alleles.DLI_67);
        updateAllele(Genes.DE_AE_DE_AE_1, Alleles.DLI_0);
        updateAllele(Genes.DE_AE_DE_AE_2, Alleles.DLI_0);
        updateAllele(Genes.DE_AE_DE_AE_3, Alleles.DLI_33);
        updateAllele(Genes.DE_CM_CSC_13_1, Alleles.DLI_0);
        updateAllele(Genes.DE_CM_CSC_13_5, Alleles.DLI_0);
        updateAllele(Genes.DE_CM_DE_CM_1, Alleles.DLI_33);
        updateAllele(Genes.DE_CM_DE_CM_4, Alleles.DLI_100);
        updateAllele(Genes.DE_CM_DE_CM_7, Alleles.DLI_0);
        updateAllele(Genes.DE_CM_DE_CM_8, Alleles.DLI_0);
        updateAllele(Genes.DE_DP_CSC_17_1, Alleles.DLI_100);
        updateAllele(Genes.DE_DP_CSC_17_4, Alleles.DLI_67);
        updateAllele(Genes.DE_DP_CSC_17_5, Alleles.DLI_33);
        updateAllele(Genes.RS_AN_RS_AN_1, Alleles.DLI_67);
        updateAllele(Genes.RS_AN_RS_AN_5, Alleles.DLI_67);
        updateAllele(Genes.RS_CO_CSC_17_4, Alleles.DLI_33);
        updateAllele(Genes.RS_CO_CSC_17_5, Alleles.DLI_33);
        updateAllele(Genes.RS_IM_RS_IM_1, Alleles.DLI_0);
        updateAllele(Genes.RS_IM_RS_IM_2, Alleles.DLI_33);
        updateAllele(Genes.RS_MI_CSC_1_2, Alleles.DLI_33);
        updateAllele(Genes.RS_MI_CSC_4_10, Alleles.DLI_33);
        updateAllele(Genes.RS_MI_CSC_7_7, Alleles.DLI_33);
        updateAllele(Genes.RS_RP_CSC_17_6, Alleles.DLI_0);
    }

}
