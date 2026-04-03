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
 * Default implementation of a FLECO progress event listener. This listener
 * simply prints the information contained in each received
 * {@link ProgressEvent}. It is intended mainly for debugging or console-based
 * monitoring.
 *
 * Applications may implement their own listener to integrate FLECO progress
 * into GUIs, dashboards, logs, or monitoring systems.
 *
 * @author Manuel Domínguez-Dorado
 */
public class DefaultProgressEventListener implements IFLECOProgressEventListener {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProgressEventListener.class);

    /**
     * Receives a progress event from a FLECO instance and prints its content.
     *
     * @param progressEvent the event containing the current evolution status.
     */
    @Override
    public void onProgressEventReceived(ProgressEvent progressEvent) {

        // Defensive check: avoids NullPointerException if someone misuses the API.
        if (progressEvent == null) {
            logger.warn("Received a null ProgressEvent. Ignoring.");
            return;
        }

        // Delegates printing to the event itself.
        progressEvent.print();
    }

}
