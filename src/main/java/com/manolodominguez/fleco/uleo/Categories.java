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

import com.manolodominguez.fleco.genetics.Genes;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enum defines all cybersecurity categories and also its weights as
 * defined in CyberTOMP proposal, depending on whether implementation groups 1,
 * 2, or 3 applies. Additional descriptions and auxiliar data is provided for
 * each.
 *
 * @author manuel Domínguez-Dorado
 */
public enum Categories {
    ID_AM((float) 8 / 12, (float) 11 / 24, (float) 11 / 40, Functions.IDENTIFY, "ID.AM", "Asset management"),
    ID_BE((float) 0 / 12, (float) 1 / 24, (float) 6 / 40, Functions.IDENTIFY, "ID.BE", "Business environment"),
    ID_GV((float) 1 / 12, (float) 3 / 24, (float) 5 / 40, Functions.IDENTIFY, "ID.GV", "Governance"),
    ID_RA((float) 1 / 12, (float) 4 / 24, (float) 9 / 40, Functions.IDENTIFY, "ID.RA", "Risk assessment"),
    ID_RM((float) 0 / 12, (float) 1 / 24, (float) 4 / 40, Functions.IDENTIFY, "ID.RM", "Risk management strategy"),
    ID_SC((float) 2 / 12, (float) 4 / 24, (float) 5 / 40, Functions.IDENTIFY, "ID.SC", "Supply chain risk management"),
    PR_AC((float) 7 / 29, (float) 11 / 61, (float) 14 / 80, Functions.PROTECT, "PR.AC", "Identity management, authentication and access control"),
    PR_AT((float) 1 / 29, (float) 4 / 61, (float) 4 / 80, Functions.PROTECT, "PR.AT", "Awareness and training"),
    PR_DS((float) 2 / 29, (float) 6 / 61, (float) 10 / 80, Functions.PROTECT, "PR.DS", "Data security"),
    PR_IP((float) 8 / 29, (float) 18 / 61, (float) 24 / 80, Functions.PROTECT, "PR.IP", "Information protection processes and procedures"),
    PR_MA((float) 6 / 29, (float) 15 / 61, (float) 17 / 80, Functions.PROTECT, "PR.MA", "Maintenance"),
    PR_PT((float) 5 / 29, (float) 7 / 61, (float) 11 / 80, Functions.PROTECT, "PR.PT", "Protective technology"),
    DE_AE((float) 1 / 4, (float) 3 / 12, (float) 6 / 23, Functions.DETECT, "DE.AE", "Anomalies and events"),
    DE_CM((float) 2 / 4, (float) 6 / 12, (float) 11 / 23, Functions.DETECT, "DE.CM", "Security continuous monitoring"),
    DE_DP((float) 1 / 4, (float) 3 / 12, (float) 6 / 23, Functions.DETECT, "DE.DP", "Detection processes"),
    RS_AN((float) 0 / 2, (float) 1 / 10, (float) 2 / 18, Functions.RESPOND, "RS.AN", "Analysis"),
    RS_CO((float) 1 / 2, (float) 2 / 10, (float) 3 / 18, Functions.RESPOND, "RS.CO", "Communications"),
    RS_IM((float) 0 / 2, (float) 2 / 10, (float) 5 / 18, Functions.RESPOND, "RS.IM", "Improvements"),
    RS_MI((float) 1 / 2, (float) 3 / 10, (float) 6 / 18, Functions.RESPOND, "RS.MI", "Mitigation"),
    RS_RP((float) 0.0f / 2.0f, (float) 2 / 10, (float) 2 / 18, Functions.RESPOND, "RS.RP", "Response planning"),
    RC_CO((float) 0.0f, (float) 0.0f, (float) 1 / 6, Functions.RECOVER, "RC.CO", "Communications"),
    RC_IM((float) 0.0f, (float) 0.0f, (float) 2 / 6, Functions.RECOVER, "RC.IM", "Improvements"),
    RC_RP((float) 0.0f, (float) 0.0f, (float) 3 / 6, Functions.RECOVER, "RC.RP", "Recovery planning");

    private final float weights[] = new float[3];
    private Functions function = Functions.DETECT;
    private String acronym = "";
    private String purpose = "";

    private final Logger logger = LoggerFactory.getLogger(Categories.class);
    
    /**
     * This is the constructor of the class. it creates the enum and assigns the
     * corresponding values.
     *
     * @param weightIG1 A float value representing the weight of this
     * cybersecurity category when applying implementation group 1. A number
     * between 0.0 and 1.0.
     * @param weightIG2 A float value representing the weight of this
     * cybersecurity category when applying implementation group 2. A number
     * between 0.0 and 1.0.
     * @param weightIG3 A float value representing the weight of this
     * cybersecurity category when applying implementation group 3. A number
     * between 0.0 and 1.0.
     * @param category The cybersecurity function the category belongs to.
     * @param acronym the very short name of this category.
     * @param purpose the main purpose of this of this category.
     */
    private Categories(float weightIG1, float weightIG2, float weightIG3, Functions function, String acronym, String purpose) {
        this.weights[ImplementationGroups.IG1.getImplementationGroupIndex()] = weightIG1;
        this.weights[ImplementationGroups.IG2.getImplementationGroupIndex()] = weightIG2;
        this.weights[ImplementationGroups.IG3.getImplementationGroupIndex()] = weightIG3;
        this.function = function;
        this.acronym = acronym;
        this.purpose = purpose;
    }

    /**
     * This method returns the weight of this cybersecurity category taking into
     * consideration the impleentation group that applies.
     *
     * @param implementationGroup The implementation group that applies.
     * @return the weight of the cybersecurity category taking into
     * consideration the impleentation group that applies.
     */
    public float getWeight(ImplementationGroups implementationGroup) {
        return this.weights[implementationGroup.getImplementationGroupIndex()];
    }

    /**
     * This method returns the very short name of this category.
     *
     * @return the very short name of this category.
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * This method returns the main purpose of this of this category.
     *
     * @return the main purpose of this of this category.
     */
    public String getPurpose() {
        return this.purpose;
    }

    /**
     * This method returns the cybersecurity function the cybersecurity category
     * belongs to.
     *
     * @return the cybersecurity function the cybersecurity category belongs to.
     */
    public Functions getFunction() {
        return this.function;
    }

    /**
     * Given an implementation group, this method returns whether the
     * cybersecurity category applies for it or not.
     *
     * @param implementationGroup The applicable implementation group.
     * @return true, if the cybersecurity category applies. Otherwise, false.
     */
    public boolean appliesToIG(ImplementationGroups implementationGroup) {
        return weights[implementationGroup.getImplementationGroupIndex()] > 0.0f;
    }

    /**
     * Given an implementation group, this method returns a list of
     * genes/expected outcomes that applies to that implementation group and
     * belongs to the cybersecurity category.
     *
     * @param implementationGroup The applicable implementation group.
     * @return a list of genes/expected outcomes that applies to that
     * implementation group and belongs to the cybersecurity category.
     */
    public LinkedList<Genes> getGenes(ImplementationGroups implementationGroup) {
        LinkedList<Genes> genes = new LinkedList<>();
        for (Genes g : Genes.values()) {
            if ((g.getCategory() == this) && (g.getWeight(implementationGroup) > 0.0f)) {
                genes.add(g);
            }
        }
        return genes;
    }

    /**
     * This method returns the list of cybersecurity categories that belongs to
     * a given cybersecurity function and are applicable for a given
     * implementation group.
     *
     * @param function The cybersecurity function whose cbyersecurity categories
     * are required.
     * @param implementationGroup The applicable implementation group.
     * @return the list of cybersecurity categories that belongs to a given
     * cybersecurity function and are applicable for a given implementation
     * group.
     */
    public static CopyOnWriteArrayList<Categories> getCategoriesFor(Functions function, ImplementationGroups implementationGroup) {
        CopyOnWriteArrayList<Categories> categoriesList = new CopyOnWriteArrayList<>();
        for (Categories category : Categories.values()) {
            if (category.appliesToIG(implementationGroup) && (category.getFunction() == function)) {
                categoriesList.add(category);
            }
        }
        return categoriesList;
    }

    /**
     * This method returns the list of genes/expected outcomes that belongs to a
     * given cybersecurity function and are applicable for a given
     * implementation group.
     *
     * @param function The cybersecurity function whose cybersecurity categories
     * are required.
     * @param implementationGroup The applicable implementation group.
     * @return the list of genes/expected outcomes that belongs to a given
     * cybersecurity function and are applicable for a given implementation
     * group.
     */
    public static CopyOnWriteArrayList<Genes> getGenesFor(Functions function, ImplementationGroups implementationGroup) {
        CopyOnWriteArrayList<Genes> genesList = new CopyOnWriteArrayList<>();
        for (Categories category : Categories.getCategoriesFor(function, implementationGroup)) {
            genesList.addAll(Genes.getGenesFor(category, implementationGroup));
        }
        return genesList;
    }

}
