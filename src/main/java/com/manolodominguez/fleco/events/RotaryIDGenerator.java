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
 * This class implements a ID generator that generates consecutive numeric IDs,
 * in a cycle that never ends.
 *
 * @author Manuel Domínguez Dorado
 */
@SuppressWarnings("serial")
public class RotaryIDGenerator {

    private static final int DEFAULT_ID = 0;

    private int identifier;

    private final Logger logger = LoggerFactory.getLogger(RotaryIDGenerator.class);
    
    /**
     * This method is the constructor of the class. It is create a new instance.
     */
    public RotaryIDGenerator() {
        identifier = DEFAULT_ID;
    }

    /**
     * This method resets the ID generator to its initial internal value.
     */
    public synchronized void reset() {
        identifier = DEFAULT_ID;
    }

    /**
     * This method generates a new ID.
     *
     * @return the next identifier, as an integer value.
     */
    synchronized public int getNextIdentifier() {
        if (identifier >= Integer.MAX_VALUE) {
            identifier = DEFAULT_ID;
        } else {
            identifier++;
        }
        return (identifier);
    }

}
