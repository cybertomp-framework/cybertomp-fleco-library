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

/**
 * Represents the discrete levels of implementation (DLI) defined in the
 * CyberTOMP proposal. Each allele corresponds to a numeric value between 0.0
 * and 1.0.
 *
 * The enumeration is ordered from lowest to highest DLI. This ordering is
 * validated at class-loading time to ensure correctness of threshold-based
 * queries.
 *
 * None of the lookup methods return {@code null}. Instead, they return the
 * closest boundary value when no exact match is found:
 *
 * {@code getGreater(value)} → returns {@code DLI_100} if none is greater  
 * {@code getGreaterOrEqual(value)} → returns {@code DLI_100} if none is greater or equal  
 * {@code getEqual(value)} → returns {@code DLI_0} if no exact match  
 * {@code getLesserOrEqual(value)} → returns {@code DLI_0} if none is less or equal  
 * {@code getLesser(value)} → returns {@code DLI_0} if none is less than the given value
 *
 * @author Manuel Domínguez-Dorado
 */
public enum Alleles {

    DLI_0(0.0f),
    DLI_33(0.33f),
    DLI_67(0.67f),
    DLI_100(1.0f);

    private final float dli;

    Alleles(float dli) {
        this.dli = dli;
    }

    /**
     * Ensures that the enum values are declared in ascending order of DLI. This
     * validation runs once when the class is loaded.
     */
    static {
        Alleles[] values = Alleles.values();
        for (int i = 1; i < values.length; i++) {
            if (values[i].dli < values[i - 1].dli) {
                throw new IllegalStateException(
                        "Alleles enum values must be declared in ascending DLI order."
                );
            }
        }
    }

    /**
     * Returns the numeric value of this discrete level of implementation.
     *
     * @return the DLI value as a float between 0.0 and 1.0.
     */
    public float getDLI() {
        return dli;
    }

    /**
     * Returns the first allele whose DLI is strictly greater than the given
     * value. If no allele satisfies the condition, {@link #DLI_100} is
     * returned.
     *
     * @param value the reference value.
     * @return the first allele with DLI greater than the given value,
     *         or {@code DLI_100} if none exists.
     */
    public static Alleles getGreater(float value) {
        for (Alleles allele : values()) {
            if (allele.dli > value) {
                return allele;
            }
        }
        return DLI_100;
    }

    /**
     * Returns the first allele whose DLI is greater than or equal to the given
     * value. If no allele satisfies the condition, {@link #DLI_100} is
     * returned.
     *
     * @param value the reference value.
     * @return the first allele with DLI greater than or equal to the given value,
     *         or {@code DLI_100} if none exists.
     */
    public static Alleles getGreaterOrEqual(float value) {
        for (Alleles allele : values()) {
            if (allele.dli >= value) {
                return allele;
            }
        }
        return DLI_100;
    }

    /**
     * Returns the allele whose DLI is exactly equal to the given value. If no
     * allele matches exactly, {@link #DLI_0} is returned.
     *
     * @param value the reference value.
     * @return the allele with DLI equal to the given value,
     *         or {@code DLI_0} if none exists.
     */
    public static Alleles getEqual(float value) {
        for (Alleles allele : values()) {
            if (allele.dli == value) {
                return allele;
            }
        }
        return DLI_0;
    }

    /**
     * Returns the first allele whose DLI is less than or equal to the given
     * value. If no allele satisfies the condition, {@link #DLI_0} is returned.
     *
     * @param value the reference value.
     * @return the first allele with DLI less than or equal to the given value,
     *         or {@code DLI_0} if none exists.
     */
    public static Alleles getLesserOrEqual(float value) {
        for (Alleles allele : values()) {
            if (allele.dli <= value) {
                return allele;
            }
        }
        return DLI_0;
    }

    /**
     * Returns the first allele whose DLI is strictly less than the given value.
     * If no allele satisfies the condition, {@link #DLI_0} is returned.
     *
     * @param value the reference value.
     * @return the first allele with DLI less than the given value,
     *         or {@code DLI_0} if none exists.
     */
    public static Alleles getLesser(float value) {
        for (Alleles allele : values()) {
            if (allele.dli < value) {
                return allele;
            }
        }
        return DLI_0;
    }
}