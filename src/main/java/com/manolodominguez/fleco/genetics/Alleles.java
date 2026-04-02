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
package com.manolodominguez.fleco.genetics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum define the Discrete Levels of Implementation of each expected
 * outcomes as defined in CyberTOMP proposal.
 *
 * @author Manuel Domínguez-Dorado
 */
public enum Alleles {
    DLI_0(0.0f),
    DLI_33(0.33f),
    DLI_67(0.67f),
    DLI_100(1.0f);

    private final float DLI;

    private static final Logger logger = LoggerFactory.getLogger(Alleles.class);
    
    /**
     * This is the constructor of the class. it creates the enum and assigns the
     * corresponding value.
     *
     * @param DLI Afloat value representing the discrete level of
     * implementation. A number between 0.0 and 1.0.
     */
    private Alleles(float DLI) {
        this.DLI = DLI;
    }

    /**
     * This method return the numeric value of the discrete level of
     * implementation.
     *
     * @return The numeric value of the discrete level of implementation.
     */
    public float getDLI() {
        return this.DLI;
    }

    /**
     * This method returns the first DLI that is greater than the value
     * specified as a parameter.
     *
     * @param value The reference value.
     * @return the first DLI that is greater than the value specified as a
     * parameter, if exists. Otherwise, null.
     */
    public static Alleles getGreater(Float value) {
        Alleles result = Alleles.DLI_100;
        for (Alleles allele : Alleles.values()) {
            if (allele.getDLI() > value) {
                result = allele;
                break;
            }
        }
        return result;
    }

    /**
     * This method returns the first DLI that is greater or equal than the value
     * specified as a parameter.
     *
     * @param value The reference value.
     * @return the first DLI that is greater or equal than the value specified
     * as a parameter, if exists. Otherwise, null.
     */
    public static Alleles getGreaterOrEqual(Float value) {
        Alleles result = Alleles.DLI_100;
        for (Alleles allele : Alleles.values()) {
            if (allele.getDLI() >= value) {
                result = allele;
                break;
            }
        }
        return result;
    }

    /**
     * This method returns the DLI that is equal than the value specified as a
     * parameter.
     *
     * @param value The reference value.
     * @return the first DLI equal than the value specified as a parameter, if
     * exists. Otherwise, null.
     */
    public static Alleles getEqual(Float value) {
        Alleles result = Alleles.DLI_0;
        for (Alleles allele : Alleles.values()) {
            if (allele.getDLI() == value) {
                result = allele;
                break;
            }
        }
        return result;
    }

    /**
     * This method returns the first DLI that is lesser or equal than the value
     * specified as a parameter.
     *
     * @param value The reference value.
     * @return the first DLI that is lesser or equal than the value specified as
     * a parameter, if exists. Otherwise, null.
     */
    public static Alleles getLesserOrEqual(Float value) {
        Alleles result = Alleles.DLI_0;
        for (Alleles allele : Alleles.values()) {
            if (allele.getDLI() <= value) {
                result = allele;
                break;
            }
        }
        return result;
    }

    /**
     * This method returns the first DLI that is lesser than the value specified
     * as a parameter.
     *
     * @param value The reference value.
     * @return the first DLI that is lesser than the value specified as a
     * parameter, if exists. Otherwise, null.
     */
    public static Alleles getLesser(Float value) {
        Alleles result = Alleles.DLI_0;
        for (Alleles allele : Alleles.values()) {
            if (allele.getDLI() < value) {
                result = allele;
                break;
            }
        }
        return result;
    }
}
