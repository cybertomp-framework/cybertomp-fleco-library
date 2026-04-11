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

/**
 * Represents a strategic constraint composed of a comparison operator and a
 * normalized threshold value. Constraints are used within the CyberTOMP®
 * Framework and the FLECO genetic algorithm to evaluate whether a given
 * cybersecurity metric or expected outcome satisfies a strategic requirement.
 */
public final class Constraint {

    /**
     * The comparison operator used by this constraint.
     */
    private final ComparisonOperators comparisonOperator;

    /**
     * A normalized threshold value between 0.0 and 1.0, representing a
     * percentage between 0% and 100%.
     */
    private final float threshold;

    /**
     * Creates a new strategic constraint.
     *
     * @param operator the comparison operator (must not be null)
     * @param threshold a normalized value between 0.0 and 1.0
     *
     * @throws IllegalArgumentException if the operator is null or the threshold
     * is not a finite value within [0.0, 1.0]
     */
    public Constraint(ComparisonOperators operator, float threshold) {

        if (operator == null) {
            throw new IllegalArgumentException("Comparison operator cannot be null.");
        }

        if (Float.isNaN(threshold) || Float.isInfinite(threshold)) {
            throw new IllegalArgumentException("Threshold must be a finite number.");
        }

        if (threshold < 0.0f || threshold > 1.0f) {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 1.0.");
        }

        this.comparisonOperator = operator;
        this.threshold = threshold;
    }

    /**
     * Returns the threshold of this strategic constraint.
     *
     * @return the normalized threshold value.
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * Returns the comparison operator of this strategic constraint.
     *
     * @return the comparison operator.
     */
    public ComparisonOperators getComparisonOperator() {
        return comparisonOperator;
    }
}
