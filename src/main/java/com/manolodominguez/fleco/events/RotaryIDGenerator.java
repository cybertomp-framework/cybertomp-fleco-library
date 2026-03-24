/* 
 *******************************************************************************
 * FLECO (Fast, Lightweight, and Efficient Cybersecurity Optimization) (1) 
 * Adaptive, Constrained, and Multi-objective Genetic Algorithm is a genetic 
 * algorithm designed to assist the Asset's Cybersecurity Committee (ACC) in 
 * making decisions during the application of CyberTOMP (2), aimed at managing 
 * comprehensive cybersecurity at both tactical and operational levels.
 *
 * (1) Domínguez-Dorado, M.; Cortés-Polo, D.; Carmona-Murillo, J.; 
 * Rodríguez-Pérez, F.J.; Galeano-Brajones, J. Fast, Lightweight, and Efficient 
 * Cybersecurity Optimization for Tactical–Operational Management. Appl. Sci. 
 * 2023, 13, 6327. https://doi.org/10.3390/app13106327
 *
 * (2) Dominguez-Dorado, M., Carmona-Murillo, J., Cortés-Polo, D., and
 * Rodríguez-Pérez, F. J. (2022). CyberTOMP: A novel systematic framework to
 * manage asset-focused cybersecurity from tactical and operational levels. IEEE
 * Access, 10, 122454-122485. https://doi.org/10.1109/ACCESS.2022.3223440
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
