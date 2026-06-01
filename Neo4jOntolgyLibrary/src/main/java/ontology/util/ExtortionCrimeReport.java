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
public class ExtortionCrimeReport extends Report {

    @Override
    public void defineArticlePriorProbability(Node n,
            String article,
            String rootArticle,
            LinkedHashSet<Relationship> relation) {
        switch (article) {
            case "Report":
                log.info("Report_ExtortionCrimeReport");
                resetTypeReportRelationAndProperties(n);
                assignNotConsideredAndSurplusRelationInGraph(n, law);
                log.info("Report: " + rootArticle);
                break;

            case "ExtortionReport":
                /*
                 ************************
                 * Report
                 * and (causingExtortion some ToDoLegalActOrOmissionItDoesNotWant)
                 * and (causingExtortion some
                 * ((causedExtortionBy some Accused)
                 * and (causedExtortionTo some Victim)))
                 * and (hasDamageCharacteristic some EconomicDamage)
                 * and (hasOffenceCharacteristic some ActWithIntimidationOrViolence)
                 * and (hasOffenceCharacteristic some ProfitIntent)
                 ************************
                 */
                defineArticlePriorProbability(n, "Report", rootArticle, relation);
                log.info("ExtortionReport");

                // hasOffenceCharacteristic → ActWithIntimidationOrViolence
                LinkedHashSet<Relationship> actViolence = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__ActWithIntimidationOrViolence",
                        relation, 1);
                setProbabilityElementAndFactor(actViolence, 1, 1.0 / actViolence.size());
                relation.addAll(actViolence);

                // hasOffenceCharacteristic → ProfitIntent
                LinkedHashSet<Relationship> profitIntent = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__ProfitIntent",
                        relation, 2);
                setProbabilityElementAndFactor(profitIntent, 2, 1.0 / profitIntent.size());
                relation.addAll(profitIntent);

                // hasDamageCharacteristic → EconomicDamage
                LinkedHashSet<Relationship> ecoDamage = checkSomeRelationTypeAndObject(n,
                        "ns0__hasDamageCharacteristic",
                        "ns0__EconomicDamage",
                        relation, 3);
                setProbabilityElementAndFactor(ecoDamage, 3, 1.0 / ecoDamage.size());
                relation.addAll(ecoDamage);

                // causingExtortion → ToDoLegalActOrOmissionItDoesNotWant
                LinkedHashSet<Relationship> actOmission = checkSomeRelationTypeAndObject(n,
                        "ns0__causingExtortion",
                        "ns0__ToDoLegalActOrOmissionItDoesNotWant",
                        relation, 4);
                setProbabilityElementAndFactor(actOmission, 4, 1.0 / actOmission.size());
                relation.addAll(actOmission);

                // Para cada Extortion encontrada, anidamos los actores
                for (Relationship r : actOmission) {
                    // causedExtortionBy → Accused
                    LinkedHashSet<Relationship> extBy = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__causedExtortionBy",
                            "ns0__Accused",
                            relation, 5);
                    setProbabilityElementAndFactor(extBy, 5, getFactor(r) * 1.0 / extBy.size());
                    relation.addAll(extBy);

                    // causedExtortionTo → Victim
                    LinkedHashSet<Relationship> extTo = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__causedExtortionTo",
                            "ns0__Victim",
                            relation, 6);
                    setProbabilityElementAndFactor(extTo, 6, getFactor(r) * 1.0 / extTo.size());
                    relation.addAll(extTo);
                }
                break;

            case "Article243":
                defineArticlePriorProbability(n, "ExtortionReport", rootArticle, relation);
                log.info("Article243");
                // Article243 es la forma básica del CP, sin restricciones propias adicionales
                break;
        }
    }
}