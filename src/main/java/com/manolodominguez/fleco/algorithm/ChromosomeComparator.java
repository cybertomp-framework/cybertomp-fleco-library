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
package com.manolodominguez.fleco.algorithm;

import com.manolodominguez.fleco.genetics.Chromosome;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comparator used to sort chromosomes by their weighted fitness value. Higher
 * fitness values are considered better.
 *
 * @author Manuel Domínguez-Dorado
 */
public class ChromosomeComparator implements Comparator<Chromosome> {

    private static final Logger logger = LoggerFactory.getLogger(ChromosomeComparator.class);

    /**
     * Compares two chromosomes based on their weighted fitness.
     *
     * @param chromosome1 The first chromosome to be compared.
     * @param chromosome2 The second chromosome to be compared.
     * @return -1, 0, or 1 depending on whether chromosome1 is lesser, equal, or
     * greater than chromosome2, respectively.
     */
    @Override
    public int compare(Chromosome chromosome1, Chromosome chromosome2) {

        if (chromosome1 == null || chromosome2 == null) {
            logger.error("Null chromosome detected during comparison: c1={}, c2={}", chromosome1, chromosome2);
            throw new IllegalArgumentException("Chromosomes to compare must not be null.");
        }

        // Descending order: higher fitness = better chromosome
        // Beware that the order of chromosomes is different than the one use as 
        // parameters (chromosome1, chromosome2) vs (chromosome2, chromosome1)
        return Double.compare(chromosome2.getFitness(), chromosome1.getFitness());
    }

}
