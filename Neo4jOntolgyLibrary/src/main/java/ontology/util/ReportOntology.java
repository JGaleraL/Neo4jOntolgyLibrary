/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;
import uDataTypes.SBoolean;

/**
 *
 * @author fjnavarrete
 */
public class ReportOntology {

    @Context
    public GraphDatabaseService db;

    @Context
    public Transaction tx;

    @Context
    public Log log;

    @Procedure(value = "ontology.util.addArticlePriorProbability", mode = Mode.WRITE)
    @Description("ontology.util.addArticlePriorProbability(.....")
    public Stream<RelationshipProbabilities> addArticlePriorProbability(
            @Name("node") Node node,
            @Name("article") String article) {

        Map<String, Properties> lawProp = new HashMap<>();
        Properties appProp = new Properties();
        Report report = null;

        if (node == null)
            throw new IllegalArgumentException("Nodes is Null");
        if (article == null)
            throw new IllegalArgumentException("Article is Null");
        LinkedHashSet<Relationship> relation = new LinkedHashSet<>();
        List<String> probabilities = new ArrayList();
        try {
            log.info("Load files properties...");
            readFileProperties(lawProp, appProp);
        } catch (IOException ex) {
            log.error("Error loading files properties");
            Logger.getLogger(ReportOntology.class.getName()).log(Level.SEVERE, null, ex);
        }

        /***
         * Select law, generate report and load properties into report
         */
        log.info("selectLaw: " + article);
        report = selectLaw(article, lawProp);
        log.info("report is null? " + (report == null));
        if (report == null)
            return null;
        log.info("Pre - loadProps ");
        report.loadProps(lawProp, appProp);

        // report.lawProp.putAll(lawProp);
        // report.appProp.putAll(lawProp);
        log.info("Pre - defineArticlePriorProbability");
        report.defineArticlePriorProbability(node, article, article, relation);
        log.info("Post - defineArticlePriorProbability");
        List<Relationship> r = new ArrayList<>();
        r.addAll(0, relation);
        return Stream.of(new RelationshipProbabilities(r, probabilities));
    }

    // Precondiciones: Los nombres de los nodos son únicos en el grafo
    @Procedure(value = "ontology.util.assignOpinion", mode = Mode.WRITE)
    @Description("ontology.util.assignOpinion(.....")
    public Stream<Relations> assignOpinion(
            @Name("example") String example,
            @Name("previous_node_name") String previous_node,
            @Name("previous_node_label") String previous_label,
            @Name("relation_type") String relation_type,
            @Name("back_node_name") String back_node,
            @Name("back_node_label") String back_label,
            @Name("revisor") String revisor,
            @Name("belief") String belief,
            @Name("uncertainty") String uncertainty) {
        // Sentencia para recuperar la opinion a contrastar
        String s1 = String.format("match (n1:%s)-[r1:%s]-(n2:%s) ", previous_label, relation_type, back_label);
        String s2 = String.format("where n1.name = '%s' and n2.name = '%s' and r1.example='%s' ", previous_node,
                back_node, example);
        String s3 = String.format("and r1.typeReportRelation is null return r1.opinion");

        // log.info(s1+s2+s3);
        Result r = tx.execute(s1 + s2 + s3);
        // log.info("results: " + r.resultAsString());

        String opinions = "";
        if (r.hasNext()) { // Existen opiniones
            Map<String, Object> row = r.next();
            for (Map.Entry<String, Object> column : row.entrySet()) {
                // Node node = (Node) column.getValue();
                opinions = (String) column.getValue();
            }
        }
        log.info("Opiniones: " + opinions);
        String newOpinions = Report.mergeOpinion((opinions == null ? "" : opinions), revisor, belief, uncertainty);

        // Sentencia para recuperar la opinion a contrastar
        String set1 = String.format("match (n1:%s)-[r1:%s]-(n2:%s) ", previous_label, relation_type, back_label);
        String set2 = String.format("where n1.name = '%s' and n2.name = '%s' and r1.example='%s' ", previous_node,
                back_node, example);
        String set3 = String
                .format("and r1.typeReportRelation <> 'not_considered' and r1.typeReportRelation <> 'created' ");
        String set4 = String.format("set r1.opinion = '%s' return r1", newOpinions);
        String s4 = String.format("and r1.typeReportRelation is null set r1.opinion = '%s' return r1", newOpinions);

        // Actualización de los reports con artículos con tipos de relaciones neccessary
        // y surplus
        // log.info(set1+set2+set3+set4);
        Result r2 = tx.execute(set1 + set2 + set3 + set4);
        // log.info("results: " + r2.resultAsString());
        // log.info("results: Postconmmit");
        List<Relationship> listRelations = new ArrayList<>();
        while (r2.hasNext()) {
            Map<String, Object> row = r2.next();
            for (Map.Entry<String, Object> column : row.entrySet()) {
                Relationship rel = (Relationship) column.getValue();
                listRelations.add(rel);
            }
        }
        // log.info("listOpinions: " + listRelations.toString());
        // Actualización del report original
        // log.info(s1+s2+s4);
        Result r3 = tx.execute(s1 + s2 + s4);
        // log.info("results: " + r3.resultAsString());
        while (r3.hasNext()) {
            Map<String, Object> row = r3.next();
            for (Map.Entry<String, Object> column : row.entrySet()) {
                Relationship rel = (Relationship) column.getValue();
                listRelations.add(rel);
            }
        }
        return Stream.of(new Relations(listRelations));
        // return Stream.of(new AssignOpinion(relation, "opinion_" + revisor + ": " +
        // belief + "%" + uncertainty));
    }

    // Precondiciones: Se traslada la relación a modificar
    @Procedure(value = "ontology.util.assignOpinion2", mode = Mode.WRITE)
    @Description("ontology.util.assignOpinion2(.....")
    public Stream<Relations> assignOpinion2(
            @Name("relation") Relationship relation,
            @Name("revisor") String revisor,
            @Name("belief") String belief,
            @Name("uncertainty") String uncertainty) {

        String opinions = "";
        if (relation.hasProperty("opinion"))
            opinions = (String) relation.getProperty("opinion");
        log.info("assignOpinion2 Opiniones: " + opinions);
        String newOpinions = Report.mergeOpinion((opinions == null ? "" : opinions), revisor, belief, uncertainty);

        List<Relationship> listRelations = new ArrayList<>();
        listRelations.add(relation);

        // Borro la opinion si está vacia
        if (newOpinions == null || newOpinions.isBlank())
            relation.removeProperty("opinion");
        else
            relation.setProperty("opinion", newOpinions);

        // Funciones para insertar las beliefFusion
        // Caso en que ya hay dos
        // log.info("assignOpinion2 Report.extractReviewers(listRelations).size(): " +
        // Report.extractReviewers(listRelations).size());
        if (Report.extractReviewers(listRelations).size() > 1) {
            String beliefFusion = "ABF:" + Report.bFusion(listRelations, new ArrayList<>(), "ABF") + "%"
                    + "WBF:" + Report.bFusion(listRelations, new ArrayList<>(), "WBF") + "%"
                    + "CBF:" + Report.bFusion(listRelations, new ArrayList<>(), "CBF") + "%"
                    + "CaCF:" + Report.bFusion(listRelations, new ArrayList<>(), "CaCF");
            relation.setProperty("fusion", beliefFusion);
            log.info("\t assignOpinion2 fusion: " + beliefFusion + "\n------------------\n\n");
        } else
            relation.removeProperty("fusion");

        // log.info("assignOpinion2 Fin");
        return Stream.of(new Relations(listRelations));
        // return Stream.of(new AssignOpinion(relation, "opinion_" + revisor + ": " +
        // belief + "%" + uncertainty));
    }

    @UserFunction(value = "ontology.util.getOPinion")
    @Description("ontology.util.getOPinion(.....)")
    public String getRelOPinion(
            @Name("relation") Relationship relation,
            @Name("revisor") String revisor) {
        if (relation == null)
            throw new IllegalArgumentException("Relations is Null");
        if (revisor == null)
            throw new IllegalArgumentException("Revisor is Null");

        return Report.extractSOpinion(relation, revisor);
    }

    @UserFunction(value = "ontology.util.getNodeOpinion")
    @Description("ontology.util.getNodeOpinion(.....)")
    public String getNodeOpinion(
            @Name("relation") Node node,
            @Name("revisor") String revisor) {
        if (node == null)
            throw new IllegalArgumentException("Relations is Null");
        if (revisor == null)
            throw new IllegalArgumentException("Revisor is Null");

        return Report.extractSOpinionNode(node, revisor);
    }

    @Procedure(value = "ontology.util.assignOpinionNode", mode = Mode.WRITE)
    @Description("ontology.util.assignOpinionNode(.....")
    public Stream<Nodes> assignOpinionNode(
            @Name("example") String example,
            @Name("previous_node_name") String node_name,
            @Name("previous_node_label") String node_label,
            @Name("revisor") String revisor,
            @Name("belief") String belief,
            @Name("uncertainty") String uncertainty) {
        // Sentencia para recuperar la opinion a contrastar
        String s1 = String.format("match (n1:%s) ", node_label);
        String s2 = String.format("where n1.name = '%s' and n1.example='%s' ", node_name, example);
        String s3 = String.format("return n1.opinion");

        // log.info(s1+s2+s3);
        Result r = tx.execute(s1 + s2 + s3);
        // log.info("results: " + r.resultAsString());

        String opinions = "";
        if (r.hasNext()) { // Existen opiniones
            Map<String, Object> row = r.next();
            for (Map.Entry<String, Object> column : row.entrySet()) {
                // Node node = (Node) column.getValue();
                opinions = (String) column.getValue();
            }
        }
        // log.info("Opiniones: " + opinions);
        String newOpinions = Report.mergeOpinion((opinions == null ? "" : opinions), revisor, belief, uncertainty);

        String s4 = String.format("set n1.opinion = '%s' return n1", newOpinions);
        // log.info("listOpinions: " + listRelations.toString());
        // Actualización de los nodos
        // log.info(s1+s2+s4);
        Result r2 = tx.execute(s1 + s2 + s4);
        // log.info("results: " + r2.resultAsString());
        List<Node> listNodes = new ArrayList<>();
        while (r2.hasNext()) {
            Map<String, Object> row = r2.next();
            for (Map.Entry<String, Object> column : row.entrySet()) {
                Node n = (Node) column.getValue();
                listNodes.add(n);
            }
        }
        return Stream.of(new Nodes(listNodes));
    }

    // Precondiciones: Se traslada la relación a modificar
    @Procedure(value = "ontology.util.assignOpinionNode2", mode = Mode.WRITE)
    @Description("ontology.util.assignOpinionNode2(.....")
    public Stream<Nodes> assignOpinionNode2(
            @Name("node") Node node,
            @Name("revisor") String revisor,
            @Name("belief") String belief,
            @Name("uncertainty") String uncertainty) {

        String opinions = "";
        if (node.hasProperty("opinion"))
            opinions = (String) node.getProperty("opinion");
        log.info("Opiniones: " + opinions);
        String newOpinions = Report.mergeOpinion((opinions == null ? "" : opinions), revisor, belief, uncertainty);

        List<Node> listNodes = new ArrayList<>();
        node.setProperty("opinion", newOpinions);
        listNodes.add(node);
        return Stream.of(new Nodes(listNodes));
        // return Stream.of(new AssignOpinion(relation, "opinion_" + revisor + ": " +
        // belief + "%" + uncertainty));
    }

    /*****
     * 
     * Functions subGraphSubjetiveProbability deprecated
     * removed the article parameter for better use of the belief function.
     * replaced by a new version of the function
     * @Name("node") List<Relationship> relations,
     * @Name("article") String article,
     * @Name("reviewer") String reviewer
     * 
     * @UserFunction(value = "ontology.util.subGraphSubjetiveProbability")
     *                     @Description("ontology.util.subGraphSubjetiveProbability(.....")
     *                     public List<String> subGraphSubjetiveProbability(
     *                     @Name("node") List<Relationship> relations,
     *                     @Name("article") String article,
     *                     @Name("reviewer") String reviewer
     *                     )
     *                     {
     *                     // LinkedHashSet<String> necessaryList = new
     *                     LinkedHashSet<>();
     *                     String TheftPerpretator = "";
     *                     String Stolengoods = "";
     *                     String BurglaryCrime = "";
     *                     String HouseOrPremiseBreaking = "";
     *                     String RobberyWithIntimidationOrViolence = "";
     *                     int surplus = 0, creation = 0;
     *                     List<SBoolean> neccesaryOpinions = new ArrayList<>();
     *                     List<SBoolean> createdOpinions = new ArrayList<>();
     *                     List<SBoolean> createdTrueOpinions = new ArrayList<>();
     *                     List<SBoolean> surplusOpinions = new ArrayList<>();
     *                     Map<Integer,SBoolean> opinions = new HashMap<>();
     * 
     *                     List<String> result = new ArrayList();
     *                     if (relations == null ) throw new
     *                     IllegalArgumentException("Nodes is Null");
     *                     if (article == null ) throw new
     *                     IllegalArgumentException("Article is Null");
     *                     // log.info("\nStart SubgraphProbability");
     * 
     *                     for (Relationship r : relations){
     *                     if
     *                     (!r.getAllProperties().keySet().contains("typeReportRelation")
     *                     ||
     *                     ("".equals(r.getProperty("typeReportRelation").toString()))){
     *                     result.add("error: Subgraph relations without
     *                     typeReportRelation");
     *                     return result;
     *                     }
     *                     if
     *                     ("necessary".equals(r.getProperty("typeReportRelation").toString())){
     *                     int probabilityElement =
     *                     Integer.parseInt(r.getProperty("probabilty_element").toString());
     *                     //
     *                     necessaryList.add(r.getProperty("probabilty_element").toString());
     *                     SBoolean o = Report.extractOpinion(r, reviewer)!=null ?
     *                     Report.extractOpinion(r, reviewer):
     *                     SBoolean.createDogmaticOpinion(1.0D, 0.5D);
     *                     // log.info("necces rel: " + r.getType().name() +"["+
     *                     probabilityElement +"]" + ":: opinion = " +
     *                     o.toString());
     * 
     *                     opinions.computeIfPresent(probabilityElement, (key, val)
     *                     ->
     *                     {
     *                     // log.info("\t Previus: " + r.getType().name() +"["+
     *                     probabilityElement +"] = " + val.toString());
     *                     val.coProduct(o);
     *                     // log.info("\t Post: " + r.getType().name() +"["+
     *                     probabilityElement +"] = " + val.toString());
     *                     return val;
     *                     });
     *                     opinions.computeIfAbsent(probabilityElement, key -> o);
     *                     }
     *                     if
     *                     ("surplus".equals(r.getProperty("typeReportRelation").toString())){
     *                     surplus++;
     *                     SBoolean o = Report.extractOpinion(r, reviewer)!=null ?
     *                     Report.extractOpinion(r, reviewer) :
     *                     SBoolean.createDogmaticOpinion(0.0D, 0.5D);
     *                     // log.info("surplus rel: " + r.getType().name() + "::
     *                     opinion = " + o.toString());
     * 
     *                     surplusOpinions.add(o);
     *                     }
     *                     if
     *                     ("created".equals(r.getProperty("typeReportRelation").toString())){
     *                     creation++;
     *                     createdOpinions.add(SBoolean.createDogmaticOpinion(0.0D,
     *                     0.5D));
     *                     createdTrueOpinions.add(SBoolean.createDogmaticOpinion(1.0D,
     *                     0.5D));
     *                     }
     *                     if
     *                     (r.getStartNode().getAllProperties().keySet().contains("TheftPerpretator")){
     *                     TheftPerpretator =
     *                     Report.getQualifiedRelationship(r.getStartNode(),
     *                     "TheftPerpretator");
     *                     Stolengoods =
     *                     Report.getQualifiedRelationship(r.getStartNode(),
     *                     "Stolengoods");
     *                     BurglaryCrime =
     *                     Report.getQualifiedRelationship(r.getStartNode(),
     *                     "BurglaryCrime");
     *                     HouseOrPremiseBreaking =
     *                     Report.getQualifiedRelationship(r.getStartNode(),
     *                     "HouseOrPremiseBreaking");
     *                     RobberyWithIntimidationOrViolence =
     *                     Report.getQualifiedRelationship(r.getStartNode(),
     *                     "RobberyWithIntimidationOrViolence");
     *                     }
     *                     }
     * 
     *                     // Generate the list of neccesary opinions
     *                     opinions.forEach((k,v) -> {
     *                     neccesaryOpinions.add(v);
     *                     });
     * 
     *                     // Calculate WeightedUnion of necessary and created
     *                     opinions
     *                     List<SBoolean> ncreatedOpinions = new ArrayList<>();
     *                     ncreatedOpinions.addAll(neccesaryOpinions);
     *                     // log.info("wuNecessary: " +
     *                     SBoolean.weightedUnion(ncreatedOpinions).toString() );
     *                     ncreatedOpinions.addAll(createdOpinions);
     *                     SBoolean wuNecessaryCreated =
     *                     SBoolean.weightedUnion(ncreatedOpinions);
     *                     // log.info("wuNecessaryCreated: " +
     *                     wuNecessaryCreated.toString() + " " +
     *                     wuNecessaryCreated.projection());
     * 
     *                     // Calculate WeightedUnion of necessary, !created and
     *                     surplus opinions
     *                     List<SBoolean> nTrueCreatedsurplusOpinions = new
     *                     ArrayList<>();
     *                     nTrueCreatedsurplusOpinions.addAll(neccesaryOpinions);
     *                     nTrueCreatedsurplusOpinions.addAll(createdTrueOpinions);
     *                     nTrueCreatedsurplusOpinions.addAll(surplusOpinions);
     *                     SBoolean wuNecessaryTrueCreatedSurplus =
     *                     SBoolean.weightedUnion(nTrueCreatedsurplusOpinions);
     *                     // log.info("wuNecessaryTrueCreatedSurplus: " +
     *                     wuNecessaryTrueCreatedSurplus.toString() );
     * 
     *                     // Product WeightedUnion elements
     *                     SBoolean resultOpinion =
     *                     wuNecessaryTrueCreatedSurplus.and(wuNecessaryCreated);
     *                     log.info(article + "-" + reviewer + ":: " +
     *                     "resultOpinion: " + resultOpinion.toString() + " "
     *                     + resultOpinion.projection() + " " +
     *                     resultOpinion.uncertainty());
     * 
     *                     result.add(Double.toString(resultOpinion.projection()));
     *                     result.add(Double.toString(resultOpinion.uncertainty()));
     *                     result.add(resultOpinion.toString());
     *                     if (!"".equals(TheftPerpretator))
     *                     result.add("TheftPerpretator: " + TheftPerpretator);
     *                     if (!"".equals(Stolengoods)) result.add("Stolengoods: " +
     *                     Stolengoods);
     *                     if (!"".equals(BurglaryCrime)) result.add("BurglaryCrime:
     *                     " + BurglaryCrime);
     *                     if (!"".equals(HouseOrPremiseBreaking))
     *                     result.add("HouseOrPremiseBreaking: " +
     *                     HouseOrPremiseBreaking);
     *                     if (!"".equals(RobberyWithIntimidationOrViolence))
     *                     result.add("RobberyWithIntimidationOrViolence: " +
     *                     RobberyWithIntimidationOrViolence);
     *                     return result;
     *                     }
     * 
     * 
     * 
     */

    @UserFunction(value = "ontology.util.subGraphSubjetiveProbability")
    @Description("ontology.util.subGraphSubjetiveProbability(.....")
    public List<String> subGraphSubjetiveProbability(
            @Name("relations") List<Relationship> relations,
            @Name("reviewer") String reviewer) {
        // LinkedHashSet<String> necessaryList = new LinkedHashSet<>();
        String TheftPerpretator = "";
        String Stolengoods = "";
        String BurglaryCrime = "";
        String HouseOrPremiseBreaking = "";
        String RobberyWithIntimidationOrViolence = "";
        // Extortion (Article243)
        String ExtortionViolence = "";
        String ProfitIntent = "";
        String EconomicDamage = "";
        // Usurpation (Article245_1 / Article245_2)
        String BuildingOccupation = "";
        String RealPropertyRightUsurpation = "";
        String UsurpationViolence = "";
        String PeacefulOccupation = "";
        String NotADwelling = "";
        // TheftUnlawfulUseOfVehicles (Article244_1 / Article244_2)
        String WithoutIntendingToAppropiate = "";
        String MotorVehicle = "";
        String RefundWithin48H = "";
        String ForcedVehicle = "";
        String article = "";

        List<String> result = new ArrayList();
        if (relations == null)
            throw new IllegalArgumentException("Nodes is Null");
        // if (article == null ) throw new IllegalArgumentException("Article is Null");
        // log.info("\nStart SubgraphProbability");

        for (Relationship r : relations) {
            if (r.getStartNode().getAllProperties().keySet().contains("TheftPerpretator")) {
                TheftPerpretator = Report.getQualifiedRelationship(r.getStartNode(), "TheftPerpretator");
                Stolengoods = Report.getQualifiedRelationship(r.getStartNode(), "Stolengoods");
                BurglaryCrime = Report.getQualifiedRelationship(r.getStartNode(), "BurglaryCrime");
                HouseOrPremiseBreaking = Report.getQualifiedRelationship(r.getStartNode(), "HouseOrPremiseBreaking");
                RobberyWithIntimidationOrViolence = Report.getQualifiedRelationship(r.getStartNode(),
                        "RobberyWithIntimidationOrViolence");

            }
            // Extortion tags
            if (r.getStartNode().getAllProperties().keySet().contains("ExtortionViolence")) {
                ExtortionViolence = Report.getQualifiedRelationship(r.getStartNode(), "ExtortionViolence");
                ProfitIntent = Report.getQualifiedRelationship(r.getStartNode(), "ProfitIntent");
                EconomicDamage = Report.getQualifiedRelationship(r.getStartNode(), "EconomicDamage");
            }
            // Usurpation tags
            if (r.getStartNode().getAllProperties().keySet().contains("BuildingOccupation")
                    || r.getStartNode().getAllProperties().keySet().contains("RealPropertyRightUsurpation")) {
                BuildingOccupation = Report.getQualifiedRelationship(r.getStartNode(), "BuildingOccupation");
                RealPropertyRightUsurpation = Report.getQualifiedRelationship(r.getStartNode(),
                        "RealPropertyRightUsurpation");
                UsurpationViolence = Report.getQualifiedRelationship(r.getStartNode(), "UsurpationViolence");
                PeacefulOccupation = Report.getQualifiedRelationship(r.getStartNode(), "PeacefulOccupation");
                NotADwelling = Report.getQualifiedRelationship(r.getStartNode(), "NotADwelling");
            }
            // TheftUnlawfulUseOfVehicles tags
            if (r.getStartNode().getAllProperties().keySet().contains("WithoutIntendingToAppropiate")
                    || r.getStartNode().getAllProperties().keySet().contains("MotorVehicle")) {
                WithoutIntendingToAppropiate = Report.getQualifiedRelationship(r.getStartNode(),
                        "WithoutIntendingToAppropiate");
                MotorVehicle = Report.getQualifiedRelationship(r.getStartNode(), "MotorVehicle");
                RefundWithin48H = Report.getQualifiedRelationship(r.getStartNode(), "RefundWithin48H");
                ForcedVehicle = Report.getQualifiedRelationship(r.getStartNode(), "ForcedVehicle");
            }
            if (r.getStartNode().getAllProperties().keySet().contains("article"))
                article = (String) r.getStartNode().getProperty("article");
        }

        // Product WeightedUnion elements
        // log.info(article + "-" + reviewer + ":: " + "PreResultOpinion: ");
        SBoolean resultOpinion = Report.subjetiveProbability(relations, reviewer);
        log.info(article + "-" + reviewer + ":: " + "resultOpinion: " + resultOpinion.toString() + "  "
                + resultOpinion.projection() + "  " + resultOpinion.uncertainty());

        result.add(Double.toString(resultOpinion.projection()));
        result.add(Double.toString(resultOpinion.uncertainty()));
        result.add(resultOpinion.toString());
        if (!"".equals(TheftPerpretator))
            result.add("TheftPerpretator: " + TheftPerpretator);
        if (!"".equals(Stolengoods))
            result.add("Stolengoods: " + Stolengoods);
        if (!"".equals(BurglaryCrime))
            result.add("BurglaryCrime: " + BurglaryCrime);
        if (!"".equals(HouseOrPremiseBreaking))
            result.add("HouseOrPremiseBreaking: " + HouseOrPremiseBreaking);
        if (!"".equals(RobberyWithIntimidationOrViolence))
            result.add("RobberyWithIntimidationOrViolence: " + RobberyWithIntimidationOrViolence);
        // Extortion
        if (!"".equals(ExtortionViolence))
            result.add("ExtortionViolence: " + ExtortionViolence);
        if (!"".equals(ProfitIntent))
            result.add("ProfitIntent: " + ProfitIntent);
        if (!"".equals(EconomicDamage))
            result.add("EconomicDamage: " + EconomicDamage);
        // Usurpation
        if (!"".equals(BuildingOccupation))
            result.add("BuildingOccupation: " + BuildingOccupation);
        if (!"".equals(RealPropertyRightUsurpation))
            result.add("RealPropertyRightUsurpation: " + RealPropertyRightUsurpation);
        if (!"".equals(UsurpationViolence))
            result.add("UsurpationViolence: " + UsurpationViolence);
        if (!"".equals(PeacefulOccupation))
            result.add("PeacefulOccupation: " + PeacefulOccupation);
        if (!"".equals(NotADwelling))
            result.add("NotADwelling: " + NotADwelling);
        // TheftUnlawfulUseOfVehicles
        if (!"".equals(WithoutIntendingToAppropiate))
            result.add("WithoutIntendingToAppropiate: " + WithoutIntendingToAppropiate);
        if (!"".equals(MotorVehicle))
            result.add("MotorVehicle: " + MotorVehicle);
        if (!"".equals(RefundWithin48H))
            result.add("RefundWithin48H: " + RefundWithin48H);
        if (!"".equals(ForcedVehicle))
            result.add("ForcedVehicle: " + ForcedVehicle);
        return result;
    }

    @UserFunction(value = "ontology.util.subGraphFusionProbability")
    @Description("ontology.util.subGraphFusionProbability(.....")
    public List<String> subGraphFusionProbability(
            @Name("relations") String article,
            @Name("relations") List<Relationship> relations,
            @Name("operation") String reviewer) {
        List<String> result = new ArrayList();
        List<String> rs = new ArrayList<>();

        rs = Report.extractReviewers(relations);
        if (relations == null)
            throw new IllegalArgumentException("Nodes is Null");
        log.info("Fusion article " + article + "::" + reviewer);
        SBoolean resultOpinion = Report.subjetiveProbability(relations, reviewer);
        log.info("Fusion article " + article + "::" + reviewer + ":: " + "resultOpinion: " + resultOpinion.toString()
                + "  ");

        result.add(Double.toString(resultOpinion.projection()));
        result.add(Double.toString(resultOpinion.belief()));
        result.add(Double.toString(resultOpinion.uncertainty()));
        result.add(resultOpinion.toString());
        result.add(rs.toString());
        return result;
    }

    @UserFunction(value = "ontology.util.beliefFusion")
    @Description("ontology.util.beliefFusion(.....)")
    public List<String> beliefFusion(
            @Name("relations") List<Relationship> relations,
            @Name("reviewers") List<String> reviewers,
            @Name("operation") String operation

    ) {
        List<SBoolean> opinions = new ArrayList<>();
        List<String> rs = new ArrayList<>();
        log.info("reviewers: " + reviewers.toString());
        if (relations == null)
            throw new IllegalArgumentException("Relations is Null");
        if (reviewers == null)
            throw new IllegalArgumentException("Reviewers is Null");
        rs = Report.extractReviewers(relations);

        if (!reviewers.isEmpty()) {
            if (!rs.containsAll(reviewers))
                throw new IllegalArgumentException("Some reviewer has not given an opinion");
            else
                rs = List.copyOf(reviewers);
        }

        log.info("rs: " + rs.toString());

        for (var reviewer : rs) {
            // log.info("rev: " + reviewer);
            opinions.add(Report.subjetiveProbability(relations, reviewer));
            // log.info("sp: " +
            // Report.subjetiveProbability(relations,reviewer).toString());
        }

        BeliefFusion result = new BeliefFusion();
        List<String> rl = new ArrayList<>();
        result.ABF = SBoolean.averageBeliefFusion(opinions).toString();
        result.CBF = SBoolean.cumulativeBeliefFusion(opinions).toString();
        result.WBF = SBoolean.weightedBeliefFusion(opinions).toString();
        result.CaCF = SBoolean.consensusAndCompromiseFusion(opinions).toString();
        result.revisors = rs;
        // log.info("bf: " + result.toString());
        switch (operation) {
            case "ABF":
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).projection()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).belief()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).uncertainty()));
                rl.add(result.ABF);
                break;
            case "CBF":
                rl.add(Double.toString(SBoolean.cumulativeBeliefFusion(opinions).projection()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).belief()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).uncertainty()));
                rl.add(result.CBF);
                break;
            case "WBF":
                rl.add(Double.toString(SBoolean.weightedBeliefFusion(opinions).projection()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).belief()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).uncertainty()));
                rl.add(result.CaCF);
                break;
            case "CaCF":
                rl.add(Double.toString(SBoolean.consensusAndCompromiseFusion(opinions).projection()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).belief()));
                rl.add(Double.toString(SBoolean.averageBeliefFusion(opinions).uncertainty()));
                rl.add(result.CaCF);
                break;
            default:
                rl.add("unregistered operation");
        }
        rl.add(result.revisors.toString());
        return rl;
    }

    @UserFunction(value = "ontology.util.reviewers")
    @Description("ontology.util.reviewers(.....)")
    public List<String> reviewers(
            @Name("relations") List<Relationship> relations) {
        if (relations == null)
            throw new IllegalArgumentException("Relations is Null");
        return Report.extractReviewers(relations);
    }

    @UserFunction(value = "ontology.util.subGraphProbability")
    @Description("ontology.util.subGraphProbability(.....")
    public List<String> subGraphProbability(
            @Name("node") List<Relationship> relations,
            @Name("article") String article) {
        LinkedHashSet<String> necessary = new LinkedHashSet<>();
        String TheftPerpretator = "";
        String Stolengoods = "";
        String BurglaryCrime = "";
        String HouseOrPremiseBreaking = "";
        String RobberyWithIntimidationOrViolence = "";
        int surplus = 0, creation = 0;
        double probability = 0.0;
        double probabilityFactor[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }; // 10 elements
        List<String> result = new ArrayList();
        if (relations == null)
            throw new IllegalArgumentException("Nodes is Null");
        if (article == null)
            throw new IllegalArgumentException("Article is Null");
        for (Relationship r : relations) {
            if (!r.getAllProperties().keySet().contains("typeReportRelation")
                    || ("".equals(r.getProperty("typeReportRelation").toString()))) {
                result.add("error: Subgraph relations without typeReportRelation");
                return result;
            }

            if ("necessary".equals(r.getProperty("typeReportRelation").toString())) {
                // log.info(r.getProperty("typeReportRelation").toString() + ": " +
                // r.getProperty("factor").toString());
                int probabilityElement = Integer.parseInt(r.getProperty("probabilty_element").toString());
                necessary.add(r.getProperty("probabilty_element").toString());
                double factor = Double.parseDouble(r.getProperty("factor").toString());
                probabilityFactor[probabilityElement] = probabilityFactor[probabilityElement] + factor;
            }
            if ("surplus".equals(r.getProperty("typeReportRelation").toString())) {
                // log.info(r.getProperty("typeReportRelation").toString() + ": ");
                surplus++;
            }
            if ("created".equals(r.getProperty("typeReportRelation").toString())) {
                // log.info(r.getProperty("typeReportRelation").toString() + ": ");
                creation++;
            } else {
                // log.info(r.getProperty("typeReportRelation").toString() + ": ");
            }

            if (r.getStartNode().getAllProperties().keySet().contains("TheftPerpretator")) {
                TheftPerpretator = Report.getQualifiedRelationship(r.getStartNode(), "TheftPerpretator");
                Stolengoods = Report.getQualifiedRelationship(r.getStartNode(), "Stolengoods");
                BurglaryCrime = Report.getQualifiedRelationship(r.getStartNode(), "BurglaryCrime");
                HouseOrPremiseBreaking = Report.getQualifiedRelationship(r.getStartNode(), "HouseOrPremiseBreaking");
                RobberyWithIntimidationOrViolence = Report.getQualifiedRelationship(r.getStartNode(),
                        "RobberyWithIntimidationOrViolence");
            }
        }

        for (int x = 0; x < probabilityFactor.length; x++) {
            probability = probability + probabilityFactor[x] / (necessary.size() + creation);
            // log.info(x+1 + "element: " + probability );
        }
        log.info("total element: " + probability);
        log.info("creation: " + creation);
        log.info("surplus: " + surplus);
        log.info("necessary: " + necessary.size());
        log.info("final factor: " + (necessary.size() + creation) / (necessary.size() + creation + surplus));
        probability = probability * (necessary.size() + creation) / (necessary.size() + creation + surplus);
        result.add(Double.toString(probability));
        if (!"".equals(TheftPerpretator))
            result.add("TheftPerpretator: " + TheftPerpretator);
        if (!"".equals(Stolengoods))
            result.add("Stolengoods: " + Stolengoods);
        if (!"".equals(BurglaryCrime))
            result.add("BurglaryCrime: " + BurglaryCrime);
        if (!"".equals(HouseOrPremiseBreaking))
            result.add("HouseOrPremiseBreaking: " + HouseOrPremiseBreaking);
        if (!"".equals(RobberyWithIntimidationOrViolence))
            result.add("RobberyWithIntimidationOrViolence: " + RobberyWithIntimidationOrViolence);
        return result;
    }

    /*
     * Specific Method
     */
    void readFileProperties(Map<String, Properties> lawProp, Properties appProp)
            throws FileNotFoundException, IOException {

        // load AssaultingEmergencyWorkerCrimeReport_en properties
        log.info("Load files properties: AssaultingEmergencyWorkerCrimeReport_en.properties");
        Properties p = new Properties();
        InputStream propertiesStream = ClassLoader
                .getSystemResourceAsStream("AssaultingEmergencyWorkerCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("AssaultingEmergencyWorkerCrimeReport_en", p);
        propertiesStream.close();

        // load CoercionCrimeReport_en
        log.info("Load files properties: CoercionCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("CoercionCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("CoercionCrimeReport_en", p);
        propertiesStream.close();

        // load DamageCrimeReport_en
        log.info("Load files properties: DamageCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("DamageCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("DamageCrimeReport_en", p);
        propertiesStream.close();

        // load HouseOrPremiseBreakingCrimeReport_en properties
        log.info("Load files properties: HouseOrPremiseBreakingCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("HouseOrPremiseBreakingCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("HouseOrPremiseBreakingCrimeReport_en", p);
        propertiesStream.close();

        // load InjuryCrimeReport_en properties
        log.info("Load files properties: InjuryCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("InjuryCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("InjuryCrimeReport_en", p);
        propertiesStream.close();

        // load TheftUnlawfulUseOfVehiclesCrimeReport_en properties
        log.info("Load files properties: TheftUnlawfulUseOfVehiclesCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("TheftUnlawfulUseOfVehiclesCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("TheftUnlawfulUseOfVehiclesCrimeReport_en", p);
        propertiesStream.close();

        // // loadPropertyCrimeReport_en properties
        // log.info("Load files properties: loadPropertyCrimeReport_en.properties");
        // p = new Properties();
        // propertiesStream =
        // ClassLoader.getSystemResourceAsStream("PropertyCrimeReport_en.properties");
        // p.load(propertiesStream);
        // appProp.putAll(p);
        // lawProp.put("PropertyCrimeReport_en", p);
        // propertiesStream.close();

        // loadRobberyCrimeReport_en properties
        log.info("Load files properties: loadRobberyCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("RobberyCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("RobberyCrimeReport_en", p);
        propertiesStream.close();

        // loadTheftCrimeReport_en properties
        log.info("Load files properties: loadTheftCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("TheftCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("TheftCrimeReport_en", p);
        propertiesStream.close();

        // load ThreatsCrimeReport_en properties
        log.info("Load files properties: ThreatsCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("ThreatsCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("ThreatsCrimeReport_en", p);
        propertiesStream.close();

        // load ExtortionCrimeReport_en properties
        log.info("Load files properties: ExtortionCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("ExtortionCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("ExtortionCrimeReport_en", p);
        propertiesStream.close();

        // load UsurpationCrimeReport_en properties
        log.info("Load files properties: UsurpationCrimeReport_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("UsurpationCrimeReport_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        lawProp.put("UsurpationCrimeReport_en", p);
        propertiesStream.close();

        // load OntologyDataProperties_en properties
        log.info("Load files properties: OntologyDataProperties_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("OntologyDataProperties_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        propertiesStream.close();

        // load OntologyEntities_en properties
        log.info("Load files properties: OntologyEntities_en_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("OntologyEntities_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        propertiesStream.close();

        // load OntologyObjects_en properties
        log.info("Load files properties: OntologyObjects_en.properties");
        p = new Properties();
        propertiesStream = ClassLoader.getSystemResourceAsStream("OntologyObjects_en.properties");
        p.load(propertiesStream);
        appProp.putAll(p);
        propertiesStream.close();
    }

    String findLaw(String article, Map<String, Properties> lawProp) {
        var law_temp = new Object() {
            String value = null;
        };
        log.info("findLaw: lawprop is null? " + lawProp.isEmpty());

        lawProp.forEach((k, v) -> {
            if (v.containsKey(article))
                law_temp.value = k;
        });
        return law_temp.value;
    }

    Report selectLaw(String article, Map<String, Properties> lawProp) {
        String l = findLaw(article, lawProp);
        log.info("findLaw: " + l);
        Report r;
        switch (l) {
            case "AssaultingEmergencyWorkerCrimeReport_en":
                r = new AssaultingEmergencyWorkerCrimeReport();
                break;
            case "CoercionCrimeReport_en":
                r = new CoercionCrimeReport();
                break;
            case "DamageCrimeReport_en":
                r = new DamageCrimeReport();
                break;
            case "HouseOrPremiseBreakingCrimeReport_en":
                r = new HouseOrPremiseBreakingCrimeReport();
                break;
            case "InjuryCrimeReport_en":
                r = new InjuryCrimeReport();
                break;
            case "TheftCrimeReport_en":
                r = new PropertyCrimeReport();
                break;
            case "RobberyCrimeReport_en":
                r = new PropertyCrimeReport();
                break;
            case "TheftUnlawfulUseOfVehiclesCrimeReport_en":
                r = new PropertyCrimeReport();
                break;
            case "ThreatsCrimeReport_en":
                r = new ThreatsCrimeReport();
                break;
            case "ExtortionCrimeReport_en":
                r = new ExtortionCrimeReport();
                break;
            case "UsurpationCrimeReport_en":
                r = new UsurpationCrimeReport();
                break;
            default:
                return null;
        }
        r.law = l;
        r.log = log;
        r.tx = tx;
        r.db = db;

        return r;
    }

    /*
     * static class
     */
    public static class RelationshipProbabilities {
        // These records contain two lists of distinct relationship types going in and
        // out of a Node.
        public List<Relationship> relations;
        // public List<String> probabilities;

        public RelationshipProbabilities(List<Relationship> relations, List<String> probabilities) {
            this.relations = relations;
            // this.probabilities = probabilities;
        }
    } // end static class RelationshipProbabilities

    public static class Nodes {
        // These records contain two lists of distinct relationship types going in and
        // out of a Node.
        public List<Node> nodes;
        // public List<String> probabilities;

        public Nodes(List<Node> nodes) {
            this.nodes = nodes;
            // this.probabilities = probabilities;
        }
    }

    public static class Relations {
        public List<Relationship> relations;

        public Relations(List<Relationship> relations) {
            this.relations = relations;

        }
    }

    public static class Opinions {
        // These records contain two lists of distinct relationship types going in and
        // out of a Node.
        public List<String> opinions;
        // public List<String> probabilities;

        public Opinions(List<String> opinions) {
            this.opinions = opinions;
            // this.probabilities = probabilities;
        }
    }

    public static class AssignOpinion {
        // These records contain two lists of distinct relationship types going in and
        // out of a Node.
        public Relationship relation;
        public String opinion;

        public AssignOpinion(Relationship r, String o) {
            this.relation = r;
            this.opinion = o;
        }
    } // end static class AssignOpinion

    public static class AssignOpinionNode {
        // These records contain two lists of distinct relationship types going in and
        // out of a Node.
        public Node node;
        public String opinion;

        public AssignOpinionNode(Node n, String o) {
            this.node = n;
            this.opinion = o;
        }
    } // end static class AssignOpinion

    public static class BeliefFusion {
        // These records contain two lists of distinct relationship types going in and
        // out of a Node.
        public String ABF;
        public String WBF;
        public String BCF;
        public String CBF;
        public String CaCF;
        public List<String> revisors;

        public BeliefFusion(String abf, String cbf, String wbf, String cacf) {
            this.ABF = abf;
            this.CBF = cbf;
            this.WBF = wbf;
            this.CaCF = cacf;

        }

        public BeliefFusion() {
            this.ABF = "";
            this.CBF = "";
            this.WBF = "";
            this.CaCF = "";
            this.revisors = new ArrayList<>();

        }
    }

} // end class ReportOntology
