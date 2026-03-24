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

import com.manolodominguez.fleco.algorithm.FLECO;
import java.time.Instant;
import java.util.EventObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the superclass of all events generated in FLECO. It is an
 * abstract class that has to be implemented by every subclass.
 *
 * @author Manuel Domínguez Dorado
 */
@SuppressWarnings("serial")
public abstract class FLECOEvent extends EventObject implements Comparable<FLECOEvent> {

    private long eventID;
    private Instant instant;

    private final Logger logger = LoggerFactory.getLogger(FLECOEvent.class);
    
    /**
     * This is the constructor of the class that will be called by all
     * subclasses to create a new event in FLECO.
     *
     * @param instant Every events includes the moment of their generation, in
     * nanoseconds. It allow syncronizing everything that is happening during a
     * simulation.
     * @param source The object that generates the event.
     * @param eventID The unique event identifier.
     */
    public FLECOEvent(FLECO source, long eventID, Instant instant) {
        super(source);
        this.eventID = eventID;
        this.instant = instant;
    }

    /**
     * This method gets the instant in wich the event was generated.
     *
     * @return the instant in wich the event was generated.
     */
    public Instant getInstant() {
        return this.instant;
    }

    /**
     * This method gets the event unique identifier.
     *
     * @return the event unique identifier.
     */
    public long getEventID() {
        return this.eventID;
    }

    /**
     * This method sets the event unique identifier.
     *
     * @param eventID the event unique identifier.
     */
    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    /**
     * This method compares the current instance to another instance of
     * FLECOEvent to know the ordinal position of one with respect the other.
     *
     * @param anotherEvent a FLECOEvent instance to be compared to the current
     * one.
     * @return -1, 0 or 1 depending on whether the current instance is lesser,
     * equal or greater than the one specified as an argument.
     */
    @Override
    public int compareTo(FLECOEvent anotherEvent) {
        if (instant.toEpochMilli() < anotherEvent.getInstant().toEpochMilli()) {
            return -1;
        } else if (instant.toEpochMilli() > anotherEvent.getInstant().toEpochMilli()) {
            return 1;
        } else {
            if (getEventID() < anotherEvent.getEventID()) {
                return -1;
            } else if (getEventID() == anotherEvent.getEventID()) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * This is an abstract method that should gets the event type of this event.
     * It will be one of the enum defined in EventTypes.
     *
     * @return the event type of this event. It will be one of the enum defined
     * in EventTypes.
     */
    public abstract EventTypes getType();
}
