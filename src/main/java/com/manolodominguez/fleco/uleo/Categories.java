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
package com.manolodominguez.fleco.uleo;

import com.manolodominguez.fleco.genetics.Genes;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines all cybersecurity categories and their weights per Implementation
 * Group (IG1, IG2, IG3). Each category also carries its parent Function,
 * acronym and a short purpose description.
 *
 * <p>
 * Parameter validation has been added: constructor validates weights and
 * non-null auxiliary data; public API methods validate incoming parameters and
 * log concise messages before throwing exceptions. The public contract (method
 * names, return types and semantics) is preserved.</p>
 *
 * @author Manuel Domínguez-Dorado
 */
public enum Categories {
    ID_AM(8f / 12f, 11f / 24f, 11f / 40f, Functions.IDENTIFY, "ID.AM", "Asset management"),
    ID_BE(0f / 12f, 1f / 24f, 6f / 40f, Functions.IDENTIFY, "ID.BE", "Business environment"),
    ID_GV(1f / 12f, 3f / 24f, 5f / 40f, Functions.IDENTIFY, "ID.GV", "Governance"),
    ID_RA(1f / 12f, 4f / 24f, 9f / 40f, Functions.IDENTIFY, "ID.RA", "Risk assessment"),
    ID_RM(0f / 12f, 1f / 24f, 4f / 40f, Functions.IDENTIFY, "ID.RM", "Risk management strategy"),
    ID_SC(2f / 12f, 4f / 24f, 5f / 40f, Functions.IDENTIFY, "ID.SC", "Supply chain risk management"),
    PR_AC(7f / 29f, 11f / 61f, 14f / 80f, Functions.PROTECT, "PR.AC", "Identity management, authentication and access control"),
    PR_AT(1f / 29f, 4f / 61f, 4f / 80f, Functions.PROTECT, "PR.AT", "Awareness and training"),
    PR_DS(2f / 29f, 6f / 61f, 10f / 80f, Functions.PROTECT, "PR.DS", "Data security"),
    PR_IP(8f / 29f, 18f / 61f, 24f / 80f, Functions.PROTECT, "PR.IP", "Information protection processes and procedures"),
    PR_MA(6f / 29f, 15f / 61f, 17f / 80f, Functions.PROTECT, "PR.MA", "Maintenance"),
    PR_PT(5f / 29f, 7f / 61f, 11f / 80f, Functions.PROTECT, "PR.PT", "Protective technology"),
    DE_AE(1f / 4f, 3f / 12f, 6f / 23f, Functions.DETECT, "DE.AE", "Anomalies and events"),
    DE_CM(2f / 4f, 6f / 12f, 11f / 23f, Functions.DETECT, "DE.CM", "Security continuous monitoring"),
    DE_DP(1f / 4f, 3f / 12f, 6f / 23f, Functions.DETECT, "DE.DP", "Detection processes"),
    RS_AN(0f / 2f, 1f / 10f, 2f / 18f, Functions.RESPOND, "RS.AN", "Analysis"),
    RS_CO(1f / 2f, 2f / 10f, 3f / 18f, Functions.RESPOND, "RS.CO", "Communications"),
    RS_IM(0f / 2f, 2f / 10f, 5f / 18f, Functions.RESPOND, "RS.IM", "Improvements"),
    RS_MI(1f / 2f, 3f / 10f, 6f / 18f, Functions.RESPOND, "RS.MI", "Mitigation"),
    RS_RP(0f / 2f, 2f / 10f, 2f / 18f, Functions.RESPOND, "RS.RP", "Response planning"),
    RC_CO(0f, 0f, 1f / 6f, Functions.RECOVER, "RC.CO", "Communications"),
    RC_IM(0f, 0f, 2f / 6f, Functions.RECOVER, "RC.IM", "Improvements"),
    RC_RP(0f, 0f, 3f / 6f, Functions.RECOVER, "RC.RP", "Recovery planning");

    private final float weights[] = new float[3];
    private final Functions function;
    private final String acronym;
    private final String purpose;

    private static final Logger logger = LoggerFactory.getLogger(Categories.class);

    /**
     * Enum constructor. Validates non-null auxiliary data and weight ranges.
     *
     * @param weightIG1 weight for IG1 (0.0 - 1.0)
     * @param weightIG2 weight for IG2 (0.0 - 1.0)
     * @param weightIG3 weight for IG3 (0.0 - 1.0)
     * @param function parent Function (must not be null)
     * @param acronym short name (must not be null)
     * @param purpose purpose description (must not be null)
     * @throws IllegalArgumentException if any auxiliary parameter is null or
     * any weight is out of [0,1]
     */
    private Categories(float weightIG1, float weightIG2, float weightIG3, Functions function, String acronym, String purpose) {
        if (function == null || acronym == null || purpose == null) {
            throw new IllegalArgumentException("Function, acronym and purpose cannot be null.");
        }
        if (!isValidWeight(weightIG1) || !isValidWeight(weightIG2) || !isValidWeight(weightIG3)) {
            throw new IllegalArgumentException("Weights must be between 0.0 and 1.0.");
        }
        this.weights[ImplementationGroups.IG1.getImplementationGroupIndex()] = weightIG1;
        this.weights[ImplementationGroups.IG2.getImplementationGroupIndex()] = weightIG2;
        this.weights[ImplementationGroups.IG3.getImplementationGroupIndex()] = weightIG3;
        this.function = function;
        this.acronym = acronym;
        this.purpose = purpose;
    }

    /**
     * Checks whether a category weight is valid.
     *
     * <p>
     * A valid weight is a floating point value in the closed interval
     * {@code [0.0f, 1.0f]}. This helper is used internally during enum
     * initialization to validate the weights supplied for each Implementation
     * Group.</p>
     *
     * @param w the weight to validate
     * @return {@code true} if {@code w} is between {@code 0.0f} and
     * {@code 1.0f} (inclusive); {@code false} otherwise
     */
    private static boolean isValidWeight(float w) {
        return w >= 0.0f && w <= 1.0f;
    }

    /**
     * Returns the weight for the provided implementation group.
     *
     * @param implementationGroup the implementation group (must not be null)
     * @return weight in range [0.0, 1.0]
     * @throws NullPointerException if implementationGroup is null
     */
    public float getWeight(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Null implementationGroup passed to getWeight().");
            throw new NullPointerException("implementationGroup must not be null.");
        }
        return this.weights[implementationGroup.getImplementationGroupIndex()];
    }

    /**
     * Returns the acronym (short name) of this category.
     *
     * @return acronym (never null)
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * Returns the purpose (short description) of this category.
     *
     * @return purpose (never null)
     */
    public String getPurpose() {
        return this.purpose;
    }

    /**
     * Returns the parent Function of this category.
     *
     * @return function (never null)
     */
    public Functions getFunction() {
        return this.function;
    }

    /**
     * Returns whether this category applies to the provided implementation
     * group.
     *
     * @param implementationGroup the implementation group (must not be null)
     * @return true if weight for that IG is greater than 0.0, which means the
     * category applies to the implementation group.
     * @throws NullPointerException if implementationGroup is null
     */
    public boolean appliesToIG(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Null implementationGroup passed to appliesToIG().");
            throw new NullPointerException("implementationGroup must not be null.");
        }
        return weights[implementationGroup.getImplementationGroupIndex()] > 0.0f;
    }

    /**
     * Returns the list of Genes that belong to this category and apply to the
     * given implementation group.
     *
     * @param implementationGroup the implementation group (must not be null)
     * @return LinkedList of Genes (never null, may be empty)
     * @throws NullPointerException if implementationGroup is null
     */
    public LinkedList<Genes> getGenes(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Null implementationGroup passed to getGenes().");
            throw new NullPointerException("implementationGroup must not be null.");
        }
        LinkedList<Genes> genes = new LinkedList<>();
        for (Genes g : Genes.values()) {
            if ((g.getCategory() == this) && (g.getWeight(implementationGroup) > 0.0f)) {
                genes.add(g);
            }
        }
        return genes;
    }

    /**
     * Returns the list of Categories that belong to the given Function and
     * apply to the provided implementation group.
     *
     * <p>
     * Compact parameter check: logs and throws {@link IllegalArgumentException}
     * when either parameter is null.</p>
     *
     * @param function the Function (must not be null)
     * @param implementationGroup the implementation group (must not be null)
     * @return thread-safe list of Categories (never null, may be empty)
     * @throws IllegalArgumentException if function or implementationGroup is
     * null
     */
    public static CopyOnWriteArrayList<Categories> getCategoriesFor(Functions function, ImplementationGroups implementationGroup) {
        if (function == null || implementationGroup == null) {
            logger.error("Null function or implementationGroup passed to getCategoriesFor(). function={}, implementationGroup={}", function, implementationGroup);
            throw new IllegalArgumentException("Function and implementationGroup cannot be null.");
        }
        CopyOnWriteArrayList<Categories> categoriesList = new CopyOnWriteArrayList<>();
        for (Categories category : Categories.values()) {
            if (category.appliesToIG(implementationGroup) && (category.getFunction() == function)) {
                categoriesList.add(category);
            }
        }
        return categoriesList;
    }

    /**
     * Returns the list of Genes that belong to the given Function and apply to
     * the provided implementation group.
     *
     * <p>
     * Compact parameter check: logs and throws {@link IllegalArgumentException}
     * when either parameter is null.</p>
     *
     * @param function the Function (must not be null)
     * @param implementationGroup the implementation group (must not be null)
     * @return thread-safe list of Genes (never null, may be empty)
     * @throws IllegalArgumentException if function or implementationGroup is
     * null
     */
    public static CopyOnWriteArrayList<Genes> getGenesFor(Functions function, ImplementationGroups implementationGroup) {
        if (function == null || implementationGroup == null) {
            logger.error("Null function or implementationGroup passed to getGenesFor(). function={}, implementationGroup={}", function, implementationGroup);
            throw new IllegalArgumentException("Function and implementationGroup cannot be null.");
        }
        CopyOnWriteArrayList<Genes> genesList = new CopyOnWriteArrayList<>();
        for (Categories category : Categories.getCategoriesFor(function, implementationGroup)) {
            genesList.addAll(Genes.getGenesFor(category, implementationGroup));
        }
        return genesList;
    }

}
