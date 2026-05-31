/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontology.util;

import java.util.LinkedHashSet;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author franciscoj.navarrete
 */
public class PropertyCrimeReport extends Report {
    /*
     * Especific article clause enum for property crime reports
     */
    public enum OrClauseArticle241_4 {
        ns0__AggravatingFactorAffectingInjuredParty,
        ns0__AggravatyFactorAffectsAccusedParty,
        ns0__AgriculturalOrLivestockProduct,
        ns0__ArtisticThing,
        ns0__CulturalThing,
        ns0__EssentialThing,
        ns0__HistoricalThing,
        ns0__ProvisionOfServiceThing,
        ns0__ScientificThing
    }

    public enum OrClauseArticle240_2 {
        AgriculturalOrLivestockProduct,
        ArtisticThing,
        CulturalThing,
        EssentialThing,
        HistoricalThing,
        ProvisionOfServiceThing,
        ScientificThing
    }

    public enum OrClauseArticle235_1__1 {
        ThingCharacteristic
    }

    public enum OrClauseArticle235_1__2 {
        AggravatingFactorAffectingInjuredParty,
    }

    public enum OrClauseArticle235_2 {
        AgriculturalOrLivestockProduct,
        ArtisticThing,
        CulturalThing,
        EssentialThing,
        HistoricalThing,
        ProvisionOfServiceThing,
        ScientificThing
    }

    /*
     * Especific crime report methods
     */

    private void article235Based(Node n,
            String article,
            LinkedHashSet<Relationship> relation,
            boolean creation) {
        log.info("\tarticle235Based stolenthing relations: ");
        LinkedHashSet<Relationship> stolenthing7 = findSomeRelationTypeAndObject(n,
                "ns0__stolenthing",
                "ns0__StolenGoods",
                relation);
        log.info("\tarticle235Based stolenthing relations: " + stolenthing7.size());
        LinkedHashSet<Relationship> hasThingCharacteristic = new LinkedHashSet<>();
        LinkedHashSet<Relationship> hasOffenceAggravatingFactor = new LinkedHashSet<>();
        LinkedHashSet<Relationship> hasPreviusSentence1 = new LinkedHashSet<>();
        LinkedHashSet<Relationship> hasAccomplice1 = new LinkedHashSet<>();
        LinkedHashSet<Relationship> belongsToCriminalOrganization1 = new LinkedHashSet<>();
        for (var r : stolenthing7) {
            log.info("\t\tArticle235Based stolenthing" + r.getStartNode().getProperty("name").toString() + " --- "
                    + r.getType().name()
                    + " ---> " + r.getEndNode().getProperty("name").toString());
            LinkedHashSet<Relationship> htc = findSomeRelationTypeAndObject(r.getEndNode(),
                    "ns0__hasThingCharacteristic",
                    "ns0__ThingCharacteristic",
                    relation);
            hasThingCharacteristic.addAll(htc);
            // checkOrRelationTypeAndListObjectWithoutCreate(r.getEndNode(),
            // "ns0__hasThingCharacteristic",
            // Stream.of(OrClauseArticle235_1__1.values())
            // .map(OrClauseArticle235_1__1::name)
            // .collect(Collectors.toList()),
            // relation);

            LinkedHashSet<Relationship> stolenBy7 = findSomeRelationTypeAndObject(r.getEndNode(),
                    "ns0__stolenBy",
                    "ns0__Accused",
                    relation);
            log.info("\tarticle235Based stolenBy7 relations: " + stolenBy7.size());
            LinkedHashSet<Relationship> belongsTo7 = findSomeRelationTypeAndObject(r.getEndNode(),
                    "ns0__belongsTo",
                    "ns0__Victim",
                    relation);
            for (var r2 : stolenBy7) {
                // List<String> list = Arrays.asList("ns0__PunishentForCrimesAgainstProperty");
                log.info("\t\t\tArticle235Based stolenBy7" + r2.getStartNode().getProperty("name").toString() + " --- "
                        + r2.getType().name()
                        + " ---> " + r2.getEndNode().getProperty("name").toString());
                LinkedHashSet<Relationship> hos = findSomeRelationTypeAndObject(r2.getEndNode(),
                        "ns0__hasPreviusSentence",
                        "ns0__PropertyCrimePunishments",
                        relation);
                log.info("\t\t\tarticle235Based hos relations: " + hos.size());
                hasPreviusSentence1.addAll(hos);
                // checkOrRelationTypeAndListObjectWithoutCreate
                // list = Arrays.asList("ns0__Person");
                LinkedHashSet<Relationship> ha = findSomeRelationTypeAndObject(r2.getEndNode(),
                        "ns0__hasAccomplice",
                        "ns0__Person",
                        relation);
                hasAccomplice1.addAll(ha);
                // list = Arrays.asList("ns0__CriminalOrganization");
                LinkedHashSet<Relationship> bco = findSomeRelationTypeAndObject(r2.getEndNode(),
                        "ns0__belongsToCriminalOrganization",
                        "ns0__CriminalOrganization",
                        relation);
                belongsToCriminalOrganization1.addAll(bco);
            }

            for (var r3 : belongsTo7) {
                // List<String> list = Arrays.asList("ns0__PunishentForCrimesAgainstProperty");
                LinkedHashSet<Relationship> eaf = findSomeRelationTypeAndObject(r3.getEndNode(),
                        "ns0__hasEffectOffence",
                        "ns0__OffenceEffects",
                        relation);
                hasOffenceAggravatingFactor.addAll(eaf);
            }
        }

        // LinkedHashSet<Relationship> hasOffenceAggravatingFactor =
        // checkOrRelationTypeAndListObjectWithoutCreate(n,
        // "ns0__hasOffenceAggravatingFactor",
        // Stream.of(OrClauseArticle235_1__2.values())
        // .map(OrClauseArticle235_1__2::name)
        // .collect(Collectors.toList()),
        // relation);
        log.info("\thasOffenceAggravatingFactor relations: " + hasOffenceAggravatingFactor.size());
        log.info("\thasPreviusSentence1 relations: " + hasPreviusSentence1.size());
        log.info("\thasAccomplice1 relations: " + hasAccomplice1.size());
        log.info("\thasThingCharacteristic relations: " + hasThingCharacteristic.size());
        log.info("\tbelongsToCriminalOrganization1 relations: " + belongsToCriminalOrganization1.size());

        // Check conditions
        for (var os : hasPreviusSentence1) {
            log.info("\t\tArticle235Based " + os.getStartNode().getProperty("name").toString() + " --- "
                    + os.getType().name()
                    + " ---> " + os.getEndNode().getProperty("name").toString());
            Object num = null;
            if (os.getAllProperties().keySet().contains("typeReportRelation"))
                num = os.getEndNode().getProperty("ns0__num");
            if (num == null || Integer.parseInt(num.toString()) < 3)
                hasPreviusSentence1.remove(os);
        }
        for (var ha : hasAccomplice1) {
            log.info("\t\tArticle235Based " + ha.getStartNode().getProperty("name").toString() + " --- "
                    + ha.getType().name()
                    + " ---> " + ha.getEndNode().getProperty("name").toString());
            Object age = null;
            if (ha.getAllProperties().keySet().contains("typeReportRelation"))
                age = ha.getEndNode().getProperty("ns0__Age");
            if (age == null || Integer.valueOf(age.toString()) > 15)
                hasAccomplice1.remove(ha);
        }
        // Add probabilities factor
        int size = !hasThingCharacteristic.isEmpty() ? hasThingCharacteristic.size() : 0;
        size = size + (!hasOffenceAggravatingFactor.isEmpty() ? hasOffenceAggravatingFactor.size() : 0);
        size = size + (!hasPreviusSentence1.isEmpty() ? hasPreviusSentence1.size() : 0);
        size = size + (!hasAccomplice1.isEmpty() ? hasAccomplice1.size() : 0);
        size = size + (!belongsToCriminalOrganization1.isEmpty() ? belongsToCriminalOrganization1.size() : 0);
        if (!hasThingCharacteristic.isEmpty()) {
            log.info("\thasThingCharacteristic relations not empty: " + hasThingCharacteristic.size());
            setProbabilityElementFactorAndTypeRel(hasThingCharacteristic, 7, 1.0 / size, "necessary");
            relation.addAll(hasThingCharacteristic);
        }
        if (!hasOffenceAggravatingFactor.isEmpty()) {
            setProbabilityElementFactorAndTypeRel(hasOffenceAggravatingFactor, 7, 1.0 / size, "necessary");
            relation.addAll(hasOffenceAggravatingFactor);
        }
        if (!hasPreviusSentence1.isEmpty()) {
            setProbabilityElementFactorAndTypeRel(hasPreviusSentence1, 7, 1.0 / size, "necessary");
            relation.addAll(hasPreviusSentence1);
        }
        if (!hasAccomplice1.isEmpty()) {
            setProbabilityElementFactorAndTypeRel(hasAccomplice1, 7, 1.0 / size, "necessary");
            relation.addAll(hasAccomplice1);
        }
        if (!belongsToCriminalOrganization1.isEmpty()) {
            setProbabilityElementFactorAndTypeRel(belongsToCriminalOrganization1, 7, 1.0 / size, "necessary");
            relation.addAll(belongsToCriminalOrganization1);
        }
        if (size == 0 && creation) {
            log.info("\tCreate node with relationType: " + "ns0__hasThingCharacteristic");
            Node o = tx.createNode(Label.label("ThingCharacteristic"));
            o.setProperty("name", "no_name");
            Relationship r = n.createRelationshipTo(o, RelationshipType.withName("ns0__hasThingCharacteristic"));
            setTypeReportRelation(r, "created");
            relation.add(r);
        }
        if (size == 1 && article.endsWith("_2")) {
            // log.info("\tCreate node with relationType: " +
            // "ns0__hasThingCharacteristic");
            Node o = tx.createNode(Label.label("ThingCharacteristic"));
            o.setProperty("name", "no_name2");
            Relationship r = n.createRelationshipTo(o, RelationshipType.withName("ns0__hasThingCharacteristic"));
            setTypeReportRelation(r, "created");
            relation.add(r);
        }

    }

    /*
     * define prior probability
     */

    @Override
    public void defineArticlePriorProbability(Node n,
            String article,
            String rootArticle,
            LinkedHashSet<Relationship> relation) {
        // double baseRate = 1.0/numRelationOfArticle(rootArticle);
        log.info("defineArticlePriorProbability " + rootArticle + " in " + article);
        switch (article) {
            case "Report":
                log.info("assignNotConsideredAndSurplusRelationInGraph: " + rootArticle);
                // Poner todas las relaciones del grafo a surplus
                /*
                 *********** define asign surplus and not defined
                 * assignSurplusRelationInGraph(n);
                 */
                resetTypeReportRelationAndProperties(n);
                assignNotConsideredAndSurplusRelationInGraph(n, law);
                log.info("Report_PropertyCrimeReport: " + rootArticle);
                break;
            case "PropertyCrimeReport":
                defineArticlePriorProbability(n, "Report", rootArticle, relation);
                log.info("PropertyCrimeReport");
                LinkedHashSet<Relationship> stolenthing = checkSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation, 1);
                setProbabilityElementAndFactor(stolenthing, 1, 1.0 / stolenthing.size());
                relation.addAll(stolenthing);
                // log.info("\tstolenthing relations: " + stolenthing.size());
                for (Relationship r : stolenthing) {
                    // setNotConsideredRelation(r.getEndNode(), "ns0__usedBy");
                    LinkedHashSet<Relationship> belongsTo = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__belongsTo",
                            "ns0__Person",
                            relation, 2);
                    // log.info("\t\tstolenBy relations: " + belongsTo.size());
                    setProbabilityElementAndFactor(belongsTo, 2, getFactor(r) * 1.0 / belongsTo.size());
                    this.setQualifiedRelationship(n, r, "Stolengoods");
                    relation.addAll(belongsTo);

                }

                break;
            case "TheftReport":
                defineArticlePriorProbability(n, "PropertyCrimeReport", rootArticle, relation);
                log.info("TheftReport");

                LinkedHashSet<Relationship> notHasOffenceCharacteristic = checkNotSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__RobberyCharacteristic",
                        relation, 3);

                relation.addAll(notHasOffenceCharacteristic);

                // log.info("\thasOffenceCharacteristic relations: " +
                // notHasOffenceCharacteristic.size());
                LinkedHashSet<Relationship> stolenthing2 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // relation.addAll(stolenthing2);
                // log.info("\tstolenthing relations: " + stolenthing2.size());
                for (Relationship r : stolenthing2) {
                    LinkedHashSet<Relationship> stolenBy = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__stolenBy",
                            "ns0__Person",
                            relation, 5);
                    // for (Relationship r2 : stolenBy) { //Solo para el casode los acusados no
                    // tratados
                    // setNotConsideredIncomingRelation(r2.getEndNode());
                    // r2.getEndNode().getRelationships(Direction.OUTGOING).iterator()
                    // .forEachRemaining(rel -> setTypeReportRelation(rel, "surplus"));
                    // }
                    setProbabilityElementAndFactor(stolenBy, 5, getFactor(r) * 1.0 / stolenBy.size());
                    this.setQualifiedRelationship(n, r, "TheftPerpretator");
                    relation.addAll(stolenBy);
                    // log.info("\t\tstolenBy relations: " + stolenBy.size());
                }
                break;
            case "RobberyReport":
                defineArticlePriorProbability(n, "PropertyCrimeReport", rootArticle, relation);
                log.info("RobberyReport");
                LinkedHashSet<Relationship> hasOffenceCharacteristic = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__RobberyCharacteristic",
                        relation, 3);
                setProbabilityElementAndFactor(hasOffenceCharacteristic, 3, 1.0 / hasOffenceCharacteristic.size());
                relation.addAll(hasOffenceCharacteristic);
                // log.info("\thasOffenceCharacteristic relations: " +
                // hasOffenceCharacteristic.size());
                LinkedHashSet<Relationship> stolenthing1 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // log.info("\tstolenthing relations: " + stolenthing1.size());
                for (Relationship r : stolenthing1) {
                    LinkedHashSet<Relationship> stolenBy = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__stolenBy",
                            "ns0__Person",
                            relation, 4);

                    /**
                     * *
                     * Deleted after the inclusion of the new laws and new allocation
                     * mechanism for surpluses and not considered in August 2024.
                     */
                    // Evitar surplus
                    // setNotConsideredRelation(r.getEndNode(), "ns0__usedByOwner");
                    // setNotConsideredRelation(r.getEndNode(), "ns0__stolenByOwner");

                    setProbabilityElementAndFactor(stolenBy, 4, getFactor(r) * 1.0 / stolenBy.size());
                    this.setQualifiedRelationship(n, r, "TheftPerpretator");
                    relation.addAll(stolenBy);
                    // log.info("\t\tstolenBy relations: " + stolenBy.size());

                    // LinkedHashSet<Relationship> usedByOwner =
                    // checkSomeRelationTypeAndObject(r.getEndNode(),
                    // "ns0__usedByOwner",
                    // "ns0__Person",
                    // relation, 8);
                    // setProbabilityElementAndFactor (usedByOwner, 8, getFactor(r) *
                    // 1.0/usedByOwner.size());
                    // relation.addAll(usedByOwner);
                }

                break;
            case "TheftByOwner":
                defineArticlePriorProbability(n, "TheftReport", rootArticle, relation);
                log.info("TheftByOwner");
                LinkedHashSet<Relationship> stolenthing3 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // log.info("\tstolenthing relations: " + stolenthing3.size());
                for (Relationship r : stolenthing3) {
                    LinkedHashSet<Relationship> stolenByOwner = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__stolenByOwner",
                            "ns0__Person",
                            relation, 9);
                    // setProbabilityElementAndFactor (stolenByOwner, 5, getFactor(r) *
                    // 1.0/stolenByOwner.size());
                    setProbabilityElementAndFactor(stolenByOwner, 9, getFactor(r) * 1.0 / stolenByOwner.size());
                    relation.addAll(stolenByOwner);
                    // log.info("\t\tusedByOwner relations: " + stolenByOwner .size());
                }
                break;
            case "TheftByNotOwner":
                defineArticlePriorProbability(n, "TheftReport", rootArticle, relation);
                log.info("TheftByNotOwner");
                LinkedHashSet<Relationship> stolenthing4 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // log.info("\tstolenthing relations: " + stolenthing4.size());
                for (Relationship r : stolenthing4) {
                    LinkedHashSet<Relationship> usedByOwner = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__usedByOwner",
                            "ns0__Person",
                            relation, 8);
                    setProbabilityElementAndFactor(usedByOwner, 8, getFactor(r) * 1.0 / usedByOwner.size());
                    relation.addAll(usedByOwner);
                    // log.info("\t\tusedByOwner relations: " + usedByOwner.size());
                }
                break;
            case "Article240_1":
                defineArticlePriorProbability(n, "RobberyReport", rootArticle, relation);
                log.info("Article240_1");
                LinkedHashSet<Relationship> someHasOffenceCharacteristic_1 = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__BurglaryCrime",
                        relation, 3);

                setProbabilityElementAndFactor(someHasOffenceCharacteristic_1, 3,
                        1.0 / someHasOffenceCharacteristic_1.size());
                for (Relationship r : someHasOffenceCharacteristic_1) {
                    this.setQualifiedRelationship(n, r, "BurglaryCrime");
                }
                relation.addAll(someHasOffenceCharacteristic_1);
                // log.info("\tsome hasOffenceCharacteristic relations: " +
                // someHasOffenceCharacteristic_1.size());
                break;
            case "Article241":
                defineArticlePriorProbability(n, "RobberyReport", rootArticle, relation);
                log.info("Article241");
                LinkedHashSet<Relationship> someHasOffenceCharacteristic_2 = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__HouseOrPremiseBreaking",
                        relation, 3);
                for (Relationship r : someHasOffenceCharacteristic_2) {
                    this.setQualifiedRelationship(n, r, "HouseOrPremiseBreaking");
                }
                setProbabilityElementAndFactor(someHasOffenceCharacteristic_2, 3,
                        1.0 / someHasOffenceCharacteristic_2.size());
                relation.addAll(someHasOffenceCharacteristic_2);
                // log.info("\tsome hasOffenceCharacteristic relations: " +
                // someHasOffenceCharacteristic_2.size());
                break;
            case "Article242": // 7 relaciones y
            case "Article242_1":
                defineArticlePriorProbability(n, "RobberyReport", rootArticle, relation);
                log.info("Article242--Article242_1");
                LinkedHashSet<Relationship> someHasOffenceCharacteristic_3 = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__RobberyWithIntimidationOrViolence",
                        relation, 3);

                setProbabilityElementAndFactor(someHasOffenceCharacteristic_3, 3,
                        1.0 / someHasOffenceCharacteristic_3.size());
                for (Relationship r : someHasOffenceCharacteristic_3) {
                    this.setQualifiedRelationship(n, r, "RobberyWithIntimidationOrViolence");
                }
                relation.addAll(someHasOffenceCharacteristic_3);
                // log.info("\tsomeHasOffenceCharacteristic relations: " +
                // someHasOffenceCharacteristic_3.size());
                break;
            case "Article234_1":
                defineArticlePriorProbability(n, "TheftByNotOwner", rootArticle, relation);
                log.info("Article234_1");
                LinkedHashSet<Relationship> stolenthing6 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                LinkedHashSet<Relationship> valueCostOver400 = findSomeRelationTypeAndObjectValueCostOver400(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        stolenthing6);
                if (!valueCostOver400.isEmpty() && valueCostOver400.size() == 1) {
                    setProbabilityElementAndFactor(valueCostOver400, 1, 1.0 / valueCostOver400.size());
                    relation.addAll(valueCostOver400);
                }
                log.info("\t\tvalueCostOver400 movablethings: " + valueCostOver400.size());
                break;
            case "Article234_2":
                defineArticlePriorProbability(n, "TheftByNotOwner", rootArticle, relation);
                log.info("Article234_2");
                LinkedHashSet<Relationship> stolenthing5 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // log.info("\tstolenthing relations: " + stolenthing5.size());

                LinkedHashSet<Relationship> valueCostMinus400 = findSomeRelationTypeAndObjectValueCostMinus400(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        stolenthing5);
                if (!valueCostMinus400.isEmpty() && valueCostMinus400.size() == 1) {
                    setProbabilityElementAndFactor(valueCostMinus400, 1, 1.0 / valueCostMinus400.size());
                    relation.addAll(valueCostMinus400);
                }
                log.info("\t\tvalueCostMinus400 movablethings: " + valueCostMinus400.size());

                break;
            case "Article234_3":
                defineArticlePriorProbability(n, "TheftByNotOwner", rootArticle, relation);
                log.info("Article234_3");
                LinkedHashSet<Relationship> hasOffenceCharacteristic_4 = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__UnlawfulEntry",
                        relation, 11);
                setProbabilityElementAndFactor(hasOffenceCharacteristic_4, 11, 1.0 / hasOffenceCharacteristic_4.size());
                relation.addAll(hasOffenceCharacteristic_4);
                // log.info("\thasOffenceCharacteristic relations: " +
                // hasOffenceCharacteristic_4.size());
                break;
            case "Article235_1":
                defineArticlePriorProbability(n, "TheftByNotOwner", rootArticle, relation);
                log.info("Article235_1");
                article235Based(n, article, relation, true);
                break;
            case "Article235_2":
                defineArticlePriorProbability(n, "TheftByNotOwner", rootArticle, relation);
                log.info("Article235_2");
                article235Based(n, article, relation, true);
                break;

            case "Article236_1":
                defineArticlePriorProbability(n, "TheftByOwner", rootArticle, relation);
                log.info("Article236_1");
                LinkedHashSet<Relationship> stolenthing8 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // log.info("\tstolenthing relations: " + stolenthing8.size());
                LinkedHashSet<Relationship> valueCostMinus400_2 = findSomeRelationTypeAndObjectValueCostMinus400(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        stolenthing8);
                if (!valueCostMinus400_2.isEmpty() && valueCostMinus400_2.size() == 1) {
                    setProbabilityElementAndFactor(valueCostMinus400_2, 1, 1.0 / valueCostMinus400_2.size());
                    relation.addAll(valueCostMinus400_2);
                }
                // log.info("\t\tvalueCostMinus400 movablethings: " +
                // valueCostMinus400_2.size());
                break;
            case "Article236_2":
                defineArticlePriorProbability(n, "TheftByOwner", rootArticle, relation);
                log.info("Article236_2");
                LinkedHashSet<Relationship> stolenthing9 = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        relation);
                // log.info("\tstolenthing relations: " + stolenthing9.size());
                LinkedHashSet<Relationship> valueCostOver400_2 = findSomeRelationTypeAndObjectValueCostOver400(n,
                        "ns0__stolenthing",
                        "ns0__StolenGoods",
                        stolenthing9);
                if (!valueCostOver400_2.isEmpty() && valueCostOver400_2.size() == 1) {
                    setProbabilityElementAndFactor(valueCostOver400_2, 1, 1.0 / valueCostOver400_2.size());
                    relation.addAll(valueCostOver400_2);
                }
                // log.info("\t\tvalueCostMinus400 movablethings: " +
                // valueCostOver400_2.size());
                break;
            case "Article240_2":
                defineArticlePriorProbability(n, "Article240_1", rootArticle, relation);
                log.info("Article240_2");
                article235Based(n, article, relation, true);
                break;
            case "Article241_1":
                defineArticlePriorProbability(n, "Article241", rootArticle, relation);
                log.info("Article241_1");
                break;
            case "Article241_4":
                defineArticlePriorProbability(n, "Article241", rootArticle, relation);
                log.info("Article241_4");
                LinkedHashSet<Relationship> someExistsOffenceAggravatingFactor = findSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceAggravatingFactor",
                        "ns0__DamageCaused",
                        relation);
                // LinkedHashSet<Relationship> someExistsOffenceAggravatingFactor =
                // checkSomeRelationTypeAndObject(n,
                // "ns0__hasOffenceAggravatingFactor",
                // "ns0__DamageCaused",
                // relation, 7);
                log.info("\tsomeHasOffenceCharacteristic relations: " + someExistsOffenceAggravatingFactor.size());
                if (!someExistsOffenceAggravatingFactor.isEmpty()) {
                    setProbabilityElementAndFactor(someExistsOffenceAggravatingFactor, 6,
                            1.0 / someExistsOffenceAggravatingFactor.size());
                    relation.addAll(someExistsOffenceAggravatingFactor);
                    article235Based(n, article, relation, false);
                    break;
                } else
                    article235Based(n, article, relation, true);

                // LinkedHashSet<Relationship> article_241_4_clause =
                // checkOrRelationTypeAndListObject(n,
                // "ns0__stolenthing",
                // Stream.of(OrClauseArticle241_4.values())
                // .map(OrClauseArticle241_4::name)
                // .collect(Collectors.toList()),
                // relation);
                // //log.info("\tsomeHasOffenceCharacteristic relations: " +
                // article_241_4_clause.size());
                // setProbabilityElementAndFactor (article_241_4_clause, 1,
                // 1.0/article_241_4_clause.size());
                // relation.addAll(article_241_4_clause);

                break;
            case "Article242_2":
                defineArticlePriorProbability(n, "Article242", rootArticle, relation);
                log.info("Article242_2");
                LinkedHashSet<Relationship> someHasOffenceCharacteristic1 = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__HouseOrPremiseBreaking",
                        relation, 3);
                // log.info("\tsomeHasOffenceCharacteristic relations: " +
                // someHasOffenceCharacteristic1.size());
                // No hace falta, ya está establecido en Robbery, sólo necesitamos crear si no
                // existe
                setProbabilityElementAndFactor(someHasOffenceCharacteristic1, 3,
                        1.0 / someHasOffenceCharacteristic1.size());
                relation.addAll(someHasOffenceCharacteristic1);
                break;
            case "Article242_3":
                defineArticlePriorProbability(n, "Article242", rootArticle, relation);
                log.info("Article242_3");
                LinkedHashSet<Relationship> hasAggravatingFactor = checkSomeRelationTypeAndObject(n,
                        "ns0__hasAggravatingFactor",
                        "ns0__ArmedRobbery",
                        relation, 6);
                // log.info("\tsome hasAggravatingFactor relations: " +
                // hasAggravatingFactor.size());
                // No hace falta, ya está establecido en Robbery, sólo necesitamos crear si no
                // existe
                setProbabilityElementAndFactor(hasAggravatingFactor, 9, 1.0 / hasAggravatingFactor.size());
                relation.addAll(hasAggravatingFactor);
                break;
            case "Article242_4":
                defineArticlePriorProbability(n, "Article242", rootArticle, relation);
                log.info("Article242_4");
                LinkedHashSet<Relationship> hasMitigatingFactor = checkSomeRelationTypeAndObject(n,
                        "ns0__hasMitigatingFactor",
                        "ns0__MinimalIntimidationOrViolence",
                        relation, 12);
                // log.info("\tsome hasMitigatingFactor relations: " +
                // hasMitigatingFactor.size());
                setProbabilityElementAndFactor(hasMitigatingFactor, 12, 1.0 / hasMitigatingFactor.size());
                relation.addAll(hasMitigatingFactor);
                break;
            case "TheftUnlawfulUseOfVehicles":
                defineArticlePriorProbability(n, "PropertyCrimeReport", rootArticle, relation);
                log.info("TheftUnlawfulUseOfVehicles");
                // hasOffenceCharacteristic -> WithoutIntimidationOrViolence
                LinkedHashSet<Relationship> noViolence = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__WithoutIntimidationOrViolence",
                        relation, 13);
                setProbabilityElementAndFactor(noViolence, 3, 1.0 / noViolence.size());
                relation.addAll(noViolence);

                // hasOffenceCharacteristic -> WithoutIntendingToAppropiate
                LinkedHashSet<Relationship> noAppropiate = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__WithoutIntendingToAppropiate",
                        relation, 14);
                setProbabilityElementAndFactor(noAppropiate, 3, 1.0 / noAppropiate.size());
                relation.addAll(noAppropiate);

                // stolenthing -> StolenGoods -> hasThingCharacteristic -> MotorVehicle
                LinkedHashSet<Relationship> stolenthingTUOV = findSomeRelationTypeAndObject(n,
                        "ns0__stolenthing", "ns0__StolenGoods", relation);
                for (Relationship r : stolenthingTUOV) {
                    LinkedHashSet<Relationship> motor = checkSomeRelationTypeAndObject(r.getEndNode(),
                            "ns0__hasThingCharacteristic",
                            "ns0__MotorVehicle",
                            relation, 15);
                    setProbabilityElementAndFactor(motor, 6, getFactor(r) * 1.0 / Math.max(motor.size(), 1));
                    relation.addAll(motor);
                }
                break;
            case "Article244_1":
                defineArticlePriorProbability(n, "TheftUnlawfulUseOfVehicles", rootArticle, relation);
                log.info("Article244_1");
                // hasOffenceCharacteristic -> RefundWithin48H (lo que diferencia 244.1 de su
                // agravado)
                LinkedHashSet<Relationship> refund = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__RefundWithin48H",
                        relation, 16);
                setProbabilityElementAndFactor(refund, 3, 1.0 / refund.size());
                relation.addAll(refund);
                break;
            case "Article244_2":
                defineArticlePriorProbability(n, "TheftUnlawfulUseOfVehicles", rootArticle, relation);
                log.info("Article244_2");
                // hasOffenceCharacteristic -> ForcedVehicle (agravante por fuerza en las cosas)
                LinkedHashSet<Relationship> forced = checkSomeRelationTypeAndObject(n,
                        "ns0__hasOffenceCharacteristic",
                        "ns0__ForcedVehicle",
                        relation, 3);
                setProbabilityElementAndFactor(forced, 3, 1.0 / forced.size());
                relation.addAll(forced);
                break;
        }

    }

}
