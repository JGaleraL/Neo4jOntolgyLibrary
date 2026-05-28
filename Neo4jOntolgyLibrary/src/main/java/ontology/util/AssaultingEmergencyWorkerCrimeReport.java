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
public class AssaultingEmergencyWorkerCrimeReport extends Report {
    
    @Override
    public void defineArticlePriorProbability (Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation){
        
        
        switch (article) {
            case "Report" :
                log.info("Report_AssaultingEmergencyWorkerCrimeReport");
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
            case "AssaultingEmergencyWorkerCrimeReport" :
                /*
                ************************
                Report
                and (carringOut some AssaultOrResistence)
                ************************
                */
                defineArticlePriorProbability(n, "Report", rootArticle, relation);
                log.info("AssaultingEmergencyWorkerCrimeReport");
                LinkedHashSet<Relationship> cO  = checkSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__AssaultOrResistence",
                                                                     relation, 1);
                setProbabilityElementAndFactor (cO, 1, 1.0/cO.size());
                relation.addAll(cO);                
                break;
            case "AssaultingEmergencyWorkerCrime_a" :
                /*
                ************************
                AssaultingEmergencyWorkerCrimeReport
                and (carringOut some 
                   ((assaultedBy some Accused)
                    and (assaults some GovernmentOfficialOrAssistance)))
                ************************
                */
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrimeReport", rootArticle, relation);
                log.info("AssaultingEmergencyWorkerCrime_a");
                LinkedHashSet<Relationship> cO_a  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__AssaultOrResistence",
                                                                     relation);


                for (Relationship r : cO_a) {
                    LinkedHashSet<Relationship> aB  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaultedBy", 
                                                            "ns0__Accused",
                                                             relation, 2);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (aB, 2, getFactor(r) * 1.0/aB.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(aB);                
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__GovernmentOfficialOrAssistance",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                    
                }
                break;
            case "AssaultingEmergencyWorkerCrime_b" :
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrimeReport", rootArticle, relation);
                log.info("AssaultingEmergencyWorkerCrime_b");
                LinkedHashSet<Relationship> cO_b  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__AssaultOrResistence",
                                                                     relation);


                for (Relationship r : cO_b) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__Victim",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                    
                    LinkedHashSet<Relationship> tg  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__triggered", 
                                                            "ns0__Person",
                                                             relation, 4);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (tg, 4, getFactor(r) * 1.0/tg.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(tg);
                }
                break;
            case "AssaultingEmergencyWorkerCrime_c" :
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrimeReport", rootArticle, relation);
                log.info("AssaultingEmergencyWorkerCrime_c");
                LinkedHashSet<Relationship> cO_c  = checkSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Insult",
                                                                    relation, 1);
                setProbabilityElementAndFactor (cO_c, 1, 1.0/cO_c.size());
                relation.addAll(cO_c);                                                     

                                                                     
                for (Relationship r : cO_c) {
                    LinkedHashSet<Relationship> in  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__insulting", 
                                                            "ns0__Victim",
                                                             relation, 6);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (in, 6, getFactor(r) * 1.0/in.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(in);
                    
                    LinkedHashSet<Relationship> nR  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__notRespeting", 
                                                            "ns0__Accussed",
                                                             relation, 5);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (nR, 5, getFactor(r) * 1.0/nR.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(nR);
                }
                break;
            case "Article550_1_a" :
                /*
                ************************
                AssaultingEmergencyWorkerCrime_a
                and (hasOffenceCharacteristic some ActWithIntimidationOrViolence)
                ************************
                */

                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrime_a", rootArticle, relation);
//                log.info("Article550");
//                LinkedHashSet<Relationship> cO_550  = checkSomeRelationTypeAndObject(n, 
//                                                                    "ns0__carringOut", 
//                                                                    "ns0__Assault",
//                                                                     relation, 1);
//                setProbabilityElementAndFactor (cO_550, 1, 1.0/cO_550.size());
//                relation.addAll(cO_550);

                
                LinkedHashSet<Relationship> hO  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasAssaultingCharacteristic", 
                                                        "ns0__ActWithIntimidationOrViolence",
                                                         relation, 7);
                //log.info("\t\tstolenBy relations: " + belongsTo.size());
                setProbabilityElementAndFactor (hO, 7, 1.0/hO.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                relation.addAll(hO);    
                break;
//            case "Article550_1_a" :
//                defineArticlePriorProbability(n, "Article550", rootArticle, relation);
//                log.info("Article550_1_a");
//                LinkedHashSet<Relationship> cO_550_1_a  = findSomeRelationTypeAndObject(n, 
//                                                                    "ns0__carringOut", 
//                                                                    "ns0__Assault",
//                                                                     relation);
//                
//                for (Relationship r : cO_550_1_a) {
//                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
//                                                            "ns0__assaults", 
//                                                            "ns0__Authority",
//                                                             relation, 3);
//                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
//                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
////                    this.setQualifiedRelationship(n, r, "Stolengoods");
//                    relation.addAll(as);
//                }
//                break;
            case "Article550_1_b" :
                /*
                ************************
                Article550_1_a
                and (carringOut some (assaults some HealthOrEducationPersonnelCrime))
                ************************
                */
                defineArticlePriorProbability(n, "Article550_1_a", rootArticle, relation);
                log.info("Article550_1_b");
                LinkedHashSet<Relationship> cO_550_1_b  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Assault",
                                                                     relation);
                
                for (Relationship r : cO_550_1_b) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__HealthOrEducationPersonnelCrime",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article550_3" :
                /*
                ************************
                Article550_1_a
                and (carringOut some (assaults some GovernmentMemberCrime))
                ************************
                */
                defineArticlePriorProbability(n, "Article550_1_a", rootArticle, relation);
                log.info("Article550_3");
                LinkedHashSet<Relationship> cO_550_3  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Assault",
                                                                     relation);
                
                for (Relationship r : cO_550_3) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__GovernmentMemberCrime",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article551_a" :
                /*
                ************************
                Article550_1_a
                and (hasOffenceCharacteristic some AggravatedAssaulting)
                ************************
                */
                defineArticlePriorProbability(n, "Article550_1_a", rootArticle, relation);
                log.info("Article551_a");
                LinkedHashSet<Relationship> hO_551_a  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasAssaultingCharacteristic", 
                                                        "ns0__AggravatedAssaulting",
                                                         relation, 8);
                //log.info("\t\tstolenBy relations: " + belongsTo.size());
                setProbabilityElementAndFactor (hO_551_a, 8, 1.0/hO_551_a.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                relation.addAll(hO_551_a);
                break;
            case "Article551_b" :
                defineArticlePriorProbability(n, "Article550_1_b", rootArticle, relation);
                log.info("Article551_b");
                LinkedHashSet<Relationship> hO_551_b  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasAssaultingCharacteristic", 
                                                        "ns0__AggravatedAssaulting",
                                                         relation ,8);
                //log.info("\t\tstolenBy relations: " + belongsTo.size());
                setProbabilityElementAndFactor (hO_551_b, 8, 1.0/hO_551_b.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                relation.addAll(hO_551_b);
                break;
            case "Article551_c" :
                defineArticlePriorProbability(n, "Article550_3", rootArticle, relation);
                log.info("Article551_a");
                LinkedHashSet<Relationship> hO_551_c  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasAssaultingCharacteristic", 
                                                        "ns0__AggravatedAssaulting",
                                                         relation, 8);
                //log.info("\t\tstolenBy relations: " + belongsTo.size());
                setProbabilityElementAndFactor (hO_551_c, 8, 1.0/hO_551_c.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                relation.addAll(hO_551_c);
                break;
            case "Article553" :
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrime_b", rootArticle, relation);
                log.info("Article553");
                LinkedHashSet<Relationship> cO_553  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__AssaultOrResistence",
                                                                     relation);
                for (Relationship r : cO_553) {
                    LinkedHashSet<Relationship> tr  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__triggered", 
                                                            "ns0__Instigator",
                                                             relation, 4);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (tr, 4, getFactor(r) * 1.0/tr.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(tr);
                }
                break;
            case "Article554_a" :
                defineArticlePriorProbability(n, "Article550_1_a", rootArticle, relation);
                log.info("Article554_a");
                LinkedHashSet<Relationship> cO_554_a  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Assault",
                                                                     relation);
                
                for (Relationship r : cO_554_a) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__EmergencyWorkerCrime",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article554_b" :
                defineArticlePriorProbability(n, "Article550_1_b", rootArticle, relation);
                log.info("Article554_b");
                LinkedHashSet<Relationship> cO_554_b  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Assault",
                                                                     relation);
                
                for (Relationship r : cO_554_b) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__EmergencyWorkerCrime",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article554_c" :
                defineArticlePriorProbability(n, "Article550_3", rootArticle, relation);
                log.info("Article554_c");
                LinkedHashSet<Relationship> cO_554_c  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Assault",
                                                                     relation);
                
                for (Relationship r : cO_554_c) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__EmergencyWorkerCrime",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article554_d" :
                defineArticlePriorProbability(n, "Article551_a", rootArticle, relation);
                log.info("Article554_d");
                LinkedHashSet<Relationship> cO_554_d  = findSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__Assault",
                                                                     relation);
                
                for (Relationship r : cO_554_d) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__EmergencyWorkerCrime",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article556_1_1" :
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrime_c", rootArticle, relation);
                log.info("Article556_1_1");
                LinkedHashSet<Relationship> cO_556_1_1  = checkSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__SeriousResistance",
                                                                     relation, 1);
                setProbabilityElementAndFactor (cO_556_1_1, 1, 1.0/cO_556_1_1.size());
                relation.addAll(cO_556_1_1);
           
                LinkedHashSet<Relationship> hO_556_1_1  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasAssaultingCharacteristic", 
                                                        "ns0__ResistanceAndDisobedience",
                                                         relation, 9);
                //log.info("\t\tstolenBy relations: " + belongsTo.size());
                setProbabilityElementAndFactor (hO_556_1_1, 9, 1.0/hO_556_1_1.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                relation.addAll(hO_556_1_1); 
                
                for (Relationship r : cO_556_1_1) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__Authority",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break;
            case "Article556_1_2" :
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrime_c", rootArticle, relation);
                log.info("Article556_1_2");
                LinkedHashSet<Relationship> cO_556_1_2  = checkSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__SeriousResistance",
                                                                     relation, 1);
                setProbabilityElementAndFactor (cO_556_1_2, 1, 1.0/cO_556_1_2.size());
                relation.addAll(cO_556_1_2);
           
                LinkedHashSet<Relationship> hO_556_1_2  = checkSomeRelationTypeAndObject(n, 
                                                        "ns0__hasAssaultingCharacteristic", 
                                                        "ns0__ResistanceAndDisobedience",
                                                         relation, 9);
                //log.info("\t\tstolenBy relations: " + belongsTo.size());
                setProbabilityElementAndFactor (hO_556_1_2, 9, 1.0/hO_556_1_2.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                relation.addAll(hO_556_1_2); 
                
                for (Relationship r : cO_556_1_2) {
                    LinkedHashSet<Relationship> as  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__assaults", 
                                                            "ns0__PrivateSecurityPersonnel",
                                                             relation, 3);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (as, 3, getFactor(r) * 1.0/as.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(as);
                }
                break; 
            case "Article556_2" :
                defineArticlePriorProbability(n, "AssaultingEmergencyWorkerCrime_c", rootArticle, relation);
                log.info("Article556_2");
                LinkedHashSet<Relationship> cO_556_2  = checkSomeRelationTypeAndObject(n, 
                                                                    "ns0__carringOut", 
                                                                    "ns0__ Insult",
                                                                     relation, 1);
                setProbabilityElementAndFactor (cO_556_2, 1, 1.0/cO_556_2.size());
                relation.addAll(cO_556_2);
           
                for (Relationship r : cO_556_2) {
                    LinkedHashSet<Relationship> in  = checkSomeRelationTypeAndObject(r.getEndNode(), 
                                                            "ns0__insulting", 
                                                            "ns0__Authority",
                                                             relation, 6);
                    //log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor (in, 6, getFactor(r) * 1.0/in.size());
//                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(in);
                }
                break;
        }  
    }    
}
