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
public class DamageCrimeReport extends Report {
    
    @Override
    public void defineArticlePriorProbability (Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation){
        switch (article) {
                case "Report" :
                    log.info("Report_DamageCrimeReport");
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
                case "DamageCrimeReport" :
                    /*
                    ************************
                    Report
                    and (causeDamage some 
                       ((damagedBy some Accused)
                        and (hasHarmed some Victim)))
                    and (hasOffenceCharacteristic some DamageCharacteristic)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Report", rootArticle, relation);
                    log.info("DamageCrimeReport");
                    LinkedHashSet<Relationship> hO  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__DamageCharacteristic",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO, 1, 1.0/hO.size());
                    relation.addAll(hO);
                    
                    LinkedHashSet<Relationship> cD  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causeDamage", 
                                                            "ns0__DamageThing",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cD, 2, 1.0/cD.size());
                    relation.addAll(cD);
                    
                    for (Relationship r : cD) {
                        LinkedHashSet<Relationship> db  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__damagedBy", 
                                                                "ns0__Accused",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (db, 3, getFactor(r) * 1.0/db.size());
                        relation.addAll(db);
                        
                        LinkedHashSet<Relationship> cCt  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__hasHarmed", 
                                                                "ns0__Victim",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (cCt, 4, getFactor(r) * 1.0/cCt.size());
                        relation.addAll(cCt);
                    }
                    break;
                case "Article263_1_a" :
                    /*
                    ************************
                    DamageCrimeReport
                    and (causeDamage some (ValueCost some xsd:decimal[>= 400.0]))
                    ************************
                    */
                    defineArticlePriorProbability(n, "DamageCrimeReport", rootArticle, relation);
                    log.info("Article263_1_a"); 
                    LinkedHashSet<Relationship> cD_263_1_a  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__causeDamage", 
                                                            "ns0__DamageThing",
                                                             relation);
                    
                    LinkedHashSet<Relationship> valueCostOver400   = findSomeRelationTypeAndObjectValueCostOver400(n, 
                                                                    "ns0__causeDamage", 
                                                                    "ns0__DamageThing",
                                                                    cD_263_1_a);
                    if(!valueCostOver400.isEmpty() && valueCostOver400.size()==1) {
                        setProbabilityElementAndFactor (valueCostOver400, 2, 1.0/valueCostOver400.size());
                        relation.addAll(valueCostOver400);
                    }
                    log.info("\t\tvalueCostOver400 InjurieOrBlow: " + valueCostOver400.size());
                    break;
                case "Article263_1_b" :
                    /*
                    ************************
                    DamageCrimeReport
                    and (causeDamage some (ValueCost some xsd:decimal[< 400.0]))
                    ************************
                    */
                    defineArticlePriorProbability(n, "DamageCrimeReport", rootArticle, relation);
                    log.info("Article263_1_b"); 
                    LinkedHashSet<Relationship> cD_263_1_b  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__causeDamage", 
                                                            "ns0__DamageThing",
                                                             relation);
                    
                    LinkedHashSet<Relationship> valueCostMinus400   = findSomeRelationTypeAndObjectValueCostMinus400(n, 
                                                                    "ns0__causeDamage", 
                                                                    "ns0__DamageThing",
                                                                    cD_263_1_b);
                    if(!valueCostMinus400.isEmpty() && valueCostMinus400.size()==1) {
                        setProbabilityElementAndFactor (valueCostMinus400, 2, 1.0/valueCostMinus400.size());
                        relation.addAll(valueCostMinus400);
                    }
                    log.info("\t\tvalueCostMinus400 InjurieOrBlow: " + valueCostMinus400.size());
                    break;               
                case "Article263_2_a_1" :
                    /*
                    ************************
                    Article263_1_a
                    and (hasOffenceCharacteristic some DefendersToApplicationOfLaws)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_1_a", rootArticle, relation);
                    log.info("Article263_2_a_1"); 
                    LinkedHashSet<Relationship> hO_263_2_a_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__DefendersToApplicationOfLaws",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_263_2_a_1, 1, 1.0/hO_263_2_a_1.size());
                    relation.addAll(hO_263_2_a_1);
                    break;
                case "Article263_2_a_2" :
                    /*
                    ************************
                    Article263_1_a
                    and (hasOffenceCharacteristic some AggravatedDamage)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_1_a", rootArticle, relation);
                    log.info("Article263_2_a_2"); 
                    LinkedHashSet<Relationship> hO_263_2_a_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__AggravatedDamage",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_263_2_a_2, 1, 1.0/hO_263_2_a_2.size());
                    relation.addAll(hO_263_2_a_2);
                    break;
                case "Article263_2_b_1" :
                    /*
                    ************************
                    Article263_1_b
                    and (hasOffenceCharacteristic some DefendersToApplicationOfLaws)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_1_b", rootArticle, relation);
                    log.info("Article263_2_b_1"); 
                    LinkedHashSet<Relationship> hO_263_2_b_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__DefendersToApplicationOfLaws",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_263_2_b_1, 1, 1.0/hO_263_2_b_1.size());
                    relation.addAll(hO_263_2_b_1);
                    break;
                case "Article263_2_b_2" :
                    /*
                    ************************
                    Article263_1_b
                    and (hasOffenceCharacteristic some AggravatedDamage)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_1_b", rootArticle, relation);
                    log.info("Article263_2_b_2"); 
                    LinkedHashSet<Relationship> hO_263_2_b_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__AggravatedDamage",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_263_2_b_2, 1, 1.0/hO_263_2_b_2.size());
                    relation.addAll(hO_263_2_b_2);
                    break;
                case "Article266_1_a" :
                    /*
                    ************************
                    Article263_1_a
                    and (hasOffenceCharacteristic some FireOrExplosion)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_1_a", rootArticle, relation);
                    log.info("Article266_1_a"); 
                    LinkedHashSet<Relationship> hO_266_1_a  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__FireOrExplosion",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_266_1_a, 1, 1.0/hO_266_1_a.size());
                    relation.addAll(hO_266_1_a);
                    break;
                case "Article266_1_b" :
                    /*
                    ************************
                    Article263_1_b
                    and (hasOffenceCharacteristic some FireOrExplosion)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_1_b", rootArticle, relation);
                    log.info("Article266_1_a"); 
                    LinkedHashSet<Relationship> hO_266_1_b  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__FireOrExplosion",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_266_1_b, 1, 1.0/hO_266_1_b.size());
                    relation.addAll(hO_266_1_b);
                    break;
                case "Article266_2" :
                    /*
                    ************************
                    Article263_2_b_2
                    and (hasOffenceCharacteristic some FireOrExplosion)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article263_2_b_2", rootArticle, relation);
                    log.info("Article266_2"); 
                    LinkedHashSet<Relationship> hO_266_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__FireOrExplosion",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_266_2, 1, 1.0/hO_266_2.size());
                    relation.addAll(hO_266_2);
                    break;
                case "Article266_4_a" :
                    /*
                    ************************
                    Article266_1_b
                    and (hasOffenceCharacteristic some DangerOfLife)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article266_1_b", rootArticle, relation);
                    log.info("Article266_4_a"); 
                    LinkedHashSet<Relationship> hO_266_4_a  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__DangerOfLife",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_266_4_a, 1, 1.0/hO_266_4_a.size());
                    relation.addAll(hO_266_4_a);
                    break;
                case "Article266_4_b" :
                    /*
                    ************************
                    Article266_2
                    and (hasOffenceCharacteristic some DangerOfLife)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Article266_2", rootArticle, relation);
                    log.info("Article266_4_b"); 
                    LinkedHashSet<Relationship> hO_266_4_b  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__DangerOfLife",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_266_4_b, 1, 1.0/hO_266_4_b.size());
                    relation.addAll(hO_266_4_b);
                    break;
                case "Article267" :
                    /*
                    ************************
                    DamageCrimeReport
                    and (causeDamage some (ValueCost some xsd:decimal[>= 80000.0]))
                    and (hasOffenceCharacteristic some MajorNegligence)
                    ************************
                    */
                    defineArticlePriorProbability(n, "DamageCrimeReport", rootArticle, relation);
                    log.info("Article267"); 
                    LinkedHashSet<Relationship> hO_267  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasDamageCharacteristic", 
                                                            "ns0__MajorNegligence",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_267, 1, 1.0/hO_267.size());
                    relation.addAll(hO_267);
                    
                    LinkedHashSet<Relationship> cD_267  = findSomeRelationTypeAndObject(n, 
                                                            "ns0__causeDamage", 
                                                            "ns0__DamageThing",
                                                             relation);
                    
                    LinkedHashSet<Relationship> valueCostOver80000   = findSomeRelationTypeAndObjectValueCostOver(n, 
                                                                    "ns0__causeDamage", 
                                                                    "ns0__DamageThing",
                                                                    cD_267,
                                                                    80000.0);
                    if(!valueCostOver80000.isEmpty() && valueCostOver80000.size()==1) {
                        setProbabilityElementAndFactor (valueCostOver80000, 2, 1.0/valueCostOver80000.size());
                        relation.addAll(valueCostOver80000);
                    }
                    log.info("\t\tvalueCostOver80000 DamageThing: " + valueCostOver80000.size());
                    break;
        }
    }// End defineArticlePriorProbability
}
