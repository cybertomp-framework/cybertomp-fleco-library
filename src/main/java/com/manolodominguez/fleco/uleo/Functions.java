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

import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum defines all cybersecurity functions and their weights per
 * Implementation Group (IG1, IG2, IG3). Each function also carries an acronym
 * and a short purpose description.
 *
 * <p>
 * Parameter validation has been added: enum constants are validated at
 * initialization and public API methods validate incoming parameters and log
 * concise messages before throwing exceptions. The public contract (method
 * names, return types and semantics) is preserved.</p>
 *
 * @author Manuel Domínguez-Dorado
 */
public enum Functions {
    IDENTIFY(4f / 15f, 6f / 20f, 6f / 23f, "ID", "Develop an organizational understanding to manage cybersecurity risk to systems, people, assets, data, and capabilities."),
    PROTECT(6f / 15f, 6f / 20f, 6f / 23f, "PR", "Develop and implement appropriate safeguards to ensure delivery of critical services."),
    DETECT(3f / 15f, 3f / 20f, 3f / 23f, "DE", "Develop and implement appropriate activities to identify the occurrence of a cybersecurity event."),
    RESPOND(2f / 15f, 5f / 20f, 5f / 23f, "RS", "Develop and implement appropriate activities to take action regarding a detected cybersecurity incident."),
    RECOVER(0f / 15f, 0f / 20f, 3f / 23f, "RC", "Develop and implement appropriate activities to maintain plans for resilience and to restore any capabilities or services that were impaired due to a cybersecurity incident");

    private final float weights[] = new float[3];
    private final String acronym;
    private final String purpose;

    private static final Logger logger = LoggerFactory.getLogger(Functions.class);

    /**
     * Enum constructor. Validates non-null auxiliary data and weight ranges.
     *
     * @param weightIG1 weight for IG1 (0.0 - 1.0)
     * @param weightIG2 weight for IG2 (0.0 - 1.0)
     * @param weightIG3 weight for IG3 (0.0 - 1.0)
     * @param acronym short name (must not be null)
     * @param purpose purpose description (must not be null)
     * @throws IllegalArgumentException if acronym or purpose is null or any
     * weight is out of [0,1]
     */
    private Functions(float weightIG1, float weightIG2, float weightIG3, String acronym, String purpose) {
        if (acronym == null || purpose == null) {
            throw new IllegalArgumentException("Acronym and purpose cannot be null.");
        }
        if (!isValidWeight(weightIG1) || !isValidWeight(weightIG2) || !isValidWeight(weightIG3)) {
            throw new IllegalArgumentException("Weights must be between 0.0 and 1.0.");
        }

        this.weights[ImplementationGroups.IG1.getImplementationGroupIndex()] = weightIG1;
        this.weights[ImplementationGroups.IG2.getImplementationGroupIndex()] = weightIG2;
        this.weights[ImplementationGroups.IG3.getImplementationGroupIndex()] = weightIG3;

        this.acronym = acronym;
        this.purpose = purpose;
    }

    /**
     * Checks whether a weight value is valid.
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
     * Returns the very short name (acronym) of this function.
     *
     * @return the acronym (never null)
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * Returns the main purpose of this function.
     *
     * @return the purpose (never null)
     */
    public String getPurpose() {
        return this.purpose;
    }

    /**
     * Given an implementation group, returns whether this function applies.
     *
     * @param implementationGroup the applicable implementation group (must not
     * be null)
     * @return true if the function applies for the given IG
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
     * Given an implementation group, returns the list of Categories that belong
     * to this function and apply to that implementation group.
     *
     * @param implementationGroup the applicable implementation group (must not
     * be null)
     * @return thread-safe list of Categories (never null, may be empty)
     * @throws NullPointerException if implementationGroup is null
     */
    public CopyOnWriteArrayList<Categories> getCategories(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Null implementationGroup passed to getCategories().");
            throw new NullPointerException("implementationGroup must not be null.");
        }
        CopyOnWriteArrayList<Categories> categories = new CopyOnWriteArrayList<>();
        for (Categories category : Categories.values()) {
            if ((category.getFunction() == this) && (category.getWeight(implementationGroup) > 0.0f)) {
                categories.add(category);
            }
        }
        return categories;
    }

    /**
     * Returns the list of Functions that are applicable for the given
     * implementation group.
     *
     * @param implementationGroup the applicable implementation group (must not
     * be null)
     * @return thread-safe list of Functions (never null, may be empty)
     * @throws NullPointerException if implementationGroup is null
     */
    public static CopyOnWriteArrayList<Functions> getFunctionsFor(ImplementationGroups implementationGroup) {
        if (implementationGroup == null) {
            logger.error("Null implementationGroup passed to getFunctionsFor().");
            throw new NullPointerException("implementationGroup must not be null.");
        }
        CopyOnWriteArrayList<Functions> functionsList = new CopyOnWriteArrayList<>();
        for (Functions function : Functions.values()) {
            if (function.appliesToIG(implementationGroup)) {
                functionsList.add(function);
            }
        }
        return functionsList;
    }
}
