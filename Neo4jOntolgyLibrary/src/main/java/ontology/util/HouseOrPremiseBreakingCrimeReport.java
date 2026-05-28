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
public class HouseOrPremiseBreakingCrimeReport extends Report {
 
    @Override
    public void defineArticlePriorProbability (Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation){
        switch (article) {
            case "Report" :
                log.info("Report_HouseOrPremiseBreakingCrimeReport");
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
            case "HouseOrPremiseBreakingCrimeReport" :
                /*
                ************************
                Report
                and (hasOffenceCharacteristic some HouseOrPremiseBreaking)
                ************************
                */
                defineArticlePriorProbability(n, "Report", rootArticle, relation);
                log.info("HouseOrPremiseBreakingCrimeReport");
                LinkedHashSet<Relationship> hO  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasOffenceCharacteristic", 
                                                        "ns0__HouseOrPremiseBreaking",
                                                         relation, 1);
                setProbabilityElementAndFactor (hO, 1, 1.0/hO.size());
                relation.addAll(hO);                
                break;
            case "Article202_1" :
                /*
                ************************
                HouseOrPremiseBreakingCrimeReport
                and (isBreakInHome some (isBreakInBy some Accused))
                ************************
                */ 
                defineArticlePriorProbability(n, "HouseOrPremiseBreakingCrimeReport", rootArticle, relation);
                log.info("Article202_1");
                LinkedHashSet<Relationship> iBi_202_1  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInHome", 
                                                        "ns0__House",
                                                         relation, 2);
                setProbabilityElementAndFactor (iBi_202_1, 2, 1.0/iBi_202_1.size());
                relation.addAll(iBi_202_1);   
                
                for (Relationship r : iBi_202_1) {
                    LinkedHashSet<Relationship> iBiB  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation, 3);
                    setProbabilityElementAndFactor (iBiB, 3, getFactor(r) * 1.0/iBiB.size());
                    relation.addAll(iBiB);
                }
                break;
            case "Article202_2" :
                /*
                ************************
                Article202_1
                and (hasOffenceCharacteristic some ActWithIntimidationOrViolence)
                ************************
                */ 
                defineArticlePriorProbability(n, "Article202_1", rootArticle, relation);
                log.info("Article202_2");
                LinkedHashSet<Relationship> hO_202_2  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasOffenceCharacteristic", 
                                                        "ns0__ActWithIntimidationOrViolence",
                                                         relation, 6);
                setProbabilityElementAndFactor (hO_202_2, 6, 1.0/hO_202_2.size());
                relation.addAll(hO_202_2);
                break;
            case "Article203" :
                /*
                ************************
                HouseOrPremiseBreakingCrimeReport
                and (isBreakInPremise some (isBreakInBy some Accused))
                ************************
                */ 
                defineArticlePriorProbability(n, "HouseOrPremiseBreakingCrimeReport", rootArticle, relation);
                log.info("Article203");
                LinkedHashSet<Relationship> iBi_203  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInPremise", 
                                                        "ns0__Premise",
                                                         relation, 5);
                setProbabilityElementAndFactor (iBi_203, 5, 1.0/iBi_203.size());
                relation.addAll(iBi_203);   
                
                for (Relationship r : iBi_203) {
                    LinkedHashSet<Relationship> iBiB  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation, 3);
                    setProbabilityElementAndFactor (iBiB, 3, getFactor(r) * 1.0/iBiB.size());
                    relation.addAll(iBiB);
                }
                break;
            case "Article203_1" :
                /*
                ************************
                Article_203
                and (hasOffenceCharacteristic some ClosedToThePublic)
                ************************
                */ 
                defineArticlePriorProbability(n, "Article203", rootArticle, relation);
                log.info("Article203_1");
                LinkedHashSet<Relationship> hO_203_1  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasOffenceCharacteristic", 
                                                        "ns0__ClosedToThePublic",
                                                         relation, 7);
                setProbabilityElementAndFactor (hO_203_1, 7, 1.0/hO_203_1.size());
                relation.addAll(hO_203_1);
                break;
            case "Article203_2" :
                /*
                ************************
                Article_203
                and (hasOffenceCharacteristic some OpenToThePublic)
                ************************
                */ 
                defineArticlePriorProbability(n, "Article203", rootArticle, relation);
                log.info("Article203_2");
                LinkedHashSet<Relationship> hO_203_2  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasOffenceCharacteristic", 
                                                        "ns0__OpenToThePublic",
                                                         relation, 7);
                setProbabilityElementAndFactor (hO_203_2, 7, 1.0/hO_203_2.size());
                relation.addAll(hO_203_2);
                break;
            case "Article203_3" :
                /*
                ************************
                Article203_2
                and (hasOffenceCharacteristic some ActWithIntimidationOrViolence)
                ************************
                */ 
                defineArticlePriorProbability(n, "Article203_2", rootArticle, relation);
                log.info("Article203_3");
                LinkedHashSet<Relationship> hO_203_3  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasOffenceCharacteristic", 
                                                        "ns0__ActWithIntimidationOrViolence",
                                                         relation, 6);
                setProbabilityElementAndFactor (hO_203_3, 6, 1.0/hO_203_3.size());
                relation.addAll(hO_203_3);
                break;
            case "Article204_a" :
                /*
                ************************
                Article202_1
                and (isBreakIn some (isBreakInBy some (hasPersonCharacteristic some Official)))
                ************************
                */ 
                defineArticlePriorProbability(n, "Article202_1", rootArticle, relation);
                log.info("Article204_a");
                LinkedHashSet<Relationship> iBi_204_a  = findSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInHome", 
                                                        "ns0__House",
                                                         relation);  
                
                for (Relationship r : iBi_204_a) {
                    LinkedHashSet<Relationship> iBiB_204_a  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation);
                    for (Relationship r2 : iBiB_204_a) {
                        LinkedHashSet<Relationship> hO_204_a  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                "ns0__hasPersonCharacteristic", 
                                                                "ns0__Official",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (hO_204_a, 4, getFactor(r2) * 1.0/hO_204_a.size());
                        relation.addAll(hO_204_a);
                    }
                }
                break;
            case "Article204_b" :
                /*
                ************************
                Article202_2
                and (isBreakIn some (isBreakInBy some (hasPersonCharacteristic some Official)))
                ************************
                */ 
                defineArticlePriorProbability(n, "Article202_2", rootArticle, relation);
                log.info("Article204_b");
                LinkedHashSet<Relationship> iBi_204_b  = findSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInHome", 
                                                        "ns0__House",
                                                         relation);  
                
                for (Relationship r : iBi_204_b) {
                    LinkedHashSet<Relationship> iBiB_204_b  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation);
                    for (Relationship r2 : iBiB_204_b) {
                        LinkedHashSet<Relationship> hO_204_b  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                "ns0__hasPersonCharacteristic", 
                                                                "ns0__Official",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (hO_204_b, 4, getFactor(r2) * 1.0/hO_204_b.size());
                        relation.addAll(hO_204_b);
                    }
                }
                break;
            case "Article204_c" :
                /*
                ************************
                Article203_1
                and (isBreakIn some (isBreakInBy some (hasPersonCharacteristic some Official)))
                ************************
                */ 
                defineArticlePriorProbability(n, "Article203_1", rootArticle, relation);
                log.info("Article204_c");
                LinkedHashSet<Relationship> iBi_204_c  = findSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInHome", 
                                                        "ns0__House",
                                                         relation);  
                
                for (Relationship r : iBi_204_c) {
                    LinkedHashSet<Relationship> iBiB_204_c  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation);
                    for (Relationship r2 : iBiB_204_c) {
                        LinkedHashSet<Relationship> hO_204_c  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                "ns0__hasPersonCharacteristic", 
                                                                "ns0__Official",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (hO_204_c, 4, getFactor(r2) * 1.0/hO_204_c.size());
                        relation.addAll(hO_204_c);
                    }
                }
                break;
            case "Article204_d" :
                /*
                ************************
                Article203_2
                and (isBreakIn some (isBreakInBy some (hasPersonCharacteristic some Official)))
                ************************
                */ 
                defineArticlePriorProbability(n, "Article203_2", rootArticle, relation);
                log.info("Article204_d");
                LinkedHashSet<Relationship> iBi_204_d  = findSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInHome", 
                                                        "ns0__House",
                                                         relation);  
                
                for (Relationship r : iBi_204_d) {
                    LinkedHashSet<Relationship> iBiB_204_d  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation);
                    for (Relationship r2 : iBiB_204_d) {
                        LinkedHashSet<Relationship> hO_204_d  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                "ns0__hasPersonCharacteristic", 
                                                                "ns0__Official",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (hO_204_d, 4, getFactor(r2) * 1.0/hO_204_d.size());
                        relation.addAll(hO_204_d);
                    }
                }
                break;
            case "Article204_e" :
                /*
                ************************
                Article203_3
                and (isBreakIn some (isBreakInBy some (hasPersonCharacteristic some Official)))
                ************************
                */ 
                defineArticlePriorProbability(n, "Article203_3", rootArticle, relation);
                log.info("Article204_e");
                LinkedHashSet<Relationship> iBi_204_e  = findSomeRelationTypeAndObject(n, 
                                                        "ns0__isBreakInHome", 
                                                        "ns0__House",
                                                         relation);  
                
                for (Relationship r : iBi_204_e) {
                    LinkedHashSet<Relationship> iBiB_204_e  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__isBreakInBy", 
                                                            "ns0__Accused",
                                                             relation);
                    for (Relationship r2 : iBiB_204_e) {
                        LinkedHashSet<Relationship> hO_204_e  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                "ns0__hasPersonCharacteristic", 
                                                                "ns0__Official",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (hO_204_e, 4, getFactor(r2) * 1.0/hO_204_e.size());
                        relation.addAll(hO_204_e);
                    }
                }
                break;
         }
    }
}
