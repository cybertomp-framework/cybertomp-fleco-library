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

import com.manolodominguez.fleco.algorithm.FLECO;
import java.time.Instant;
import java.util.EventObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the superclass of all events generated in FLECO. It is an
 * abstract class that must be extended by every specific event type.
 *
 * Each event stores: - A unique event identifier - The instant when the event
 * was generated - The FLECO instance that produced it
 *
 * Events are comparable based on their timestamp and, if equal, by their ID.
 * This ensures deterministic ordering even when multiple events occur at the
 * same instant.
 *
 * @author Manuel Domínguez Dorado
 */
public abstract class FLECOEvent extends EventObject implements Comparable<FLECOEvent> {

    private long eventID;
    private final Instant instant;

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(FLECOEvent.class);

    /**
     * Constructor used by all subclasses to create a new FLECO event.
     *
     * @param source The FLECO instance that generated the event.
     * @param eventID The unique event identifier.
     * @param instant The instant when the event was generated.
     */
    public FLECOEvent(FLECO source, long eventID, Instant instant) {
        super(source);

        if (source == null) {
            logger.error("Null FLECO source provided to FLECOEvent.");
            throw new IllegalArgumentException("source must not be null.");
        }

        if (instant == null) {
            logger.error("Null Instant provided to FLECOEvent.");
            throw new IllegalArgumentException("instant must not be null.");
        }

        if (eventID < 0) {
            logger.error("Invalid eventID: {}", eventID);
            throw new IllegalArgumentException("eventID must be non-negative.");
        }

        this.eventID = eventID;
        this.instant = instant;
    }

    /**
     * Returns the instant when the event was generated.
     *
     * @return the instant when the event was generated.
     */
    public Instant getInstant() {
        return this.instant;
    }

    /**
     * Returns the unique event identifier.
     *
     * @return the event unique identifier.
     */
    public long getEventID() {
        return this.eventID;
    }

    /**
     * Sets the event unique identifier.
     *
     * @param eventID the event unique identifier.
     */
    public void setEventID(long eventID) {
        if (eventID < 0) {
            logger.error("Attempted to set invalid eventID: {}", eventID);
            throw new IllegalArgumentException("eventID must be non-negative.");
        }
        this.eventID = eventID;
    }

    /**
     * Compares this event with another event based on: 1. The instant of
     * generation 2. The event ID (as a tie-breaker)
     *
     * @param anotherEvent the event to compare with.
     * @return -1, 0, or 1 depending on ordering.
     */
    @Override
    public int compareTo(FLECOEvent anotherEvent) {
        int timeComparison = this.instant.compareTo(anotherEvent.instant);
        if (timeComparison != 0) {
            return timeComparison;
        }
        return Long.compare(this.eventID, anotherEvent.eventID);
    }

    /**
     * Returns the type of this event. It must be one of the values defined in
     * {@link EventTypes}.
     *
     * @return the event type.
     */
    public abstract EventTypes getType();
}
