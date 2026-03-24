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
package com.manolodominguez.fleco.uleo;

import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum defines all cybersecurity functions and also its weights as defined
 * in CyberTOMP proposal, depending on whether implementation groups 1, 2, or 3
 * applies. Additional descriptions and auxiliar data is provided for each.
 *
 * @author manuel Domínguez-Dorado
 */
public enum Functions {
    IDENTIFY((float) 4 / 15, (float) 6 / 20, (float) 6 / 23, "ID", "Develop an organizational understanding to manage cybersecurity risk to systems, people, assets, data, and capabilities."),
    PROTECT((float) 6 / 15, (float) 6 / 20, (float) 6 / 23, "PR", "Develop and implement appropriate safeguards to ensure delivery of critical services."),
    DETECT((float) 3 / 15, (float) 3 / 20, (float) 3 / 23, "DE", "Develop and implement appropriate activities to identify the occurrence of a cybersecurity event."),
    RESPOND((float) 2 / 15, (float) 5 / 20, (float) 5 / 23, "RS", "Develop and implement appropriate activities to take action regarding a detected cybersecurity incident."),
    RECOVER((float) 0 / 15, (float) 0 / 20, (float) 3 / 23, "RC", "Develop and implement appropriate activities to maintain plans for resilience and to restore any capabilities or services that were impaired due to a cybersecurity incident");

    private float weights[] = new float[3];
    private String acronym = "";
    private String purpose = "";

    private final Logger logger = LoggerFactory.getLogger(Functions.class);
    
    /**
     * This is the constructor of the class. it creates the enum and assigns the
     * corresponding values.
     *
     * @param weightIG1 A float value representing the weight of this
     * cybersecurity function when applying implementation group 1. A number
     * between 0.0 and 1.0.
     * @param weightIG2 A float value representing the weight of this
     * cybersecurity function when applying implementation group 2. A number
     * between 0.0 and 1.0.
     * @param weightIG3 A float value representing the weight of this
     * cybersecurity function when applying implementation group 3. A number
     * between 0.0 and 1.0.
     * @param acronym the very short name of this function.
     * @param purpose the main purpose of this of this function.
     */
    private Functions(float weightIG1, float weightIG2, float weightIG3, String acronym, String purpose) {
        this.weights[0] = weightIG1;
        this.weights[1] = weightIG2;
        this.weights[2] = weightIG3;

        this.weights[ImplementationGroups.IG1.getImplementationGroupIndex()] = weightIG1;
        this.weights[ImplementationGroups.IG2.getImplementationGroupIndex()] = weightIG2;
        this.weights[ImplementationGroups.IG3.getImplementationGroupIndex()] = weightIG3;

        this.acronym = acronym;
        this.purpose = purpose;
    }

    /**
     * This method returns the weight of this cybersecurity function taking into
     * consideration the impleentation group that applies.
     *
     * @param implementationGroup The implementation group that applies.
     * @return the weight of the cybersecurity function taking into
     * consideration the impleentation group that applies.
     */
    public float getWeight(ImplementationGroups implementationGroup) {
        return this.weights[implementationGroup.getImplementationGroupIndex()];
    }

    /**
     * This method returns the very short name of this function.
     *
     * @return the very short name of this function.
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * This method returns the main purpose of this of this function.
     *
     * @return the main purpose of this of this function.
     */
    public String getPurpose() {
        return this.purpose;
    }

    /**
     * Given an implementation group, this method returns whether the
     * cybersecurity function applies for it or not.
     *
     * @param implementationGroup The applicable implementation group.
     * @return true, if the cybersecurity function applies. Otherwise, false.
     */
    public boolean appliesToIG(ImplementationGroups implementationGroup) {
        return weights[implementationGroup.getImplementationGroupIndex()] > 0.0f;
    }

    /**
     * Given an implementation group, this method returns a list of
     * cybersecurity categories that applies to that implementation group and
     * belongs to the cybersecurity function.
     *
     * @param implementationGroup The applicable implementation group.
     * @return a list of cybersecurity categories that applies to that
     * implementation group and belongs to the cybersecurity function.
     */
    public CopyOnWriteArrayList<Categories> getCategories(ImplementationGroups implementationGroup) {
        CopyOnWriteArrayList<Categories> categories = new CopyOnWriteArrayList<>();
        for (Categories category : Categories.values()) {
            if ((category.getFunction() == this) && (category.getWeight(implementationGroup) > 0.0f)) {
                categories.add(category);
            }
        }
        return categories;
    }

    /**
     * This method returns the list of cybersecurity categories that are
     * applicable for a given implementation group.
     *
     * @param implementationGroup The applicable implementation group.
     * @return the list of cybersecurity categories that are applicable for a
     * given implementation group
     */
    public static CopyOnWriteArrayList<Functions> getFunctionsFor(ImplementationGroups implementationGroup) {
        CopyOnWriteArrayList<Functions> functionsList = new CopyOnWriteArrayList<>();
        for (Functions function : Functions.values()) {
            if (function.appliesToIG(implementationGroup)) {
                functionsList.add(function);
            }
        }
        return functionsList;
    }
}
