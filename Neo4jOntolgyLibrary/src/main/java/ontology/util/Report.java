/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontology.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import uDataTypes.SBoolean;

/**
 *
 * @author franciscoj.navarrete
 */
public abstract class Report {
    
    
    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;
    
    @Context 
    public Transaction tx;
    
    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;
    

    public Map<String, Properties> lawProp = new HashMap<>();
    public Properties appProp = new Properties();
    public String law = "";
    
    
    
    /*
     *  Abstract method
    */
    public  abstract void defineArticlePriorProbability(Node n, 
                                            String article, 
                                            String rootArticle, 
                                            LinkedHashSet<Relationship> relation);
    
    
    /*****************
     * Auxiliar methods
     */
    
    void loadProps ( Map<String, Properties> law, Properties app){
        lawProp.putAll(law);
        appProp.putAll(app);
    }
    
//    static SBoolean extractOpinion(Relationship r){
//        List<SBoolean> l = new ArrayList<>(); 
//        if (r.getAllProperties().keySet().contains("opinion")){            
//            String params[] = r.getProperty("opinion").toString().split("%");
//            if (params.length!=2)
//                throw new IllegalArgumentException("extractOpinion: Illegal parameter");
//
//            Double p = Double.valueOf(params[0]);
//            Double un = Report.mapUncertainty(params[1]);  
////            log.info("extractOpinion projection: " + p/100.0D);
////            log.info("extractOpinion uncertainty: " + un);
//        
//            l.add(SBoolean.binomicalOpinion(p/100.0D, un));
//        }
//        SBoolean sn = extractOpinionNode(r.getStartNode());
//        SBoolean en = extractOpinionNode(r.getEndNode());
//        if (sn != null) l.add(sn);
//        if (en != null) l.add(en);
////        if (!l.isEmpty()) 
////            log.info("extractOpinion list: " + l.toString());
////        else 
////            log.info("extractOpinion list: null");
//        return l.isEmpty()?null:SBoolean.and(l);
//    }
    
//    static SBoolean extractOpinion(Relationship r, String rev){
//        List<SBoolean> l = new ArrayList<>(); 
//        if (r.getAllProperties().keySet().contains("opinion_"+rev)){            
//            String params[] = r.getProperty("opinion_"+rev).toString().split("%");
////            System.out.println("param_0: " + params[0]);
////            System.out.println("param_1: " + params[1]);
//            if (params.length!=2)
//                throw new IllegalArgumentException("extractOpinion: Illegal parameter");
//
//            Double be = Report.mapBelief(params[0], params[1]);
//            Double un = Report.mapUncertainty(params[1]);   
////            log.info("extractOpinion belief: " + be);
////            log.info("extractOpinion uncertainty: " + un);       
//            l.add(new SBoolean(be, 1.0 - be - un, un, 0.5));
//        }
//        SBoolean sn = extractOpinionNode(r.getStartNode(), rev);
//        SBoolean en = extractOpinionNode(r.getEndNode(), rev);
//        if (sn != null) l.add(sn);
//        if (en != null) l.add(en);
////        if (!l.isEmpty()) 
////            log.info("extractOpinion list: " + l.toString());
////        else 
////            log.info("extractOpinion list: null");
//        return l.isEmpty()?null:SBoolean.and(l);
//    }
    
    static SBoolean extractOpinion(Relationship r, String rev){
        boolean isFusion = false;
        List<SBoolean> l = new ArrayList<>(); 
        switch(rev){
            case "ABF":
            case "WBF":
            case "CBF":
            case "CaCF":
                isFusion =true;
              break;  
        }
         
        if (!isFusion) {
//            System.out.println("\textractOpinion Exits opinion? " + r.getAllProperties().keySet().contains("opinion"));
            if (r.getAllProperties().keySet().contains("opinion") && !"".equals(r.getProperty("opinion"))){ 
//                System.out.println("\textractOpinion opinion: " + r.getProperty("opinion"));            
                String opinions[] = r.getProperty("opinion").toString().split("%");
//                System.out.println("extractOpinion num opinions: " + opinions.length);
                for (String opinion : opinions) {
                    String[] params = opinion.split(":");
                    if (params.length!=2)
                        throw new IllegalArgumentException("extractOpinion: Illegal parameter");
//                System.out.println("param_0: " + params[0]);
//                System.out.println("param_1: " + params[1]); 
                    if (params[0].equals(rev)){   //Si coincide con la opinion del revisor                 
                        Double un = Report.mapUncertainty(params[1]); 
                        Double be = Report.mapBelief(params[1], un);
                        l.add(new SBoolean(be, 1.0 - be - un, un, 0.5));
                    }
                }      
            }
            SBoolean sn = extractOpinionNode(r.getStartNode(), rev);
            SBoolean en = extractOpinionNode(r.getEndNode(), rev);
            if (sn != null) l.add(sn);
            if (en != null) l.add(en);    
    //        if (!l.isEmpty()) 
    //            log.info("extractOpinion list: " + l.toString());
    //        else 
    //            log.info("extractOpinion list: null");
//            System.out.println("\textractOpinion l.isEmpty()? " + l.isEmpty());    
        } else{
//            System.out.println("\textractOpinion Exits fusion? " + r.getAllProperties().keySet().contains("fusion"));
            if (r.getAllProperties().keySet().contains("fusion") && !"".equals(r.getProperty("fusion"))){ 
//                System.out.println("\textractOpinion fusion: " + r.getProperty("fusion"));            
                String fusions[] = r.getProperty("fusion").toString().split("%");
//                System.out.println("extractOpinion num fusions: " + fusions.length);
                for (String fusion : fusions) {
                    String[] params = fusion.split(":");
                    if (params.length!=2)
                        throw new IllegalArgumentException("extractOpinion: Illegal parameterssssssss");
//                System.out.println("param_0: " + params[0]);
//                System.out.println("param_1: " + params[1]); 
                    if (params[0].equals(rev)){   //Si coincide con la fusion del revisor                 
                        Double un = Report.mapUncertaintyFusion(params[1]); 
                        Double be = Report.mapBeliefFusion(params[1]);
                        l.add(new SBoolean(be, 1.0 - be - un, un, 0.5));
                    }
                }  
            }
//            SBoolean sn = extractOpinionNode(r.getStartNode(), rev);
//            SBoolean en = extractOpinionNode(r.getEndNode(), rev);
//            if (sn != null) l.add(sn);
//            if (en != null) l.add(en);    
    //        if (!l.isEmpty()) 
    //            log.info("extractOpinion list: " + l.toString());
    //        else 
    //            log.info("extractOpinion list: null");
//            System.out.println("\textractOpinion l.isEmpty()? " + l.isEmpty());
        }
        return l.isEmpty()?null:SBoolean.and(l);
    } 
    
    static String extractSOpinion(Relationship r, String rev){
        List<SBoolean> l = new ArrayList<>(); 
        if (r.getAllProperties().keySet().contains("opinion")){            
            String opinions[] = r.getProperty("opinion").toString().split("%");
            for (String opinion : opinions) {
                String[] params = opinion.split(":");
                if (params.length!=2)
                    throw new IllegalArgumentException("extractOpinion: Illegal parameter");
//            System.out.println("param_0: " + params[0]);
//            System.out.println("param_1: " + params[1]); 
                if (params[0].equals(rev)){   //Si coincide con la opinion del revisor                 
                    return params[1];
                }
            }      
        }
        return null;
    }
    
    
    static String extractSOpinionNode(Node n, String rev){
        List<SBoolean> l = new ArrayList<>(); 
        if (n.getAllProperties().keySet().contains("opinion")){            
            String opinions[] = n.getProperty("opinion").toString().split("%");
            for (String opinion : opinions) {
                String[] params = opinion.split(":");
                if (params.length!=2)
                    throw new IllegalArgumentException("extractOpinion: Illegal parameter");
//            System.out.println("param_0: " + params[0]);
//            System.out.println("param_1: " + params[1]); 
                if (params[0].equals(rev)){   //Si coincide con la opinion del revisor                 
                    return params[1];
                }
            }      
        }
        return null;
    }
    
    
    
    
    
    
    
    static String mergeOpinion(String ops, 
            String revisor, 
            String belief,  
            String uncertainty){
                    
        String resultOpinions = ""; 
        boolean existsRevisor = false;
        int i = 0;
        
        if (!"".equals(ops)){
            String opinions[] = ops.split("%");
            for (String opinion : opinions) {
                String[] params = opinion.split(":");
                if (params.length!=2)
                    throw new IllegalArgumentException("extractOpinion: Illegal parameter"); 
                if (params[0].equals(revisor)){   //Coincide con la opinion del revisor                 
                    params[1] = belief + uncertainty;
                    existsRevisor = true;
                }
                resultOpinions = resultOpinions + (i!=0? "%" : "" ) 
                        + (!"".equals(params[1]) ? params[0] + ":" + params[1] : "");
                i++;
            }
        }

        if (!existsRevisor)
            resultOpinions = resultOpinions + (!"".equals(resultOpinions)? "%" : "" ) 
                    + (!"".equals(belief + uncertainty) ? revisor + ":" + belief + uncertainty : "");
                    
        return resultOpinions;
    }
    
    static String bFusion(
            List<Relationship> relations, 
            List<String> reviewers,
            String operation
            )
    {
        List<SBoolean> opinions = new ArrayList<>();
        List<String> rs = new ArrayList<>();
//        System.out.println("\tbFusion init: " + operation);
        if (relations == null ) throw new IllegalArgumentException("Relations is Null");
        if (reviewers == null ) throw new IllegalArgumentException("Reviewers is Null");
        rs = Report.extractReviewers(relations);

        if (!reviewers.isEmpty()) {
            if (!rs.containsAll(reviewers)) 
                throw new IllegalArgumentException("Some reviewer has not given an opinion");
            else
                rs = List.copyOf(reviewers);
        }
        
//        System.out.println("\tbFusion rs: " + rs.toString());
//        System.out.println("\tbFusion relations.size(): " + relations.size());
        
        for (var reviewer : rs){
//            System.out.println("\tbFusionrev: " + reviewer);
//            System.out.println("\tbFusion sp: " +  Report.subjetiveProbability(relations,reviewer).toString());
            for (Relationship r : relations){
                SBoolean o = Report.extractOpinion(r, reviewer);
                if (o != null)
                    opinions.add(o); 
            }
            
            
        }
        
        ReportOntology.BeliefFusion result = new ReportOntology.BeliefFusion();
//        List<String> rl = new ArrayList<>();
        result.ABF = SBoolean.averageBeliefFusion(opinions).toString();
        result.CBF = SBoolean.cumulativeBeliefFusion(opinions).toString();
        result.WBF = SBoolean.weightedBeliefFusion(opinions).toString();
        result.CaCF = SBoolean.consensusAndCompromiseFusion(opinions).toString();
        result.revisors = rs;
//        log.info("bf: " + result.toString());
        switch(operation){
            case "ABF":
//                    System.out.println("\tbFusion result: " + result.ABF);
                    return result.ABF;
            case "CBF":
//                    System.out.println("\tbFusion result: " + result.CBF);
                    return result.CBF;
            case "WBF":
//                    System.out.println("\tbFusion result: " + result.WBF);
                    return result.WBF;
            case "CaCF":
//                   System.out.println("\tbFusion result: " + result.CaCF);
                   return result.CaCF;
            default:
                    return "";
        }
    }
    
    
//    static SBoolean extractOpinionNode(Node n){
//        if (!n.getAllProperties().keySet().contains("opinion"))
//            return null;
//        String params[] = n.getProperty("opinion").toString().split("%");
//        if (params.length!=2)
//            throw new IllegalArgumentException("mapUncertainty: Illegal parameter");
//        Double p = Double.valueOf(params[0]);
//        Double un = Report.mapUncertainty(params[1]);   
//        return SBoolean.binomicalOpinion(p/100.0D, un);
//    }
    
    
    static SBoolean extractOpinionNode(Node n, String rev){        
        if (n.getAllProperties().keySet().contains("opinion")){            
            String opinions[] = n.getProperty("opinion").toString().split("%");
            for (String opinion : opinions) {
                String[] params = opinion.split(":");
                if (params.length!=2)
                    throw new IllegalArgumentException("extractOpinion: Illegal parameter");
//            System.out.println("param_0: " + params[0]);
//            System.out.println("param_1: " + params[1]); 
                if (params[0].equals(rev)){   //Si coincide con la opinion del revisor                 
                    Double un = Report.mapUncertainty(params[1]); 
                    Double be = Report.mapBelief(params[1], un);
                    return new SBoolean(be, 1.0 - be - un, un, 0.5);
                }
            }      
        }
        return null;
    }
    
  
    
    
//    static Double mapUncertainty (String u) {
//        Map<String, Double> mapUn = new HashMap<>();
//        mapUn.put("!", 0.0D);
//        mapUn.put("~",0.2);
//        mapUn.put("?",0.4);
//        mapUn.put("??",0.6);
//        mapUn.put("x",1.0);
////        System.out.println("param: " + u);
//        if (mapUn.get(u)== null)
//            throw new IllegalArgumentException("mapUncertainty: Illegal parameter");
//        return mapUn.get(u);
//    }
//    
//    static Double mapBelief (String b, String u) {
//        Map<String, Double> mapBe = new HashMap<>();
//        
////        System.out.println("b: " + b + "  u: " + u);
//        
//        Double un = Report.mapUncertainty(u);
//        mapBe.put("T",1.0 - un);
//        mapBe.put("X",(1.0 - un) / 2.0);
//        mapBe.put("F",0.0);;
//        if (mapBe.get(b)== null)
//            throw new IllegalArgumentException("mapBelief: Illegal parameter b");
//        
//        return mapBe.get(b);
//    }
    
    static Double mapUncertainty (String o) {
        Map<String, Double> mapUn = new HashMap<>();
        mapUn.put("!", 0.0D);
        mapUn.put("~",0.2);
        mapUn.put("?",0.5);
        mapUn.put("??",0.8);
        mapUn.put("???",1.0);
        String u =o;
//        System.out.println("param: " + u);
        if(o.startsWith("T")|| o.startsWith("X")||o.startsWith("F")) {
            u=o.substring(1);
        }
        if (mapUn.get(u)== null)
            throw new IllegalArgumentException("mapUncertainty: Illegal parameter: " + u);
        return mapUn.get(u);
    }
    
    static Double mapUncertaintyFusion (String o) {
        String aux, u;
        System.out.println("\t\tmapUncertaintyFusion Uncertainty param: " + o);
        if(o.startsWith("SBoolean(")) {
            aux = o.substring("SBoolean(".length(), o.length()-1);
            System.out.println("\t\tmapUncertaintyFusion Uncertainty param2: " + aux);
            String[] params = aux.split(",");
            if (params.length!=8)
                        throw new IllegalArgumentException("mapUncertainty: Illegal parameter:"  + o);
            return Double.parseDouble(params[4] + "." + params[5]);
        }
        else
            throw new IllegalArgumentException("mapUncertainty: Illegal parameter: " + o);
        
    }
    
    static Double mapBeliefFusion (String o) {
        String aux, u;
        System.out.println("\t\tmapUncertaintyFusion Belief param: " + o);
        if(o.startsWith("SBoolean(")) {
            aux = o.substring("SBoolean(".length(), o.length()-1);
            System.out.println("\t\tmapUncertaintyFusion Belief param: " + aux);
            String[] params = aux.split(",");
            if (params.length!=8)
                        throw new IllegalArgumentException("mapUncertainty: Illegal parameter:"  + o);
            return Double.parseDouble(params[0] + "." + params[1]);
        }
        else
            throw new IllegalArgumentException("mapUncertainty: Illegal parameter: " + o);
        
    }
    
    static Double mapBelief (String o, Double un) {
        Map<String, Double> mapBe = new HashMap<>();
        mapBe.put("T",1.0 - un);
        mapBe.put("X",(1.0 - un) / 2.0);
        mapBe.put("F",0.0);
        if (mapBe.get(o.substring(0, 1))== null)
            return 1.0 - un;       
        return mapBe.get(o.substring(0, 1));
    }
    
    static String getQualifiedRelationship(Node n, String property){  
        if (n.getAllProperties().keySet().contains(property)){
            return n.getProperty(property).toString();
        }
        return "";
    }
    
    
    
    /*
     * Métodos de recorrido del grafo
    */
    
    public LinkedHashSet<Relationship> findSomeRelationTypeAndObjectValueCostMinus400(Node n, 
                                    String relationType, 
                                    String nodeLabel, 
                                    LinkedHashSet<Relationship> relation) {
        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
//        n.getRelationships(Direction.OUTGOING).iterator()
         n.getRelationships(Direction.OUTGOING).iterator()
                    .forEachRemaining(rel -> 
                            addRelationsWithObjectValueCost(rlist, rel, relationType, nodeLabel));
//        setNotConsideredIncomingRelation(n);
        
        //Create Object-node and relation if not exists  
        if (rlist.size()< 1){
            log.info("\tCreate node with relationType:" + relationType);
            Node o = tx.createNode(Label.label(nodeLabel));
            o.setProperty("name", "no_name");
            o.setProperty("ns0__ValueCost",Double.toString(1.0));
            Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
            setTypeReportRelation(r, "created");
            rlist.add(r);
        }
        //change the relationship rate according to the sum of costs
        else {
            double sumValueCost = 0.0;
            for (Relationship r : rlist) {
                sumValueCost = sumValueCost + Double.parseDouble(r.getEndNode().getProperty("ns0__ValueCost").toString());
            }
            if (!(sumValueCost < 400.0)) {
                for (Relationship r : rlist) {
                    setNullProbabilityElementAndFactor(r);
//                    r.setProperty("typeReportRelation", "surplus");
                    setTypeReportRelationSurplus(r);
                }
                log.info("\tCreate node with relationType:" + relationType);
                Node o = tx.createNode(Label.label(nodeLabel));
                o.setProperty("name", "no_name");
                Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
                setTypeReportRelation(r, "created");
                o.setProperty("ns0__ValueCost",Double.toString(1.0));
                rlist.add(r);
                //relation.add(r);
            }
        
                
        }   
        return rlist;
    }
    
    public LinkedHashSet<Relationship> findSomeRelationTypeAndObjectValueCostOver400(Node n, 
                                    String relationType, 
                                    String nodeLabel, 
                                    LinkedHashSet<Relationship> relation) {
        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
         n.getRelationships(Direction.OUTGOING).iterator()
                    .forEachRemaining(rel -> 
                            addRelationsWithObjectValueCost(rlist, rel, relationType, nodeLabel));
//        setNotConsideredIncomingRelation(n);
        
        //Create Object-node and relation if not exists  
        if (rlist.size()< 1){
            log.info("\tCreate node with relationType:" + relationType);
            Node o = tx.createNode(Label.label(nodeLabel));
            o.setProperty("name", "no_name");
            Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
            setTypeReportRelation(r, "created");
            o.setProperty("ns0__ValueCost",Double.toString(401.00));
            rlist.add(r);
        }
        else {
            double sumValueCost = 0.0;
            for (Relationship r : rlist) {
                sumValueCost = sumValueCost + Double.parseDouble(r.getEndNode().getProperty("ns0__ValueCost").toString());
            }
            if (!(sumValueCost >= 400.0))  {
                for (Relationship r : rlist) {
                    setNullProbabilityElementAndFactor(r);
//                    r.setProperty("typeReportRelation", "surplus");
                    setTypeReportRelationSurplus(r);
                }
                log.info("\tCreate node with relationType:" + relationType);
                Node o = tx.createNode(Label.label(nodeLabel));
                o.setProperty("name", "no_name");
                Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
                setTypeReportRelation(r, "created");
                o.setProperty("ns0__ValueCost",Double.toString(401.00));
                rlist.add(r);
                //relation.add(r);
            }
        
                
        }   
        return rlist;
    }
    
    
    public LinkedHashSet<Relationship> findSomeRelationTypeAndObjectValueCostOver(Node n, 
                                    String relationType, 
                                    String nodeLabel, 
                                    LinkedHashSet<Relationship> relation,
                                    double quantity) {
        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
         n.getRelationships(Direction.OUTGOING).iterator()
                    .forEachRemaining(rel -> 
                            addRelationsWithObjectValueCost(rlist, rel, relationType, nodeLabel));
//        setNotConsideredIncomingRelation(n);
        
        //Create Object-node and relation if not exists  
        if (rlist.size()< 1){
            //log.info("\tCreate node with relationType:" + relationType);
            Node o = tx.createNode(Label.label(nodeLabel));
            o.setProperty("name", "no_name");
            Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
            setTypeReportRelation(r, "created");
            o.setProperty("ns0__ValueCost",Double.toString(quantity + 1.0));
            rlist.add(r);
        }
        else {
            double sumValueCost = 0.0;
            for (Relationship r : rlist) {
                sumValueCost = sumValueCost + Double.parseDouble(r.getEndNode().getProperty("ns0__ValueCost").toString());
            }
            if (!(sumValueCost >= quantity))  {
                for (Relationship r : rlist) {
                    setNullProbabilityElementAndFactor(r);
//                    r.setProperty("typeReportRelation", "surplus");
                    setTypeReportRelationSurplus(r);
                }
                //log.info("\tCreate node with relationType:" + relationType);
                Node o = tx.createNode(Label.label(nodeLabel));
                o.setProperty("name", "no_name");
                Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
                setTypeReportRelation(r, "created");
                o.setProperty("ns0__ValueCost",Double.toString(quantity + 1.0));
                rlist.add(r);
                //relation.add(r);
            }
        
                
        }   
        return rlist;
    }
    
//    public LinkedHashSet<Relationship> checkSomeRelationTypeAndObject(Node n, 
//                                    String relationType, 
//                                    String nodeLabel, 
//                                    LinkedHashSet<Relationship> relation) {
//        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
//        n.getRelationships(Direction.OUTGOING).iterator()
//                    .forEachRemaining(rel -> 
//                            addRelations(rlist, rel, relationType, nodeLabel));
//        
//        
//        setNotConsideredIncomingRelation(n);
//        
//        //Create Object-node and relation if not exists  
//        if (rlist.size()< 1){
//            log.info("\tCreate node with relationType:" + relationType);
//                Node o = tx.createNode(Label.label(nodeLabel));
//                o.setProperty("name", "no_name");                
//                Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
//                setTypeReportRelation(r, "created");
//                rlist.add(r);
//        }
//        log.info("\tcheckSomeRelation " + relationType + ": " + rlist.size());
//        return rlist;
//    }
    
    
    public LinkedHashSet<Relationship> checkSomeRelationTypeAndObject(Node n, 
                                    String relationType, 
                                    String nodeLabel, 
                                    LinkedHashSet<Relationship> relation,
                                    double relationNumber) {
        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
        n.getRelationships(Direction.OUTGOING).iterator()
                    .forEachRemaining(rel -> 
                            addRelations(rlist, rel, relationType, nodeLabel, relationNumber));
        /***
         * Deleted after the inclusion of the new laws and new allocation mechanism for surpluses and 
         * not considered in August 2024.
         */
//        setNotConsideredIncomingRelation(n);
        
        //Create Object-node and relation if not exists  
        if (rlist.size()< 1){
            log.info("\tCreate node with relationType:" + relationType);
                Node o = tx.createNode(Label.label(nodeLabel));
                o.setProperty("name", "no_name");                
                Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
                setTypeReportRelation(r, "created");
                rlist.add(r);
        }
        log.info("\tcheckSomeRelation " + relationType + ": " + rlist.size());
        return rlist;
    }
    
    
//    public LinkedHashSet<Relationship> checkSomeRelationTypeAndObjectWithoutCreation(Node n, 
//                                    String relationType, 
//                                    String nodeLabel, 
//                                    LinkedHashSet<Relationship> relation,
//                                    double factor) {
//        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
//        n.getRelationships(Direction.OUTGOING).iterator()
//                    .forEachRemaining(rel -> 
//                            addRelations(rlist, rel, relationType, nodeLabel, factor));
////        setNotConsideredIncomingRelation(n);
//        return rlist;
//    }
    
    public LinkedHashSet<Relationship> findSomeRelationTypeAndObject(Node n, 
                                    String relationType, 
                                    String nodeLabel, 
                                    LinkedHashSet<Relationship> relation) {
        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
        n.getRelationships(Direction.OUTGOING).iterator()
                    .forEachRemaining(rel -> 
                            addRelationsSimple(rlist, rel, relationType, nodeLabel));
        
//        //Create Object-node and relation if not exists  
//        if (rlist.size()< 1){
//            //log.info("\tCreate node with relationType:" + relationType);
//                Node o = tx.createNode(Label.label(nodeLabel));
//                o.setProperty("name", "no_name");                
//                Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
//                setTypeReportRelation(r, "created");
//                rlist.add(r);
//                //relation.add(r);
//        }
        return rlist;
    }
    
//    public LinkedHashSet<Relationship> checkNotSomeRelationTypeAndObject(Node n, 
//                                    String relationType, 
//                                    String nodeLabel, 
//                                    LinkedHashSet<Relationship> relation) {
//        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
//        n.getRelationships(Direction.OUTGOING).iterator()
//                    .forEachRemaining(rel -> 
//                            addNegationRelations(rlist, rel, relationType, nodeLabel));
//        /***
//         * Deleted after the inclusion of the new laws and new allocation mechanism for surpluses and 
//         * not considered in August 2024.
//         */
////        setNotConsideredIncomingRelation(n);
//        return rlist;
//    }
    
    
     public LinkedHashSet<Relationship> checkNotSomeRelationTypeAndObject(Node n, 
                                    String relationType, 
                                    String nodeLabel, 
                                    LinkedHashSet<Relationship> relation,
                                    double relationNumber) {
        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
        n.getRelationships(Direction.OUTGOING).iterator()
                    .forEachRemaining(rel -> 
                            addNegationRelations(rlist, rel, relationType, nodeLabel, relationNumber));
        return rlist;
    }
    
    
//    public LinkedHashSet<Relationship> checkOrRelationTypeAndListObject(Node n, 
//                                    String relationType, 
//                                    List<String> listLabel,  
//                                    LinkedHashSet<Relationship> relation) {
//        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
//        n.getRelationships(Direction.OUTGOING).iterator()
//                    .forEachRemaining(rel -> {
//                                addRelationsListLabel(rlist, rel, relationType, listLabel);
//                            });
//        /***
//         * Deleted after the inclusion of the new laws and new allocation mechanism for surpluses and 
//         * not considered in August 2024.
//         */
////        setNotConsideredIncomingRelation(n);
//        //Create Object-node and relation if not exists  
//        if (rlist.size()< 1){
//            //log.info("\tCreate node with relationType:" + relationType);
//            Node o = tx.createNode(Label.label(listLabel.get(0)));
//            o.setProperty("name", "no_name");
//            Relationship r = n.createRelationshipTo(o, RelationshipType.withName(relationType));
//            setTypeReportRelation(r, "created_or");
//            rlist.add(r);
//            //relation.add(r);
//        }   
//        return rlist;
//    }
    
//    public LinkedHashSet<Relationship> checkOrRelationTypeAndListObjectWithoutCreate(Node n, 
//                                    String relationType, 
//                                    List<String> listLabel,  
//                                    LinkedHashSet<Relationship> relation) {
//        LinkedHashSet<Relationship> rlist = new LinkedHashSet<>();
//        n.getRelationships(Direction.OUTGOING).iterator()
//                    .forEachRemaining(rel -> {
//                                addRelationsListLabel(rlist, rel, relationType, listLabel);
//                            });
        /**
         * *
         * Deleted after the inclusion of the new laws and new allocation
         * mechanism for surpluses and not considered in August 2024.
         */
////        setNotConsideredIncomingRelation(n); 
//        return rlist;
//    }
    
    
    
  
    private  void addRelationsSimple(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, String nodeLabel){
        if (relationship.getType().name().startsWith(relationType))
            relationship.getEndNode().getLabels().forEach(label-> addRelationsByObjectSimple(list, relationship, label, relationType, nodeLabel));             
        
    }
    
    
//    private  void addRelations(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, String nodeLabel){
//        if (relationship.getType().name().startsWith(relationType))
//            relationship.getEndNode().getLabels().forEach(label-> addRelationsByObject(list, relationship, label, relationType, nodeLabel));             
//        else
//           setTypeReportRelation(relationship, "surplus"); 
//    }
    
    private  void addRelations(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, 
                                String nodeLabel, double relationNumber){
        if (relationship.getType().name().startsWith(relationType)){
            double relNumber = 0;
            if (relationship.getEndNode().getAllProperties().keySet().contains("factor")) 
                relNumber = Double.parseDouble(relationship.getEndNode().getProperty("factor").toString());
            if (relationNumber == relNumber) {
                setTypeReportRelationSurplus(relationship);   
            }
            relationship.getEndNode().getLabels().forEach(label-> addRelationsByObject(list, relationship, label, relationType, nodeLabel));
        }
    }
    
    private  void addRelationsWithObjectValueCost(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, String nodeLabel){
        if (relationship.getType().name().startsWith(relationType))
            relationship.getEndNode().getLabels().forEach(label-> addRelationsByObjectValueCost(list, relationship, label, relationType, nodeLabel));             
        else
           setTypeReportRelation(relationship, "surplus"); 
    }
    
  
    
//    private  void addNegationRelations(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, String nodeLabel){
//        if (relationship.getType().name().startsWith(relationType))
//            relationship.getEndNode().getLabels().forEach(label-> addNegationRelationsByObject(list, relationship, label, relationType, nodeLabel));             
//        else
//           setTypeReportRelation(relationship, "surplus"); 
//    }
    
    private  void addNegationRelations(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, 
                                String nodeLabel, double relationNumber){
        if (relationship.getType().name().startsWith(relationType)){
            double relNumber = 0;
            if (relationship.getEndNode().getAllProperties().keySet().contains("factor")) 
                relNumber = Double.parseDouble(relationship.getEndNode().getProperty("factor").toString());
            if (relationNumber == relNumber) {
                setTypeReportRelationSurplus(relationship);   
            }
            relationship.getEndNode().getLabels().forEach(label-> addNegationRelationsByObject(list, relationship, label, relationType, nodeLabel));
        }
    }
    
    
    
    private  void addRelationsListLabel(LinkedHashSet<Relationship> list, Relationship relationship, String relationType, List<String> listLabel){
        if (relationship.getType().name().startsWith(relationType))
            relationship.getEndNode().getLabels().forEach(label->
                        addRelationsByListObject(list, relationship, label, relationType, listLabel)); 
        else 
            setTypeReportRelation(relationship, "surplus");
    }

    private  void addRelationsByObjectValueCost(LinkedHashSet<Relationship> list,  Relationship relationship, Label label, String relationType, String nodeLabel) {
        //log.info( "addRelationsByObject: "+ relationType);
        if (label.name().equals(nodeLabel) 
                && relationship.getEndNode().getAllProperties().keySet().contains("ns0__ValueCost")){
            list.add(relationship);
            setTypeReportRelation(relationship, "necessary");
        } else
            setTypeReportRelation(relationship, "surplus");
    }  

      
    private  void addRelationsByObject(LinkedHashSet<Relationship> list,  Relationship relationship, Label label, String relationType, String nodeLabel) {
        //log.info( "\t\t\taddRelationsByObject: "+ relationType + " - label.name(): " + label.name() + " - nodeLabel: " + nodeLabel);
        if (label.name().equals(nodeLabel)){
            list.add(relationship);
            setTypeReportRelation(relationship, "necessary");
        }
            //setTypeReportRelation(relationship, "surplus");
        log.info( "\t\t\taddRelationsByObject: "+ relationType + " - label.name(): " + label.name() 
                + " - nodeLabel: " + nodeLabel + " -relationship.getProperty(\"typeReportRelation\"): " + relationship.getProperty("typeReportRelation"));
    }
    
     private  void addRelationsByObjectSimple(LinkedHashSet<Relationship> list,  Relationship relationship, Label label, String relationType, String nodeLabel) {
        //log.info( "addRelationsByObject: "+ relationType);        
        if (label.name().equals(nodeLabel))
            list.add(relationship);            
    }
    
    private  void addNegationRelationsByObject(LinkedHashSet<Relationship> list,  Relationship relationship, Label label, String relationType, String nodeLabel) {
        //log.info( "addNegationRelationsByObject: "+ relationType);
        if (label.name().equals(nodeLabel)){
            list.add(relationship);
//            setTypeReportRelation(relationship, "negation");
            setTypeReportRelation(relationship, "surplus");
        } 
//        else
//            setTypeReportRelation(relationship, "surplus");
    }
    
    private void setIncomingTypeReportRelation(Relationship r, String type) {   
//        //log.info("typeReportRelation: " +  r.getAllProperties().keySet().contains("typeReportRelation"));
        if (!r.getAllProperties().keySet().contains("typeReportRelation") || 
                "".equals(r.getProperty("typeReportRelation").toString()) ||
                "surplus".equals(r.getProperty("typeReportRelation").toString()))
            r.setProperty("typeReportRelation", type);        
    }
    
    void setTypeReportRelation(Relationship r, String type) {   
//        log.info("typeReportRelation: " +  r.getAllProperties().keySet().contains("typeReportRelation"));
        if (!r.getAllProperties().keySet().contains("typeReportRelation") || ("".equals(r.getProperty("typeReportRelation").toString())))
            r.setProperty("typeReportRelation", type);
        else if ("surplus".equals(r.getProperty("typeReportRelation").toString())) 
            r.setProperty("typeReportRelation", type);
//        log.info("typeReportRelation: " +  r.getProperty("typeReportRelation").toString());
    }
    
    void setTypeReportRelationSurplus(Relationship r) {   
//        log.info("typeReportRelation: " +  r.getAllProperties().keySet().contains("typeReportRelation"));
        r.setProperty("typeReportRelation", "surplus");        
//        log.info("typeReportRelation: " +  r.getProperty("typeReportRelation").toString());
    }
    
    //TheftPerpretator - StolenGoods -
    void setQualifiedRelationship(Node n, Relationship r, String property){  
        if (r.getAllProperties().keySet().contains("typeReportRelation")){
            //log.info("Qualified property of " + r.getType().name() + ": " + r.getProperty("typeReportRelation").toString());
            switch (r.getProperty("typeReportRelation").toString()){
                case "created":
                    n.setProperty(property, "Unknown");
                    break;
                case "necessary":
                    n.setProperty(property, "Known");
                    break;
            }
        }         
    }
    
    
    private  void addRelationsByListObject(LinkedHashSet<Relationship> list,  Relationship relationship, Label label, String relationType, List<String> listLabel) {
        
        listLabel.forEach(nodeLabel -> {
            if (label.name().equals(nodeLabel)){
                list.add(relationship);
                setTypeReportRelation(relationship, "necessary_or");
            } else
                setTypeReportRelation(relationship, "surplus");
        }); 
    }
    
    public void setProbabilityElementAndFactor (LinkedHashSet<Relationship> relations, 
            int probabilityElement, 
            double factor){
        for (Relationship r : relations){
            r.setProperty("probabilty_element", String.valueOf(probabilityElement));
            r.setProperty("factor", String.valueOf(factor));
            log.info("\tProb from " +r.getStartNode().getProperty("name").toString() + " --- " + r.getType().name() + ": " + factor
                        + " ---> " + r.getEndNode().getProperty("name").toString());
        }
            
    }
    
    public void setProbabilityElementFactorAndTypeRel (LinkedHashSet<Relationship> relations, 
            int probabilityElement, 
            double factor,
            String typeRel){
        for (Relationship r : relations){
            r.setProperty("probabilty_element", String.valueOf(probabilityElement));
            r.setProperty("factor", String.valueOf(factor));
            log.info("\tProb from " +r.getStartNode().getProperty("name").toString() + " --- " + r.getType().name() + ": " + factor
                        + " ---> " + r.getEndNode().getProperty("name").toString());
            setTypeReportRelation(r, typeRel);
        }
            
    }
    

    
    public void setNullProbabilityElementAndFactor (Relationship r){
        r.setProperty("probabilty_element", "");
        r.setProperty("factor", "");
    
    }
    
    public double getFactor (Relationship r) {
        double result;
        if (!r.getAllProperties().keySet().contains("factor") || ("".equals(r.getProperty("factor").toString())))
            result = 1.0;
        else
            result = Double.parseDouble(r.getProperty("factor").toString());
        log.info("\tInitialFactor: " + result);
        return result;
    }
    
/*****
     * 
     * Functions assignSurplusRelationInGraph, setNotConsideredIncomingRelation and setNotConsideredRelation deprecated
     * applying nre neccesary, surplus an not considered relations based on properties files.
     * These have been replaced by assignNotconsideredAndSurplusRelationInGraph
     * @param n
     * @param law
     */
    
//    public void assignSurplusRelationInGraph(Node n) {
//        n.getRelationships().iterator()
//                    .forEachRemaining(rel -> { 
//                        if (!rel.getAllProperties().keySet().contains("typeReportRelation")) {
//                            if (!rel.getType().name().equals("ns0__isEmployed"))
//                                setTypeReportRelation(rel, "surplus");
//                            else
//                                setTypeReportRelation(rel, "not_considered");
//                            assignSurplusRelationInGraph(rel.getEndNode());
//                         }
//                    });
//    } 
    
//    public void setNotConsideredIncomingRelation(Node n) {
//        n.getRelationships(Direction.INCOMING).iterator()
//                    .forEachRemaining(rel -> { 
//                        setIncomingTypeReportRelation(rel, "not_considered");
//                    });
//    }
    
//    public void setNotConsideredRelation(Node n, String relationType) {
//        n.getRelationships(Direction.OUTGOING).iterator()
//                    .forEachRemaining(rel -> { 
//                        if (rel.getType().toString().equals(relationType)) {
//                            setIncomingTypeReportRelation(rel, "not_considered");
//                        }
//                    });
//    }
    
    public void assignNotConsideredAndSurplusRelationInGraph(Node n, String law) {
        log.info("assignNotConsideredAndSurplusRelationInGraph::getRelationships(): " + n.getRelationships(Direction.OUTGOING).toString());
        log.info("lawprop: " + lawProp.get(law).keySet().toString());
        n.getRelationships().iterator()
                    .forEachRemaining(rel -> { 
                        if (!rel.getAllProperties().keySet().contains("typeReportRelation")) {
                            log.info("Contiene " + rel.getType().name().replace(appProp.getProperty("objectRoot"), "") + "?: " +
                                    lawProp.get(law).containsKey(rel.getType().name().replace(appProp.getProperty("objectRoot"), "")));
                            if ( lawProp.get(law).containsKey(rel.getType().name().replace(appProp.getProperty("objectRoot"), ""))){
                                setTypeReportRelation(rel, "surplus");
                                log.info(rel.getType().name() + " --> surplus");
                            } else{
                                setTypeReportRelation(rel, "not_considered");
                                log.info(rel.getType().name() + " --> not_considered");
                            }
                            assignNotConsideredAndSurplusRelationInGraph(rel.getEndNode(), law);
                         }
                    });
    }
    
     public void resetTypeReportRelationAndProperties(Node n) {
        //ResetProperties
        n.getRelationships().iterator()
            .forEachRemaining(rel -> { 
                    if (rel.getAllProperties().keySet().contains("typeReportRelation")) {
                        if(!rel.getProperty("typeReportRelation").equals("created")){
                            rel.removeProperty("typeReportRelation");
                            if (rel.hasProperty("probabilty_element"))
                                rel.removeProperty("probabilty_element");
                            if (rel.hasProperty("factor"))
                                rel.removeProperty("factor");
                            log.info(rel.getType().name() + " --> Reset");
                            resetTypeReportRelationAndProperties(rel.getEndNode());
                        }else{
                            rel.getEndNode().delete();
                        }
                    }
        });
    }
     
     
    static public  SBoolean subjetiveProbability(
            List<Relationship> relations, 
            String reviewer
            )
    {

        int surplus = 0, creation = 0;      
        List<SBoolean> neccesaryOpinions = new ArrayList<>();
        List<SBoolean> createdOpinions = new ArrayList<>();
        List<SBoolean> createdTrueOpinions = new ArrayList<>();
        List<SBoolean> surplusOpinions = new ArrayList<>();  
        Map<Integer,SBoolean> opinions = new HashMap<>();
             
        if (relations == null ) throw new IllegalArgumentException("Nodes is Null");
        System.out.println("\t\tsubjetiveProbability relation size: " + relations.size() );
        for (Relationship r : relations){
            System.out.println("\t\tsubjetiveProbability relation: " + r.getType().toString() + ":" + r.getProperty("typeReportRelation").toString());
            if (!r.getAllProperties().keySet().contains("typeReportRelation") || 
                    ("".equals(r.getProperty("typeReportRelation").toString()))){                
                throw new IllegalArgumentException("error: Subgraph relations without typeReportRelation");
            }  
//            System.out.println("\t\tsubjetiveProbability relation: " + r.getProperty("typeReportRelation").toString());
            if ("necessary".equals(r.getProperty("typeReportRelation").toString())){ 
                // System.out.println("subjetiveProbability opinion necessary: " + r.getProperty("opinion").toString());
                int probabilityElement = Integer.parseInt(r.getProperty("probabilty_element").toString());
                SBoolean o = Report.extractOpinion(r, reviewer)!=null ?
                        Report.extractOpinion(r, reviewer):
                        SBoolean.createDogmaticOpinion(1.0D, 0.5D);              
                opinions.computeIfPresent(probabilityElement, (key, val) -> 
                    {
                        val.coProduct(o);
                        return val;
                    });
                opinions.computeIfAbsent(probabilityElement, key -> o);
            }
            if ("surplus".equals(r.getProperty("typeReportRelation").toString())){
                //System.out.println("subjetiveProbability opinion surplus: " + r.getProperty("opinion").toString());
                surplus++; 
                SBoolean o = Report.extractOpinion(r, reviewer)!=null ?
                        Report.extractOpinion(r, reviewer) :
                        SBoolean.createDogmaticOpinion(0.0D, 0.5D);              
                surplusOpinions.add(o);
            }
            if ("created".equals(r.getProperty("typeReportRelation").toString())){
                creation++;  
                createdOpinions.add(SBoolean.createDogmaticOpinion(0.0D, 0.5D));
                createdTrueOpinions.add(SBoolean.createDogmaticOpinion(1.0D, 0.5D));
            }
        } 
        
        // Generate the list of neccesary opinions  
        opinions.forEach((k,v) -> {
            neccesaryOpinions.add(v);         
        });

        // Calculate WeightedUnion of necessary and created opinions
        List<SBoolean> ncreatedOpinions = new ArrayList<>();
        ncreatedOpinions.addAll(neccesaryOpinions);
        ncreatedOpinions.addAll(createdOpinions);
//        System.out.println("neccesaryOpinions: " + neccesaryOpinions.toString());
//        System.out.println("createdOpinions: " + createdOpinions.toString());
        SBoolean wuNecessaryCreated = SBoolean.weightedUnion(ncreatedOpinions);
        
        // Calculate WeightedUnion of necessary, !created and surplus opinions
        List<SBoolean> nTrueCreatedsurplusOpinions = new ArrayList<>();
        nTrueCreatedsurplusOpinions.addAll(neccesaryOpinions);
        nTrueCreatedsurplusOpinions.addAll(createdTrueOpinions);
        nTrueCreatedsurplusOpinions.addAll(surplusOpinions);
//        System.out.println("surplusOpinions: " + surplusOpinions.toString());
        SBoolean wuNecessaryTrueCreatedSurplus = SBoolean.weightedUnion(nTrueCreatedsurplusOpinions);
      
        // Product WeightedUnion elements
        SBoolean resultOpinion = wuNecessaryTrueCreatedSurplus.and(wuNecessaryCreated);
//        log.info(article + "-" + reviewer + ":: " + "resultOpinion: " + resultOpinion.toString()  + "  " 
//            + resultOpinion.projection() + "  " + resultOpinion.uncertainty());       
        return resultOpinion;
    }
    
    
    static public  List<String> extractReviewers(
            List<Relationship> relations
            )
    {      
        if (relations == null ) throw new IllegalArgumentException("Nodes is Null");
        
        Set<String> revisorsSet = new HashSet<>();
        List<String> revisors = new ArrayList<>();
        
        for (Relationship r : relations){
            revisorsSet.addAll(extractReviewersRelation(r));
            revisorsSet.addAll(extractReviewersNode(r.getStartNode()));
            revisorsSet.addAll(extractReviewersNode(r.getEndNode()));
        } 
        
        revisors.addAll(revisorsSet);
        return revisors;
    }
    
    static public  Set<String> extractReviewersRelation(
            Relationship r
            )
    {           
        HashSet<String> revisorsSet = new HashSet<>(); 
//        System.out.println("extractReviewersRelation Relation: " + r.getType().toString());
        if (r.getAllProperties().keySet().contains("opinion")){ 
//           System.out.println("\tOpinion: " + r.getProperty("opinion").toString());
           String opinions[] = r.getProperty("opinion").toString().split("%");
            for (String opinion : opinions) {
                String[] params = opinion.split(":");
                if (params.length!=2)
                    throw new IllegalArgumentException("extractOpinion: Illegal parameter");
                revisorsSet.add(params[0]);
//                System.out.println("Reviewer: " + params[0]);
            } 
        }             
        return revisorsSet;
    }
    
    static public  Set<String> extractReviewersNode(
            Node n
            )
    {           
        HashSet<String> revisorsSet = new HashSet<>();
        if (n.getAllProperties().keySet().contains("opinion")){ 
           String opinions[] = n.getProperty("opinion").toString().split("%");
            for (String opinion : opinions) {
                String[] params = opinion.split(":");
                if (params.length!=2)
                    throw new IllegalArgumentException("extractOpinion: Illegal parameter");
                revisorsSet.add(params[0]);
            } 
        }             
        return revisorsSet;
    }
       
    
     
} // class Report

    