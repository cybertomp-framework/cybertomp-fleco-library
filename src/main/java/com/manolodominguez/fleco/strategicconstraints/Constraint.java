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
package com.manolodominguez.fleco.strategicconstraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a strategic constraint that consist of a comparison
 * operator and a thresshold value it refers to.
 *
 * @author Manuel Domínguez-Dorado
 */
public class Constraint {

    private ComparisonOperators comparisonOperator;
    private float thresshold;

    private final Logger logger = LoggerFactory.getLogger(Constraint.class);

    /**
     * This is the constructor of the class. It sets the initial values of all
     * attributes and create a new instance.
     *
     * @param operator A comparison operator.
     * @param thresshold A normalized float value, between 0.0 and 1.0, that
     * represents a percentaje between 0% and 100% and is related to the defined
     * operator.
     */
    public Constraint(ComparisonOperators operator, float thresshold) {
        this.comparisonOperator = operator;
        this.thresshold = thresshold;
    }

    /**
     * This method returns the the thresshold of this strategic constraint.
     *
     * @return the thresshold of this strategic constraint.
     */
    public float getThreshold() {
        return thresshold;
    }

    /**
     * This method returns the comparison operator of this strategic constraint.
     *
     * @return the comparison operator of this strategic constraint.
     */
    public ComparisonOperators getComparisonOperator() {
        return comparisonOperator;
    }
}
