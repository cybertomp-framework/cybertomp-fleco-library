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
package com.manolodominguez.fleco.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates consecutive numeric identifiers in a cyclic manner. When the
 * maximum integer value is reached, the sequence restarts from zero.
 *
 * This class is thread-safe.
 *
 * @author Manuel Domínguez Dorado
 */
public class RotaryIDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RotaryIDGenerator.class);

    private static final int DEFAULT_ID = 0;

    /** Internal counter for ID generation. */
    private int identifier = DEFAULT_ID;

    /**
     * Creates a new ID generator initialized at zero.
     */
    public RotaryIDGenerator() {
        // No additional initialization required
    }

    /**
     * Resets the ID generator to its initial value.
     */
    public synchronized void reset() {
        identifier = DEFAULT_ID;
    }

    /**
     * Returns the next identifier in the sequence. When the maximum integer
     * value is reached, the sequence wraps around to zero.
     *
     * @return the next generated identifier.
     */
    public synchronized int getNextIdentifier() {
        if (identifier == Integer.MAX_VALUE) {
            logger.debug("Identifier reached MAX_VALUE. Wrapping to zero.");
            identifier = DEFAULT_ID;
        } else {
            identifier++;
        }
        return identifier;
    }
}