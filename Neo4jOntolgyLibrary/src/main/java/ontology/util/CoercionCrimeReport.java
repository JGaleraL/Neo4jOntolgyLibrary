/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontology.util;

import java.util.LinkedHashSet;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;

/**
 *
 * @author franciscoj.navarrete
 */
public class CoercionCrimeReport extends Report {
    
    @Override
    public void defineArticlePriorProbability (Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation){
        switch (article) {
                case "Report" :
                    log.info("Report_CoercionCrimeReport");
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
                case "CoercionCrimeReport" :
                    /*
                    ************************
                    Report
                    and (causingCoercion some 
                       ((causedCoercionBy some Accused)
                        and (causedCoercionTo some Victim)))
                    and (hasOffenceCharacteristic some ActWithIntimidationOrViolence)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Report", rootArticle, relation);
                    log.info("CoercionCrimeReport");
                    LinkedHashSet<Relationship> hO  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasOffenceCharacteristic", 
                                                            "ns0__ActWithIntimidationOrViolence",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO, 1, 1.0/hO.size());
                    relation.addAll(hO);  
                    LinkedHashSet<Relationship> cC  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__Coercion",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cC, 2, 1.0/cC.size());
                    relation.addAll(cC);
                    
                    for (Relationship r : cC) {
                        LinkedHashSet<Relationship> cCb  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__causedCoercionBy", 
                                                                "ns0__Accused",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (cCb, 3, getFactor(r) * 1.0/cCb.size());
                        relation.addAll(cCb);
                        
                        LinkedHashSet<Relationship> cCt  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__causedCoercionTo", 
                                                                "ns0__Victim",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (cCt, 4, getFactor(r) * 1.0/cCt.size());
                        relation.addAll(cCt);
                    }
                    break;
                case "Article172_1_1_a" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (causingCoercion some ToDoWhatItDoesNotWant)
                    ************************
                    */ 
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_1_1_a");
                    LinkedHashSet<Relationship> cC172_1_1_a  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__ToDoWhatItDoesNotWant",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cC172_1_1_a, 2, 1.0/cC172_1_1_a.size());
                    relation.addAll(cC172_1_1_a);   
                    break;
                case "Article172_1_1_b" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (causingCoercion some PreventDoingWhatLawDoesNotProhibit)
                    ************************
                    */ 
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_1_1_b");
                    LinkedHashSet<Relationship> cC172_1_1_b  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__PreventDoingWhatLawDoesNotProhibit",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cC172_1_1_b, 2, 1.0/cC172_1_1_b.size());
                    relation.addAll(cC172_1_1_b);   
                    break;
                case "Article172_1_2" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (causingCoercion some PreventFundamentalRight)
                    ************************
                    */ 
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_1_2");
                    LinkedHashSet<Relationship> cC172_1_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__PreventFundamentalRight",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cC172_1_2, 2, 1.0/cC172_1_2.size());
                    relation.addAll(cC172_1_2);   
                    break;
                case "Article172_1_3" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (causingCoercion some PreventTheLawfulUseOfHome)
                    ************************
                    */ 
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_1_3");
                    LinkedHashSet<Relationship> cC172_1_3  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__PreventTheLawfulUseOfHome",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cC172_1_3, 2, 1.0/cC172_1_3.size());
                    relation.addAll(cC172_1_3);   
                    break;
                case "Article172_2_1" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (causingCoercion some (causedCoercionTo some (affectiveRelationship some Accused)))
                    and (hasOffenceCharacteristic some MildIntimidation)
                    ************************
                    */
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_2_1");
                    LinkedHashSet<Relationship> hO_172_2_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasOffenceCharacteristic", 
                                                            "ns0__MildIntimidation",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_172_2_1, 1, 1.0/hO_172_2_1.size());
                    relation.addAll(hO_172_2_1);  
                    LinkedHashSet<Relationship> cC_172_2_1  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__Coercion",
                                                             relation);
                    
                    for (Relationship r : cC_172_2_1) {                        
                        LinkedHashSet<Relationship> cCt_172_2_1  = findSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__causedCoercionTo", 
                                                                "ns0__Victim",
                                                                 relation);
                        for (Relationship r2 : cCt_172_2_1) {                        
                            LinkedHashSet<Relationship> aR  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__affectiveRelationship", 
                                                                    "ns0__Accused",
                                                                     relation, 5);
                            setProbabilityElementAndFactor (aR, 5, getFactor(r2) * 1.0/aR.size());
                            relation.addAll(aR);
                        }
                    }
                    break;
                case "Article172_2_2" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (causingCoercion some (causedCoercionTo some VulnerablePerson))
                    and (causingCoercion some (causedCoercionTo some (livingTogether some Accused)))
                    and (hasOffenceCharacteristic some MildIntimidation)
                    ************************
                    */
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_2_2");
                    LinkedHashSet<Relationship> hO_172_2_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasOffenceCharacteristic", 
                                                            "ns0__MildIntimidation",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_172_2_2, 1, 1.0/hO_172_2_2.size());
                    relation.addAll(hO_172_2_2);  
                    LinkedHashSet<Relationship> cC_172_2_2  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__causingCoercion", 
                                                            "ns0__Coercion",
                                                             relation);
                    
                    for (Relationship r : cC_172_2_2) {                        
                        LinkedHashSet<Relationship> cCt_172_2_2  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__causedCoercionTo", 
                                                                "ns0__VulnerablePerson",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (cCt_172_2_2, 4, getFactor(r) * 1.0/cCt_172_2_2.size());
                        relation.addAll(cCt_172_2_2);
                        for (Relationship r2 : cCt_172_2_2) {                        
                            LinkedHashSet<Relationship> aR  = checkSomeRelationTypeAndObject(r2.getEndNode(), 
                                                                    "ns0__livingTogether", 
                                                                    "ns0__Accused",
                                                                     relation, 6);
                            setProbabilityElementAndFactor (aR, 6, getFactor(r2) * 1.0/aR.size());
                            relation.addAll(aR);
                        }
                    }
                    break;
                case "Article172_3_1" :
                    /*
                    ************************
                    CoercionCrimeReport
                    and (hasOffenceCharacteristic some MildIntimidation)
                    and (reportedCoercionBy some Victim)
                    ************************
                    */ 
                    defineArticlePriorProbability(n, "CoercionCrimeReport", rootArticle, relation);
                    log.info("Article172_3_1");
                    LinkedHashSet<Relationship> hO_172_3_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasOffenceCharacteristic", 
                                                            "ns0__MildIntimidation",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_172_3_1, 1, 1.0/hO_172_3_1.size());
                    relation.addAll(hO_172_3_1);
                    
                    LinkedHashSet<Relationship> cC172_3_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__reportedCoercionBy", 
                                                            "ns0__Victim",
                                                             relation, 7);
                    setProbabilityElementAndFactor (cC172_3_1, 7, 1.0/cC172_3_1.size());
                    relation.addAll(cC172_3_1);   
                    break;
        }
    } //End defineArticlePriorProbability
    
}
