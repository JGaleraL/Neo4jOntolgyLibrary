/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontology.util;

import java.util.LinkedHashSet;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class UsurpationCrimeReport extends Report {

    @Override
    public void defineArticlePriorProbability(Node n,
            String article,
            String rootArticle,
            LinkedHashSet<Relationship> relation) {
        switch (article) {
            case "Report":
                log.info("Report_UsurpationCrimeReport");
                resetTypeReportRelationAndProperties(n);
                assignNotConsideredAndSurplusRelationInGraph(n, law);
                log.info("Report: " + rootArticle);
                break;

            case "UsurpationReport":
                /*
                 * Report
                 * and (
                 * (occupy some BuildingOrProperty)
                 * or (usurpationRealPropertyRight some RealPropertyRight)
                 * )
                 */
                defineArticlePriorProbability(n, "Report", rootArticle, relation);
                log.info("UsurpationReport");

                // occupy → BuildingOrProperty
                LinkedHashSet<Relationship> occupyBldg = checkSomeRelationTypeAndObject(n,
                        "ns0__occupy",
                        "ns0__BuildingOrProperty",
                        relation, 1);
                setProbabilityElementAndFactor(occupyBldg, 1, 1.0 / occupyBldg.size());
                relation.addAll(occupyBldg);
                for (Relationship r : occupyBldg) {
                    this.setQualifiedRelationship(n, r, "BuildingOccupation");
                }

                // usurpationRealPropertyRight → RealPropertyRight
                LinkedHashSet<Relationship> usurpRight = checkSomeRelationTypeAndObject(n,
                        "ns0__usurpationRealPropertyRight",
                        "ns0__RealPropertyRight",
                        relation, 2);
                setProbabilityElementAndFactor(usurpRight, 2, 1.0 / usurpRight.size());
                relation.addAll(usurpRight);
                for (Relationship r : usurpRight) {
                    this.setQualifiedRelationship(n, r, "RealPropertyRightUsurpation");
                }
                break;

            case "Article245_1":
                /*
                 * UsurpationReport + hasOffenceCharacteristic some
                 * ActWithIntimidationOrViolence
                 */
                defineArticlePriorProbability(n, "UsurpationReport", rootArticle, relation);
                log.info("Article245_1");
                LinkedHashSet<Relationship> usurpViolence = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__ActWithIntimidationOrViolence",
                        relation, 3);
                setProbabilityElementAndFactor(usurpViolence, 3, 1.0 / usurpViolence.size());
                relation.addAll(usurpViolence);
                for (Relationship r : usurpViolence) {
                    this.setQualifiedRelationship(n, r, "UsurpationViolence");
                }
                break;

            case "Article245_2":
                /*
                 * UsurpationReport
                 * and (hasOffenceCharacteristic some WithoutIntimidationOrViolence)
                 * and (occupy some NotADwelling)
                 */
                defineArticlePriorProbability(n, "UsurpationReport", rootArticle, relation);
                log.info("Article245_2");

                // hasOffenceCharacteristic -> WithoutIntimidationOrViolence
                LinkedHashSet<Relationship> noViolence = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__WithoutIntimidationOrViolence",
                        relation, 3);
                setProbabilityElementAndFactor(noViolence, 3, 1.0 / noViolence.size());
                relation.addAll(noViolence);
                for (Relationship r : noViolence) {
                    this.setQualifiedRelationship(n, r, "PeacefulOccupation");
                }

                // occupy -> NotADwelling
                LinkedHashSet<Relationship> occupyNotDwelling = checkSomeRelationTypeAndObject(n,
                        "ns0__occupy",
                        "ns0__NotADwelling",
                        relation, 1);
                setProbabilityElementAndFactor(occupyNotDwelling, 1, 1.0 / occupyNotDwelling.size());
                relation.addAll(occupyNotDwelling);
                for (Relationship r : occupyNotDwelling) {
                    this.setQualifiedRelationship(n, r, "NotADwelling");
                }
                break;
        }
    }
}