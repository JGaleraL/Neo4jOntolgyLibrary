/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontology.util;

import java.util.LinkedHashSet;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


/**
 *
 * @author franciscoj.navarrete
 */
public class ThreatsCrimeReport extends Report {
    
    @Override
    public void defineArticlePriorProbability (Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation){
        switch (article) {
                case "Report" :
                    log.info("Report_ThreatsCrimeReport");
                    //log.info("Surplus: " + rootArticle);
                    //Poner todas las relaciones del grafo a surplus
                    /*
                    *********** define asign surplus and not defined
                    assignSurplusRelationInGraph(n);
                    */
                    resetTypeReportRelationAndProperties(n);
                    assignNotConsideredAndSurplusRelationInGraph(n, law);
                    log.info("Report: " + rootArticle);
                    break;            
                case "ThreatsCrimeReport" :
                    /*
                    ************************
                    Report
                    and (hasOffenceCharacteristic some ThreatsCharacteristic)
                    and (threatMade some Threats)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Report", rootArticle, relation);
                    log.info("ThreatsCrimeReport");
//                    LinkedHashSet<Relationship> hO  = checkSomeRelationTypeAndObject(n, 
//                                                            "ns0__hasOffenceCharacteristic", 
//                                                            "ns0__ThreatsCharacteristic",
//                                                             relation, 1);
//                    setProbabilityElementAndFactor (hO, 1, 1.0/hO.size());
//                    relation.addAll(hO);
                    
                    LinkedHashSet<Relationship> cM  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__Threats",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM, 2, 1.0/cM.size());
                    relation.addAll(cM);
                    break;
                case "Article169_1_1" :
                    /*
                    ************************
                    Article169_2
                    and (threatMade some 
                       ((threatenTo some (grantingConditions some ImposedConditionOrAmount))
                        and (threatenedBy some (demandingConditions some ImposedConditionOrAmount))))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article169_2", rootArticle, relation);
                    log.info("Article169_1_1");                    
                    LinkedHashSet<Relationship> cM_169_1_1  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithCriminalOffence",
                                                             relation);
                                     
                    for (Relationship r : cM_169_1_1) {
                        LinkedHashSet<Relationship> tb_169_1_1  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__Accused",
                                                                 relation);
                        for (Relationship r2 : tb_169_1_1) {
                            LinkedHashSet<Relationship> dC  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__demandingConditions", 
                                                                    "ns0__ImposedConditionOrAmount",
                                                                     relation, 5);
                            setProbabilityElementAndFactor (dC, 5, getFactor(r2) * 1.0/dC.size());
                            relation.addAll(dC);
                        }
                        
                        LinkedHashSet<Relationship> tt_169_1_1  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__Victim",
                                                                 relation);
                        
                        for (Relationship r3 : tt_169_1_1) {
                            LinkedHashSet<Relationship> gC  = checkSomeRelationTypeAndObject(r3.getEndNode(), 
                                                                    "ns0__grantingConditions", 
                                                                    "ns0__ImposedConditionOrAmount",
                                                                     relation, 6);
                            setProbabilityElementAndFactor (gC, 6, getFactor(r3) * 1.0/gC.size());
                            relation.addAll(gC);
                        }
                        
                    }
                    break;
                case "Article169_1_2" :
                    /*
                    ************************
                    Article169_1_1
                    and (threatMade some ThreatsMadeByMeansOfCommunicationOrSupposedEntities)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article169_1_1", rootArticle, relation);
                    log.info("Article169_1_2");
                    LinkedHashSet<Relationship> hO_169_1_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsMadeByMeansOfCommunicationOrSupposedEntities",
                                                             relation, 2);
                    setProbabilityElementAndFactor (hO_169_1_2, 2, 1.0/hO_169_1_2.size());
                    relation.addAll(hO_169_1_2);
                    break;
                case "Article169_2" :
                    /*
                    ************************
                    ThreatsCrimeReport
                    and (threatMade some ThreatsWithCriminalOffence)
                    and (threatMade some 
                       ((threatenTo some Victim)
                        and (threatenedBy some Accused)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "ThreatsCrimeReport", rootArticle, relation);
                    log.info("Article169_2");                    
                    LinkedHashSet<Relationship> cM_169_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithCriminalOffence",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_169_2, 2, 1.0/cM_169_2.size());
                    relation.addAll(cM_169_2);
                                     
                    for (Relationship r : cM_169_2) {
                        LinkedHashSet<Relationship> tb  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__Accused",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (tb, 3, getFactor(r) * 1.0/tb.size());
                        relation.addAll(tb);
                        
                        LinkedHashSet<Relationship> tt  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__Victim",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (tt, 4, getFactor(r) * 1.0/tt.size());
                        relation.addAll(tt);
                    }
                    break;
                case "Article170" :
                    /*
                    ************************
                    ThreatsCrimeReport
                    and (threatMade some ThreatsWithCriminalOffence)
                    and (threatMade some 
                       ((threatenTo some GroupOfPeople)
                        and (threatenedBy some Accused)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "ThreatsCrimeReport", rootArticle, relation);
                    log.info("Article170");                    
                    LinkedHashSet<Relationship> cM_170  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithCriminalOffence",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_170, 2, 1.0/cM_170.size());
                    relation.addAll(cM_170);
                                     
                    for (Relationship r : cM_170) {
                        LinkedHashSet<Relationship> tb  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__Accused",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (tb, 3, getFactor(r) * 1.0/tb.size());
                        relation.addAll(tb);
                        
                        LinkedHashSet<Relationship> tt  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__GroupOfPeople",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (tt, 4, getFactor(r) * 1.0/tt.size());
                        relation.addAll(tt);
                    }
                    break;
                case "Article170_1" :
                    /*
                    ************************
                    Article170
                    and (hasOffenceAggravatingFactor some DamageCaused)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article170", rootArticle, relation);
                    log.info("Article170_1");
                    LinkedHashSet<Relationship> hOAF_170_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasOffenceAggravatingFactor", 
                                                            "ns0__DamageCaused",
                                                             relation,7);
                    setProbabilityElementAndFactor (hOAF_170_1, 7, 1.0/hOAF_170_1.size());
                    relation.addAll(hOAF_170_1);
                    break;
                case "Article170_2" :
                    /*
                    ************************
                    Article170_1
                    and (threatMade some (threatenedBy some CollaborationWithTerroristGroup))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article170_1", rootArticle, relation);
                    log.info("Article170_2");                    
                    LinkedHashSet<Relationship> cM_170_2  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithCriminalOffence",
                                                             relation);
                                     
                    for (Relationship r : cM_170_2) {
                        LinkedHashSet<Relationship> tb  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__CollaborationWithTerroristGroup",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (tb, 3, getFactor(r) * 1.0/tb.size());
                        relation.addAll(tb);
                    }
                    break;
                case "Article171" :
                    /*
                    ************************
                    ThreatsCrimeReport
                    and (threatMade some ThreatsWithoutCriminalOffence)
                    and (threatMade some 
                       ((threatenTo some Victim)
                        and (threatenedBy some Accused)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "ThreatsCrimeReport", rootArticle, relation);
                    log.info("Article171");                    
                    LinkedHashSet<Relationship> cM_171  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithoutCriminalOffence",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_171, 2, 1.0/cM_171.size());
                    relation.addAll(cM_171);
                                     
                    for (Relationship r : cM_171) {
                        LinkedHashSet<Relationship> tb  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__Accused",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (tb, 3, getFactor(r) * 1.0/tb.size());
                        relation.addAll(tb);
                        
                        LinkedHashSet<Relationship> tt  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__Victim",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (tt, 4, getFactor(r) * 1.0/tt.size());
                        relation.addAll(tt);
                    }
                    break;
                case "Article171_1_1" :
                    /*
                    ************************
                    Article171
                    and (threatMade some (threatenedBy some (demandingConditions some ImposedConditionOrAmount)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171", rootArticle, relation);
                    log.info("Article171_1_1");                    
                    LinkedHashSet<Relationship> cM_171_1_1  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithoutCriminalOffence",
                                                             relation);
                                     
                    for (Relationship r : cM_171_1_1) {
                        LinkedHashSet<Relationship> tb_171_1_1  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__Accused",
                                                                 relation);
                        for (Relationship r2 : tb_171_1_1) {
                            LinkedHashSet<Relationship> dC  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__demandingConditions", 
                                                                    "ns0__ImposedConditionOrAmount",
                                                                     relation, 5);
                            setProbabilityElementAndFactor (dC, 5, getFactor(r2) * 1.0/dC.size());
                            relation.addAll(dC);
                        }
                    }
                    break;
                case "Article171_1_2" :
                    /*
                    ************************
                    Article171_1_1
                    and (threatMade some (threatenTo some (grantingConditions some ImposedConditionOrAmount)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_1_1", rootArticle, relation);
                    log.info("Article171_1_2");                    
                    LinkedHashSet<Relationship> cM_171_1_2  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithoutCriminalOffence",
                                                             relation);
                                     
                    for (Relationship r : cM_171_1_2) {
                        LinkedHashSet<Relationship> tt_171_1_2  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__Victim",
                                                                 relation);
                        for (Relationship r2 : tt_171_1_2) {
                            LinkedHashSet<Relationship> gC  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__grantingConditions", 
                                                                    "ns0__ImposedConditionOrAmount",
                                                                     relation, 6);
                            setProbabilityElementAndFactor (gC, 6, getFactor(r2) * 1.0/gC.size());
                            relation.addAll(gC);
                        }
                    }
                    break;
                case "Article171_2_1" :
                    /*
                    ************************
                    Article171
                    and (threatMade some ThreatstoPrivateLifeOrFamilyRelations)
                    and (threatMade some (threatenedBy some (demandingConditions some Amount)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171", rootArticle, relation);
                    log.info("Article171_2_1");                    
                    LinkedHashSet<Relationship> cM_171_2_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatstoPrivateLifeOrFamilyRelations",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_171_2_1, 2, 1.0/cM_171_2_1.size());
                    relation.addAll(cM_171_2_1);
                                     
                    for (Relationship r : cM_171_2_1) {
                        LinkedHashSet<Relationship> tb_171_2_1  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenedBy", 
                                                                "ns0__Accused",
                                                                 relation);
                        for (Relationship r2 : tb_171_2_1) {
                            LinkedHashSet<Relationship> dC  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__demandingConditions", 
                                                                    "ns0__Amount",
                                                                     relation, 5);
                            setProbabilityElementAndFactor (dC, 5, getFactor(r2) * 1.0/dC.size());
                            relation.addAll(dC);
                        }
                    }
                    break;
                case "Article171_2_2" :
                    /*
                    ************************
                    Article171_1_1
                    and (threatMade some (threatenTo some (grantingConditions some ImposedConditionOrAmount)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_2_1", rootArticle, relation);
                    log.info("Article171_2_2");                    
                    LinkedHashSet<Relationship> cM_171_2_2  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsToIntimacy",
                                                             relation);
                                     
                    for (Relationship r : cM_171_2_2) {
                        LinkedHashSet<Relationship> tt_171_2_2  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__Victim",
                                                                 relation);
                        for (Relationship r2 : tt_171_2_2) {
                            LinkedHashSet<Relationship> gC  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__grantingConditions", 
                                                                    "ns0__ImposedConditionOrAmount",
                                                                     relation, 6);
                            setProbabilityElementAndFactor (gC, 6, getFactor(r2) * 1.0/gC.size());
                            relation.addAll(gC);
                        }
                    }
                    break;
                case "Article171_3_1_a" :
                    /*
                    ************************
                    Article171_2_1
                    and (threatMade some ThreatToDiscloseCrime)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_2_1", rootArticle, relation);
                    log.info("Article171_3_1_a");                    
                    LinkedHashSet<Relationship> cM_171_3_1_a  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatToDiscloseCrime",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_171_3_1_a, 2, 1.0/cM_171_3_1_a.size());
                    relation.addAll(cM_171_3_1_a);
                    break;
                case "Article171_3_1_b" :
                    /*
                    ************************
                    Article171_2_2
                    and (threatMade some ThreatToDiscloseCrime)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_2_2", rootArticle, relation);
                    log.info("Article171_3_1_b");                    
                    LinkedHashSet<Relationship> cM_171_3_1_b  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatToDiscloseCrime",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_171_3_1_b, 2, 1.0/cM_171_3_1_b.size());
                    relation.addAll(cM_171_3_1_b);
                    break;
                case "Article171_3_2_a" :
                    /*
                    ************************
                    Article171_3_1_a
                    and (threatMade some ThreadOnCrimeWithPrisionAbove2Years)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_3_1_a", rootArticle, relation);
                    log.info("Article171_3_2_a");                    
                    LinkedHashSet<Relationship> cM_171_3_2_a  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreadOnCrimeWithPrisionAbove2Years",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_171_3_2_a, 2, 1.0/cM_171_3_2_a.size());
                    relation.addAll(cM_171_3_2_a);
                    break;
                case "Article171_3_2_b" :
                    /*
                    ************************
                    Article171_3_1_b
                    and (threatMade some ThreadOnCrimeWithPrisionAbove2Years)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_3_1_b", rootArticle, relation);
                    log.info("Article171_3_2_b");                    
                    LinkedHashSet<Relationship> cM_171_3_2_b  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreadOnCrimeWithPrisionAbove2Years",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cM_171_3_2_b, 2, 1.0/cM_171_3_2_b.size());
                    relation.addAll(cM_171_3_2_b);
                    break;
                case "Article171_4" :
                    /*
                    ************************
                    Article171
                    and (hasOffenceCharacteristic some MildIntimidation)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171", rootArticle, relation);
                    log.info("Article171_4");
//                    LinkedHashSet<Relationship> hO_171_4  = checkSomeRelationTypeAndObject(n, 
//                                                            "ns0__hasOffenceCharacteristic", 
//                                                            "ns0__MildIntimidation",
//                                                             relation, 10);
//                    setProbabilityElementAndFactor (hO_171_4, 10, 1.0/hO_171_4.size());
//                    relation.addAll(hO_171_4);                
                    break;
                case "Article171_4_1" :
                    /*
                    ************************
                    Article171_4
                    and (threatMade some (threatenTo some (affectiveRelationship some Accused)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_4", rootArticle, relation);
                    log.info("Article171_4_1");
                    LinkedHashSet<Relationship> tM_171_4_1  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithoutCriminalOffence",
                                                             relation);
                    
                    for (Relationship r : tM_171_4_1) {                        
                        LinkedHashSet<Relationship> tt_171_4_1  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__Victim",
                                                                 relation);
                        for (Relationship r2 : tt_171_4_1) {                        
                            LinkedHashSet<Relationship> aR  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__affectiveRelationship", 
                                                                    "ns0__Accused",
                                                                     relation, 8);
                            setProbabilityElementAndFactor (aR, 8, getFactor(r2) * 1.0/aR.size());
                            relation.addAll(aR);
                        }
                    }
                    break;
                case "Article171_4_2" :
                    /*
                    ************************
                    Article171_4
                    and (threatMade some (threatenTo some VulnerablePerson))
                    and (threatMade some (threatenTo some (livingTogether some Accused)))
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article171_4", rootArticle, relation);
                    log.info("Article171_4_2");
                    LinkedHashSet<Relationship> tM_171_4_2  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__threatMade", 
                                                            "ns0__ThreatsWithoutCriminalOffence",
                                                             relation);
                    
                    for (Relationship r : tM_171_4_2) {                        
                        LinkedHashSet<Relationship> tt_171_4_2  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__threatenTo", 
                                                                "ns0__VulnerablePerson",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (tt_171_4_2, 4, getFactor(r) * 1.0/tt_171_4_2.size());
                        relation.addAll(tt_171_4_2);
                        for (Relationship r2 : tt_171_4_2) {                        
                            LinkedHashSet<Relationship> aR  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__livingTogether", 
                                                                    "ns0__Accused",
                                                                     relation, 9);
                            setProbabilityElementAndFactor (aR, 9, getFactor(r2) * 1.0/aR.size());
                            relation.addAll(aR);
                        }
                    }
                    break;
        }
    }// End defineArticlePriorProbability
    
}
