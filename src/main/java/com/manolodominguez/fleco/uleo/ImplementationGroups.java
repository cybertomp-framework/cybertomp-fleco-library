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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum defines all implementation groups as defined in the CyberTOMP®
 * proposal.
 *
 * <p>
 * Each constant carries an integer index used to address arrays of weights (IG1
 * -> 0, IG2 -> 1, IG3 -> 2). The constructor validates the supplied index to
 * catch accidental mistakes at initialization time.</p>
 *
 * @author Manuel Domínguez-Dorado
 */
public enum ImplementationGroups {
    IG1(0),
    IG2(1),
    IG3(2);

    private final int implementationGroupIndex;

    private static final Logger logger = LoggerFactory.getLogger(ImplementationGroups.class);

    /**
     * Enum constructor.
     *
     * @param implementationGroupIndex The index of this implementation group,
     * used as an array index afterwards.
     * @throws IllegalArgumentException if the index is out of the valid range
     * [0,2]
     */
    private ImplementationGroups(int implementationGroupIndex) {
        if (!isValidIndex(implementationGroupIndex)) {
            throw new IllegalArgumentException("implementationGroupIndex must be 0, 1 or 2.");
        }
        this.implementationGroupIndex = implementationGroupIndex;
    }

    /**
     * Returns the index associated to this implementation group.
     *
     * @return the index associated to this implementation group (0..2)
     */
    public int getImplementationGroupIndex() {
        return this.implementationGroupIndex;
    }

    /**
     * Validates an implementation group index.
     *
     * <p>
     * Valid indices are 0, 1 and 2. This helper is private because it is only
     * used during enum initialization to ensure constants are created with
     * correct indices.</p>
     *
     * @param idx the index to validate
     * @return {@code true} if {@code idx} is 0, 1 or 2; {@code false} otherwise
     */
    private static boolean isValidIndex(int idx) {
        return idx >= 0 && idx <= 2;
    }
}
