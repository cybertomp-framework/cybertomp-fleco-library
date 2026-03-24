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
package com.manolodominguez.fleco.uleo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum defines the functional areas involved on cybersecurity as defined
 * in CyberTOMP proposal. Additional descriptions and auxiliar data is provided
 * for each.
 *
 * @author manuel Domínguez-Dorado
 */
public enum FunctionalAreas {
    FA1("Physical security", "Security of Internet of Things (IoT) devices."),
    FA2("Security operation", "Implementation of active defense measures, vulnerabilities management, threat hunting, Security Information and Event Management (SIEM) operation, activities within a CyberSOC, and incident response."),
    FA3("User education", "Human resources preparation regarding cybersecurity threats through continuous training and its reinforcement, as well as the design and execution of practical cybersecurity exercises"),
    FA4("Threat intelligence", "Analysis of internal and external threats, exchange of threat intelligence with third parties, and preparation and incorporation of Indicators of Compromise (IoCs)."),
    FA5("Governance", "Surveillance of the applicable regulation and its incorporation into cybersecurity. Key Performance Indicators (KPI) monitoring, establishment of strategies, policies, standards, processes, procedures, and corporate instructions."),
    FA6("Enterprise risk management", "Risk treatment, business continuity management, crisis management, establishing the organization’s position regarding cyber risks, insurance contracting, risk registration, auditing, definition of groups of risk management, and definition of those responsible and owners of the processes and assets."),
    FA7("Risk assesment", "Cybersecurity risk analysis, vulnerability scanning, supply chain risk identification and analysis, asset inventory, risk monitoring, penetration testing of infrastructure, people, or information systems."),
    FA8("Application security", "Leading the secure software development cycle, continuous integration and deployment, user experience security, software quality, API security, identification of information flows in information systems, management of the free software used and the static or dynamic analysis of the code."),
    FA9("Frameworks and standards", "Management, development, implementation, and verification of compliance with the standards and regulations defined at the corporate level for cybersecurity: CIS controls , CIS Community Defense Model , MITRE matrix , , NIST framework for the improvement of cybersecurity of critical infrastructures or the family of standards ISO27000, CyberTOMP."),
    FA10("Security architecture", "Management, definition, implementation, operation, prevention, etc., in relation to cryptography, key and certificate management, encryption standards, security engineering, access controls with or without multiple authentication factors, single sign-on, privileged access management, identity management, identity federation, cloud security, container security, endpoint security, data protection and prevention of data leakage, network design to prevent distributed denial of service attacks, development and secure configuration of systems, patch and update management and the establishment of secure reference configurations."),
    FA11("Career development", "Promote study, education and training, attendance at conferences and participation in related professional groups, training, or certification."),
    FA12("Communication and relationships", "Internal and external corporate communication, social networks management, marketing and the establishment and maintenance of institutional relationship with interested third parties with whom the organization maintains some type of contact."),
    SEVERAL("Several functional areas", "The work to be done cannot be defined at this level and, therefore, are detailed in the corresponding nested metrics.");

    private String mainReponsibilities = "";
    private String areaName = "";

    private final Logger logger = LoggerFactory.getLogger(FunctionalAreas.class);
    
    /**
     * This is the constructor of the class. it creates the enum and assigns the
     * corresponding values.
     *
     * @param name the short name of the functional area.
     * @param mainReponsibilities the main responsibilities the corresponding
     * functional area must be in charge of.
     */
    private FunctionalAreas(String name, String mainReponsibilities) {
        this.areaName = name;
        this.mainReponsibilities = mainReponsibilities;
    }

    /**
     * This method return the short name of the functional area.
     *
     * @return the short name of the functional area.
     */
    public String getAreaName() {
        if (name().equals("SEVERAL")) {
            return this.areaName;
        }
        return name() + " - " + this.areaName;
    }

    /**
     * This method return the main responsibilities the corresponding functional
     * area must be in charge of.
     *
     * @return the main responsibilities the corresponding functional area must
     * be in charge of.
     */
    public String getMainResponsibilities() {
        return this.mainReponsibilities;
    }

}
