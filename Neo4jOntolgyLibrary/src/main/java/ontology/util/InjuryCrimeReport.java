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
public class InjuryCrimeReport extends Report {
    
    @Override
    public void defineArticlePriorProbability (Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation){
        switch (article) {
                case "Report" :
                    log.info("Report_InjuryCrimeReport");
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
                case "InjuryCrimeReport" :
                    /*
                    ************************
                    Report
                    and (causingInjury some 
                       ((causedBy some Accused)
                        and (causedTo some Victim)))
                    and (hasInjuryCharacteristic some InjuriesCharacteristic)
                    ************************
                    */
                    defineArticlePriorProbability(n, "Report", rootArticle, relation);
                    log.info("InjuryCrimeReport");
                    LinkedHashSet<Relationship> hO  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasInjuryCharacteristic", 
                                                            "ns0__InjuriesCharacteristic",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO, 1, 1.0/hO.size());
                    relation.addAll(hO);
                    
                    LinkedHashSet<Relationship> cI  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingInjury", 
                                                            "ns0__InjurieOrBlow",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cI, 2, 1.0/cI.size());
                    relation.addAll(cI);
                    
                    for (Relationship r : cI) {
                        LinkedHashSet<Relationship> cb  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__causedBy", 
                                                                "ns0__Accused",
                                                                 relation, 3);
                        setProbabilityElementAndFactor (cb, 3, getFactor(r) * 1.0/cb.size());
                        relation.addAll(cb);
                        
                        LinkedHashSet<Relationship> ct  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                                "ns0__causedTo", 
                                                                "ns0__Victim",
                                                                 relation, 4);
                        setProbabilityElementAndFactor (ct, 4, getFactor(r) * 1.0/ct.size());
                        relation.addAll(ct);
                    }
                    break;
                case "Article147_1" :
                    /*
                    ************************
                    InjuryCrimeReport
                    and (causingInjury some Injury)
                    and (hasInjuryCharacteristic some MedicalCharacteristic)
                    ************************
                    */
                    defineArticlePriorProbability(n, "InjuryCrimeReport", rootArticle, relation);
                    log.info("Article147_1");
                    LinkedHashSet<Relationship> hO_147_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasInjuryCharacteristic", 
                                                            "ns0__MedicalCare",
                                                             relation, 1);
                    setProbabilityElementAndFactor (hO_147_1, 1, 1.0/hO_147_1.size());
                    relation.addAll(hO_147_1);
                    
                    LinkedHashSet<Relationship> cI_147_1  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingInjury", 
                                                            "ns0__Injury",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cI_147_1, 2, 1.0/cI_147_1.size());
                    relation.addAll(cI_147_1);
                    break;
                case "Article147_2" :
                    /*
                    ************************
                    InjuryCrimeReport
                    and (causingInjury some OtherInjury)
                    and (reportedBy some Victim)
                    ************************
                    */
                    defineArticlePriorProbability(n, "InjuryCrimeReport", rootArticle, relation);
                    log.info("Article147_2");
                    LinkedHashSet<Relationship> rb_147_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__reportedBy", 
                                                            "ns0__Victim",
                                                             relation, 5);
                    setProbabilityElementAndFactor (rb_147_2, 5, 1.0/rb_147_2.size());
                    relation.addAll(rb_147_2);
                    
                    LinkedHashSet<Relationship> cI_147_2  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingInjury", 
                                                            "ns0__OtherInjury",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cI_147_2, 2, 1.0/cI_147_2.size());
                    relation.addAll(cI_147_2);
                    break;
                case "Article147_3" :
                    /*
                    ************************
                    InjuryCrimeReport
                    and (causingInjury some BlowOrMistreatment)
                    and (reportedBy some Victim)
                    ************************
                    */
                    defineArticlePriorProbability(n, "InjuryCrimeReport", rootArticle, relation);
                    log.info("Article147_3");
                    LinkedHashSet<Relationship> rb_147_3  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__reportedBy", 
                                                            "ns0__Victim",
                                                             relation, 5);
                    setProbabilityElementAndFactor (rb_147_3, 5, 1.0/rb_147_3.size());
                    relation.addAll(rb_147_3);
                    
                    LinkedHashSet<Relationship> cI_147_3  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__causingInjury", 
                                                            "ns0__InjurieOrBlow",
                                                             relation, 2);
                    setProbabilityElementAndFactor (cI_147_3, 2, 1.0/cI_147_3.size());
                    relation.addAll(cI_147_3);
                    break;
                case "Article148" :
                    /*
                    ************************
                    Article147_1
                    and (hasInjuryCharacteristic some SeriousInjuryOrRisk))
                    ************************
                    */
                    defineArticlePriorProbability(n, "InjuryCrimeReport", rootArticle, relation);
                    log.info("Article148");
                    LinkedHashSet<Relationship> hO_148  = checkSomeRelationTypeAndObject(n, 
                                                            "ns0__hasInjuryCharacteristic", 
                                                            "SeriousInjuryOrRisk",
                                                             relation, 6);
                    setProbabilityElementAndFactor (hO_148, 6, 1.0/hO_148.size());
                    relation.addAll(hO_148);
                    
                    
        }
    }// End defineArticlePriorProbability
    
}
